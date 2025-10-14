CREATE TABLE store_api.orders (
	id BIGINT auto_increment,
	customer_id BIGINT NOT NULL,
	status varchar(20) NOT NULL,
	created_at DATETIME DEFAULT current_timestamp NOT NULL,
	total_price DECIMAL(10,2) NOT NULL,
	CONSTRAINT orders_pk PRIMARY KEY (id),
	CONSTRAINT orders_users_FK FOREIGN KEY (customer_id) REFERENCES store_api.users(id)
);

CREATE TABLE store_api.order_items (
	id BIGINT auto_increment,
	order_id BIGINT NOT NULL,
	product_id BIGINT NOT NULL,
	unit_price DECIMAL(10,2) NOT NULL,
	quantity INT NOT NULL,
	total_price DECIMAL(10,2) NOT NULL,
	CONSTRAINT order_items_pk PRIMARY KEY (id),
	CONSTRAINT order_items_products_FK FOREIGN KEY (product_id) REFERENCES store_api.products(id),
	CONSTRAINT order_items_orders_FK FOREIGN KEY (order_id) REFERENCES store_api.orders(id)
)