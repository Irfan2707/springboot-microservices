package com.shopify.product;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost:" + port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateProduct() {
		String productRequest = """
            {
                "name": "Test Product",
                "description": "This is a test product",
                "price": 19.99
            }
            """;

		RestAssured.given()
				.contentType("application/json")
				.body(productRequest)
				.when()
				.post("/api/v1/products/create")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("Test Product"))
				.body("description", Matchers.equalTo("This is a test product"))
				.body("price", Matchers.equalTo(19.99F));
	}

//	@Test
//	void shouldGetAllProducts() {
//		RestAssured.given()
//				.contentType("application/json")
//				.when()
//				.get("/api/v1/products/all")
//				.then()
//				.statusCode(200)
//				.body("$", Matchers.not(Matchers.empty()))
//				.body("[0].id", Matchers.notNullValue())
//				.body("[0].name", Matchers.notNullValue())
//				.body("[0].description", Matchers.notNullValue())
//				.body("[0].price", Matchers.notNullValue());
//	}
}