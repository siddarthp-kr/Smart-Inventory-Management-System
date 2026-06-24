package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.PdmAlertInfoRecord;
import mocksims.project.backend.domain.mapper.PdmAlertsInfoMapper;
import mocksims.project.backend.exception.MockSimsCustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Repository
public class PdmAlertGenerationRepositoryImpl implements PdmAlertGenerationRepository{

    private static final Logger LOG = LoggerFactory.getLogger(PdmAlertGenerationRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public PdmAlertGenerationRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String DIVISION_NUMBER = "DIVISION_NUMBER";
    private static final String STORE_NUMBER = "STORE_NUMBER";
    private static final String UPC_NUMBER = "UPC_NUMBER";

    private static final String PRODUCT_ORDER_ID = "PRODUCT_ORDER_ID";

    private static final String DEPARTMENT_NUMBER = "DEPARTMENT_NUMBER";
    private static final String QUANTITY = "QUANTITY";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String MARKDOWN_AFTER_DATE = "MARKDOWN_AFTER_DATE";
    private static final String RFI_AFTER_DATE = "RFI_AFTER_DATE";
    private static final String FIRST_MARKDOWN_PERCENT = "FIRST_MARKDOWN_PERCENT";
    private static final String IS_ACTIVE = "IS_ACTIVE";

    private static final String SQL_INSERT_ALERTS = """
            INSERT INTO PDM_ALERTS (store_number, division_number, department_number, upc_number, quantity, expiration_date, markdown_after_date, rfi_after_date, first_markdown_percent, is_active)
            VALUES (:STORE_NUMBER, :DIVISION_NUMBER, :DEPARTMENT_NUMBER, :UPC_NUMBER, :QUANTITY, :EXPIRATION_DATE, :MARKDOWN_AFTER_DATE, :RFI_AFTER_DATE, :FIRST_MARKDOWN_PERCENT, :IS_ACTIVE);
            """;


    private static final String SQL_GET_QUANTITY_INFO = "" +
            "SELECT\n" +
            "    pboh.qod_number,\n" +
            "    pboh.qom_number,\n" +
            "    COALESCE(SUM(pii.quantity), 0) AS total_active_quantity\n" +
            "FROM PRODUCT_BOH_INFO pboh\n" +
            "JOIN PRODUCT_INVENTORY_INFO pii\n" +
            "    ON pii.upc_number = pboh.upc_number\n" +
            "JOIN ORDER_TRANSACTION_INFO oti\n" +
            "    ON pii.general_order_id = oti.general_order_id\n" +
            "   AND oti.store_number = pboh.store_number\n" +
            "   AND oti.division_number = pboh.division_number\n" +
            "WHERE pboh.upc_number = :UPC_NUMBER\n" +
            "  AND pboh.store_number = :STORE_NUMBER\n" +
            "  AND pboh.division_number = :DIVISION_NUMBER\n" +
            "  AND pii.is_active = TRUE\n" +
            "GROUP BY pboh.qod_number, pboh.qom_number;";

    private static final String SQL_GET_BOH = "SELECT (qod_number + qom_number) " +
            "FROM PRODUCT_BOH_INFO " +
            "WHERE store_number = :STORE_NUMBER AND division_number = :DIVISION_NUMBER AND upc_number = :UPC_NUMBER;";
    private static final String SQL_GET_TOTAL_QUANTITY = """
            SELECT
                COALESCE(SUM(pii.quantity), 0) AS total_active_quantity
            FROM PRODUCT_INVENTORY_INFO pii
            JOIN ORDER_TRANSACTION_INFO oti
                ON pii.general_order_id = oti.general_order_id
            WHERE pii.upc_number = :UPC_NUMBER
              AND oti.store_number = :STORE_NUMBER
              AND oti.division_number = :DIVISION_NUMBER
              AND pii.is_active = TRUE;
            """;

    private static final String SQL_SET_INVENTORY_INACTIVE = """
            UPDATE PRODUCT_INVENTORY_INFO
            SET is_active = FALSE
            WHERE product_order_id = :PRODUCT_ORDER_ID;
            """;

    private static final String SQL_GET_ALERTS_INFO = "" +
            "SELECT\n" +
            "    pii.product_order_id,\n" +
            "    pii.upc_number,\n" +
            "    pii.expiration_date,\n" +
            "    pii.quantity,\n" +
            "    oti.store_number,\n" +
            "    oti.division_number,\n" +
            "    pbi.department_number,\n" +
            "    mr.first_markdown_percent,\n" +
            "    mr.days_before_exp_to_markdown_number,\n" +
            "    mr.days_before_exp_to_rfi_number,\n" +
            "    pboh.qod_number,\n" +
            "    pboh.qom_number\n" +
            "FROM PRODUCT_INVENTORY_INFO pii\n" +
            "JOIN ORDER_TRANSACTION_INFO oti\n" +
            "    ON pii.general_order_id = oti.general_order_id\n" +
            "JOIN PRODUCT_BASIC_INFO pbi\n" +
            "    ON pii.upc_number = pbi.upc_number\n" +
            "JOIN MARKDOWN_RULES mr\n" +
            "    ON pbi.subcommodity_number = mr.subcommodity_number\n" +
            "JOIN PRODUCT_BOH_INFO pboh\n" +
            "    ON pboh.division_number = oti.division_number\n" +
            "   AND pboh.store_number = oti.store_number\n" +
            "   AND pboh.upc_number = pii.upc_number\n" +
            "JOIN (\n" +
            "    SELECT\n" +
            "        oti2.store_number,\n" +
            "        oti2.division_number,\n" +
            "        pii2.upc_number,\n" +
            "        MIN(oti2.order_placed_time) AS earliest_order_placed_time\n" +
            "    FROM PRODUCT_INVENTORY_INFO pii2\n" +
            "    JOIN ORDER_TRANSACTION_INFO oti2\n" +
            "        ON pii2.general_order_id = oti2.general_order_id\n" +
            "    WHERE pii2.is_active = TRUE\n" +
            "    GROUP BY oti2.store_number, oti2.division_number, pii2.upc_number\n" +
            ") earliest\n" +
            "    ON earliest.store_number = oti.store_number\n" +
            "   AND earliest.division_number = oti.division_number\n" +
            "   AND earliest.upc_number = pii.upc_number\n" +
            "   AND earliest.earliest_order_placed_time = oti.order_placed_time\n" +
            "WHERE pii.is_active = TRUE;";

    @Override
    public List<PdmAlertInfoRecord> getPdmAlertsInfo(){
        //get all the information for each alert (before checking whether it is eligible)
        //only gets the most recent row for each upc
        List<PdmAlertInfoRecord> alertsInfo;
        try {
            alertsInfo = jdbcTemplate.query(SQL_GET_ALERTS_INFO, new PdmAlertsInfoMapper());
        } catch (DataAccessException e){
            LOG.error("Failed to retrieve PDM Alerts Info", e);
            throw new MockSimsCustomException(500, "Failed to retrieve PDM Alerts Info");
        }

        return alertsInfo;
    }

    @Override
    public void insertNewAlerts(List<PdmAlertInfoRecord> alerts){
        //generate alerts and mark the corresponding inventory rows as inactive
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<MapSqlParameterSource> batchArgs = new ArrayList<>();
        List<Integer> orderIdsToSetInactive = new ArrayList<>();

        for(PdmAlertInfoRecord alert: alerts){
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                    .addValue(STORE_NUMBER, alert.getStoreNumber())
                    .addValue(DIVISION_NUMBER, alert.getDivisionNumber())
                    .addValue(DEPARTMENT_NUMBER, alert.getDepartmentNumber())
                    .addValue(UPC_NUMBER, alert.getUpcNumber())
                    .addValue(QUANTITY, alert.getQuantity())
                    .addValue(EXPIRATION_DATE, alert.getExpirationDate())
                    .addValue(MARKDOWN_AFTER_DATE, alert.getExpirationDate().minusDays(alert.getDaysBeforeExpToMD()))
                    .addValue(RFI_AFTER_DATE, alert.getExpirationDate().minusDays(alert.getDaysBeforeExpToRFI()))
                    .addValue(FIRST_MARKDOWN_PERCENT, alert.getFirstMarkdownPercent())
                    .addValue(IS_ACTIVE, true);

            batchArgs.add(mapSqlParameterSource);
            orderIdsToSetInactive.add(alert.getProductOrderId());
        }

        try {
            namedParameterJdbcTemplate.batchUpdate(SQL_INSERT_ALERTS , batchArgs.toArray(new MapSqlParameterSource[0]));
        } catch (DataAccessException e){
            LOG.error("Failed to insert new alerts into alerts table.", e);
            throw new MockSimsCustomException(500, "Failed to insert new alerts into alerts table.");
        }

        markInventoryAsInactive(orderIdsToSetInactive);

    }

    @Override
    public Integer getItemTotalBoh(PdmAlertInfoRecord alert){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, alert.getStoreNumber())
                .addValue(DIVISION_NUMBER, alert.getDivisionNumber())
                .addValue(UPC_NUMBER, alert.getUpcNumber());

        return namedParameterJdbcTemplate.queryForObject(SQL_GET_BOH, mapSqlParameterSource, Integer.class);
    }

    @Override
    public Integer getItemTotalQuantity(PdmAlertInfoRecord alert){
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue(STORE_NUMBER, alert.getStoreNumber())
                .addValue(DIVISION_NUMBER, alert.getDivisionNumber())
                .addValue(UPC_NUMBER, alert.getUpcNumber());

        return namedParameterJdbcTemplate.queryForObject(SQL_GET_TOTAL_QUANTITY, mapSqlParameterSource, Integer.class);
    }

    @Override
    public void markInventoryAsInactive(List<Integer> orderIds){
        List<MapSqlParameterSource> batchArgs = new ArrayList<>();
        int[] retVals;
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        for(Integer orderId: orderIds){
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(PRODUCT_ORDER_ID, orderId);
            batchArgs.add(mapSqlParameterSource);
        }

        try {
            retVals = namedParameterJdbcTemplate.batchUpdate(SQL_SET_INVENTORY_INACTIVE, batchArgs.toArray(new MapSqlParameterSource[0]));
        } catch (DataAccessException e){
            LOG.error("Failed to set order inventory as inactive", e);
            throw new MockSimsCustomException(500, e.getMessage());
        }

        if(IntStream.of(retVals).sum() != orderIds.size()){
            throw new MockSimsCustomException(500, "Failed to set inventory as inactive. Some rows were not set to inactive");
        }

    }

}
