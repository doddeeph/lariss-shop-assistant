package id.lariss.domain;

import static id.lariss.domain.ColorTestSamples.*;
import static id.lariss.domain.VariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ColorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Color.class);
        Color color1 = getColorSample1();
        Color color2 = new Color();
        assertThat(color1).isNotEqualTo(color2);

        color2.setId(color1.getId());
        assertThat(color1).isEqualTo(color2);

        color2 = getColorSample2();
        assertThat(color1).isNotEqualTo(color2);
    }

    @Test
    void variantTest() {
        Color color = getColorRandomSampleGenerator();
        Variant variantBack = getVariantRandomSampleGenerator();

        color.addVariant(variantBack);
        assertThat(color.getVariants()).containsOnly(variantBack);
        assertThat(variantBack.getColor()).isEqualTo(color);

        color.removeVariant(variantBack);
        assertThat(color.getVariants()).doesNotContain(variantBack);
        assertThat(variantBack.getColor()).isNull();

        color.variants(new HashSet<>(Set.of(variantBack)));
        assertThat(color.getVariants()).containsOnly(variantBack);
        assertThat(variantBack.getColor()).isEqualTo(color);

        color.setVariants(new HashSet<>());
        assertThat(color.getVariants()).doesNotContain(variantBack);
        assertThat(variantBack.getColor()).isNull();
    }
}
