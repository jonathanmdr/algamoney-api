package com.algaworks.algamoney.api.token;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.catalina.util.ParameterMap;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Profile("oauth-security")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenPreProcessorFilter implements Filter {

    public static final String URI_TOKEN = "/oauth/token";
    public static final String TOKEN_VALUE = "refresh_token";
    public static final String TOKEN_KEY = "grant_type";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        if (URI_TOKEN.equalsIgnoreCase(req.getRequestURI())
                && TOKEN_VALUE.equals(req.getParameter(TOKEN_KEY))
                && req.getCookies() != null) {

            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    String refreshToken = cookie.getValue();
                    req = new MyServletRequestWrapper(req, refreshToken);
                }
            }
        }

        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {

    }

    static class MyServletRequestWrapper extends HttpServletRequestWrapper {

        private String refreshToken;

        public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
            super(request);
            this.refreshToken = refreshToken;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
            map.put(TOKEN_VALUE, new String[]{refreshToken});
            map.setLocked(true);
            return map;
        }
    }
}
