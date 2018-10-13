package com.toficer.dialogs.controllers;

import com.toficer.data.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FolderCreationDialogController implements CreationDialogController, ValidatingController {

    @FXML
    TextField folderField;
    @FXML
    TextField descriptionField;
    @FXML
    Label warningLabel;

    // creates a new folder and adds it to the database
    public void performCreation() {
            Folder folder = new Folder(0, folderField.getText().trim(), descriptionField.getText().trim());
            DataModel.getData().addFolder(folder);
    }

    public void loadData(DisplayableInListView object){
        Folder folder = (Folder) object;
        folderField.setText(folder.getName());
        descriptionField.setText(folder.getDescription());
    }

    public void updateFolder(Folder folder){
        folder.setName(folderField.getText().trim());
        folder.setDescription(descriptionField.getText().trim());
        folder.createDescriptionBox();
        DataModel.getData().updateFolder(folder);
    }

    public Boolean validateInput(){
        if(folderField.getText().length() == 0 || folderField.getText().contains("'") || folderField.getText().contains("\\")
                || descriptionField.getText().length() == 0 || descriptionField.getText().contains("'") || descriptionField.getText().contains("\\")){
            warningLabel.setVisible(true);
            return false;
        }
        else return true;
    }

    public void select(DisplayableInListView object){
        // does nothing
    }
}
