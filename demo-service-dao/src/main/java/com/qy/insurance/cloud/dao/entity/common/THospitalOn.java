package com.qy.insurance.cloud.dao.entity.common;

import java.util.Date;
import javax.persistence.*;

@Table(name = "t_hospital_on")
public class THospitalOn {
    @Id
    private Integer id;

    /**
     * 公司编码
     */
    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "hospital_id")
    private Integer hospitalId;

    /**
     * 是否开通：0-未开通 1-已开通
     */
    @Column(name = "open_flag")
    private Boolean openFlag;

    /**
     * 请求开通日期
     */
    @Column(name = "request_open_date")
    private Date requestOpenDate;

    /**
     * 开通日期
     */
    @Column(name = "open_date")
    private Date openDate;

    /**
     * 修改人
     */
    @Column(name = "modify_user")
    private String modifyUser;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取公司编码
     *
     * @return client_id - 公司编码
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * 设置公司编码
     *
     * @param clientId 公司编码
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * @return hospital_id
     */
    public Integer getHospitalId() {
        return hospitalId;
    }

    /**
     * @param hospitalId
     */
    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    /**
     * 获取是否开通：0-未开通 1-已开通
     *
     * @return open_flag - 是否开通：0-未开通 1-已开通
     */
    public Boolean getOpenFlag() {
        return openFlag;
    }

    /**
     * 设置是否开通：0-未开通 1-已开通
     *
     * @param openFlag 是否开通：0-未开通 1-已开通
     */
    public void setOpenFlag(Boolean openFlag) {
        this.openFlag = openFlag;
    }

    /**
     * 获取请求开通日期
     *
     * @return request_open_date - 请求开通日期
     */
    public Date getRequestOpenDate() {
        return requestOpenDate;
    }

    /**
     * 设置请求开通日期
     *
     * @param requestOpenDate 请求开通日期
     */
    public void setRequestOpenDate(Date requestOpenDate) {
        this.requestOpenDate = requestOpenDate;
    }

    /**
     * 获取开通日期
     *
     * @return open_date - 开通日期
     */
    public Date getOpenDate() {
        return openDate;
    }

    /**
     * 设置开通日期
     *
     * @param openDate 开通日期
     */
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    /**
     * 获取修改人
     *
     * @return modify_user - 修改人
     */
    public String getModifyUser() {
        return modifyUser;
    }

    /**
     * 设置修改人
     *
     * @param modifyUser 修改人
     */
    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }
}