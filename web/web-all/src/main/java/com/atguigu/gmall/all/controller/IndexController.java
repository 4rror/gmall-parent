package com.atguigu.gmall.all.controller;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private ProductFeignClient productFeignClient;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping({"/", "/index", "/index.html"})
    public String index(Model model) {
        Result<List<JSONObject>> result = productFeignClient.getBaseCategoryList();
        model.addAttribute("list", result.getData());
        return "index/index";
    }

    @ResponseBody
    @GetMapping("/createIndex")
    public Result<Object> createIndex() {
        Result<List<JSONObject>> baseCategoryList = productFeignClient.getBaseCategoryList();
        List<JSONObject> data = baseCategoryList.getData();

        Context context = new Context();
        context.setVariable("list", data);

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("D:\\workspace\\atguigu\\proj3\\gmall-index\\index.html");
            templateEngine.process("index/index", context, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.ok();
    }
}
