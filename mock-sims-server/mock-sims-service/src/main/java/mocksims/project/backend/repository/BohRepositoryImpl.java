package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.BohItem;
import mocksims.project.backend.domain.mapper.BohMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Repository
public class BohRepositoryImpl implements BohRepository{
    private final static Logger LOG = LoggerFactory.getLogger(BohRepositoryImpl.class);
    private final BohMapper bohMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public BohRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, BohMapper bohMapper){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.bohMapper = bohMapper;
    }

    /**
     *  SQL query to retrieve BOH info for all products of specific store and division
     */
    private static final String GET_BOH_INFO = """
                SELECT
                b.upc_number,
                b.qod_number,
                b.qom_number,
                p.department_number,
                d.department_name,
                p.product_name
            FROM PRODUCT_BOH_INFO b
            INNER JOIN PRODUCT_BASIC_INFO p
                ON b.upc_number = p.upc_number
            INNER JOIN DEPARTMENT_INFO d
                ON p.department_number = d.department_number
            WHERE b.store_number = :storeNumber
              AND b.division_number = :divisionNumber
            ORDER BY d.department_name, p.product_name
            """;

    /**
     * Retrieve  all BOH info
     * @param storeNumber used to filter BOH results
     * @param divisionNumber used to filer BOH results
     * @return list of BOH objects details
     */
    @Override
    public List<BohItem> getBohInfo(String storeNumber, String divisionNumber){
        LOG.info("Retrieving BOH information for store {} and division {}", storeNumber, divisionNumber);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storeNumber", storeNumber)
                .addValue("divisionNumber", divisionNumber);

        return namedParameterJdbcTemplate.query(
                GET_BOH_INFO,
                params,
                bohMapper
        );
    }
}
