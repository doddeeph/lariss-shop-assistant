package id.lariss.domain;

import static id.lariss.domain.BoxContentTestSamples.*;
import static id.lariss.domain.CategoryTestSamples.*;
import static id.lariss.domain.DescriptionTestSamples.*;
import static id.lariss.domain.FeatureTestSamples.*;
import static id.lariss.domain.ProductTestSamples.*;
import static id.lariss.domain.WarrantyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void categoryTest() {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }

    @Test
    void descriptionTest() {
        Product product = getProductRandomSampleGenerator();
        Description descriptionBack = getDescriptionRandomSampleGenerator();

        product.setDescription(descriptionBack);
        assertThat(product.getDescription()).isEqualTo(descriptionBack);

        product.description(null);
        assertThat(product.getDescription()).isNull();
    }

    @Test
    void featureTest() {
        Product product = getProductRandomSampleGenerator();
        Feature featureBack = getFeatureRandomSampleGenerator();

        product.setFeature(featureBack);
        assertThat(product.getFeature()).isEqualTo(featureBack);

        product.feature(null);
        assertThat(product.getFeature()).isNull();
    }

    @Test
    void boxContentTest() {
        Product product = getProductRandomSampleGenerator();
        BoxContent boxContentBack = getBoxContentRandomSampleGenerator();

        product.setBoxContent(boxContentBack);
        assertThat(product.getBoxContent()).isEqualTo(boxContentBack);

        product.boxContent(null);
        assertThat(product.getBoxContent()).isNull();
    }

    @Test
    void warrantyTest() {
        Product product = getProductRandomSampleGenerator();
        Warranty warrantyBack = getWarrantyRandomSampleGenerator();

        product.setWarranty(warrantyBack);
        assertThat(product.getWarranty()).isEqualTo(warrantyBack);

        product.warranty(null);
        assertThat(product.getWarranty()).isNull();
    }
}
