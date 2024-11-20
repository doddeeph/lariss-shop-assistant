package id.lariss.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import id.lariss.domain.enumeration.Color;
import id.lariss.domain.enumeration.Memory;
import id.lariss.domain.enumeration.Processor;
import id.lariss.domain.enumeration.Storage;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
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
    @Column("currency")
    private String currency;

    @NotNull(message = "must not be null")
    @Column("color")
    private Color color;

    @NotNull(message = "must not be null")
    @Column("processor")
    private Processor processor;

    @NotNull(message = "must not be null")
    @Column("memory")
    private Memory memory;

    @NotNull(message = "must not be null")
    @Column("storage")
    private Storage storage;

    @Column("description")
    private String description;

    @Column("feature")
    private String feature;

    @Column("box_contents")
    private String boxContents;

    @Column("warranty")
    private String warranty;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Category category;

    @Column("category_id")
    private Long categoryId;

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

    public String getCurrency() {
        return this.currency;
    }

    public Product currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Color getColor() {
        return this.color;
    }

    public Product color(Color color) {
        this.setColor(color);
        return this;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Processor getProcessor() {
        return this.processor;
    }

    public Product processor(Processor processor) {
        this.setProcessor(processor);
        return this;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public Memory getMemory() {
        return this.memory;
    }

    public Product memory(Memory memory) {
        this.setMemory(memory);
        return this;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public Product storage(Storage storage) {
        this.setStorage(storage);
        return this;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeature() {
        return this.feature;
    }

    public Product feature(String feature) {
        this.setFeature(feature);
        return this;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getBoxContents() {
        return this.boxContents;
    }

    public Product boxContents(String boxContents) {
        this.setBoxContents(boxContents);
        return this;
    }

    public void setBoxContents(String boxContents) {
        this.boxContents = boxContents;
    }

    public String getWarranty() {
        return this.warranty;
    }

    public Product warranty(String warranty) {
        this.setWarranty(warranty);
        return this;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
        this.categoryId = category != null ? category.getId() : null;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long category) {
        this.categoryId = category;
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
            ", currency='" + getCurrency() + "'" +
            ", color='" + getColor() + "'" +
            ", processor='" + getProcessor() + "'" +
            ", memory='" + getMemory() + "'" +
            ", storage='" + getStorage() + "'" +
            ", description='" + getDescription() + "'" +
            ", feature='" + getFeature() + "'" +
            ", boxContents='" + getBoxContents() + "'" +
            ", warranty='" + getWarranty() + "'" +
            "}";
    }
}
