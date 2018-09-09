package com.toficer.data;

import javafx.scene.layout.VBox;

public abstract class Card {
    private int _id;
    private String question;
    private String answerStringRepresentation;
    private boolean answerStatus;
    private int deckid;
    private String cardType;

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

    public boolean getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(boolean answerStatus) {
        this.answerStatus = answerStatus;
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
