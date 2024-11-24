package id.lariss.service.mapper;

import id.lariss.domain.Color;
import id.lariss.domain.Memory;
import id.lariss.domain.Processor;
import id.lariss.domain.Product;
import id.lariss.domain.Storage;
import id.lariss.domain.Variant;
import id.lariss.service.dto.ColorDTO;
import id.lariss.service.dto.MemoryDTO;
import id.lariss.service.dto.ProcessorDTO;
import id.lariss.service.dto.ProductDTO;
import id.lariss.service.dto.StorageDTO;
import id.lariss.service.dto.VariantDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Variant} and its DTO {@link VariantDTO}.
 */
@Mapper(componentModel = "spring")
public interface VariantMapper extends EntityMapper<VariantDTO, Variant> {
    @Mapping(target = "color", source = "color", qualifiedByName = "colorOptionLabel")
    @Mapping(target = "processor", source = "processor", qualifiedByName = "processorOptionLabel")
    @Mapping(target = "memory", source = "memory", qualifiedByName = "memoryOptionLabel")
    @Mapping(target = "storage", source = "storage", qualifiedByName = "storageOptionLabel")
    @Mapping(target = "products", source = "products", qualifiedByName = "productNameSet")
    VariantDTO toDto(Variant s);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "removeProduct", ignore = true)
    Variant toEntity(VariantDTO variantDTO);

    @Named("colorOptionLabel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "optionLabel", source = "optionLabel")
    ColorDTO toDtoColorOptionLabel(Color color);

    @Named("processorOptionLabel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "optionLabel", source = "optionLabel")
    ProcessorDTO toDtoProcessorOptionLabel(Processor processor);

    @Named("memoryOptionLabel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "optionLabel", source = "optionLabel")
    MemoryDTO toDtoMemoryOptionLabel(Memory memory);

    @Named("storageOptionLabel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "optionLabel", source = "optionLabel")
    StorageDTO toDtoStorageOptionLabel(Storage storage);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);

    @Named("productNameSet")
    default Set<ProductDTO> toDtoProductNameSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductName).collect(Collectors.toSet());
    }
}
