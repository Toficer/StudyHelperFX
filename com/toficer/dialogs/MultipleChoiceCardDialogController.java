package com.toficer.dialogs;

import com.toficer.data.*;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class MultipleChoiceCardDialogController {

    @FXML
    ChoiceBox<Folder> folderSelector;
    @FXML
    ChoiceBox<Deck> deckSelector;
    @FXML
    Label emptyWarningLabel;
    @FXML
    Label warningLabel;
    @FXML
    TextField questionField;
    @FXML
    TextField answerFieldA;
    @FXML
    TextField answerFieldB;
    @FXML
    TextField answerFieldC;
    @FXML
    TextField answerFieldD;
    @FXML
    RadioButton boxA;
    @FXML
    RadioButton boxB;
    @FXML
    RadioButton boxC;
    @FXML
    RadioButton boxD;

    @FXML
    public void initialize(){
        folderSelector.getItems().setAll(DataModel.getData().getListViewFolders());
        answerFieldA.textProperty().addListener((observable, oldValue, newValue) -> {
            setFieldDisableStatus();
        });
        answerFieldB.textProperty().addListener((observable, oldValue, newValue) -> {
            setFieldDisableStatus();
        });
        answerFieldC.textProperty().addListener((observable, oldValue, newValue) -> {
            setFieldDisableStatus();
        });
        answerFieldD.textProperty().addListener((observable, oldValue, newValue) -> {
            setFieldDisableStatus();
        });
    }

    public void populateDeckSelector(){
        if(folderSelector.getSelectionModel().getSelectedItem() != null){
            int folder_id = folderSelector.getSelectionModel().getSelectedItem().get_id();
            deckSelector.getItems().setAll(DataModel.getData().getDeckList(folder_id));
            if(deckSelector.getItems().size() == 0){
                emptyWarningLabel.setVisible(true);
//                //questionField.setText("");
//                questionField.setDisable(true);
//                //answerArea.setText("");
//                answerArea.setDisable(true);
            }
            else{
                emptyWarningLabel.setVisible(false);
//                //questionField.setText("");
//                questionField.setDisable(false);
//                //answerArea.setText("");
//                answerArea.setDisable(false);
            }
        }
    }

    public Card buildCard(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(questionField.getText() + "[:]");
        if(answerFieldA.getText().length() != 0 && !answerFieldA.isDisable()){
            stringBuilder.append(answerFieldA.getText());
            if(answerFieldB.getText().length() != 0 && !answerFieldB.isDisable()){
                stringBuilder.append("[:]" + answerFieldB.getText());
                if(answerFieldC.getText().length() != 0 && !answerFieldC.isDisable()){
                    stringBuilder.append("[:]" + answerFieldC.getText());
                    if(answerFieldD.getText().length() != 0 && !answerFieldD.isDisable()){
                        stringBuilder.append("[:]" + answerFieldD.getText());
                    }
                }
            }
        }
        int correctAnswer;
        if(boxA.isSelected()){
            correctAnswer = 0;
        }
        else if(boxB.isSelected()){
            correctAnswer = 1;
        }
        else if(boxC.isSelected()){
            correctAnswer = 2;
        }
        else if(boxD.isSelected()){
            correctAnswer = 3;
        }
        else return null;

        Card card = new MultipleChoiceCard(0, stringBuilder.toString(), Integer.toString(correctAnswer), deckSelector.getSelectionModel().getSelectedItem().get_id(), DataModel.TYPE_MULTIPLECHOICE);
        return card;
    }

    public void createCard(){
        Card card = buildCard();
        if(card != null){
            DataModel.getData().addCard(card);
        }
    }

    public void updateCard(Card card){

        Card tempcard = buildCard();
        card.setQuestionStringRepresentation(tempcard.getQuestionStringRepresentation());
        card.setAnswerStringRepresentation(tempcard.getAnswerStringRepresentation());
        card.setDeckid(tempcard.getDeckid());
        card.setCardType(tempcard.getCardType());

        DataModel.getData().updateCard(card);
    }

    public void loadData(Card card){
        selectDeck(DataModel.getData().getCurrentDeck());
        String questionParts[] = card.getQuestionStringRepresentation().split(Pattern.quote("[:]"));
        questionField.setText(questionParts[0]);
        int i=1;
        System.out.println(questionParts.length);
        System.out.println(card.getAnswerStringRepresentation());
        if(i<questionParts.length){
            answerFieldA.setText(questionParts[i]);
            if(card.getAnswerStringRepresentation().equals("0")){
                boxA.setSelected(true);
            }
            i++;
        }
        if(i<questionParts.length){
            answerFieldB.setText(questionParts[i]);
            if(card.getAnswerStringRepresentation().equals("1")){
                boxB.setSelected(true);
            }
            i++;
        }
        if(i<questionParts.length){
            answerFieldC.setText(questionParts[i]);
            if(card.getAnswerStringRepresentation().equals("2")){
                boxC.setSelected(true);
            }
            i++;
        }
        if(i<questionParts.length){
            answerFieldD.setText(questionParts[i]);
            if(card.getAnswerStringRepresentation().equals("3")){
                boxD.setSelected(true);
            }
        }
        setFieldDisableStatus();
    }

    @FXML
    public void setFieldDisableStatus(){
        if(answerFieldA.getText().length()!=0){
            answerFieldB.setDisable(false);
            boxA.setDisable(false);
            if(answerFieldB.getText().length()!=0){
                answerFieldC.setDisable(false);
                boxB.setDisable(false);
                if(answerFieldC.getText().length()!=0){
                    answerFieldD.setDisable(false);
                    boxC.setDisable(false);
                    if(answerFieldD.getText().length()!=0){
                        boxD.setDisable(false);
                    }
                    else boxD.setDisable(true);
                }
                else {
                    answerFieldD.setDisable(true);
                    boxC.setDisable(true);
                    boxD.setDisable(true);
                }
            }
            else {
                answerFieldC.setDisable(true);
                answerFieldD.setDisable(true);
                boxB.setDisable(true);
                boxC.setDisable(true);
                boxD.setDisable(true);
            }
        }
        else {
        answerFieldB.setDisable(true);
        answerFieldC.setDisable(true);
        answerFieldD.setDisable(true);
        boxA.setDisable(true);
        boxB.setDisable(true);
        boxC.setDisable(true);
        boxD.setDisable(true);
        }
    }

    public Boolean validateInput(){
        if(questionField.getText().length()==0 || questionField.getText().contains("'") || questionField.getText().contains("\\") || questionField.getText().contains("[:]")
                || ((answerFieldA.getText().length()==0 || answerFieldA.getText().contains("'") || answerFieldA.getText().contains("\\") || answerFieldA.getText().contains("[:]")) && (!boxA.isDisable()))
                || ((answerFieldB.getText().length()==0 || answerFieldB.getText().contains("'") || answerFieldB.getText().contains("\\") || answerFieldB.getText().contains("[:]")) && (!boxB.isDisable()))
                || ((answerFieldC.getText().length()==0 || answerFieldC.getText().contains("'") || answerFieldC.getText().contains("\\") || answerFieldC.getText().contains("[:]")) && (!boxC.isDisable()))
                || ((answerFieldD.getText().length()==0 || answerFieldD.getText().contains("'") || answerFieldD.getText().contains("\\") || answerFieldD.getText().contains("[:]")) && (!boxD.isDisable()))
                || !( (boxA.isSelected() && !boxA.isDisable()) || (boxB.isSelected() && !boxB.isDisable())
                || (boxC.isSelected() && !boxC.isDisable()) || (boxD.isSelected() && !boxD.isDisable()))
                || folderSelector.getSelectionModel().getSelectedItem() == null
                || deckSelector.getSelectionModel().getSelectedItem() == null){
            warningLabel.setVisible(true);
            return false;
        }
        else return true;
    }

    public void selectFolder(Folder folder){
        folderSelector.getSelectionModel().select(folder);
        populateDeckSelector();
    }

    public void selectDeck(Deck deck){
        selectFolder(DataModel.getData().getCurrentFolder());

        for (Deck d : deckSelector.getItems()){
            if(d.get_id() == deck.get_id()){
                deckSelector.getSelectionModel().select(d);
            }
        }
    }

}
