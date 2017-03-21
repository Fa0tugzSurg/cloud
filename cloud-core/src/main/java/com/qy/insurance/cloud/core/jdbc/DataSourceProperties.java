package com.qy.insurance.cloud.core.jdbc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/3/17 14:51
 * @version: 1.0.0
 */
@Data
@Slf4j
public class DataSourceProperties {
    private String type;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Map<String, String> properties = new HashMap<>();

    public DataSourceProperties setUrl(String url) {
        this.properties.put("url", url);
        this.url = url;
        return this;
    }

    public DataSourceProperties setDriverClassName(String driverClassName) {
        this.properties.put("driverClassName", driverClassName);
        this.driverClassName = driverClassName;
        return this;
    }

    public DataSourceProperties setUsername(String username) {
        this.properties.put("username", username);
        this.username = username;
        return this;
    }

    public DataSourceProperties setPassword(String password) {
        this.properties.put("password", password);
        this.password = password;
        return this;
    }

    @SuppressWarnings("unchecked")
    public DataSource generateDatasource() {
        try {
            if (this.type != null) {
                Class<? extends DataSource> tClass = (Class<? extends DataSource>) ClassUtils.forName(type, null);
                DataSource result = BeanUtils.instantiate(tClass);
                MutablePropertyValues propertyValues = new MutablePropertyValues(properties);
                new RelaxedDataBinder(result).withAlias("url", "jdbcUrl")
                        .withAlias("username", "user").bind(propertyValues);
                return result;
            } else {
                throw new IllegalArgumentException("The type of dataSource can't be null!");
            }
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }
}
