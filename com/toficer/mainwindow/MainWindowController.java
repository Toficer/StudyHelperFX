package com.toficer.mainwindow;

import com.toficer.data.Card;
import com.toficer.data.DataModel;
import com.toficer.data.Deck;
import com.toficer.data.Folder;
import com.toficer.dialogs.CardTypeDialogController;
import com.toficer.dialogs.DeckDialogController;
import com.toficer.dialogs.FolderDialogController;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
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
    private Random random;
    private double xOffset = 0;
    private double yOffset = 0;
    private double xOffsetDialog = 0;
    private double yOffsetDialog = 0;
    public static final int INPUT_MENU = 0;
    public static final int INPUT_CONTEXT = 1;
    public static final int INPUT_OK_CANCEL = 0;
    public static final int INPUT_OK_ONLY = 1;
    public static final int INPUT_CONTEXT_FOLDER = 0; // Leaving more options than needed, just in case.
    public static final int INPUT_CONTEXT_DECK = 1; // Leaving more options than needed, just in case.
    public static final int INPUT_CONTEXT_CARD = 2; // Leaving more options than needed, just in case.
    public static final int STATE_ANSWERING = 0;
    public static final int STATE_ANSWERED = 1;
    private Image folderImage;
    private Image deckImage;
    private Image cardImage;
    private Image exitButtonImage;
    private Image exitButtonHoverImage;
    private Image minimizeButtonImage;
    private Image minimizeButtonHoverImage;
    private int currentState = STATE_ANSWERED;


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
    ScrollPane centerScrollPane;

    @FXML
    public void initialize(){

        // Loading images...
        folderImage = new Image("images/folder.png");
        deckImage = new Image("images/deck.png");
        cardImage = new Image("images/file.png");
        exitButtonImage = new Image("images/exitButton.png");
        exitButtonHoverImage = new Image("images/exitButtonHover.png");
        minimizeButtonImage = new Image("images/minimizeButton.png");
        minimizeButtonHoverImage = new Image("images/minimizeButtonHover.png");

        // Setting up the list.
        DataModel.getData().loadFolders();
        mainListView.setItems(DataModel.getData().getListViewFolders());
        mainListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        random = new Random();

        // Changing the questionLabel to display the selected item's description or (in case of cards) the question and the answer.
        mainListView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {
            if(newValue != null){
                if(mainListView.getSelectionModel().getSelectedItem() instanceof Folder){
                    Folder folder = (Folder)mainListView.getSelectionModel().getSelectedItem();
                    //questionLabel.setText(folder.getDescription());
                    centerContentBox.getChildren().clear();
                    centerContentBox.getChildren().add(folder.getDescriptionBox());
                }
                else if(mainListView.getSelectionModel().getSelectedItem() instanceof Deck){
                    Deck deck = (Deck)mainListView.getSelectionModel().getSelectedItem();
                    //questionLabel.setText(deck.getDescription());
                    centerContentBox.getChildren().clear();
                    centerContentBox.getChildren().add(deck.getDescriptionBox());
                }
                else if(mainListView.getSelectionModel().getSelectedItem() instanceof Card){
                    Card card = (Card)mainListView.getSelectionModel().getSelectedItem();
                    //questionLabel.setText(card.getQuestion());
                    centerContentBox.getChildren().clear();
                    centerContentBox.getChildren().add(card.getDescriptionBox());
                }
            }
        });

        // ListView context menus.
        ContextMenu folderContext = generateListViewContextMenu(INPUT_CONTEXT_FOLDER);
        ContextMenu deckContext = generateListViewContextMenu(INPUT_CONTEXT_DECK);
        ContextMenu cardContext = generateListViewContextMenu(INPUT_CONTEXT_CARD);


        // ListView cell formatting and assigning the context menu.
        generateListViewCellFactory(folderContext, deckContext, cardContext);

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
        exitButton.setOnMouseEntered(event -> exitButton.setImage(exitButtonHoverImage));
        exitButton.setOnMouseExited(event -> exitButton.setImage(exitButtonImage));
        minimizeButton.setOnMouseEntered(event -> minimizeButton.setImage(minimizeButtonHoverImage));
        minimizeButton.setOnMouseExited(event -> minimizeButton.setImage(minimizeButtonImage));
        minimizeButton.setOnMousePressed(event -> stage.setIconified(true));
    }

    @FXML
    public void addNewFolder(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = createDialog("CREATE NEW FOLDER",
                "NEW FOLDER: TYPE IN THE DATA AND PRESS OK",
                "folderdialog.fxml",
                "FolderDialog",
                INPUT_OK_CANCEL,
                fxmlLoader);

        FolderDialogController controller = fxmlLoader.getController();

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!controller.validateInput()) event.consume();
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.createFolder();
        }

    }

    @FXML
    public void addNewDeck(int input){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = createDialog("CREATE NEW DECK",
                "NEW DECK: TYPE IN THE DATA AND PRESS OK",
                "deckdialog.fxml",
                "DeckDialog",
                INPUT_OK_CANCEL,
                fxmlLoader);

        DeckDialogController controller = fxmlLoader.getController();

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!controller.validateInput()) event.consume();
        });

        if(mainListView.getSelectionModel().getSelectedItem() instanceof Folder && input==INPUT_CONTEXT){
            controller.select((Folder)mainListView.getSelectionModel().getSelectedItem());
        }

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.createDeck();
        }
    }

    @FXML
    public void addNewDeckFromMenu(){
        addNewDeck(INPUT_MENU);
    }

    @FXML
    public void displayInfo(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = createDialog("INFO AND CREDITS",
                "INFO AND CREDITS",
                "infodialog.fxml",
                "InfoDialog",
                INPUT_OK_ONLY,
                fxmlLoader);

        dialog.showAndWait();
    }

    @FXML
    public void enterDeckList(Folder folder){
        DataModel.getData().loadDecks(folder);
        DataModel.getData().setCurrentFolder(folder);
        mainListView.setItems(DataModel.getData().getListViewDecks());
        backButton.setDisable(false);
        locationLabel.setText("HOME >" + folder.toString());
        questionLabel.setText("");
    }

    @FXML
    public void enterCardList(Deck deck){
        DataModel.getData().loadCards(deck);
        DataModel.getData().setCurrentDeck(deck);
        mainListView.setItems(DataModel.getData().getListViewCards());
        backButton.setDisable(false);
        locationLabel.setText("HOME >" + DataModel.getData().getCurrentFolder().toString() + " >" + deck.toString());
        questionLabel.setText("");
    }

    @FXML
    public void backToPreviousList(){
        if(DataModel.getData().getCurrentDeck() == null){
            mainListView.setItems(DataModel.getData().getListViewFolders());
            DataModel.getData().setCurrentFolder(null);
            backButton.setDisable(true);
            locationLabel.setText("HOME >");
        }
        else {
            mainListView.setItems(DataModel.getData().getListViewDecks());
            DataModel.getData().setCurrentDeck(null);
            locationLabel.setText("HOME >" + DataModel.getData().getCurrentFolder().toString());
        }
        questionLabel.setText("");
        centerContentBox.getChildren().clear();
    }

    @FXML
    public void deleteItem(){
        FXMLLoader fxmlLoader = new FXMLLoader();

        String itemType = mainListView.getSelectionModel().getSelectedItem().toString();

        Dialog<ButtonType> dialog = createDialog("DELETE",
                "DELETE" + itemType,
                "deletedialog.fxml",
                "DeleteDialog",
                INPUT_OK_CANCEL,
                fxmlLoader);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            if(mainListView.getSelectionModel().getSelectedItem() instanceof Folder){
                DataModel.getData().deleteFolder(((Folder) mainListView.getSelectionModel().getSelectedItem()).get_id());
            }
            else if(mainListView.getSelectionModel().getSelectedItem() instanceof Deck){
                DataModel.getData().deleteDeck(((Deck) mainListView.getSelectionModel().getSelectedItem()).get_id());
            }
            else System.err.println("ERROR: UNKNOWN ITEM TYPE, CAN'T DELETE FROM DB");
            mainListView.getItems().remove(mainListView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    public void displayCardTypeSelectionDialog(int input){

        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = createDialog("NEW CARD",
                "CHOOSE THE CARD TYPE",
                "cardtypedialog.fxml",
                "CardTypeDialog",
                INPUT_OK_CANCEL,
                fxmlLoader);

        CardTypeDialogController controller = fxmlLoader.getController();

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!controller.validateInput()) event.consume();
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.addNewCard(mainWindow, mainListView, input);
        }
    }

    @FXML
    public void displayCardTypeSelectionDialogFromMenu(){
        displayCardTypeSelectionDialog(INPUT_MENU);
    }

    @FXML
    public Dialog<ButtonType> createDialog(String title, String headerText, String fxmlFile, String cssLabel, int dialogType, FXMLLoader fxmlLoader){
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

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        if(dialogType == INPUT_OK_CANCEL){
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        }

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

        return dialog;
    }

    @FXML
    public void answerButtonHandler(){

        if(currentState == STATE_ANSWERED){
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
                currentState = STATE_ANSWERING;
            }
        }
        else{
            centerContentBox.getChildren().add(DataModel.getData().getCurrentCard().getAnswerBox());
            nextCardButton.setText("   NEXT CARD   ");
            currentState = STATE_ANSWERED;
        }

    }

    public void openStudySession(int deck_id){
        DataModel.getData().openStudySession(deck_id);
        if(DataModel.getData().getStudySession() != null){
            nextCardButton.setDisable(false);
            nextCardButton.setText("   NEXT CARD   ");
            currentState = STATE_ANSWERED;
            answerButtonHandler();
        }
    }

    public ContextMenu generateListViewContextMenu(int menuType){
        ContextMenu listViewMenu = new ContextMenu();
        listViewMenu.getStyleClass().add("listContext");
        if(menuType != INPUT_CONTEXT_CARD){
            MenuItem contextOpen = new MenuItem("Open/Study");
            contextOpen.setOnAction(event -> {
                Object item = mainListView.getSelectionModel().getSelectedItem();
                if(item instanceof Folder) {
                    enterDeckList((Folder)item);
                }
                else if(item instanceof Deck) {
                    openStudySession(((Deck) item).get_id());
                }
            });
            listViewMenu.getItems().addAll(contextOpen);
        }
        if(menuType != INPUT_CONTEXT_CARD){
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
        }
        MenuItem contextDelete = new MenuItem("Delete");
        contextDelete.setOnAction(event -> {
            deleteItem();
        });
        listViewMenu.getItems().addAll(contextDelete);
        return listViewMenu;
    }

    public void generateListViewCellFactory(ContextMenu folderContext, ContextMenu deckContext, ContextMenu cardContext){
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
                            setText(item.toString());

                            if(item instanceof Folder){
                                view.setImage(folderImage);
                            }
                            else if(item instanceof Deck){
                                view.setImage(deckImage);
                            }
                            else view.setImage(cardImage);

                            view.setFitHeight(32);
                            view.setFitWidth(32);
                            setGraphic(view);
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if(isNowEmpty) {
                                cell.setContextMenu(null);
                            }
                            else {
                                if(cell.getItem() instanceof Folder){
                                    cell.setContextMenu(folderContext);
                                }
                                else if(cell.getItem() instanceof Deck){
                                    cell.setContextMenu(deckContext);
                                }
                                else cell.setContextMenu(cardContext);
                            }
                        }
                );
                cell.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        Object item = mainListView.getSelectionModel().getSelectedItem();
                        if(item instanceof Folder) {
                            enterDeckList((Folder)item);
                        }
                        if(item instanceof Deck) {
                            enterCardList((Deck)item);
                        }
                    }
                });
                return cell;
            }
        });
    }

    public ListView getMainListView() {
        return mainListView;
    }

    @FXML
    public void exitHelper(){
        DataModel.getData().closeConnection();
        System.exit(0);
    }
}
