package com.bigfong.spring.framework.orm.template;

import com.bigfong.spring.framework.orm.common.jdbc.RowMapper;
import com.bigfong.spring.framework.orm.framework.EntityOperation;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author bigfong
 * @since 2019/10/6
 */
public abstract class JdbcTemplate {
    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected void closeConnection(Connection conn) throws Exception {
        conn.close();
    }

    protected void closeStatement(PreparedStatement pstm) throws Exception {
        pstm.close();
    }

    protected void closeResultSet(ResultSet rs) throws Exception {
        rs.close();
    }

    protected Connection getConnect() throws Exception {
        return this.dataSource.getConnection();
    }

    protected List<?> parseResultSet(ResultSet rs, RowMapper<?> rowMapper) throws Exception {
        List<Object> result = new ArrayList<>();
        int rowNum = 1;
        while (rs.next()) {
            result.add(rowMapper.mapRow(rs, rowNum++));
        }
        return result;
    }

    protected List<?> executeQuery(String sql, RowMapper<?> rowMapper, Object[] values) {
        try {
            //获取连接
            Connection conn = this.getConnect();
            //创建语名集
            PreparedStatement pstm = this.createPrepareStatement(conn, sql);
            //执行语句集
            ResultSet rs = this.executeQuery(pstm, values);
            //处理结果集
            List<?> result = this.parseResultSet(rs, rowMapper);
            //关闭结果集
            this.closeResultSet(rs);
            //关半语句集
            this.closeStatement(pstm);
            //关闭连接
            this.closeConnection(conn);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected PreparedStatement createPrepareStatement(Connection conn, String sql) throws Exception{
        return conn.prepareStatement(sql);
    }

    private ResultSet executeQuery(PreparedStatement pstm, Object[] values) throws Exception {
        for (int i = 0; i < values.length; i++) {
            pstm.setObject(i, values[i]);
        }
        return pstm.executeQuery();
    }

    public <T extends Serializable> Object query(String sql, RowMapper<T> rowMapper, Object[] values) throws Exception{
        return this.executeQuery(sql,rowMapper,values);
    }

    public List<Map<String,Object>> queryForList(String sql, Object[] args) {
        return null;
    }

    public Map<String,Object> queryForMap(String countSql, Object[] values) {
        return null;
    }
}
