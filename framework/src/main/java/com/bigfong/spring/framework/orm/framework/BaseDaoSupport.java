package com.bigfong.spring.framework.orm.framework;

import com.bigfong.spring.framework.orm.common.Page;
import com.bigfong.spring.framework.orm.common.jdbc.BaseDao;
import com.bigfong.spring.framework.orm.common.jdbc.RowMapper;
import com.bigfong.spring.framework.orm.template.JdbcTemplate;
import com.bigfong.spring.framework.utils.GenericsUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
//import org.springframework.jdbc.core.JdbcTemplate;

/**
 * BaseDao扩展类,主要对JdbcTemplate的包装
 * 主要功能是支持自动拼接SQL语句，必须继承方可合租
 *
 * @author bigfong
 * @since 2019/10/6
 */
public abstract class BaseDaoSupport<T extends Serializable, PK extends Serializable> implements BaseDao<T, PK> {
    private Logger logger = LoggerFactory.getLogger(BaseDaoSupport.class);

    private String tableName = "";
    private JdbcTemplate jdbcTemplateWrite;
    private JdbcTemplate jdbcTemplateReadOnly;

    private DataSource dataSourceReadOnly;
    private DataSource dataSourceWrite;

    private EntityOperation<T> op;

    public String getTableName() {
        return tableName;
    }

    public DataSource getDataSourceReadOnly() {
        return dataSourceReadOnly;
    }

    public DataSource getDataSourceWrite() {
        return dataSourceWrite;
    }

    /**
     * 动态切换表名
     *
     * @param tableName
     */
    public void setTableName(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            this.tableName = op.tableName;
        } else {
            this.tableName = tableName;
        }
        this.tableName = tableName;
    }

    public void setDataSouceReadOnly(DataSource dataSourceReadOnly) {
        this.dataSourceReadOnly = dataSourceReadOnly;
    }

    public void setDataSourceWrite(DataSource dataSourceWrite) {
        this.dataSourceWrite = dataSourceWrite;
    }

    public JdbcTemplate jdbcTemplateWrite() {
        return jdbcTemplateWrite;
    }

    public JdbcTemplate jdbcTemplateReadOnly() {
        return jdbcTemplateReadOnly;
    }


    @SuppressWarnings("unchecked")
    public BaseDaoSupport() {
        try {
            Class<T> entityClass = GenericsUtils.getSuperClassGenricType(getClass(), 0);
            op = new EntityOperation<T>(entityClass, this.getPkCloumn());
            this.setTableName(op.tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取主键，由子类重写
     * @return
     */
    protected abstract String getPkCloumn();

    protected abstract void setDataSource(DataSource dataSource);

    /**
     * 还原默认表名
     */
    protected void restoreTableName() {
        this.setTableName(op.tableName);
    }

    protected String removeFirsetAnd(String whereSql){
        if (whereSql!=null){
            whereSql = whereSql.trim();
            if ((whereSql.toLowerCase()).startsWith("and")){
                whereSql = whereSql.substring(3);
            }
        }
        return whereSql;
    }

    /**
     * 查询函数，使用查询规则
     *
     * @param queryRule 查询条件
     * @return 结果List
     * @throws Exception
     */
    public List<T> select(QueryRule queryRule) throws Exception {
        QueryRuleSqlBuilder builder = new QueryRuleSqlBuilder(queryRule);
        String ws = removeFirsetAnd(builder.getWhereSql());
        String whereSql = ("".equals(ws) ? ws : (" where " + ws));
        String sql = "select " + op.allColum + " from " + getTableName() + whereSql;
        Object[] values = builder.getValues();
        String orderSql = builder.getOrderSql();
        orderSql = (StringUtils.isEmpty(orderSql) ? " " : (" order by " + orderSql));
        sql += orderSql;
        logger.info(sql);
        return (List<T>) this.jdbcTemplateReadOnly.query(sql, this.op.rowMapper, values);
    }

    /**
     * 根据Sql语句执行查询，参数为Object数组对像
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> sellectBysql(String sql,Object... args) throws Exception{
        return this.jdbcTemplateReadOnly.queryForList(sql,args);
    }

    public Page<T> select(QueryRule queryRule,final int pageNo,final int pageSize) throws Exception{
        QueryRuleSqlBuilder builder = new QueryRuleSqlBuilder(queryRule);
        Object[] values = builder.getValues();
        String ws = removeFirsetAnd(builder.getWhereSql());
        String whereSql = ("".equals(ws) ? ws : (" where " + ws));
        String countSql = "select count("+getPkCloumn()+") as c from " + getTableName() + whereSql;
        long count = (Long)this.jdbcTemplateReadOnly().queryForMap(countSql,values).get("c");

        if (count==0){
            return new Page<T>();
        }
        long start = (pageNo-1)*pageSize;
        String orderSql = builder.getOrderSql();
        orderSql = (StringUtils.isEmpty(orderSql) ? " " : (" order by " + orderSql));

        String sql = "select " + op.allColum + " from " + getTableName() + whereSql + orderSql +" limit "+start+","+pageSize;
        List<T> list = (List<T>) this.jdbcTemplateReadOnly.query(sql,this.op.rowMapper,values);
        logger.debug(sql);
        return new Page<>(start,count,pageSize,list);
    }

    /**
     * 分页查询特殊SQL语句
     * @param sql
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    public Page<Map<String ,Object>> selectBySqlToPage(String sql,Object[] param,final int pageNo,final int pageSize) throws Exception{
        String countSql = "select count(1) from ("+sql+") a";
        long count = (Long) this.jdbcTemplateReadOnly().queryForMap(countSql,param).get("count(1)");

        if (count==0){
            return new Page<Map<String ,Object>>();
        }
        long start = (pageNo-1)*pageSize;

        sql = sql +" limit "+start+","+pageSize;
        List<Map<String ,Object>> list = (List<Map<String ,Object>>) this.jdbcTemplateReadOnly.queryForList(sql,param);
        logger.debug(sql);
        return new Page<>(start,count,pageSize,list);
    }

    private <T> T doLoad(Object pkValue, RowMapper<T> rowMapper){
        Object obj= this.doLoad(getTableName(),getPkCloumn(),pkValue,rowMapper);
        if (obj!=null){
            return (T)obj;
        }
        return null;
    }

    private <T> Object doLoad(String tableName, String pkCloumn, Object pkValue, RowMapper<T> rowMapper) {
        return null;
    }
}
