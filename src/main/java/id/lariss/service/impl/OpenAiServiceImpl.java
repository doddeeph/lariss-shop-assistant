package id.lariss.service.impl;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

import id.lariss.advisor.LoggingAdvisor;
import id.lariss.service.OpenAiService;
import java.time.LocalDate;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Log4j2
@Service
public class OpenAiServiceImpl implements OpenAiService {

    private final ChatClient chatClient;

    public OpenAiServiceImpl(ChatClient.Builder modelBuilder, ChatMemory chatMemory) {
        this.chatClient = modelBuilder
            .defaultSystem(
                """
                	You are a customer chat support agent of Reseller of Apple Premium Products in Indonesia named "iBox"."
                	Respond in a friendly, helpful, and joyful manner.
                	You are interacting with customers through an online chat system.
                	Before providing information about a product details, you MUST always
                	get the following information from the user: product name which will be needed as input for get product details.
                	Check the message history for this information before asking the user.
                	Before changing a booking you MUST ensure it is permitted by the terms.
                	If there is a charge for the change, you MUST ask the user to consent before proceeding.
                	Use the provided functions to fetch product details.
                	Use parallel function calling if required.
                	Today is {current_date}.
                """
            )
            .defaultAdvisors(
                new PromptChatMemoryAdvisor(chatMemory), // Chat Memory
                // new VectorStoreChatMemoryAdvisor(vectorStore)),
                new LoggingAdvisor()
            )
            .defaultFunctions("getProductDetails") // FUNCTION CALLING
            .build();
    }

    @Override
    public Flux<String> chat(String chatId, String chatMsg) {
        return this.chatClient.prompt()
            .system(s -> s.param("current_date", LocalDate.now().toString()))
            .user(chatMsg)
            .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
            .stream()
            .content()
            .doOnError(t -> log.error("Error chat -> chatId: {}, chatMsg: {}", chatId, chatMsg));
    }
}
