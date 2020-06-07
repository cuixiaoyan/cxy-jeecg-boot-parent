package org.jeecg.modules.demo.KdApiSearchDemo;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.security.MessageDigest;

import com.alibaba.fastjson.JSONArray;


/**
 * 快递鸟订阅推送2.0接口
 *
 * @技术QQ: 4009633321
 * @技术QQ群: 200121393
 * @see: http://www.kdniao.com/api-subscribe
 * @copyright: 深圳市快金数据技术服务有限公司
 * <p>
 * ID和Key请到官网申请：http://www.kdniao.com/ServiceApply.aspx
 */

@RestController
@RequestMapping("/participate/personnel")
public class KdniaoAPI {

    //DEMO
    public static void main(String[] args) {
        KdniaoAPI api = new KdniaoAPI();
        try {

            //物流信息订阅
            //String result = api.orderTracesSubByJson("{\"ShipperCode\":\"SF\",\"OrderCode\":\"SF201608081055208281\",\"LogisticCode\":\"3100707578976\",\"PayType\":\"1\",\"ExpType\":\"1\",\"CustomerName\":\"\",\"CustomerPwd\":\"\",\"MonthCode\":\"\",\"IsNotice\":\"0\",\"Sender\":{\"Name\":\"1255760\",\"Tel\":\"\",\"Mobile\":\"13700000000\",\"ProvinceName\":\"广东省\",\"CityName\":\"深圳市\",\"ExpAreaName\":\"福田区\",\"Address\":\"测试地址\"},\"Receiver\":{\"Name\":\"1255760\",\"Tel\":\"\",\"Mobile\":\"13800000000\",\"ProvinceName\":\"广东省\",\"CityName\":\"深圳市\",\"ExpAreaName\":\"龙华新区\",\"Address\":\"测试地址2\"},\"Commodity\":[{\"GoodsName\":\"书本\"}]}");
            //String result = api.orderTracesSubByJson("773016743957842");

            //即时快递接口 快递公司编号 运单编号
            String result = api.getOrderTracesByJson("773016743957842");


            //Map<String, Object> result = api.receiveMessages(null, null);
            System.out.print(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //电商ID
    private String EBusinessID = "1605639";
    //电商加密私钥，快递鸟提供，注意保管，不要泄漏
    private String AppKey = "0bd9dd48-04b0-4efb-b9a0-7acd959eddcd";
    //请求url
    private String ReqURL = "http://api.kdniao.com/api/dist";

//	//电商ID
//	private String EBusinessID="test1605639";
//	//电商加密私钥，快递鸟提供，注意保管，不要泄漏
//	private String AppKey="5d1e89b4-e00e-49ec-82f3-08515fa856ac";
//	//测试请求url
//	private String ReqURL = "http://sandboxapi.kdniao.com:8080/kdniaosandbox/gateway/exterfaceInvoke.json";


    /**
     * Json方式  物流信息订阅
     *
     * @throws Exception
     */
    public String orderTracesSubByJson(String expNo) throws Exception {
        String requestData = "{'OrderCode':'','ShipperCode':'" + getShipperCode(expNo) + "','LogisticCode':'" + expNo + "'}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1008");
        String dataSign = encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result = sendPost(ReqURL, params);

        //根据公司业务处理返回的信息......

        return result;
    }


    /**
     * Json方式 查询订单物流轨迹
     *
     * @throws Exception
     */
    public String getOrderTracesByJson(String expNo) throws Exception {
        String requestData = "{'OrderCode':'','ShipperCode':'" + getShipperCode(expNo) + "','LogisticCode':'" + expNo + "'}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1002");
        String dataSign = encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        String result = sendPost(ReqURL, params);
        //根据公司业务处理返回的信息......
        return result;
    }


    //接收快递信息
    @PostMapping(value = "/receiveMessages")
    public Map<String, Object> receiveMessages(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> params = new HashMap<>();
        //String RequestData = request.getParameter("RequestData");

        String RequestData1 = "{\"EBusinessID\":\"1109259\",\"Count\":\"2\",\"PushTime\":\"2015-3-11 16:21:06\",\"Data\":[{\"EBusinessID\":\"1109259\",\"OrderCode\":\"\",\"ShipperCode\":\"EMS\",\"LogisticCode\":\"5042260908504\",\"Success\":true,\"Reason\":\"\",\"State\":\"2\",\"CallBack\":\"0\",\"Traces\":[{\"AcceptTime\":\"2015-03-06 21:16:58\",\"AcceptStation\":\"深圳市横岗速递营销部已收件，（揽投员姓名：钟定基;联系电话：）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-07 14:25:00\",\"AcceptStation\":\"离开深圳市 发往广州市\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-08 00:17:00\",\"AcceptStation\":\"到达广东速递物流公司广航中心处理中心（经转）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-08 01:15:00\",\"AcceptStation\":\"离开广州市 发往北京市（经转）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-09 09:01:00\",\"AcceptStation\":\"到达北京黄村转运站处理中心（经转）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-09 18:39:00\",\"AcceptStation\":\"离开北京市 发往呼和浩特市（经转）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-10 18:06:00\",\"AcceptStation\":\"到达  呼和浩特市 处理中心\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-11 09:53:48\",\"AcceptStation\":\"呼和浩特市邮政速递物流分公司金川揽投部安排投递（投递员姓名：安长虹;联系电话：18047140142）\",\"Remark\":\"\"}]},{\"EBusinessID\":\"1109259\",\"OrderCode\":\"\",\"ShipperCode\":\"EMS\",\"LogisticCode\":\"5042260943004\",\"Success\":true,\"Reason\":\"\",\"State\":\"2\",\"CallBack\":\"0\",\"Traces\":[{\"AcceptTime\":\"2015-03-07 15:26:09\",\"AcceptStation\":\"深圳市横岗速递营销部已收件，（揽投员姓名：周宏彪;联系电话：13689537568）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-08 16:32:00\",\"AcceptStation\":\"离开深圳市 发往广州市\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-09 00:58:00\",\"AcceptStation\":\"到达广东速递物流公司广航中心处理中心（经转）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-09 01:15:00\",\"AcceptStation\":\"离开广州市 发往北京市（经转）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-10 05:20:00\",\"AcceptStation\":\"到达北京黄村转运站处理中心（经转）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-10 11:59:00\",\"AcceptStation\":\"离开北京市 发往廊坊市（经转）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-10 14:23:00\",\"AcceptStation\":\"到达廊坊市处理中心（经转）\",\"Remark\":\"\"},{\"AcceptTime\":\"2015-03-11 08:55:00\",\"AcceptStation\":\"离开廊坊市 发往保定市（经转）\",\"Remark\":\"\"}]}]}";
        //String RequestData1 ="{\"PushTime\":\"2019-12-04 11:18:14\",\"EBusinessID\":\"test1605639\",\"Data\":[{\"LogisticCode\":\"1234561\",\"ShipperCode\":\"SF\",\"Traces\":[{\"AcceptStation\":\"顺丰速运已收取快件\",\"AcceptTime\":\"2019-12-04 11:18:14\",\"Remark\":\"\"},{\"AcceptStation\":\"货物已经到达深圳\",\"AcceptTime\":\"2019-12-04 11:18:142\",\"Remark\":\"\"},{\"AcceptStation\":\"货物到达福田保税区网点\",\"AcceptTime\":\"2019-12-04 11:18:143\",\"Remark\":\"\"},{\"AcceptStation\":\"货物已经被张三签收了\",\"AcceptTime\":\"2019-12-04 11:18:144\",\"Remark\":\"\"}],\"State\":\"3\",\"EBusinessID\":\"test1605639\",\"Success\":true,\"Reason\":\"\",\"CallBack\":\"\",\"EstimatedDeliveryTime\":\"2019-12-04 11:18:14\"}],\"Count\":\"1\"}";
        String RequestData = "[" + RequestData1 + "]";
        if (StringUtils.isNotBlank(RequestData)) {
            //解析json为数组
            JSONArray trackingData = JSONArray.parseArray(RequestData);
            for (Iterator iterator = trackingData.iterator(); iterator.hasNext(); ) {
                JSONObject job = (JSONObject) iterator.next();

                String EBusinessID = job.get("EBusinessID").toString();
                String PushTime = job.get("PushTime").toString();
                //推送物流单号轨迹个数
                String Count = job.get("Count").toString();
                //推送物流单号轨迹集合
                JSONArray data = job.getJSONArray("Data");
                for (Iterator iterator1 = data.iterator(); iterator1.hasNext(); ) {
                    JSONObject job1 = (JSONObject) iterator1.next();
                    //用户电商ID
                    String EBusinessID1 = job1.get("EBusinessID").toString();
                    //快递公司编码
                    String ShipperCode = job1.get("ShipperCode").toString();
                    //只有京东才会有订单编号
                    if(ShipperCode.equals("JD")){
                        //订单编号
                        String OrderCode = job1.get("OrderCode").toString();
                    }
                    //快递单号
                    String LogisticCode = job1.get("LogisticCode").toString();
                    //成功与否：true,false
                    String Success = job1.get("Success").toString();
                    //失败原因
                    String Reason = job1.get("Reason").toString();
                    //物流状态: 0-无轨迹，1-已揽收，2-在途中，3-签收,4-问题件
                    String State = job1.get("State").toString();
                    //订阅接口的Bk值
                    String CallBack = job1.get("CallBack").toString();
                    //单条记录集合
                    String Traces = job1.get("Traces").toString();
                    //如果快递已签收 将数据写入数据库
                    if(State.equals(3)){

                    }

                }
                params.put("Success", true);
                params.put("EBusinessID", EBusinessID);
                params.put("UpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                params.put("Reason", "共接收" + Count + "个");
            }
        } else {
            params.put("Success", false);
            params.put("EBusinessID", EBusinessID);
            params.put("UpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            params.put("Reason", "系统内部异常");
        }


        return params;
    }


    /**
     * Json方式 单号识别
     * @throws Exception
     */
    public String getShipperCode(String expNo) throws Exception{
        String requestData= "{'LogisticCode':'" + expNo + "'}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "2002");
        String dataSign=encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result=sendPost(ReqURL, params);
        String RequestData = "[" + result + "]";
        //快递公司编号
        String shipperCode=null;
        JSONArray trackingData = JSONArray.parseArray(RequestData);
        for (Iterator iterator = trackingData.iterator(); iterator.hasNext(); ) {
            JSONObject job = (JSONObject) iterator.next();
            JSONArray shippers = job.getJSONArray("Shippers");
            for (Iterator iterator1 = shippers.iterator(); iterator1.hasNext(); ) {
                JSONObject job1 = (JSONObject) iterator1.next();
                shipperCode = job1.getString("ShipperCode");
                String shipperName = job1.getString("ShipperName");
            }
        }
        //根据公司业务处理返回的信息......
        return shipperCode;
    }

    /**
     * MD5加密
     *
     * @param str     内容
     * @param charset 编码方式
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    /**
     * base64编码
     *
     * @param str     内容
     * @param charset 编码方式
     * @throws UnsupportedEncodingException
     */
    private String base64(String str, String charset) throws UnsupportedEncodingException {
        String encoded = base64Encode(str.getBytes(charset));
        return encoded;
    }

    @SuppressWarnings("unused")
    private String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
        String result = URLEncoder.encode(str, charset);
        return result;
    }

    /**
     * 电商Sign签名生成
     *
     * @param content  内容
     * @param keyValue Appkey
     * @param charset  编码方式
     * @return DataSign签名
     * @throws UnsupportedEncodingException ,Exception
     */
    @SuppressWarnings("unused")
    private String encrypt(String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception {
        if (keyValue != null) {
            return base64(MD5(content + keyValue, charset), charset);
        }
        return base64(MD5(content, charset), charset);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url    发送请求的 URL
     * @param params 请求的参数集合
     * @return 远程资源的响应结果
     */
    @SuppressWarnings("unused")
    private String sendPost(String url, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数            
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (param.length() > 0) {
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                    System.out.println(entry.getKey() + ":" + entry.getValue());
                }
                System.out.println("param:" + param.toString());
                out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    private static char[] base64EncodeChars = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/'};

    public static String base64Encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }
}
