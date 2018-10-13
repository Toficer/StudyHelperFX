package com.toficer.data;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SimpleTextCard extends Card {

    public SimpleTextCard(int _id, String question, String answerStringRepresentation, int deckid, String cardType) {
        super(_id, question, answerStringRepresentation, deckid, cardType);
    }

    @Override
    public HBox getQuestionBox(){
        HBox containerBox = new HBox();
        containerBox.setMinHeight(50);
        VBox stripeBox = new VBox();
        stripeBox.setPrefWidth(50);
        stripeBox.getStyleClass().add("QuestionStripeBox");
        containerBox.getChildren().add(stripeBox);
        VBox questionBox = new VBox();
        questionBox.setPadding(new Insets(5,5,5,5));
        questionBox.getStyleClass().add("QuestionBox");
        questionBox.setMaxWidth(750);
        questionBox.setAlignment(Pos.CENTER);
        Label questionLabel = new Label();
        questionLabel.getStyleClass().add("descriptionLabel");
        questionLabel.setWrapText(true);
        questionLabel.setText(getQuestionStringRepresentation());
        questionBox.getChildren().add(questionLabel);
        containerBox.getChildren().add(questionBox);
        return containerBox;
    }

    @Override
    public HBox getAnswerBox(){
        HBox containerBox = new HBox();
        containerBox.setMinHeight(50);
        VBox stripeBox = new VBox();
        stripeBox.setPrefWidth(50);
        stripeBox.getStyleClass().add("AnswerStripeBox");
        containerBox.getChildren().add(stripeBox);
        VBox answerBox = new VBox();
        answerBox.setPadding(new Insets(5,5,5,5));
        answerBox.getStyleClass().add("QuestionBox");
        answerBox.setMaxWidth(750);
        answerBox.setAlignment(Pos.CENTER);
        Label answerTextLabel = new Label();
        answerTextLabel.setWrapText(true);
        answerTextLabel.setText(getAnswerStringRepresentation());
        answerBox.getChildren().add(answerTextLabel);
        containerBox.getChildren().add(answerBox);
        return containerBox;
    }

    @Override
    public String toString(){
        if(getQuestionStringRepresentation().length() > 30){
            return (getQuestionStringRepresentation().substring(0, 26) + "...");
        }
        else return (getQuestionStringRepresentation());
    }
}
