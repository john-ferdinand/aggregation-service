package com.example.aggregationserviceexercise.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShpimentResponse {

	@JsonUnwrapped
	private Shipment shipment;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class Shipment {
		private Map<String, List<String>> shipment;
	}

}
