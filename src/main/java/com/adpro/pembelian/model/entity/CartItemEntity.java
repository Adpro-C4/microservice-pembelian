package com.adpro.pembelian.model.entity;
import com.adpro.pembelian.model.dto.DTOCartItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private String name;
    private int quantity;
    private double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderTemplate order;

    public DTOCartItem toDTO() {
        DTOCartItem dto = new DTOCartItem();
        dto.setId(this.id);
        dto.setProductId(this.productId);
        dto.setName(this.name);
        dto.setQuantity(this.quantity);
        dto.setPrice(this.price);
        return dto;
    }

    public static CartItemEntity fromDTO(DTOCartItem dto) {
        CartItemEntity entity = new CartItemEntity();
        entity.setId(dto.getId());
        entity.setProductId(dto.getProductId());
        entity.setName(dto.getName());
        entity.setQuantity(dto.getQuantity());
        entity.setPrice(dto.getPrice());
        return entity;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ShoppingCartEntity shoppingCart;


    public CartItemEntity(){}
    public CartItemEntity(Long id, String productId, String name, int quantity, double price){
        this.id = id;
        this.productId = productId;
        this.name = name;
        setPrice(price);
        setQuantity(quantity);
    }

    public void setPrice(double price) {
        validatePrice(price);
        this.price = price;
    }

    public void validatePrice(double price){
        if(price<=0) throw  new IllegalArgumentException("harga harus bilangan positif");
    }

    public void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }
    public void validateQuantity(int quantity){
        if(quantity <=0) throw new IllegalArgumentException("Cart Item tidak boleh kosong");
    }
    public double calculateTotalPrice() {
        return price * quantity;
    }
}

