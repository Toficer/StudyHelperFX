package com.toficer.data;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public abstract class Card {
    private int _id;
    private String question;
    private String answerStringRepresentation;
    private int deckid;
    private String cardType;
    private VBox descriptionBox;
    private static Image cardImage;

    public Card() {
        if(cardImage == null){
            cardImage = new Image("images/file.png");
        }
        createDescriptionBox();
    }

    public Card(int _id, String question, String answerStringRepresentation, int deckid, String cardType) {
        this._id = _id;
        this.question = question;
        this.answerStringRepresentation = answerStringRepresentation;
        this.deckid = deckid;
        this.cardType = cardType;
        if(cardImage == null){
            cardImage = new Image("images/file.png");
        }
        createDescriptionBox();
    }

    public void createDescriptionBox(){
        descriptionBox = new VBox();
        descriptionBox.setAlignment(Pos.TOP_CENTER);
        descriptionBox.getChildren().add(new ImageView(cardImage));
        Label nameLabel = new Label(getQuestion());
        nameLabel.setWrapText(true);
        nameLabel.getStyleClass().add("descriptionLabel");
        descriptionBox.getChildren().add(nameLabel);
        descriptionBox.getChildren().add(getAnswerBox());
    }

    public VBox getDescriptionBox(){
        return descriptionBox;
    }

    public abstract VBox getAnswerBox();

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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
}
