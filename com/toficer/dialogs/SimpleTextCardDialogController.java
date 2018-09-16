package com.toficer.dialogs;

import com.toficer.data.*;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SimpleTextCardDialogController {
    @FXML
    TextField questionField;
    @FXML
    TextArea answerArea;
    @FXML
    ChoiceBox<Folder> folderSelector;
    @FXML
    ChoiceBox<Deck> deckSelector;
    @FXML
    Label emptyWarningLabel;
    @FXML
    Label warningLabel;

    @FXML
    public void initialize(){
        folderSelector.getItems().setAll(DataModel.getData().getListViewFolders());
    }

    public void populateDeckSelector(){
        if(folderSelector.getSelectionModel().getSelectedItem() != null){
            int folder_id = folderSelector.getSelectionModel().getSelectedItem().get_id();
            deckSelector.getItems().setAll(DataModel.getData().getDeckList(folder_id));
            if(deckSelector.getItems().size() == 0){
                emptyWarningLabel.setVisible(true);
                //questionField.setText("");
                questionField.setDisable(true);
                //answerArea.setText("");
                answerArea.setDisable(true);
            }
            else{
                emptyWarningLabel.setVisible(false);
                //questionField.setText("");
                questionField.setDisable(false);
                //answerArea.setText("");
                answerArea.setDisable(false);
            }
        }
    }

    public void createCard(){
        Card card = new SimpleTextCard(0, questionField.getText(), answerArea.getText(), deckSelector.getSelectionModel().getSelectedItem().get_id(), DataModel.TYPE_SIMPLETEXT);
        DataModel.getData().addCard(card);
    }

    public void loadData(Card card){
        selectDeck(DataModel.getData().getCurrentDeck());
        questionField.setText(card.getQuestionStringRepresentation());
        answerArea.setText(card.getAnswerStringRepresentation());
    }

    public void updateCard(Card card){
        card.setQuestionStringRepresentation(questionField.getText());
        card.setAnswerStringRepresentation(answerArea.getText());
        card.setDeckid(deckSelector.getSelectionModel().getSelectedItem().get_id());
        //card.createDescriptionBox();
        DataModel.getData().updateCard(card);
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

    public Boolean validateInput(){
        if(questionField.getText().length()==0 || questionField.getText().contains("'") || questionField.getText().contains("\\")
            || answerArea.getText().length()==0 || answerArea.getText().contains("'") || answerArea.getText().contains("\\")
            || folderSelector.getSelectionModel().getSelectedItem() == null
            || deckSelector.getSelectionModel().getSelectedItem() == null){
            warningLabel.setVisible(true);
            return false;
        }
        else return true;
    }
}
