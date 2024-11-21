package id.lariss.service.dto;

import id.lariss.domain.enumeration.Color;
import id.lariss.domain.enumeration.Currency;
import id.lariss.domain.enumeration.DiscountType;
import id.lariss.domain.enumeration.Memory;
import id.lariss.domain.enumeration.Processor;
import id.lariss.domain.enumeration.Storage;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link id.lariss.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String sku;

    @NotNull(message = "must not be null")
    private BigDecimal basePrice;

    private BigDecimal discountPrice;

    private BigDecimal discountAmount;

    private DiscountType discountType;

    @NotNull(message = "must not be null")
    private Currency currency;

    @NotNull(message = "must not be null")
    private Color color;

    @NotNull(message = "must not be null")
    private Processor processor;

    @NotNull(message = "must not be null")
    private Memory memory;

    @NotNull(message = "must not be null")
    private Storage storage;

    private CategoryDTO category;

    private DescriptionDTO description;

    private FeatureDTO feature;

    private BoxContentDTO boxContent;

    private WarrantyDTO warranty;

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public DescriptionDTO getDescription() {
        return description;
    }

    public void setDescription(DescriptionDTO description) {
        this.description = description;
    }

    public FeatureDTO getFeature() {
        return feature;
    }

    public void setFeature(FeatureDTO feature) {
        this.feature = feature;
    }

    public BoxContentDTO getBoxContent() {
        return boxContent;
    }

    public void setBoxContent(BoxContentDTO boxContent) {
        this.boxContent = boxContent;
    }

    public WarrantyDTO getWarranty() {
        return warranty;
    }

    public void setWarranty(WarrantyDTO warranty) {
        this.warranty = warranty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
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
            ", category=" + getCategory() +
            ", description=" + getDescription() +
            ", feature=" + getFeature() +
            ", boxContent=" + getBoxContent() +
            ", warranty=" + getWarranty() +
            "}";
    }
}
