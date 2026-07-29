/* ============================================================
   MARKDOWN RULES
   ============================================================ */

INSERT INTO MARKDOWN_RULES
(subcommodity_number, first_markdown_percent, can_be_marked_down,
 days_before_exp_to_markdown_number, days_before_exp_to_rfi_number,
 days_after_order_to_set_exp)
VALUES
    ('62000', 25, TRUE, 2, 1, 5),
    ('64307', 20, TRUE, 30, 7, 180),
    ('44805', 25, TRUE, 7, 2, 21),
    ('40610', 25, TRUE, 3, 1, 7),
    ('53000', 30, TRUE, 2, 1, 5),
    ('56005', 20, TRUE, 30, 7, 180),
    ('04703', NULL, FALSE, NULL, NULL, NULL),
    ('98418', 15, TRUE, 30, 7, 365),
    ('27602', 15, TRUE, 30, 7, 270),
    ('29465', 15, TRUE, 30, 7, 365),

    ('70001', 30, TRUE, 2, 1, 5),
    ('70002', 20, TRUE, 2, 1, 4),
    ('90001', 35, TRUE, 2, 1, 4),
    ('10001', 25, TRUE, 2, 1, 5),
    ('10002', 20, TRUE, 2, 1, 4),
    ('01001', 20, TRUE, 5, 2, 14),
    ('03001', 10, TRUE, 90, 30, 730),
    ('03002', 10, TRUE, 120, 60, 1095),
    ('06001', 10, TRUE, 180, 30, 730),
    ('30001', 10, TRUE, 30, 7, 365),
    ('00001', 10, TRUE, 30, 7, 180),
    ('34001', NULL, FALSE, NULL, NULL, NULL),
    ('58001', NULL, FALSE, NULL, NULL, NULL);


/* ============================================================
   DEPARTMENT INFO
   ============================================================ */

INSERT INTO DEPARTMENT_INFO
(department_number, department_name)
VALUES
    ('00', 'DFLT 159 VALUE'),
    ('01', 'GROCERY'),
    ('03', 'DRUG/GM'),
    ('06', 'PHARMACY'),
    ('07', 'PRODUCE'),
    ('09', 'MEAT'),
    ('10', 'DELI/BAKE'),
    ('30', 'SUPPLIES'),
    ('34', 'MISC SALES TRAN'),
    ('58', 'FUEL');


/* ============================================================
   PRODUCT BASIC INFO
   ============================================================ */

INSERT INTO PRODUCT_BASIC_INFO
(upc_number, subcommodity_number, department_number, product_name, standard_price)
VALUES
    ('4011', '62000', '07', 'Bananas', 0.75),
    ('4022', '64307', '07', 'ST Cashews', 4.99),
    ('3011', '44805', '10', 'Feta Greek', 4.79),
    ('3022', '40610', '10', 'Shortbread Butter Cookies', 3.79),
    ('1011', '53000', '09', 'Chicken Breast Boneless', 9.48),
    ('1022', '56005', '09', 'KRO GRND BF Burger', 7.49),
    ('2011', '04703', '01', 'Merry Edwards Pinot Noir', 6.99),
    ('2022', '98418', '01', 'LA PREF Garbanzo Chickpeas', 2.29),
    ('2033', '27602', '01', 'Motts Fruit Snacks', 8.49),
    ('2044', '29465', '01', 'Honey Bunches Oats', 4.29),

    ('5001', '70001', '07', 'Fresh Strawberries', 3.99),
    ('5002', '70001', '07', 'Fresh Blueberries', 4.49),
    ('5020', '70002', '07', 'Caesar Salad Kit', 5.99),
    ('5021', '62000', '07', 'Honeycrisp Apples', 1.49),

    ('5003', '90001', '09', 'Atlantic Salmon Fillet', 12.99),
    ('5023', '90001', '09', 'Raw Shrimp Skewers', 9.99),

    ('5004', '10001', '10', 'Deli Rotisserie Chicken', 8.99),
    ('5005', '10002', '10', 'Bakery French Bread', 2.49),

    ('5006', '01001', '01', 'Half Gallon Milk', 2.99),
    ('5007', '01001', '01', 'Greek Yogurt', 1.39),
    ('5022', '01001', '01', 'Sliced Cheddar Cheese', 3.99),
    ('5019', '98418', '01', 'Spring Water 24 Pack', 5.99),

    ('5008', '03001', '03', 'Herbal Shampoo', 6.49),
    ('5009', '03002', '03', 'Daily Multivitamins', 12.99),

    ('5010', '06001', '06', 'Acetaminophen Tablets', 7.99),
    ('5011', '06001', '06', 'Allergy Relief Tablets', 11.49),
    ('5018', '06001', '06', 'Cold Medicine Syrup', 8.99),
    ('5024', '06001', '06', 'Honey Lemon Cough Drops', 3.49),

    ('5012', '30001', '30', 'Paper Towels', 9.99),
    ('5013', '30001', '30', 'Tall Kitchen Trash Bags', 7.99),

    ('5014', '00001', '00', 'Reusable Shopping Bag', 1.49),

    ('5015', '34001', '34', 'Propane Tank Exchange', 19.99),

    ('5016', '58001', '58', 'Regular Fuel Gallon', 3.59);


/* ============================================================
   INITIAL PRODUCT BOH INFO
   Most products start in stock. A few are intentionally out of stock.
   qom_number starts at 0; markdown transactions later move QOD to QOM.
   ============================================================ */

INSERT INTO PRODUCT_BOH_INFO
(division_number, store_number, upc_number, qod_number, qom_number)
SELECT
    '014',
    '00045',
    upc_number,
    CASE upc_number
        WHEN '4011' THEN 90
        WHEN '4022' THEN 35
        WHEN '3011' THEN 12
        WHEN '3022' THEN 28
        WHEN '1011' THEN 16
        WHEN '1022' THEN 24
        WHEN '2011' THEN 0
        WHEN '2022' THEN 40
        WHEN '2033' THEN 22
        WHEN '2044' THEN 30
        WHEN '5001' THEN 10
        WHEN '5002' THEN 12
        WHEN '5020' THEN 18
        WHEN '5021' THEN 60
        WHEN '5003' THEN 9
        WHEN '5023' THEN 7
        WHEN '5004' THEN 14
        WHEN '5005' THEN 20
        WHEN '5006' THEN 25
        WHEN '5007' THEN 36
        WHEN '5022' THEN 18
        WHEN '5019' THEN 32
        WHEN '5008' THEN 15
        WHEN '5009' THEN 14
        WHEN '5010' THEN 22
        WHEN '5011' THEN 18
        WHEN '5018' THEN 11
        WHEN '5024' THEN 34
        WHEN '5012' THEN 50
        WHEN '5013' THEN 42
        WHEN '5014' THEN 8
        WHEN '5015' THEN 5
        WHEN '5016' THEN 0
        ELSE 10
        END,
    0
FROM PRODUCT_BASIC_INFO;

INSERT INTO PRODUCT_BOH_INFO
(division_number, store_number, upc_number, qod_number, qom_number)
SELECT
    '014',
    '00123',
    upc_number,
    CASE upc_number
        WHEN '4011' THEN 75
        WHEN '4022' THEN 30
        WHEN '3011' THEN 14
        WHEN '3022' THEN 20
        WHEN '1011' THEN 12
        WHEN '1022' THEN 18
        WHEN '2011' THEN 4
        WHEN '2022' THEN 35
        WHEN '2033' THEN 20
        WHEN '2044' THEN 25
        WHEN '5001' THEN 8
        WHEN '5002' THEN 15
        WHEN '5020' THEN 16
        WHEN '5021' THEN 55
        WHEN '5003' THEN 0
        WHEN '5023' THEN 5
        WHEN '5004' THEN 12
        WHEN '5005' THEN 18
        WHEN '5006' THEN 22
        WHEN '5007' THEN 28
        WHEN '5022' THEN 16
        WHEN '5019' THEN 26
        WHEN '5008' THEN 10
        WHEN '5009' THEN 12
        WHEN '5010' THEN 20
        WHEN '5011' THEN 14
        WHEN '5018' THEN 9
        WHEN '5024' THEN 30
        WHEN '5012' THEN 44
        WHEN '5013' THEN 38
        WHEN '5014' THEN 6
        WHEN '5015' THEN 0
        WHEN '5016' THEN 1000
        ELSE 10
        END,
    0
FROM PRODUCT_BASIC_INFO;


/* ============================================================
   ORDER TRANSACTION INFO
   New ordering logic:
   - PLACED: order exists but has not been received/cancelled
   - RECEIVED: order has been received; inventory rows exist
   - CANCELLED: order has been cancelled; no inventory rows exist
   ============================================================ */

INSERT INTO ORDER_TRANSACTION_INFO
(general_order_id, store_number, division_number, placed_by_user_euid,
 order_placed_time, order_status, action_by_user_euid, order_action_time)
VALUES
    /* PLACED orders */
    (1, '00123', '014', 'AB12345',
     DATEADD('HOUR', -6, DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
     'PLACED', NULL, NULL),

    (2, '00045', '014', 'EF24680',
     DATEADD('HOUR', -3, DATEADD('DAY', -1, CURRENT_TIMESTAMP)),
     'PLACED', NULL, NULL),

    /* RECEIVED orders */
    (3, '00045', '014', 'AB12345', DATEADD('HOUR', 9, DATEADD('DAY', -5, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 8, DATEADD('DAY', -4, CURRENT_DATE))),
    (4, '00123', '014', 'AB12345', DATEADD('HOUR', 9, DATEADD('DAY', -6, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 8, DATEADD('DAY', -5, CURRENT_DATE))),
    (5, '00045', '014', 'EF24680', DATEADD('HOUR', 10, DATEADD('DAY', -5, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 9, DATEADD('DAY', -4, CURRENT_DATE))),
    (6, '00123', '014', 'EF24680', DATEADD('HOUR', 10, DATEADD('DAY', -5, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 9, DATEADD('DAY', -4, CURRENT_DATE))),
    (7, '00045', '014', 'GH13579', DATEADD('HOUR', 8, DATEADD('DAY', -4, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 7, DATEADD('DAY', -3, CURRENT_DATE))),
    (8, '00123', '014', 'GH13579', DATEADD('HOUR', 8, DATEADD('DAY', -5, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 7, DATEADD('DAY', -4, CURRENT_DATE))),
    (9, '00045', '014', 'AB12345', DATEADD('HOUR', 11, DATEADD('DAY', -4, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 8, DATEADD('DAY', -3, CURRENT_DATE))),
    (10, '00123', '014', 'AB12345', DATEADD('HOUR', 11, DATEADD('DAY', -4, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 8, DATEADD('DAY', -3, CURRENT_DATE))),
    (11, '00045', '014', 'EF24680', DATEADD('HOUR', 9, DATEADD('DAY', -17, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 8, DATEADD('DAY', -16, CURRENT_DATE))),
    (12, '00123', '014', 'EF24680', DATEADD('HOUR', 9, DATEADD('DAY', -16, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 8, DATEADD('DAY', -15, CURRENT_DATE))),
    (13, '00045', '014', 'AB12345', DATEADD('HOUR', 9, DATEADD('DAY', -6, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 8, DATEADD('DAY', -5, CURRENT_DATE))),
    (14, '00123', '014', 'AB12345', DATEADD('HOUR', 9, DATEADD('DAY', -7, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 8, DATEADD('DAY', -6, CURRENT_DATE))),
    (15, '00045', '014', 'GH13579', DATEADD('HOUR', 10, DATEADD('DAY', -5, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 9, DATEADD('DAY', -4, CURRENT_DATE))),
    (16, '00123', '014', 'GH13579', DATEADD('HOUR', 10, DATEADD('DAY', -4, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 9, DATEADD('DAY', -3, CURRENT_DATE))),
    (17, '00045', '014', 'AB12345', DATEADD('HOUR', 9, DATEADD('DAY', -13, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 8, DATEADD('DAY', -12, CURRENT_DATE))),
    (18, '00123', '014', 'EF24680', DATEADD('HOUR', 9, DATEADD('DAY', -11, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 8, DATEADD('DAY', -10, CURRENT_DATE))),
    (19, '00045', '014', 'AB12345', DATEADD('HOUR', 11, DATEADD('DAY', -10, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 8, DATEADD('DAY', -9, CURRENT_DATE))),
    (20, '00123', '014', 'GH13579', DATEADD('HOUR', 11, DATEADD('DAY', -4, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 8, DATEADD('DAY', -3, CURRENT_DATE))),
    (21, '00045', '014', 'EF24680', DATEADD('HOUR', 9, DATEADD('DAY', -341, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 8, DATEADD('DAY', -340, CURRENT_DATE))),
    (22, '00123', '014', 'EF24680', DATEADD('HOUR', 9, DATEADD('DAY', -246, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 8, DATEADD('DAY', -245, CURRENT_DATE))),
    (23, '00045', '014', 'AB12345', DATEADD('HOUR', 9, DATEADD('DAY', -601, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 8, DATEADD('DAY', -600, CURRENT_DATE))),
    (24, '00123', '014', 'AB12345', DATEADD('HOUR', 9, DATEADD('DAY', -651, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 8, DATEADD('DAY', -650, CURRENT_DATE))),
    (25, '00045', '014', 'GH13579', DATEADD('HOUR', 10, DATEADD('DAY', -341, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 9, DATEADD('DAY', -340, CURRENT_DATE))),
    (26, '00123', '014', 'GH13579', DATEADD('HOUR', 10, DATEADD('DAY', -341, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 9, DATEADD('DAY', -340, CURRENT_DATE))),
    (27, '00045', '014', 'AB12345', DATEADD('HOUR', 9, DATEADD('DAY', -161, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 8, DATEADD('DAY', -160, CURRENT_DATE))),
    (28, '00123', '014', 'EF24680', DATEADD('HOUR', 9, DATEADD('DAY', -161, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 8, DATEADD('DAY', -160, CURRENT_DATE))),
    (29, '00045', '014', 'AB12345', DATEADD('HOUR', 10, DATEADD('DAY', -101, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 9, DATEADD('DAY', -100, CURRENT_DATE))),
    (30, '00123', '014', 'EF24680', DATEADD('HOUR', 10, DATEADD('DAY', -101, CURRENT_DATE)), 'RECEIVED', 'CD78910', DATEADD('HOUR', 9, DATEADD('DAY', -100, CURRENT_DATE))),
    (31, '00045', '014', 'GH13579', DATEADD('HOUR', 10, DATEADD('DAY', -2, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 9, DATEADD('DAY', -1, CURRENT_DATE))),
    (32, '00123', '014', 'GH13579', DATEADD('HOUR', 10, DATEADD('DAY', -2, CURRENT_DATE)), 'RECEIVED', 'IJ11223', DATEADD('HOUR', 9, DATEADD('DAY', -1, CURRENT_DATE))),

    /* CANCELLED orders */
    (33, '00045', '014', 'AB12345',
     DATEADD('HOUR', 10, DATEADD('DAY', -3, CURRENT_DATE)),
     'CANCELLED', 'AB12345',
     DATEADD('HOUR', 12, DATEADD('DAY', -3, CURRENT_DATE))),

    (34, '00123', '014', 'EF24680',
     DATEADD('HOUR', 14, DATEADD('DAY', -2, CURRENT_DATE)),
     'CANCELLED', 'EF24680',
     DATEADD('HOUR', 16, DATEADD('DAY', -2, CURRENT_DATE)));


/* ============================================================
   ORDER MOVEMENT TRANSACTIONS
   qod_before_transaction reflects qod before the order action.
   For PLACED/CANCELLED orders this is left NULL.
   ============================================================ */

INSERT INTO ORDER_MOVEMENT_TRANSACTIONS
(general_order_id, upc_number, quantity, qod_before_transaction)
VALUES
    /* PLACED orders */
    (1, '3011', 60, NULL),
    (1, '3022', 100, NULL),
    (2, '4022', 45, NULL),
    (2, '5005', 30, NULL),

    /* RECEIVED orders */
    (3, '4011', 24, 90),
    (4, '4011', 30, 75),
    (5, '5001', 18, 10),
    (6, '5002', 16, 15),
    (7, '1011', 14, 16),
    (8, '1011', 18, 12),
    (9, '5003', 8, 9),
    (10, '5023', 9, 5),
    (11, '3011', 20, 12),
    (12, '3011', 26, 14),
    (13, '3022', 40, 28),
    (14, '3022', 35, 20),
    (15, '5004', 22, 14),
    (16, '5005', 28, 18),
    (17, '5006', 35, 25),
    (18, '5007', 32, 28),
    (19, '5022', 18, 18),
    (20, '5020', 24, 16),
    (21, '2044', 50, 30),
    (22, '2033', 45, 20),
    (23, '5018', 12, 11),
    (24, '5008', 10, 10),
    (25, '5012', 60, 50),
    (26, '5013', 55, 38),
    (27, '5014', 5, 8),
    (28, '5019', 7, 26),
    (29, '1022', 20, 24),
    (30, '4022', 25, 30),
    (31, '2011', 6, 0),
    (32, '5015', 10, 0),

    /* CANCELLED orders */
    (33, '5006', 12, NULL),
    (33, '5007', 18, NULL),
    (34, '5021', 40, NULL),
    (34, '5004', 15, NULL);


/* ============================================================
   PRODUCT INVENTORY INFO
   Only RECEIVED orders have inventory rows.
   Rows 1-26 generate alerts and are later set inactive.
   Rows 27-28 are not alert eligible yet.
   Rows 29-30 cannot generate alerts because can_be_marked_down = FALSE.
   ============================================================ */

INSERT INTO PRODUCT_INVENTORY_INFO
(product_order_id, general_order_id, upc_number, quantity,
 expiration_date, order_date, is_active)
VALUES
    (1, 3, '4011', 24, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -4, CURRENT_DATE), TRUE),
    (2, 4, '4011', 30, CURRENT_DATE, DATEADD('DAY', -5, CURRENT_DATE), TRUE),
    (3, 5, '5001', 18, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -4, CURRENT_DATE), TRUE),
    (4, 6, '5002', 16, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -4, CURRENT_DATE), TRUE),
    (5, 7, '1011', 14, DATEADD('DAY', 2, CURRENT_DATE), DATEADD('DAY', -3, CURRENT_DATE), TRUE),
    (6, 8, '1011', 18, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -4, CURRENT_DATE), TRUE),
    (7, 9, '5003', 8, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -3, CURRENT_DATE), TRUE),
    (8, 10, '5023', 9, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -3, CURRENT_DATE), TRUE),
    (9, 11, '3011', 20, DATEADD('DAY', 5, CURRENT_DATE), DATEADD('DAY', -16, CURRENT_DATE), TRUE),
    (10, 12, '3011', 26, DATEADD('DAY', 6, CURRENT_DATE), DATEADD('DAY', -15, CURRENT_DATE), TRUE),
    (11, 13, '3022', 40, DATEADD('DAY', 2, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE), TRUE),
    (12, 14, '3022', 35, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -6, CURRENT_DATE), TRUE),
    (13, 15, '5004', 22, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -4, CURRENT_DATE), TRUE),
    (14, 16, '5005', 28, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -3, CURRENT_DATE), TRUE),
    (15, 17, '5006', 35, DATEADD('DAY', 2, CURRENT_DATE), DATEADD('DAY', -12, CURRENT_DATE), TRUE),
    (16, 18, '5007', 32, DATEADD('DAY', 4, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE), TRUE),
    (17, 19, '5022', 18, DATEADD('DAY', 5, CURRENT_DATE), DATEADD('DAY', -9, CURRENT_DATE), TRUE),
    (18, 20, '5020', 24, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -3, CURRENT_DATE), TRUE),
    (19, 21, '2044', 50, DATEADD('DAY', 25, CURRENT_DATE), DATEADD('DAY', -340, CURRENT_DATE), TRUE),
    (20, 22, '2033', 45, DATEADD('DAY', 25, CURRENT_DATE), DATEADD('DAY', -245, CURRENT_DATE), TRUE),
    (21, 23, '5018', 12, DATEADD('DAY', 130, CURRENT_DATE), DATEADD('DAY', -600, CURRENT_DATE), TRUE),
    (22, 24, '5008', 10, DATEADD('DAY', 80, CURRENT_DATE), DATEADD('DAY', -650, CURRENT_DATE), TRUE),
    (23, 25, '5012', 60, DATEADD('DAY', 25, CURRENT_DATE), DATEADD('DAY', -340, CURRENT_DATE), TRUE),
    (24, 26, '5013', 55, DATEADD('DAY', 25, CURRENT_DATE), DATEADD('DAY', -340, CURRENT_DATE), TRUE),
    (25, 27, '5014', 5, DATEADD('DAY', 20, CURRENT_DATE), DATEADD('DAY', -160, CURRENT_DATE), TRUE),
    (26, 28, '5019', 7, DATEADD('DAY', 20, CURRENT_DATE), DATEADD('DAY', -160, CURRENT_DATE), TRUE),

    /* Not alert eligible yet */
    (27, 29, '1022', 20, DATEADD('DAY', 80, CURRENT_DATE), DATEADD('DAY', -100, CURRENT_DATE), TRUE),
    (28, 30, '4022', 25, DATEADD('DAY', 80, CURRENT_DATE), DATEADD('DAY', -100, CURRENT_DATE), TRUE),

    /* Cannot be marked down */
    (29, 31, '2011', 6, NULL, DATEADD('DAY', -1, CURRENT_DATE), FALSE),
    (30, 32, '5015', 10, NULL, DATEADD('DAY', -1, CURRENT_DATE), FALSE);


/* ============================================================
   BOH UPDATE FROM RECEIVED ORDERS
   Adds received quantities to existing starting BOH.
   ============================================================ */

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number + COALESCE((
                                           SELECT SUM(omt.quantity)
                                           FROM ORDER_MOVEMENT_TRANSACTIONS omt
                                                    JOIN ORDER_TRANSACTION_INFO oti
                                                         ON oti.general_order_id = omt.general_order_id
                                           WHERE oti.order_status = 'RECEIVED'
                                             AND oti.store_number = PRODUCT_BOH_INFO.store_number
                                             AND oti.division_number = PRODUCT_BOH_INFO.division_number
                                             AND omt.upc_number = PRODUCT_BOH_INFO.upc_number
                                           ), 0);


/* ============================================================
   PDM ALERTS
   Active alerts remain visible/actionable.
   Actioned alerts have matching MD/RFI transactions below.
   ============================================================ */

INSERT INTO PDM_ALERTS
(alert_id, store_number, division_number, department_number, upc_number,
 quantity, expiration_date, markdown_after_date, rfi_after_date,
 first_markdown_percent, is_active,
 alert_actioned_time, alert_actioned_user_euid, alert_actioned_code)
VALUES
    /* Active alerts - store 00045 */
    (1, '00045', '014', '07', '4011', 24, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, 25, TRUE, NULL, NULL, NULL),
    (7, '00045', '014', '09', '5003', 8, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, 35, TRUE, NULL, NULL, NULL),
    (9, '00045', '014', '10', '3011', 20, DATEADD('DAY', 5, CURRENT_DATE), DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', 3, CURRENT_DATE), 25, TRUE, NULL, NULL, NULL),
    (19, '00045', '014', '01', '2044', 50, DATEADD('DAY', 25, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE), DATEADD('DAY', 18, CURRENT_DATE), 15, TRUE, NULL, NULL, NULL),
    (25, '00045', '014', '00', '5014', 5, DATEADD('DAY', 20, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE), DATEADD('DAY', 13, CURRENT_DATE), 10, TRUE, NULL, NULL, NULL),

    /* Active alerts - store 00123 */
    (2, '00123', '014', '07', '4011', 30, CURRENT_DATE, DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), 25, TRUE, NULL, NULL, NULL),
    (12, '00123', '014', '10', '3022', 35, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -2, CURRENT_DATE), CURRENT_DATE, 25, TRUE, NULL, NULL, NULL),
    (16, '00123', '014', '01', '5007', 32, DATEADD('DAY', 4, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), DATEADD('DAY', 2, CURRENT_DATE), 20, TRUE, NULL, NULL, NULL),
    (18, '00123', '014', '07', '5020', 24, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, 20, TRUE, NULL, NULL, NULL),
    (20, '00123', '014', '01', '2033', 45, DATEADD('DAY', 25, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE), DATEADD('DAY', 18, CURRENT_DATE), 15, TRUE, NULL, NULL, NULL),
    (24, '00123', '014', '30', '5013', 55, DATEADD('DAY', 25, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE), DATEADD('DAY', 18, CURRENT_DATE), 10, TRUE, NULL, NULL, NULL),
    (26, '00123', '014', '01', '5019', 7, DATEADD('DAY', 20, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE), DATEADD('DAY', 13, CURRENT_DATE), 15, TRUE, NULL, NULL, NULL),

    /* Actioned MD alerts */
    (3, '00045', '014', '07', '5001', 18, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, 30, FALSE, DATEADD('HOUR', -8, CURRENT_TIMESTAMP), 'AB12345', 'MD'),
    (4, '00123', '014', '07', '5002', 16, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, 30, FALSE, DATEADD('HOUR', -7, CURRENT_TIMESTAMP), 'CD78910', 'MD'),
    (10, '00123', '014', '10', '3011', 26, DATEADD('DAY', 6, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), DATEADD('DAY', 4, CURRENT_DATE), 25, FALSE, DATEADD('HOUR', -6, CURRENT_TIMESTAMP), 'EF24680', 'MD'),
    (11, '00045', '014', '10', '3022', 40, DATEADD('DAY', 2, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), DATEADD('DAY', 1, CURRENT_DATE), 25, FALSE, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 'EF24680', 'MD'),
    (14, '00123', '014', '10', '5005', 28, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, 20, FALSE, DATEADD('HOUR', -4, CURRENT_TIMESTAMP), 'GH13579', 'MD'),
    (15, '00045', '014', '01', '5006', 35, DATEADD('DAY', 2, CURRENT_DATE), DATEADD('DAY', -3, CURRENT_DATE), CURRENT_DATE, 20, FALSE, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), 'AB12345', 'MD'),
    (17, '00045', '014', '01', '5022', 18, DATEADD('DAY', 5, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', 3, CURRENT_DATE), 20, FALSE, DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 'IJ11223', 'MD'),
    (21, '00045', '014', '06', '5018', 12, DATEADD('DAY', 130, CURRENT_DATE), DATEADD('DAY', -50, CURRENT_DATE), DATEADD('DAY', 100, CURRENT_DATE), 10, FALSE, DATEADD('HOUR', -1, CURRENT_TIMESTAMP), 'CD78910', 'MD'),

    /* Actioned RFI alerts */
    (5, '00045', '014', '09', '1011', 14, DATEADD('DAY', 2, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', 1, CURRENT_DATE), 30, FALSE, DATEADD('HOUR', -8, CURRENT_TIMESTAMP), 'GH13579', 'RFI'),
    (6, '00123', '014', '09', '1011', 18, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, 30, FALSE, DATEADD('HOUR', -7, CURRENT_TIMESTAMP), 'EF24680', 'RFI'),
    (8, '00123', '014', '09', '5023', 9, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, 35, FALSE, DATEADD('HOUR', -6, CURRENT_TIMESTAMP), 'AB12345', 'RFI'),
    (13, '00045', '014', '10', '5004', 22, DATEADD('DAY', 1, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), CURRENT_DATE, 25, FALSE, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 'GH13579', 'RFI'),
    (22, '00123', '014', '03', '5008', 10, DATEADD('DAY', 80, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE), DATEADD('DAY', 50, CURRENT_DATE), 10, FALSE, DATEADD('HOUR', -4, CURRENT_TIMESTAMP), 'IJ11223', 'RFI'),
    (23, '00045', '014', '30', '5012', 60, DATEADD('DAY', 25, CURRENT_DATE), DATEADD('DAY', -5, CURRENT_DATE), DATEADD('DAY', 18, CURRENT_DATE), 10, FALSE, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), 'IJ11223', 'RFI');


/* Once alerts are generated, related inventory rows become inactive. */

UPDATE PRODUCT_INVENTORY_INFO
SET is_active = FALSE
WHERE product_order_id BETWEEN 1 AND 26;


/* ============================================================
   MARKDOWN TRANSACTIONS
   Markdown moves quantity from QOD to QOM.
   qod_before_transaction includes starting BOH plus received quantity.
   ============================================================ */

INSERT INTO MD_TRANSACTIONS
(transaction_id, user_euid, store_number, division_number, upc_number,
 qod_before_transaction, qom_before_transaction, action_time,
 quantity_marked_down, original_price, new_price)
VALUES
    (1, 'AB12345', '00045', '014', '5001', 28, 0, DATEADD('HOUR', -8, CURRENT_TIMESTAMP), 18, 3.99, 2.79),
    (2, 'CD78910', '00123', '014', '5002', 31, 0, DATEADD('HOUR', -7, CURRENT_TIMESTAMP), 16, 4.49, 3.14),
    (3, 'EF24680', '00123', '014', '3011', 40, 0, DATEADD('HOUR', -6, CURRENT_TIMESTAMP), 26, 4.79, 3.59),
    (4, 'EF24680', '00045', '014', '3022', 68, 0, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 40, 3.79, 2.84),
    (5, 'GH13579', '00123', '014', '5005', 46, 0, DATEADD('HOUR', -4, CURRENT_TIMESTAMP), 28, 2.49, 1.99),
    (6, 'AB12345', '00045', '014', '5006', 60, 0, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), 35, 2.99, 2.39),
    (7, 'IJ11223', '00045', '014', '5022', 36, 0, DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 18, 3.99, 3.19),
    (8, 'CD78910', '00045', '014', '5018', 23, 0, DATEADD('HOUR', -1, CURRENT_TIMESTAMP), 12, 8.99, 8.09);

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 18,
    qom_number = qom_number + 18
WHERE store_number = '00045'
  AND division_number = '014'
  AND upc_number = '5001';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 16,
    qom_number = qom_number + 16
WHERE store_number = '00123'
  AND division_number = '014'
  AND upc_number = '5002';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 26,
    qom_number = qom_number + 26
WHERE store_number = '00123'
  AND division_number = '014'
  AND upc_number = '3011';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 40,
    qom_number = qom_number + 40
WHERE store_number = '00045'
  AND division_number = '014'
  AND upc_number = '3022';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 28,
    qom_number = qom_number + 28
WHERE store_number = '00123'
  AND division_number = '014'
  AND upc_number = '5005';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 35,
    qom_number = qom_number + 35
WHERE store_number = '00045'
  AND division_number = '014'
  AND upc_number = '5006';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 18,
    qom_number = qom_number + 18
WHERE store_number = '00045'
  AND division_number = '014'
  AND upc_number = '5022';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 12,
    qom_number = qom_number + 12
WHERE store_number = '00045'
  AND division_number = '014'
  AND upc_number = '5018';


/* ============================================================
   RFI TRANSACTIONS
   RFI removes quantity from QOD.
   ============================================================ */

INSERT INTO RFI_TRANSACTIONS
(transaction_id, user_euid, store_number, division_number, upc_number,
 qod_before_transaction, qom_before_transaction, action_time,
 quantity_removed, reason_code)
VALUES
    (1, 'GH13579', '00045', '014', '1011', 30, 0, DATEADD('HOUR', -8, CURRENT_TIMESTAMP), 14, 'OD'),
    (2, 'EF24680', '00123', '014', '1011', 30, 0, DATEADD('HOUR', -7, CURRENT_TIMESTAMP), 18, 'OD'),
    (3, 'AB12345', '00123', '014', '5023', 14, 0, DATEADD('HOUR', -6, CURRENT_TIMESTAMP), 9, 'OD'),
    (4, 'GH13579', '00045', '014', '5004', 36, 0, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 22, 'OD'),
    (5, 'IJ11223', '00123', '014', '5008', 20, 0, DATEADD('HOUR', -4, CURRENT_TIMESTAMP), 10, 'DMG'),
    (6, 'IJ11223', '00045', '014', '5012', 110, 0, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), 60, 'DMG');

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 14
WHERE store_number = '00045'
  AND division_number = '014'
  AND upc_number = '1011';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 18
WHERE store_number = '00123'
  AND division_number = '014'
  AND upc_number = '1011';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 9
WHERE store_number = '00123'
  AND division_number = '014'
  AND upc_number = '5023';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 22
WHERE store_number = '00045'
  AND division_number = '014'
  AND upc_number = '5004';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 10
WHERE store_number = '00123'
  AND division_number = '014'
  AND upc_number = '5008';

UPDATE PRODUCT_BOH_INFO
SET qod_number = qod_number - 60
WHERE store_number = '00045'
  AND division_number = '014'
  AND upc_number = '5012';


/* ============================================================
   RESET H2 AUTO-INCREMENT SEQUENCES
   Prevents duplicate key errors when the app inserts new rows.
   ============================================================ */

ALTER TABLE ORDER_TRANSACTION_INFO
    ALTER COLUMN general_order_id RESTART WITH 35;

ALTER TABLE PRODUCT_INVENTORY_INFO
    ALTER COLUMN product_order_id RESTART WITH 31;

ALTER TABLE PDM_ALERTS
    ALTER COLUMN alert_id RESTART WITH 27;

ALTER TABLE MD_TRANSACTIONS
    ALTER COLUMN transaction_id RESTART WITH 9;

ALTER TABLE RFI_TRANSACTIONS
    ALTER COLUMN transaction_id RESTART WITH 7;