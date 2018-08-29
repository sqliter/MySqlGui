package ui;

import dao.Session;
import dao.SessionManager;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;

public class SchemaPane extends VBox {
    public TreeItem<SchemaItem> rootNode = new TreeItem<>(new SchemaItem("所有会话"));
    public TreeView<SchemaItem> schemaView = new TreeView(rootNode);
    public MenuItem openSession = new MenuItem("打开会话");
    public MenuItem editTableData = new MenuItem("修改表数据");
    public MenuItem editTableSchema = new MenuItem("修改表架构");

    public SchemaPane(){
        rootNode.setExpanded(true);
        rootNode.getValue().type="root";
        rootNode.getValue().title="所有会话";

        HBox hBox = new HBox();
        hBox.setSpacing(10);

        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("所有数据库","MySQL数据库",
                "SQLite数据库","MongoDB数据库");
        comboBox.setValue("MySQL数据库");
        comboBox.getStyleClass().add("noborder");
        hBox.getStyleClass().add("bghui");
        //HBox.setHgrow(comboBox,Priority.ALWAYS);
        hBox.getChildren().addAll(comboBox);

        VBox.setVgrow(schemaView,Priority.ALWAYS);
        getChildren().addAll(hBox,schemaView);


        schemaView.setOnContextMenuRequested((ContextMenuEvent e) -> {
            onMenuShowing();
        });
    }

    public void openSession() {
        //1,依据传进来的会话名称获取会话配置，创建对应配置会话对象
        SessionManager sm = new SessionManager();
        Session session = sm.getCurrentSession();

        SchemaItem schemaItem;
        //2,连接
        if(session.connect()){
            //添加一个会话节点到树控件中
            schemaItem = new SchemaItem(session.getName());
            schemaItem.type="session";
            schemaItem.title=session.getName();
            TreeItem<SchemaItem> sessionItem = new TreeItem<>(schemaItem);
            sessionItem.setExpanded(true);
            rootNode.getChildren().add(sessionItem);
            //3,查询数据库列表，
            List list=session.query("show databases");
            if(list.size()==0)return;
            // 4,将数据库列表作为会话节点的子节点创建添加。
            int rowcount= list.size();

            HashMap row,table_row;
            TreeItem<SchemaItem> dbitem,table_item;
            List tablelist;
            String dbname="",table_name="";
            for(int i=0;i<rowcount;i++){
                row = (HashMap) list.get(i);
                dbname = row.get("SCHEMA_NAME").toString();
                schemaItem=new SchemaItem(dbname);
                schemaItem.type="database";
                schemaItem.title=session.getName() + "." + dbname;
                dbitem = new TreeItem<>(schemaItem);
                sessionItem.getChildren().add(dbitem);
                //5,查询数据库中的表列表并作为数据库节点的子节点添加到树控件中
                tablelist = session.query("show tables from " + dbname );
                int count = tablelist.size();
                for(int j=0;j<count;j++){
                    table_row = (HashMap)tablelist.get(j);
                    table_name = table_row.get("TABLE_NAME").toString();
                    schemaItem = new SchemaItem(table_name);
                    schemaItem.type="table";
                    schemaItem.title = dbitem.getValue().title + "." + table_name;
                    table_item = new TreeItem<>(schemaItem);
                    dbitem.getChildren().add(table_item);
                }

            }

        }
        session.disconnect();
    }

    public void setContextMenu(ContextMenu contextMenu) {
        schemaView.setContextMenu(contextMenu);
    }

    public void onMenuShowing() {
        System.out.println("会话架构树菜单显示中...");
        try {
            SchemaItem item = schemaView.getSelectionModel().getSelectedItem().getValue();
            //String type = item.getValue().toString();
            if(item.type.equals("root")){
                System.out.println("根节点");
                openSession.setVisible(true);
                editTableData.setVisible(false);
                editTableSchema.setVisible(false);
            }
            else if(item.type.equals("session")){
                System.out.println("会话节点");
                openSession.setVisible(false);
                editTableData.setVisible(false);
                editTableSchema.setVisible(false);
            }
            else if(item.type.equals("database")){
                System.out.println("数据库节点");
                openSession.setVisible(false);
                editTableData.setVisible(false);
                editTableSchema.setVisible(false);
            }
            else if (item.type.equals("table")){
                System.out.println("表节点");
                openSession.setVisible(false);
                editTableData.setVisible(true);
                editTableSchema.setVisible(true);
            }
        }catch (NullPointerException e){
            System.out.println("没有选中节点");
            openSession.setVisible(true);
            editTableData.setVisible(false);
            editTableSchema.setVisible(false);
        }
    }

    public class SchemaItem{
        public String text="item";
        public String type="unknown";
        public String title="";

        public SchemaItem(String _text){
            this.text = _text;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}
