package id.lariss.service.impl;

import id.lariss.service.FlowiseAiService;
import id.lariss.service.dto.flowiseai.FlowiseAiRequest;
import id.lariss.service.dto.flowiseai.FlowiseAiResponse;
import io.netty.handler.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Service
public class FlowiseAiServiceImpl implements FlowiseAiService {

    private static final Logger LOG = LoggerFactory.getLogger(FlowiseAiServiceImpl.class);

    private final WebClient webClient;

    @Value("${flowiseai.api.prediction.id}")
    private String predictionId;

    public FlowiseAiServiceImpl(WebClient.Builder builder) {
        this.webClient = builder
            .baseUrl("http://117.53.144.144:3000")
            .clientConnector(
                new ReactorClientHttpConnector(
                    HttpClient.create().wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                )
            )
            .build();
    }

    @Override
    public Mono<String> prediction(String question) {
        return Mono.defer(() -> {
            LOG.debug("Flowise AI prediction -> question: {}, predictionId: {}", question, predictionId);
            return webClient
                .post()
                .uri("/api/v1/prediction/" + predictionId)
                .bodyValue(buildFlowiseAiRequest(question))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                    response.bodyToMono(String.class).flatMap(s -> Mono.error(new RuntimeException("Client error " + s)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                    response.bodyToMono(String.class).flatMap(s -> Mono.error(new RuntimeException("Server error " + s)))
                )
                .bodyToMono(FlowiseAiResponse.class)
                .map(FlowiseAiResponse::getText)
                .doOnError(WebClientResponseException.class, e -> LOG.error("Error occurred: {}", e.getResponseBodyAsString()));
        });
    }

    private FlowiseAiRequest buildFlowiseAiRequest(String question) {
        return new FlowiseAiRequest(question);
    }
}
