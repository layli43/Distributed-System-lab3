package org.example;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final BackendClient backendClient;

    public ClientController(BackendClient backendClient) {
        this.backendClient = backendClient;
    }

    @GetMapping("/call-backend")
    public String callBackend() {
        return backendClient.fetchData();
    }
}
