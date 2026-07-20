package com.houduan.drone.gateway.Config;

import com.houduan.drone.Common.CustomClaim;
import com.houduan.drone.Context.LocalThreadHolder;
import com.houduan.drone.Utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String PARAM_TOKEN = "x-auth-token";
    public static final String TOKEN_CLAIM = "customClaim";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从 Header 中获取 token
        String token = request.getHeader(PARAM_TOKEN);

        if (token == null || token.isEmpty()) {
            log.warn("请求缺少 token: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或 token 缺失\"}");
            return false;
        }

        try {
            // 验签并解析 JWT
            Optional<CustomClaim> claimOpt = JwtUtils.parseToken(token);
            if (claimOpt.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"token 无效或已过期\"}");
                return false;
            }

            CustomClaim claim = claimOpt.get();
            // 将 userId 和 role 存入 ThreadLocal，供后续业务使用
            LocalThreadHolder.setUserId(claim.getUserId(), claim.getRole());
            // 将 claims 存入 request attribute，方便 Controller 获取
            request.setAttribute(TOKEN_CLAIM, claim);

            return true;
        } catch (Exception e) {
            log.error("token 校验异常: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"token 校验失败\"}");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.removeAttribute(TOKEN_CLAIM);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理 ThreadLocal，防止内存泄漏
        LocalThreadHolder.clear();
    }
}
