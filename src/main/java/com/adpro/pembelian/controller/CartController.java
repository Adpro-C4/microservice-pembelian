package com.adpro.pembelian.controller;

import com.adpro.pembelian.common.ResponseHandler;
import com.adpro.pembelian.model.dto.DTOCartItemDeletionInformation;
import com.adpro.pembelian.model.dto.DTOCartItemUpdateInformation;
import com.adpro.pembelian.model.dto.DTOShoppingCartInformation;
import com.adpro.pembelian.service.internal.CartService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/shopping-cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/data/{id}")
    public ResponseEntity<Object> getShoppingCartInformation(@PathVariable String id){
        try {
            DTOShoppingCartInformation information = cartService.getShoppingCartInformation(id);
            Map<String,Object> response = new HashMap<>();
            String message = "berhasil mendapatkan data shopping cart";
            response.put("message", message);
            response.put("shoppingCart", information.dtoCartItemList());
            response.put("totalPrice", information.totalPrice());
            return  ResponseHandler.generateResponse(message,
                    HttpStatus.ACCEPTED, response);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse("Data shopping cart tidak ditemukan!", HttpStatus.NOT_FOUND, e);
        }
    }
    @PostMapping("/update")
    public ResponseEntity<Object> updateCartItemOnShoppingCart(@RequestBody DTOCartItemUpdateInformation cartItemUpdateInformation){
        try{
            cartService.createOrUpdateCartItemToShoppingCart(cartItemUpdateInformation);
            Map<String, Object> response = new HashMap<>();
            String message = "Sukses mengupdate cart item";
            response.put("message", message);
            return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED,response);
        }catch (NoSuchElementException exception){
            String message = "Cart item tidak ditemukan";
            return ResponseHandler.generateResponse(message, HttpStatus.NOT_FOUND, exception);
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<Object> deleteCartItemFromShoppingCart(@RequestBody DTOCartItemDeletionInformation information){
        try {
            cartService.deleteCartItemFromShoppingCart(information);
            Map<String, Object> response = new HashMap<>();
            String message = "Sukses menghapus cart item";
            response.put("message", message);
            return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED,response);
        } catch (NoSuchElementException e) {
            return ResponseHandler.generateResponse("Cart Item yang akan dihapus rupanya tidak ada!", HttpStatus.NOT_FOUND, new HashMap<>());
        }
    }
    @PostMapping("/create")
    public ResponseEntity<Object> createShoppingCart(@RequestBody JsonNode node){
        try{
            String userId = node.get("userId").asText();
            cartService.createShoppingCart(userId);
            Map<String, Object> response = new HashMap<>();
            String message = "Sukses membuat shopping cart untuk user " +userId;
            response.put("message", message);
            return ResponseHandler.generateResponse(message, HttpStatus.ACCEPTED,response);
        }catch (NoSuchElementException exception){
            return ResponseHandler.generateResponse("User tidak ditemukan!",
                    HttpStatus.INTERNAL_SERVER_ERROR, new HashMap<>());
        }
    }
}
