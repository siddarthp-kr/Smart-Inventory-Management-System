package mocksims.project.backend.domain.mapper;

import mocksims.project.backend.api.domain.DepartmentInfoRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GetDepartmentInfoMapper implements RowMapper<DepartmentInfoRecord> {

    private static final String DEPARTMENT_NAME = "department_name";
    private static final String DEPARTMENT_NUMBER = "department_number";

    @Override
    public DepartmentInfoRecord mapRow(ResultSet rs, int index) throws SQLException {
        DepartmentInfoRecord departmentInfoRecord = new DepartmentInfoRecord();

        departmentInfoRecord.setDepartmentNumber(rs.getString(DEPARTMENT_NUMBER));
        departmentInfoRecord.setDepartmentName(rs.getString(DEPARTMENT_NAME));

        return departmentInfoRecord;
    }
}
