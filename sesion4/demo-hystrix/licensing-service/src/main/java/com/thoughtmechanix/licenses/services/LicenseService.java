package com.thoughtmechanix.licenses.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.thoughtmechanix.licenses.clients.OrganizationDiscoveryClient;
import com.thoughtmechanix.licenses.clients.OrganizationRestTemplateClient;
import com.thoughtmechanix.licenses.clients.OrganizationFeignClient;
import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;


    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;
    
    
    @Autowired
    OrganizationRestTemplateClient organizationRestClient;
    
    @Autowired
    OrganizationFeignClient organizationFeignClient;


    private Organization retrieveOrgInfo(String organizationId){
        Organization organization = organizationRestClient.getOrganization(organizationId);
        return organization;
    }

    //6
    @HystrixCommand(commandProperties = {
            @HystrixProperty(
                    name = "execution.isolation.thread.timeoutInMilliseconds", 
                    value = "12000") })
    public License getLicense(String organizationId,String licenseId) {
        randomlyRunLong();
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization org = retrieveOrgInfo(organizationId);

        return license
                .withOrganizationName( org.getName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() )
                .withComment(config.getExampleProperty());
    }	
    
    //2
    private void randomlyRunLong(){
      Random rand = new Random();

      int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        //4
      if (randomNum==3) sleep();
    }

    //3
    private void sleep(){
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //1
    @HystrixCommand
    //7
    //@HystrixCommand(fallbackMethod = "buildFallbackLicenseList")
    //@HystrixCommand(threadPoolKey = "licenseByOrgThreadPool")
    public List<License> getLicensesByOrg(String organizationId){
        //5
        randomlyRunLong();
        return licenseRepository.findByOrganizationId( organizationId );
    }
    //8
    private List<License> buildFallbackLicenseList(String organizationId){
        List<License> fallbackList = new ArrayList<>();
        License license = new License()
                .withId("0000000-00-00000")
                .withOrganizationId( organizationId )
                .withProductName("Sorry no licensing information currently available");

        fallbackList.add(license);
        return fallbackList;
    }
         

    public void saveLicense(License license){
        license.withId( UUID.randomUUID().toString());

        licenseRepository.save(license);

    }

    public void updateLicense(License license){
      licenseRepository.save(license);
    }

    public void deleteLicense(License license){
        licenseRepository.delete( license.getLicenseId());
    }

}
