package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VariantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VariantDTO.class);
        VariantDTO variantDTO1 = new VariantDTO();
        variantDTO1.setId(1L);
        VariantDTO variantDTO2 = new VariantDTO();
        assertThat(variantDTO1).isNotEqualTo(variantDTO2);
        variantDTO2.setId(variantDTO1.getId());
        assertThat(variantDTO1).isEqualTo(variantDTO2);
        variantDTO2.setId(2L);
        assertThat(variantDTO1).isNotEqualTo(variantDTO2);
        variantDTO1.setId(null);
        assertThat(variantDTO1).isNotEqualTo(variantDTO2);
    }
}
