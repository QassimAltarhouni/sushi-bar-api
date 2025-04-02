package org.example.sushibar.controllers;

import com.example.api.OrdersApi;
import com.example.models.MenuItem;
import com.example.models.OrderRequest;
import com.example.models.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class OrdersController implements OrdersApi {

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return OrdersApi.super.getRequest();
    }

    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest) {
        // Convert item IDs into MenuItem objects (mocked)
        List<MenuItem> menuItems = new ArrayList<>();
        for (Integer itemId : orderRequest.getItems()) {
            MenuItem item = new MenuItem()
                    .id(itemId)
                    .name("Sushi #" + itemId)
                    .description("Mock description for item " + itemId)
                    .price(10.99F)
                    .category("Default")
                    .imageUrl(URI.create("https://example.com/image/" + itemId));
            menuItems.add(item);
        }

        // Simulate order creation
        OrderResponse order = new OrderResponse()
                .orderId(1)
                .customerNumber(orderRequest.getCustomerNumber())
                .items(menuItems)
                .status("confirmed");

        return ResponseEntity.status(201).body(order);
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Integer orderId) {
        // Simulate deletion logic
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        // Simulate returning order list
        List<OrderResponse> orders = new ArrayList<>();

        orders.add(new OrderResponse()
                .orderId(1)
                .customerNumber("12345")
                .status("shipped")
                .items(new ArrayList<>()) // Empty list for simplicity
        );

        orders.add(new OrderResponse()
                .orderId(2)
                .customerNumber("67890")
                .status("processing")
                .items(new ArrayList<>())
        );

        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(Integer orderId) {
        // Simulate fetching order by ID
        OrderResponse order = new OrderResponse()
                .orderId(orderId)
                .customerNumber("12345")
                .status("delivered")
                .items(new ArrayList<>()); // Empty list for now

        return ResponseEntity.ok(order);
    }
}
