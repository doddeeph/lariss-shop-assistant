package id.lariss.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link id.lariss.domain.Feature} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeatureDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String featureEn;

    private String featureId;

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

    public String getFeatureEn() {
        return featureEn;
    }

    public void setFeatureEn(String featureEn) {
        this.featureEn = featureEn;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeatureDTO)) {
            return false;
        }

        FeatureDTO featureDTO = (FeatureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, featureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeatureDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", featureEn='" + getFeatureEn() + "'" +
            ", featureId='" + getFeatureId() + "'" +
            "}";
    }
}
