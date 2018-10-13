package com.toficer.mainwindow;

import com.toficer.data.*;
import com.toficer.dialogs.controllers.*;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
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
    public static final int INPUT_CONTEXT_EMPTY = 3; // Leaving more options than needed, just in case.
    public static final int STATE_ANSWERING = 0;
    public static final int STATE_ANSWERED = 1;
    public static final int CREATION_FOLDER = 0;
    public static final int CREATION_DECK = 1;
    public static final int CREATION_CARD = 2;
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
    Button exitStudySessionButton;
    @FXML
    VBox centerContentBox;
    @FXML
    ScrollPane centerScrollPane;

    @FXML
    public void initialize(){

        // Setting up the list.
        DataModel.getData().loadFolders();
        mainListView.setItems(DataModel.getData().getListViewFolders());
        mainListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        random = new Random();

        // Changing the questionLabel to display the selected item's description or (in case of cards) the question and the answer.
        mainListView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {
            if(newValue != null){
                centerContentBox.getChildren().clear();
                Object obj = mainListView.getSelectionModel().getSelectedItem();
                if(obj instanceof DisplayableInListView){
                    centerContentBox.getChildren().add(((DisplayableInListView)obj).getDescriptionBox());
                }
            }
        });

        // ListView context menus.
        ContextMenu folderContext = generateListViewContextMenu(INPUT_CONTEXT_FOLDER);
        ContextMenu deckContext = generateListViewContextMenu(INPUT_CONTEXT_DECK);
        ContextMenu cardContext = generateListViewContextMenu(INPUT_CONTEXT_CARD);
        ContextMenu emptyFieldContext = generateListViewContextMenu(INPUT_CONTEXT_EMPTY);


        // ListView cell formatting and assigning the context menu.
        generateListViewCellFactory(folderContext, deckContext, cardContext);
        mainListView.setContextMenu(emptyFieldContext);
        // BELOW: adding all needed window listeners...
        // including window movement

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
        exitButton.setOnMouseEntered(event -> exitButton.setImage(DataModel.getData().getExitButtonHoverImage()));
        exitButton.setOnMouseExited(event -> exitButton.setImage(DataModel.getData().getExitButtonImage()));
        minimizeButton.setOnMouseEntered(event -> minimizeButton.setImage(DataModel.getData().getMinimizeButtonHoverImage()));
        minimizeButton.setOnMouseExited(event -> minimizeButton.setImage(DataModel.getData().getMinimizeButtonImage()));
        minimizeButton.setOnMousePressed(event -> stage.setIconified(true));
    }

    // displays a creation dialog for a new folder object
    @FXML
    public void addNewFolder(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        displayCreationDialog("CREATE NEW FOLDER",
                "NEW FOLDER: TYPE IN THE DATA AND PRESS OK",
                "folderdialog.fxml",
                "FolderDialog",
                INPUT_OK_CANCEL,
                fxmlLoader,
                CREATION_DECK,
                INPUT_CONTEXT);
    }

    // displays a creation dialog for a new deck object
    // the input parameter specifies whether the window is called from a context menu,
    // or from a button in the menu bar (which determines whether it will be filled with
    // information on the currently selected folder or not)
    public void addNewDeck(int input){
        FXMLLoader fxmlLoader = new FXMLLoader();
        displayCreationDialog("CREATE NEW DECK",
                "NEW DECK: TYPE IN THE DATA AND PRESS OK",
                "deckdialog.fxml",
                "DeckDialog",
                INPUT_OK_CANCEL,
                fxmlLoader,
                CREATION_DECK,
                input);
    }

    // calls the addNewDeck() method with the INPUT_MENU input value
    // which results in a creation window with empty fields
    @FXML
    public void addNewDeckFromMenu(){
        addNewDeck(INPUT_MENU);
    }

    // displays a generic dialog that allows the user to choose the type of card
    // they want to create
    // the input parameter specifies whether the window is called from a context menu,
    // or from a button in the menu bar (which determines whether it will be filled with
    // information on the currently selected deck or not - it is then passed to the
    // user-chosen card creation dialog
    public void displayCardTypeSelectionDialog(int input){

        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = displayGenericDialog("NEW CARD",
                "CHOOSE THE CARD TYPE",
                "cardtypedialog.fxml",
                "CardTypeDialog",
                INPUT_OK_CANCEL,
                fxmlLoader);

        CardTypeDialogController controller = fxmlLoader.getController();
        addDialogInputValidation(dialog, fxmlLoader);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.addNewCard(mainWindow, mainListView, input);
            if(DataModel.getData().getCurrentDeck() != null){
                enterCardList(DataModel.getData().getCurrentDeck());
            }
        }
    }

    // calls the displayCardTypeSelectionDialog() method with the INPUT_MENU input value
    // which later results in a card creation window with empty fields
    @FXML
    public void displayCardTypeSelectionDialogFromMenu(){
        displayCardTypeSelectionDialog(INPUT_MENU);
    }

    // displays a generic dialog window with a single "OK" button
    // the dialog contains information on the program
    @FXML
    public void displayInfo(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = displayGenericDialog("INFO AND CREDITS",
                "INFO AND CREDITS",
                "infodialog.fxml",
                "InfoDialog",
                INPUT_OK_ONLY,
                fxmlLoader);

        dialog.showAndWait();
    }

    // loads the contents of a chosen folder and displays them inside the mainListView
    // also clears the centerContentBox of any descriptions and updates the locationLabel
    // with the current folder path
    public void enterDeckList(Folder folder){
        DataModel.getData().loadDecks(folder);
        DataModel.getData().setCurrentFolder(folder);
        mainListView.setItems(DataModel.getData().getListViewDecks());
        backButton.setDisable(false);
        locationLabel.setText("HOME > " + folder.toString());
        centerContentBox.getChildren().clear();
    }

    // loads the contents of a chosen deck and displays them inside the mainListView
    // also clears the centerContentBox of any descriptions and updates the locationLabel
    // with the current deck path
    public void enterCardList(Deck deck){
        DataModel.getData().loadCards(deck);
        DataModel.getData().setCurrentDeck(deck);
        mainListView.setItems(DataModel.getData().getListViewCards());
        backButton.setDisable(false);
        locationLabel.setText("HOME > " + DataModel.getData().getCurrentFolder().toString() + " > " + deck.toString());
        centerContentBox.getChildren().clear();
    }

    // loads the contents of the previous folder or folder list
    @FXML
    public void backToPreviousList(){
        if(DataModel.getData().getCurrentDeck() == null){
            mainListView.setItems(DataModel.getData().getListViewFolders());
            DataModel.getData().setCurrentFolder(null);
            backButton.setDisable(true);
            locationLabel.setText("HOME > ");
        }
        else {
            mainListView.setItems(DataModel.getData().getListViewDecks());
            DataModel.getData().setCurrentDeck(null);
            locationLabel.setText("HOME > " + DataModel.getData().getCurrentFolder().toString());
        }
        centerContentBox.getChildren().clear();
    }

    // calls the appropriate editX() method for the item editing dialog creation process.
    public void editItem(){
        if(mainListView.getSelectionModel().getSelectedItem() instanceof Folder){
            editFolder((Folder)(mainListView.getSelectionModel().getSelectedItem()));
        }
        else if(mainListView.getSelectionModel().getSelectedItem() instanceof Deck){
            editDeck((Deck)(mainListView.getSelectionModel().getSelectedItem()));
        }
        else if(mainListView.getSelectionModel().getSelectedItem() instanceof Card){
            editCard((Card)(mainListView.getSelectionModel().getSelectedItem()));
        }
    }

    // displays the folder creation window for the chosen folder
    // which is filled with the folder's parameters, allowing for editing it
    public void editFolder(Folder folder){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = displayGenericDialog("EDIT FOLDER",
                "EDIT FOLDER: TYPE IN THE DATA AND PRESS OK",
                "folderdialog.fxml",
                "FolderDialog",
                INPUT_OK_CANCEL,
                fxmlLoader);

        FolderCreationDialogController controller = fxmlLoader.getController();

        addDialogInputValidation(dialog, fxmlLoader);

        controller.loadData(folder);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.updateFolder(folder);
            DataModel.getData().populateFolderListView();
            mainListView.refresh();
        }
    }

    // displays the deck creation window for the chosen deck
    // which is filled with the deck's parameters, allowing for editing it
    public void editDeck(Deck deck){
        FXMLLoader fxmlLoader = new FXMLLoader();
        Dialog<ButtonType> dialog = displayGenericDialog("EDIT DECK",
                "EDIT DECK: TYPE IN THE DATA AND PRESS OK",
                "deckdialog.fxml",
                "DeckDialog",
                INPUT_OK_CANCEL,
                fxmlLoader);

        DeckCreationDialogController controller = fxmlLoader.getController();

        addDialogInputValidation(dialog, fxmlLoader);

        controller.loadData(deck);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.updateDeck(deck);
            enterDeckList(DataModel.getData().getCurrentFolder());
            mainListView.refresh();
        }
    }

    // displays the card creation window for the chosen card
    // which is filled with the card's parameters, allowing for editing it
    public void editCard(Card card){
        FXMLLoader fxmlLoader = new FXMLLoader();
        if(card.getCardType() == DataModel.TYPE_SIMPLETEXT){
            Dialog<ButtonType> dialog = displayGenericDialog("EDIT CARD",
                    "EDIT CARD: TYPE IN THE DATA AND PRESS OK",
                    "simpletextcarddialog.fxml",
                    "SimpleTextCardDialog",
                    INPUT_OK_CANCEL,
                    fxmlLoader);
            SimpleTextCardCreationDialogController controller = fxmlLoader.getController();
            addDialogInputValidation(dialog, fxmlLoader);

            controller.loadData(card);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                controller.updateCard(card);
                enterCardList(DataModel.getData().getCurrentDeck());
                mainListView.refresh();
            }
        }
        else if(card.getCardType() == DataModel.TYPE_MULTIPLECHOICE){
            Dialog<ButtonType> dialog = displayGenericDialog("EDIT CARD",
                    "EDIT CARD: TYPE IN THE DATA AND PRESS OK",
                    "multiplechoicecarddialog.fxml",
                    "MultipleChoiceCardDialog",
                    INPUT_OK_CANCEL,
                    fxmlLoader);
            MultipleChoiceCardCreationDialogController controller = fxmlLoader.getController();
            addDialogInputValidation(dialog, fxmlLoader);

            controller.loadData(card);

            Optional<ButtonType> result = dialog.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                controller.updateCard(card);
                enterCardList(DataModel.getData().getCurrentDeck());
                mainListView.refresh();
            }
        }
    }

    // displays the deletion confirmation window for the item selected in the mainListView
    public void deleteItem(){
        FXMLLoader fxmlLoader = new FXMLLoader();

        String itemType = mainListView.getSelectionModel().getSelectedItem().toString();

        Dialog<ButtonType> dialog = displayGenericDialog("DELETE",
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
            else if(mainListView.getSelectionModel().getSelectedItem() instanceof Card){
                DataModel.getData().deleteCard(((Card) mainListView.getSelectionModel().getSelectedItem()).get_id());
            }
            else System.err.println("ERROR: UNKNOWN ITEM TYPE, CAN'T DELETE FROM DB");
            mainListView.getItems().remove(mainListView.getSelectionModel().getSelectedItem());
        }
    }

    // creates a generic dialog with the chosen title and header texts
    // the dialogType parameter specifies the buttons that need to be added to the dialog
    public Dialog<ButtonType> displayGenericDialog(String title, String headerText, String fxmlFile, String cssLabel, int dialogType, FXMLLoader fxmlLoader) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        fxmlLoader.setLocation(getClass().getResource("/com/toficer/dialogs/fxml/" + fxmlFile));

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

    // calls the displayGenericDialog() method,
    // the dialogType parameter specifies the buttons that need to be added to the dialog
    // the creation_parameter specifies which special options need to be added to the dialog
    // the input parameter specifies whether the window is called from a context menu,
    // or from a button in the menu bar (which determines whether it will be filled with
    // information on the currently selected item or not)
    public void displayCreationDialog(String title, String headerText, String fxmlFile, String cssLabel, int dialogType, FXMLLoader fxmlLoader, int creation_parameter, int input){

        Dialog<ButtonType> dialog = displayGenericDialog(title, headerText, fxmlFile, cssLabel, dialogType, fxmlLoader);
        addDialogInputValidation(dialog, fxmlLoader);

        CreationDialogController controller = fxmlLoader.getController();

        if(creation_parameter == CREATION_DECK){
            if(mainListView.getSelectionModel().getSelectedItem() instanceof Folder && input==INPUT_CONTEXT){
                controller.select((Folder)mainListView.getSelectionModel().getSelectedItem());
            }
            else if(input==INPUT_CONTEXT_EMPTY){
                controller.select(DataModel.getData().getCurrentFolder());
            }
        }

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            controller.performCreation();
        }

        if(creation_parameter == CREATION_DECK){
            if(DataModel.getData().getCurrentFolder() != null){
                enterDeckList(DataModel.getData().getCurrentFolder());
            }
        }
    }

    // adds an input validation event handlet
    public void addDialogInputValidation(Dialog<ButtonType> dialog, FXMLLoader loader){

        ValidatingController controller = loader.getController();

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!controller.validateInput()) event.consume();
        });
    }

    @FXML
    public void answerButtonHandler(){

        if(currentState == STATE_ANSWERED){
            if(DataModel.getData().getCurrentCard() != null){
                DataModel.getData().getStudySession().remove(DataModel.getData().getCurrentCard());
            }
            if(DataModel.getData().getStudySession().size() == 0){
                centerContentBox.getChildren().clear();
                nextCardButton.setDisable(true);
                exitStudySessionButton.setDisable(true);
                nextCardButton.setVisible(false);
                exitStudySessionButton.setVisible(false);
                mainListView.setDisable(false);
                backButton.setDisable(false);
            }
            else {
                int currentCard = random.nextInt(DataModel.getData().getStudySession().size());
                DataModel.getData().setCurrentCard(DataModel.getData().getStudySession().get(currentCard));
                centerContentBox.getChildren().clear();
                centerContentBox.getChildren().add(DataModel.getData().getCurrentCard().getQuestionBox());
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

    @FXML
    public void exitStudySessionButtonHandler(){
        centerContentBox.getChildren().clear();
        nextCardButton.setDisable(true);
        exitStudySessionButton.setDisable(true);
        mainListView.setDisable(false);
        backButton.setDisable(false);
        exitStudySessionButton.setVisible(false);
        nextCardButton.setVisible(false);
    }

    public void openStudySession(int deck_id){
        DataModel.getData().openStudySession(deck_id);
        if(DataModel.getData().getStudySession() != null){

            exitStudySessionButton.setVisible(true);
            nextCardButton.setVisible(true);

            nextCardButton.setDisable(false);
            exitStudySessionButton.setDisable(false);

            nextCardButton.setText("   NEXT CARD   ");
            currentState = STATE_ANSWERED;
            mainListView.setDisable(true);
            backButton.setDisable(true);
            answerButtonHandler();
        }
    }

    public ContextMenu generateListViewContextMenu(int menuType){
        ContextMenu listViewMenu = new ContextMenu();
        listViewMenu.getStyleClass().add("listContext");
        if(menuType == INPUT_CONTEXT_EMPTY){
            MenuItem contextAdd = new MenuItem("Create new...");
            contextAdd.setOnAction(event -> {
                if(DataModel.getData().getCurrentDeck() != null){
                    displayCardTypeSelectionDialog(INPUT_CONTEXT_EMPTY);
                }
                else if(DataModel.getData().getCurrentFolder() != null){
                    addNewDeck(INPUT_CONTEXT_EMPTY);
                }
                else addNewFolder();
            });
            listViewMenu.getItems().addAll(contextAdd);
        }
        else {
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
            MenuItem contextEdit = new MenuItem("Edit");
            contextEdit.setOnAction(event -> {
                editItem();
            });
            listViewMenu.getItems().addAll(contextEdit);
            MenuItem contextDelete = new MenuItem("Delete");
            contextDelete.setOnAction(event -> {
                deleteItem();
            });
            listViewMenu.getItems().addAll(contextDelete);
        }

        return listViewMenu;
    }

    // TODO: need to get rid of those colors and put them somewhere else, possibly in the stylesheet.
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

                            if(!isSelected()) setStyle("-fx-background-color: #003459");

                            if(item instanceof Folder){
                                view.setImage(DataModel.getData().getFolderImage());
                            }
                            else if(item instanceof Deck){
                                view.setImage(DataModel.getData().getDeckImage());
                            }
                            else view.setImage(DataModel.getData().getCardImage());

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
                    if(!cell.isEmpty()){
                        cell.setStyle("-fx-background-color: #007eaf");
                    }
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
                cell.setOnMouseEntered(event -> {
                    if(!cell.isSelected() && !cell.isEmpty()) cell.setStyle("-fx-background-color: #003e64");
                    else if(!cell.isEmpty()) cell.setStyle("-fx-background-color: #007eaf");
                });
                cell.setOnMouseExited(event -> {
                    if(!cell.isSelected()) cell.setStyle("-fx-background-color: #003459");
                    else cell.setStyle("-fx-background-color: #007eaf");
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
