package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.DepartmentInfoRecord;
import mocksims.project.backend.domain.mapper.GetDepartmentInfoMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.List;

@Repository
public class GetDepartmentInfoRepositoryImpl implements GetDepartmentInfoRepository{

    private String SQL_GET_DEPARTMENT_INFO = "SELECT * FROM DEPARTMENT_INFO";

    private final JdbcTemplate jdbcTemplate;

    public GetDepartmentInfoRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DepartmentInfoRecord> getDepartmentInfo() throws DataAccessException {
        return jdbcTemplate.query(SQL_GET_DEPARTMENT_INFO, new GetDepartmentInfoMapper());
    }
}
