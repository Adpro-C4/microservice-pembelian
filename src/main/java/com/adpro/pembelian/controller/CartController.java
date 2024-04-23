package com.adpro.pembelian.controller;

import com.adpro.pembelian.common.ResponseHandler;
import com.adpro.pembelian.model.CartItemDeletionInformation;
import com.adpro.pembelian.model.CartItemUpdateInformation;
import com.adpro.pembelian.model.ShoppingCartInformation;
import com.adpro.pembelian.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/shopping-cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/data/{id}")
    public ResponseEntity<Object> getShoppingCartInformation(@PathVariable String id){
        ShoppingCartInformation information = cartService.getShoppingCartInformation(id);
        Map<String,Object> response = new HashMap<>();
        String message = "berhasil mendapatkan data shopping cart";
        response.put("message", message);
        response.put("shoppingCart", information.shoppingCart());
        response.put("totalPrice", information.totalPrice());
        return  ResponseHandler.generateResponse(message,
                HttpStatus.ACCEPTED, response);
    }
    @PostMapping("/update")
    public ResponseEntity<Object> updateCartItemOnShoppingCart(@RequestBody CartItemUpdateInformation cartItemUpdateInformation){
        cartService.createOrUpdateCartItemToShoppingCart(cartItemUpdateInformation);
        Map<String, Object> response = new HashMap<>();
        String message = "Sukses mengupdate cart item";
        response.put("message", message);
        return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED,response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCartItemFromShoppingCart(@RequestBody CartItemDeletionInformation information){
        cartService.deleteCartItemFromShoppingCart(information);
        Map<String, Object> response = new HashMap<>();
        String message = "Sukses menghapus cart item";
        response.put("message", message);
        return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED,response);
    }
    @PostMapping("/create")
    public ResponseEntity<Object> createShoppingCart(@RequestBody String userId){
        cartService.createShoppingCart(userId);
        Map<String, Object> response = new HashMap<>();
        String message = "Sukses membuat shopping cart untuk user " +userId;
        response.put("message", message);
        return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED,response);
    }


}
