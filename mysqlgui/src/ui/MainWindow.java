package ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;

public class MainWindow extends Application {
    private MenuBar menuBar=new MenuBar();
    MenuItem openSessionItem = new MenuItem("打开会话");
    MenuItem closeSessionItem = new MenuItem("关闭会话");

    private ContextMenu schemaMenu=new ContextMenu();

    private TitleBar titleBar=new TitleBar();
    private SchemaPane schemaPane = new SchemaPane();
    private PageManager pageManager = new PageManager();
    private SplitPane mainPane=new SplitPane();
    private SplitPane midderPane=new SplitPane();
    private TabPane outputPane=new TabPane();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        List<String> stringList = new ArrayList<>();
        stringList.add("所有会话");
         titleBar.setTitles(stringList);

        initMenus();
        initMainPane();
        setSchemaMenu();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(menuBar,titleBar,mainPane);
        VBox.setVgrow(mainPane,Priority.ALWAYS);

        Image appIcon = new Image(getClass().getResourceAsStream("../images/mysql.png"));
        stage.setTitle("乐不思蜀");
        Scene scene = new Scene(vbox,1024,600);
        scene.getStylesheets().add("test.css");
        stage.setScene(scene);
        stage.getIcons().add(appIcon);
        stage.show();
    }
    private void initMenus(){
        //menuBar.setStyle("-fx-background-color: #6a6a6a;-fx-border-width: 1px;");

        Menu menuFile = new Menu("文件");
        ImageView icon = new ImageView(new Image("images/new.png"));
        icon.setFitWidth(24);
        icon.setFitHeight(24);
        openSessionItem.setGraphic(icon);
        openSessionItem.setOnAction((ActionEvent t) -> {
            schemaPane.openSession();
        });

        closeSessionItem.setOnAction((ActionEvent t) -> {
            System.out.println("关闭会话");
        });

        MenuItem exit = new MenuItem("退出");
        exit.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        exit.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });

        MenuItem sessions = new MenuItem("会话管理");
        MenuItem settings = new MenuItem("设置");
        MenuItem users = new MenuItem("用户管理");
        menuFile.getItems().addAll(openSessionItem, closeSessionItem, sessions,new SeparatorMenuItem(),
                users,settings,new SeparatorMenuItem(),exit);

        Menu menuEdit = new Menu("编辑");
        MenuItem cut = new MenuItem("剪切");
        MenuItem copy = new MenuItem("复制");
        MenuItem paste = new MenuItem("粘贴");
        MenuItem del = new MenuItem("删除");
        menuEdit.getItems().addAll(cut,copy,paste,del);

        Menu menuView = new Menu("视图");
        MenuItem schema = new MenuItem("架构");
        schema.setAccelerator(KeyCombination.keyCombination("Ctrl+0"));
        MenuItem terminal = new MenuItem("终端");
        terminal.setAccelerator(KeyCombination.keyCombination("Ctrl+2"));
        MenuItem run = new MenuItem("运行");
        run.setAccelerator(KeyCombination.keyCombination("Ctrl+1"));
        MenuItem statusbar = new MenuItem("状态栏");
        menuView.getItems().addAll(schema,run,terminal,new SeparatorMenuItem(),statusbar);

        Menu menuHelp = new Menu("帮助");
        MenuItem donate = new MenuItem("赞助");
        donate.setAccelerator(KeyCombination.keyCombination("Ctrl+$"));
        MenuItem about = new MenuItem("关于");
        menuHelp.getItems().addAll(donate,about);

        menuBar.getMenus().addAll(menuFile,menuEdit,menuView,menuHelp);

    }

    private void initMainPane() {
        schemaPane.setPrefWidth(320);

        midderPane.setOrientation(Orientation.HORIZONTAL);
        midderPane.getItems().addAll(schemaPane,pageManager);
        midderPane.setDividerPositions(0.25f,0.6f);

        outputPane.setPrefHeight(280);

        mainPane.setOrientation(Orientation.VERTICAL);
        mainPane.getItems().addAll(midderPane,outputPane);
        mainPane.setDividerPositions(0.7f,0.2f);

        SplitPane.setResizableWithParent(schemaPane,false);
        SplitPane.setResizableWithParent(outputPane,false);

        pageManager.setOnMouseClicked((Event e) -> {
            System.out.println("mouse clicked");
            if(!pageManager.getSelectionModel().isEmpty()){
                Node node= pageManager.getSelectionModel().getSelectedItem().getContent();
                titleBar.setTitles(((Page)node).getTitles());
            }
        });
    }

    private void setSchemaMenu(){
        schemaPane.openSession.setOnAction((ActionEvent e) -> {
            schemaPane.openSession();
        });
        schemaPane.editTableData.setOnAction((ActionEvent ae) -> {
            System.out.println("编辑表数据");
            String title = schemaPane.schemaView.getSelectionModel().getSelectedItem().getValue().title;
            Node node=pageManager.openPage(title,"edit_table_data");
            titleBar.setTitles(((Page)node).getTitles());
            if(null!=node){
                TableDataEditor tde = (TableDataEditor) node;
                Button add = new Button("增加行");
                Button remove = new Button("删除行");
                Button submit = new Button("提交");
                add.setOnAction(tde.addRowHandler);
                remove.setOnAction(tde.removeRowHandler);
                submit.setOnAction(tde.submitHandler);
                titleBar.addToolButton(add);
                titleBar.addToolButton(remove);
                titleBar.addToolButton(submit);
            }
        });

        schemaPane.editTableSchema.setOnAction((ActionEvent e) -> {
            String title = schemaPane.schemaView.getSelectionModel().getSelectedItem().getValue().title;
            Node node = pageManager.openPage(title,"edit_table_schema");
            titleBar.setTitles(((Page)node).getTitles());
        });
//        schemaMenu.setOnShowing((WindowEvent e) -> {
//            schemaPane.onMenuShowing();
//        });
        schemaMenu.getItems().addAll(schemaPane.openSession,
                schemaPane.editTableData,
                schemaPane.editTableSchema);
        schemaPane.setContextMenu(schemaMenu);
    }



}
