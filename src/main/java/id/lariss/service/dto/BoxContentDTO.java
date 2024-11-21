package id.lariss.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link id.lariss.domain.BoxContent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BoxContentDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    private String contentEn;

    private String contentId;

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

    public String getContentEn() {
        return contentEn;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BoxContentDTO)) {
            return false;
        }

        BoxContentDTO boxContentDTO = (BoxContentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, boxContentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoxContentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contentEn='" + getContentEn() + "'" +
            ", contentId='" + getContentId() + "'" +
            "}";
    }
}
