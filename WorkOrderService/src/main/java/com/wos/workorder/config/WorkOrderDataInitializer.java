package com.wos.workorder.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wos.workorder.enums.WorkOrderStatus;
import com.wos.workorder.model.WorkOrder;
import com.wos.workorder.repository.WorkOrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
@Profile("dev")
public class WorkOrderDataInitializer implements CommandLineRunner {

    private final WorkOrderRepository workOrderRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public WorkOrderDataInitializer(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Clear existing data (optional - remove if you want to keep data between restarts)
        workOrderRepository.deleteAll();
        
        // Get the name of the sequence used by PostgreSQL for the 'work_orders' table.
        // It's typically 'table_name_id_seq'.
        String sequenceName = "work_orders_id_seq";
        
        // Use TRUNCATE to clear data and safely reset the sequence to 1.
        // TRUNCATE is faster than deleteAll() and resets the sequence automatically in PostgreSQL (WITH RESTART IDENTITY).
        entityManager.createNativeQuery("TRUNCATE TABLE work_orders RESTART IDENTITY CASCADE").executeUpdate();
        
        System.out.println("Work Orders table truncated and sequence reset.");

        // Create mock work orders
        WorkOrder wo1 = new WorkOrder();
        wo1.setCustomerName("ABC Manufacturing");
        wo1.setAssetId("PUMP-2401");
        wo1.setStatus(WorkOrderStatus.PENDING);
        wo1.setCreationDate(LocalDate.now());
        wo1.setRequiredCompletionDate(LocalDate.now().plusDays(5));

        WorkOrder wo2 = new WorkOrder();
        wo2.setCustomerName("PowerGrid Solutions");
        wo2.setAssetId("TRANSFORMER-5502");
        wo2.setStatus(WorkOrderStatus.IN_PROGRESS);
        wo2.setCreationDate(LocalDate.now().minusDays(2));
        wo2.setRequiredCompletionDate(LocalDate.now().plusDays(3));

        WorkOrder wo3 = new WorkOrder();
        wo3.setCustomerName("Metro Water Authority");
        wo3.setAssetId("VALVE-3301");
        wo3.setStatus(WorkOrderStatus.COMPLETED);
        wo3.setCreationDate(LocalDate.now().minusDays(10));
        wo3.setRequiredCompletionDate(LocalDate.now().minusDays(3));

        WorkOrder wo4 = new WorkOrder();
        wo4.setCustomerName("City Electric Department");
        wo4.setAssetId("CABLE-7890");
        wo4.setStatus(WorkOrderStatus.PENDING);
        wo4.setCreationDate(LocalDate.now().minusDays(1));
        wo4.setRequiredCompletionDate(LocalDate.now().plusDays(7));

        WorkOrder wo5 = new WorkOrder();
        wo5.setCustomerName("Industrial Gas Systems");
        wo5.setAssetId("COMPRESSOR-4455");
        wo5.setStatus(WorkOrderStatus.ON_HOLD);
        wo5.setCreationDate(LocalDate.now().minusDays(5));
        wo5.setRequiredCompletionDate(LocalDate.now().plusDays(2));

        // Save all work orders to database
        workOrderRepository.save(wo1);
        workOrderRepository.save(wo2);
        workOrderRepository.save(wo3);
        workOrderRepository.save(wo4);
        workOrderRepository.save(wo5);

        System.out.println("================================================");
        System.out.println("Mock Work Orders Created Successfully!");
        System.out.println("Total Work Orders: " + workOrderRepository.count());
        System.out.println("Next expected ID: " + (workOrderRepository.count() + 1));
        System.out.println("================================================");
        
        // Display created work orders
//        workOrderRepository.findAll().forEach(wo -> 
//            System.out.println("ID: " + wo.getId() + 
//                             " | Customer: " + wo.getCustomerName() + 
//                             " | Asset: " + wo.getAssetId() + 
//                             " | Status: " + wo.getStatus())
//        );
//        System.out.println("================================================");
    }
}

