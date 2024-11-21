package id.lariss.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import id.lariss.domain.enumeration.Color;
import id.lariss.domain.enumeration.Currency;
import id.lariss.domain.enumeration.DiscountType;
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
    @Column("base_price")
    private BigDecimal basePrice;

    @Column("discount_price")
    private BigDecimal discountPrice;

    @Column("discount_amount")
    private BigDecimal discountAmount;

    @Column("discount_type")
    private DiscountType discountType;

    @NotNull(message = "must not be null")
    @Column("currency")
    private Currency currency;

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

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Category category;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Description description;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Feature feature;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private BoxContent boxContent;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Warranty warranty;

    @Column("category_id")
    private Long categoryId;

    @Column("description_id")
    private Long descriptionId;

    @Column("feature_id")
    private Long featureId;

    @Column("box_content_id")
    private Long boxContentId;

    @Column("warranty_id")
    private Long warrantyId;

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

    public BigDecimal getBasePrice() {
        return this.basePrice;
    }

    public Product basePrice(BigDecimal basePrice) {
        this.setBasePrice(basePrice);
        return this;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice != null ? basePrice.stripTrailingZeros() : null;
    }

    public BigDecimal getDiscountPrice() {
        return this.discountPrice;
    }

    public Product discountPrice(BigDecimal discountPrice) {
        this.setDiscountPrice(discountPrice);
        return this;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice != null ? discountPrice.stripTrailingZeros() : null;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public Product discountAmount(BigDecimal discountAmount) {
        this.setDiscountAmount(discountAmount);
        return this;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount != null ? discountAmount.stripTrailingZeros() : null;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Product discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Product currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(Currency currency) {
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

    public Description getDescription() {
        return this.description;
    }

    public void setDescription(Description description) {
        this.description = description;
        this.descriptionId = description != null ? description.getId() : null;
    }

    public Product description(Description description) {
        this.setDescription(description);
        return this;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
        this.featureId = feature != null ? feature.getId() : null;
    }

    public Product feature(Feature feature) {
        this.setFeature(feature);
        return this;
    }

    public BoxContent getBoxContent() {
        return this.boxContent;
    }

    public void setBoxContent(BoxContent boxContent) {
        this.boxContent = boxContent;
        this.boxContentId = boxContent != null ? boxContent.getId() : null;
    }

    public Product boxContent(BoxContent boxContent) {
        this.setBoxContent(boxContent);
        return this;
    }

    public Warranty getWarranty() {
        return this.warranty;
    }

    public void setWarranty(Warranty warranty) {
        this.warranty = warranty;
        this.warrantyId = warranty != null ? warranty.getId() : null;
    }

    public Product warranty(Warranty warranty) {
        this.setWarranty(warranty);
        return this;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long category) {
        this.categoryId = category;
    }

    public Long getDescriptionId() {
        return this.descriptionId;
    }

    public void setDescriptionId(Long description) {
        this.descriptionId = description;
    }

    public Long getFeatureId() {
        return this.featureId;
    }

    public void setFeatureId(Long feature) {
        this.featureId = feature;
    }

    public Long getBoxContentId() {
        return this.boxContentId;
    }

    public void setBoxContentId(Long boxContent) {
        this.boxContentId = boxContent;
    }

    public Long getWarrantyId() {
        return this.warrantyId;
    }

    public void setWarrantyId(Long warranty) {
        this.warrantyId = warranty;
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
            ", basePrice=" + getBasePrice() +
            ", discountPrice=" + getDiscountPrice() +
            ", discountAmount=" + getDiscountAmount() +
            ", discountType='" + getDiscountType() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", color='" + getColor() + "'" +
            ", processor='" + getProcessor() + "'" +
            ", memory='" + getMemory() + "'" +
            ", storage='" + getStorage() + "'" +
            "}";
    }
}
