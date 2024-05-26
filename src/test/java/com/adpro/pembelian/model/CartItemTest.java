package com.adpro.pembelian.model;
import com.adpro.pembelian.model.entity.CartItemEntity;
import com.adpro.pembelian.model.entity.OrderTemplate;
import com.adpro.pembelian.model.entity.OrderWithVoucherEntity;
import com.adpro.pembelian.model.entity.ShoppingCartEntity;
import com.adpro.pembelian.model.builder.CartItemBuilder;
import com.adpro.pembelian.model.dto.DTOCartItem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    public void testBuildCartItem() {

        Long id = 1L;
        String productId = "ABC123";
        String name = "Product Name";
        int quantity = 2;
        double price = 10.99;

        CartItemEntity cartItem = new CartItemBuilder()
                .withId(id)
                .withProductId(productId)
                .withName(name)
                .withQuantity(quantity)
                .withPrice(price)
                .build();

        assertEquals(id, cartItem.getId());
        assertEquals(productId, cartItem.getProductId());
        assertEquals(name, cartItem.getName());
        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(price, cartItem.getPrice(), 0.001);
    }


    @Test
    public void testSetNegativePrice(){
        CartItemEntity cartItem = new CartItemEntity();
        assertThrows(IllegalArgumentException.class, () -> {
            cartItem.setPrice(-3);
        });
    }

    @Test
    public void testSetPositivePrice(){
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setPrice(2000);
        assertEquals(2000, cartItem.getPrice() );
    }

    @Test
    public  void testZeroPrice(){
        CartItemEntity cartItem = new CartItemEntity();
        assertThrows(IllegalArgumentException.class, () -> {
            cartItem.setQuantity(0);
        });
    }

    @Test
    public void testCalculateTotalPrice() {

        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setProductId("123");
        cartItem.setQuantity(3);
        cartItem.setPrice(2000);
        double totalPrice = cartItem.calculateTotalPrice();
        assertEquals(6000, totalPrice);
    }

    @Test
    public void testSetNegativeQuantity() {
        CartItemEntity cartItem = new CartItemEntity();
        assertThrows(IllegalArgumentException.class, () -> {
            cartItem.setQuantity(-3);
        });
    }

    @Test
    public void testSetZeroQuantity() {
        CartItemEntity cartItem = new CartItemEntity();
        assertThrows(IllegalArgumentException.class, () -> {
            cartItem.setQuantity(0);
        });
    }

    @Test
    public void testToDTO() {
        CartItemEntity cartItem = new CartItemEntity(1L, "P123", "Product 1", 2, 100.0);
        DTOCartItem dto = cartItem.toDTO();

        assertEquals(cartItem.getId(), dto.getId());
        assertEquals(cartItem.getProductId(), dto.getProductId());
        assertEquals(cartItem.getName(), dto.getName());
        assertEquals(cartItem.getQuantity(), dto.getQuantity());
        assertEquals(cartItem.getPrice(), dto.getPrice(), 0.01);
    }

    @Test
    public void testFromDTO() {
        DTOCartItem dto = new DTOCartItem();
        dto.setId(1L);
        dto.setProductId("P123");
        dto.setName("Product 1");
        dto.setQuantity(2);
        dto.setPrice(100.0);

        CartItemEntity cartItem = CartItemEntity.fromDTO(dto);

        assertEquals(dto.getId(), cartItem.getId());
        assertEquals(dto.getProductId(), cartItem.getProductId());
        assertEquals(dto.getName(), cartItem.getName());
        assertEquals(dto.getQuantity(), cartItem.getQuantity());
        assertEquals(dto.getPrice(), cartItem.getPrice(), 0.01);
    }

    @Test
    public void testParameterizedConstructor() {
        Long id = 1L;
        String productId = "P123";
        String name = "Product 1";
        int quantity = 2;
        double price = 100.0;

        CartItemEntity cartItem = new CartItemEntity(id, productId, name, quantity, price);

        assertEquals(id, cartItem.getId());
        assertEquals(productId, cartItem.getProductId());
        assertEquals(name, cartItem.getName());
        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(price, cartItem.getPrice(), 0.01);
    }

        @Test
    public void testOrderAssociation() {
        OrderTemplate order = new OrderWithVoucherEntity();
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setOrder(order);

        assertEquals(order, cartItem.getOrder());
    }

    @Test
    public void testShoppingCartAssociation() {
        ShoppingCartEntity shoppingCart = new ShoppingCartEntity();
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setShoppingCart(shoppingCart);

        assertEquals(shoppingCart, cartItem.getShoppingCart());
    }



}
