package id.lariss.domain;

import static id.lariss.domain.FeatureTestSamples.*;
import static id.lariss.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FeatureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feature.class);
        Feature feature1 = getFeatureSample1();
        Feature feature2 = new Feature();
        assertThat(feature1).isNotEqualTo(feature2);

        feature2.setId(feature1.getId());
        assertThat(feature1).isEqualTo(feature2);

        feature2 = getFeatureSample2();
        assertThat(feature1).isNotEqualTo(feature2);
    }

    @Test
    void productTest() {
        Feature feature = getFeatureRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        feature.addProduct(productBack);
        assertThat(feature.getProducts()).containsOnly(productBack);
        assertThat(productBack.getFeature()).isEqualTo(feature);

        feature.removeProduct(productBack);
        assertThat(feature.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getFeature()).isNull();

        feature.products(new HashSet<>(Set.of(productBack)));
        assertThat(feature.getProducts()).containsOnly(productBack);
        assertThat(productBack.getFeature()).isEqualTo(feature);

        feature.setProducts(new HashSet<>());
        assertThat(feature.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getFeature()).isNull();
    }
}
