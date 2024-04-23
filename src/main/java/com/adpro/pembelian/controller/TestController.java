package com.adpro.pembelian.controller;


import com.adpro.pembelian.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    ProductService productService;
    @GetMapping("/get-a-product")
    public String getProductTest(){
        productService.getProductFromAPI("1");
        return "SUCCESS" ;
    }

    @GetMapping("/get-all-product")
    public String getAllProductTest(){
        productService.getAllProductFromAPI();
        return "SUCCESS" ;
    }
}
