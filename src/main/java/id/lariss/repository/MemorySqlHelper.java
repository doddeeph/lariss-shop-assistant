package id.lariss.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class MemorySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("attribute_label", table, columnPrefix + "_attribute_label"));
        columns.add(Column.aliased("attribute_name", table, columnPrefix + "_attribute_name"));
        columns.add(Column.aliased("option_label", table, columnPrefix + "_option_label"));
        columns.add(Column.aliased("option_value", table, columnPrefix + "_option_value"));

        return columns;
    }
}
