package com.houduan.drone.news.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.houduan.drone.Common.ApiResult;
import com.houduan.drone.Common.Result;
import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Context.LocalThreadHolder;
import com.houduan.drone.Dto.NewsQueryDto;
import com.houduan.drone.Entity.News;
import com.houduan.drone.news.Mapper.NewsMapper;
import com.houduan.drone.news.Service.NewsService;
import com.houduan.drone.Pojo.em.RoleEnum;
import com.houduan.drone.Pojo.vo.NewsVO;
import com.houduan.drone.Utils.AssertUtils;
import com.houduan.drone.Utils.RedisOpsUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Slf4j
@Service
@Transactional
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    private final static Boolean IS_SHOW = true; //展示
    private final static Boolean IS_HIDDEN = false; //隐藏位

    private static final String NEWS_CACHE_PREFIX = "news:detail:";
    private static final String NEWS_LOCK_PREFIX = "lock:news:";
    private static final int CACHE_EXPIRE_BASE = 1800;
    private static final Random RANDOM = new Random();

    @Autowired
    private NewsMapper newsMapper;
    private void validParam(News news) {
        AssertUtils.notNull(news, "新闻不能为空");
        AssertUtils.hasText(news.getTitle(), "新闻标题不能为空");
//        AssertUtils.hasText(news.getCover(), "封面必须上传");
        AssertUtils.hasText(news.getSummary(), "摘要必须上传");
//        AssertUtils.notNull(news.getIsShow(), "新闻展示状态不为空");
        AssertUtils.isTrue(news.getTitle().length()<100, "标题请控制在100字以内");
        AssertUtils.isTrue(news.getSummary().length()<300, "摘要请控制在300字以内");
    }

    /**
     * 启动预热：将前 5 条展示新闻加载到 Redis 中
     */
    @PostConstruct
    public void preloadCache() {
        try {
            Page<News> page = newsMapper.selectPage(new Page<>(1, 5), null);
            List<News> records = page.getRecords();
            for (News news : records) {
                String key = NEWS_CACHE_PREFIX + news.getId();
                NewsVO vo = new NewsVO();
                BeanUtils.copyProperties(news, vo);
                RedisOpsUtils.setWithExpire(key, vo, CACHE_EXPIRE_BASE + RANDOM.nextInt(300));
            }
            log.info("新闻缓存预热完成，加载了 {} 条新闻", records.size());
        } catch (Exception e) {
            log.warn("新闻缓存预热失败（Redis 可能未启动）: {}", e.getMessage());
        }
    }

    @Override
    public Result<String> saveEntity(News news) {
        validParam(news);
//        news.setIsShow(IS_SHOW);
        news.setCreateTime(LocalDateTime.now()); //设置发布时间
        //管理员才有权限进行新闻的新增
        AssertUtils.validAuth(RoleEnum.ADMIN.getRole());
        //新闻新增操作
        save(news);

        return ApiResult.success();
    }

    private void validUserAuth() {
        AssertUtils.isTrue(
                RoleEnum.ADMIN.getRole().equals(LocalThreadHolder.getRoleId()),
                "无操作权限"
        );
    }

    @Override
    public Result<String> updateEntity(News news) {
        validParam(news);
        AssertUtils.validAuth(RoleEnum.ADMIN.getRole());
        updateById(news);
        // 更新后删除缓存，保证最终一致性
        RedisOpsUtils.del(NEWS_CACHE_PREFIX + news.getId());
        return ApiResult.success();
    }

    @Override
    public Result<String> deleteByNewsId(Integer id) {

        AssertUtils.notNull(id,"ID不能为空");
        AssertUtils.validAuth(RoleEnum.ADMIN.getRole());
        removeById(id);
        // 删除后清除缓存
        RedisOpsUtils.del(NEWS_CACHE_PREFIX + id);
        return ApiResult.success();
    }

    /**
     * 缓存旁路读新闻详情 —— 防穿透、防雪崩、防击穿
     */
    public Result<NewsVO> getByNewsIdWithCache(Integer id) {
        AssertUtils.notNull(id, "ID不能为空");
        String cacheKey = NEWS_CACHE_PREFIX + id;

        // 1. 先查 Redis 缓存
        Object cached = RedisOpsUtils.get(cacheKey);
        if (cached != null) {
            // 穿透防护：如果是空对象标记，直接返回 null
            if (cached instanceof String && "null".equals(cached)) {
                return ApiResult.success("新闻查询成功");
            }
            return ApiResult.success("新闻查询成功", (NewsVO) cached);
        }

        // 2. 缓存未命中 —— 使用分布式锁防击穿
        String lockKey = NEWS_LOCK_PREFIX + id;
        boolean locked = false;
        try {
            locked = RedisOpsUtils.setNX(lockKey, "1", 10);
            if (locked) {
                // 获取锁成功，查 MySQL
                News news = getById(id);
                NewsVO newsVO = null;

                if (news != null) {
                    newsVO = new NewsVO();
                    BeanUtils.copyProperties(news, newsVO);
                    // 回填缓存，过期时间加随机值防雪崩
                    RedisOpsUtils.setWithExpire(cacheKey, newsVO, CACHE_EXPIRE_BASE + RANDOM.nextInt(300));
                } else {
                    // 穿透防护：缓存空对象 60 秒，防止恶意查询不存在的 ID
                    RedisOpsUtils.setWithExpire(cacheKey, "null", 60);
                }
                return ApiResult.success("新闻查询成功", newsVO);
            } else {
                // 未获取锁，等待后重试读缓存
                Thread.sleep(100);
                Object retryCached = RedisOpsUtils.get(cacheKey);
                if (retryCached instanceof String && "null".equals(retryCached)) {
                    return ApiResult.success("新闻查询成功");
                }
                if (retryCached != null) {
                    return ApiResult.success("新闻查询成功", (NewsVO) retryCached);
                }
                // 最后兜底查库
                News news = getById(id);
                if (news == null) {
                    return ApiResult.success("新闻查询成功");
                }
                NewsVO newsVO = new NewsVO();
                BeanUtils.copyProperties(news, newsVO);
                return ApiResult.success("新闻查询成功", newsVO);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 兜底查库
            News news = getById(id);
            if (news == null) {
                return ApiResult.success("新闻查询成功");
            }
            NewsVO newsVO = new NewsVO();
            BeanUtils.copyProperties(news, newsVO);
            return ApiResult.success("新闻查询成功", newsVO);
        } finally {
            if (locked) {
                RedisOpsUtils.del(lockKey);
            }
        }
    }

    @Override
    public Result<NewsVO> getByNewsId(Integer id) {
        // 委托给缓存旁路方法
        return getByNewsIdWithCache(id);
    }

    @Override
    public HttpResultResponse list(NewsQueryDto newsQueryDto) {
//        System.out.println("1111111111" + newsQueryDto.getCurrent());
        AssertUtils.notNull(newsQueryDto.getCurrent(),"当前页面不能为空");
//        AssertUtils.notNull(newsQueryDto,"查询条件不能为空");
//        System.out.println("111111111111111");
        Page<News> newsPage = newsMapper.selectPage(new Page<>(newsQueryDto.getCurrent(), newsQueryDto.getSize()), null);
        List<News> records = newsPage.getRecords();

        long count = newsPage.getTotal();
        Map<String,Object> map = new HashMap<>();
        map.put("count",count);
        map.put("news",records);
        return HttpResultResponse.success(map);
    }

    @Override
    public Result<String> setShowStatus(News news) {
        AssertUtils.notNull(news,"新闻数据不能为空");
        AssertUtils.validAuth(RoleEnum.ADMIN.getRole());
        updateById(news);
        return ApiResult.success();
    }
}
