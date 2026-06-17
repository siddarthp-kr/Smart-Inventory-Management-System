--For Mark Down
INSERT INTO MARKDOWN_RULES
VALUES ('62000', 20, TRUE, 1, 2, 3);
INSERT INTO MARKDOWN_RULES
VALUES ('64307', 20, TRUE, 1, 2, 3);
INSERT INTO MARKDOWN_RULES
VALUES ('44805', 20, TRUE, 1, 2, 3);
INSERT INTO MARKDOWN_RULES
VALUES ('40610', 20, TRUE, 1, 2, 3);
INSERT INTO MARKDOWN_RULES
VALUES ('53000', 20, TRUE, 1, 2, 3);
INSERT INTO MARKDOWN_RULES
VALUES ('56005', 20, TRUE, 1, 2, 3);
INSERT INTO MARKDOWN_RULES
VALUES ('04703', 20, TRUE, 1, 2, 3);
INSERT INTO MARKDOWN_RULES
VALUES ('98418', 20, TRUE, 1, 2, 3);
INSERT INTO MARKDOWN_RULES
VALUES ('27602', 20, TRUE, 1, 2, 3);
INSERT INTO MARKDOWN_RULES
VALUES ('29465', 20, TRUE, 1, 2, 3);

--For Product Basic Info
INSERT INTO PRODUCT_BASIC_INFO (upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES ('4011', '62000', '07', 'Bananas', 0.75);
INSERT INTO PRODUCT_BASIC_INFO (upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES ('4022', '64307', '07', 'ST Cashews', 4.99);
INSERT INTO PRODUCT_BASIC_INFO (upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES ('3011', '44805', '10', 'Feta Greek', 4.79);
INSERT INTO PRODUCT_BASIC_INFO (upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES ('3022', '40610', '10', 'Shortbread Butter Cookies', 3.79);
INSERT INTO PRODUCT_BASIC_INFO (upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES ('1011', '53000', '09', 'Chicken Breast Boneless', 9.48);
INSERT INTO PRODUCT_BASIC_INFO (upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES ('1022', '56005', '09', 'KRO GRND BF Burger', 7.49);
INSERT INTO PRODUCT_BASIC_INFO (upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES ('2011', '04703', '01', 'Merry Edwards Pinot Noir', 6.99);
INSERT INTO PRODUCT_BASIC_INFO (upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES ('2022', '98418', '01', 'LA PREF Garbanzo Chickpeas', 2.29);
INSERT INTO PRODUCT_BASIC_INFO (upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES ('2033', '27602', '01', 'Motts Fruit Snacks', 8.49);
INSERT INTO PRODUCT_BASIC_INFO (upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES ('2044', '29465', '01', 'Honey Bunches Oats', 4.29);

--For Product BOH Info
INSERT INTO PRODUCT_BOH_INFO (division_number, store_number, upc_number,qod_number, qom_number)
VALUES ( '014', '00045', '4011', 0, 0 );
INSERT INTO PRODUCT_BOH_INFO (division_number, store_number, upc_number,qod_number, qom_number)
VALUES ( '014', '00045', '4022', 0, 0 );
INSERT INTO PRODUCT_BOH_INFO (division_number, store_number, upc_number,qod_number, qom_number)
VALUES ( '014', '00045', '3011', 0, 0 );
INSERT INTO PRODUCT_BOH_INFO (division_number, store_number, upc_number,qod_number, qom_number)
VALUES ( '014', '00045', '3022', 0, 0 );
INSERT INTO PRODUCT_BOH_INFO (division_number, store_number, upc_number,qod_number, qom_number)
VALUES ( '014', '00045', '1011', 0, 0 );
INSERT INTO PRODUCT_BOH_INFO (division_number, store_number, upc_number,qod_number, qom_number)
VALUES ( '014', '00045', '1022', 0, 0 );
INSERT INTO PRODUCT_BOH_INFO (division_number, store_number, upc_number,qod_number, qom_number)
VALUES ( '014', '00045', '2011', 0, 0 );
INSERT INTO PRODUCT_BOH_INFO (division_number, store_number, upc_number,qod_number, qom_number)
VALUES ( '014', '00045', '2022', 0, 0 );
INSERT INTO PRODUCT_BOH_INFO (division_number, store_number, upc_number,qod_number, qom_number)
VALUES ( '014', '00045', '2033', 0, 0 );
INSERT INTO PRODUCT_BOH_INFO (division_number, store_number, upc_number,qod_number, qom_number)
VALUES ( '014', '00045', '2044', 0, 0 );

