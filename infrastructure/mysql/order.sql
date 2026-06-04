show databases;
use gula_order_db;
show tables;

CREATE TABLE if not exists orders (
                        id varchar(32) NOT NULL,
                        customer_id varchar(32) NOT NULL,
                        order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        total_amount DECIMAL(15, 2) NOT NULL,
                        status ENUM('PENDING', 'PROCESSING', 'COMPLETED', 'CANCELLED') NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE if not exists order_item (
                            id varchar(32) NOT NULL,
                            order_id varchar(32) NOT NULL,
                            product_id varchar(32) NOT NULL, -- Logical FK ke Product Service
                            product_name VARCHAR(255) NOT NULL, -- Denormalisasi untuk snapshot data
                            price DECIMAL(15, 2) NOT NULL,        -- Denormalisasi harga saat dibeli
                            quantity INT NOT NULL,
                            subtotal DECIMAL(15, 2) NOT NULL,
                            PRIMARY KEY (id),
                            CONSTRAINT fk_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);