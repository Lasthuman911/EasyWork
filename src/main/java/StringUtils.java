import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
/**
 * Created by lszhen on 2017/6/27.
 */
public class StringUtils extends org.apache.commons.lang.StringUtils{

    /**
     * 一次性判断多个或单个对象为空。
     * @param objects
     * @author cai niao
     * @return 只要有一个元素为Blank，则返回true
     */
    public static boolean isBlank(Object...objects){
        Boolean result = false ;
        for (Object object : objects) {
            if(object == null || "".equals(object.toString().trim())
                    || "null".equals(object.toString().trim())
                    || "[null]".equals(object.toString().trim())
                    || "[]".equals(object.toString().trim())){
                result = true ;
                break ;
            }
        }
        return result ;
    }
    /**
     * 一次性判断多个或单个对象不为空。
     * @param objects
     * @author zhou-baicheng
     * @return 只要有一个元素不为Blank，则返回true
     */
    public static boolean isNotBlank(Object...objects){
        return !isBlank(objects);
    }

    public static String checkNullToConvert(Object obj) {
        return StringUtils.isBlank(obj) ? "" : obj.toString();
    }
    /**
     * 判断一个字符串在数组中存在几个
     * @param baseStr
     * @param strings
     * @return
     */
    public static int indexOf(String baseStr,String[] strings){

        if(null == baseStr || baseStr.length() == 0 || null == strings)
            return 0;

        int i = 0;
        for (String string : strings) {
            boolean result = baseStr.equals(string);
            i = result ? ++i : i;
        }
        return i ;
    }
    /**
     * 判断一个字符串是否为JSONObject,是返回JSONObject,不是返回null
     * @param args
     * @return
     */
    public static net.sf.json.JSONObject isJSONObject(Object args) {
        net.sf.json.JSONObject result = null ;

        if(args instanceof net.sf.json.JSONObject){
            return (net.sf.json.JSONObject)args;
        }

        if(isBlank(args)){
            return result ;
        }
        try {
            return net.sf.json.JSONObject.fromObject(args);
        } catch (Exception e) {
            return result ;
        }
    }

    /**
     * 判断一个字符串是否为JSONArray,是返回JSONArray,不是返回null
     * @param args
     * @return
     */
    public static net.sf.json.JSONArray isJSONArray(Object args) {

        if(args instanceof net.sf.json.JSONArray){
            return (net.sf.json.JSONArray)args;
        }

        net.sf.json.JSONArray result = null ;
        if(isBlank(args)){
            return result ;
        }
        try {
            return net.sf.json.JSONArray.fromObject(args);
        } catch (Exception e) {
            return result ;
        }
    }
    public static String trimToEmpty(Object str){
        return (isBlank(str) ? "" : str.toString().trim());
    }

    /**
     * 将 Strig  进行 BASE64 编码
     * @param str [要编码的字符串]
     * @param bf  [true|false,true:去掉结尾补充的'=',false:不做处理]
     * @return
     */
    public static String getBASE64(String str,boolean...bf) {
        if (StringUtils.isBlank(str)) return null;
        String base64 = new sun.misc.BASE64Encoder().encode(str.getBytes()) ;
        //去掉 '='
        if(isBlank(bf) && bf[0]){
            base64 = base64.replaceAll("=", "");
        }
        return base64;
    }

    /** 将 BASE64 编码的字符串 s 进行解码**/
    public static String getStrByBASE64(String s) {
        if (isBlank(s)) return "";
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return "";
        }
    }
    /**
     * 把Map转换成get请求参数类型,如 {"name"=20,"age"=30} 转换后变成 name=20&age=30
     * @param map
     * @return
     */
    public static String mapToGet(Map<? extends Object,? extends Object> map){
        String result = "" ;
        if(map == null || map.size() ==0){
            return result ;
        }
        Set<? extends Object> keys = map.keySet();
        for (Object key : keys ) {
            result += ((String)key + "=" + (String)map.get(key) + "&");
        }

        return isBlank(result) ? result : result.substring(0,result.length() - 1);
    }

    /**
     * 把一串参数字符串,转换成Map 如"?a=3&b=4" 转换为Map{a=3,b=4}
     * @param args
     * @return
     */
    public static Map<String, ? extends Object> getToMap(String args){
        if(isBlank(args)){
            return null ;
        }
        args = args.trim();
        //如果是?开头,把?去掉
        if(args.startsWith("?")){
            args = args.substring(1,args.length());
        }
        String[] argsArray = args.split("&");

        Map<String,Object> result = new HashMap<String,Object>();
        for (String ag : argsArray) {
            if(!isBlank(ag) && ag.indexOf("=")>0){

                String[] keyValue = ag.split("=");
                //如果value或者key值里包含 "="号,以第一个"="号为主 ,如  name=0=3  转换后,{"name":"0=3"}, 如果不满足需求,请勿修改,自行解决.

                String key = keyValue[0];
                String value = "" ;
                for (int i = 1; i < keyValue.length; i++) {
                    value += keyValue[i]  + "=";
                }
                value = value.length() > 0 ? value.substring(0,value.length()-1) : value ;
                result.put(key,value);

            }
        }

        return result ;
    }

    /**
     * 转换成Unicode
     * @param str
     * @return
     */
    public static String toUnicode(String str) {
        String as[] = new String[str.length()];
        String s1 = "";
        for (int i = 0; i < str.length(); i++) {
            as[i] = Integer.toHexString(str.charAt(i) & 0xffff);
            s1 = s1 + "\\u" + as[i];
        }
        return s1;
    }

    public static String getDoubleTOString(Double str){
        String money = str.toString();
        try {
            Double.parseDouble(money);
        } catch (Exception e) {
            BigDecimal bDecimal = new BigDecimal(str);
            money = bDecimal.toPlainString();
        }
        return money;

    }

    /**
     * 把数组转换成Set 方便判断
     * @param objs
     * @return
     */
    public static TreeSet<String> arrayToSet(String[] objs){
        TreeSet<String> result = new TreeSet<String>();
        if(null ==objs){
            return result ;
        }
        for (String obj:objs) {
            result.add(obj);
        }
        return result;
    }

    /**
     * 字符串转urlcode
     * @param value
     * @return
     */
    public static String strToUrlcode(String value){
        try {
            value = java.net.URLEncoder.encode(value,"utf-8");
            return value ;
        } catch (UnsupportedEncodingException e) {
            LoggerUtils.error(StringUtils.class, "字符串转换为URLCode失败,value:" + value,e);
            e.printStackTrace();
            return null;
        }
    }
    /**
     * urlcode转字符串
     * @param value
     * @return
     */
    public static String urlcodeToStr(String value){
        try {
            value = java.net.URLDecoder.decode(value,"utf-8");
            return value ;
        } catch (UnsupportedEncodingException e) {
            LoggerUtils.error(StringUtils.class, "URLCode转换为字符串失败;value:" + value,e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置JSON字符串返回
     *
     * @param key
     * @param value
     * @return
     */
    public static String setJsonString(String key, String value) {

        if (StringUtils.isNotBlank(key, value)) {

            JSONObject goodsJson = new JSONObject();
            goodsJson.put(key, value);
            return goodsJson.toString();
        }
        return "";
    }

    /**
     * 替换字符串
     *
     * @param str
     * @param nowStr
     * @param replaceStr
     * @return
     */
    public static String replaceString(String str, String nowStr, String replaceStr) {

        nowStr = StringUtils.isBlank(nowStr) ? "" : nowStr;
        replaceStr = StringUtils.isBlank(replaceStr) ? "" : replaceStr;

        if (StringUtils.isNotBlank(str)) {

            return str.replaceAll(nowStr, replaceStr);
        }
        return "";
    }

}
}
