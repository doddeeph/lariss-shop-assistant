package id.lariss.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Feature.
 */
@Table("feature")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Feature implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("feature_en")
    private String featureEn;

    @Column("feature_id")
    private String featureId;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "category", "description", "feature", "boxContent", "warranty" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Feature id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Feature name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeatureEn() {
        return this.featureEn;
    }

    public Feature featureEn(String featureEn) {
        this.setFeatureEn(featureEn);
        return this;
    }

    public void setFeatureEn(String featureEn) {
        this.featureEn = featureEn;
    }

    public String getFeatureId() {
        return this.featureId;
    }

    public Feature featureId(String featureId) {
        this.setFeatureId(featureId);
        return this;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setFeature(null));
        }
        if (products != null) {
            products.forEach(i -> i.setFeature(this));
        }
        this.products = products;
    }

    public Feature products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Feature addProduct(Product product) {
        this.products.add(product);
        product.setFeature(this);
        return this;
    }

    public Feature removeProduct(Product product) {
        this.products.remove(product);
        product.setFeature(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Feature)) {
            return false;
        }
        return getId() != null && getId().equals(((Feature) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Feature{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", featureEn='" + getFeatureEn() + "'" +
            ", featureId='" + getFeatureId() + "'" +
            "}";
    }
}
