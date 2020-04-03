package com.example.menudemo;

import android.text.format.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.menudemo.DBUtils;

public class LoginDaoImpl implements LoginDao {

    public LoginDaoImpl() {
        // TODO Auto-generated constructor stub
    }




    public  boolean login(String username, String password) {
        // TODO Auto-generated method stub
        Connection  conn=null;
        PreparedStatement  pstmt=null;
        ResultSet  rs=null;

        try
        {



            conn=DBUtils.getConnection();



            String  sql="select  *  from  login  where username=? and password=?";

            pstmt=conn.prepareStatement(sql);



            pstmt.setString(1,username);

            pstmt.setString(2, password);

            rs=pstmt.executeQuery();

            if(rs.next())
            {

                return true;

            }

        }

        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return false;
    }

}