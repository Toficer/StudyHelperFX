package com.toficer.dialogs;

import com.toficer.data.DataModel;
import com.toficer.data.Deck;
import com.toficer.data.Folder;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DeckDialogController {

    @FXML
    TextField deckField;
    @FXML
    TextField descriptionField;
    @FXML
    ChoiceBox<Folder> folderSelector;
    @FXML
    Label warningLabel;

    @FXML
    public void initialize(){
        folderSelector.getItems().setAll(DataModel.getData().getListViewFolders());
    }

    public Deck createDeck() {
        if(folderSelector.getSelectionModel().getSelectedItem() == null){
            return null;
        }
        else {
            Deck deck = new Deck(0, deckField.getText().trim(), descriptionField.getText().trim(), folderSelector.getSelectionModel().getSelectedItem().get_id());
            DataModel.getData().addDeck(deck);
            return deck;
        }

    }

    public void loadData(Deck deck){
        initialize();
        for (Folder f : folderSelector.getItems()){
            if (f.get_id() == deck.getFolderId()){
                folderSelector.getSelectionModel().select(f);
            }
        }
        deckField.setText(deck.getName());
        descriptionField.setText(deck.getDescription());
    }

    public void updateDeck(Deck deck){
        deck.setName(deckField.getText().trim());
        deck.setDescription(descriptionField.getText().trim());
        deck.setFolderId(folderSelector.getSelectionModel().getSelectedItem().get_id());
        deck.createDescriptionBox();
        DataModel.getData().updateDeck(deck);
    }

    public Boolean validateInput(){
        if(deckField.getText().length() == 0 || deckField.getText().contains("'") || deckField.getText().contains("\\")
                || descriptionField.getText().length() == 0 || descriptionField.getText().contains("'") || descriptionField.getText().contains("\\") || folderSelector.getSelectionModel().getSelectedItem() == null){
            warningLabel.setVisible(true);
            return false;
        }
        else return true;
    }

    public void select(Folder folder){
        folderSelector.getSelectionModel().select(folder);
    }

}
