package com.demo.service.logic;

import com.demo.model.order.ProductsInOrder;
import com.demo.model.product.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BasicSum {
    public float sum(HashMap<ProductEntity, Float> productsPrice) {
        float sum = 0;
        for (Map.Entry<ProductEntity, Float> entry : productsPrice.entrySet()) {
            sum += entry.getValue();
        }
        return sum;
    }

    public float orderSum(List<ProductsInOrder> productsInOrderList) {
        float sum = 0;
        for (ProductsInOrder productsInOrder : productsInOrderList) {
            sum += productsInOrder.getProductPrice();
        }
        return sum;
    }
}

