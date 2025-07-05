CREATE TABLE `orders` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
   `order_number` VARCHAR(50) DEFAULT NULL,
   `skucode` VARCHAR(50) NOT NULL,
   `price` DECIMAL(10,2) NOT NULL,
   `quantity` INT(11) NOT NULL,
   PRIMARY KEY (`id`)
);
