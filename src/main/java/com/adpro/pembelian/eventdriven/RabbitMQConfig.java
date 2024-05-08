package com.adpro.pembelian.eventdriven;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue purchaseQueue() {
        return new Queue("purchase-queue", false);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue("order-status-queue", false);
    }

    @Bean
    public Queue trackingOrderQueue(){
        return new Queue("tracking-order-queue", false);
    }
    @Bean
    public Queue stokProdukQueue(){
        return new Queue("stock-product-queue", false);
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange("helloworld");
    }

    @Bean
    public Binding purchaseBinding(Queue purchaseQueue, Exchange exchange) {
        return BindingBuilder.bind(purchaseQueue)
            .to(exchange)
            .with("purchase-routing-key")
            .noargs();
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, Exchange exchange) {
        return BindingBuilder.bind(orderQueue)
            .to(exchange)
            .with("order-status-routing-key")
            .noargs();
    }

    @Bean
    public Binding trackingOrderBinding(Queue trackingOrderQueue, Exchange exchange) {
        return BindingBuilder.bind(trackingOrderQueue)
            .to(exchange)
            .with("tracking-order-routing-key")
            .noargs();
    }
    @Bean
    public Binding updateStokProdukBinding(Queue stokProdukQueue, Exchange exchange){
        return BindingBuilder.bind(stokProdukQueue)
            .to(exchange)
            .with("update-stok-produk-routing-key")
            .noargs();
    }
}