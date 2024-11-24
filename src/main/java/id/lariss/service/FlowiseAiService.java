package id.lariss.service;

import reactor.core.publisher.Mono;

public interface FlowiseAiService {
    Mono<String> prediction(String question);
}
