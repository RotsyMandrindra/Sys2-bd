package bdsys;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.*;
import java.util.UUID;

public class Application {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/sysdb";
    private static final String DB_USER = "rotsy";
    private static final String DB_PASSWORD = "rotsy123";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar sys2db.jar <command> [<params>]");
            return;
        }

        String command = args[0];

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            if ("newconnection".equals(command)) {
                if (args.length < 3) {
                    System.out.println("Usage: java -jar sys2db.jar newconnection <firstname>");
                    return;
                }
                String firstName = args[1];
                insertNewConnection(connection, firstName);
            } else if ("readconnections".equals(command)) {
                int limit = args.length >= 2 ? Integer.parseInt(args[1]) : 5;
                readConnections(connection, limit);
            } else {
                System.out.println("Unknown command: " + command);
            }

            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void insertNewConnection(Connection connection, String firstName) throws SQLException {
        String sql = "INSERT INTO connection (id, firstname, connection_datetime) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            UUID id = UUID.randomUUID();
            Timestamp connectionDatetime = new Timestamp(System.currentTimeMillis());
            statement.setObject(1, id);
            statement.setString(2, firstName);
            statement.setTimestamp(3, connectionDatetime);
            statement.executeUpdate();
            System.out.println("New connection inserted.");
        }
    }

    private static void readConnections(Connection connection, int limit) throws SQLException {
        String sql = "SELECT * FROM connection ORDER BY connection_datetime DESC LIMIT ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UUID id = (UUID) resultSet.getObject("id");
                String firstName = resultSet.getString("firstname");
                Timestamp connectionDatetime = resultSet.getTimestamp("connection_datetime");
                System.out.println(id + " | " + firstName + " | " + connectionDatetime);
            }
        }
    }
}

