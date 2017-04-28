package com.qy.insurance.cloud.oauth.mapper;

/**
 * Created by leepon on 17/4/28.
 */
public interface PropertyResourceMapper {

    /**
     * 根据propertyName查propertyValue
     * @param propertyName
     * @return
     */
    String queryPropertyValueBypropertyName(String propertyName);
}
