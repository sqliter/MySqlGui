package ui;

import dao.Session;
import dao.SessionManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.*;

public class TestTableDataEditor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addRowItem = new MenuItem("添加行");
        MenuItem removeRowItem = new MenuItem("移除行");
        MenuItem editRowItem = new MenuItem("修改行");
        contextMenu.getItems().addAll(addRowItem,removeRowItem);

        TableView tableView = new TableView();
        SessionManager sm = new SessionManager();
        Session session = sm.getCurrentSession();
        if(session.connect()) {
            session.execute("use imooc");
            List rs = session.query("select * from user");
            if (rs.size() > 0) {
                tableView.setEditable(true);
                tableView.getSelectionModel().setCellSelectionEnabled(true);
                Callback<TableColumn<Map,String>,TableCell<Map,String>>
                        cellFactoryForMap = (TableColumn<Map,String> p) ->
                        new TextFieldTableCell<>(new StringConverter() {
                            @Override
                            public String toString(Object t) {
                                return t.toString();
                            }

                            @Override
                            public String fromString(String string) {
                                return string;
                            }
                        });

                int count = rs.size();
                HashMap row = (HashMap) rs.get(0);
                Set<String> keyset = row.keySet();
                for (String value : keyset) {
                    TableColumn<Map, String> column = new TableColumn<>(value);
                    column.setCellValueFactory(new MapValueFactory<>(value));
                    column.setCellFactory(cellFactoryForMap);
                    column.setOnEditCommit(
                            (TableColumn.CellEditEvent<Map, String> t) -> {
                                ((Map) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                ).put(value,t.getNewValue());
                            });
                    tableView.getColumns().add(column);
                }
                ObservableList<Map<String,String>> data = FXCollections.observableArrayList(rs);
                tableView.setItems(data);

                addRowItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("添加行");
                        Map rowData = new HashMap();//声明Map
                        for(String key : keyset){
                            rowData.put(key,"");
                        }
                        data.add(rowData);
                    }
                });
            }
            session.disconnect();
        }

        tableView.setContextMenu(contextMenu);

        Scene scene = new Scene(tableView,1024,600);
        stage.setTitle("表数据编辑");
        stage.setScene(scene);
        stage.show();
    }
}
