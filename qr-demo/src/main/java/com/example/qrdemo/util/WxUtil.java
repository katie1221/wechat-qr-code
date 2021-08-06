package com.example.qrdemo.util;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;


/**
 * 微信小程序 工具类
 * @author Administrator
 *
 */
public class WxUtil {

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * @return
     */
    public static String getWxAccessToken(){
        //从redis缓存中获取AccessToken，有且未过期，直接返回；否则重新获取
//        String accessToken =RedisManager.get("accessToken");
//        if(Tool.notEmpty(accessToken)){
//            return accessToken;
//        }
        //重新获取accessToken，并存入redis
        String newToken = getAccessToken();
        //存入redis
        //RedisManager.set("accessToken", newToken, 7000);
        return newToken;
    }
    /**
     * 调用微信开放接口 获取小程序全局唯一后台接口调用凭据（access_token）
     * @return
     */
    public static String getAccessToken(){
        String APPID="wx81fb97ab9ecd3c4d";
        String APPSECRET="2699261e75db9498331357128f534c69";
        String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+APPSECRET;
        HttpRequestUtil hru = new HttpRequestUtil();
        JSONObject json = hru.sendUrlGet(accessTokenUrl);
        String access_token = json.getString("access_token");
        if(Tool.isEmpty(access_token)){
            access_token="";
        }

        System.out.println("json:"+json.toString());

        return access_token;
    }




    /**
     * 调用微信开放接口wxacode.getUnlimited获取小程序码
     * 通过该接口生成的小程序码，永久有效，数量暂无限制
     * @param sceneStr 参数
     * @param pageUrl 小程序页面链接---不能携带参数（参数请放在scene字段里）
     * @param accessToken 小程序全局唯一后台接口调用凭据
     * @return true:含敏感信息   false：正常
     */
    public static Map<String,Object> getWXAcode2(String sceneStr,String pageUrl) {
        Map<String, Object>  returnData= new HashMap<String, Object>();
        try {
            //获取token  小程序全局唯一后台接口调用凭据
            String accessToken = getWxAccessToken();
            URL url = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true); // 打开写入属性
            httpURLConnection.setDoInput(true); // 打开读取属性
            httpURLConnection.setRequestMethod("POST");// 提交方式
            // 不得不说一下这个提交方式转换！！真的坑。。改了好长时间！！一定要记得加响应头
            httpURLConnection.setRequestProperty("Content-Type", "application/x-javascript; charset=UTF-8");// 设置响应头
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            JSONObject paramJson = new JSONObject();
            paramJson.put("scene", sceneStr); // 你要放的内容
            paramJson.put("path", pageUrl);
            paramJson.put("width", 280); //二维码的宽度，单位 px，默认430px,最小 280px，最大 1280px
            paramJson.put("auto_color", false);//自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false
            Map<String,Object> line_color = new HashMap<>();
            line_color.put("r", 0);
            line_color.put("g", 0);
            line_color.put("b", 0);
            paramJson.put("line_color", line_color);//auto_color 为 false 时生效，使用 rgb 设置颜色
            paramJson.put("is_hyaline", false);//是否需要透明底色，为 true 时，生成透明底色的小程序

            printWriter.write(paramJson.toString());
            // flush输出流的缓冲
            printWriter.flush();
            BufferedInputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

            //小程序码图片文件夹位置
            //设置文件存储路径，可以存放在你想要指定的路径里面
            String rootPath = "D:/mimi/upload/images/";
            Long res = System.currentTimeMillis();
            String newFileName =res + ".png";
            // 新文件
            File newFile = new File(rootPath +File.separator+ newFileName);
            // 判断目标文件所在目录是否存在
            if( !newFile.getParentFile().exists()) {
                // 如果目标文件所在的目录不存在，则创建父目录
                newFile.getParentFile().mkdirs();
            }

            //-------把图片文件写入磁盘 start ----------------
            FileOutputStream fos = new FileOutputStream(newFile);
            //写入目标文件
            byte[] buffer=new byte[1024*1024];
            int byteRead=0;
            //stream.read(buffer) 每次读到的数据存放在 buffer 数组中
            while((byteRead=inputStream.read(buffer))!=-1){
                //在 buffer 数组中 取出数据 写到 （输出流）磁盘上
                fos.write(buffer, 0, byteRead);
                fos.flush();
            }
            fos.close();
            inputStream.close();
            //-------把图片文件写入磁盘 end ----------------
            // 完整的url(图片映射地址)
            //小程序码保存路径
            String fileUrl = "http://localhost:8080/mimi/upload/images/" + newFileName;
            returnData.put("codeURL", fileUrl);
            returnData.put("state", "200");
            return  returnData;

        }  catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        returnData.put("msg", "生成失败");
        returnData.put("state", "400");
        return  returnData;
    }

    public static void main(String[] args) {

    }
}


