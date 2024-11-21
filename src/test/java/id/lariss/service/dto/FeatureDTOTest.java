package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeatureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeatureDTO.class);
        FeatureDTO featureDTO1 = new FeatureDTO();
        featureDTO1.setId(1L);
        FeatureDTO featureDTO2 = new FeatureDTO();
        assertThat(featureDTO1).isNotEqualTo(featureDTO2);
        featureDTO2.setId(featureDTO1.getId());
        assertThat(featureDTO1).isEqualTo(featureDTO2);
        featureDTO2.setId(2L);
        assertThat(featureDTO1).isNotEqualTo(featureDTO2);
        featureDTO1.setId(null);
        assertThat(featureDTO1).isNotEqualTo(featureDTO2);
    }
}
