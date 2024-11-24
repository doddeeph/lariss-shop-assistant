package id.lariss.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link id.lariss.domain.Variant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VariantDTO implements Serializable {

    private Long id;

    private String label;

    private String sku;

    private ColorDTO color;

    private ProcessorDTO processor;

    private MemoryDTO memory;

    private StorageDTO storage;

    private Set<ProductDTO> products = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ColorDTO getColor() {
        return color;
    }

    public void setColor(ColorDTO color) {
        this.color = color;
    }

    public ProcessorDTO getProcessor() {
        return processor;
    }

    public void setProcessor(ProcessorDTO processor) {
        this.processor = processor;
    }

    public MemoryDTO getMemory() {
        return memory;
    }

    public void setMemory(MemoryDTO memory) {
        this.memory = memory;
    }

    public StorageDTO getStorage() {
        return storage;
    }

    public void setStorage(StorageDTO storage) {
        this.storage = storage;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VariantDTO)) {
            return false;
        }

        VariantDTO variantDTO = (VariantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, variantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VariantDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", sku='" + getSku() + "'" +
            ", color=" + getColor() +
            ", processor=" + getProcessor() +
            ", memory=" + getMemory() +
            ", storage=" + getStorage() +
            ", products=" + getProducts() +
            "}";
    }
}
