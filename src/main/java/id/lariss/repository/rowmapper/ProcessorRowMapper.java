package id.lariss.repository.rowmapper;

import id.lariss.domain.Processor;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Processor}, with proper type conversions.
 */
@Service
public class ProcessorRowMapper implements BiFunction<Row, String, Processor> {

    private final ColumnConverter converter;

    public ProcessorRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Processor} stored in the database.
     */
    @Override
    public Processor apply(Row row, String prefix) {
        Processor entity = new Processor();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAttributeLabel(converter.fromRow(row, prefix + "_attribute_label", String.class));
        entity.setAttributeName(converter.fromRow(row, prefix + "_attribute_name", String.class));
        entity.setOptionLabel(converter.fromRow(row, prefix + "_option_label", String.class));
        entity.setOptionValue(converter.fromRow(row, prefix + "_option_value", String.class));
        return entity;
    }
}
