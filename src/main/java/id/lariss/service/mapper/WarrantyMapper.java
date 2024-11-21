package id.lariss.service.mapper;

import id.lariss.domain.Warranty;
import id.lariss.service.dto.WarrantyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Warranty} and its DTO {@link WarrantyDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarrantyMapper extends EntityMapper<WarrantyDTO, Warranty> {}
