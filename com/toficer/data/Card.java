package com.toficer.data;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public abstract class Card {
    private int _id;
    private String questionStringRepresentation;
    private String answerStringRepresentation;
    private int deckid;
    private String cardType;
    private static Image cardImage;

    public Card() {
        if(cardImage == null){
            cardImage = new Image("images/file.png");
        }
    }

    public Card(int _id, String questionStringRepresentation, String answerStringRepresentation, int deckid, String cardType) {
        this._id = _id;
        this.questionStringRepresentation = questionStringRepresentation;
        this.answerStringRepresentation = answerStringRepresentation;
        this.deckid = deckid;
        this.cardType = cardType;
        if(cardImage == null){
            cardImage = new Image("images/file.png");
        }
    }

    public VBox getDescriptionBox(){

        VBox descriptionBox = new VBox();
        descriptionBox.setAlignment(Pos.TOP_CENTER);
        descriptionBox.getChildren().add(new ImageView(cardImage));
        Label nameLabel = new Label(getQuestionStringRepresentation());
        nameLabel.setWrapText(true);
        nameLabel.getStyleClass().add("descriptionLabel");
        descriptionBox.getChildren().add(nameLabel);
        descriptionBox.getChildren().add(getAnswerBox());

        return descriptionBox;
    }

    public abstract VBox getQuestionBox();
    public abstract VBox getAnswerBox();

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getQuestionStringRepresentation() {
        return questionStringRepresentation;
    }

    public void setQuestionStringRepresentation(String questionStringRepresentation) {
        this.questionStringRepresentation = questionStringRepresentation;
    }

    public String getAnswerStringRepresentation() {
        return answerStringRepresentation;
    }

    public void setAnswerStringRepresentation(String answer) {
        this.answerStringRepresentation = answer;
    }

    public int getDeckid() {
        return deckid;
    }

    public void setDeckid(int deckid) {
        this.deckid = deckid;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public static Image getCardImage() {
        return cardImage;
    }

    public static void setCardImage(Image cardImage) {
        Card.cardImage = cardImage;
    }
}
