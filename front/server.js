const express = require('express');
const axios = require('axios');
const cheerio = require('cheerio');
const app = express();

// 代理接口：获取就业网最新3条新闻
app.get('/api/jiuye-news', async(req, res) => {
    try {
        const targetUrl = 'http://jiuye.xupt.edu.cn/module/news?type_id=18080';
        const { data } = await axios.get(targetUrl);
        const $ = cheerio.load(data);

        // 【关键】调整选择器，匹配目标网站实际新闻列表结构
        const newsList = [];
        // 假设新闻在 class 为 "news_list" 的 ul 中，每个 li 包含新闻项
        $('.news_list li').each((index, elem) => {
            if (index < 3) { // 只取前3条
                const title = $(elem).find('a').text().trim(); // 提取新闻标题
                const date = $(elem).find('.time').text().trim(); // 提取新闻日期
                // 拼接详情页绝对链接（必须确保路径正确）
                const detailUrl = 'http://jiuye.xupt.edu.cn' + $(elem).find('a').attr('href');
                newsList.push({ title, date, detailUrl });
            }
        });

        res.json(newsList); // 返回提取的新闻数据
    } catch (error) {
        res.status(500).json({ error: '获取新闻失败' });
    }
});

app.listen(3000, () => {
    console.log('后端服务启动：http://localhost:3000');
});