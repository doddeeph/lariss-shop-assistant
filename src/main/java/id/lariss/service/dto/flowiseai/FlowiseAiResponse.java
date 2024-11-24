package id.lariss.service.dto.flowiseai;

import java.util.List;
import java.util.Objects;

public class FlowiseAiResponse {

    private String text;
    private List<UsedTool> usedTools;
    private String question;
    private String chatId;
    private String chatMessageId;
    private Boolean isStreamValid;
    private String sessionId;
    private String memoryType;

    public FlowiseAiResponse() {}

    public FlowiseAiResponse(
        String text,
        List<UsedTool> usedTools,
        String question,
        String chatId,
        String chatMessageId,
        Boolean isStreamValid,
        String sessionId,
        String memoryType
    ) {
        this.text = text;
        this.usedTools = usedTools;
        this.question = question;
        this.chatId = chatId;
        this.chatMessageId = chatMessageId;
        this.isStreamValid = isStreamValid;
        this.sessionId = sessionId;
        this.memoryType = memoryType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<UsedTool> getUsedTools() {
        return usedTools;
    }

    public void setUsedTools(List<UsedTool> usedTools) {
        this.usedTools = usedTools;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(String chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public Boolean getStreamValid() {
        return isStreamValid;
    }

    public void setStreamValid(Boolean streamValid) {
        isStreamValid = streamValid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlowiseAiResponse that = (FlowiseAiResponse) o;
        return (
            Objects.equals(text, that.text) &&
            Objects.equals(usedTools, that.usedTools) &&
            Objects.equals(question, that.question) &&
            Objects.equals(chatId, that.chatId) &&
            Objects.equals(chatMessageId, that.chatMessageId) &&
            Objects.equals(isStreamValid, that.isStreamValid) &&
            Objects.equals(sessionId, that.sessionId) &&
            Objects.equals(memoryType, that.memoryType)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, usedTools, question, chatId, chatMessageId, isStreamValid, sessionId, memoryType);
    }

    @Override
    public String toString() {
        return (
            "FlowiseAiResponse{" +
            "text='" +
            text +
            '\'' +
            ", usedTools=" +
            usedTools +
            ", question='" +
            question +
            '\'' +
            ", chatId='" +
            chatId +
            '\'' +
            ", chatMessageId='" +
            chatMessageId +
            '\'' +
            ", isStreamValid=" +
            isStreamValid +
            ", sessionId='" +
            sessionId +
            '\'' +
            ", memoryType='" +
            memoryType +
            '\'' +
            '}'
        );
    }
}
