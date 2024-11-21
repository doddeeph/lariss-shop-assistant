package id.lariss.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ProductSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("sku", table, columnPrefix + "_sku"));
        columns.add(Column.aliased("base_price", table, columnPrefix + "_base_price"));
        columns.add(Column.aliased("discount_price", table, columnPrefix + "_discount_price"));
        columns.add(Column.aliased("discount_amount", table, columnPrefix + "_discount_amount"));
        columns.add(Column.aliased("discount_type", table, columnPrefix + "_discount_type"));
        columns.add(Column.aliased("currency", table, columnPrefix + "_currency"));
        columns.add(Column.aliased("color", table, columnPrefix + "_color"));
        columns.add(Column.aliased("processor", table, columnPrefix + "_processor"));
        columns.add(Column.aliased("memory", table, columnPrefix + "_memory"));
        columns.add(Column.aliased("storage", table, columnPrefix + "_storage"));

        columns.add(Column.aliased("category_id", table, columnPrefix + "_category_id"));
        columns.add(Column.aliased("description_id", table, columnPrefix + "_description_id"));
        columns.add(Column.aliased("feature_id", table, columnPrefix + "_feature_id"));
        columns.add(Column.aliased("box_content_id", table, columnPrefix + "_box_content_id"));
        columns.add(Column.aliased("warranty_id", table, columnPrefix + "_warranty_id"));
        return columns;
    }
}
