package ui;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

public class TitleBar extends HBox {
    protected PathTitle pathTitle;
    protected ToolBar toolBar;

    public TitleBar() {
        pathTitle = new PathTitle();
        toolBar = new ToolBar();
        getChildren().addAll(pathTitle,toolBar);
        HBox.setHgrow(pathTitle,Priority.ALWAYS);
        setMinHeight(28);
    }

    public void setTitles(List<String> ts) {
        pathTitle.setTitles(ts);
    }

    public void addToolButton(Node node) {
        toolBar.getItems().add(node);
    }
}
