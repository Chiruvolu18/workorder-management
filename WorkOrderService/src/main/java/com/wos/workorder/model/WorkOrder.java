package com.wos.workorder.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.wos.workorder.enums.WorkOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "work_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String assetId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkOrderStatus status; // PENDING, ASSIGNED, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private LocalDate requiredCompletionDate;

    @Column
    private String assignedTechnician;

    @Column(length = 1000)
    private String description;

    @Column
    private String priority;  // HIGH, MEDIUM, LOW

    @Column
    private LocalDateTime lastModifiedDate;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDate.now();
        lastModifiedDate = LocalDateTime.now();
        if (status == null) {
            status = WorkOrderStatus.PENDING;
        }
        if (priority == null) {
            priority = "MEDIUM";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = LocalDateTime.now();
    }
}
