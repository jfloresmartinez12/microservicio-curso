package com.thoughtmechanix.licenses.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.thoughtmechanix.licenses.clients.OrganizationDiscoveryClient;
import com.thoughtmechanix.licenses.clients.OrganizationRestTemplateClient;
import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	ServiceConfig config;
//3
	@HystrixCommand
	public License getLicense(String organizationId, String licenseId) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		return license.withComment(config.getExampleProperty());
	}

	public List<License> getLicensesByOrg(String organizationId) {
		return licenseRepository.findByOrganizationId(organizationId);
	}

	public void saveLicense(License license) {
		license.withId(UUID.randomUUID().toString());

		licenseRepository.save(license);

	}

	public void updateLicense(License license) {
		licenseRepository.save(license);
	}

	public void deleteLicense(License license) {
		licenseRepository.delete(license.getLicenseId());
	}

	@Autowired
	OrganizationDiscoveryClient organizationDiscoveryClient;

//1
	@HystrixCommand
	private Organization retrieveOrgInfo(String organizationId, String clientType) {
		Organization organization = null;

		switch (clientType) {
		case "rest":
			System.out.println("I am using the rest client");
//1
			organization = organizationRestClient.getOrganization(organizationId);
			break;

		case "discovery":
			System.out.println("I am using the discovery client");
			organization = organizationDiscoveryClient.getOrganization(organizationId);
			break;
		default:
			organization = organizationRestClient.getOrganization(organizationId);

		}

		return organization;
	}

	// 2
	@Autowired
	OrganizationRestTemplateClient organizationRestClient;

//2
	public License getLicense(String organizationId, String licenseId, String clientType) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
System.out.println(license);
		Organization org = retrieveOrgInfo(organizationId, clientType);
		System.out.println(org);
		return license.withOrganizationName(org.getName()).withContactName(org.getContactName())
				.withContactEmail(org.getContactEmail()).withContactPhone(org.getContactPhone())
				.withComment(config.getExampleProperty());
	}

}
