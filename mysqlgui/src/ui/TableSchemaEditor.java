package ui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class TableSchemaEditor extends TableView implements Page{
    private String tableName ="";
    private List<String> titles = new ArrayList<>();

    @Override
    public List<String> getTitles() {
        return titles;
    }

    @Override
    public void setTitles(List<String> titles) {
        this.titles = titles;
    }


    public TableSchemaEditor() {
        TableColumn nameCol = new TableColumn("名称");
        TableColumn typeCol = new TableColumn("类型");
        TableColumn defaultValueCol = new TableColumn("默认值");
        TableColumn enableNullCol = new TableColumn("允许空");
        this.getColumns().addAll(nameCol,typeCol,defaultValueCol,enableNullCol);
    }
    public void setTableName(String table_name) {
        this.tableName = table_name;
        requery();
    }
    public void requery() {

    }
}
