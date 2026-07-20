package com.houduan.drone.gateway.Config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.houduan.drone.gateway.Config.AuthInterceptor.PARAM_TOKEN;


@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Credentials", "true");
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT,OPTIONS");
        res.addHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers," +
                "Authorization, Content-Length, X-CSRF-Token, Token,session,X_Requested_With,Accept, "+
                "Origin, Host, Connection, Accept-Encoding, Accept-Language,DNT, X-CustomHeader, Keep-Alive," +
                " User-Agent, X-Requested-With, If-Modified-Since, Cache-Control, Content-Type, Pragma," +(PARAM_TOKEN ));
        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
            return;
        }
        filterChain.doFilter(request, response);
    }
}
