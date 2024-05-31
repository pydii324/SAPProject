package com.demo.service.logic;

import com.demo.dto.SaleDTO;
import com.demo.model.product.ProductEntity;
import com.demo.model.sale.Sale;
import com.demo.model.sale.SaleStatus;
import com.demo.repository.ProductRepository;
import com.demo.repository.SaleRepository;
import com.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SaleService {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private SaleRepository saleRepository;

    public void startSale(SaleDTO saleDTO) {
        Sale sale = new Sale();
        sale.setStartDate(LocalDateTime.now());
        sale.setEndDate(saleDTO.getEndDate());
        sale.setDiscountPercentage(saleDTO.getDiscountPercentage());

        sale.setStatus(SaleStatus.ACTIVE);

        saleRepository.save(sale);
    }

    public Sale loadActiveSale() {
        Sale activeSale = saleRepository.findByEndDateBefore(LocalDateTime.now());
        return activeSale;
    }
    public List<Sale> loadAllSales() {
        return saleRepository.findAll();
    }

    public boolean addProductToSale(Long saleId, Long productId) {
        Optional<Sale> optionalSale = saleRepository.findById(saleId);
        if(optionalSale.isEmpty())
            return false;

        Sale activeSale = optionalSale.get();

        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isEmpty())
            return false;

        ProductEntity product = optionalProduct.get();

        List<ProductEntity> productsInSale = activeSale.getProducts();
        List<Sale> applicableSales = product.getSales();

        productsInSale.add(product);
        product.setSales(applicableSales);

        activeSale.setProducts(productsInSale);
        saleRepository.save(activeSale);
        productRepository.save(product);
        return true;
    }

    public boolean removeProductFromSale(Long saleId, Long productId) {
        Optional<Sale> optionalSale = saleRepository.findById(saleId);
        if(optionalSale.isEmpty())
            return false;

        Sale activeSale = optionalSale.get();

        Optional<ProductEntity> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isEmpty())
            return false;

        ProductEntity product = optionalProduct.get();

        List<ProductEntity> productsInSale = activeSale.getProducts();
        List<Sale> applicableSales = product.getSales();

        productsInSale.remove(product);
        product.setSales(applicableSales);

        activeSale.setProducts(productsInSale);
        saleRepository.save(activeSale);
        productRepository.save(product);
        return true;
    }

    public void endSale(Sale sale) {
        sale.setStatus(SaleStatus.INACTIVE);
        saleRepository.save(sale);
    }

    public boolean endSale(Long saleId) {
        Optional<Sale> optionalSale = saleRepository.findById(saleId);
        if (optionalSale.isEmpty())
            return false;

        Sale sale = optionalSale.get();
        sale.setStatus(SaleStatus.INACTIVE);
        saleRepository.save(sale);

        return true;
    }

    public List<Sale> getSalesForProduct(ProductEntity product) {
        List<Sale> allSales = saleRepository.findAll();
        List<Sale> applicableSales = new ArrayList<>();
        for (Sale sale : allSales) {
            if (sale.getProducts().contains(product)) {
                applicableSales.add(sale);
            }
        }
        return applicableSales;
    }

}
