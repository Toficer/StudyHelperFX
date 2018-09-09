package com.toficer.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class DataModel {

    public static String DATABASE_TYPE = "jdbc:sqlite:";
    public static String DATABASE_NAME = "cardbase.db";
    public static String DATABASE_PATH = "C:\\Users\\Toficer\\Documents\\StudyHelperData\\";
//    public static String DATABASE_PATH = "";
    public static String CONNECTION_STRING = DATABASE_TYPE + DATABASE_PATH + DATABASE_NAME;
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

    private static DataModel data = new DataModel();
    private ObservableList<Folder> currentListViewFolders;
    private Folder currentFolder;
    private ObservableList<Deck> currentListViewDecks;
    private ArrayList<Deck> currentDeckList;
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

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public void loadFolders(){
        currentListViewFolders = FXCollections.observableArrayList();
        populateFolderList();
    }

    public void loadDecks(Folder folder){
        currentListViewDecks = FXCollections.observableArrayList();
        int id = folder.get_id();
        populateDeckListView(id);
    }

    public void populateFolderList(){
        try {
            Statement stmt = connection.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS " + FOLDER_TABLE + "  (" + FOLDER_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + FOLDER_NAME + " STRING, " + FOLDER_DESC + " STRING)");
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

            stmt.execute("CREATE TABLE IF NOT EXISTS " + DECK_TABLE + "  (" + DECK_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + DECK_NAME + " STRING, " + DECK_DESC + " STRING, " + DECK_FOLDERID + " STRING)");

            System.out.println("SELECT * FROM " + DECK_TABLE + " WHERE " + DECK_FOLDERID + "=" + folder_id);
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DECK_TABLE + " WHERE " + DECK_FOLDERID + "=" + folder_id);
            String name, desc;
            int id;
            if(rs != null){
                while(rs.next()){
                    id = rs.getInt(DECK_ID);
                    name = rs.getString(DECK_NAME);
                    desc = rs.getString(DECK_DESC);

                    currentListViewDecks.add(new Deck(id, name, desc, folder_id));
                }
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Deck> getDeckList(int folder_id){
        currentDeckList = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS " + DECK_TABLE + "  (" + DECK_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + DECK_NAME + " STRING, " + DECK_DESC + " STRING, " + DECK_FOLDERID + " STRING)");

            System.out.println("SELECT * FROM " + DECK_TABLE + " WHERE " + DECK_FOLDERID + "=" + folder_id);
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DECK_TABLE + " WHERE " + DECK_FOLDERID + "=" + folder_id);
            String name, desc;
            int id;
            if(rs != null){
                while(rs.next()){
                    id = rs.getInt(DECK_ID);
                    name = rs.getString(DECK_NAME);
                    desc = rs.getString(DECK_DESC);

                    currentDeckList.add(new Deck(id, name, desc, folder_id));
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
            PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + FOLDER_TABLE + "  (" + FOLDER_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + FOLDER_NAME + " STRING, " + FOLDER_DESC + " STRING)");
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
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void addDeck(Deck deck){

        try {
            PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + DECK_TABLE + "  (" + DECK_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + DECK_NAME + " STRING, " + DECK_DESC + " STRING, " + DECK_FOLDERID + " STRING)");
            stmt.execute();
            stmt = connection.prepareStatement("INSERT INTO " + DECK_TABLE +
                    " (" + DECK_NAME + ", " + DECK_DESC + ", " + DECK_FOLDERID + ") VALUES ('" + deck.getName() +
                    "', '" + deck.getDescription() + "', '" + deck.getFolderId() + "')", Statement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(1);
            deck.set_id(key);
            if(currentListViewDecks != null){
                currentListViewDecks.add(deck);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void addCard(Card card){
        try {
            PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + CARD_TABLE + "  (" + CARD_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + CARD_QUESTION + " STRING, " + CARD_ANSWER + " STRING, "+ CARD_TYPE + " STRING, " + CARD_DECKID + " INTEGER)");
            stmt.execute();
            stmt = connection.prepareStatement("INSERT INTO " + CARD_TABLE +
                    " (" + CARD_QUESTION + ", " + CARD_ANSWER + ", " + CARD_TYPE + ", " + CARD_DECKID + ") VALUES ('" + card.getQuestion() +
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
            System.out.println("SELECT count(*) FROM " + CARD_TABLE + " WHERE " + CARD_DECKID + "=" + deck_id + "    COUNT " + count);
            if(count>0){
                rs = stmt.executeQuery("SELECT * FROM " + CARD_TABLE + " WHERE " + CARD_DECKID + "=" + deck_id);
                String answer;
                String question;
                int _id;
                while(rs.next()){
                    System.out.println(rs.getString(CARD_TYPE));
                    if(rs.getString(CARD_TYPE).equals(TYPE_SIMPLETEXT)){
                        Card card = new SimpleTextCard();
                        card.setAnswerStringRepresentation(rs.getString(CARD_ANSWER));
                        card.set_id(rs.getInt(CARD_ID));
                        card.setCardType(TYPE_SIMPLETEXT);
                        card.setDeckid(deck_id);
                        card.setQuestion(rs.getString(CARD_QUESTION));
                        card.setAnswerStatus(false);
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
