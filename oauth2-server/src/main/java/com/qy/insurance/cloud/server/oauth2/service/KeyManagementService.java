package com.qy.insurance.cloud.server.oauth2.service;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2016/11/9 14:15
 * @version: 1.0.0
 */
public interface KeyManagementService {

    /**
     * Get valid key for decrypt data in web request
     * @param clientId clientId/username of request user
     * @param type encryption type
     * @return The string key of any valid; null if not found
     */
    String getValidKey(String clientId, String type);

    /**
     * Get the sign key for verify Token, which should be exactly the same between Auth Server and Resource Server
     * @return The string key
     */
    String getTokenSignKey();

    /**
     * Refresh the verify Token to enhance security.
     * It should only be executed in Auth Server, never in Resource Server.
     */
    void refreshTokenSignKey();
}
