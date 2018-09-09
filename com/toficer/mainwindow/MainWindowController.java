package com.toficer.mainwindow;

import com.toficer.data.DataModel;
import com.toficer.data.Deck;
import com.toficer.data.Folder;
import com.toficer.dialogs.CardTypeDialogController;
import com.toficer.dialogs.DeckDialogController;
import com.toficer.dialogs.FolderDialogController;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

public class MainWindowController {

    private Stage stage;
    Random random;
    private double xOffset = 0;
    private double yOffset = 0;
    public static final int INPUT_MENU = 0;
    public static final int INPUT_CONTEXT = 1;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    Button backButton;
    @FXML
    VBox topBox;
    @FXML
    ImageView minimizeButton;
    @FXML
    ImageView exitButton;

    @FXML
    ListView mainListView;
    @FXML
    BorderPane mainWindow;
    @FXML
    Label questionLabel;
    @FXML
    Label locationLabel;
    @FXML
    Button nextCardButton;
    @FXML
    VBox centerContentBox;

    @FXML
    public void initialize(){

        // Setting up the list.
        DataModel.getData().loadFolders();
        mainListView.setItems(DataModel.getData().getListViewFolders());
        mainListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        random = new Random();

        // Changing the questionLabel to display the selected item's description.
        mainListView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {
            if(newValue != null){
                if(mainListView.getSelectionModel().getSelectedItem() instanceof Folder){
                    Folder folder = (Folder)mainListView.getSelectionModel().getSelectedItem();
                    questionLabel.setText(folder.getDescription());
                }
                else if(mainListView.getSelectionModel().getSelectedItem() instanceof Deck){
                    Deck deck = (Deck)mainListView.getSelectionModel().getSelectedItem();
                    questionLabel.setText(deck.getDescription());
                }
            }
        });

        // ListView context menu.
        ContextMenu listViewMenu = generateListViewContextMenu();

        // ListView cell formatting and assigning the context menu.
        generateListViewCellFactory(listViewMenu);

        // BELOW: adding all needed window listeners...

        topBox.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        topBox.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        exitButton.setOnMousePressed(event -> {
            DataModel.getData().closeConnection();
            System.exit(0);
        });
        exitButton.setOnMouseEntered(event -> exitButton.setImage(new Image("images/exitButtonHover.png")));
        exitButton.setOnMouseExited(event -> exitButton.setImage(new Image("images/exitButton.png")));
        minimizeButton.setOnMouseEntered(event -> minimizeButton.setImage(new Image("images/minimizeButtonHover.png")));
        minimizeButton.setOnMouseExited(event -> minimizeButton.setImage(new Image("images/minimizeButton.png")));
        minimizeButton.setOnMousePressed(event -> stage.setIconified(true));
    }

    @FXML
    public void addNewFolder(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = createDialog("CREATE NEW FOLDER", "NEW FOLDER: TYPE IN THE DATA AND PRESS OK", "folderdialog.fxml", "FolderDialog", fxmlLoader);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            FolderDialogController controller = fxmlLoader.getController();
            Folder folder = controller.createFolder();
        }

    }

    @FXML
    public void addNewDeck(int input){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = createDialog("CREATE NEW DECK", "NEW DECK: TYPE IN THE DATA AND PRESS OK", "deckdialog.fxml", "DeckDialog", fxmlLoader);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        DeckDialogController controller = fxmlLoader.getController();

        if(mainListView.getSelectionModel().getSelectedItem() instanceof Folder && input==INPUT_CONTEXT){
            System.out.println(INPUT_CONTEXT);
            controller.select((Folder)mainListView.getSelectionModel().getSelectedItem());
        }

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Deck deck = controller.createDeck();
        }
    }

    @FXML
    public void addNewDeckFromMenu(){
        addNewDeck(INPUT_MENU);
    }


    @FXML
    public void exitHelper(){
        DataModel.getData().closeConnection();
        System.exit(0);
    }

    @FXML
    public void displayInfo(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = createDialog("INFO AND CREDITS", "INFO AND CREDITS", "infodialog.fxml", "InfoDialog", fxmlLoader);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    @FXML
    public void chooseDeck(Folder folder){
        DataModel.getData().loadDecks(folder);
        DataModel.getData().setCurrentFolder(folder);
        mainListView.setItems(DataModel.getData().getListViewDecks());
        backButton.setDisable(false);
        locationLabel.setText("HOME >" + folder.toString());
        questionLabel.setText("");
    }

    @FXML
    public void backToFolders(){
        backButton.setDisable(true);
        mainListView.setItems(DataModel.getData().getListViewFolders());
        locationLabel.setText("HOME >");
        questionLabel.setText("");
    }

    @FXML
    public void deleteItem(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        String itemType;
        if(mainListView.getSelectionModel().getSelectedItem() instanceof Folder){
            itemType = ((Folder) mainListView.getSelectionModel().getSelectedItem()).toString();
        }
        else if(mainListView.getSelectionModel().getSelectedItem() instanceof Deck){
            itemType = ((Deck) mainListView.getSelectionModel().getSelectedItem()).toString();
        }
        else itemType = "UNKNOWN_ITEM";
        Dialog<ButtonType> dialog = createDialog("DELETE", "DELETE" + itemType, "deletedialog.fxml", "DeleteDialog", fxmlLoader);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            if(mainListView.getSelectionModel().getSelectedItem() instanceof Folder){
                DataModel.getData().deleteFolder(((Folder) mainListView.getSelectionModel().getSelectedItem()).get_id());
            }
            else if(mainListView.getSelectionModel().getSelectedItem() instanceof Deck){
                DataModel.getData().deleteDeck(((Deck) mainListView.getSelectionModel().getSelectedItem()).get_id());
            }
            else System.err.println("ERROR: UNKNOWN ITEM TYPE, CAN'T DELETE FROM DB");;
            mainListView.getItems().remove(mainListView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    public void displayCardTypeSelectionDialog(int input){

        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = createDialog("NEW CARD", "CHOOSE THE CARD TYPE", "cardtypedialog.fxml", "CardTypeDialog", fxmlLoader);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            CardTypeDialogController controller = fxmlLoader.getController();
            controller.addNewCard(mainWindow, mainListView, input);
        }
    }

    @FXML
    public Dialog<ButtonType> createDialog(String title, String headerText, String fxmlFile, String cssLabel, FXMLLoader fxmlLoader){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle(title);

        dialog.setHeaderText(headerText);
        fxmlLoader.setLocation(getClass().getResource("/com/toficer/dialogs/" + fxmlFile));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.getDialogPane().getStylesheets().add("stylesheet.css");
            dialog.getDialogPane().getStyleClass().add(cssLabel);

        }catch (IOException e){
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return null;
        }

        return dialog;
    }

    @FXML
    public void answerButtonHandler(){

        if(nextCardButton.getText().equals("   NEXT CARD   ")){
            System.out.println("NEXT CARD");
            if(DataModel.getData().getCurrentCard() != null){
                DataModel.getData().getStudySession().remove(DataModel.getData().getCurrentCard());
            }
            if(DataModel.getData().getStudySession().size() == 0){
                questionLabel.setText("");
                centerContentBox.getChildren().clear();
                nextCardButton.setDisable(true);
            }
            else {
                int currentCard = random.nextInt(DataModel.getData().getStudySession().size());
                DataModel.getData().setCurrentCard(DataModel.getData().getStudySession().get(currentCard));
                questionLabel.setText(DataModel.getData().getCurrentCard().getQuestion());
                centerContentBox.getChildren().clear();
                nextCardButton.setText("   SHOW ANSWER   ");
            }
        }
        else{
            centerContentBox.getChildren().add(DataModel.getData().getCurrentCard().getAnswerBox());
            nextCardButton.setText("   NEXT CARD   ");
        }

    }

    public void openStudySession(int deck_id){
        System.out.println(deck_id);
        DataModel.getData().openStudySession(deck_id);
        System.out.println(DataModel.getData().getStudySession().size());
        if(DataModel.getData().getStudySession() != null){
            nextCardButton.setDisable(false);
            nextCardButton.setText("   NEXT CARD   ");
            answerButtonHandler();
        }
    }

    public ListView getMainListView() {
        return mainListView;
    }

    public ContextMenu generateListViewContextMenu(){
        ContextMenu listViewMenu = new ContextMenu();
        listViewMenu.getStyleClass().add("listContext");
        MenuItem contextOpen = new MenuItem("Open/Study");
        contextOpen.setOnAction(event -> {
            Object item = mainListView.getSelectionModel().getSelectedItem();
            if(item instanceof Folder) {
                chooseDeck((Folder)item);
            }
            else if(item instanceof Deck) {
                openStudySession(((Deck) item).get_id());
            }
        });
        listViewMenu.getItems().addAll(contextOpen);
        MenuItem contextDelete = new MenuItem("Delete");
        contextDelete.setOnAction(event -> {
            deleteItem();
        });
        listViewMenu.getItems().addAll(contextDelete);
        MenuItem contextAdd = new MenuItem("Add...");
        contextAdd.setOnAction(event -> {
            if(mainListView.getSelectionModel().getSelectedItem() instanceof Folder){
                addNewDeck(INPUT_CONTEXT);
            }
            if(mainListView.getSelectionModel().getSelectedItem() instanceof Deck){
                displayCardTypeSelectionDialog(INPUT_CONTEXT);
            }
        });
        listViewMenu.getItems().addAll(contextAdd);
        return listViewMenu;
    }

    public void generateListViewCellFactory(ContextMenu listViewMenu){
        mainListView.setCellFactory(new Callback<ListView<Object>, ListCell<Object>>() {
            @Override
            public ListCell<Object> call(ListView<Object> param) {
                ListCell<Object> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        ImageView view = new ImageView();
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                            setGraphic(null);
                        }
                        else {
                            if(item instanceof Folder){
                                setText(((Folder) item).toString());
                                view.setImage(new Image("images/folder.png"));
                                view.setFitHeight(32);
                                view.setFitWidth(32);
                                setGraphic(view);
                            }
                            else if(item instanceof Deck){
                                setText(((Deck) item).toString());
                                view.setImage(new Image("images/deck.png"));
                                view.setFitHeight(32);
                                view.setFitWidth(32);
                                setGraphic(view);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if(isNowEmpty) {
                                cell.setContextMenu(null);
                            }
                            else {
                                cell.setContextMenu(listViewMenu);
                            }
                        }
                );
                cell.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        Object item = mainListView.getSelectionModel().getSelectedItem();
                        if(item instanceof Folder) {
                            chooseDeck((Folder)item);
                        }
                    }

                });
                return cell;
            }
        });
    }
}
