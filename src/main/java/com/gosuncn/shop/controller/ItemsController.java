package com.gosuncn.shop.controller;

import com.gosuncn.shop.entities.Items;
import com.gosuncn.shop.entities.School;
import com.gosuncn.shop.service.ItemsService;
import com.gosuncn.shop.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: chenxihua
 * @Date: 2018-12-26:9:34
 */
@Slf4j
@RequestMapping(value = "/items")
@Controller
public class ItemsController {


    @Autowired
    ItemsService itemsService;
    @Autowired
    SchoolService schoolService;

    /**
     * 查询商品分类，总有几大类
     * @return
     */
//    @GetMapping(value = "/getItems")
//    public Map<String, Object> getItems(){
//        Map<String, Object> map = new HashMap<>();
//        List<Items> itemsByAll = itemsService.getItemsByAll();
//        map.put("code", 0);
//        map.put("success", true);
//        map.put("data", itemsByAll);
//        map.put("msg", "查询商品分类成功");
//        return map;
//    }

    /**
     * 用于商品上传页面，加载上传商品时，下拉框的资料信息。
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSelectedDates")
    public Map<String, Object> getSelectedDates(){
        Map<String, Object> result = new HashMap<>();
        try{
            List<School> schools = schoolService.findAllSchoolInfos();
            List<Items> itemsByAll = itemsService.getItemsByAll();
            result.put("code", 0);
            result.put("schoolDatas", schools);
            result.put("itemDatas", itemsByAll);
            result.put("msg", "查询成功");
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result.put("code", 500);
            result.put("data", new ArrayList());
            result.put("msg", e.toString());
        }
        return result;
    }


}
