package com.houduan.drone.gateway.Controller;

import com.houduan.drone.Common.CustomClaim;
import com.houduan.drone.Common.Result;
import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.NewsQueryDto;
import com.houduan.drone.Dto.QueryDto;
import com.houduan.drone.Dto.UserLoginDTO;
import com.houduan.drone.Dto.UsersDTO;
import com.houduan.drone.Entity.ActionOperation;
import com.houduan.drone.Entity.News;
import com.houduan.drone.Pojo.em.CommonErrorEnum;
import com.houduan.drone.Pojo.vo.DroneRankVO;
import com.houduan.drone.Pojo.vo.NewsVO;
import com.houduan.drone.Service.rpc.ActionRpcService;
import com.houduan.drone.Service.rpc.NewsRpcService;
import com.houduan.drone.Service.rpc.RankRpcService;
import com.houduan.drone.Service.rpc.UserRpcService;
import com.houduan.drone.Utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.houduan.drone.gateway.Config.AuthInterceptor.PARAM_TOKEN;

/**
 * Unified API Gateway Controller.
 * All HTTP requests arrive here, are authenticated by AuthInterceptor,
 * and forwarded to downstream microservices via Dubbo RPC.
 */
@RestController
public class GatewayController {

    @DubboReference
    private UserRpcService userRpcService;

    @DubboReference
    private NewsRpcService newsRpcService;

    @DubboReference
    private RankRpcService rankRpcService;

    @DubboReference
    private ActionRpcService actionRpcService;

    // ===================== Login / Token =====================

    @PostMapping("${url.manage.prefix}${url.manage.version}/login")
    public HttpResultResponse login(@RequestBody UserLoginDTO loginDTO) {
        HttpResultResponse result = userRpcService.login(
                loginDTO.getUsername(), loginDTO.getPassword());
        if (result.getCode() != null && result.getCode() == HttpStatus.UNAUTHORIZED.value()) {
            return result;
        }
        // Extract user info, create JWT at gateway level
        UsersDTO userData = (UsersDTO) result.getData();
        if (userData == null) {
            return HttpResultResponse.error("invalid username or password");
        }
        Integer role = Integer.valueOf(userData.getUserstatus());
        String token = JwtUtils.createToken(userData.getId(), role, userData.getUsername());
        userData.setAccessToken(token);
        return HttpResultResponse.success(userData);
    }

    @PostMapping("${url.manage.prefix}${url.manage.version}/token/refresh")
    public HttpResultResponse refreshToken(HttpServletRequest request,
                                           HttpServletResponse response) {
        String token = request.getHeader(PARAM_TOKEN);
        Optional<CustomClaim> claimOpt = JwtUtils.parseToken(token);
        if (claimOpt.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return HttpResultResponse.error(CommonErrorEnum.NO_TOKEN.getMessage());
        }
        CustomClaim claim = claimOpt.get();
        HttpResultResponse userResult = userRpcService.getUserById(claim.getUserId());
        UsersDTO user = (UsersDTO) userResult.getData();
        if (user == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return HttpResultResponse.error("user not found");
        }
        String newToken = JwtUtils.createToken(user.getId(), claim.getRole(), user.getUsername());
        user.setAccessToken(newToken);
        return HttpResultResponse.success(user);
    }

    // ===================== User Management =====================

    @PostMapping("/user/updateUser")
    public HttpResultResponse getUserList(@RequestBody QueryDto queryDto) {
        return userRpcService.pageUsers(queryDto);
    }

    @PostMapping("/user/adduser")
    public HttpResultResponse userAdd(@RequestBody UsersDTO usersDTO) {
        return userRpcService.register(usersDTO);
    }

    @PostMapping("/user/userModify")
    public HttpResultResponse userModify(@RequestBody UsersDTO usersDTO) {
        return userRpcService.modify(usersDTO);
    }

    @PostMapping("/user/userDelete")
    public HttpResultResponse userDelete(@RequestBody Map<String, Integer> body) {
        Integer id = body.get("id");
        if (id == null) {
            return HttpResultResponse.error("id is required");
        }
        return userRpcService.delete(id);
    }

    // ===================== News Management =====================

    @PostMapping("/news/saveNewsEntity")
    public Result<String> saveNews(@RequestBody News news) {
        return newsRpcService.saveNews(news);
    }

    @PutMapping("/news/updateNewsEntity")
    public Result<String> updateNews(@RequestBody News news) {
        return newsRpcService.updateNews(news);
    }

    @DeleteMapping("/news/{id}")
    public Result<String> deleteNews(@PathVariable Integer id) {
        return newsRpcService.deleteNews(id);
    }

    @GetMapping("/news/{id}")
    public Result<NewsVO> getNewsById(@PathVariable Integer id) {
        return newsRpcService.getNewsById(id);
    }

    @PostMapping("/news/list")
    public HttpResultResponse listNews(@RequestBody NewsQueryDto queryDto) {
        return newsRpcService.listNews(queryDto);
    }

    @PostMapping("/news/setShowStatus")
    public Result<String> setShowStatus(@RequestBody News news) {
        return newsRpcService.setShowStatus(news);
    }

    // ===================== Drone Ranking =====================

    @PostMapping("${url.manage.prefix}${url.manage.version}/drone/flight/add")
    public HttpResultResponse addFlightTime(@RequestParam String droneId,
                                            @RequestParam long seconds) {
        rankRpcService.addFlightTime(droneId, seconds);
        return HttpResultResponse.success("飞行时长记录成功");
    }

    @GetMapping("${url.manage.prefix}${url.manage.version}/drone/ranking/top")
    public Result<List<DroneRankVO>> getTopRanking(@RequestParam(defaultValue = "10") int n) {
        return rankRpcService.getTopRanking(n);
    }

    // ===================== Action Operation =====================

    @PostMapping("/action-operation/saveActionEntity")
    @ResponseBody
    public Result<String> saveAction(@RequestBody ActionOperation actionOperation) {
        return actionRpcService.saveAction(actionOperation);
    }
}
