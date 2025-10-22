package com.wos.workorder.repository;

import com.wos.workorder.enums.WorkOrderStatus;
import com.wos.workorder.model.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

	// Find work orders by status
	List<WorkOrder> findByStatus(WorkOrderStatus status);

	// Find work orders by customer name
	List<WorkOrder> findByCustomerName(String customerName);

	// Find work orders by asset ID
	List<WorkOrder> findByAssetId(String assetId);

	// Find work orders created on a specific date
	List<WorkOrder> findByCreationDate(LocalDate creationDate);

	// Find work orders due before a specific date
	List<WorkOrder> findByRequiredCompletionDateBefore(LocalDate date);

	// Find work orders by status and ordered by required completion date
	List<WorkOrder> findByStatusOrderByRequiredCompletionDateAsc(String status);
}
