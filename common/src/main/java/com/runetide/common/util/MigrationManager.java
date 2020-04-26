package com.runetide.common.util;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.runetide.common.Constants;
import com.runetide.common.LockManager;
import org.eclipse.jetty.util.IO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Singleton
public class MigrationManager {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String TABLE_NAME = "migration";
    private static final SimpleStatement CREATE_TABLE = SchemaBuilder
            .createTable(TABLE_NAME)
            .ifNotExists()
            .withPartitionKey("name", DataTypes.TEXT)
            .withColumn("date", DataTypes.TIMEUUID)
            .withCompaction(SchemaBuilder.leveledCompactionStrategy())
            .build();
    private static final SimpleStatement SELECT_MIGRATE = QueryBuilder
            .selectFrom(TABLE_NAME)
            .column("name")
            .whereColumn("name")
            .isEqualTo(QueryBuilder.bindMarker("name"))
            .build();
    private static final SimpleStatement INSERT_MIGRATE = QueryBuilder
            .insertInto(TABLE_NAME)
            .value("name", QueryBuilder.bindMarker("name"))
            .value("date", QueryBuilder.function("now"))
            .build();

    private final CqlSession cqlSession;
    private final LockManager lockManager;

    @Inject
    public MigrationManager(CqlSession cqlSession, LockManager lockManager) {
        this.cqlSession = cqlSession;
        this.lockManager = lockManager;
    }

    public void runMigrations(){
        migrateAll();
    }

    private void migrateAll() {
        try {
            //noinspection StatementWithEmptyBody
            while (!lockManager.acquire(Constants.LOCK_MIGRATION))
                /* Spin until we acquire the lock */ ;
            LOG.info("Running migrations.");
            final boolean schemaMetadataEnabled = cqlSession.isSchemaMetadataEnabled();
            cqlSession.setSchemaMetadataEnabled(false);
            createMigrationTables();
            for (final File migration : listMigrations())
                runMigration(migration);
            cqlSession.setSchemaMetadataEnabled(schemaMetadataEnabled);
            cqlSession.refreshSchema();
        } catch(final Exception e) {
            LOG.error("Got exception running migrations.", e);
        } finally {
            LOG.info("Migrations complete.");
            lockManager.release(Constants.LOCK_MIGRATION);
        }
    }

    private void createMigrationTables() {
        cqlSession.execute(CREATE_TABLE);
    }

    private void runMigration(final File migration) {
        if(hasRunMigration(migration.getName()))
            return;
        LOG.info("Running migration: {}", migration.getName());
        for(final SimpleStatement statement : getStatements(migration)) {
            LOG.debug("Migration({}): {}", migration.getName(), statement.getQuery());
            cqlSession.execute(statement);
        }
        LOG.info("Ran migration: {}", migration.getName());
        setMigrationRun(migration.getName());
    }

    private boolean hasRunMigration(final String name) {
        final ResultSet rs = cqlSession.execute(cqlSession.prepare(SELECT_MIGRATE).bind()
                .setString("name", name)
                .setConsistencyLevel(ConsistencyLevel.EACH_QUORUM));
        return rs.one() != null;
    }

    private void setMigrationRun(final String name) {
        cqlSession.execute(cqlSession.prepare(INSERT_MIGRATE).bind()
                .setString("name", name)
                .setConsistencyLevel(ConsistencyLevel.EACH_QUORUM));
    }

    private List<SimpleStatement> getStatements(final File migration) {
        try(final FileInputStream fis = new FileInputStream(migration)) {
            return StreamSupport
                    .stream(Splitter
                        .on(";")
                        .split(new String(IO.readBytes(fis))).spliterator(), false)
                    .filter(s -> !CharMatcher.whitespace().matchesAllOf(s))
                    .map(SimpleStatement::newInstance)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOG.error("Got exception while reading migration={}", migration, e);
            throw new RuntimeException(e);
        }
    }

    private List<File> listMigrations() {
        final URL folder = Thread.currentThread().getContextClassLoader().getResource("migrations");
        if(folder == null)
            return ImmutableList.of();
        final String path = folder.getPath();
        return Arrays.stream(Optional.ofNullable(new File(path).listFiles()).orElse(new File[] {}))
                .sorted(Comparator.comparing(File::getName))
                .collect(Collectors.toList());
    }
}