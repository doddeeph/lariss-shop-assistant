package id.lariss.service;

import id.lariss.service.dto.WebhookRequestForm;
import java.util.Map;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Flux;

public interface TwilioService {
    Flux<String> webhook(ServerHttpRequest request, Map<String, String> requestHeader, WebhookRequestForm reqForm);

    Flux<String> webhook2(String accountSid, String body);
}
