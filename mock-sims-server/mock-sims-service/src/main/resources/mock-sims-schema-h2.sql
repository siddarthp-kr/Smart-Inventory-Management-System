

CREATE TABLE ORDER_TRANSACTION_INFO
(
    general_order_id int AUTO_INCREMENT PRIMARY KEY,
    store_number varchar(5) NOT NULL,
    division_number varchar(3) NOT NULL,
    placed_by_user_euid varchar(10),
    order_placed_time timestamp NOT NULL,
    received_by_user_euid varchar(10),
    order_received boolean NOT NULL DEFAULT FALSE,
    order_received_time timestamp
);


CREATE TABLE MARKDOWN_RULES
(
    subcommodity_number varchar(15) PRIMARY KEY,
    first_markdown_percent int,
    can_be_marked_down boolean NOT NULL ,
    days_before_exp_to_markdown_number int,
    days_before_exp_to_rfi_number int,
    days_after_order_to_set_exp int
);

CREATE TABLE DEPARTMENT_INFO (
    department_number char(2) PRIMARY KEY,
    department_name varchar(50) NOT NULL
);

CREATE TABLE PRODUCT_BASIC_INFO
(
    upc_number varchar(15) PRIMARY KEY,
    subcommodity_number varchar(5) NOT NULL,
    department_number char(2) NOT NULL,
    product_name varchar(100) NOT NULL,
    standard_price decimal(10,2) NOT NULL,
    CONSTRAINT FK_PRODUCT_BASIC_MARKDOWN
        FOREIGN KEY (subcommodity_number)
            REFERENCES MARKDOWN_RULES(subcommodity_number),
    CONSTRAINT FK_PRODUCT_BASIC_INFO
        FOREIGN KEY (department_number) REFERENCES DEPARTMENT_INFO(department_number)
);

CREATE TABLE PRODUCT_BOH_INFO
(
    division_number varchar(3) NOT NULL,
    store_number varchar(5) NOT NULL,
    upc_number varchar(15) NOT NULL,
    qod_number int NOT NULL,
    qom_number int NOT NULL,
    CONSTRAINT PK_PRODUCT_BOH_INFO
        PRIMARY KEY (division_number, store_number, upc_number),
    CONSTRAINT FK_PRODUCT_BOH_UPC
        FOREIGN KEY (upc_number)
        REFERENCES PRODUCT_BASIC_INFO(upc_number)

);


CREATE TABLE PRODUCT_INVENTORY_INFO
(
    product_order_id int AUTO_INCREMENT PRIMARY KEY,
    general_order_id int NOT NULL,
    upc_number varchar(15) NOT NULL,
    quantity int NOT NULL,
    expiration_date date,
    order_date date,
    is_active boolean,
    CONSTRAINT FK_PRODUCT_INVENTORY_ORDER
        FOREIGN KEY (general_order_id)
        REFERENCES ORDER_TRANSACTION_INFO(general_order_id),
    CONSTRAINT FK_PRODUCT_INVENTORY_UPC
        FOREIGN KEY (upc_number)
        REFERENCES PRODUCT_BASIC_INFO(upc_number)
);

ALTER TABLE PRODUCT_INVENTORY_INFO
    ADD CONSTRAINT CHECK_PRODUCT_INVENTORY_QUANTITY CHECK (quantity >= 0);

CREATE TABLE ORDER_MOVEMENT_TRANSACTIONS(
    general_order_id int NOT NULL,
    upc_number varchar(15) NOT NULL,
    quantity int NOT NULL,
    qod_before_transaction int,

    CONSTRAINT PK_ORDER_MOVEMENT_ORDER
        PRIMARY KEY (general_order_id, upc_number),

    CONSTRAINT FK_ORDER_MOVEMENT_ORDER
        FOREIGN KEY (general_order_id)
            REFERENCES ORDER_TRANSACTION_INFO(general_order_id),

    CONSTRAINT FK_ORDER_MOVEMENT_UPC
        FOREIGN KEY (upc_number)
            REFERENCES PRODUCT_BASIC_INFO(upc_number),

    CONSTRAINT CHECK_ORDER_MOVEMENT_QUANTITY
        CHECK (quantity >= 0)
);

CREATE TABLE PDM_ALERTS
(
    alert_id int AUTO_INCREMENT PRIMARY KEY,
    store_number varchar(5) NOT NULL,
    division_number varchar(3) NOT NULL,
    department_number char(2) NOT NULL,
    upc_number varchar(15) NOT NULL,
    quantity int,
    expiration_date date,
    markdown_after_date date,
    rfi_after_date date,
    first_markdown_percent int,
    is_active BOOLEAN NOT NULL,
    alert_actioned_time timestamp,
    alert_actioned_user_euid varchar(10),
    alert_actioned_code varchar(4),

    CONSTRAINT FK_PDM_ALERTS_UPC
        FOREIGN KEY (upc_number)
        REFERENCES PRODUCT_BASIC_INFO(upc_number)
);

CREATE TABLE RFI_TRANSACTIONS (
      transaction_id int AUTO_INCREMENT PRIMARY KEY,
      user_euid varchar(10),
      store_number varchar(5) NOT NULL,
      division_number varchar(3) NOT NULL,
      upc_number varchar(15) NOT NULL,
      qod_before_transaction int NOT NULL,
      qom_before_transaction int NOT NULL,
      action_time timestamp,
      quantity_removed int,
      reason_code varchar(10),

      CONSTRAINT FK_RFI_PRODUCT
          FOREIGN KEY (upc_number)
          REFERENCES PRODUCT_BASIC_INFO(upc_number)
);

CREATE TABLE MD_TRANSACTIONS (
     transaction_id int AUTO_INCREMENT PRIMARY KEY,
     user_euid varchar(10),
     store_number varchar(5) NOT NULL,
     division_number varchar(3) NOT NULL,
     upc_number varchar(15) NOT NULL,
     qod_before_transaction int NOT NULL,
     qom_before_transaction int NOT NULL,
     action_time timestamp,
     quantity_marked_down int,
     original_price decimal(10,2),
     new_price decimal(10,2),

    CONSTRAINT FK_MD_PRODUCT
        FOREIGN KEY (upc_number)
        REFERENCES PRODUCT_BASIC_INFO(upc_number)
);


