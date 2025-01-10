package com.example.demo.manager;

import com.example.demo.CodeAreaController;
import com.example.demo.HelloController;
import com.example.demo.TreePresenter;
import com.example.demo.message.CreateStage;
import com.example.demo.message.CreateTree;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ScannerManager {
    private Stage primaryStage;
    private Scene scene;

    public ScannerManager(Stage stage) throws IOException {
        this.primaryStage = stage;
        initView();
    }

    public ScannerManager() {

    }

    private void initView() throws IOException {
        showScene();
    }


    private void showScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(HelloController.FXML));
        Parent rootParent = loader.load();
        HelloController presenter = loader.getController();
        Platform.runLater(() -> {
            primaryStage.initStyle(StageStyle.UTILITY);
            primaryStage.setTitle("Platform");
            scene = new Scene(rootParent, 900, 750);
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    @Subscribe
    public void onSendCreateStage(CreateStage createStage) throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(CodeAreaController.FXML));
        Parent parent = loader.load();
        CodeAreaController codeAreaController = loader.getController();
        Platform.runLater(() -> {
            newStage.setTitle("CodeArea");
            scene = new Scene(parent, 600, 600);
            codeAreaController.setStage(newStage);
            newStage.setMinHeight(550.0);
            newStage.setMinWidth(550.0);
            newStage.setScene(scene);
            newStage.show();
        });
        newStage.setOnCloseRequest(event -> codeAreaController.closeStage());
    }

    @Subscribe
    public void onSendCreateTreeStage(CreateTree createTree) throws IOException {
        Stage newStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(TreePresenter.FXML));
        Parent parent = loader.load();
        TreePresenter codeAreaController = loader.getController();
        Platform.runLater(() -> {
            newStage.setTitle("Tree");
            scene = new Scene(parent, 385.0, 588.0);
            codeAreaController.setStage(newStage);
            newStage.setScene(scene);
            newStage.show();
        });
        newStage.setOnCloseRequest(event -> codeAreaController.closeStage());
    }
}
