package id.lariss.repository.rowmapper;

import id.lariss.domain.Product;
import id.lariss.domain.enumeration.Color;
import id.lariss.domain.enumeration.Currency;
import id.lariss.domain.enumeration.DiscountType;
import id.lariss.domain.enumeration.Memory;
import id.lariss.domain.enumeration.Processor;
import id.lariss.domain.enumeration.Storage;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Product}, with proper type conversions.
 */
@Service
public class ProductRowMapper implements BiFunction<Row, String, Product> {

    private final ColumnConverter converter;

    public ProductRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Product} stored in the database.
     */
    @Override
    public Product apply(Row row, String prefix) {
        Product entity = new Product();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setSku(converter.fromRow(row, prefix + "_sku", String.class));
        entity.setBasePrice(converter.fromRow(row, prefix + "_base_price", BigDecimal.class));
        entity.setDiscountPrice(converter.fromRow(row, prefix + "_discount_price", BigDecimal.class));
        entity.setDiscountAmount(converter.fromRow(row, prefix + "_discount_amount", BigDecimal.class));
        entity.setDiscountType(converter.fromRow(row, prefix + "_discount_type", DiscountType.class));
        entity.setCurrency(converter.fromRow(row, prefix + "_currency", Currency.class));
        entity.setColor(converter.fromRow(row, prefix + "_color", Color.class));
        entity.setProcessor(converter.fromRow(row, prefix + "_processor", Processor.class));
        entity.setMemory(converter.fromRow(row, prefix + "_memory", Memory.class));
        entity.setStorage(converter.fromRow(row, prefix + "_storage", Storage.class));
        entity.setCategoryId(converter.fromRow(row, prefix + "_category_id", Long.class));
        entity.setDescriptionId(converter.fromRow(row, prefix + "_description_id", Long.class));
        entity.setFeatureId(converter.fromRow(row, prefix + "_feature_id", Long.class));
        entity.setBoxContentId(converter.fromRow(row, prefix + "_box_content_id", Long.class));
        entity.setWarrantyId(converter.fromRow(row, prefix + "_warranty_id", Long.class));
        return entity;
    }
}
