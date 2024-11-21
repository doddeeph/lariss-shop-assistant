package id.lariss.repository.rowmapper;

import id.lariss.domain.Description;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Description}, with proper type conversions.
 */
@Service
public class DescriptionRowMapper implements BiFunction<Row, String, Description> {

    private final ColumnConverter converter;

    public DescriptionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Description} stored in the database.
     */
    @Override
    public Description apply(Row row, String prefix) {
        Description entity = new Description();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescriptionEn(converter.fromRow(row, prefix + "_description_en", String.class));
        entity.setDescriptionId(converter.fromRow(row, prefix + "_description_id", String.class));
        return entity;
    }
}
