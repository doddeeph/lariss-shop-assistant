package id.lariss.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Product.
 */
@Table("product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("sku")
    private String sku;

    @NotNull(message = "must not be null")
    @Column("price")
    private BigDecimal price;

    @NotNull(message = "must not be null")
    @Column("special_price")
    private BigDecimal specialPrice;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private Integer quantity;

    @NotNull(message = "must not be null")
    @Column("thumbnail")
    private String thumbnail;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "color", "processor", "memory", "storage", "products" }, allowSetters = true)
    private Set<Variant> variants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return this.sku;
    }

    public Product sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price != null ? price.stripTrailingZeros() : null;
    }

    public BigDecimal getSpecialPrice() {
        return this.specialPrice;
    }

    public Product specialPrice(BigDecimal specialPrice) {
        this.setSpecialPrice(specialPrice);
        return this;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice != null ? specialPrice.stripTrailingZeros() : null;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Product quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public Product thumbnail(String thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Set<Variant> getVariants() {
        return this.variants;
    }

    public void setVariants(Set<Variant> variants) {
        this.variants = variants;
    }

    public Product variants(Set<Variant> variants) {
        this.setVariants(variants);
        return this;
    }

    public Product addVariant(Variant variant) {
        this.variants.add(variant);
        return this;
    }

    public Product removeVariant(Variant variant) {
        this.variants.remove(variant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sku='" + getSku() + "'" +
            ", price=" + getPrice() +
            ", specialPrice=" + getSpecialPrice() +
            ", quantity=" + getQuantity() +
            ", thumbnail='" + getThumbnail() + "'" +
            "}";
    }
}
