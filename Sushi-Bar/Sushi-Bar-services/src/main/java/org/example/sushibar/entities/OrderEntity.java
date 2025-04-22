package org.example.sushibar.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerNumber;
    private OrderStatus status = OrderStatus.PENDING;
    private LocalDateTime date = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @ManyToMany
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItemEntity> items;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public List<MenuItemEntity> getItems() { return items; }
    public void setItems(List<MenuItemEntity> items) { this.items = items; }


    public enum OrderStatus {
        PENDING, PREPARING, READY, DELIVERED, CANCELLED
    }
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
