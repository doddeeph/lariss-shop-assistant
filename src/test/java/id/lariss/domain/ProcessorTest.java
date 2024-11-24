package id.lariss.domain;

import static id.lariss.domain.ProcessorTestSamples.*;
import static id.lariss.domain.VariantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProcessorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Processor.class);
        Processor processor1 = getProcessorSample1();
        Processor processor2 = new Processor();
        assertThat(processor1).isNotEqualTo(processor2);

        processor2.setId(processor1.getId());
        assertThat(processor1).isEqualTo(processor2);

        processor2 = getProcessorSample2();
        assertThat(processor1).isNotEqualTo(processor2);
    }

    @Test
    void variantTest() {
        Processor processor = getProcessorRandomSampleGenerator();
        Variant variantBack = getVariantRandomSampleGenerator();

        processor.addVariant(variantBack);
        assertThat(processor.getVariants()).containsOnly(variantBack);
        assertThat(variantBack.getProcessor()).isEqualTo(processor);

        processor.removeVariant(variantBack);
        assertThat(processor.getVariants()).doesNotContain(variantBack);
        assertThat(variantBack.getProcessor()).isNull();

        processor.variants(new HashSet<>(Set.of(variantBack)));
        assertThat(processor.getVariants()).containsOnly(variantBack);
        assertThat(variantBack.getProcessor()).isEqualTo(processor);

        processor.setVariants(new HashSet<>());
        assertThat(processor.getVariants()).doesNotContain(variantBack);
        assertThat(variantBack.getProcessor()).isNull();
    }
}
