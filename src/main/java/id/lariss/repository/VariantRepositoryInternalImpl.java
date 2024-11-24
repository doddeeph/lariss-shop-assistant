package id.lariss.repository;

import id.lariss.domain.Variant;
import id.lariss.repository.rowmapper.ColorRowMapper;
import id.lariss.repository.rowmapper.MemoryRowMapper;
import id.lariss.repository.rowmapper.ProcessorRowMapper;
import id.lariss.repository.rowmapper.StorageRowMapper;
import id.lariss.repository.rowmapper.VariantRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Variant entity.
 */
@SuppressWarnings("unused")
class VariantRepositoryInternalImpl extends SimpleR2dbcRepository<Variant, Long> implements VariantRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ColorRowMapper colorMapper;
    private final ProcessorRowMapper processorMapper;
    private final MemoryRowMapper memoryMapper;
    private final StorageRowMapper storageMapper;
    private final VariantRowMapper variantMapper;

    private static final Table entityTable = Table.aliased("variant", EntityManager.ENTITY_ALIAS);
    private static final Table colorTable = Table.aliased("color", "color");
    private static final Table processorTable = Table.aliased("processor", "processor");
    private static final Table memoryTable = Table.aliased("memory", "memory");
    private static final Table storageTable = Table.aliased("storage", "e_storage");

    public VariantRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ColorRowMapper colorMapper,
        ProcessorRowMapper processorMapper,
        MemoryRowMapper memoryMapper,
        StorageRowMapper storageMapper,
        VariantRowMapper variantMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Variant.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.colorMapper = colorMapper;
        this.processorMapper = processorMapper;
        this.memoryMapper = memoryMapper;
        this.storageMapper = storageMapper;
        this.variantMapper = variantMapper;
    }

    @Override
    public Flux<Variant> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Variant> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = VariantSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ColorSqlHelper.getColumns(colorTable, "color"));
        columns.addAll(ProcessorSqlHelper.getColumns(processorTable, "processor"));
        columns.addAll(MemorySqlHelper.getColumns(memoryTable, "memory"));
        columns.addAll(StorageSqlHelper.getColumns(storageTable, "storage"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(colorTable)
            .on(Column.create("color_id", entityTable))
            .equals(Column.create("id", colorTable))
            .leftOuterJoin(processorTable)
            .on(Column.create("processor_id", entityTable))
            .equals(Column.create("id", processorTable))
            .leftOuterJoin(memoryTable)
            .on(Column.create("memory_id", entityTable))
            .equals(Column.create("id", memoryTable))
            .leftOuterJoin(storageTable)
            .on(Column.create("storage_id", entityTable))
            .equals(Column.create("id", storageTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Variant.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Variant> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Variant> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Variant> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Variant> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Variant> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    @Override
    public Flux<Variant> findAllByProductName(String productName, Pageable pageable) {
        Table productVariantTable = Table.aliased("rel_product__variant", "pv");
        Table productTable = Table.aliased("product", "product");

        List<Expression> columns = VariantSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ColorSqlHelper.getColumns(colorTable, "color"));
        columns.addAll(ProcessorSqlHelper.getColumns(processorTable, "processor"));
        columns.addAll(MemorySqlHelper.getColumns(memoryTable, "memory"));
        columns.addAll(StorageSqlHelper.getColumns(storageTable, "storage"));

        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(productVariantTable)
            .on(Column.create("id", entityTable))
            .equals(Column.create("variant_id", productVariantTable))
            .leftOuterJoin(colorTable)
            .on(Column.create("id", colorTable))
            .equals(Column.create("color_id", entityTable))
            .leftOuterJoin(processorTable)
            .on(Column.create("id", processorTable))
            .equals(Column.create("processor_id", entityTable))
            .leftOuterJoin(memoryTable)
            .on(Column.create("id", memoryTable))
            .equals(Column.create("memory_id", entityTable))
            .leftOuterJoin(storageTable)
            .on(Column.create("id", storageTable))
            .equals(Column.create("storage_id", entityTable))
            .leftOuterJoin(productTable)
            .on(Column.create("id", productTable))
            .equals(Column.create("product_id", productVariantTable));

        Condition whereClause = Conditions.just(
            "LOWER(" + productTable.column("name").getName() + ") LIKE " + SQL.literalOf('%' + productName.toLowerCase() + '%')
        );
        //        Condition whereClause = Conditions.like(productTable.column("name"), SQL.literalOf('%' + productName + '%'));
        String select = entityManager.createSelect(selectFrom, Variant.class, pageable, whereClause);

        return db
            .sql(select)
            .map((row, rowMetadata) -> {
                Variant entity = variantMapper.apply(row, "e");
                entity.setColor(colorMapper.apply(row, "color"));
                entity.setProcessor(processorMapper.apply(row, "processor"));
                entity.setMemory(memoryMapper.apply(row, "memory"));
                entity.setStorage(storageMapper.apply(row, "storage"));
                return entity;
            })
            .all();
    }

    private Variant process(Row row, RowMetadata metadata) {
        Variant entity = variantMapper.apply(row, "e");
        entity.setColor(colorMapper.apply(row, "color"));
        entity.setProcessor(processorMapper.apply(row, "processor"));
        entity.setMemory(memoryMapper.apply(row, "memory"));
        entity.setStorage(storageMapper.apply(row, "storage"));
        return entity;
    }

    @Override
    public <S extends Variant> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
