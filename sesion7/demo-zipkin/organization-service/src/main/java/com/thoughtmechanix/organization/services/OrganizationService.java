package com.thoughtmechanix.organization.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtmechanix.organization.model.Organization;
import com.thoughtmechanix.organization.repository.OrganizationRepository;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    public Organization getOrg
            (String organizationId) {

        logger.debug("In the organizationService.getOrg() call");
        try {
            return orgRepository.findById(organizationId);
        }
        finally{

        }
    }

    public void saveOrg(Organization org){
        org.setId( UUID.randomUUID().toString());

        orgRepository.save(org);
    }

    public void updateOrg(Organization org){
        orgRepository.save(org);

    }

    public void deleteOrg(String orgId){
        orgRepository.delete( orgId );
    }
}
