use gula_production_db;
show tables;

CREATE TABLE if not exists production (
        id varchar(32) NOT NULL,
        product_id varchar(32) NOT NULL, -- Logical FK ke Product Service
        quantity INT NOT NULL,
        production_date DATE NOT NULL,
        notes TEXT,
        PRIMARY KEY (id)
);