package com.gennlift.common;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于解析条件和获取Json数据
 * create by liuzhen 2017/09/30
 *
 */
public class XJsonPath {
    private static Logger LOGGER = LoggerFactory.getLogger(XJsonPath.class);

    /**
     * 根据 condition的条件 从json中搜索 符合其条件的内容
     * @param json   原始json
     * @param condition 条件 格式形如 {
     *           //等于
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
                String condition1 = "store.书.类别 包含 erence";}
     * @param conRegx 一级（" "）拆分表达式
     * @param columnRegx 二级（"."）拆分表达式
     * @return
     */
    public JSONArray searcheByCondition(JSONObject json, String condition, String conRegx, String columnRegx) throws Exception {
        LOGGER.info("参数 condition={}，conRegx={},columnRegx={}",condition,conRegx,columnRegx);
        if(StringUtils.isEmpty(condition)){
            return new JSONArray();
        }
        List<String> conditions = splitExpression(condition, conRegx);
        if (conditions == null || conditions.isEmpty() || conditions.size()<3) {
            return  new JSONArray();
        }
        //暂时只取前3个，后期根据实际修改
        String columns = conditions.get(0);
        String logicSign = conditions.get(1);
        String express = conditions.get(2);
        //获取条件中“store.书.价格”的 类："store.书"和属性："价格"
        Map<String, String> objectAndAtr = splitColumn(columns, columnRegx);
        String ex = getEx(logicSign,express);
        if(StringUtils.isEmpty(ex)){
            return new JSONArray();
        }else {
            String searchEx = "$.".concat(objectAndAtr.get("object")).concat("[?(@.").concat(objectAndAtr.get("attr")).concat(ex).concat(")]");
            JSONArray read = JsonPath.read(json, searchEx);
            LOGGER.debug("校验成功，获取到数据个数"+read.size());
            return read;
        }
    }

    /**
     * 根据 操作符 logicSign 凭借条件和值的表达式 如logicSign=小于，express=10  那么返回值为 < 10
     * @param logicSign
     * @param express
     * @return
     */
    public String getEx(String logicSign,String express){
        String ex = "";
        //数值判断
        Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        Matcher matcher = pattern.matcher(express);
        //非数字且logicSign 不包含 ”包含“字样则加上''
        if(!matcher.matches()&& !StringUtils.contains(logicSign,"包含")){
            express = "'"+express+"'";
        }
        if(logicSign.trim().equals(CommontContent.RELATION_OPERATOR_TYPE_LESS_CHINIESS) || logicSign.trim().equals(CommontContent.RELATION_OPERATOR_TYPE_ALER_THAN)|| logicSign.trim().equals(CommontContent.RELATION_OPERATOR_TYPE_ALER_THAN_ACTIVE)){
            ex = CommontContent.RELATION_OPERATOR_TYPE_LESS.concat(" ").concat(express);
        }else if(logicSign.trim().equals(CommontContent.RELATION_OPERATOR_TYPE_THAN_CHINIESS)|| logicSign.trim().equals(CommontContent.RELATION_OPERATOR_TYPE_LATER_THAN)|| logicSign.trim().equals(CommontContent.RELATION_OPERATOR_TYPE_NOT_LATER_THAN_ACTIVE)){
            ex = CommontContent.RELATION_OPERATOR_TYPE_THAN.concat(" ").concat(express);
        }else if(logicSign.trim().equals(CommontContent.RELATION_OPERATOR_TYPE_LESS_EQUAL_CHINIESS)){
            ex = CommontContent.RELATION_OPERATOR_TYPE_LESS_EQUAL.concat(" ").concat(express);
        }else if(logicSign.trim().equals(CommontContent.RELATION_OPERATOR_TYPE_THAN_EQUAL_CHINIESS)){
            ex = CommontContent.RELATION_OPERATOR_TYPE_THAN_EQUAL.concat(" ").concat(express);
        }else if(logicSign.trim().equals(CommontContent.RELATION_OPERATOR_TYPE_EQUAL_CHINIESS)){
            ex = CommontContent.RELATION_OPERATOR_TYPE_EQUAL.concat(" ").concat(express);
        }else if(logicSign.trim().equals(CommontContent.RELATION_OPERATOR_TYPE_CONTAIN)){
            //=~/.*cti.*?/i
            ex = "=~/.*".concat(express).concat(".*?/i");
        }
        return ex;
    }
    /**
     * 根据 regx 传入参数必须是形如：“书.类别 等于 reference”的字符串,将其分为查询字段/操作符/表达式
     * @param source  源字符串 格式形如 “书.类别 等于 reference”
     * @param regx    用于拆分字符串的表达式，即分隔符，此处的分隔符为 " "
     * @return
     */
    public List<String> splitExpression(String source, String regx) throws Exception {
        List<String> expressions = new ArrayList<String>();
        if(StringUtils.isNotEmpty(source)){
            String[] split = StringUtils.split(source, regx);

            if( split != null && split.length > 0 && split.length>=3){
                for (String express:split){
                    expressions.add(express);
                }
            }else{
                LOGGER.error("传入的参数"+source+"格式错误，传入参数必须是形如：“书.类别 等于 reference“");
                throw new Exception("传入的参数"+source+"格式错误，传入参数必须是形如：“书.类别 等于 reference“");
            }
        }
        return expressions;
    }

    /**
     * 拆分条件字段，将用于{@link JsonPath} 搜索数据，参数格式形如：store.书.价格
     * @param source 搜索字符串 形如store.书.价格，分隔符自行制定
     * @param regx  字符串分隔符
     * @return
     */
    public Map<String,String> splitColumn(String source, String regx) throws Exception {
        Map<String,String> expressions = new HashMap<String, String>();
        if(StringUtils.isNotEmpty(source)){
            String object = StringUtils.substringBeforeLast(source,regx);
            String attr = StringUtils.substringAfterLast(source,regx);
            expressions.put("object",object);
            expressions.put("attr",attr);
        }else{
            LOGGER.error("传入的参数错误，不能为空且格式必须为’A.B‘的格式 ");
            throw new Exception("传入的参数错误，不能为空且格式必须为’A.B‘的格式 ");
        }
        return expressions;
    }

}
