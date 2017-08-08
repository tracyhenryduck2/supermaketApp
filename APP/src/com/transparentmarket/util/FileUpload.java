package com.transparentmarket.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class FileUpload {
	public int send(String url, String filePath) throws IOException {

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return -1;
        }
 
        /**
         * 第一部分
         */
        System.out.println(url);
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
 
        /**
         * 设置关键值
         */
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
 
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
 
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                + BOUNDARY);
 
        // 请求正文信息
 
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // ////////必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
                + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
 
        byte[] head = sb.toString().getBytes("utf-8");
 
        // 获得输出流
 
        OutputStream out = new DataOutputStream(con.getOutputStream());
        out.write(head);
 
        // 文件正文部分
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
 
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
 
        out.write(foot);
 
        out.flush();
        out.close();
 
        /**
         * 读取服务器响应，必须读取,否则提交不成功
         */
 
       // return con.getResponseCode();
 
        /**
         * 下面的方式读取也是可以的
         */
        int result=0;
        try {
         //定义BufferedReader输入流来读取URL的响应
         BufferedReader reader = new BufferedReader(new InputStreamReader(
         con.getInputStream()));
        String line = null;
         while ((line = reader.readLine()) != null) {
        //System.out.println(line);
        	 JSONObject obj=new JSONObject(line);
        	 result=Integer.parseInt(obj.get("result").toString());
        }
        } catch (Exception e) {
        System.out.println("发送POST请求出现异常！" + e);
        e.printStackTrace();
        }
        return result;
    }
 
    public static void main(String[] args) throws IOException {
        FileUpload up = new FileUpload();
        System.out.println(up.send("http://localhost:88/oa/App/Attence!signIn.action?inAddress=cixi&inLng=10.1&inLat=191.1&userId=16&note=asd",
        "f:\\img.jpg"));
    }
    
    public Map<String,Object> sends(String url, String filePath) throws IOException {
    	Map<String,Object> map=new HashMap<String,Object>();
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
 
        /**
         * 第一部分
         */
        System.out.println(url);
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
 
        /**
         * 设置关键值
         */
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
 
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
 
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                + BOUNDARY);
 
        // 请求正文信息
 
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // ////////必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
                + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
 
        byte[] head = sb.toString().getBytes("utf-8");
 
        // 获得输出流
 
        OutputStream out = new DataOutputStream(con.getOutputStream());
        out.write(head);
 
        // 文件正文部分
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
 
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
 
        out.write(foot);
 
        out.flush();
        out.close();
 
        /**
         * 读取服务器响应，必须读取,否则提交不成功
         */
 
       // return con.getResponseCode();
 
        /**
         * 下面的方式读取也是可以的
         */
        try {
         //定义BufferedReader输入流来读取URL的响应
         BufferedReader reader = new BufferedReader(new InputStreamReader(
         con.getInputStream()));
        String line = null;
        StringBuilder sbread = new StringBuilder();
         while ((line = reader.readLine()) != null) {
        //System.out.println(line);
        	 sbread.append(line);
        }
    	 JSONObject obj=new JSONObject(sbread.toString());
    	 int result=Integer.parseInt(obj.get("result").toString());
    	 String path=obj.get("filepath").toString();
    	 String filename = obj.get("filename").toString();
    	 map.put("result", result);
    	 map.put("filePath", path);
    	 map.put("filename", filename);
        } catch (Exception e) {
        System.out.println("发送POST请求出现异常！" + e);
        e.printStackTrace();
        }
        return map;
    }
    
    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }

        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

	public int sendNotPic(String url) throws IOException{

 
        /**
         * 第一部分
         */
        System.out.println(url);
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
 
        /**
         * 设置关键值
         */
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
 
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        /**
         * 下面的方式读取也是可以的
         */
        int result=0;
        try {
         //定义BufferedReader输入流来读取URL的响应
         BufferedReader reader = new BufferedReader(new InputStreamReader(
         con.getInputStream()));
        String line = null;
         while ((line = reader.readLine()) != null) {
        //System.out.println(line);
        	 JSONObject obj=new JSONObject(line);
        	 result=Integer.parseInt(obj.get("result").toString());
        }
        } catch (Exception e) {
        System.out.println("发送POST请求出现异常！" + e);
        e.printStackTrace();
        }
        return result;
	}
}
