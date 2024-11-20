package id.lariss.service.dto;

import id.lariss.domain.enumeration.Color;
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
    private BigDecimal price;

    @NotNull(message = "must not be null")
    private String currency;

    @NotNull(message = "must not be null")
    private Color color;

    @NotNull(message = "must not be null")
    private Processor processor;

    @NotNull(message = "must not be null")
    private Memory memory;

    @NotNull(message = "must not be null")
    private Storage storage;

    private String description;

    private String feature;

    private String boxContents;

    private String warranty;

    private CategoryDTO category;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getBoxContents() {
        return boxContents;
    }

    public void setBoxContents(String boxContents) {
        this.boxContents = boxContents;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
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
            ", category=" + getCategory() +
            "}";
    }
}
