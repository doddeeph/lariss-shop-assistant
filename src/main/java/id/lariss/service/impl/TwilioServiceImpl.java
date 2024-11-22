package id.lariss.service.impl;

import id.lariss.service.OpenAiService;
import id.lariss.service.TwilioService;
import id.lariss.service.dto.WebhookRequestForm;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Log4j2
@Service
public class TwilioServiceImpl implements TwilioService {

    private final OpenAiService openAiService;

    public TwilioServiceImpl(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public Flux<String> webhook(ServerHttpRequest request, Map<String, String> requestHeader, WebhookRequestForm reqForm) {
        return Flux.defer(() -> {
            //WebhookRequestHeader reqHeader = new WebhookRequestHeader(requestHeader);
            return openAiService.chat(reqForm.getAccountSid(), reqForm.getBody());
        });
    }

    @Override
    public Flux<String> webhook2(String accountSid, String body) {
        return openAiService.chat(accountSid, body);
    }
}
