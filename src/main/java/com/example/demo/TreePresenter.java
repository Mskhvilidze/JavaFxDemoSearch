package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class TreePresenter implements Initializable {
    public static final String FXML = "/tree.fxml";
    @FXML
    private TreeView treeView;
    private Stage stage;
    private TreeItem<String> root = new TreeItem<>();
    private StringBuilder sb = new StringBuilder();
    private static int counter = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("C:\\Beka\\basic-structure.xml.xml");
        loadXML(file);
    }

    public void loadXML(File file) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            Node rootNode = document.getDocumentElement();
            root.setValue(rootNode.getNodeName());
            root.setExpanded(true);
            treeView.setEditable(true);
            treeView.setRoot(root);

            traverseNodes(rootNode, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void traverseNodes(Node node, TreeItem<String> parentItem) {
        TreeItem<String> currentItem = new TreeItem<>(formatNode(node));
        currentItem.setExpanded(true);
        parentItem.getChildren().add(currentItem);
    }

    private String formatNode(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            StringBuilder result = new StringBuilder();
            result.append("<").append(node.getNodeName());
            if (node.hasAttributes()) {
                NamedNodeMap attributes = node.getAttributes();
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node attribute = attributes.item(i);
                    result.append(" ")
                            .append(attribute.getNodeName()).append("=\"").append(attribute.getNodeValue()).append("\" ");
                }
                result.append(">");
            } else {
                result.append("<").append(node.getNodeName()).append(">");
            }
            return result.toString();
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            return node.getTextContent().trim();
        }
        return node.getNodeName();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void closeStage() {
        stage.close();
    }

}
