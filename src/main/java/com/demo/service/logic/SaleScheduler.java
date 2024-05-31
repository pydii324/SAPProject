package com.demo.service.logic;

import com.demo.model.sale.Sale;
import com.demo.repository.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class SaleScheduler {

    private final SaleService saleService;
    private SaleRepository saleRepository;

    @Scheduled(cron = "0 * * * * ?") // Tova se izpylnqva na vsqka minuta
    public void checkAndEndSale() {
        Sale activeSale = saleRepository.findByEndDateBefore(LocalDateTime.now());
        saleService.endSale(activeSale);
    }

}
