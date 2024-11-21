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
 * A Warranty.
 */
@Table("warranty")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Warranty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("warranty_en")
    private String warrantyEn;

    @Column("warranty_id")
    private String warrantyId;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "category", "description", "feature", "boxContent", "warranty" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Warranty id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Warranty name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarrantyEn() {
        return this.warrantyEn;
    }

    public Warranty warrantyEn(String warrantyEn) {
        this.setWarrantyEn(warrantyEn);
        return this;
    }

    public void setWarrantyEn(String warrantyEn) {
        this.warrantyEn = warrantyEn;
    }

    public String getWarrantyId() {
        return this.warrantyId;
    }

    public Warranty warrantyId(String warrantyId) {
        this.setWarrantyId(warrantyId);
        return this;
    }

    public void setWarrantyId(String warrantyId) {
        this.warrantyId = warrantyId;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setWarranty(null));
        }
        if (products != null) {
            products.forEach(i -> i.setWarranty(this));
        }
        this.products = products;
    }

    public Warranty products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Warranty addProduct(Product product) {
        this.products.add(product);
        product.setWarranty(this);
        return this;
    }

    public Warranty removeProduct(Product product) {
        this.products.remove(product);
        product.setWarranty(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warranty)) {
            return false;
        }
        return getId() != null && getId().equals(((Warranty) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Warranty{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", warrantyEn='" + getWarrantyEn() + "'" +
            ", warrantyId='" + getWarrantyId() + "'" +
            "}";
    }
}
