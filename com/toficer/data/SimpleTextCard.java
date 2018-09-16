package com.toficer.data;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class SimpleTextCard extends Card {

    public SimpleTextCard(int _id, String question, String answerStringRepresentation, int deckid, String cardType) {
        super(_id, question, answerStringRepresentation, deckid, cardType);
    }

    @Override
    public VBox getQuestionBox(){
        VBox questionBox = new VBox();
        questionBox.setMaxWidth(750);
        questionBox.setAlignment(Pos.CENTER);
        Label questionLabel = new Label();
        questionLabel.getStyleClass().add("descriptionLabel");
        questionLabel.setWrapText(true);
        questionLabel.setText(getQuestionStringRepresentation());
        questionBox.getChildren().add(questionLabel);
        return questionBox;
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
        if(getQuestionStringRepresentation().length() > 30){
            return (" " + getQuestionStringRepresentation().substring(0, 26) + "...");
        }
        else return (" " + getQuestionStringRepresentation());
    }
}
