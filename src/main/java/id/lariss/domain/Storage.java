package id.lariss.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Storage.
 */
@Table("storage")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("attribute_label")
    private String attributeLabel;

    @Column("attribute_name")
    private String attributeName;

    @Column("option_label")
    private String optionLabel;

    @Column("option_value")
    private String optionValue;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "color", "processor", "memory", "storage", "products" }, allowSetters = true)
    private Set<Variant> variants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Storage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttributeLabel() {
        return this.attributeLabel;
    }

    public Storage attributeLabel(String attributeLabel) {
        this.setAttributeLabel(attributeLabel);
        return this;
    }

    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public Storage attributeName(String attributeName) {
        this.setAttributeName(attributeName);
        return this;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getOptionLabel() {
        return this.optionLabel;
    }

    public Storage optionLabel(String optionLabel) {
        this.setOptionLabel(optionLabel);
        return this;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public String getOptionValue() {
        return this.optionValue;
    }

    public Storage optionValue(String optionValue) {
        this.setOptionValue(optionValue);
        return this;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Set<Variant> getVariants() {
        return this.variants;
    }

    public void setVariants(Set<Variant> variants) {
        if (this.variants != null) {
            this.variants.forEach(i -> i.setStorage(null));
        }
        if (variants != null) {
            variants.forEach(i -> i.setStorage(this));
        }
        this.variants = variants;
    }

    public Storage variants(Set<Variant> variants) {
        this.setVariants(variants);
        return this;
    }

    public Storage addVariant(Variant variant) {
        this.variants.add(variant);
        variant.setStorage(this);
        return this;
    }

    public Storage removeVariant(Variant variant) {
        this.variants.remove(variant);
        variant.setStorage(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Storage)) {
            return false;
        }
        return getId() != null && getId().equals(((Storage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Storage{" +
            "id=" + getId() +
            ", attributeLabel='" + getAttributeLabel() + "'" +
            ", attributeName='" + getAttributeName() + "'" +
            ", optionLabel='" + getOptionLabel() + "'" +
            ", optionValue='" + getOptionValue() + "'" +
            "}";
    }
}
