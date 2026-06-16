package mocksims.project.backend.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class AddItemRepositoryImpl implements AddItemRepository {
    // Implement Logger for logging insert action
    private static final Logger LOG = LoggerFactory.getLogger(AddItemRepositoryImpl.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Injecting JDBC
     * @param namedParameterJdbcTemplate implemented to handle database insertion
     */
    public AddItemRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // Insert mark-down rules
    private static final String INSERT_MARKDOWN_RULES = """
                INSERT INTO MARKDOWN_RULES
                (subcommodity_number, first_markdown_percent, can_be_marked_down,
                 days_before_exp_to_markdown_number, days_before_exp_to_rfi_number,
                 days_after_order_to_set_exp)
                VALUES (:subcommodity, :percent, :canMarkdown, :daysMD, :daysRFI, :daysExp)
            """;

    // Insert product basic info
    private static final String INSERT_PRODUCT_BASIC = """
        INSERT INTO PRODUCT_BASIC_INFO
        (upc_number, subcommodity_number, department_number, product_name, standard_price)
        VALUES (:upc, :subcommodity, :dept, :name, :price)
    """;

    // Insert product BOH info
    private static final String INSERT_PRODUCT_BOH = """
        INSERT INTO PRODUCT_BOH_INFO
        (division_number, store_number, upc_number, qod_number, qom_number)
        VALUES (:division, :store, :upc, :qod, :qom)
    """;

    /**
     * Insert new row for mark-down rules
     * @param subcommodityNumber
     * @param firstMarkdownPercent
     * @param canBeMarkedDown
     * @param daysBeforeExpToMD
     * @param daysBeforeExpToRFI
     * @param daysAfterOrderToSetExp
     */
    @Override
    public void insertMarkdownRules (String subcommodityNumber, int firstMarkdownPercent, boolean canBeMarkedDown, int daysBeforeExpToMD, int daysBeforeExpToRFI, int daysAfterOrderToSetExp){

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("subcommodity", subcommodityNumber)
                .addValue("percent", firstMarkdownPercent)
                .addValue("canMarkdown", canBeMarkedDown)
                .addValue("daysMD", daysBeforeExpToMD)
                .addValue("daysRFI", daysBeforeExpToRFI)
                .addValue("daysExp", daysAfterOrderToSetExp);

        LOG.info("Inserting MARKDOWN_RULES for subcommodity {}", subcommodityNumber);
        namedParameterJdbcTemplate.update(INSERT_MARKDOWN_RULES, params);
    }

    /**
     * Insert new row for product basic info
     * @param upcNumber
     * @param subcommodityNumber
     * @param departmentNumber
     * @param productName
     * @param standardPrice
     */
    @Override
    public void insertProductBasicInfo(String upcNumber, String subcommodityNumber, String departmentNumber, String productName, double standardPrice){
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("upc", upcNumber)
                .addValue("subcommodity", subcommodityNumber)
                .addValue("dept", departmentNumber)
                .addValue("name", productName)
                .addValue("price", standardPrice);

        LOG.info("Inserting PRODUCT_BASIC_INFO for UPC {}", upcNumber);
        namedParameterJdbcTemplate.update(INSERT_PRODUCT_BASIC, params);
    }

    /**
     * Insert new row for product BOH info
     * @param divisionNumber
     * @param storeNumber
     * @param upcNumber
     */
    @Override
    public void insertProductBohInfo(String divisionNumber, String storeNumber, String upcNumber){
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("division", divisionNumber)
                .addValue("store", storeNumber)
                .addValue("upc", upcNumber)
                .addValue("qod", 0)
                .addValue("qom", 0);

        // Log what is happening
        LOG.info("Inserting PRODUCT_BOH_INFO for UPC {}", upcNumber);
        namedParameterJdbcTemplate.update(INSERT_PRODUCT_BOH, params);

    }
}
