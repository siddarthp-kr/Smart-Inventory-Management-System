package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.MovementInfoRecord;
import mocksims.project.backend.domain.mapper.MovementInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MovementInfoRepositoryImpl implements MovementInfoRepository{
    private static final Logger LOG = LoggerFactory.getLogger(MovementInfoRepositoryImpl.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final MovementInfoMapper movementInfoMapper;

    public MovementInfoRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MovementInfoMapper movementInfoMapper){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.movementInfoMapper = movementInfoMapper;
    }

    private static final String GET_MOVEMENT_INFO = """
            
            
            SELECT
                    md.upc_number AS upc_number,
                    prod.product_name AS product_name,
                    'MARKDOWN' AS movement_type,
                    md.transaction_id AS transaction_id,
                    md.user_euid AS user_euid,
                    md.qod_before_transaction AS qod_before_transaction,
                    md.qom_before_transaction AS qom_before_transaction,
                    md.action_time AS action_time,
                    md.quantity_marked_down AS quantity_changed,
                    CAST(NULL AS VARCHAR(10)) AS reason_code,
                    md.original_price AS original_price,
                    md.new_price AS new_price
                FROM MD_TRANSACTIONS md
                INNER JOIN PRODUCT_BASIC_INFO prod
                    ON md.upc_number = prod.upc_number
                WHERE md.store_number = :storeNumber
                  AND md.division_number = :divisionNumber
                  AND md.upc_number = :upcNumber
            
                UNION ALL
            
                SELECT
                    rfi.upc_number AS upc_number,
                    prod.product_name AS product_name,
                    'RFI' AS movement_type,
                    rfi.transaction_id AS transaction_id,
                    rfi.user_euid AS user_euid,
                    rfi.qod_before_transaction AS qod_before_transaction,
                    rfi.qom_before_transaction AS qom_before_transaction,
                    rfi.action_time AS action_time,
                    rfi.quantity_removed AS quantity_changed,
                    rfi.reason_code AS reason_code,
                    CAST(NULL AS DECIMAL(10,2)) AS original_price,
                    CAST(NULL AS DECIMAL(10,2)) AS new_price
                FROM RFI_TRANSACTIONS rfi
                INNER JOIN PRODUCT_BASIC_INFO prod
                    ON rfi.upc_number = prod.upc_number
                WHERE rfi.store_number = :storeNumber
                  AND rfi.division_number = :divisionNumber
                  AND rfi.upc_number = :upcNumber
            
                ORDER BY action_time DESC
                """;



    @Override
    public List<MovementInfoRecord> getMovementInfo(String storeNumber, String divisionNumber, String upcNumber) {
        LOG.info(
                "Retrieving movement info for store {}, division {}, upc {}",
                storeNumber,
                divisionNumber,
                upcNumber
        );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("storeNumber", storeNumber)
                .addValue("divisionNumber", divisionNumber)
                .addValue("upcNumber", upcNumber);

        return namedParameterJdbcTemplate.query(
                GET_MOVEMENT_INFO,
                params,
                movementInfoMapper
        );
    }


}
