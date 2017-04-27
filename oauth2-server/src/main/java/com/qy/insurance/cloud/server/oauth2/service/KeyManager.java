//package com.qy.insurance.cloud.server.oauth2.service;
//
//import com.qy.insurance.cloud.server.oauth2.config.AuthorizationServerConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.crypto.KeyGenerator;
//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//
///**
// * @task:
// * @discrption:
// * @author: Aere
// * @date: 2016/11/10 10:09
// * @version: 1.0.0
// */
//@Service
//@Slf4j
//public class KeyManager implements KeyManagementService {
//
//    @Autowired
//    private JwtAccessTokenConverter converter;
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Override
//    public String getValidKey(String clientId, String type) {
//        return null;
//    }
//
//    @Override
//    public String getTokenSignKey() {
//        try {
//            KeyGenerator hmacSHA256 = KeyGenerator.getInstance("HmacSHA256");
//            hmacSHA256.init(512);
//            byte[] encoded = hmacSHA256.generateKey().getEncoded();
//            String key = new String(Base64.getUrlEncoder().encode(encoded), "UTF-8");
//            return key.substring(0, key.indexOf('='));
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            log.error("Invalid Key Gen!", e);
//        }
//        return null;
//    }
//
//    @Override
//    //@Scheduled(cron = "0 30 3 * * ?") //每天凌晨3点半
//    @Transactional
//    public void refreshTokenSignKey() {
//        String signKey = getTokenSignKey();
//        if (signKey != null) {
//            String sqlUpdate = "UPDATE t_sys_property_resource SET property_value ='?' WHERE property_name='?'";
//            int i = jdbcTemplate.update(sqlUpdate, signKey, AuthorizationServerConfig.AUTH_SIGN_KEY_NAME);
//            if (i != 0) {
//                converter.setSigningKey(signKey);
//            }
//        }
//    }
//}
