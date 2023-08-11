package com.atguigu.gmall.all.controller;

import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.client.ProductFeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@Controller
public class CartController {

    @Resource
    private ProductFeignClient productFeignClient;

    @GetMapping("/addCart.html")
    public String addCart(Long skuId, Integer skuNum, Model model) {
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        model.addAttribute("skuInfo", skuInfo);
        model.addAttribute("skuNum", skuNum);
        return "cart/addCart";
    }

    @GetMapping("/cart.html")
    public String index() {
        return "cart/index";
    }
}
