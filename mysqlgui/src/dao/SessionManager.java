package dao;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class SessionManager {
    private Map<String,Session> sessionMap=new TreeMap<>();
    //private Map<String,Connection> connectionMap=new TreeMap<>();
    //private Connection current_con;
    private String current_session;
//    private String driver = "com.lib.cj.jdbc.Driver";
//    private String url = "jdbc:lib://localhost:3306/?serverTimezone=UTC";
//    private String user="root";
//    private String passwd="123456";

    public SessionManager() {
        current_session ="";
        getSessionConfig("./src/dao/sessions.js");
    }

    private boolean  getSessionConfig(String filename){
        boolean ret = false;
        String session_str="";
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer sb=new StringBuffer();
            String s="";
            while((s=bufferedReader.readLine()) != null){
                sb.append(s+"\n");
                System.out.println(s);
            }
            bufferedReader.close();
            fileReader.close();
            session_str = sb.toString();

        }catch (FileNotFoundException e){
            System.out.println("文件没有找到！");
            e.printStackTrace();
            return false;
        }catch (IOException e){
            System.out.println("读取错误");
            e.printStackTrace();
            return false;
        }

        Session session = JSON.parseObject(session_str,Session.class);
        sessionMap.put(session.getName(),session);
        current_session = session.getName();
        return  true;
    }

    public Session getSessionByName(String name){
        return sessionMap.get(name);
    }

    public String getCurrentSessionName(){
        return current_session;
    }

    public void setCurrentSession(String name) {
        current_session = name;
    }
    public Session getCurrentSession() {
        return sessionMap.get(current_session);
    }
}
