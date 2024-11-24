package id.lariss.service.mapper;

import id.lariss.domain.Product;
import id.lariss.domain.Variant;
import id.lariss.service.dto.ProductDTO;
import id.lariss.service.dto.VariantDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "variants", source = "variants", qualifiedByName = "variantSkuSet")
    ProductDTO toDto(Product s);

    @Mapping(target = "removeVariant", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Named("variantSku")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sku", source = "sku")
    VariantDTO toDtoVariantSku(Variant variant);

    @Named("variantSkuSet")
    default Set<VariantDTO> toDtoVariantSkuSet(Set<Variant> variant) {
        return variant.stream().map(this::toDtoVariantSku).collect(Collectors.toSet());
    }
}
