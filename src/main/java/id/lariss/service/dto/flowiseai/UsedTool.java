package id.lariss.service.dto.flowiseai;

import java.util.Objects;

public class UsedTool {

    private String tool;
    private ToolInput toolInput;

    public UsedTool() {}

    public UsedTool(String tool, ToolInput toolInput) {
        this.tool = tool;
        this.toolInput = toolInput;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public ToolInput getToolInput() {
        return toolInput;
    }

    public void setToolInput(ToolInput toolInput) {
        this.toolInput = toolInput;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UsedTool usedTool = (UsedTool) o;
        return Objects.equals(tool, usedTool.tool) && Objects.equals(toolInput, usedTool.toolInput);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tool, toolInput);
    }

    @Override
    public String toString() {
        return "UsedTool{" + "tool='" + tool + '\'' + ", toolInput=" + toolInput + '}';
    }
}
