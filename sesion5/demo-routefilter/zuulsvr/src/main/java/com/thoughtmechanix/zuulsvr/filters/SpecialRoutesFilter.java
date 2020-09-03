package com.thoughtmechanix.zuulsvr.filters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.pre.PreDecorationFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.thoughtmechanix.zuulsvr.model.AbTestingRoute;

@Component
public class SpecialRoutesFilter extends ZuulFilter {
	private static final Logger logger = LoggerFactory.getLogger(SpecialRoutesFilter.class);

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return PreDecorationFilter.FILTER_ORDER + 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Autowired
	FilterUtils filterUtils;

	@Autowired
	RestTemplate restTemplate;

	private AbTestingRoute getAbRoutingInfo(String serviceName) {
		ResponseEntity<AbTestingRoute> restExchange = null;
		try {
			restExchange = restTemplate.exchange("http://specialroutesservice/v1/route/abtesting/{serviceName}",
					org.springframework.http.HttpMethod.GET, null, AbTestingRoute.class, serviceName);
		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
				return null;
			throw ex;
		}
		logger.debug("registro devuelto de specialroutesservice {}", restExchange.getBody());
		return restExchange.getBody();
	}

	public boolean useSpecialRoute(AbTestingRoute testRoute) {
		boolean respuesta = false;
		Random random = new Random();

		if (testRoute.getActive().equals("N"))
			respuesta = false;

		int value = random.nextInt((10 - 1) + 1) + 1;

		if (testRoute.getWeight() < value)
			respuesta = true;

		return respuesta;
	}

	@Override
	public Object run() {

		RequestContext ctx = RequestContext.getCurrentContext();
		// 1.1
		AbTestingRoute abTestRoute = getAbRoutingInfo(filterUtils.getServiceId());
		if (abTestRoute != null) {
			//1.2
			if (useSpecialRoute(abTestRoute)) {
				try {
					//1.3
					logger.debug("redirigiendo a specialroute {}", abTestRoute.getEndpoint());
					ctx.setRouteHost(new URL(abTestRoute.getEndpoint()));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
