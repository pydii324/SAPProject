package com.demo.service.logic;

import com.demo.model.product.ProductEntity;
import com.demo.model.sale.Sale;
import com.demo.model.sale.SaleStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PriceCalculator {

    private SaleService saleService;

    /*public float calculatePrice(ProductEntity product, int quantity) {
        if (product.getSaleList().isEmpty()) {
            return product.getPrice() * quantity;
        } else {
            float pricePercentage = 1;
            for (Sale sale : product.getSaleList()) {
                if (sale.getStatus().equals(SaleStatus.ACTIVE)) {
                    pricePercentage -= sale.getDiscountPercentage();
                }
            }
            float price = product.getPrice() * quantity * pricePercentage;
            if (price < product.getMinDiscountPrice())
                return product.getPrice() * quantity;
            return price * quantity;
        }
    }*/

    public float calculatePrice(ProductEntity product, int quantity) {
        List<Sale> saleList = saleService.loadAllSales();
        float totalPrice = 0f;
        for (Sale sale : saleList) {
            for (ProductEntity productInSale : sale.getProducts()) {
                if (!Objects.equals(productInSale.getId(), product.getId())) {
                    return product.getPrice() * quantity;
                } else {
                    float pricePercentage = 1f;
                    pricePercentage -= sale.getDiscountPercentage();
                    totalPrice = product.getPrice() * quantity * pricePercentage;
                    return totalPrice;
                }
            }
        }
        return totalPrice;
    }
}
