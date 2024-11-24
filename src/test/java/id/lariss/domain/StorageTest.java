package id.lariss.domain;

import static id.lariss.domain.StorageTestSamples.*;
import static id.lariss.domain.VariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StorageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Storage.class);
        Storage storage1 = getStorageSample1();
        Storage storage2 = new Storage();
        assertThat(storage1).isNotEqualTo(storage2);

        storage2.setId(storage1.getId());
        assertThat(storage1).isEqualTo(storage2);

        storage2 = getStorageSample2();
        assertThat(storage1).isNotEqualTo(storage2);
    }

    @Test
    void variantTest() {
        Storage storage = getStorageRandomSampleGenerator();
        Variant variantBack = getVariantRandomSampleGenerator();

        storage.addVariant(variantBack);
        assertThat(storage.getVariants()).containsOnly(variantBack);
        assertThat(variantBack.getStorage()).isEqualTo(storage);

        storage.removeVariant(variantBack);
        assertThat(storage.getVariants()).doesNotContain(variantBack);
        assertThat(variantBack.getStorage()).isNull();

        storage.variants(new HashSet<>(Set.of(variantBack)));
        assertThat(storage.getVariants()).containsOnly(variantBack);
        assertThat(variantBack.getStorage()).isEqualTo(storage);

        storage.setVariants(new HashSet<>());
        assertThat(storage.getVariants()).doesNotContain(variantBack);
        assertThat(variantBack.getStorage()).isNull();
    }
}
