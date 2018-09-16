package com.toficer.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class DataModel {

    public static final String DATABASE_TYPE_SQLITE = "jdbc:sqlite:";
    public static final String DATABASE_TYPE_MYSQL = "jdbc:mysql:";
    public static final String DATABASE_NAME_SQLITE = "cardbase.db";
    public static final String DATABASE_PATH_SQLITE = "C:\\Users\\Toficer\\Documents\\StudyHelperData\\";
    public static final String DATABASE_NAME_MYSQL = "studyhelper";
    public static final String DATABASE_PATH_MYSQL = "//db4free.net:3306/";
    public static final String CONNECTION_STRING = DATABASE_TYPE_SQLITE + DATABASE_PATH_SQLITE + DATABASE_NAME_SQLITE;

    public static final String FOLDER_TABLE = "SH_Folders";
    public static final String FOLDER_ID = "_id";
    public static final String FOLDER_DESC = "description";
    public static final String FOLDER_NAME = "name";

    public static final String DECK_TABLE = "SH_Decks";
    public static final String DECK_ID = "_id";
    public static final String DECK_DESC = "description";
    public static final String DECK_NAME = "name";
    public static final String DECK_FOLDERID = "folderid";

    public static final String CARD_TABLE = "SH_Cards";
    public static final String CARD_ID = "_id";
    public static final String CARD_QUESTION = "question";
    public static final String CARD_ANSWER = "answer";
    public static final String CARD_DECKID = "deckid";
    public static final String CARD_TYPE = "type";

    public static final String TYPE_SIMPLETEXT = "simpletext";
    public static final String TYPE_MULTIPLECHOICE = "multiplechoice";

    // SQLite and MySQL syntax differs a little. To make it easier to switch between the two for the duration of the production,
    // SQL queries for creating new tables are stored in Strings below and passed to statements.
    // Also, his way they can be easily edited without the need to search for them in the code.

    public static final String MYSQL_FOLDER_CREATETABLE = "CREATE TABLE IF NOT EXISTS " + FOLDER_TABLE + "  (" + FOLDER_ID +
            " INTEGER AUTO_INCREMENT, " + FOLDER_NAME + " TEXT, " + FOLDER_DESC + " TEXT, PRIMARY KEY (" + FOLDER_ID + "))";

    public static final String SQLITE_FOLDER_CREATETABLE = "CREATE TABLE IF NOT EXISTS " + FOLDER_TABLE + "  (" + FOLDER_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + FOLDER_NAME + " STRING, " + FOLDER_DESC + " STRING)";
    public static final String SQLITE_DECK_CREATETABLE = "CREATE TABLE IF NOT EXISTS " + DECK_TABLE + "  (" + DECK_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + DECK_NAME + " STRING, " + DECK_DESC + " STRING, " + DECK_FOLDERID + " STRING)";
    public static final String SQLITE_CARD_CREATETABLE = "CREATE TABLE IF NOT EXISTS " + CARD_TABLE + "  (" + CARD_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + CARD_QUESTION + " STRING, " + CARD_ANSWER + " STRING, "+ CARD_TYPE + " STRING, " + CARD_DECKID + " INTEGER)";


    private static DataModel data = new DataModel();
    private ObservableList<Folder> currentListViewFolders;
    private Folder currentFolder;
    private ObservableList<Deck> currentListViewDecks;
    private ArrayList<Deck> currentDeckList;
    private Deck currentDeck;
    private ObservableList<Card> currentListViewCards;
    private ArrayList<Card> studySession;
    private Card currentCard;
    private Connection connection;

    public static DataModel getData() {
        return data;
    }

    public ObservableList<Folder> getListViewFolders() {
        return currentListViewFolders;
    }
    public ObservableList<Deck> getListViewDecks() { return currentListViewDecks; }
    public ObservableList<Card> getListViewCards() {
        return currentListViewCards;
    }

    private DataModel() {
        openConnection();
    }

    public boolean openConnection(){
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            System.out.println("CONNECTION FAILED: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void closeConnection(){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public Folder getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(Folder currentFolder) {
        this.currentFolder = currentFolder;
    }

    public ArrayList<Card> getStudySession() {
        return studySession;
    }

    public Deck getCurrentDeck() {
        return currentDeck;
    }

    public void setCurrentDeck(Deck currentDeck) {
        this.currentDeck = currentDeck;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public void loadFolders(){
        currentListViewFolders = FXCollections.observableArrayList();
        populateFolderListView();
    }

    public void loadDecks(Folder folder){
        currentListViewDecks = FXCollections.observableArrayList();
        populateDeckListView(folder.get_id());
    }

    public void loadCards(Deck deck){
        currentListViewCards = FXCollections.observableArrayList();
        populateCardListView(deck.get_id());
    }

    public void populateFolderListView(){
        try {
            Statement stmt = connection.createStatement();

            stmt.execute(SQLITE_FOLDER_CREATETABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + FOLDER_TABLE);
            String name, desc;
            int id;
            if(rs != null){
                while(rs.next()){
                    id = rs.getInt(FOLDER_ID);
                    name = rs.getString(FOLDER_NAME);
                    desc = rs.getString(FOLDER_DESC);
                    currentListViewFolders.add(new Folder(id, name, desc));
                }
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateDeckListView(int folder_id){
        try {
            currentListViewDecks.clear();
            Statement stmt = connection.createStatement();

            stmt.execute(SQLITE_DECK_CREATETABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DECK_TABLE + " WHERE " + DECK_FOLDERID + "=" + folder_id);
            if(rs != null){
                while(rs.next()){
                    currentListViewDecks.add(new Deck(rs.getInt(DECK_ID), rs.getString(DECK_NAME), rs.getString(DECK_DESC), folder_id));
                }
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateCardListView(int deck_id){
        try {
            currentListViewCards.clear();
            Statement stmt = connection.createStatement();

            stmt.execute(SQLITE_CARD_CREATETABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + CARD_TABLE + " WHERE " + CARD_DECKID + "=" + deck_id);
            while(rs.next()){
                if(rs.getString(CARD_TYPE).equals(TYPE_SIMPLETEXT)){
                    currentListViewCards.add(new SimpleTextCard(rs.getInt(CARD_ID), rs.getString(CARD_QUESTION), rs.getString(CARD_ANSWER), deck_id, TYPE_SIMPLETEXT));
                }
                if(rs.getString(CARD_TYPE).equals(TYPE_MULTIPLECHOICE)){
                    currentListViewCards.add(new MultipleChoiceCard(rs.getInt(CARD_ID), rs.getString(CARD_QUESTION), rs.getString(CARD_ANSWER), deck_id, TYPE_MULTIPLECHOICE));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Deck> getDeckList(int folder_id){
        currentDeckList = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();

            stmt.execute(SQLITE_DECK_CREATETABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DECK_TABLE + " WHERE " + DECK_FOLDERID + "=" + folder_id);
            if(rs != null){
                while(rs.next()){
                    currentDeckList.add(new Deck(rs.getInt(DECK_ID), rs.getString(DECK_NAME), rs.getString(DECK_DESC), folder_id));
                }
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currentDeckList;
    }

    public void addFolder(Folder folder){

        try {
            PreparedStatement stmt = connection.prepareStatement(SQLITE_FOLDER_CREATETABLE);
            stmt.execute();
            stmt = connection.prepareStatement("INSERT INTO " + FOLDER_TABLE +
                    " (" + FOLDER_NAME + ", " + FOLDER_DESC + ") VALUES ('" + folder.getName() +
                    "', '" + folder.getDescription() + "')", Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(1);
            folder.set_id(key);
            currentListViewFolders.add(folder);
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void updateFolder(Folder folder){
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("UPDATE " + FOLDER_TABLE +
                    " SET " + FOLDER_NAME + "='" + folder.getName() + "', " +
                    FOLDER_DESC + "='" + folder.getDescription() + "' WHERE " +
                    FOLDER_ID + "=" + folder.get_id());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void updateDeck(Deck deck){
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("UPDATE " + DECK_TABLE +
                    " SET " + DECK_NAME + "='" + deck.getName() + "', " +
                    DECK_DESC + "='" + deck.getDescription() + "', " +
                    DECK_FOLDERID + "=" + deck.getFolderId() + " WHERE " +
                    DECK_ID + "=" + deck.get_id());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void updateCard(Card card){
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("UPDATE " + CARD_TABLE +
                    " SET " + CARD_QUESTION + "='" + card.getQuestionStringRepresentation() + "', " +
                    CARD_ANSWER + "='" + card.getAnswerStringRepresentation() + "', " +
                    CARD_TYPE + "='" + card.getCardType() + "', " +
                    CARD_DECKID + "=" + card.getDeckid() + " WHERE " +
                    CARD_ID + "=" + card.get_id());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void addDeck(Deck deck){

        try {
            PreparedStatement stmt = connection.prepareStatement(SQLITE_DECK_CREATETABLE);
            stmt.execute();
            stmt = connection.prepareStatement("INSERT INTO " + DECK_TABLE +
                    " (" + DECK_NAME + ", " + DECK_DESC + ", " + DECK_FOLDERID + ") VALUES ('" + deck.getName() +
                    "', '" + deck.getDescription() + "', '" + deck.getFolderId() + "')", Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(1);
            deck.set_id(key);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void addCard(Card card){
        try {
            PreparedStatement stmt = connection.prepareStatement(SQLITE_CARD_CREATETABLE);
            stmt.execute();
            stmt = connection.prepareStatement("INSERT INTO " + CARD_TABLE +
                    " (" + CARD_QUESTION + ", " + CARD_ANSWER + ", " + CARD_TYPE + ", " + CARD_DECKID + ") VALUES ('" + card.getQuestionStringRepresentation() +
                    "', '" + card.getAnswerStringRepresentation() + "', '" + card.getCardType() + "', " + card.getDeckid() + ")", Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(1);
            card.set_id(key);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<Card> openStudySession(int deck_id){
        studySession = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) AS count FROM " + CARD_TABLE + " WHERE " + CARD_DECKID + "=" + deck_id);
            int count;
            count = rs.getInt("count");
            if(count>0){
                rs = stmt.executeQuery("SELECT * FROM " + CARD_TABLE + " WHERE " + CARD_DECKID + "=" + deck_id);
                while(rs.next()){
                    if(rs.getString(CARD_TYPE).equals(TYPE_SIMPLETEXT)){
                        Card card = new SimpleTextCard(rs.getInt(CARD_ID), rs.getString(CARD_QUESTION), rs.getString(CARD_ANSWER), deck_id, TYPE_SIMPLETEXT);
                        studySession.add(card);
                    }
                    if(rs.getString(CARD_TYPE).equals(TYPE_MULTIPLECHOICE)){
                        Card card = new MultipleChoiceCard(rs.getInt(CARD_ID), rs.getString(CARD_QUESTION), rs.getString(CARD_ANSWER), deck_id, TYPE_MULTIPLECHOICE);
                        studySession.add(card);
                    }
                }
                return studySession;
            }
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFolder(int folder_id){
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DECK_TABLE + " WHERE " + DECK_FOLDERID + "=" + folder_id);
            while(rs.next()){
                stmt.execute("DELETE FROM " + CARD_TABLE + " WHERE " + CARD_DECKID + "=" + rs.getInt(DECK_ID));
            }
            stmt.execute("DELETE FROM " + DECK_TABLE + " WHERE " + DECK_FOLDERID + "=" + folder_id);
            stmt.execute("DELETE FROM " + FOLDER_TABLE + " WHERE " + FOLDER_ID + "=" + folder_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDeck(int deck_id){
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM " + CARD_TABLE + " WHERE " + CARD_DECKID + "=" + deck_id);
            stmt.execute("DELETE FROM " + DECK_TABLE + " WHERE " + DECK_ID + "=" + deck_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
