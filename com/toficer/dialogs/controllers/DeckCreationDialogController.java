package com.toficer.dialogs.controllers;

import com.toficer.data.*;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DeckCreationDialogController implements CreationDialogController, ValidatingController {

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

    public void performCreation() {
        if(folderSelector.getSelectionModel().getSelectedItem() == null){
            System.err.println("No parent folder selected, can't create deck.");
        }
        else {
            Deck deck = new Deck(0, deckField.getText().trim(), descriptionField.getText().trim(), folderSelector.getSelectionModel().getSelectedItem().get_id());
            DataModel.getData().addDeck(deck);
        }
    }

    public void loadData(DisplayableInListView object){
        initialize();
        Deck deck = (Deck) object;
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

    public void select(DisplayableInListView folder){
        if(folder instanceof Folder){
            folderSelector.getSelectionModel().select((Folder)folder);
        }
    }

}
