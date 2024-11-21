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
 * A BoxContent.
 */
@Table("box_content")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BoxContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("content_en")
    private String contentEn;

    @Column("content_id")
    private String contentId;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "category", "description", "feature", "boxContent", "warranty" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BoxContent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public BoxContent name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentEn() {
        return this.contentEn;
    }

    public BoxContent contentEn(String contentEn) {
        this.setContentEn(contentEn);
        return this;
    }

    public void setContentEn(String contentEn) {
        this.contentEn = contentEn;
    }

    public String getContentId() {
        return this.contentId;
    }

    public BoxContent contentId(String contentId) {
        this.setContentId(contentId);
        return this;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setBoxContent(null));
        }
        if (products != null) {
            products.forEach(i -> i.setBoxContent(this));
        }
        this.products = products;
    }

    public BoxContent products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public BoxContent addProduct(Product product) {
        this.products.add(product);
        product.setBoxContent(this);
        return this;
    }

    public BoxContent removeProduct(Product product) {
        this.products.remove(product);
        product.setBoxContent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BoxContent)) {
            return false;
        }
        return getId() != null && getId().equals(((BoxContent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BoxContent{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contentEn='" + getContentEn() + "'" +
            ", contentId='" + getContentId() + "'" +
            "}";
    }
}
