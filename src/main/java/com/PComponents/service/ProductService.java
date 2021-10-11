package com.PComponents.service;

import com.PComponents.model.Product;
import com.PComponents.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findByName(String name) {
        return productRepository.findAllByProductName(name);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> listAll(String keyword) {
        if (keyword != null) {
            return productRepository.search(keyword);
        }
        return productRepository.findAll();
    }

    public Product updateProduct(Product product) {
        if (product.getIdProduct() == null) {
            product = productRepository.save(product);
            return product;
        } else {
            Optional<Product> existingProduct = productRepository.findById(product.getIdProduct());
            if (existingProduct.isPresent()) {
                Product newProduct = existingProduct.get();
                newProduct.setProductPrice(product.getProductPrice());
                newProduct.setStock(product.isStock());
                newProduct = productRepository.save(newProduct);
                return newProduct;
            } else {
                product = productRepository.save(product);
                return product;
            }
        }
    }

    public void deleteProductById(Long id) throws Exception {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            id = product.get().getIdProduct();
            productRepository.deleteById(id);
        } else {
            throw new Exception("No product for that ID");
        }
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
}
