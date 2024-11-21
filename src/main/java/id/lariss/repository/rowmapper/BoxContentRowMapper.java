package id.lariss.repository.rowmapper;

import id.lariss.domain.BoxContent;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link BoxContent}, with proper type conversions.
 */
@Service
public class BoxContentRowMapper implements BiFunction<Row, String, BoxContent> {

    private final ColumnConverter converter;

    public BoxContentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link BoxContent} stored in the database.
     */
    @Override
    public BoxContent apply(Row row, String prefix) {
        BoxContent entity = new BoxContent();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setContentEn(converter.fromRow(row, prefix + "_content_en", String.class));
        entity.setContentId(converter.fromRow(row, prefix + "_content_id", String.class));
        return entity;
    }
}
