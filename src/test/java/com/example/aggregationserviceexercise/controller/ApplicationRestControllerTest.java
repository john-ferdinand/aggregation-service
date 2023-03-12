package com.example.aggregationserviceexercise.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.aggregationserviceexercise.model.AggregatedResponse;
import com.example.aggregationserviceexercise.model.PricingPerCountryResponse;
import com.example.aggregationserviceexercise.model.ShpimentResponse;
import com.example.aggregationserviceexercise.model.TrackingStatusResponse;
import com.example.aggregationserviceexercise.service.AggregatorService;
import com.example.aggregationserviceexercise.service.ApplicationWebClient;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ApplicationRestController.class)
public class ApplicationRestControllerTest {

	@MockBean
	private ApplicationWebClient applicationWebClient;

	@MockBean
	private AggregatorService service;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void testGetOrderDetails() throws Exception {
		ShpimentResponse shpimentResponse = Mockito.mock(ShpimentResponse.class);
		TrackingStatusResponse trackingStatusResponse = Mockito.mock(TrackingStatusResponse.class);
		PricingPerCountryResponse pricingPerCountryResponse = Mockito.mock(PricingPerCountryResponse.class);

		AggregatedResponse aggregatedResponse = AggregatedResponse.create(shpimentResponse, trackingStatusResponse,
				pricingPerCountryResponse);
		when(service.getAggregatedResponseBy(anyList(), anyList(), anyList()))
				.thenReturn(Mono.just(aggregatedResponse));
		webTestClient.get().uri("/aggregation").exchange().expectStatus().isOk();
	}
}
