package com.adpro.pembelian.controller;


import com.adpro.pembelian.model.dto.DTOPurchaseInformation;
import com.adpro.pembelian.service.external.APIProductService;
import com.adpro.pembelian.service.external.APICustomerDetailsService;
import com.adpro.pembelian.service.internal.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    APIProductService productService;
    @Autowired
    APICustomerDetailsService userDetailsService;
    @Autowired
    PurchaseService purchaseService;
    @GetMapping("/get-a-product")
    public String getProductTest(){
        productService.getProductFromAPI("1");
        return "SUCCESS";
    }

    @GetMapping("/create-order")
    public String testCreateOrder(){
        purchaseService.createPurchaseRequest(new DTOPurchaseInformation(
                "152",
                new ArrayList<>(),
                "12",
                "Depok Jawa Barat",
                "WUZZ"
        ));
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
