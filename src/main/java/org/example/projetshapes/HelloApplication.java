package org.example.projetshapes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.projetshapes.DAO.ShapeDAO;
import org.example.projetshapes.Selection.ShapeDraw;
import org.example.projetshapes.Selection.ShapeFactory;
import org.example.projetshapes.entities.Shape;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    private final Canvas canvas = new Canvas(600, 400);
    private final ShapeDAO shapeDAO = new ShapeDAO();


//    public void start(Stage stage) throws IOException {
//
//        /**/
//        VBox sidebar = new VBox(10);
//        sidebar.setPadding(new Insets(10));
//        sidebar.setPrefWidth(120);
//        sidebar.setStyle("-fx-background-color: #eeeeee;");
//
//        Label rectLabel = new Label("⬛ Rectangle");
//        Label circleLabel = new Label("⚪ Cercle");
//
//        Button showShapesButton=new Button("Afficher toutes les formes");
//        showShapesButton.setWrapText(true);
//        showShapesButton.setOnAction(e -> {
//            GraphicsContext gc = canvas.getGraphicsContext2D();
//            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//            loadSavedShapes();
//        });
//        for (Label label : new Label[]{rectLabel, circleLabel}) {
//            label.setOnDragDetected(e -> {
//                Dragboard db = label.startDragAndDrop(TransferMode.COPY);
//                ClipboardContent content = new ClipboardContent();
//                content.putString(label.getText());
//                db.setContent(content);
//                e.consume();
//            });
//        }
//        sidebar.getChildren().addAll(rectLabel, circleLabel, showShapesButton);
//
//        /**/
//        canvas.setOnDragOver(e -> {
//            if (e.getGestureSource() != canvas && e.getDragboard().hasString()) {
//                e.acceptTransferModes(TransferMode.COPY);
//            }
//            e.consume();
//        });
//
//        canvas.setOnDragDropped(e -> {
//            Dragboard db = e.getDragboard();
//            boolean success = false;
//
//            if (db.hasString()) {
//                String type = db.getString();
//                GraphicsContext gc = canvas.getGraphicsContext2D();
//
//                double x = e.getX();
//                double y = e.getY();
//
//                switch (type) {
//                    case "⬛ Rectangle" -> gc.strokeRect(x, y, 100, 60);
//                    case "⚪ Cercle" -> gc.strokeOval(x, y, 60, 60);
//                }
//
//                String shapeType = type.equals("⬛ Rectangle") ? "rectangle" : "circle";
//                Shape shape = new Shape(shapeType, x, y);
//                shapeDAO.saveShape(shape);
//
//                success = true;
//            }
//
//            e.setDropCompleted(success);
//            e.consume();
//        });
//        BorderPane root = new BorderPane();
//        root.setLeft(sidebar);
//        root.setCenter(canvas);
//
//      // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//       // Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        Scene scene = new Scene(root, 320, 240);
//        stage.setTitle("Shaspes!");
//        stage.setScene(scene);
//        stage.show();
//    }
//    private void loadSavedShapes(){
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        List<Shape> shapes = shapeDAO.getAllShapes();
//        for(Shape s:shapes){
//            System.out .println("TYPE : "+s.getType() + " | x: " + s.getX() + " | y: " + s.getY());
//            ShapeDraw shapeDraw = ShapeFactory.createShape(s.getType(), s.getX(), s.getY());
//            shapeDraw.draw(gc);
//        }
//    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Shapes!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}