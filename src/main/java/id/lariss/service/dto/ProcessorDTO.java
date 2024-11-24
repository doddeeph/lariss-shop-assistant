package id.lariss.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link id.lariss.domain.Processor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcessorDTO implements Serializable {

    private Long id;

    private String attributeLabel;

    private String attributeName;

    private String optionLabel;

    private String optionValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttributeLabel() {
        return attributeLabel;
    }

    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessorDTO)) {
            return false;
        }

        ProcessorDTO processorDTO = (ProcessorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, processorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessorDTO{" +
            "id=" + getId() +
            ", attributeLabel='" + getAttributeLabel() + "'" +
            ", attributeName='" + getAttributeName() + "'" +
            ", optionLabel='" + getOptionLabel() + "'" +
            ", optionValue='" + getOptionValue() + "'" +
            "}";
    }
}
