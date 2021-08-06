package com.example.qrdemo.util;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;

/**
 * 用于发送HTTP请求-----返回值为json
 * @author JH
 * @date 2018年9月21日 16:09:36
 */
public class HttpRequestUtil {
    /**
     * 调用接口查询数据 get
     *
     * @param action
     * @return
     * @throws MalformedURLException
     */
    public JSONObject sendUrlGet(String action) {
        URL url;
        JSONObject json = null;
        try {
            url = new URL(action.toString());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            // 设定请求的方法为"POST"，默认是GET
            http.setRequestMethod("GET");
            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            http.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false;
            http.setDoOutput(false);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            http.setDoInput(true);
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] buf = new byte[size];
            is.read(buf);
            String resp = new String(buf, "UTF-8");
            json = JSONObject.parseObject(resp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return json;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        return json;
    }

    /**
     * 调用接口查询数据 post
     *
     * @param action
     * @return
     * @throws MalformedURLException
     */
    public JSONObject sendUrlPost(String action, Map inMap) {
        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        String str = "";
        try {
            URL url = new URL(action);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            // 默认为false的，所以需要设置
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.connect();
            // 开始访问
            StringBuilder postData = new StringBuilder();
            OutputStream outStream = conn.getOutputStream();
            DataOutputStream out = new DataOutputStream(outStream);
            JSONObject jso = new JSONObject();
            //把参数转换成json字符串
            String params = jso.toJSONString(inMap);
            //传输参数
            out.writeBytes(params);
            out.close();

            is = conn.getInputStream();
            reader = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(reader);
            String readLine = "";
            while ((readLine = br.readLine()) != null) {
                str += readLine + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return JSONObject.parseObject(str);
    }



    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(8*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }


        System.out.println("info:"+url+" download success");

    }
    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}


