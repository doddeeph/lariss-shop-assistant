package id.lariss.web.rest;

import id.lariss.service.TwilioService;
import id.lariss.service.dto.twilio.WebhookRequestForm;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/public/twilio")
public class TwilioResource {

    private static final Logger LOG = LoggerFactory.getLogger(TwilioResource.class);

    private final TwilioService twilioService;

    public TwilioResource(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @PostMapping(path = "/webhook", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public Mono<String> webhook(
        ServerHttpRequest request,
        @RequestHeader Map<String, String> requestHeader,
        WebhookRequestForm requestForm
    ) {
        return Mono.defer(() -> {
            LOG.debug("Twilio Webhook incoming request ->  requestHeader: {}, requestForm: {}", requestHeader, requestForm);
            return twilioService.webhook(request, requestHeader, requestForm);
        });
    }
}
