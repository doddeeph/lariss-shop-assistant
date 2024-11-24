package id.lariss.service.dto.flowiseai;

import java.util.Objects;

public class FlowiseAiRequest {

    private String question;

    public FlowiseAiRequest() {}

    public FlowiseAiRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlowiseAiRequest that = (FlowiseAiRequest) o;
        return Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(question);
    }

    @Override
    public String toString() {
        return "FlowiseAiRequest{" + "question='" + question + '\'' + '}';
    }
}
