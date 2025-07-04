package org.example.sushibar.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerNumber;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

    private LocalDateTime date;
    private Integer tableNumber;

    @Column(name = "number_of_people")
    private Integer numberOfPeople;

    @Column(name = "location")
    private String location;

    @Column(name = "position")
    private String position;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Integer getTableNumber() { return tableNumber; }
    public void setTableNumber(Integer tableNumber) { this.tableNumber = tableNumber; }

    public Integer getNumberOfPeople() { return numberOfPeople; }
    public void setNumberOfPeople(Integer numberOfPeople) { this.numberOfPeople = numberOfPeople; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }

    // Enum definition
    public enum ReservationStatus {
        PENDING,
        CONFIRMED,
        SEATED,
        COMPLETED,
        CANCELLED
    }
}
