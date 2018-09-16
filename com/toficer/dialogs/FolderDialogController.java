package com.toficer.dialogs;

import com.toficer.data.DataModel;
import com.toficer.data.Folder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FolderDialogController {

    @FXML
    TextField folderField;
    @FXML
    TextField descriptionField;
    @FXML
    Label warningLabel;

    public Folder createFolder() {
            Folder folder = new Folder(0, folderField.getText().trim(), descriptionField.getText().trim());
            DataModel.getData().addFolder(folder);
            return folder;
    }

    public void loadData(Folder folder){
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
}
