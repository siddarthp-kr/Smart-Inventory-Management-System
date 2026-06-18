package mocksims.project.backend.repository;

import org.springframework.data.relational.core.sql.In;
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

    public AddItemRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    // Check if mark-down rule already exists for the subcommodity
    private static final String CHECK_MARKDOWN_RULE_EXISTS = """
        SELECT COUNT(*) FROM MARKDOWN_RULES
        WHERE subcommodity_number = :subcommodity
    """;

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
     * Checks if the mark-down rule already exists for the subcommodity
     * @param subcommodityNumber set as an identifier for an item
     * @return true if mark-down rule exists for the subcmmodity or false
     */

    @Override
    public boolean markdownRuleExists(String subcommodityNumber) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("subcommodity", subcommodityNumber);

        Integer count = namedParameterJdbcTemplate.queryForObject(CHECK_MARKDOWN_RULE_EXISTS, params, Integer.class);
        return count != null && count > 0;
    }

    /**
     * Insert new row for mark-down rules
     * @param subcommodityNumber identifier for an item
     * @param firstMarkdownPercent first mark-down percent that is applied
     * @param canBeMarkedDown whether an item can be marked down or not
     * @param daysBeforeExpToMD number of days before expiration to mark-down
     * @param daysBeforeExpToRFI number of days before expiration to rfi
     * @param daysAfterOrderToSetExp number opf days after the order to set the expiration
     */
    @Override
    public void insertMarkdownRules (String subcommodityNumber, Integer firstMarkdownPercent, Boolean canBeMarkedDown, Integer daysBeforeExpToMD, Integer daysBeforeExpToRFI, Integer daysAfterOrderToSetExp){

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
     * @param upcNumber upc number for each product
     * @param subcommodityNumber links to the product
     * @param departmentNumber department identity for product
     * @param productName name of product
     * @param standardPrice retail price of product
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
     * QOD Default value of 0
     * QOM Default value of 0
     * @param divisionNumber hardcoded division number
     * @param storeNumber hardcoded store number
     * @param upcNumber upc number for each product
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
