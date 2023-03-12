package com.example.aggregationserviceexercise.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingPerCountryResponse {

	@JsonUnwrapped
	private Pricing pricing;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Pricing {
		private Map<String,String> pricing;
	}
}
