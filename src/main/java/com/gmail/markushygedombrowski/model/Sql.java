package com.gmail.markushygedombrowski.model;

import java.sql.*;

public class Sql {
    private SqlSettings sqlSettings;

    public Sql(SqlSettings sqlSettings) {
        this.sqlSettings = sqlSettings;
    }

    public void closeAllSQL(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() throws SQLException {
        String host = sqlSettings.getHost();
        int port = sqlSettings.getPort();
        String database = sqlSettings.getDatabase();
        String username = sqlSettings.getUser();
        String password = sqlSettings.getPassword();

        String connectionString = String.format("jdbc:mysql://%s:%d/%s?characterEncoding=utf8", host, port, database);

        return DriverManager.getConnection(connectionString, username, password);
    }

    public void createDeliveredItemsTableIfNotExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS DeliveredItems (" +
                "uuid VARCHAR(50) PRIMARY KEY," +
                "seed INT DEFAULT 0," +
                "bread INT DEFAULT 0," +
                "ironHelmet INT DEFAULT 0," +
                "ironChestplate INT DEFAULT 0," +
                "ironLeggings INT DEFAULT 0," +
                "ironBoots INT DEFAULT 0," +
                "ironSword INT DEFAULT 0," +
                "diamondHelmet INT DEFAULT 0," +
                "diamondChestplate INT DEFAULT 0," +
                "diamondLeggings INT DEFAULT 0," +
                "diamondBoots INT DEFAULT 0," +
                "diamondSword INT DEFAULT 0," +
                "heads INT DEFAULT 0" +
                ")";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
