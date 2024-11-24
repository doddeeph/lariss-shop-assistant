package id.lariss.domain;

import static id.lariss.domain.ColorTestSamples.*;
import static id.lariss.domain.MemoryTestSamples.*;
import static id.lariss.domain.ProcessorTestSamples.*;
import static id.lariss.domain.ProductTestSamples.*;
import static id.lariss.domain.StorageTestSamples.*;
import static id.lariss.domain.VariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VariantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Variant.class);
        Variant variant1 = getVariantSample1();
        Variant variant2 = new Variant();
        assertThat(variant1).isNotEqualTo(variant2);

        variant2.setId(variant1.getId());
        assertThat(variant1).isEqualTo(variant2);

        variant2 = getVariantSample2();
        assertThat(variant1).isNotEqualTo(variant2);
    }

    @Test
    void colorTest() {
        Variant variant = getVariantRandomSampleGenerator();
        Color colorBack = getColorRandomSampleGenerator();

        variant.setColor(colorBack);
        assertThat(variant.getColor()).isEqualTo(colorBack);

        variant.color(null);
        assertThat(variant.getColor()).isNull();
    }

    @Test
    void processorTest() {
        Variant variant = getVariantRandomSampleGenerator();
        Processor processorBack = getProcessorRandomSampleGenerator();

        variant.setProcessor(processorBack);
        assertThat(variant.getProcessor()).isEqualTo(processorBack);

        variant.processor(null);
        assertThat(variant.getProcessor()).isNull();
    }

    @Test
    void memoryTest() {
        Variant variant = getVariantRandomSampleGenerator();
        Memory memoryBack = getMemoryRandomSampleGenerator();

        variant.setMemory(memoryBack);
        assertThat(variant.getMemory()).isEqualTo(memoryBack);

        variant.memory(null);
        assertThat(variant.getMemory()).isNull();
    }

    @Test
    void storageTest() {
        Variant variant = getVariantRandomSampleGenerator();
        Storage storageBack = getStorageRandomSampleGenerator();

        variant.setStorage(storageBack);
        assertThat(variant.getStorage()).isEqualTo(storageBack);

        variant.storage(null);
        assertThat(variant.getStorage()).isNull();
    }

    @Test
    void productTest() {
        Variant variant = getVariantRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        variant.addProduct(productBack);
        assertThat(variant.getProducts()).containsOnly(productBack);
        assertThat(productBack.getVariants()).containsOnly(variant);

        variant.removeProduct(productBack);
        assertThat(variant.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getVariants()).doesNotContain(variant);

        variant.products(new HashSet<>(Set.of(productBack)));
        assertThat(variant.getProducts()).containsOnly(productBack);
        assertThat(productBack.getVariants()).containsOnly(variant);

        variant.setProducts(new HashSet<>());
        assertThat(variant.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getVariants()).doesNotContain(variant);
    }
}
