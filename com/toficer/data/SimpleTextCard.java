package com.toficer.data;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SimpleTextCard extends Card {

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
}
