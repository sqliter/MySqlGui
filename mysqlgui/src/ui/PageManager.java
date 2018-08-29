package ui;

//import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/*
我们这个设计将PageManager直接继承自TabPane，可能这是不合理的。因为tabpane提供了
让我们操纵里面的tab的方法。而我们这里的tab，也就是page，是不可见的。外在使用者只
可以通过数据库对象的名称和请求操作的名称作为关键字来激活page，或者添加page。而且
加完了也不能再操作了。不过这样严格的限制访问是否合理真是未知数，说不定确实有需要
知道page对象并进行访问的时候，如此想的话，提供一个访问通道也是不错的。
暂时就这么弄吧
 */
public class PageManager extends TabPane {
    protected TreeMap<String ,Tab> pageMap = new TreeMap<>();

//    public PageManager() {
//
//        setPadding(new Insets(0,1,0,1));
//    }
    public Node openPage(String title,String request_name){
        ARetValue value = getSomething(title);
        String obj_name = value.obj_name;

        String key = obj_name + "|" + request_name;
        //查询或者创建page
        if(pageMap.containsKey(key)){
            for(Tab tab:this.getTabs()){
                if(tab.getUserData().equals(key)){
                    this.getSelectionModel().select(tab);
                    return tab.getContent();
                }
            }
        }else {
            Node page = createPage(value,request_name);
            Tab tab = new Tab();
            tab.setText(obj_name);
            tab.setContent(page);
            tab.setUserData(key);
            this.getTabs().add(tab);
            this.getSelectionModel().select(tab);
            pageMap.put(key,tab);
            return page;
        }
        return null;
    }
    private   Node createPage(ARetValue value,String request_name) {

        value.list.add(request_name);

        if(request_name=="edit_table_data"){
            System.out.println("创建编辑器");
            TableDataEditor tableDataEditor = new TableDataEditor();
            tableDataEditor.setTableName(value.obj_name);
            tableDataEditor.setTitles(value.list);
            return tableDataEditor;
        }
        else if(request_name == "edit_table_schema"){
            TableSchemaEditor tableSchemaEditor = new TableSchemaEditor();
            tableSchemaEditor.setTableName(value.obj_name);
            tableSchemaEditor.setTitles(value.list);
            return tableSchemaEditor;
        }
        return new TableView<>();
    }

    private ARetValue getSomething(String title) {

        String[] titles = title.split("\\.");
        ARetValue retValue = new ARetValue();

        for(int i=1;i<titles.length;i++){
            retValue.list.add(titles[i]);
            if(i!=1) {
                retValue.obj_name = retValue.obj_name + "." + titles[i];
            }else{
                retValue.obj_name = titles[1];
            }
        }

        return retValue;
    }
    public class ARetValue {
        String obj_name="";
        List<String> list = new ArrayList<>();
    }
}
