package id.lariss.service;

import reactor.core.publisher.Flux;

public interface OpenAiService {
    Flux<String> chat(String chatId, String chatMsg);
}
