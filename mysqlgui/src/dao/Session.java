package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session {
    private String name;
    private String driver;
    private String url;
    private String username;
    private String password;

    private Connection connection;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean connect(){
        try{
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username,password);
            if(!connection.isClosed()){
                System.out.println("成功连接数据库!");
                return true;
            }
        }catch (ClassNotFoundException e){
            System.out.println("Sorry,cant find the Driver!");
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public void disconnect(){
        try {
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public List query(String sql){
        try {
            Statement stat = connection.createStatement();
            ResultSet rs=stat.executeQuery(sql);
            List list=convertList(rs);
            if(rs!=null)rs.close();
            if(stat!=null)stat.close();
            return list;
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList();
    }
    public int execute(String sql){
        int affect_row=-1;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            affect_row = ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return affect_row;
    }
    private static List convertList(ResultSet rs) throws SQLException{
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        while (rs.next()) {
            Map rowData = new HashMap();//声明Map
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
            }
            list.add(rowData);
        }
        return list;
    }
}
