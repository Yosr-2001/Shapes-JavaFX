package org.example.projetshapes.DAO;

import org.example.projetshapes.entities.Dessin;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DessinDAO {
    private final String user = "root";
    private final String password = "";
    private final String url = "jdbc:mysql://localhost:3306/masi";


    public int saveDessin(Dessin dessin) throws SQLException {
        String sql = "INSERT INTO dessin (nom, date_creation, nb_shapes) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {  // <-- correction ici
            stmt.setString(1, dessin.getNom());
            stmt.setString(2, dessin.getDateCreation().toString());
            stmt.setInt(3, dessin.getNbShapes());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // retourne l'id généré
            }
        }
        return -1; // erreur ou pas d'id
    }

    public Dessin getDessinById(int id) throws SQLException {
        String sql = "SELECT * FROM dessin WHERE id_dessin = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {  // <-- correction ici
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Dessin(
                        rs.getInt("id_dessin"),
                        rs.getString("nom"),
                        LocalDateTime.parse(rs.getString("date_creation")),
                        rs.getInt("nb_shapes")
                );
            }
        }
        return null;
    }

    public void updateNbShapes(int currentDessinId, int shapesCount)throws SQLException {
        String sql = "UPDATE dessin SET nb_shapes = ? WHERE id_dessin = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {  // <-- correction ici
            stmt.setInt(1, shapesCount);      // Utilise shapesCount ici
                stmt.setInt(2, currentDessinId);  // Utilise currentDessinId ici

                int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException("Aucun dessin trouvé avec l'id : " + currentDessinId);
            }
        }
    }

    public List<Dessin> getAllDessins() throws SQLException {
        List<Dessin> dessins = new ArrayList<>();
        String sql = "SELECT * FROM dessin ORDER BY date_creation DESC";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Dessin d = new Dessin(
                        rs.getInt("id_dessin"),       // nom exact de la colonne id
                        rs.getString("nom"),
                        LocalDateTime.parse(rs.getString("date_creation"), formatter),
                        rs.getInt("nb_shapes")
                );
                dessins.add(d);
            }
        }
        return dessins;
    }

}
