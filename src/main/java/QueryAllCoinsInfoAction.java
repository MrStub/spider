

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryAllCoinsInfoAction {
    //查询结果
    public static List<Map<String,String>> dataList = new ArrayList<>();
    public static BufferedReader in = null;
    public static OutputStreamWriter out = null;
    public static void main(String[] args) throws IOException {
        String url = "https://dncapi.bqrank.net/api/coin/web-coinrank?";
        String paramsString = "page=1&type=-1&pagesize=100&webp=1";
        HashMap send = new HashMap();
        //页面index
        send.put("page",1);
        send.put("type",-1);
        //页面数据数量 最大页面数量185 ，
        send.put("pagesize",100);
        send.put("webp",1);
        HttpURLConnection conn=getConnObject(url,"GET",send); //根据请求方式和url获得相应的conn对象
        conn.connect();
        // 获取URLConnection对象对应的输出流
            /*out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // flush输出流的缓冲
            out.flush();*/
        // 定义BufferedReader输入流来读取URL的响应
        in = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        while ((line = in.readLine()) != null) {
            //将获取到的每一条数据转换成jsonObject
            JSONObject jo = JSONObject.parseObject(line);
            Map h = JSONObject.toJavaObject(jo, Map.class);
            dataList.add(h);
        }

        for (Map m:
             dataList) {
            List<Map> mSon = (List<Map>) m.get("data");
            System.out.println(mSon.get(0));
            break;
        }
    }
    /**
     * 根据请求方式和url获得相应的conn对象
     * @param url
     * @param method
     * @return
     */
    private static HttpURLConnection getConnObject(String url,String method,Map<String, Integer> params)
    {
        HttpURLConnection conn=null;
        try {
            if(method.equals("GET")) {
                if(params!=null){
                    for(String m :params.keySet()){
                        url+=m+"="+params.get(m);
                    }
                }
                URL realUrl = new URL(url);
                conn =(HttpURLConnection) realUrl.openConnection();
                conn.setRequestMethod("GET");
            }else if(method.equals("POST")) {
                URL realUrl = new URL(url);
                conn =(HttpURLConnection) realUrl.openConnection();
                conn.setRequestMethod("POST");
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //设置连接超时和读取超时
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            //conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        }catch (MalformedURLException e){
            System.out.println("无效url！"+e);
        }catch (IOException e){
            System.out.println("url资源获取异常！"+e);
        }catch (Exception e){
            System.out.println("不能处理的请求方式！"+e);
        }
        return conn;
    }
}
