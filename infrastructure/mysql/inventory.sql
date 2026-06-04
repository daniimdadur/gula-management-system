use gula_inventory_db;
show tables;

CREATE TABLE if not exists inventory (
       id varchar(32) NOT NULL,
       product_id varchar(32) NOT NULL, -- Logical FK ke Product Service
       current_stock INT NOT NULL DEFAULT 0,
       minimum_stock INT NOT NULL DEFAULT 0,
       PRIMARY KEY (id)
);

CREATE TABLE if not exists inventory_transaction (
       id varchar(32) NOT NULL,
       inventory_id varchar(32) NOT NULL,
       transaction_type ENUM('IN', 'OUT', 'ADJUSTMENT') NOT NULL,
       quantity INT NOT NULL,
       reference_type VARCHAR(100), -- Menyimpan asal transaksi (misal: 'PRODUCTION' atau 'ORDER')
       reference_id varchar(32),       -- Menyimpan ID dari Production atau Order terkait
       notes TEXT,
       PRIMARY KEY (id),
       CONSTRAINT fk_transaction_inventory FOREIGN KEY (inventory_id) REFERENCES inventory(id) ON DELETE CASCADE
);