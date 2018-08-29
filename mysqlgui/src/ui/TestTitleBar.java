package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TestTitleBar extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        PathTitle pathTitle = new PathTitle();
        List<String> stringList = new ArrayList<>();
        stringList.add("mysqlgui");
        stringList.add("src");
        stringList.add("ui");
        pathTitle.setTitles(stringList);

        HBox hbox = new HBox();
        ToolBar toolBar = new ToolBar();
        toolBar.setStyle("-fx-background-color: #6a6a6a");
        toolBar.setPadding(new Insets(3.0,10.0,3.0,10));
        Separator separator = new Separator();
        //separator.setPrefWidth(0.6);
        //separator.setStyle("-fx-border-width: 1;-fx-border-color: #bdbdbd");
        toolBar.getItems().addAll(separator,new ToolItem("add"),
                new ToolItem("del"),new ToolItem("edit"));
        hbox.getChildren().addAll(pathTitle,toolBar);
        HBox.setHgrow(pathTitle,Priority.ALWAYS);

        VBox vbox = new VBox();
        Pane pane = new Pane();
        vbox.getChildren().addAll(hbox,pane);
        VBox.setVgrow(pane,Priority.ALWAYS);

        stage.setTitle("标题栏");
        Scene scene = new Scene(vbox, 800,480);
        stage.setScene(scene);
        stage.show();
    }

    public class ToolItem extends Button {
        public ToolItem(String text){
            super(text);
            setStyle("-fx-background-color: #6a6a6a");
        }
    }


}
