package com.bigfong.spring.framework.orm.framework;

import com.bigfong.spring.framework.annotation.orm.*;
import com.bigfong.spring.framework.orm.common.jdbc.RowMapper;
import com.bigfong.spring.framework.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 实现数据库表结构和对象的映射关系
 *
 * @author bigfong
 * @since 2019/10/6
 */
public class EntityOperation<T> {
    private Logger logger = LoggerFactory.getLogger(EntityOperation.class);

    public Class<T> entityClass = null;//泛型实体Class对象
    public final Map<String, PropertyMapping> mappings;
    public final RowMapper<T> rowMapper;

    public final String tableName;
    public String allColum = "*";
    public Field pkField;


    public EntityOperation(Class<T> clazz, String pk) throws Exception {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new Exception("在" + clazz.getName() + "中没有找到Entity注解,不能执行ORM映射");
        }
        this.entityClass = clazz;
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null) {
            this.tableName = table.value().trim();
        } else {
            this.tableName = entityClass.getSimpleName();
        }

        Map<String, Method> getters = ClassMappings.findPublicGetters(entityClass);
        Map<String, Method> setters = ClassMappings.findPublicSetters(entityClass);
        Field[] fields = ClassMappings.findFields(entityClass);
        fillPkFieldAndAllColumn(pk, fields);
        this.mappings = getPropertyMappings(getters, setters, fields);
        this.allColum = this.mappings.keySet().toString().replace("[", "").replace("]", "").replaceAll(" ", "");
        this.rowMapper = createRowMapper();
    }

    /**
     * 设置主键
     *
     * @param pk
     * @param fields
     */
    private void fillPkFieldAndAllColumn(String pk, Field[] fields) {

        try {
            if (!StringUtils.isEmpty(pk)) {
                pkField = entityClass.getDeclaredField(pk);
                pkField.setAccessible(true);
            }
        } catch (Exception e) {
            logger.info("没找到主键列，主键名必须与属性名相同");
        }

        for (Field f : fields) {
            if (StringUtils.isEmpty(pk)) {
                Id id = f.getAnnotation(Id.class);
                if (id != null) {
                    pkField = f;
                    break;
                }
            }
        }
    }

    private Map<String, PropertyMapping> getPropertyMappings(Map<String, Method> getters, Map<String, Method> setters, Field[] fields) {
        Map<String, PropertyMapping> mappings = new HashMap<>();
        String name;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Transient.class)) {//不序列化
                continue;
            }
            name = field.getName();
            if (name.startsWith("is")) {
                name = name.substring(2);
            }
            name = StringUtil.toLowerFirstClass(name);
            Method setter = setters.get(name);
            Method getter = getters.get(name);
            if (setter == null || getter == null) {
                continue;
            }
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                mappings.put(field.getName(), new PropertyMapping(getter, setter, field));
            } else {
                mappings.put(column.name(), new PropertyMapping(getter, setter, field));
            }
        }
        return mappings;
    }

    private RowMapper createRowMapper() {
        return new RowMapper<T>() {
            public T mapRow(ResultSet rs, int rowNum) throws Exception {
                try {
                    T t = entityClass.newInstance();
                    ResultSetMetaData mete = rs.getMetaData();
                    int columns = mete.getColumnCount();
                    String columName;
                    for (int i = 0; i < columns; i++) {
                        Object value = rs.getObject(i);
                        columName = mete.getCatalogName(i);
                        fillBeanFieldValue(t, columName, value);
                    }
                    return t;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    protected void fillBeanFieldValue(T t, String columName, Object value) {
        if (value != null) {
            PropertyMapping pm = mappings.get(columName);
            if (pm != null) {
                try {
                    pm.set(t, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public T parese(ResultSet rs) {
        T t = null;
        if (null == rs) {
            return null;
        }
        Object value = null;
        try {
            t = (T) entityClass.newInstance();
            for (String columnName : mappings.keySet()) {
                try {
                    value = rs.getObject(columnName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fillBeanFieldValue(t, columnName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public Map<String, Object> parse(T t) {
        Map<String, Object> _map = new TreeMap<>();
        try {
            for (String columnName : mappings.keySet()) {
                Object value = mappings.get(columnName).getter.invoke(t);
                if (value == null) {
                    continue;
                }
                _map.put(columnName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _map;
    }

    public void println(T t) {
        try {
            for (String columnName : mappings.keySet()) {
                Object value = mappings.get(columnName).getter.invoke(t);
                if (value == null) {
                    continue;
                }
                System.out.println(columnName + " = " + value + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   public class PropertyMapping {
        final boolean insertable;
        final boolean updatable;
        final String columnName;
        final boolean id;
        final Method getter;
        final Method setter;
        final Class enumClass;
        final String fieldName;


        public PropertyMapping(Method getter, Method setter, Field field) {
            this.getter = getter;
            this.setter = setter;
            this.enumClass = getter.getReturnType().isEnum() ? getter.getReturnType() : null;
            Column column = field.getAnnotation(Column.class);
            this.insertable = column == null || column.insertable();
            this.updatable = column == null || column.updateble();
            this.columnName = column == null ? ClassMappings.getGetterName(getter) : ("".equals(column.name()) ? ClassMappings.getGetterName(getter) : column.name());
            this.id = field.isAnnotationPresent(Id.class);
            this.fieldName = field.getName();
        }

        @SuppressWarnings("unchecked")
        public Object get(Object target) throws Exception {
            Object r = getter.invoke(target);
            return enumClass == null ? r : Enum.valueOf(enumClass, (String) r);
        }

        @SuppressWarnings("unchecked")
        public void set(T target, Object value) throws Exception {
            if (enumClass != null && value != null) {
                value = Enum.valueOf(enumClass, (String) value);
            }

            try {
                if (value != null) {
                    setter.invoke(target, setter.getParameterTypes()[0].cast(value));
                }
            } catch (Exception e) {
                e.printStackTrace();
                //注意出错：boolen字段，mysql 字段类型设置tingint(1)
                System.out.println(fieldName + "--" + value);
            }
        }
    }
}
