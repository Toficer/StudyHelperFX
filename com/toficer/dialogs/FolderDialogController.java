package com.toficer.dialogs;

import com.toficer.data.DataModel;
import com.toficer.data.Folder;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FolderDialogController {

    @FXML
    TextField folderField;
    @FXML
    TextField descriptionField;

    public Folder createFolder() {
        String name = folderField.getText().trim();
        String desc = descriptionField.getText().trim();
        Folder folder = new Folder();
        folder.setDescription(desc);
        folder.setName(name);
        DataModel.getData().addFolder(folder);
        return folder;
    }
}
