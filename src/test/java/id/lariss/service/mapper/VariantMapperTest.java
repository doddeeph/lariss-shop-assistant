package id.lariss.service.mapper;

import static id.lariss.domain.VariantAsserts.*;
import static id.lariss.domain.VariantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VariantMapperTest {

    private VariantMapper variantMapper;

    @BeforeEach
    void setUp() {
        variantMapper = new VariantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVariantSample1();
        var actual = variantMapper.toEntity(variantMapper.toDto(expected));
        assertVariantAllPropertiesEquals(expected, actual);
    }
}
