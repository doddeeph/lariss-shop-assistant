package id.lariss.repository.rowmapper;

import id.lariss.domain.Warranty;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Warranty}, with proper type conversions.
 */
@Service
public class WarrantyRowMapper implements BiFunction<Row, String, Warranty> {

    private final ColumnConverter converter;

    public WarrantyRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Warranty} stored in the database.
     */
    @Override
    public Warranty apply(Row row, String prefix) {
        Warranty entity = new Warranty();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setWarrantyEn(converter.fromRow(row, prefix + "_warranty_en", String.class));
        entity.setWarrantyId(converter.fromRow(row, prefix + "_warranty_id", String.class));
        return entity;
    }
}
