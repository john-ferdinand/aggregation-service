package com.example.aggregationserviceexercise.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.aggregationserviceexercise.model.AggregatedResponse;
import com.example.aggregationserviceexercise.service.AggregatorService;

import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Mono;

@RestController
public class ApplicationRestController {

	@Autowired
	private AggregatorService service;

	@GetMapping(path = "/aggregation")
	@Operation(summary = "GET api that aggregates the response from 3 external api calls. Shipment API, Track API, and Pricing API")
	public ResponseEntity<Mono<AggregatedResponse>> getOrderDetails(
			@RequestParam(required = false, name = "shipmentsOrderNumbers") List<String> shipmentOrderNos,
			@RequestParam(required = false, name = "trackOrderNumbers") List<String> trackingOrderNos,
			@RequestParam(required = false, name = "pricingCountryCodes") List<String> countryCodes) {

		Mono<AggregatedResponse> aggregatedResponse = service.getAggregatedResponseBy(shipmentOrderNos,
				trackingOrderNos, countryCodes);
		return new ResponseEntity<>(aggregatedResponse, HttpStatus.OK);
	}
}
