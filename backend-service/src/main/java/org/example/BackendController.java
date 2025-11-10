package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api")
public class BackendController {

    private final Random random = new Random();

    // Tweak to simulate slow + failing behavior
    private int delayChancePct = 30;   // % of slow calls
    private int failChancePct  = 20;   // % of errors
    private int delayMinMs = 100;
    private int delayMaxMs = 2500;

    @GetMapping("/data")
    public String getData() throws InterruptedException {
        int roll = random.nextInt(100);

        if (roll < delayChancePct) {
            int delay = delayMinMs + random.nextInt(delayMaxMs - delayMinMs + 1);
            Thread.sleep(delay);
        }
        if (roll >= (100 - failChancePct)) {
            throw new BackendCrashedException("Simulated backend crash");
        }
        return "Backend OK @" + System.currentTimeMillis();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    static class BackendCrashedException extends RuntimeException {
        BackendCrashedException(String msg) { super(msg); }
    }
}
