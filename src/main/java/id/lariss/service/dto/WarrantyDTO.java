package id.lariss.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link id.lariss.domain.Warranty} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarrantyDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String warrantyEn;

    private String warrantyId;

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

    public String getWarrantyEn() {
        return warrantyEn;
    }

    public void setWarrantyEn(String warrantyEn) {
        this.warrantyEn = warrantyEn;
    }

    public String getWarrantyId() {
        return warrantyId;
    }

    public void setWarrantyId(String warrantyId) {
        this.warrantyId = warrantyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarrantyDTO)) {
            return false;
        }

        WarrantyDTO warrantyDTO = (WarrantyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, warrantyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarrantyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", warrantyEn='" + getWarrantyEn() + "'" +
            ", warrantyId='" + getWarrantyId() + "'" +
            "}";
    }
}
