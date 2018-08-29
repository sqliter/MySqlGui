package ui;

import dao.Session;
import dao.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.*;

public class TableDataEditor extends TableView implements Page{
    private String tableName="";

    ContextMenu contextMenu = new ContextMenu();
    MenuItem addRowItem = new MenuItem("添加行");
    MenuItem removeRowItem = new MenuItem("移除行");
    public EventHandler<ActionEvent> addRowHandler,removeRowHandler,submitHandler;

    private List<String> titles = new ArrayList<>();

    @Override
    public List<String> getTitles() {
        return titles;
    }

    @Override
    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public void setTableName(String table_name){
        this.tableName = table_name;
        requery();
    }
    public void requery() {
        SessionManager sm = new SessionManager();
        Session session = sm.getCurrentSession();
        if(session.connect()) {
            String sql = "select * from " + tableName;
            List rs = session.query(sql);
            if (rs.size() > 0) {
                this.setEditable(true);
                this.getSelectionModel().setCellSelectionEnabled(true);
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
                    this.getColumns().add(column);
                }
                ObservableList<Map<String,String>> data = FXCollections.observableArrayList(rs);
                this.setItems(data);

                addRowHandler = (ActionEvent e) -> {
                    System.out.println("添加行");
                    Map rowData = new HashMap();//声明Map
                    for(String key : keyset){
                        rowData.put(key,"");
                    }
                    data.add(rowData);
                };
                removeRowHandler = (ActionEvent event) -> {
                    System.out.println("删除行");
                };
                addRowItem.setOnAction(addRowHandler);
                removeRowItem.setOnAction(removeRowHandler);

            }
            session.disconnect();
        }
    }
//    public void addRow() {
//
//    }
//    public void removeRow() {
//
//    }
}
