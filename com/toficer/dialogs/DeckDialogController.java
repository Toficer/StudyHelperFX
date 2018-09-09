package com.toficer.dialogs;

import com.toficer.data.DataModel;
import com.toficer.data.Deck;
import com.toficer.data.Folder;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class DeckDialogController {

    @FXML
    TextField folderField;
    @FXML
    TextField descriptionField;
    @FXML
    ChoiceBox<Folder> folderSelector;

    @FXML
    public void initialize(){
        folderSelector.getItems().setAll(DataModel.getData().getListViewFolders());
    }

    public Deck createDeck() {
        if(folderSelector.getSelectionModel().getSelectedItem() == null){
            return null;
        }
        else {
            String name = folderField.getText().trim();
            String desc = descriptionField.getText().trim();
            int folder_id = folderSelector.getSelectionModel().getSelectedItem().get_id();
            Deck deck = new Deck();
            deck.setDescription(desc);
            deck.setName(name);
            deck.setFolderId(folder_id);
            System.out.println(deck.toString());
            DataModel.getData().addDeck(deck);
            return deck;
        }

    }

    public void select(Folder folder){
        folderSelector.getSelectionModel().select(folder);
    }

}
