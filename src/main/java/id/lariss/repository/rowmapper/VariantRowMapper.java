package id.lariss.repository.rowmapper;

import id.lariss.domain.Variant;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Variant}, with proper type conversions.
 */
@Service
public class VariantRowMapper implements BiFunction<Row, String, Variant> {

    private final ColumnConverter converter;

    public VariantRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Variant} stored in the database.
     */
    @Override
    public Variant apply(Row row, String prefix) {
        Variant entity = new Variant();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setLabel(converter.fromRow(row, prefix + "_label", String.class));
        entity.setSku(converter.fromRow(row, prefix + "_sku", String.class));
        entity.setColorId(converter.fromRow(row, prefix + "_color_id", Long.class));
        entity.setProcessorId(converter.fromRow(row, prefix + "_processor_id", Long.class));
        entity.setMemoryId(converter.fromRow(row, prefix + "_memory_id", Long.class));
        entity.setStorageId(converter.fromRow(row, prefix + "_storage_id", Long.class));
        return entity;
    }
}
