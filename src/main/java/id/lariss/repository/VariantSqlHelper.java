package id.lariss.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class VariantSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("label", table, columnPrefix + "_label"));
        columns.add(Column.aliased("sku", table, columnPrefix + "_sku"));

        columns.add(Column.aliased("color_id", table, columnPrefix + "_color_id"));
        columns.add(Column.aliased("processor_id", table, columnPrefix + "_processor_id"));
        columns.add(Column.aliased("memory_id", table, columnPrefix + "_memory_id"));
        columns.add(Column.aliased("storage_id", table, columnPrefix + "_storage_id"));
        return columns;
    }
}
