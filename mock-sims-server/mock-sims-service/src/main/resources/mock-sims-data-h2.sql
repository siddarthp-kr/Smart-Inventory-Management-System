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

--For Department Info

INSERT INTO DEPARTMENT_INFO (department_number, department_name)
VALUES ('00', 'DFLT 159 VALUE');
INSERT INTO DEPARTMENT_INFO (department_number, department_name)
VALUES ('01', 'GROCERY');
INSERT INTO DEPARTMENT_INFO (department_number, department_name)
VALUES ('03', 'DRUG/GM');
INSERT INTO DEPARTMENT_INFO (department_number, department_name)
VALUES ('06', 'PHARMACY');
INSERT INTO DEPARTMENT_INFO (department_number, department_name)
VALUES ('07', 'PRODUCE');
INSERT INTO DEPARTMENT_INFO (department_number, department_name)
VALUES ('09', 'MEAT');
INSERT INTO DEPARTMENT_INFO (department_number, department_name)
VALUES ('10', 'DELI/BAKE');
INSERT INTO DEPARTMENT_INFO (department_number, department_name)
VALUES ('30', 'SUPPLIES');
INSERT INTO DEPARTMENT_INFO (department_number, department_name)
VALUES ('34', 'MISC SALES TRAN');
INSERT INTO DEPARTMENT_INFO (department_number, department_name)
VALUES ('58', 'FUEL');


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

