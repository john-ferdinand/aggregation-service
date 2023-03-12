package com.example.aggregationserviceexercise.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.util.Pair;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.example.aggregationserviceexercise.model.PricingPerCountryResponse;
import com.example.aggregationserviceexercise.model.ShpimentResponse;
import com.example.aggregationserviceexercise.model.TrackingStatusResponse;

import io.netty.channel.ChannelOption;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class ApplicationWebClient {

	private final WebClient client;

	private static final String BASE_URL = "http://localhost:4000";

	@Value("${service.timeout:5}")
	private int timeoutServiceLevelAgreement;

	public ApplicationWebClient(WebClient.Builder builder) {
		HttpClient httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutServiceLevelAgreement)
				.responseTimeout(Duration.ofSeconds(timeoutServiceLevelAgreement));
		DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(BASE_URL);
		ReactorClientHttpConnector clientHttpConnector = new ReactorClientHttpConnector(httpClient);
		this.client = builder.uriBuilderFactory(uriBuilderFactory).clientConnector(clientHttpConnector).build();
	}

	public Mono<TrackingStatusResponse> getTrack(List<String> trackOrderNumbers) {
		if (trackOrderNumbers == null) {
			Mono<TrackingStatusResponse> emptyResponse = Mono.just(new TrackingStatusResponse());
			return emptyResponse;
		}

		final String apiUrl = "/track-status";
		final String queryParam = "orderNumber";

		ParameterizedTypeReference<String> expectedType = new ParameterizedTypeReference<String>() {
		};

		Mono<TrackingStatusResponse> response = Flux.fromIterable(trackOrderNumbers)
				.flatMap(orderNo -> this.client.get()
						.uri(uriBuilder -> uriBuilder.path(apiUrl).queryParam(queryParam, orderNo).build()).retrieve()
						.bodyToMono(expectedType)
						.map(trackingStatus -> Pair.of(orderNo, trackingStatus.replaceAll("\"", ""))))
				.collectMap(Pair::getFirst, Pair::getSecond).map(TrackingStatusResponse.Tracking::new)
				.map(TrackingStatusResponse::new)
				.onErrorReturn(new TrackingStatusResponse());

		return response;
	}

	public Mono<ShpimentResponse> getShipments(List<String> shipmentOrderNumbers) {
		if (shipmentOrderNumbers == null) {
			Mono<ShpimentResponse> emptyResponse = Mono.just(new ShpimentResponse());
			return emptyResponse;
		}
		final String apiUrl = "/shipment-products";
		final String queryParam = "orderNumber";

		ParameterizedTypeReference<List<String>> expectedType = new ParameterizedTypeReference<List<String>>() {
		};

		Mono<ShpimentResponse> response = Flux.fromIterable(shipmentOrderNumbers)
				.flatMap(orderNo -> this.client.get()
						.uri(uriBuilder -> uriBuilder.path(apiUrl).queryParam(queryParam, orderNo).build()).retrieve()
						.bodyToMono(expectedType).map(product -> Pair.of(orderNo, product)))
				.collectMap(Pair::getFirst, Pair::getSecond).map(ShpimentResponse.Shipment::new)
				.map(ShpimentResponse::new)				
				.onErrorReturn(new ShpimentResponse());
		return response;
	}

	public Mono<PricingPerCountryResponse> getPricing(List<String> countryCodes) {
		if (countryCodes == null) {
			Mono<PricingPerCountryResponse> emptyMono = Mono.just(new PricingPerCountryResponse());
			return emptyMono;
		}
		final String apiUrl = "/pricing";
		final String queryParam = "countryCode";
		Mono<PricingPerCountryResponse> response = Flux.fromIterable(countryCodes)
				.flatMap(country -> this.client.get()
						.uri(uriBuilder -> uriBuilder.path(apiUrl).queryParam(queryParam, country).build()).retrieve()
						.bodyToMono(String.class).map(price -> Pair.of(country, price)))
				.collectMap(Pair::getFirst, Pair::getSecond).map(PricingPerCountryResponse.Pricing::new)
				.map(PricingPerCountryResponse::new)
				.onErrorReturn(new PricingPerCountryResponse());

		return response;
	}

}
