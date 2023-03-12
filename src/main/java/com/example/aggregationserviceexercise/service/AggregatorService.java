package com.example.aggregationserviceexercise.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.aggregationserviceexercise.model.AggregatedResponse;
import com.example.aggregationserviceexercise.model.PricingPerCountryResponse;
import com.example.aggregationserviceexercise.model.PricingPerCountryResponse.Pricing;
import com.example.aggregationserviceexercise.model.ShpimentResponse;
import com.example.aggregationserviceexercise.model.ShpimentResponse.Shipment;
import com.example.aggregationserviceexercise.model.TrackingStatusResponse;
import com.example.aggregationserviceexercise.model.TrackingStatusResponse.Tracking;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

@Service
@AllArgsConstructor
public class AggregatorService {

	@Autowired
	private final ApplicationWebClient webClient;

	public Mono<AggregatedResponse> getAggregatedResponseBy(List<String> shipmentOrderNumbers,
			List<String> trackOrderNumbers, List<String> countryCodes) {
		Mono<ShpimentResponse> shipments = this.webClient.getShipments(shipmentOrderNumbers);
		Mono<TrackingStatusResponse> track = this.webClient.getTrack(trackOrderNumbers);
		Mono<PricingPerCountryResponse> pricing = this.webClient.getPricing(countryCodes);

		return Mono.zip(shipments, track, pricing).map(this::combine);
	}

	private AggregatedResponse combine(
			Tuple3<ShpimentResponse, TrackingStatusResponse, PricingPerCountryResponse> tuple) {
		
		Shipment emptyShipment = Shipment.builder().shipment(new HashMap<>()).build();
		Pricing emptyPricing = Pricing.builder().pricing(new HashMap<>()).build();
		Tracking emptyTracking = Tracking.builder().track(new HashMap<>()).build();

		if (tuple.getT1().getShipment() == null)
			tuple.getT1().setShipment(emptyShipment);
		if (tuple.getT2().getTracking() == null)
			tuple.getT2().setTracking(emptyTracking);
		if (tuple.getT3().getPricing() == null)
			tuple.getT3().setPricing(emptyPricing);

		return AggregatedResponse.create(tuple.getT1(), tuple.getT2(), tuple.getT3());

	}

}
