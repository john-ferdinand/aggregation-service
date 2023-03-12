package com.example.aggregationserviceexercise.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class AggregatedResponse {

	@JsonUnwrapped
	private ShpimentResponse.Shipment shipment;

	@JsonUnwrapped
	private TrackingStatusResponse.Tracking tracking;

	@JsonUnwrapped()
	private PricingPerCountryResponse.Pricing pricing;

	public static AggregatedResponse create(final ShpimentResponse ship, final TrackingStatusResponse track,
			final PricingPerCountryResponse price) {
		return new AggregatedResponse(ship, track, price);
	}

	public AggregatedResponse(ShpimentResponse ship, TrackingStatusResponse track, PricingPerCountryResponse price) {
		this.pricing = price.getPricing();
		this.shipment = ship.getShipment();
		this.tracking = track.getTracking();
	}
}
