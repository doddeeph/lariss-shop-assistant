package id.lariss.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link id.lariss.domain.Description} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DescriptionDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String descriptionEn;

    private String descriptionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DescriptionDTO)) {
            return false;
        }

        DescriptionDTO descriptionDTO = (DescriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, descriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DescriptionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", descriptionEn='" + getDescriptionEn() + "'" +
            ", descriptionId='" + getDescriptionId() + "'" +
            "}";
    }
}
