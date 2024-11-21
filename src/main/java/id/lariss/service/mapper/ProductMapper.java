package id.lariss.service.mapper;

import id.lariss.domain.BoxContent;
import id.lariss.domain.Category;
import id.lariss.domain.Description;
import id.lariss.domain.Feature;
import id.lariss.domain.Product;
import id.lariss.domain.Warranty;
import id.lariss.service.dto.BoxContentDTO;
import id.lariss.service.dto.CategoryDTO;
import id.lariss.service.dto.DescriptionDTO;
import id.lariss.service.dto.FeatureDTO;
import id.lariss.service.dto.ProductDTO;
import id.lariss.service.dto.WarrantyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryCategoryEn")
    @Mapping(target = "description", source = "description", qualifiedByName = "descriptionName")
    @Mapping(target = "feature", source = "feature", qualifiedByName = "featureName")
    @Mapping(target = "boxContent", source = "boxContent", qualifiedByName = "boxContentName")
    @Mapping(target = "warranty", source = "warranty", qualifiedByName = "warrantyName")
    ProductDTO toDto(Product s);

    @Named("categoryCategoryEn")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "categoryEn", source = "categoryEn")
    CategoryDTO toDtoCategoryCategoryEn(Category category);

    @Named("descriptionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DescriptionDTO toDtoDescriptionName(Description description);

    @Named("featureName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FeatureDTO toDtoFeatureName(Feature feature);

    @Named("boxContentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    BoxContentDTO toDtoBoxContentName(BoxContent boxContent);

    @Named("warrantyName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    WarrantyDTO toDtoWarrantyName(Warranty warranty);
}
