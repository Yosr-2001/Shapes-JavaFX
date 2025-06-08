package org.example.projetshapes.Logging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseLogger implements LoggerStrategy{

    private static final String DB_URL = "jdbc:mysql://localhost:3306/masi"; // ou votre URL de DB
    private static final String INSERT_LOG_SQL = "INSERT INTO logs (message) VALUES (?)";

    public DatabaseLogger() {
        createLogTableIfNotExists();
    }

    @Override
    public void log(String message) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(INSERT_LOG_SQL)) {

            stmt.setString(1, message);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du log en base : " + e.getMessage());
        }
    }

    private void createLogTableIfNotExists() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                message TEXT NOT NULL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
            stmt.execute();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table logs : " + e.getMessage());
        }
    }
}
