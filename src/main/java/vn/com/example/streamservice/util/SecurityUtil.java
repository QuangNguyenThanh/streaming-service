package vn.com.example.streamservice.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import vn.com.example.streamservice.exception.UnauthorizedException;

@Component
public class SecurityUtil {

    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    public static Long getCurrentUserLogin() {
        HttpServletRequest currentRequestContext = getRequest();
        if (currentRequestContext == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        return (Long) currentRequestContext.getAttribute("X-User-Id");
    }
}
