package com.wos.workorder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkOrderRequest {
    
    private String customerName;
    private String assetId;
    private String status;
    private LocalDate requiredCompletionDate;
    private String assignedTechnician;
    private String description;
    private String priority;
}
