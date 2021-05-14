package com.mongodb.demo.service;

import com.mongodb.demo.dto.ProductDto;
import com.mongodb.demo.repository.ProductRepository;
import com.mongodb.demo.utils.ProductToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Flux<ProductDto> getProducts(){
        return productRepository.findAll().map(ProductToDtoConverter::entityToDto);
    }

    public Mono<ProductDto> getProductById(String id){
        return productRepository.findById(id).map(ProductToDtoConverter::entityToDto);
    }

    public Flux<ProductDto> getProductsInRanga(double min, double max){
        return productRepository.findByPriceBetween(Range.closed(min, max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
        return productDtoMono.map(ProductToDtoConverter::dtoToEntity).
                flatMap(productRepository::insert)
                .map(ProductToDtoConverter::entityToDto);

    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id){
        return productRepository.findById(id)
                .flatMap(p -> productDtoMono.map(ProductToDtoConverter::dtoToEntity))
                .doOnNext(e -> e.setId(id))
                .flatMap(productRepository::save)
                .map(ProductToDtoConverter::entityToDto);
    }

    public Mono<Void> deleteProduct(String id){
        return productRepository.deleteById(id);
    }


}
