package id.lariss.service.mapper;

import id.lariss.domain.Feature;
import id.lariss.service.dto.FeatureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feature} and its DTO {@link FeatureDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeatureMapper extends EntityMapper<FeatureDTO, Feature> {}
