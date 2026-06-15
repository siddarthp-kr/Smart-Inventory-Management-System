package mocksims.project.backend.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class AddItemRepositoryImpl implements AddItemRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AddItemRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String INSERT_MARKDOWN_RULES = """
                INSERT INTO MARKDOWN_RULES
                (subcommodity_number, first_markdown_percent, can_be_marked_down,
                 days_before_exp_to_markdown_number, days_before_exp_to_rfi_number,
                 days_after_order_to_set_exp)
                VALUES (:subcommodity, :percent, :canMarkdown, :daysMD, :daysRFI, :daysExp)
            """;

    private static final String INSERT_PRODUCT_BASIC = """
        INSERT INTO PRODUCT_BASIC_INFO
        (upc_number, subcommodity_number, department_number, product_name, standard_price)
        VALUES (:upc, :subcommodity, :dept, :name, :price)
    """;

    private static final String INSERT_PRODUCT_BOH = """
        INSERT INTO PRODUCT_BOH_INFO
        (division_number, store_number, upc_number, qod_number, qom_number)
        VALUES (:division, :store, :upc, :qod, :qom)
    """;

    @Override
    public void insertMarkdownRules (String subcommodityNumber, int firstMarkdownPercent, boolean canBeMarkedDown, int daysBeforeExpToMD, int daysBeforeExpToRFI, int daysAfterOrderToSetExp){

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("subcommodity", subcommodityNumber)
                .addValue("percent", firstMarkdownPercent)
                .addValue("canMarkdown", canBeMarkedDown)
                .addValue("daysMD", daysBeforeExpToMD)
                .addValue("daysRFI", daysBeforeExpToRFI)
                .addValue("daysExp", daysAfterOrderToSetExp);

        try {
            namedParameterJdbcTemplate.update(INSERT_MARKDOWN_RULES, params);
        } catch (DataAccessException error) {
            System.out.println("Error inserting MARKDOWN_RULES: " + error.getMessage());
            throw error;
        }
    }

    @Override
    public void insertProductBasicInfo(String upcNumber, String subcommodityNumber, String departmentNumber, String productName, double standardPrice){
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("upc", upcNumber)
                .addValue("subcommodity", subcommodityNumber)
                .addValue("dept", departmentNumber)
                .addValue("name", productName)
                .addValue("price", standardPrice);

        try {
            namedParameterJdbcTemplate.update(INSERT_PRODUCT_BASIC, params);
        } catch (DataAccessException error) {
            System.out.println("Error inserting PRODUCT_BASIC_INFO: " + error.getMessage());
            throw error;
        }
    }

    @Override
    public void insertProductBohInfo(String divisionNumber, String storeNumber, String upcNumber){
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("division", divisionNumber)
                .addValue("store", storeNumber)
                .addValue("upc", upcNumber)
                .addValue("qod", 0)
                .addValue("qom", 0);

        try {
            namedParameterJdbcTemplate.update(INSERT_PRODUCT_BOH, params);
        } catch (DataAccessException error) {
            System.out.println("Error inserting PRODUCT_BOH_INFO: " + error.getMessage());
            throw error;
        }

    }
}
