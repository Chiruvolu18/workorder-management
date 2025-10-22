package com.wos.workorder.service;

import com.wos.workorder.enums.WorkOrderStatus;
import com.wos.workorder.model.WorkOrder;
import com.wos.workorder.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * Retrieves all work orders from the database
     * 
     * @return List of all work orders
     */
    public List<WorkOrder> findAllOrders() {
        return workOrderRepository.findAll();
    }

    /**
     * Retrieves a work order by its ID
     * 
     * @param id The ID of the work order to retrieve
     * @return Optional containing the work order if found, empty otherwise
     */
    public Optional<WorkOrder> findOrderById(Long id) {
        return workOrderRepository.findById(id);
    }

    /**
     * Creates a new work order
     * 
     * @param workOrder The work order to create
     * @return The created work order with generated ID
     */
    public WorkOrder createOrder(WorkOrder workOrder) {
        return workOrderRepository.save(workOrder);
    }

    /**
     * Updates an existing work order
     * 
     * @param id The ID of the work order to update
     * @param updatedWorkOrder The updated work order data
     * @return Optional containing the updated work order if found, empty otherwise
     */
    public Optional<WorkOrder> updateOrder(Long id, WorkOrder updatedWorkOrder) {
        return workOrderRepository.findById(id)
            .map(existingOrder -> {
                existingOrder.setCustomerName(updatedWorkOrder.getCustomerName());
                existingOrder.setAssetId(updatedWorkOrder.getAssetId());
                existingOrder.setStatus(updatedWorkOrder.getStatus());
                existingOrder.setRequiredCompletionDate(updatedWorkOrder.getRequiredCompletionDate());
                return workOrderRepository.save(existingOrder);
            });
    }

    /**
     * Deletes a work order by its ID
     * 
     * @param id The ID of the work order to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteOrder(Long id) {
        if (workOrderRepository.existsById(id)) {
            workOrderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Retrieves work orders by status
     * 
     * @param status The status to filter by
     * @return List of work orders with the specified status
     */
    public List<WorkOrder> findOrdersByStatus(String status) {
    	try {
    		WorkOrderStatus statusEnum = WorkOrderStatus.valueOf(status.toUpperCase());
    		return workOrderRepository.findByStatus(statusEnum);
    	} catch (IllegalArgumentException e) {
    		return List.of();
    	}
    }

    /**
     * Retrieves work orders by customer name
     * 
     * @param customerName The customer name to filter by
     * @return List of work orders for the specified customer
     */
    public List<WorkOrder> findOrdersByCustomer(String customerName) {
        return workOrderRepository.findByCustomerName(customerName);
    }

    /**
     * Retrieves work orders by asset ID
     * 
     * @param assetId The asset ID to filter by
     * @return List of work orders for the specified asset
     */
    public List<WorkOrder> findOrdersByAsset(String assetId) {
        return workOrderRepository.findByAssetId(assetId);
    }

    /**
     * Retrieves work orders due before a specific date
     * 
     * @param date The date to check against
     * @return List of work orders due before the specified date
     */
    public List<WorkOrder> findOverdueOrders(LocalDate date) {
        return workOrderRepository.findByRequiredCompletionDateBefore(date);
    }

    /**
     * Updates the status of a work order
     * 
     * @param id The ID of the work order
     * @param newStatus The new status
     * @return Optional containing the updated work order if found, empty otherwise
     */
    public Optional<WorkOrder> updateOrderStatus(Long id, WorkOrderStatus newStatus) {
        return workOrderRepository.findById(id)
            .map(order -> {
                order.setStatus(newStatus);
                return workOrderRepository.save(order);
            });
    }

    /**
     * Gets the total count of work orders
     * 
     * @return Total number of work orders
     */
    public long getTotalOrderCount() {
        return workOrderRepository.count();
    }
}
