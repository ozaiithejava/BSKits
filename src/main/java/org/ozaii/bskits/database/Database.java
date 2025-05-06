package org.ozaii.bskits.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Database {

    private static volatile Database instance;

    private final String dbPath;
    private ConnectionSource connectionSource;

    private Database(String dbPath) {
        this.dbPath = dbPath;
    }

    // DC INSTANCE.
    public static Database getInstance(String dbPath) {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database(dbPath);
                }
            }
        }
        return instance;
    }

    public void connect() throws SQLException {
        File file = new File(dbPath);
        file.getParentFile().mkdirs();
        connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + dbPath);
    }

    public void createTable(Class<?> clazz) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, clazz);
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public void disconnect() throws SQLException, IOException {
        if (connectionSource != null) {
            connectionSource.close();
        }
    }
}
