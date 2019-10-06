package com.bigfong.spring.framework.orm.common.jdbc;

import com.bigfong.spring.framework.orm.common.Page;
import com.bigfong.spring.framework.orm.framework.QueryRule;

import java.util.List;
import java.util.Map;

/**
 * @author bigfong
 * @since 2019/10/6
 */
public interface BaseDao<T,PK> {
    /**
     * 获取列表
     * @param queryRule 查询条件
     * @return
     * @throws Exception
     */
    List<T> select(QueryRule queryRule) throws Exception;

    /**
     * 获取分页结果
     * @param queryRule 查询条件
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @return
     * @throws Exception
     */
    Page<T> select(QueryRule queryRule,int pageNo, int pageSize) throws Exception;

    /**
     * 根据SQL获取列表
     * @param sql sql语句
     * @param args 参数
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> selectBySql(String sql,Object... args) throws Exception;

    /**
     * 根据SQL获取分页
     * @param sql
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<Map<String,Object>> selectBySqlToPage(String sql, Object[] param, int pageNo, int pageSize) throws Exception;

    /**
     * 删除一条记录
     * @param entity
     * @return
     * @throws Exception
     */
    boolean delete(T entity) throws Exception;

    /**
     * 批量删除
     * @param list
     * @return 受影响的行数
     * @throws Exception
     */
    int deleteAll(List<T> list)throws Exception;

    /**
     * 插入一条记录并返因插入后的ID
     * entity不等于null,执行插入
     * @param entity
     * @return
     * @throws Exception
     */
    int insertAndReturnId(T entity) throws Exception;

    /**
     * 插入一条记录
     * @param entity
     * @return
     * @throws Exception
     */
    boolean insert(T entity) throws Exception;

    /**
     * 批量插入
     * @param list
     * @return 返回受影响行数
     * @throws Exception
     */
    int insertAll(List<T> list) throws Exception;

    /**
     * 修改一条记录
     * entity中的ID不能为空，如果ID为空，其他条件不能为空
     * @param entity
     * @return
     * @throws Exception
     */
    boolean update(T entity) throws Exception;
}
