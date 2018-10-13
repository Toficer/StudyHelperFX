package com.toficer.dialogs.controllers;

import com.toficer.data.DataModel;
import com.toficer.data.Deck;
import com.toficer.data.ValidatingController;
import com.toficer.mainwindow.MainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

public class CardTypeDialogController implements ValidatingController {

    private double xOffsetDialog = 0;
    private double yOffsetDialog = 0;

    @FXML
    ChoiceBox<String> cardTypeSelector;
    @FXML
    Label warningLabel;

    @FXML
    public void initialize(){
        cardTypeSelector.getItems().add("Simple text card");
        cardTypeSelector.getItems().add("Multiple choice card");
    }

    public Boolean validateInput(){
        if(cardTypeSelector.getSelectionModel().getSelectedItem() == null){
            warningLabel.setVisible(true);
            return false;
        }
        else return true;
    }

    public void addNewCard(BorderPane mainWindow, ListView mainListView, int input){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle("CREATE NEW CARD");
        dialog.setHeaderText("NEW CARD: TYPE IN THE DATA AND PRESS OK");
        FXMLLoader fxmlLoader = new FXMLLoader();

        xOffsetDialog = 0;
        yOffsetDialog = 0;

        dialog.getDialogPane().setOnMousePressed(event -> {
            xOffsetDialog = event.getSceneX();
            yOffsetDialog = event.getSceneY();
        });
        dialog.getDialogPane().setOnMouseDragged(event -> {
            dialog.setX(event.getScreenX() - xOffsetDialog);
            dialog.setY(event.getScreenY() - yOffsetDialog);
        });

        if(cardTypeSelector.getSelectionModel().getSelectedItem().contentEquals("Simple text card")){
            fxmlLoader.setLocation(getClass().getResource("/com/toficer/dialogs/fxml/simpletextcarddialog.fxml"));
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
                dialog.getDialogPane().getStylesheets().add("stylesheet.css");
                dialog.getDialogPane().getStyleClass().add("SimpleTextCardDialog");

            }catch (IOException e){
                System.err.println("Couldn't load the dialog.");
                e.printStackTrace();
                return;
            }

            SimpleTextCardCreationDialogController controller = fxmlLoader.getController();

            if(input == MainWindowController.INPUT_CONTEXT){
                Deck deck = (Deck)mainListView.getSelectionModel().getSelectedItem();
                controller.selectDeck(deck);
            }
            else if(input == MainWindowController.INPUT_CONTEXT_EMPTY){
                controller.selectDeck(DataModel.getData().getCurrentDeck());
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            okButton.addEventFilter(ActionEvent.ACTION, event -> {
                if (!controller.validateInput()) event.consume();
            });

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                controller.createCard();
            }
        }

        else if(cardTypeSelector.getSelectionModel().getSelectedItem().contentEquals("Multiple choice card")){
            fxmlLoader.setLocation(getClass().getResource("/com/toficer/dialogs/fxml/multiplechoicecarddialog.fxml"));
            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
                dialog.getDialogPane().getStylesheets().add("stylesheet.css");
                dialog.getDialogPane().getStyleClass().add("MultipleChoiceCardDialog");

            }catch (IOException e){
                System.err.println("Couldn't load the dialog.");
                e.printStackTrace();
                return;
            }

            MultipleChoiceCardCreationDialogController controller = fxmlLoader.getController();

            if(input == MainWindowController.INPUT_CONTEXT){
                Deck deck = (Deck)mainListView.getSelectionModel().getSelectedItem();
                controller.selectDeck(deck);
            }
            else if(input == MainWindowController.INPUT_CONTEXT_EMPTY){
                controller.selectDeck(DataModel.getData().getCurrentDeck());
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            okButton.addEventFilter(ActionEvent.ACTION, event -> {
                if (!controller.validateInput()) event.consume();
            });

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                controller.createCard();
            }
        }

    }

}
