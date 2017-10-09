/*
package com.gennlift.common;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

*/
/**
 * Created by lz on 2017/9/29.
 *//*

public class ConditionSplitFirstAnd {
    */
/**
     * 先将查询条件使用逻辑词 regx 拆分
     * 如果regx="or" ,那么只拆分主逻辑内的，不会才分（ or ）这种逻辑内的 or 关联的部分
     * 目前此方法只支持or
     * @param source
     * @param regx
     * @param result  所有被regx非开的字符串的集合
     *//*

    public void splitCondition(String source, String regx, List<String> result){
        if(StringUtils.isEmpty(regx)){
            regx = CommontContent.LOGIC_OPERATOR_TYPE_OR;
        }


    }

    */
/**
     * 获取source中 Or 逻辑词不在（）内的or出现的下标值
     * @param source
     * @return
     *//*

    public int getOrIndexNotInBraces(String source){
        int index = 0;
        if(StringUtils.isEmpty(source)){
            return -1;
        }
        //获取Or第一次出现在source中的位置
        int orFirstIndex = findPatterIndex(source, "Or");
        String forOrInSource = source.substring(0,orFirstIndex);

        return index;
    }

    */
/**
     * 搜索所有的(和),如果全部可以匹配为（），则说明不在（）内，否则在
     * @param source
     * @return
     *//*

    public boolean isInBraces(String source ){
        int leftBracesNum = 0;
        int rightBracesNum = 0;
        if(StringUtils.isEmpty(source)){
            return false;
        }
        int sourceLength = source.length()-1;
        for(int i=0, j = sourceLength; i<=j; i++,j--){
            if(source.charAt(i) ==CommontContent.BRACKETS[0]){
                leftBracesNum++;
            }
            if(source.charAt(i) ==CommontContent.BRACKETS[1]){
                rightBracesNum++;
            }
            if(source.charAt(j) ==CommontContent.BRACKETS[0]){
                leftBracesNum++;
            }
            if(source.charAt(j) ==CommontContent.BRACKETS[1]){
                rightBracesNum++;
            }

        }
        return leftBracesNum==rightBracesNum;
    }

    */
/**
     * 找到关键词第一次出现在text中的位置
     * @param text
     * @param pattern
     * @return pattern 出现在text中的位置
     *//*

    public int findPatterIndex(String text, String pattern) {
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
}
*/
