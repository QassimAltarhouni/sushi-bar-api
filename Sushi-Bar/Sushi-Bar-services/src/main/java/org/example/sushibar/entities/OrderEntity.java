package org.example.sushibar.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {

    // ================== Primary Key ==================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================== Order Info ==================
    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private boolean paid = false;

    @Column(nullable = false)
    private double totalCost;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    // ✅ Optional: this field is not used in logic but kept for potential UI/log use
    private LocalDateTime date;

    // ================== Relationships ==================
    @ManyToMany
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItemEntity> items;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // ================== Lifecycle Callback ==================
    @PrePersist
    public void prePersist() {
        if (this.createdOn == null) {
            this.createdOn = LocalDateTime.now();
        }
    }

    // ================== Getters & Setters ==================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public LocalDateTime getCreatedOn() { return createdOn; }
    public void setCreatedOn(LocalDateTime createdOn) { this.createdOn = createdOn; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public List<MenuItemEntity> getItems() { return items; }
    public void setItems(List<MenuItemEntity> items) { this.items = items; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    // ================== Enum ==================
    public enum OrderStatus {
        PENDING, PREPARING, READY, DELIVERED, CANCELLED
    }
}
