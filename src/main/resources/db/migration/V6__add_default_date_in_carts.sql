ALTER TABLE carts
MODIFY COLUMN date_created DATE NOT NULL DEFAULT (curdate());
