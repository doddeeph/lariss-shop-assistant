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
 * A Description.
 */
@Table("description")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Description implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("description_en")
    private String descriptionEn;

    @Column("description_id")
    private String descriptionId;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "category", "description", "feature", "boxContent", "warranty" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Description id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Description name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionEn() {
        return this.descriptionEn;
    }

    public Description descriptionEn(String descriptionEn) {
        this.setDescriptionEn(descriptionEn);
        return this;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionId() {
        return this.descriptionId;
    }

    public Description descriptionId(String descriptionId) {
        this.setDescriptionId(descriptionId);
        return this;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setDescription(null));
        }
        if (products != null) {
            products.forEach(i -> i.setDescription(this));
        }
        this.products = products;
    }

    public Description products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Description addProduct(Product product) {
        this.products.add(product);
        product.setDescription(this);
        return this;
    }

    public Description removeProduct(Product product) {
        this.products.remove(product);
        product.setDescription(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Description)) {
            return false;
        }
        return getId() != null && getId().equals(((Description) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Description{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", descriptionEn='" + getDescriptionEn() + "'" +
            ", descriptionId='" + getDescriptionId() + "'" +
            "}";
    }
}
