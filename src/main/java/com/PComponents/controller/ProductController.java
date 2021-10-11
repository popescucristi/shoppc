package com.PComponents.controller;

import com.PComponents.model.Product;
import com.PComponents.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping(value = "/newProduct")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "/newProduct";
    }

    @PostMapping(value = "/new-product")
    public String createNewProduct(@Valid Product product, Model model) {
        productService.addProduct(product);
        model.addAttribute("product", product);
        model.addAttribute("successMessage", "Produsul a fost adaugat cu succes!");
        return "/newProduct";
    }

    @GetMapping(value = "/search")
    public String viewHomePage(Model model, @Param("keyword") String keyword) {
        List<Product> listProducts = productService.listAll(keyword);
        model.addAttribute("productsList", listProducts);
        model.addAttribute("keyword", keyword);
        return "user/ProductsUserHome";
    }

    @GetMapping(value = "/edit")
    public String edit(Model model) {
        model.addAttribute("edit");
        return "edit";
    }

    @GetMapping(value = "/editProduct/{id}")
    public String editProduct(Model model, @PathVariable("id") Long id) {
        Optional<Product> product = productService.findById(id);
        model.addAttribute("product", product);
        return "edit";
    }

    @PostMapping(value = "/updateProduct")
    public String updateProduct(Product product) {
        productService.updateProduct(product);
        return "redirect:/user/ProductsUserHome";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id) throws Exception {
        productService.deleteProductById(id);
        return "redirect:/user/ProductsUserHome";
    }
}
