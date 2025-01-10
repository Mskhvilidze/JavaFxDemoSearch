package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.richtext.model.TwoDimensional;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CodeAreaController implements Initializable {
    public StackPane sp;
    public ScrollPane scroll;
    @FXML
    private DialogPane dp;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private TextField search;
    @FXML
    private CodeArea codeArea;
    private Stage stage;
    public static final String FXML = "/codeArea.fxml";
    private final File file = new File("C:\\Beka\\basic-structure.xml.xml");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        test();
        search.textProperty().addListener((obs, oldText, tt) -> {
            String newText = tt.toLowerCase();
            String content = codeArea.getText().toLowerCase();
            boolean isFound = false;
            if (newText.isEmpty()) {
                dp.setVisible(isFound);
                return;
            }
            int index = content.indexOf(newText.toLowerCase());
            List<String> contents = Arrays.stream(content.split("\n")).toList();
            Map<Integer, String> resultMap =
                    IntStream.range(0, contents.size()).parallel().filter(i -> contents.get(i).contains(newText.toLowerCase())).boxed()
                            .collect(Collectors.toMap(i -> i, contents::get));
            if (index != -1 && newText.length() > 3 && resultMap.size() > 0) {
                if (resultMap.size() > 1) {
                    isFound = true;
                }
                resultMap.forEach((key, value) -> {
                    int i = content.indexOf(value);
                    codeArea.foldText(i, i);
                    int paragraph = codeArea.offsetToPosition(i, TwoDimensional.Bias.Forward).getMajor();
                    codeArea.showParagraphAtCenter(paragraph);
                });
            }
            dp.setVisible(isFound);
            if (isFound) {
                TreeItem<String> root = new TreeItem<>("Root");
                root.setExpanded(true);

                resultMap.forEach((key, value) -> {
                    TreeItem<String> item = new TreeItem<>(value);
                    root.getChildren().add(item);
                });
                treeView.setRoot(root);
            }
        });

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.getValue().equals("Root")) {
                String value = newValue.getValue().toLowerCase();
                int i = codeArea.getText().toLowerCase().indexOf(value);
                codeArea.foldText(i, i);
                int paragraph = codeArea.offsetToPosition(i, TwoDimensional.Bias.Forward).getMajor();
                codeArea.showParagraphAtCenter(paragraph);
            }
        });
        Button closeButton = (Button) dp.lookupButton(ButtonType.CLOSE);
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(event -> dp.setVisible(false));

    }

    public void test() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            Node node = document.getDocumentElement();
            traverseNodes(node, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void traverseNodes(Node node, int indentLevel) {
        String indent = "  ".repeat(indentLevel); // Einrückung basierend auf der Ebene

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            int start = codeArea.getText().isEmpty() ? 0 : codeArea.getText().length();

            // Öffnendes Tag mit Einrückung
            codeArea.appendText(indent + "<" + node.getNodeName());
            codeArea.setStyleSpans(start, computeHighlighting("tag", codeArea.getText().length() - start));

            // Attribute einfügen
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                codeArea.appendText(" " + attribute.getNodeName() + "=\"" + attribute.getNodeValue() + "\"");
                int attrStart = codeArea.getText().length() - (attribute.getNodeName().length() + attribute.getNodeValue().length() + 3);
                codeArea.setStyleSpans(attrStart, computeHighlighting("attribute", attribute.getNodeName().length()));
            }

            codeArea.appendText(">\n");

            // Kinderknoten einfügen
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                traverseNodes(children.item(i), indentLevel + 1);
            }

            // Schließendes Tag
            start = codeArea.getText().length();
            codeArea.appendText(indent + "</" + node.getNodeName() + ">\n");
            codeArea.setStyleSpans(start, computeHighlighting("tag", codeArea.getText().length() - start));
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            String textContent = node.getTextContent().trim();
            if (!textContent.isEmpty()) {
                int start = codeArea.getText().length() + indentLevel;
                codeArea.appendText(indent + textContent + "\n");
                codeArea.setStyleSpans(start, computeHighlighting("content", textContent.length() + indentLevel));
            }
        }
    }


    private StyleSpans<Collection<String>> computeHighlighting(String tag, int length) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        if (tag.equals("tag")) {
            spansBuilder.add(Collections.singleton(tag), length);
        } else if (tag.equals("content")) {
            spansBuilder.add(Collections.singleton(tag), length);
        } else {
            spansBuilder.add(Collections.singleton(tag), length);
        }
        return spansBuilder.create();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void closeStage() {
        stage.close();
    }

    @FXML
    private void onSaveFile(ActionEvent event) {
        // Der Text aus dem CodeArea
        String content = codeArea.getText();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Schreibe den Text aus CodeArea in die Datei
            writer.write(content);
            System.out.println("Datei wurde erfolgreich gespeichert: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveASFile() {
        String content = codeArea.getText();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File fileSave = fileChooser.showSaveDialog(null);

        if (fileSave != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileSave))) {
                // Schreibe den Text aus CodeArea in die Datei
                writer.write(content);
                System.out.println("Datei wurde erfolgreich gespeichert: " + fileSave.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
