package id.lariss.service.dto.flowiseai;

import java.util.Objects;

public class ToolInput {

    private String input;

    public ToolInput() {}

    public ToolInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ToolInput toolInput = (ToolInput) o;
        return Objects.equals(input, toolInput.input);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(input);
    }

    @Override
    public String toString() {
        return "ToolInput{" + "input='" + input + '\'' + '}';
    }
}
