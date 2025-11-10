package org.example;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BackendClient {

    private final RestTemplate restTemplate;

    public BackendClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Order matters: Retry will happen inside the CircuitBreaker call chain.
     * If many attempts fail/are slow, CB will open and subsequent calls short-circuit fast.
     */
    @CircuitBreaker(name = "backendCb", fallbackMethod = "fallback")
    @Retry(name = "backendRetry", fallbackMethod = "fallback")
    public String fetchData() {
        String baseUrl = System.getenv().getOrDefault("BACKEND_BASE_URL", "http://localhost:8080");
        return restTemplate.getForObject(baseUrl + "/api/data", String.class);
    }

    // Fallback — receives the same args as the original method + Throwable at the end
    private String fallback(Throwable t) {
        return "Fallback: " + t.getClass().getSimpleName() + " (circuit open or retries exhausted)";
    }
}
