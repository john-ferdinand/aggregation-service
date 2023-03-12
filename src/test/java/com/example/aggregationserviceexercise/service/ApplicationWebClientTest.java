package com.example.aggregationserviceexercise.service;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.aggregationserviceexercise.model.PricingPerCountryResponse;
import com.example.aggregationserviceexercise.model.ShpimentResponse;
import com.example.aggregationserviceexercise.model.TrackingStatusResponse;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApplicationWebClientTest {

	@Autowired
	private ApplicationWebClient appWebClient;

	@Test
	public void testGetTrack() {
		List<String> trackOrderNumbers = Arrays.asList(new String[] { "987654321", "123456789" });
		Mono<TrackingStatusResponse> track = appWebClient.getTrack(trackOrderNumbers);
		System.out.println(track.block());
		Assertions.assertNotNull(track);
	}

	@Test
	public void testGetShipments() {
		List<String> shipmentsOrderNumbers = Arrays.asList(new String[] { "987654321", "123456789" });
		Mono<ShpimentResponse> products = appWebClient.getShipments(shipmentsOrderNumbers);
		System.out.println(products.block());
		Assertions.assertNotNull(products);
	}

	@Test
	public void testGetPricing() {
		List<String> countryCodes = Arrays.asList(new String[] { "NL", "CN" });
		Mono<PricingPerCountryResponse> pricingPerCountry = appWebClient.getPricing(countryCodes);
		System.out.println(pricingPerCountry.block());
		Assertions.assertNotNull(pricingPerCountry);
	}

	@Test
	public void testGetTrackEmptyParams() {

		Mono<TrackingStatusResponse> track = appWebClient.getTrack(null);
		System.out.println(track.block());
		Assertions.assertEquals(new TrackingStatusResponse(), track.block());
	}

}
