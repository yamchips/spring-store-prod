CREATE TABLE carts (
    id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) NOT NULL,
    date_created DATE NOT NULL,
    CONSTRAINT Cart_PK PRIMARY KEY (id)
);
CREATE TABLE cart_items (
	id BIGINT auto_increment NOT NULL,
	cart_id BINARY(16) NOT NULL,
	product_id BIGINT NOT NULL,
	quantity INT DEFAULT 1 NOT NULL,
	CONSTRAINT cart_items_pk PRIMARY KEY (id),
	CONSTRAINT cart_items_cart_product_unique UNIQUE KEY (cart_id,product_id),
	CONSTRAINT cart_items_carts_FK FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
	CONSTRAINT cart_items_products_FK FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);