package id.lariss.service.mapper;

import static id.lariss.domain.BoxContentAsserts.*;
import static id.lariss.domain.BoxContentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoxContentMapperTest {

    private BoxContentMapper boxContentMapper;

    @BeforeEach
    void setUp() {
        boxContentMapper = new BoxContentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBoxContentSample1();
        var actual = boxContentMapper.toEntity(boxContentMapper.toDto(expected));
        assertBoxContentAllPropertiesEquals(expected, actual);
    }
}
