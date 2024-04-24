package com.adpro.pembelian.controller;

import com.adpro.pembelian.common.ResponseHandler;
import com.adpro.pembelian.model.dto.DTOPurchaseInformation;
import com.adpro.pembelian.service.internal.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    PurchaseService purchaseService;
    @PostMapping("/create")
    ResponseEntity<Object> createOrder(@RequestBody DTOPurchaseInformation purchaseInformation){
        Map<String, Object> data = new HashMap<>();
        String message = "Berhasil menambahkan order";
        purchaseService.createPurchaseRequest(purchaseInformation);
        data.put("message", message);
        return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED, data);
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Object> removeOrder(@PathVariable String id){
        Map<String, Object> data = new HashMap<>();
        String message = "Berhasil menghapus order";
        purchaseService.removePurchaseRequest(id);
        data.put("message", message);
        return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED, data);
    }

    @GetMapping("/view/user/{id}")
    ResponseEntity<Object>  viewUserOrders(@PathVariable String id ){
        Map<String, Object> data = new HashMap<>();
        String message = "Berhasil menampilkan semua order dari user dengan id" + id;
        data.put("orders", purchaseService.viewAllOrderByUserId(id));
        data.put("message", message);
        return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED, data);
    }

    @GetMapping("view/order-data/{id}")
    ResponseEntity<Object>  viewOrder(@PathVariable String id ){
        Map<String, Object> data = new HashMap<>();
        String message = "Menampilkan order dengan id" + id;
        data.put("order", purchaseService.viewOrder(id));
        data.put("message", message);
        return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED, data);
    }


}
