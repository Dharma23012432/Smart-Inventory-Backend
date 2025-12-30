package com.example.demo.Service;

import com.example.demo.Models.Product;
import com.example.demo.Models.Sale;
import com.example.demo.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Transactional
    public Sale recordSale(Product product, int quantity) {
        double unitPrice = product.getPrice();
        double total = unitPrice * quantity;
        Sale sale = new Sale(product, quantity, unitPrice, total);
        return saleRepository.save(sale);
    }
}
