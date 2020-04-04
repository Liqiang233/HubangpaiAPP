package com.example.menudemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class DBUtils {




    private static final String URL="jdbc:mysql://localhost/dd?useUnicode=true&characterEncoding=UTF-8";
    private static final String USERNAME="root";
    private static final String PASSWORD="";
    private DBUtils(){}
    static{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static Connection getConnection()
    {
        Connection conn=null;
        try {
            conn=DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;

    }
    public static void CloseConnection(Connection conn)
    {
        try {
            if(conn!=null){
                conn.close();}
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void CloseStatement(Statement stmt)
    {
        try {
            if(stmt!=null){
                stmt.close();}
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void CloseResultSet(ResultSet rs)
    {
        try {
            if(rs!=null){
                rs.close();}
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
