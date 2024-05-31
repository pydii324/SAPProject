package com.demo.dto;

import com.demo.model.sale.SaleStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SaleDTO {
    private LocalDateTime endDate;
    private float discountPercentage;
}
