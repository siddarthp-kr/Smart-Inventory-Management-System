package mocksims.project.backend.domain.mapper;

import mocksims.project.backend.api.domain.MovementInfoRecord;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class MovementInfoMapper implements RowMapper<MovementInfoRecord> {

    private static final String UPC_NUMBER = "upc_number";
    private static final String PRODUCT_NAME = "product_name";
    private static final String MOVEMENT_TYPE = "movement_type";
    private static final String TRANSACTION_ID = "transaction_id";
    private static final String USER_EUID = "user_euid";
    private static final String QOD_BEFORE_TRANSACTION = "qod_before_transaction";
    private static final String QOM_BEFORE_TRANSACTION = "qom_before_transaction";
    private static final String ACTION_TIME = "action_time";
    private static final String QUANTITY_CHANGED = "quantity_changed";
    private static final String REASON_CODE = "reason_code";
    private static final String ORIGINAL_PRICE = "original_price";
    private static final String NEW_PRICE = "new_price";

    @Override
    public MovementInfoRecord mapRow(ResultSet rs, int index) throws SQLException {
        MovementInfoRecord movementInfoRecord = new MovementInfoRecord();

        movementInfoRecord.setUpcNumber(rs.getString(UPC_NUMBER));
        movementInfoRecord.setProductName(rs.getString(PRODUCT_NAME));
        movementInfoRecord.setMovementType(rs.getString(MOVEMENT_TYPE));
        movementInfoRecord.setTransactionId(rs.getInt(TRANSACTION_ID));
        movementInfoRecord.setUserEuid(rs.getString(USER_EUID));
        movementInfoRecord.setQodBeforeTransaction(rs.getInt(QOD_BEFORE_TRANSACTION));
        movementInfoRecord.setQomBeforeTransaction(rs.getInt(QOM_BEFORE_TRANSACTION));

        Timestamp actionTimestamp = rs.getTimestamp(ACTION_TIME);
        if (actionTimestamp != null) {
            movementInfoRecord.setActionTime(actionTimestamp.toLocalDateTime());
        }

        movementInfoRecord.setQuantityChanged(rs.getInt(QUANTITY_CHANGED));
        movementInfoRecord.setReasonCode(rs.getString(REASON_CODE));

        if (rs.getObject(ORIGINAL_PRICE) != null) {
            movementInfoRecord.setOriginalPrice(rs.getDouble(ORIGINAL_PRICE));
        }

        if (rs.getObject(NEW_PRICE) != null) {
            movementInfoRecord.setNewPrice(rs.getDouble(NEW_PRICE));
        }

        return movementInfoRecord;
    }
}
