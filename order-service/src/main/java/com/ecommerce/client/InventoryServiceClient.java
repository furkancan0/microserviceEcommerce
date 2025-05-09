package com.ecommerce.client;

import com.ecommerce.dto.InventoryResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class InventoryServiceClient {

    @Autowired
    @Lazy
    private final RestTemplate restTemplate;

    private String inventoryServiceUrl = "http://inventory-service/api/v1/inventory"; ;

    public InventoryServiceClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @CircuitBreaker(name = "inventory-service")
    public boolean getProductsBySkuCodes(List<String> skuCodes) {
        String url = inventoryServiceUrl + "/by-skus";

        HttpEntity<List<String>> requestEntity = new HttpEntity<>(skuCodes);

        ResponseEntity<InventoryResponse[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                InventoryResponse[].class
        );

        return Arrays.stream(Objects.requireNonNull(response.getBody())).allMatch(InventoryResponse::isInStock);
    }
}
