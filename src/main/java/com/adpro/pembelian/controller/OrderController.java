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
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    PurchaseService purchaseService;
    @PostMapping("/create")
    ResponseEntity<Object> createOrder(@RequestBody DTOPurchaseInformation purchaseInformation){
        try{
            Map<String, Object> data = new HashMap<>();
            String message = "Berhasil menambahkan order";
            purchaseService.createPurchaseRequest(purchaseInformation);
            data.put("message", message);
            return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED, data);
        }catch(IllegalArgumentException err){
            return ResponseHandler.generateResponse(err.getMessage(), 
            HttpStatus.NOT_ACCEPTABLE, err);
        }catch(NoSuchElementException err){
            return ResponseHandler.generateResponse(err.getMessage(), 
            HttpStatus.NOT_FOUND, err);
        }
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Object> removeOrder(@PathVariable String id){
        try {
            Map<String, Object> data = new HashMap<>();
            String message = "Berhasil menghapus order";
            purchaseService.removePurchaseRequest(id);
            data.put("message", message);
            return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED, data);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, e);
        }
    }

    @GetMapping("/view/user/{id}")
    ResponseEntity<Object>  viewUserOrders(@PathVariable String id ){
        try {
            Map<String, Object> data = new HashMap<>();
            String message = "Berhasil menampilkan semua order dari user dengan id" + id;
            data.put("orders", purchaseService.viewAllOrderByUserId(id));
            data.put("message", message);
            return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED, data);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, e);
        }
    }

    @GetMapping("view/order-data/{id}")
    ResponseEntity<Object>  viewOrder(@PathVariable String id ){
        try {
            Map<String, Object> data = new HashMap<>();
            String message = "Menampilkan order dengan id" + id;
            data.put("order", purchaseService.viewOrder(id));
            data.put("message", message);
            return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED, data);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, e);
        }
    }


}
