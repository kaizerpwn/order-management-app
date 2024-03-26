package com.ibrahimokic.ordermanagement.controller.api;

import com.ibrahimokic.ordermanagement.domain.dto.OrderDto;
import com.ibrahimokic.ordermanagement.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Operations related to orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders with details", description = "Get a list of all orders along with associated details")
    @ApiResponse(responseCode = "200", description = "List of orders with details", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
    })
    public ResponseEntity<List<OrderDto>> getAllOrdersWithDetails() {
        List<OrderDto> ordersWithDetails = orderService.getAllOrdersWithDetails();
        return ResponseEntity.ok(ordersWithDetails);
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Order by ID", description = "Get details of an order based on the provided order ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        OrderDto orderDto = orderService.getOrderById(orderId);

        if (orderDto != null) {
            return ResponseEntity.ok(orderDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Order not found");
        }
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete Order by ID", description = "Delete an order based on the provided order ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    public ResponseEntity<?> deleteOrderById(@PathVariable Long orderId) {
        boolean deleted = orderService.deleteOrderById(orderId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Order deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Order not found");
        }
    }

    @PostMapping
    @Operation(summary = "Create Order", description = "Create a new order based on the request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order with ID '4' does not exist.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDto orderDto) {
        ResponseEntity<?> responseEntity = orderService.createNewOrder(orderDto);

        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseEntity.getBody());
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode())
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(responseEntity.getBody());
        }
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "Edit Order", description = "Edit an order based on the request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order edited successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody @Valid OrderDto orderDto) {
        ResponseEntity<?> responseEntity = orderService.updateOrder(orderId, orderDto);

        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseEntity.getBody());
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode())
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(responseEntity.getBody());
        }
    }
}
