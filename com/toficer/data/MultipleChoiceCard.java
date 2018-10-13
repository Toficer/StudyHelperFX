package com.toficer.data;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MultipleChoiceCard extends Card {

    private ArrayList<RadioButton> answers;
    private Label questionLabel;
    private int correctAnswer;

    public MultipleChoiceCard(int _id, String question, String answerStringRepresentation, int deckid, String cardType) {
        super(_id, question, answerStringRepresentation, deckid, cardType);
        parseRepresentationStrings();
    }

    public ArrayList<RadioButton> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<RadioButton> answers) {
        this.answers = answers;
    }

    public Label getQuestionLabel() {
        return questionLabel;
    }

    public void setQuestionLabel(Label questionLabel) {
        this.questionLabel = questionLabel;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
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
        questionLabel.getStyleClass().add("descriptionLabel");
        questionLabel.setWrapText(true);
        questionBox.getChildren().add(questionLabel);
        ToggleGroup group = new ToggleGroup();
        VBox radioButtonBox = new VBox();
        radioButtonBox.setMaxWidth(500);
        radioButtonBox.setAlignment(Pos.TOP_LEFT);
        for(RadioButton b : answers){
            b.setToggleGroup(group);
            b.wrapTextProperty().set(true);
            radioButtonBox.getChildren().add(b);
        }
        questionBox.getChildren().add(radioButtonBox);
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
        if(answers.get(correctAnswer).isSelected()){
            Label correctLabel = new Label("CORRECT");
            correctLabel.setStyle("-fx-text-fill: green; -fx-font-size: 25;");
            answerBox.getChildren().add(correctLabel);
        }
        else {
            Label incorrectLabel = new Label("INCORRECT");
            incorrectLabel.setStyle("-fx-text-fill: red; -fx-font-size: 25;");
            Label correctLabel = new Label("CORRECT ANSWER: " + answers.get(correctAnswer).getText());
            answerBox.getChildren().add(incorrectLabel);
            answerBox.getChildren().add(correctLabel);
        }
        containerBox.getChildren().add(answerBox);
        return containerBox;
    }

    @Override
    public VBox getDescriptionBox(){

        VBox descriptionBox = new VBox();
        descriptionBox.setAlignment(Pos.TOP_CENTER);
        descriptionBox.getChildren().add(getQuestionBox());
        descriptionBox.setSpacing(10);
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
        Label correctLabel = new Label("CORRECT ANSWER: " + answers.get(correctAnswer).getText());
        answerBox.getChildren().add(correctLabel);
        containerBox.getChildren().add(answerBox);
        descriptionBox.getChildren().add(containerBox);

        return descriptionBox;
    }

    public void parseRepresentationStrings(){

        String questionParts[] = getQuestionStringRepresentation().split(Pattern.quote("[:]"));
        questionLabel = new Label();
        answers = new ArrayList<>();
        questionLabel.setText(questionParts[0]);
        for(int i=1; i<questionParts.length; i++){
            answers.add(new RadioButton(questionParts[i]));
        }

        correctAnswer = Integer.parseInt(getAnswerStringRepresentation());

    }

    @Override
    public String toString(){
        if(questionLabel.getText().length() > 30){
            return (questionLabel.getText().substring(0, 26) + "...");
        }
        else return (questionLabel.getText());
    }
}
