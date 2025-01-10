package com.example.demo;

import com.example.demo.manager.ScannerManager;
import com.example.demo.message.CreateStage;
import com.example.demo.message.CreateTree;
import com.example.demo.model.FileInfo;
import com.google.common.eventbus.EventBus;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import java.awt.Desktop;

public class HelloController implements Initializable {
    private ObservableList<FileInfo> observalbeFiles;
    public static final String FXML = "/view.fxml";
    @FXML
    private TextField search;
    @FXML
    private TableView<FileInfo> tableView;
    @FXML
    private TableColumn<FileInfo, String> fileName;
    @FXML
    private TableColumn<FileInfo, String> fileSize;
    @FXML
    private TableColumn<FileInfo, String> fileDate;
    @javafx.fxml.FXML
    private ImageView headerImage;
    private EventBus bus = new EventBus();

    public HelloController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.bus.register(new ScannerManager());
        Image image = new Image(String.valueOf(getClass().getResource("/image/OIP.jpg")), 910, 367, true, false);
        this.headerImage.setImage(image);
        iniTable();
    }

    public void iniTable() {
        fileName.setCellValueFactory(new PropertyValueFactory<>("FileName"));
        fileSize.setCellValueFactory(new PropertyValueFactory<>("FileSize"));
        fileDate.setCellValueFactory(new PropertyValueFactory<>("FileDate"));
    }

    /**
     * Holt alle Dateien aus einem Verzeichnis und gibt sie als ObservableList zurück.
     */
    private ObservableList<FileInfo> getFilesFromDirectory(File directory) {
        ObservableList<FileInfo> fileList = FXCollections.observableArrayList();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                fileList.add(new FileInfo(file.getName(), formatFileSize(file.length()),
                        new SimpleDateFormat("dd.MM.yyyy").format(file.lastModified())));
            }
        }
        return fileList;
    }

    private String formatFileSize(long sizeInBytes) {
        if (sizeInBytes < 1024) {
            return sizeInBytes + " B";
        } else if (sizeInBytes < 1024 * 1024) {
            return String.format("%.2f KB", sizeInBytes / 1024.0);
        } else if (sizeInBytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", sizeInBytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", sizeInBytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    @FXML
    private void onCreateTable() {
        observalbeFiles = getFilesFromDirectory(new File("C:\\Beka\\Softwaretechnik\\Vorlesungen\\"));
        tableView.setItems(observalbeFiles);
        System.out.println(tableView.getColumns().size());
    }

    public void fileClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            FileInfo selectedFile = tableView.getSelectionModel().getSelectedItem();
            if (selectedFile != null) {
                File file = new File("C:\\Beka\\Softwaretechnik\\Vorlesungen\\" + selectedFile.getFileName()); // Pfad ergänzen
                if (file.exists()) {
                    try {
                        Desktop.getDesktop().open(file); // Datei mit Standardprogramm öffnen
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Datei existiert nicht: " + file.getAbsolutePath());
                }
            }
        }
    }

    @FXML
    private void onTyped() {
        String searchText = this.search.textProperty().get();
        if (searchText.isEmpty()) {
            tableView.setItems(observalbeFiles);
        } else {
            ObservableList<FileInfo> ff = FXCollections.observableArrayList(
                    observalbeFiles.parallelStream()
                            .filter(f -> f.getFileName().contains(searchText))
                            .toList()
            );
            tableView.setItems(ff);
        }
    }

    @FXML
    public void onCreateStage() {
        this.bus.post(new CreateStage());
    }

    @FXML
    private void onCreateTree() {
        this.bus.post(new CreateTree());
    }
}