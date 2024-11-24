package id.lariss.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Memory.
 */
@Table("memory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Memory implements Serializable {

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

    public Memory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttributeLabel() {
        return this.attributeLabel;
    }

    public Memory attributeLabel(String attributeLabel) {
        this.setAttributeLabel(attributeLabel);
        return this;
    }

    public void setAttributeLabel(String attributeLabel) {
        this.attributeLabel = attributeLabel;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public Memory attributeName(String attributeName) {
        this.setAttributeName(attributeName);
        return this;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getOptionLabel() {
        return this.optionLabel;
    }

    public Memory optionLabel(String optionLabel) {
        this.setOptionLabel(optionLabel);
        return this;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public String getOptionValue() {
        return this.optionValue;
    }

    public Memory optionValue(String optionValue) {
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
            this.variants.forEach(i -> i.setMemory(null));
        }
        if (variants != null) {
            variants.forEach(i -> i.setMemory(this));
        }
        this.variants = variants;
    }

    public Memory variants(Set<Variant> variants) {
        this.setVariants(variants);
        return this;
    }

    public Memory addVariant(Variant variant) {
        this.variants.add(variant);
        variant.setMemory(this);
        return this;
    }

    public Memory removeVariant(Variant variant) {
        this.variants.remove(variant);
        variant.setMemory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Memory)) {
            return false;
        }
        return getId() != null && getId().equals(((Memory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Memory{" +
            "id=" + getId() +
            ", attributeLabel='" + getAttributeLabel() + "'" +
            ", attributeName='" + getAttributeName() + "'" +
            ", optionLabel='" + getOptionLabel() + "'" +
            ", optionValue='" + getOptionValue() + "'" +
            "}";
    }
}
