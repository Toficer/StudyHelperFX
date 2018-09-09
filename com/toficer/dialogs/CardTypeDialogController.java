package com.toficer.dialogs;

import com.toficer.data.Deck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

public class CardTypeDialogController {

    public static final int INPUT_MENU = 0;
    public static final int INPUT_CONTEXT = 1;

    @FXML
    ChoiceBox<String> cardTypeSelector;

    @FXML
    public void initialize(){
        cardTypeSelector.getItems().add("Simple text card");
        //cardTypeSelector.getItems().add("Multiple choice card");
    }

    public void addNewCard(BorderPane mainWindow, ListView mainListView, int input){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle("CREATE NEW CARD");
        dialog.setHeaderText("NEW CARD: TYPE IN THE DATA AND PRESS OK");
        FXMLLoader fxmlLoader = new FXMLLoader();

        if(cardTypeSelector.getSelectionModel().getSelectedItem().contentEquals("Simple text card")){
            fxmlLoader.setLocation(getClass().getResource("/com/toficer/dialogs/simpletextcarddialog.fxml"));
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
                dialog.getDialogPane().getStylesheets().add("stylesheet.css");
                dialog.getDialogPane().getStyleClass().add("SimpleTextCardDialog");

            }catch (IOException e){
                System.out.println("Couldn't load the dialog.");
                e.printStackTrace();
                return;
            }

            SimpleTextCardDialogController controller = fxmlLoader.getController();
            if(input == INPUT_CONTEXT){
                System.out.println("SELECT DECK");
                Deck deck = (Deck)mainListView.getSelectionModel().getSelectedItem();
                System.out.println(deck.getDescription());
                controller.selectDeck(deck);
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                controller.createCard();
            }
        }

    }

}
