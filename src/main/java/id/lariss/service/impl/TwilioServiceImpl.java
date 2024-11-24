package id.lariss.service.impl;

import id.lariss.service.FlowiseAiService;
import id.lariss.service.TwilioService;
import id.lariss.service.dto.twilio.WebhookRequestForm;
import java.util.Map;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TwilioServiceImpl implements TwilioService {

    private final FlowiseAiService flowiseAiService;

    public TwilioServiceImpl(FlowiseAiService flowiseAiService) {
        this.flowiseAiService = flowiseAiService;
    }

    @Override
    public Mono<String> webhook(ServerHttpRequest request, Map<String, String> requestHeader, WebhookRequestForm reqForm) {
        return Mono.defer(() -> {
            //            WebhookRequestHeader reqHeader = new WebhookRequestHeader(requestHeader);
            return flowiseAiService.prediction(reqForm.getBody());
        });
    }
}
