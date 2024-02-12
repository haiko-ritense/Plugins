package com.ritense.valtimo.amsterdam.emailapi.plugin;

import com.auth0.jwt.JWT;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JWTUtils {

    public static boolean isExpired (String token) {
        if(StringUtils.hasText(token)) {
            Date expires = JWT.decode(token).getExpiresAt();
            return JWT.decode(token).getExpiresAt().before(new Date());
        }
        return true;
    }
}
