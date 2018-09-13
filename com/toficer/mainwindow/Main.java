package com.toficer.mainwindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.initStyle(StageStyle.UNDECORATED);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainwindow.fxml"));
        Parent root = loader.load();
        try{
            primaryStage.getIcons().add(new Image("images/trayicon.png"));
        }
        catch (Exception e){
            System.err.println("Couldn't load the tray icon!");
        }

        MainWindowController controller = (MainWindowController)loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setTitle("Study Helper FX");
        Scene scene = new Scene(root, 1200, 900);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
