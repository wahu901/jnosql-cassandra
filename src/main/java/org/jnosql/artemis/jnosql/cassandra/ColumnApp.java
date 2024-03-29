/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jnosql.artemis.jnosql.cassandra;

import java.util.Optional;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnConfiguration;
import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.ColumnEntity;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;
import org.jnosql.diana.api.column.ColumnQuery;
import org.jnosql.diana.api.column.Columns;
import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.delete;
import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.select;
import org.jnosql.diana.cassandra.column.CassandraConfiguration;


/**
 *
 * @author WayneHu
 */
public class ColumnApp {
      private static final String KEY_SPACE = "myKeySpace";
    private static final String COLUMN_FAMILY = "books";

    public static void main(String... args) throws Exception {

        EmbeddedCassandraServerHelper.startEmbeddedCassandra(); 
        // Embedded Cassandra starts up on port 9142, see setup in diana-cassandra.properties
        ColumnConfiguration configuration = new CassandraConfiguration();
        try(ColumnFamilyManagerFactory entityManagerFactory = configuration.get()) {
            ColumnFamilyManager entityManager = entityManagerFactory.get(KEY_SPACE);
            ColumnEntity columnEntity = ColumnEntity.of(COLUMN_FAMILY);
            Column key = Columns.of("id", 10L);
            Column name = Columns.of("name", "JNoSQL in Action");
            columnEntity.add(key);
            columnEntity.add(name);
            ColumnEntity saved = entityManager.insert(columnEntity);
            System.out.println("debug result:---->");
            System.out.println("saved entity="+saved);
            ColumnQuery select = select().from(COLUMN_FAMILY).where("id").eq(10L).build();
            Optional<ColumnEntity> result = entityManager.singleResult(select);
            System.out.println("debug query result="+result);
            ColumnDeleteQuery delete = delete().from(COLUMN_FAMILY).where("id").eq(10L).build();
            entityManager.delete(delete);
            System.out.println("debug delete");
        }
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
        EmbeddedCassandraServerHelper.stopEmbeddedCassandra();
    }           
    
}
