package com.example.menudemo.ui.utills;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


/*
 * @author  Yapi
 * @note  登陆界面的逻辑设计
 * @time  2020.4.6
 * @version 1.0
 */
public class HttpUtillConnection {

 public static String BASE_URL = "http://192.168.13.100:8080/hubangpai/LoginUser";
 public static String BASE_URL_Task="http://192.168.13.100:8080/hubangpai/PublishTask";
 public static String BASE_URL_Task_Ya="http://192.168.1.3:8080/hubangpai/PublishTask";
 public static String Ya_URL = "http://192.168.1.3:8080/hubangpai/";

    /*
     * urlStr:网址
     * parms：提交数据
     * return:网页源码
     * */
    public static  String getContextByHttp(String urlStr,Map<String,String> parms){
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            writer.write(getStringFromOutput(parms));
            writer.flush();
            writer.close();
            outputStream.close();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String temp;
                while((temp = reader.readLine()) != null){
                    sb.append(temp);
                }
                reader.close();
            }else{
                return "connection error:" + connection.getResponseCode();
            }

            connection.disconnect();
        }catch (Exception e){
            return e.toString();
        }
        return sb.toString();
    }

    /**
     * 将map转换成key1=value1&key2=value2的形式
     * @param map
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String getStringFromOutput(Map<String,String> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (isFirst)
                isFirst = false;
            else
                sb.append("&");

            sb.append(URLEncoder.encode(entry.getKey(), "utf-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), "utf-8"));
        }
        return sb.toString();
    }

    public static  String getContextByHttp(String urlStr){
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(50000);
            connection.setConnectTimeout(50000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(true);

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String temp;
                while((temp = reader.readLine()) != null){
                    sb.append(temp);
                }
                reader.close();
            }else{
                return "connection error:" + connection.getResponseCode();
            }

            connection.disconnect();
        }catch (Exception e){
            return e.toString();
        }
        return sb.toString();
    }
}
