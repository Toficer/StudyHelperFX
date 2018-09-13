package com.toficer.data;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SimpleTextCard extends Card {

    public SimpleTextCard(int _id, String question, String answerStringRepresentation, int deckid, String cardType) {
        super(_id, question, answerStringRepresentation, deckid, cardType);
    }

    @Override
    public VBox getAnswerBox(){
        VBox answerBox = new VBox();
        answerBox.setMaxWidth(750);
        answerBox.setAlignment(Pos.CENTER);
        Label answerTextLabel = new Label();
        answerTextLabel.setWrapText(true);
        answerTextLabel.setText(getAnswerStringRepresentation());
        answerBox.getChildren().add(answerTextLabel);
        return answerBox;
    }

    @Override
    public String toString(){
        if(getQuestion().length() > 30){
            return (" " + getQuestion().substring(0, 26) + "...");
        }
        else return (" " + getQuestion());
    }
}
