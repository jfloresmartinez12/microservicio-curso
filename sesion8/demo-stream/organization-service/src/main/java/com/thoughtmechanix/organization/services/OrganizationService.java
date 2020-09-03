package com.thoughtmechanix.organization.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtmechanix.organization.events.source.SimpleSourceBean;
import com.thoughtmechanix.organization.model.Organization;
import com.thoughtmechanix.organization.repository.OrganizationRepository;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepository;

    
    @Autowired  //1.
    SimpleSourceBean simpleSourceBean;

    public Organization getOrg(String organizationId) {
        return orgRepository.findById(organizationId);
    }

    public void saveOrg(Organization org){
        org.setId( UUID.randomUUID().toString());

        orgRepository.save(org);
        simpleSourceBean.publishOrgChange("SAVE", org.getId()); //2.
    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
        simpleSourceBean.publishOrgChange("UPDATE", org.getId());

    }

    public void deleteOrg(String  orgId){
        orgRepository.delete( orgId );
        simpleSourceBean.publishOrgChange("DELETE", orgId);
    }
}
