package com.nhekfqn.sample.server.persistense;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class PingDao {

    private static final Logger logger = LoggerFactory.getLogger(PingDao.class);

    private final String jdbcUrl;
    private final String jdbcUsername;
    private final String jdbcPassword;

    @Inject
    public PingDao(@Named("jdbc.url") String jdbcUrl, @Named("jdbc.username") String jdbcUsername, @Named("jdbc.password") String jdbcPassword) throws SQLException {
        this.jdbcUrl = jdbcUrl;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;

        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword)) {
            try (Statement createTableStatement = connection.createStatement()) {
                createTableStatement.executeUpdate("create table if not exists ping (userId varchar(20) primary key, n int)");
            }
        }
    }

    public int incrementPingN(String userId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword)) {
            connection.setAutoCommit(false);

            int n = 0;
            try (PreparedStatement selectNPreparedStatement = connection.prepareStatement("select n from ping where userId = ?")) {
                selectNPreparedStatement.setString(1, userId);
                try (ResultSet resultSet = selectNPreparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        n = resultSet.getInt(1);
                    }
                }
            }

            n++;

            if (n == 1) {
                try (PreparedStatement insertNPreparedStatement = connection.prepareStatement("insert into ping (userId, n) values (?, ?)")) {
                    insertNPreparedStatement.setString(1, userId);
                    insertNPreparedStatement.setInt(2, n);

                    insertNPreparedStatement.execute();
                }
            } else {
                try (PreparedStatement updateNPreparedStatement = connection.prepareStatement("update ping set n = ? where userId = ?")) {
                    updateNPreparedStatement.setInt(1, n);
                    updateNPreparedStatement.setString(2, userId);

                    updateNPreparedStatement.execute();
                }
            }

            connection.commit();

            return n;
        }
    }

}
