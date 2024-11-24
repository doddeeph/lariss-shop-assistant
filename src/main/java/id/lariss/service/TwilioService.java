package id.lariss.service;

import id.lariss.service.dto.twilio.WebhookRequestForm;
import java.util.Map;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface TwilioService {
    Mono<String> webhook(ServerHttpRequest request, Map<String, String> requestHeader, WebhookRequestForm reqForm);
}
