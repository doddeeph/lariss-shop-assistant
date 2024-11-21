package id.lariss.domain;

import static id.lariss.domain.DescriptionTestSamples.*;
import static id.lariss.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DescriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Description.class);
        Description description1 = getDescriptionSample1();
        Description description2 = new Description();
        assertThat(description1).isNotEqualTo(description2);

        description2.setId(description1.getId());
        assertThat(description1).isEqualTo(description2);

        description2 = getDescriptionSample2();
        assertThat(description1).isNotEqualTo(description2);
    }

    @Test
    void productTest() {
        Description description = getDescriptionRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        description.addProduct(productBack);
        assertThat(description.getProducts()).containsOnly(productBack);
        assertThat(productBack.getDescription()).isEqualTo(description);

        description.removeProduct(productBack);
        assertThat(description.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getDescription()).isNull();

        description.products(new HashSet<>(Set.of(productBack)));
        assertThat(description.getProducts()).containsOnly(productBack);
        assertThat(productBack.getDescription()).isEqualTo(description);

        description.setProducts(new HashSet<>());
        assertThat(description.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getDescription()).isNull();
    }
}
