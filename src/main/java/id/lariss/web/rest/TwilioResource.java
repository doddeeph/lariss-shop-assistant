package id.lariss.web.rest;

import id.lariss.service.TwilioService;
import id.lariss.service.dto.WebhookRequestForm;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/public/twilio")
public class TwilioResource {

    private final TwilioService twilioService;

    public TwilioResource(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @PostMapping(path = "/webhook", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Flux<String> webhook(
        ServerHttpRequest request,
        @RequestHeader Map<String, String> requestHeader,
        WebhookRequestForm requestForm
    ) {
        return twilioService.webhook(request, requestHeader, requestForm);
    }

    @PostMapping(path = "/webhook-2")
    public Flux<String> webhook2(String accountSid, String body) {
        return twilioService.webhook2(accountSid, body);
    }
}
