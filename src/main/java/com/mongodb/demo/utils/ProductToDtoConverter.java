package com.mongodb.demo.utils;

import com.mongodb.demo.dto.ProductDto;
import com.mongodb.demo.entity.Product;
import org.springframework.beans.BeanUtils;

public class ProductToDtoConverter {

    public static ProductDto entityToDto(Product product){
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    public static Product dtoToEntity(ProductDto productDto){
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }
}
