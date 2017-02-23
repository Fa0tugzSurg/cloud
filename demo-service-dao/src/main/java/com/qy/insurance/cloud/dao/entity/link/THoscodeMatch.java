package com.qy.insurance.cloud.dao.entity.link;

import javax.persistence.*;

@Table(name = "t_hoscode_match")
public class THoscodeMatch {
    /**
     * 保险公司code
     */
    @Column(name = "client_code")
    private String clientCode;

    /**
     * 保险对应医院id
     */
    @Column(name = "client_hos_id")
    private String clientHosId;

    /**
     * 趣医医院id
     */
    @Column(name = "qy_hos_id")
    private Integer qyHosId;

    /**
     * 获取保险公司code
     *
     * @return client_code - 保险公司code
     */
    public String getClientCode() {
        return clientCode;
    }

    /**
     * 设置保险公司code
     *
     * @param clientCode 保险公司code
     */
    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    /**
     * 获取保险对应医院id
     *
     * @return client_hos_id - 保险对应医院id
     */
    public String getClientHosId() {
        return clientHosId;
    }

    /**
     * 设置保险对应医院id
     *
     * @param clientHosId 保险对应医院id
     */
    public void setClientHosId(String clientHosId) {
        this.clientHosId = clientHosId;
    }

    /**
     * 获取趣医医院id
     *
     * @return qy_hos_id - 趣医医院id
     */
    public Integer getQyHosId() {
        return qyHosId;
    }

    /**
     * 设置趣医医院id
     *
     * @param qyHosId 趣医医院id
     */
    public void setQyHosId(Integer qyHosId) {
        this.qyHosId = qyHosId;
    }
}