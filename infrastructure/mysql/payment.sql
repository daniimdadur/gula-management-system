use gula_payment_db;
show tables;

CREATE TABLE if not exists payment (
                         id varchar(32) NOT NULL,
                         order_id varchar(32) NOT NULL, -- Logical FK ke Order Service
                         payment_method ENUM('MANUAL_BANK_TRANSFER', 'VIRTUAL_ACCOUNT', 'E_WALLET', 'CREDIT_CARD') NOT NULL, -- Contoh isi opsi pembayaran
                         amount DECIMAL(15, 2) NOT NULL,
                         status ENUM('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING', -- Contoh isi opsi status
                         payment_date DATETIME, -- Diisi null dulu saat PENDING, diupdate saat SUCCESS
                         PRIMARY KEY (id)
);