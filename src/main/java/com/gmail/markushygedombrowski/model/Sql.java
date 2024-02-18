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
}
