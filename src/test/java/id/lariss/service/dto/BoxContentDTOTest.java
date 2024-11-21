package id.lariss.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import id.lariss.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoxContentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoxContentDTO.class);
        BoxContentDTO boxContentDTO1 = new BoxContentDTO();
        boxContentDTO1.setId(1L);
        BoxContentDTO boxContentDTO2 = new BoxContentDTO();
        assertThat(boxContentDTO1).isNotEqualTo(boxContentDTO2);
        boxContentDTO2.setId(boxContentDTO1.getId());
        assertThat(boxContentDTO1).isEqualTo(boxContentDTO2);
        boxContentDTO2.setId(2L);
        assertThat(boxContentDTO1).isNotEqualTo(boxContentDTO2);
        boxContentDTO1.setId(null);
        assertThat(boxContentDTO1).isNotEqualTo(boxContentDTO2);
    }
}
