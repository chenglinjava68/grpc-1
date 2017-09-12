package cn.sic777.db.cassandra;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.sic777.container.ContainerGetter;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;


/**
 * Cassandra初始化
 *
 * @author Zhengzx
 */
public class Cassandra {
    private static Cluster cluster;

    private Cassandra() {
    }

    private static final Cassandra singleton = new Cassandra();

    public static final Cassandra instance() {
        return singleton;
    }

    private final String DEFAULT_PORT = "9042";

    /**
     * Cassandra初始化
     *
     * @param props
     * @throws Exception
     */
    public void init(Properties props) throws Exception {
        String hostStr = props.getProperty(CassandraConstant.CASSANDRA_HOST);
        int port = Integer.parseInt(props.getProperty(CassandraConstant.CASSANDRA_PORT, DEFAULT_PORT));
        String userName = props.getProperty(CassandraConstant.CASSANDRA_USER_NAME);
        String password = props.getProperty(CassandraConstant.CASSANDRA_PD);
        if (hostStr != null && !hostStr.trim().isEmpty()) {
            cluster = addContactPoint(Cluster.builder(), hostStr.split(",\\s*")).withPort(port).withCredentials(userName, password).build();
        } else {
            throw new Exception("Please define Cassandra contact points for property: cassandra.contactPoints");
        }
    }

    /**
     * 服务器列表
     *
     * @param builder
     * @param hosts
     */
    private Cluster.Builder addContactPoint(Cluster.Builder builder, String[] hosts) {
        for (String host : hosts) {
            builder.addContactPoint(host);
        }
        return builder;
    }

    /**
     * 执行
     *
     * @param statement
     * @return
     */
    private ResultSet execute(Statement statement) {
        Session session = null;
        try {
            session = cluster.connect();
            return session.execute(statement);
        } finally {
            close(session);
        }
    }

    /**
     * 执行
     *
     * @param cql
     * @return
     */
    private ResultSet execute(String cql) {
        Session session = null;
        try {
            session = cluster.connect();
            return session.execute(cql);
        } finally {
            close(session);
        }
    }

    /**
     * 关闭会话
     *
     * @param session
     */
    private void close(Session session) {
        if (null != session) {
            session.close();
        }
    }

    /********************************** CRUD ********************************/
    /********************************** CRUD ********************************/
    /********************************** CRUD ********************************/

    /**
     * 插入数据
     *
     * @param keySpace
     * @param table
     * @param names
     * @param values
     * @return
     */
    public final ResultSet insert(String keySpace, String table, String[] names, Object[] values) {
        return execute(QueryBuilder.insertInto(keySpace, table).values(names, values));
    }

    /**
     * 插入数据
     *
     * @param keySpace
     * @param table
     * @param names
     * @param values
     * @return
     */
    public final ResultSet insert(String keySpace, String table, List<String> names, List<Object> values) {
        return execute(QueryBuilder.insertInto(keySpace, table).values(names, values));
    }

    /**
     * 插入数据
     *
     * @param keySpace
     * @param table
     * @param params
     * @return
     */
    public final ResultSet insert(String keySpace, String table, Map<String, Object> params) {
        if (null == params) {
            return null;
        }
        List<String> names = ContainerGetter.arrayList();
        List<Object> values = ContainerGetter.arrayList();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            names.add(entry.getKey());
            values.add(entry.getValue());
        }
        return execute(QueryBuilder.insertInto(keySpace, table).values(names, values));
    }

}
