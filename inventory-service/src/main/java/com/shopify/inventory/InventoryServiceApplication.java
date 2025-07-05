package com.shopify.inventory;

import com.shopify.inventory.model.Inventory;
import com.shopify.inventory.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){

		return args -> {
			List<Inventory> inventoryList = new ArrayList<>();
			Inventory inventory = new Inventory();
			inventory.setSkuCode("Iphone-13-red");
			inventory.setQuantity(10);

			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("iphone-14");
			inventory1.setQuantity(5);

			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("Iphone-15");
			inventory2.setQuantity(0);

			inventoryList.add(inventory);
			inventoryList.add(inventory1);
			inventoryList.add(inventory2);

			inventoryRepository.saveAll(inventoryList);
		};
	}
}
