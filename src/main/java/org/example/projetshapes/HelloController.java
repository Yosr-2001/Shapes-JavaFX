package org.example.projetshapes;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.example.projetshapes.DAO.ShapeDAO;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.example.projetshapes.Decorator.ColorDecorator;
import org.example.projetshapes.Logging.Logger;
import org.example.projetshapes.Selection.CircleShape;
import org.example.projetshapes.Selection.RectangleShape;
import org.example.projetshapes.Selection.ShapeDraw;
import org.example.projetshapes.Selection.ShapeFactory;
import org.example.projetshapes.entities.Shape;

import org.example.projetshapes.Logging.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML private Label circleLabel;
    @FXML private Label rectLabel;
    @FXML private Button showShapesButton;
    @FXML private Canvas canvas;
/*Logger*/
@FXML
private ChoiceBox<String> loggerChoiceBox;

    private final ContextLogger logger = new ContextLogger();
    /**/
    private final ShapeDAO shapeDAO = new ShapeDAO();
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML private ColorPicker colorPicker;


    @FXML
    public void initialize() {

        loggerChoiceBox.getItems().addAll("Console Logger", "File Logger");
        loggerChoiceBox.setValue("Console Logger");
        logger.setStrategy(new ConsoleLogger());

        loggerChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            switch (newVal) {
                case "Console Logger" -> logger.setStrategy(new ConsoleLogger());
                case "File Logger" -> logger.setStrategy(new FileLogger());
            }
            logger.log("Stratégie de logger changée en : " + newVal);
        });

        setupDragEvents();
        showShapesButton.setOnAction(e -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            loadSavedShapes();
        });
    }

    private void setupDragEvents() {
        for (Label label : new Label[]{ rectLabel ,circleLabel}) {
            label.setOnDragDetected(e -> {
                String labelText = label.getText();
                                Dragboard db = label.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                content.putString(label.getText());
                db.setContent(content);
                e.consume();
            });
        }

        canvas.setOnDragOver(e -> {
            if (e.getGestureSource() != canvas && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.COPY);
            }
            e.consume();
        });

        canvas.setOnDragDropped(e -> {
             Dragboard db = e.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String type = db.getString();

                GraphicsContext gc = canvas.getGraphicsContext2D();

                double x = e.getX();
                double y = e.getY();
                Color selectedColor = colorPicker.getValue();
                ShapeDraw baseShape = switch (type) {
                    case "⬛ Rectangle" -> new RectangleShape(x, y);
                    case "⚪ Cercle" -> new CircleShape(x, y);
                    default -> null;
                };

                if (baseShape != null) {
                    ShapeDraw coloredShape = new ColorDecorator(baseShape, selectedColor);
                    coloredShape.draw(gc);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    String timestamp = LocalDateTime.now().format(formatter);

                     String hexColor = String.format("#%02X%02X%02X",
                            (int)(selectedColor.getRed()*255),
                            (int)(selectedColor.getGreen()*255),
                            (int)(selectedColor.getBlue()*255));

                    logger.log("[" + timestamp + "] " + type + " dessiné aux coordonnées : (x=" + x + ", y=" + y +  "), couleur : " + hexColor);

                    // Persist shape (without color, unless extended)
                    String shapeType = type.equals("⬛ Rectangle") ? "rectangle" : "circle";
                    shapeDAO.saveShape(new Shape(shapeType, x, y));

                    success = true;
                }
            }
//
//                switch (type) {
//                    case "⬛ Rectangle" ->{
//                        gc.strokeRect(x, y, 100, 60);
//                        //logger.log(LocalDateTime.now()+"Dessin du rectangle aux coordonnées : (" + x + ", " + y + ")");
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//                        String timestamp = LocalDateTime.now().format(formatter);
//                        logger.log("[" + timestamp + "] Rectangle dessiné aux coordonnées : (x=" + x + ", y=" + y + ")");
//
//                    }
//                    case "⚪ Cercle" ->{
//                        gc.strokeOval(x, y, 60, 60);
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//                        String timestamp = LocalDateTime.now().format(formatter);
//                       logger.log("[" + timestamp + "] cercle dessiné aux coordonnées : (" + x + ", " + y + ")");
//
//                    }
//                }
//
//                String shapeType = type.equals("⬛ Rectangle") ? "rectangle" : "circle";
//                Shape shape = new Shape(shapeType, x, y);
//                shapeDAO.saveShape(shape);
//
//                success = true;
//            }

            e.setDropCompleted(success);
            e.consume();
        });
    }

    private void loadSavedShapes() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        List<Shape> shapes = shapeDAO.getAllShapes();
        for (Shape s : shapes) {
            ShapeDraw shapeDraw = ShapeFactory.createShape(s.getType(), s.getX(), s.getY());
            shapeDraw.draw(gc);
            logger.log("Forme dessinée depuis la BDD : " + s);

        }

    }

}