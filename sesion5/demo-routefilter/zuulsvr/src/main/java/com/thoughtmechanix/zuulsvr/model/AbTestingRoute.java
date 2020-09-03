package com.thoughtmechanix.zuulsvr.model;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AbTestingRoute {
    String serviceName;
    String active;
    String endpoint;
    Integer weight;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    
    @Override
    public String toString() {
        // Generate toString including transient and static fields.
        return ReflectionToStringBuilder.toString(this,
            ToStringStyle.MULTI_LINE_STYLE, true, true);
    }

}