use gula_product_db;
show tables;

CREATE TABLE if not exists product (
         id varchar(32) NOT NULL,
         code VARCHAR(50) NOT NULL,
         name VARCHAR(255) NOT NULL,
         category VARCHAR(100),
         price DECIMAL(15, 2) NOT NULL,
         status BOOLEAN DEFAULT TRUE,
         PRIMARY KEY (id)
);