package com.toficer.data;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Deck implements DisplayableInListView{
    private int _id;
    private String name;
    private String description;
    private int folderId;
    private VBox descriptionBox;
    private static Image deckImage;

    public Deck(int _id, String name, String description, int folderId) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.folderId = folderId;
        if(deckImage == null){
            deckImage = new Image("images/deck.png");
        }
        createDescriptionBox();
    }

    public Deck() {
        if(deckImage == null){
            deckImage = new Image("images/deck.png");
        }
        createDescriptionBox();
    }

    public void createDescriptionBox(){
        descriptionBox = new VBox();
        descriptionBox.setAlignment(Pos.TOP_CENTER);
        descriptionBox.setSpacing(10);

        HBox containerBox = new HBox();
        containerBox.setMinHeight(50);

        VBox stripeBox = new VBox();
        stripeBox.setPrefWidth(50);
        stripeBox.getStyleClass().add("DeckStripeBox");
        containerBox.getChildren().add(stripeBox);

        VBox contentBox = new VBox();
        contentBox.setPadding(new Insets(5,5,5,5));
        contentBox.getStyleClass().add("QuestionBox");
        contentBox.setMaxWidth(750);
        contentBox.setAlignment(Pos.TOP_LEFT);

        Label nameLabel = new Label(toString());
        nameLabel.getStyleClass().add("titleLabel");
        contentBox.getChildren().add(nameLabel);

        Label descriptionLabel = new Label(getDescription());
        descriptionLabel.getStyleClass().add("descriptionLabel");
        descriptionLabel.setWrapText(true);
        contentBox.getChildren().add(descriptionLabel);

        containerBox.getChildren().add(contentBox);
        descriptionBox.getChildren().add(containerBox);
    }

    public VBox getDescriptionBox(){
        return descriptionBox;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    @Override
    public String toString(){
        return name.toUpperCase();
    }
}
