/**
 * copyRight
 */
package com.gennlife.controller;

import com.alibaba.fastjson.JSONObject;
import com.gennlife.dao.CustomActiveIndexDao;
import com.gennlife.dao.CustomerActiveIndexReferDao;
import com.gennlife.entity.CustomActiveIndex;
import com.gennlife.entity.CustomerActiveIndexRefer;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuzhen.
 * Date: 2017/10/10
 * Time: 11:02
 */
@RestController
@RequestMapping("active")
@Api(description = "用户")
public class ActiveIndexController {
    @Autowired
    private CustomActiveIndexDao activeIndexDao;
    @Autowired
    private CustomerActiveIndexReferDao indexReferDao;
    @ApiOperation(value = "根据Id获取数据",notes = "获取活动指标数据 Created by liuzhen.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" , value = "id" , paramType = "query", dataType = "Long",required = true)
    })
    @RequestMapping(value = "/index",method = {RequestMethod.GET,RequestMethod.POST})
    public Iterable<CustomActiveIndex> getActiveIndex(Long id){

        Iterable<CustomActiveIndex> all = activeIndexDao.findAll();
        /*if(all != null && all.iterator() != null){
            Iterator<CustomActiveIndex> iterator = all.iterator();
            while (iterator.hasNext()){
                CustomActiveIndex next = iterator.next();
            }
        }*/
        /*String dependence = byId.getDependenceActive();
        String[] split = dependence.split(",");
        for(String v:split){
            Long in_id = Long.valueOf(v);
            CustomActiveIndex byId1 = activeIndexDao.findById(in_id);

        }*/
        return all;
    }

    @ApiOperation(value = "删除活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",paramType = "query",dataType = "Long",required = true)
    })
    @RequestMapping(value="/delete", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String delete(Long id){
        String result = "";
        List<CustomerActiveIndexRefer> byReferId = indexReferDao.findByReferId(id);
        if(byReferId == null || byReferId.isEmpty()){
            CustomActiveIndex byId = activeIndexDao.findById(id);
            if(byId != null){
                activeIndexDao.delete(id);
                result = "删除成功";
            }else{
                result = "id="+id+"的数据不存在";
            }
        }else {
            result = JSONObject.toJSONString(byReferId);
        }
        return result;
    }



}
