package id.lariss.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Variant.
 */
@Table("variant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Variant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("label")
    private String label;

    @Column("sku")
    private String sku;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "variants" }, allowSetters = true)
    private Color color;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "variants" }, allowSetters = true)
    private Processor processor;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "variants" }, allowSetters = true)
    private Memory memory;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "variants" }, allowSetters = true)
    private Storage storage;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "variants" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @Column("color_id")
    private Long colorId;

    @Column("processor_id")
    private Long processorId;

    @Column("memory_id")
    private Long memoryId;

    @Column("storage_id")
    private Long storageId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Variant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Variant label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSku() {
        return this.sku;
    }

    public Variant sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.colorId = color != null ? color.getId() : null;
    }

    public Variant color(Color color) {
        this.setColor(color);
        return this;
    }

    public Processor getProcessor() {
        return this.processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
        this.processorId = processor != null ? processor.getId() : null;
    }

    public Variant processor(Processor processor) {
        this.setProcessor(processor);
        return this;
    }

    public Memory getMemory() {
        return this.memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
        this.memoryId = memory != null ? memory.getId() : null;
    }

    public Variant memory(Memory memory) {
        this.setMemory(memory);
        return this;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
        this.storageId = storage != null ? storage.getId() : null;
    }

    public Variant storage(Storage storage) {
        this.setStorage(storage);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.removeVariant(this));
        }
        if (products != null) {
            products.forEach(i -> i.addVariant(this));
        }
        this.products = products;
    }

    public Variant products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Variant addProduct(Product product) {
        this.products.add(product);
        product.getVariants().add(this);
        return this;
    }

    public Variant removeProduct(Product product) {
        this.products.remove(product);
        product.getVariants().remove(this);
        return this;
    }

    public Long getColorId() {
        return this.colorId;
    }

    public void setColorId(Long color) {
        this.colorId = color;
    }

    public Long getProcessorId() {
        return this.processorId;
    }

    public void setProcessorId(Long processor) {
        this.processorId = processor;
    }

    public Long getMemoryId() {
        return this.memoryId;
    }

    public void setMemoryId(Long memory) {
        this.memoryId = memory;
    }

    public Long getStorageId() {
        return this.storageId;
    }

    public void setStorageId(Long storage) {
        this.storageId = storage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Variant)) {
            return false;
        }
        return getId() != null && getId().equals(((Variant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Variant{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", sku='" + getSku() + "'" +
            "}";
    }
}
