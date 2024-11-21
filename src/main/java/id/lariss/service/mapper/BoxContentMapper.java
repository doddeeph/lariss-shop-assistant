package id.lariss.service.mapper;

import id.lariss.domain.BoxContent;
import id.lariss.service.dto.BoxContentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BoxContent} and its DTO {@link BoxContentDTO}.
 */
@Mapper(componentModel = "spring")
public interface BoxContentMapper extends EntityMapper<BoxContentDTO, BoxContent> {}
