package com.mongodb.demo;

import com.mongodb.demo.controller.ProductController;
import com.mongodb.demo.dto.ProductDto;
import com.mongodb.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class DemoApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ProductService productService;

	@Test
	public void addProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("202","Orange",3, 300.00));
		when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);

		webTestClient.post().uri("/api/products/")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void getProductsTest(){
		Flux<ProductDto> productDtoFlux= Flux.just(new ProductDto("102","Radio",2,300.00),
				new ProductDto("103","TV",2,300.00),
				new ProductDto("104","Mobile",2,300.00),
				new ProductDto("102","Radio",2,300.00));
		when(productService.getProducts()).thenReturn(productDtoFlux);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/api/products/")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new ProductDto("102","Radio",2,300.00))
				.expectNext(new ProductDto("103","TV",2,300.00))
				.expectNext(new ProductDto("104","Mobile",2,300.00))
				.expectNext(new ProductDto("102","Radio",2,300.00))
				.verifyComplete();
	}

	@Test
	public void getProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102", "Mobile",3, 300.00));
		when(productService.getProductById("102")).thenReturn(productDtoMono);
		Flux<ProductDto> responseBody = webTestClient.get().uri("/api/products/102")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();
		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p -> p.getName().equals("Mobile"))
				.verifyComplete();
	}

	@Test
	public void updateProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102","mobile",2, 300.00));
		when(productService.updateProduct(productDtoMono, "102")).thenReturn(productDtoMono);

		webTestClient.put().uri("/api/products/102")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void deleteProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102","mobile",2, 300.00));
		when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);
		given(productService.deleteProduct(any())).willReturn(Mono.empty());
		webTestClient.delete().uri("/api/products/102")
				.exchange()
				.expectStatus().isOk();

	}





}
