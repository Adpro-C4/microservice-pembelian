package com.adpro.pembelian.controller;


import com.adpro.pembelian.service.external.APIProductService;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    APIProductService productService;
    @Autowired
    APICustomerDetailsService userDetailsService;
    @GetMapping("/get-a-product")
    public String getProductTest(){
        productService.getProductFromAPI("1");
        return "SUCCESS";
    }

    @GetMapping("/get-a-user")
    public String getUserDetailTest(){
        userDetailsService.getUserDetailsAPI("152");
        return  "SUCCESS";
    }

    @GetMapping("/get-all-product")
    public String getAllProductTest(){
        productService.getAllProductFromAPI();
        return "SUCCESS" ;
    }
}
