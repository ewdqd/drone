package com.houduan.drone.Utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.houduan.drone.Common.CustomClaim;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class JwtUtils {

    private static String secret;

    private static String issuer;

    private static long age;

    public static Algorithm algorithm;

    @Value("${jwt.issuer: amov}")
    private void setIssuer(String issuer) {
        JwtUtils.issuer = issuer;
    }

    @Value("${jwt.secret: dikonjingjiyanjiuyuan}")
    private void setSecret(String secret) {
        JwtUtils.secret = secret;
        setAlgorithm();
    }

    private void setAlgorithm() {
        JwtUtils.algorithm = Algorithm.HMAC256(secret);

    }
    @Value("${jwt.age: 86400}")
    private void setAge(long age){
        JwtUtils.age = age * 1000;
    }

    private JwtUtils() {
    }

    /**
     * 根据 userId + role 生成 JWT（不再传入密码）
     */
    public static String createToken(Integer userId, Integer role) {
        Map<String, String> claims = new LinkedHashMap<>();
        claims.put("userId", String.valueOf(userId));
        claims.put("role", String.valueOf(role));
        return JwtUtils.createToken(claims, age, algorithm);
    }

    /**
     * 根据 userId + role + username 生成 JWT（用于 token 刷新场景）
     */
    public static String createToken(Integer userId, Integer role, String username) {
        Map<String, String> claims = new LinkedHashMap<>();
        claims.put("userId", String.valueOf(userId));
        claims.put("role", String.valueOf(role));
        claims.put("username", username);
        return JwtUtils.createToken(claims, age, algorithm);
    }

    //根据用户的信息生成token
    public static String createToken(Map<String, ?> map) {
        return JwtUtils.createToken(map, age, algorithm);
    }


    //生成token
    public static String createToken(Map<String,?> claims, long age, Algorithm algorithm) {
        if (Objects.isNull(algorithm)) {
            throw new IllegalArgumentException();
        }

        Date now = new Date();
        Date expiry = new Date(now.getTime() + age);
        JWTCreator.Builder builder = JWT.create();
        claims.forEach((k, v) -> {
            if (Objects.nonNull(v.getClass().getClassLoader())) {
                log.error("claim can't be set to a custom object.");
                return;
            }
            if (v instanceof Map) {
                builder.withClaim(k, (Map) v);
            } else if (v instanceof List) {
                builder.withClaim(k, (List) v);
            } else {
                builder.withClaim(k, String.valueOf(v));
            }

        });
        if (Objects.nonNull(age)) {
            builder.withExpiresAt(new Date(now.getTime() + age));
        }

        String token = builder
                .withIssuedAt(now)
                .withNotBefore(now)
               .withExpiresAt(expiry)
                .sign(algorithm);
        return token;
    }

    //验证token
    public static DecodedJWT verifyToken(String token) {
        return JWT.require(algorithm).build().verify(token);
    }


    public static Optional<CustomClaim> parseToken(String token) {
        DecodedJWT jwt;
        try {
            jwt = verifyToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(new CustomClaim(jwt.getClaims()));
    }
}
