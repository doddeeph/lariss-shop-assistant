package id.lariss.domain;

import static id.lariss.domain.BoxContentTestSamples.*;
import static id.lariss.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BoxContentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoxContent.class);
        BoxContent boxContent1 = getBoxContentSample1();
        BoxContent boxContent2 = new BoxContent();
        assertThat(boxContent1).isNotEqualTo(boxContent2);

        boxContent2.setId(boxContent1.getId());
        assertThat(boxContent1).isEqualTo(boxContent2);

        boxContent2 = getBoxContentSample2();
        assertThat(boxContent1).isNotEqualTo(boxContent2);
    }

    @Test
    void productTest() {
        BoxContent boxContent = getBoxContentRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        boxContent.addProduct(productBack);
        assertThat(boxContent.getProducts()).containsOnly(productBack);
        assertThat(productBack.getBoxContent()).isEqualTo(boxContent);

        boxContent.removeProduct(productBack);
        assertThat(boxContent.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getBoxContent()).isNull();

        boxContent.products(new HashSet<>(Set.of(productBack)));
        assertThat(boxContent.getProducts()).containsOnly(productBack);
        assertThat(productBack.getBoxContent()).isEqualTo(boxContent);

        boxContent.setProducts(new HashSet<>());
        assertThat(boxContent.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getBoxContent()).isNull();
    }
}
