package mocksims.project.backend.domain.mapper;

import mocksims.project.backend.api.domain.BohItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BohMapper implements RowMapper<BohItem>{
    private static final String UPC_NUMBER = "upc_number";
    private static final String QOD_NUMBER = "qod_number";
    private static final String QOM_NUMBER = "qom_number";
    private static final String DEPARTMENT_NUMBER = "department_number";
    private static final String DEPARTMENT_NAME = "department_name";
    private static final String PRODUCT_NAME = "product_name";


    @Override
    public BohItem mapRow(ResultSet rs, int index) throws SQLException {
        BohItem bohItem = new BohItem();

        bohItem.setUpcNumber(rs.getString(UPC_NUMBER));
        bohItem.setQodNumber(rs.getInt(QOD_NUMBER));
        bohItem.setQomNumber(rs.getInt(QOM_NUMBER));
        bohItem.setDepartmentNumber(rs.getString(DEPARTMENT_NUMBER));
        bohItem.setDepartmentName(rs.getString(DEPARTMENT_NAME));
        bohItem.setProductName(rs.getString(PRODUCT_NAME));

        return bohItem;
    }
}
