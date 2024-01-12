package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class HomeViewController {

    @FXML
    private FlowPane paneFlowContent;

    public void initialize(){
        paneFlowContent.getChildren().add(createCard());
    }

    private AnchorPane createCard() {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(350, 300);
        card.setMaxSize(350,300);
        card.setBorder(new Border
                (new BorderStroke
                (new Color(0,0,0,1), BorderStrokeStyle.SOLID,null,new BorderWidths(1))));

        // Image
        Image image = new Image("assets/background_img.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(350);
        imageView.setFitHeight(200);
        AnchorPane.setTopAnchor(imageView, 0.0);
        AnchorPane.setLeftAnchor(imageView, 0.0);
        AnchorPane.setRightAnchor(imageView, 0.0);

        // Label 1
        Label label1 = new Label("Title");
        label1.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: green;");
        label1.setWrapText(true);
        AnchorPane.setTopAnchor(label1, 215.0);
        AnchorPane.setLeftAnchor(label1, 10.0);

        // Label 2
        Label label2 = new Label("Description");
        label2.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");
        label2.setWrapText(true);
        AnchorPane.setTopAnchor(label2, 240.0);
        AnchorPane.setLeftAnchor(label2, 10.0);

        // Add nodes to the card
        card.getChildren().addAll(imageView, label1, label2);

        return card;
    }
}
