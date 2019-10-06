package com.bigfong.spring.framework.orm.common.jdbc;

import java.sql.ResultSet;

/**
 * @author bigfong
 * @since 2019/10/6
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs,int rowNum) throws Exception;
}
