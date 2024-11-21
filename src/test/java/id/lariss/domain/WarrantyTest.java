package id.lariss.domain;

import static id.lariss.domain.ProductTestSamples.*;
import static id.lariss.domain.WarrantyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WarrantyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Warranty.class);
        Warranty warranty1 = getWarrantySample1();
        Warranty warranty2 = new Warranty();
        assertThat(warranty1).isNotEqualTo(warranty2);

        warranty2.setId(warranty1.getId());
        assertThat(warranty1).isEqualTo(warranty2);

        warranty2 = getWarrantySample2();
        assertThat(warranty1).isNotEqualTo(warranty2);
    }

    @Test
    void productTest() {
        Warranty warranty = getWarrantyRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        warranty.addProduct(productBack);
        assertThat(warranty.getProducts()).containsOnly(productBack);
        assertThat(productBack.getWarranty()).isEqualTo(warranty);

        warranty.removeProduct(productBack);
        assertThat(warranty.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getWarranty()).isNull();

        warranty.products(new HashSet<>(Set.of(productBack)));
        assertThat(warranty.getProducts()).containsOnly(productBack);
        assertThat(productBack.getWarranty()).isEqualTo(warranty);

        warranty.setProducts(new HashSet<>());
        assertThat(warranty.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getWarranty()).isNull();
    }
}
