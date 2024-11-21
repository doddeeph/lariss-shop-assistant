package id.lariss.repository.rowmapper;

import id.lariss.domain.Feature;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Feature}, with proper type conversions.
 */
@Service
public class FeatureRowMapper implements BiFunction<Row, String, Feature> {

    private final ColumnConverter converter;

    public FeatureRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Feature} stored in the database.
     */
    @Override
    public Feature apply(Row row, String prefix) {
        Feature entity = new Feature();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setFeatureEn(converter.fromRow(row, prefix + "_feature_en", String.class));
        entity.setFeatureId(converter.fromRow(row, prefix + "_feature_id", String.class));
        return entity;
    }
}
