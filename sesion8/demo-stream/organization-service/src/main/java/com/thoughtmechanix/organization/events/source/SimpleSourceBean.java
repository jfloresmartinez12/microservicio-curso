package com.thoughtmechanix.organization.events.source;

import com.thoughtmechanix.organization.events.models.OrganizationChangeModel;
import com.thoughtmechanix.organization.utils.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Component
public class SimpleSourceBean {
    private Source source;

    private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

    //1
    @Autowired
    public SimpleSourceBean(Source source){
        this.source = source;
    }
    //2
    public void publishOrgChange(String action,String orgId){
       logger.debug("Sending Kafka message {} for Organization Id: {}", action, orgId);
       //3
        OrganizationChangeModel change =  new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action,
                orgId,
                UserContext.getCorrelationId());
        //4
        source.output().send(MessageBuilder.withPayload(change).build());
    }
}
