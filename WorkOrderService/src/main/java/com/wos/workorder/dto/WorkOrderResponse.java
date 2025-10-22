package com.wos.workorder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrderResponse {
    
    private Long id;
    private String customerName;
    private String assetId;
    private String status;
    private LocalDate creationDate;
    private LocalDate requiredCompletionDate;
    private String assignedTechnician;
    private String description;
    private String priority;
    private LocalDateTime lastModifiedDate;
}
