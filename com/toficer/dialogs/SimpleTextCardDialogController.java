package com.toficer.dialogs;

import com.toficer.data.*;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
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
    public void initialize(){
        folderSelector.getItems().setAll(DataModel.getData().getListViewFolders());
    }

    public void populateDeckSelector(){
        if(folderSelector.getSelectionModel().getSelectedItem() != null){
            int folder_id = folderSelector.getSelectionModel().getSelectedItem().get_id();
            deckSelector.getItems().setAll(DataModel.getData().getDeckList(folder_id));
        }
    }

    public void createCard(){
        Card card = new SimpleTextCard();
        card.setAnswerStatus(false);
        card.setQuestion(questionField.getText());
        card.setAnswerStringRepresentation(answerArea.getText());
        card.setDeckid(deckSelector.getSelectionModel().getSelectedItem().get_id());
        card.setCardType(DataModel.TYPE_SIMPLETEXT);
        DataModel.getData().addCard(card);
    }

    public void selectFolder(Folder folder){
        //folderSelector.getItems().setAll(DataModel.getData().getListViewFolders());
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
