package com.qy.insurance.cloud.client.security.demo.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/3/24 15:46
 * @version: 1.0.0
 */
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    private static final String sqlQueryAuthority = "SELECT authority FROM oauth_role_authority WHERE role=?";

    private final JdbcTemplate jdbcTemplate;

    public CustomUserAuthenticationConverter(JdbcTemplate jdbcTemplate){
        Assert.notNull(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        Object roles = map.get("authorities");
        Object principal = map.get("client_id");
        if (roles != null && roles instanceof List) {
            List<String> r = (List) roles;
            List<String> authorities = r.stream()
                    .map(role -> jdbcTemplate.queryForList(sqlQueryAuthority, new Object[]{role}, String.class))
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(principal, "N/A", AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString(authorities)));

        }
        return null;
    }
}
