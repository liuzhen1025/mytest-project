package com.gennlift.json.path;


import com.alibaba.fastjson.JSONObject;
import com.gennlift.common.ConditionFilter;
import com.gennlift.common.XJsonPath;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONStyle;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lz on 2017/9/28.
 */
public class JsonPathTest {
    private static String jsonstr = "";
    private static JSONObject jsonObject;
    @BeforeClass
    public static void before(){
        jsonstr = "{ \"store\": {" +
                "    \"书\": [ " +
                "      { \"类别\": \"reference\"," +
                "        \"作者\": \"Nigel Rees\"," +
                "        \"标题\": \"Sayings of the Century\"," +
                "        \"价格\": 8.95" +
                "      }," +
                "      { \"类别\": \"fiction\"," +
                "        \"作者\": \"Nigel Rees\"," +
                "        \"标题\": \"Sword of Honour\"," +
                "        \"价格\": 12.99" +
                "      }," +
                "      { \"类别\": \"fiction\"," +
                "        \"作者\": \"Herman Melville\"," +
                "        \"标题\": \"Moby Dick\"," +
                "        \"SN码\": \"0-553-21311-3\"," +
                "        \"价格\": 8.99" +
                "      }," +
                "      { \"类别\": \"fiction\"," +
                "        \"作者\": \"J. R. R. Tolkien\"," +
                "        \"标题\": \"The Lord of the Rings\"," +
                "        \"SN码\": \"0-395-19395-8\"," +
                "        \"价格\": 22.99" +
                "      }" +
                "    ]," +
                "    \"bicycle\": {" +
                "      \"color\": \"red\"," +
                "      \"price\": 19.95" +
                "    }" +
                "  }" +
                "}";
        jsonObject = JSONObject.parseObject(jsonstr);
        System.out.println(jsonstr);
    }

    /**
     * //The authors of all books：获取json中store下book下的所有author值
     List<String> authors1 = JsonPath.read(json, "$.store.book[*].author");

     //All authors：获取所有json中所有author的值
     List<String> authors2 = JsonPath.read(json, "$..author");

     //All things, both books and bicycles //authors3返回的是net.minidev.json.JSONArray：获取json中store下的所有value值，不包含key，如key有两个，book和bicycle
     List<Object> authors3 = JsonPath.read(json, "$.store.*");


     //The price of everything：获取json中store下所有price的值
     List<Object> authors4 = JsonPath.read(json, "$.store..price");

     //The third book：获取json中book数组的第3个值
     List<Object> authors5 = JsonPath.read(json, "$..book[2]");

     //The first two books：获取json中book数组的第1和第2两个个值
     List<Object> authors6 = JsonPath.read(json, "$..book[0,1]");

     //All books from index 0 (inclusive) until index 2 (exclusive)：获取json中book数组的前两个区间值
     List<Object> authors7 = JsonPath.read(json, "$..book[:2]");

     //All books from index 1 (inclusive) until index 2 (exclusive)：获取json中book数组的第2个值
     List<Object> authors8 = JsonPath.read(json, "$..book[1:2]");

     //Last two books：获取json中book数组的最后两个值
     List<Object> authors9 = JsonPath.read(json, "$..book[-2:]");

     //Book number two from tail：获取json中book数组的第3个到最后一个的区间值
     List<Object> authors10 = JsonPath.read(json, "$..book[2:]");

     //All books with an ISBN number：获取json中book数组中包含isbn的所有值
     List<Object> authors11 = JsonPath.read(json, "$..book[?(@.isbn)]");

     //All books in store cheaper than 10：获取json中book数组中price<10的所有值
     List<Object> authors12 = JsonPath.read(json, "$.store.book[?(@.price < 10)]");

     //All books in store that are not "expensive"：获取json中book数组中price<=expensive的所有值
     List<Object> authors13 = JsonPath.read(json, "$..book[?(@.price <= $['expensive'])]");

     //All books matching regex (ignore case)：获取json中book数组中的作者以REES结尾的所有值（REES不区分大小写）
     List<Object> authors14 = JsonPath.read(json, "$..book[?(@.author =~ /.*REES/i)]");

     //Give me every thing：逐层列出json中的所有值，层级由外到内
     List<Object> authors15 = JsonPath.read(json, "$..*");

     //The number of books：获取json中book数组的长度
     List<Object> authors16 = JsonPath.read(json, "$..book.length()");
     */
    @Test
    public void testGetSingleKey(){
        Object read = JsonPath.read(jsonObject, "$.store.书[*].类别");
        JSONArray read1 = (JSONArray)JsonPath.read(jsonObject, "$.store.书[?(@.类别 <= 'aeference')]");
        JSONArray read2 = (JSONArray)JsonPath.read(jsonObject, "$.store.书[?(@.类别 =~/.*erence.*?/i)]");
        System.out.println(read);
    }


    @Test
    public void testGetSomeOneKey(){
        Object read = JsonPath.read(jsonObject, "$..book[2]");
        List<Object> authors14 = JsonPath.read(jsonObject, "$..书[?(@.author =~/.*Herman/.*)]");
        System.out.println(JsonPath.read(jsonObject, "$..书[?(@.price <= $['5']).max()]"));
        System.out.println(read);
    }
    @Test
    public void autoAnalysis(){
        String ex = "([脑血管CRF.既往史.高血压史.]高血压史年限 < 10 AND [脑血管CRF.吸烟史.有无吸烟史] 包含 无 OR" +
                " [脑血管CRF.家族病史.有无卒中家族史] 包含 否 NOT [患者基本信息.性别] 包含 女)";
        System.out.println(strStr(ex.substring(1,ex.length()-1),"OR"));
        //Regex regex = new Regex(@"^[0-9]*");
        System.out.println(ex.substring(0,59));

    }





    public List<String> getAllOrCondition(String str, String regx){
        String[] split = str.split(regx);
        int orPosition = strStr(str.substring(1, str.length() - 1), "OR");

        return Arrays.asList(split);
    }
    public int strStr(String text, String pattern) {
        int result = 0;
        if (pattern == null || text == null) return -1;
        if (pattern.equals("")) return 0;   // 如果模式串为空，认为第0位已经匹配
        int tlen = text.length(), plen = pattern.length();
        if (plen > tlen) return -1;
        int i = 0, j = 0, k;  // 分别记录text索引，pattern索引和模式串计数索引
        int index;  // 记录不匹配时字符（m所在位置）的索引
        while (i < tlen && j < plen) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                continue;
            }
            index = result + plen;
            if (index >= tlen) return -1;
            for (k = plen - 1; k >= 0 && text.charAt(index) != pattern.charAt(k); k--);
            i = result;
            i += plen - k;
            result = i;
            j = 0;
            if (result + plen > tlen) return -1;  // 如果匹配长度超过主串，匹配失败
        }
        return i <= tlen? result: -1;
    }
    @Test
    public void testIsInBraces(){
        ///ConditionSplitFirstAnd and = new ConditionSplitFirstAnd();
        //System.out.println(and.isInBraces(""));
    }
    @Test
    public void manySpaceToOneSpace(){
        String str = "  kk kkk klaf    fsaff fsge tg sagh ger fsaga   ";

        System.out.println(StringUtils.replaceAll(str,"\\s+"," "));
    }
    @Test
    public void testSplitCondition(){
        String ex = "([脑血管CRF.既往史.高血压史.高血压史年限] < 10 AND ([脑血管CRF.吸烟史.有无吸烟史] 包含 无 OR [脑血管CRF.家族病史.有无卒中家族史] 包含 否)" +
                "  AND [患者基本信息.性别] 包含 女)";
        ConditionFilter conditionFilter = new ConditionFilter();
        List<String> result = new ArrayList<String>();
        conditionFilter.splitCondition(ex.substring(1,ex.length()-1),"AND",result);
        System.out.println(result);
    }
    @Test
    public void testXJsonPath() throws Exception {
        //等于
        //String condition1 = "store.书.价格 等于 22.99";
        //String condition1 = "store.书.类别 等于 reference";

        //小于
        //String condition1 = "store.书.价格 小于 22.99";
        //String condition1 = "store.书.类别 小于 zeference";
        //大于
        //String condition1 = "store.书.价格 大于 12.99";
       //String condition1 = "store.书.类别 大于 aeference";
        //大于等于
        //String condition1 = "store.书.价格 大于等于 0.99";
       //String condition1 = "store.书.类别 大于等于 zeference";
        //小于等于
        //String condition1 = "store.书.价格 小于等于 12.99";
       //String condition1 = "store.书.类别 小于等于 fyference";
        //包含
       String condition1 = "store.书.类别 包含 erence";

        XJsonPath xJsonPath = new XJsonPath();
        JSONArray objects = xJsonPath.searcheByCondition(jsonObject, condition1, " ", ".");
        long start = System.currentTimeMillis();
        JSONArray read = JsonPath.read(jsonObject, "$..书.length()");
        com.alibaba.fastjson.JSONArray objects1 = JSONObject.parseArray(read.toJSONString());
        JSONObject object = new JSONObject(true);
        System.out.println(read.get(0)+"====="+(System.currentTimeMillis()-start)/1000);
    }


    @Test
    public void judesTest() throws Exception {
        String condition1 = "书.类别 等于 reference";
        String condition2 = "书.作者 等于引用 condition1.书.类别";
        String condition3 = "书.作者 等于引用 condition1.书.类别";
        Map<String,String> conditions = new HashMap<String,String>();
        conditions.put("condition1",condition1);
        conditions.put("condition2",condition2);
        conditions.put("condition3",condition3);
        List<String> queue = new ArrayList<String>();
        queue.add("condition1");
        queue.add("condition2");
        queue.add("condition3");
        judgs(jsonObject,null,queue,0,conditions);
    }
    public void judgs(JSONObject json,String condition,List<String> queue,int queueIndex,Map<String, String> conditions) throws Exception {
        String s = queue.get(queueIndex);
        if(StringUtils.isEmpty(condition)){
            String s1 = conditions.get(s);
            Object value = judge(json,s1);
            //System.out.println(1.getClass().getName());
            String s2 = queue.get(queueIndex+1);
            if(s2.contains("引用")){

            }
        }
    }

    public Object judge(JSONObject json,String condition) throws Exception {
        String[] split = condition.split(" ");
        if(split[1].equals("等于")){
            //All books with an ISBN number：获取json中book数组中包含isbn的所有值
            try {
                JSONArray read = (JSONArray)JsonPath.read(json, "$..书[?(@.类别 == 'reference' && @.作者 == 'Nigel Rees')]");
                return read;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new Exception("参数错误");
    }
    @Test
    public void flotNum (){
        /**
         * 验证数字的正则表达式集
         验证数字：^[0-9]*$
         验证n位的数字：^\d{n}$
         验证至少n位数字：^\d{n,}$
         验证m-n位的数字：^\d{m,n}$
         验证零和非零开头的数字：^(0|[1-9][0-9]*)$
         验证有两位小数的正实数：^[0-9]+(.[0-9]{2})?$
         验证有1-3位小数的正实数：^[0-9]+(.[0-9]{1,3})?$
         验证非零的正整数：^\+?[1-9][0-9]*$
         验证非零的负整数：^\-[1-9][0-9]*$
         验证非负整数（正整数 + 0） ^\d+$
         验证非正整数（负整数 + 0） ^((-\d+)|(0+))$
         验证长度为3的字符：^.{3}$
         验证由26个英文字母组成的字符串：^[A-Za-z]+$
         验证由26个大写英文字母组成的字符串：^[A-Z]+$
         验证由26个小写英文字母组成的字符串：^[a-z]+$
         验证由数字和26个英文字母组成的字符串：^[A-Za-z0-9]+$
         验证由数字、26个英文字母或者下划线组成的字符串：^\w+$
         验证用户密码:^[a-zA-Z]\w{5,17}$ 正确格式为：以字母开头，长度在6-18之间，只能包含字符、数字和下划线。
         验证是否含有 ^%&',;=?$\" 等字符：[^%&',;=?$\x22]+
         验证汉字：^[\u4e00-\u9fa5],{0,}$
         验证Email地址：^\w+[-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$
         验证InternetURL：^http://([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$ ；^[a-zA-z]+://(w+(-w+)*)(.(w+(-w+)*))*(?S*)?$
         验证电话号码：^(\(\d{3,4}\)|\d{3,4}-)?\d{7,8}$：--正确格式为：XXXX-XXXXXXX，XXXX-XXXXXXXX，XXX-XXXXXXX，XXX-XXXXXXXX，XXXXXXX，XXXXXXXX。
         验证身份证号（15位或18位数字）：^\d{15}|\d{}18$
         验证一年的12个月：^(0?[1-9]|1[0-2])$ 正确格式为：“01”-“09”和“1”“12”
         验证一个月的31天：^((0?[1-9])|((1|2)[0-9])|30|31)$ 正确格式为：01、09和1、31。
         整数：^-?\d+$
         非负浮点数（正浮点数 + 0）：^\d+(\.\d+)?$
         正浮点数 ^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$
         非正浮点数（负浮点数 + 0） ^((-\d+(\.\d+)?)|(0+(\.0+)?))$
         负浮点数 ^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$
         浮点数 ^(-?\d+)(\.\d+)?$
         */
        Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        Matcher matcher = pattern.matcher("1");
        if(matcher.matches()){
            System.out.println("true");
        }else{
            System.out.println("false");

        }

    }

}
