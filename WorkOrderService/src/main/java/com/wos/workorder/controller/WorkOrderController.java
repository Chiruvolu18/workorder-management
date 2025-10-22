package com.wos.workorder.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wos.workorder.enums.WorkOrderStatus;
import com.wos.workorder.model.WorkOrder;
import com.wos.workorder.service.WorkOrderService;

@RestController
@RequestMapping("/api/workorders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping
    public ResponseEntity<List<WorkOrder>> getAllWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.findAllOrders();
        return ResponseEntity.ok(workOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrder> getWorkOrderById(@PathVariable Long id) {
        return workOrderService.findOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WorkOrder> createWorkOrder(@RequestBody WorkOrder workOrder) {
        WorkOrder createdOrder = workOrderService.createOrder(workOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrder> updateWorkOrder(@PathVariable Long id, 
                                                     @RequestBody WorkOrder workOrder) {
        return workOrderService.updateOrder(id, workOrder)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkOrder(@PathVariable Long id) {
        if (workOrderService.deleteOrder(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<WorkOrder>> getWorkOrdersByStatus(@PathVariable String status) {
        List<WorkOrder> workOrders = workOrderService.findOrdersByStatus(status);
        return ResponseEntity.ok(workOrders);
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<WorkOrder>> getWorkOrdersByCustomer(@PathVariable String customerName) {
        List<WorkOrder> workOrders = workOrderService.findOrdersByCustomer(customerName);
        return ResponseEntity.ok(workOrders);
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<WorkOrder>> getWorkOrdersByAsset(@PathVariable String assetId) {
        List<WorkOrder> workOrders = workOrderService.findOrdersByAsset(assetId);
        return ResponseEntity.ok(workOrders);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<WorkOrder>> getOverdueWorkOrders() {
        List<WorkOrder> workOrders = workOrderService.findOverdueOrders(LocalDate.now());
        return ResponseEntity.ok(workOrders);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WorkOrder> updateWorkOrderStatus(@PathVariable Long id, 
                                                           @RequestParam WorkOrderStatus status) {
        return workOrderService.updateOrderStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getWorkOrderCount() {
        long count = workOrderService.getTotalOrderCount();
        return ResponseEntity.ok(count);
    }
}