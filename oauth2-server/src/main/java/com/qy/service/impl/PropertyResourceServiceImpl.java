package com.qy.service.impl;

import com.qy.mapper.PropertyResourceMapper;
import com.qy.service.PropertyResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leepon on 17/4/28.
 */
@Service
public class PropertyResourceServiceImpl implements PropertyResourceService {

    @Autowired
    PropertyResourceMapper propertyResourceMapper;

    @Override
    public String queryPropertyValueBypropertyName(String propertyName) {
        return propertyResourceMapper.queryPropertyValueBypropertyName(propertyName);
    }
}
