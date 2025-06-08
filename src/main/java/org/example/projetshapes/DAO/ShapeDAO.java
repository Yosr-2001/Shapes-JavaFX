package org.example.projetshapes.DAO;

import org.example.projetshapes.Logging.Logger;
import org.example.projetshapes.entities.Shape;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShapeDAO {
    private final String url = "jdbc:mysql://localhost:3306/masi";
    private final String user = "root";
    private final String password = "";

    public void saveShape(Shape shape) {
        String sql = "INSERT INTO shapes (type, x, y) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, shape.getType());
            stmt.setDouble(2, shape.getX());
            stmt.setDouble(3, shape.getY());
            stmt.executeUpdate();

            System.out.println("✅ Forme enregistrée dans la base.");
            Logger.getInstance().log("Forme sauvegardée avec succès : " + shape);
        } catch (SQLException ex) {
            System.out.println("Erreur base : " + ex.getMessage());
        }
    }
    public List<Shape> getAllShapes() {
        List<Shape> list = new ArrayList<>();
        String sql = "SELECT type, x, y FROM shapes";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String type = rs.getString("type");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                list.add(new Shape(type, x, y));
            }
            Logger.getInstance().log("Nombre de formes récupérées : " + list.size());

        } catch (SQLException e) {
            Logger.getInstance().log("Erreur getAllShapes : " + e.getMessage());

          //  System.out.println("Erreur getAllShapes : " + e.getMessage());
        }
        return list;
    }


}
