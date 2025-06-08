package org.example.projetshapes;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.example.projetshapes.DAO.DessinDAO;
import org.example.projetshapes.DAO.ShapeDAO;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.example.projetshapes.Decorator.ColorDecorator;
import org.example.projetshapes.Strategy.CircleShape;
import org.example.projetshapes.Strategy.RectangleShape;
import org.example.projetshapes.Strategy.ShapeDraw;
import org.example.projetshapes.Factory.ShapeFactory;
import org.example.projetshapes.entities.Dessin;
import org.example.projetshapes.entities.Shape;

import org.example.projetshapes.Logging.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelloController {

    @FXML private Label circleLabel;
    @FXML private Label rectLabel;
    @FXML private Canvas canvas;

    @FXML
    private ChoiceBox<String> loggerChoiceBox;

    private final ContextLogger logger = new ContextLogger();

    private final ShapeDAO shapeDAO = new ShapeDAO();
    private final DessinDAO dessinDAO = new DessinDAO();

    private int currentDessinId = -1;
    private int shapesCount = 0;
    @FXML
    private Button openButton;

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
        try {

            Dessin dessin = new Dessin();
            String uniqueName = "Dessin de " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH'h'mm'm'ss"));
            dessin.setNom(uniqueName);
            dessin.setDateCreation(LocalDateTime.now());
            dessin.setNbShapes(0);
            currentDessinId = dessinDAO.saveDessin(dessin);

        } catch (Exception e) {
            e.printStackTrace();
            logger.log("Erreur lors de la création du dessin : " + e.getMessage());
        }
        setupDragEvents();
        openButton.setOnAction(e -> {
            try {
                List<Dessin> dessins = dessinDAO.getAllDessins();
                if (dessins.isEmpty()) {
                    logger.log("Aucun dessin trouvé.");
                    return;
                }

                List<String> nomsDessins = dessins.stream()
                        .map(Dessin::getNom)
                        .collect(Collectors.toList());

                ChoiceDialog<String> dialog = new ChoiceDialog<>(nomsDessins.get(0), nomsDessins);
                dialog.setTitle("Ouvrir un dessin");
                dialog.setHeaderText("Sélectionnez un dessin à ouvrir");
                dialog.setContentText("Dessins disponibles :");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(nomSelectionne -> {
                    try {
                        Dessin selectionne = dessins.stream()
                                .filter(d -> d.getNom().equals(nomSelectionne))
                                .findFirst().orElse(null);

                        if (selectionne != null) {
                            currentDessinId = selectionne.getIdDessin();
                            shapesCount = selectionne.getNbShapes();

                            GraphicsContext gc = canvas.getGraphicsContext2D();
                            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                            loadSavedShapesForDessin(currentDessinId);
                            logger.log("Dessin '" + nomSelectionne + "' chargé.");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        logger.log("Erreur lors du chargement du dessin: " + ex.getMessage());
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.log("Erreur lors de la récupération des dessins : " + ex.getMessage());
            }
        });

    }
    private void loadSavedShapesForDessin(int dessinId) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        List<Shape> shapes = shapeDAO.getShapesByDessinId(dessinId);
        for (Shape s : shapes) {
            ShapeDraw shapeDraw = ShapeFactory.createShape(s.getType(), s.getX(), s.getY());
            shapeDraw.draw(gc);
            logger.log("Forme dessinée depuis la BDD : " + s);
        }
    }

    private void setupDragEvents() {
        for (Label label : new Label[]{ rectLabel ,circleLabel}) {
            label.setOnDragDetected(e -> {
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

                    String shapeType = type.equals("⬛ Rectangle") ? "rectangle" : "circle";
                    Shape shapeToSave = new Shape(shapeType, x, y, currentDessinId);

                    try {
                        shapeDAO.saveShape(shapeToSave);
                        shapesCount++;
                        dessinDAO.updateNbShapes(currentDessinId, shapesCount);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        logger.log("Erreur lors de la sauvegarde de la forme : " + ex.getMessage());
                    }

                    success = true;
                }
            }
            e.setDropCompleted(success);
            e.consume();
        });
    }

//    private void loadSavedShapes() {
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        List<Shape> shapes = shapeDAO.getAllShapes();
//        for (Shape s : shapes) {
//            ShapeDraw shapeDraw = ShapeFactory.createShape(s.getType(), s.getX(), s.getY());
//            shapeDraw.draw(gc);
//            logger.log("Forme dessinée depuis la BDD : " + s);
//
//        }
//
//    }

}