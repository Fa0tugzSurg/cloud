package com.qy.insurance.cloud.zuul.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.qy.insurance.cloud.zuul.property.CustomZuulProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/3/21 11:08
 * @version: 1.0.0
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "jwtFilter")
public class JwtFilter extends ZuulFilter {

    private static final long timeout = 300_1000L;

    private static final String sqlSelectKey = "SELECT property_value FROM t_sys_property_resource WHERE property_name='insurance.cloud.auth.key'";

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final AtomicReference<JWTVerifier> verifierReference = new AtomicReference<>();

    private final AtomicLong lastTime = new AtomicLong();

    @Autowired
    private CustomZuulProperties zuulProperties;

    private CustomZuulProperties.ZuulRoute zuulRoute;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initial() {
        zuulRoute = zuulProperties.getRoutes().get("jwt");
    }

    @Override
    public String filterType() {
        //Add following null check due to com.netflix.zuul.ZuulFilter.disablePropertyName
        return zuulRoute == null ? "pre" : zuulRoute.getType();
    }

    @Override
    public int filterOrder() {
        return zuulRoute.getOrder();
    }

    @Override
    public boolean shouldFilter() {
        return zuulRoute.isEnable();
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String uri = request.getRequestURI();
        if (zuulRoute.getPaths().stream().anyMatch(path -> pathMatcher.match(path, uri))) {
            if (zuulRoute.getRoles() == null || zuulRoute.getRoles().isEmpty()) { // Role in config is empty
                generateUnauthResponse(context);
                log.warn("Reject to forward due to no roles is configured for {}", zuulRoute.getPaths());
                return null;
            }
            String token = extractHeaderToken(request);
            try {
                long now = System.currentTimeMillis();
                if (lastTime.accumulateAndGet(now, (pre, now2) -> now2 - pre > timeout ? now2 : pre) == now) {
                    String key = jdbcTemplate.queryForObject(sqlSelectKey, String.class);
                    Algorithm algorithm = Algorithm.HMAC256(key);
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    verifierReference.set(jwtVerifier);
                }
                DecodedJWT decodedJWT = verifierReference.get().verify(token);
                List<String> authorities = decodedJWT.getClaim("authorities").asList(String.class);
                if (!authorities.stream().anyMatch(role -> zuulRoute.getRoles().contains(role))) { // Role in token is not in config
                    generateUnauthResponse(context);
                    log.warn("Reject to forward due to roles {} is not authorized for {}", authorities, zuulRoute.getPaths());
                }
            } catch (UnsupportedEncodingException | JWTVerificationException e) {
                log.error("The token is not valid!", e);
                generateUnauthResponse(context);
            }
        }
        return null;
    }

    private void generateUnauthResponse(RequestContext context) {
        context.setSendZuulResponse(false);
        context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        context.addZuulResponseHeader("Content-Type", "application/json;charset=UTF-8");
        context.setResponseBody("{}");
    }

    private String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith("bearer"))) {
                String authHeaderValue = value.substring("bearer".length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }

        return "";
    }
}
