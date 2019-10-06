package com.bigfong.spring.framework.orm.framework;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据QueryRule 自动生成SQL语句
 * @author bigfong
 * @since 2019/10/6
 */
public class QueryRuleSqlBuilder {
    private int CURR_INDEX = 0;//记录参数所在的位置
    private List<String> properties;//保存列名表
    private List<Object> values;//保存参数值列表
    private List<Order> orders;//保存排序规则列表

    private String whereSql = "";
    private String orderSql = "";
    private Object[] valueArr = new Object[]{};
    private Map<Object,Object> valueMap = new HashMap<>();

    public String getWhereSql() {
        return whereSql;
    }

    /**
     * 排序条件
     * @return
     */
    public String getOrderSql() {
        return orderSql;
    }

    /**
     * 获得参数值列表
     * @return
     */
    public Object[] getValues() {
        return this.valueArr;
    }

    public Map<Object, Object> getValueMap() {
        return valueMap;
    }

    public QueryRuleSqlBuilder(QueryRule queryRule) {
        CURR_INDEX = 0;
        properties = new ArrayList<>();
        values = new ArrayList<>();
        orders = new ArrayList<>();
        for (QueryRule.Rule rule : queryRule.getRuleList()) {
            switch (rule.getType()){
                case QueryRule.BETWEEN:
                    processBetween(rule);
                    break;
                case QueryRule.EQ:
                    processEqual(rule);
                    break;
                case QueryRule.NOTEQ:
                    processNotEqual(rule);
                    break;
                case QueryRule.LIKE:
                    processLike(rule);
                    break;

                case QueryRule.GT:
                    processGreaterThan(rule);
                    break;
                case QueryRule.GE:
                    processGreaterEqual(rule);
                    break;
                case QueryRule.LT:
                    processLessThan(rule);
                    break;
                case QueryRule.LE:
                    processLessEqual(rule);
                    break;
                case QueryRule.IN:
                    processIn(rule);
                    break;
                case QueryRule.NOTIN:
                    processNotIn(rule);
                    break;
                case QueryRule.ISNULL:
                    processIsNull(rule);
                    break;
                case QueryRule.ISNOTNULL:
                    processIsNotNull(rule);
                    break;
                case QueryRule.ISEMPTY:
                    processIsEmpty(rule);
                    break;
                case QueryRule.ISNOTEMPTY:
                    processIsNotEmpty(rule);
                    break;
                case QueryRule.ASC_ORDER:
                    processOrder(rule);
                    break;
                case QueryRule.DESC_ORDER:
                    processOrder(rule);
                    break;
                default:
                    throw new IllegalArgumentException("type: "+rule.getType()+" not supported.");
            }
        }
        //拼接where条件
        appendWhereSql();
        //拼接排序语句
        appendOrderSql();
        //拼装参数值
        appendValues();
    }

    /**
     * 处理between
     * @param rule
     */
    private void processBetween(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues()) || rule.getValues().length<2){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),"","between",rule.getValues()[0],"and");
        add(0,"","","",rule.getValues()[1],"");
    }

    /**
     * 处理 =
     * @param rule
     */
    private void processEqual(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),"=",rule.getValues()[0]);
    }

    /**
     * 处理 <>
     * @param rule
     */
    private void processNotEqual(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),"<>",rule.getValues()[0]);
    }


    /**
     * 处理 >
     * @param rule
     */
    private void processGreaterThan(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),">",rule.getValues()[0]);
    }

    /**
     * 处理 >=
     * @param rule
     */
    private void processGreaterEqual(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),">=",rule.getValues()[0]);
    }

    /**
     * 处理 <
     * @param rule
     */
    private void processLessThan(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),"<",rule.getValues()[0]);
    }

    /**
     * 处理 <=
     * @param rule
     */
    private void processLessEqual(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),"<=",rule.getValues()[0]);
    }

    /**
     * 处理 in
     * @param rule
     */
    private void processIn(QueryRule.Rule rule) {
        processInAndNotIn(rule,"in");
    }

    /**
     * 处理 not in
     * @param rule
     */
    private void processNotIn(QueryRule.Rule rule) {
        processInAndNotIn(rule,"in");
    }

    /**
     * 处理 in 和 not in
     * @param rule
     */
    private void processInAndNotIn(QueryRule.Rule rule,String name) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }

        if (rule.getValues().length==1 && rule.getValues()[0]!=null && (rule.getValues()[0] instanceof  List)){
            List<Object> list = (List) rule.getValues()[0];
            if (list!=null && list.size()>0){
                for (int i = 0; i < list.size(); i++) {
                    if (i==0 && list.size()==1){
                        add(rule.getAndOr(),rule.getPropertyName(),"",name+" (",list.get(i),") ");
                    }else if (i==0 && i< list.size()-1){
                        add(rule.getAndOr(),rule.getPropertyName(),"",name+" (",list.get(i),"");
                    }

                    if (i>0 && i<list.size()-1){
                        add(0,"",",","",list.get(i),"");
                    }
                    if (i== list.size()-1 && i!=0){
                        add(0,"",",","",list.get(i),") ");
                    }
                }
            }
        }else{
            Object[] list = rule.getValues();
            for (int i = 0; i < list.length; i++) {
                if (i==0 && list.length==1){
                    add(rule.getAndOr(),rule.getPropertyName(),"",name+" (",list[i],") ");
                }else if (i==0 && i< list.length-1){
                    add(rule.getAndOr(),rule.getPropertyName(),"",name+" (",list[i],"");
                }

                if (i>0 && i<list.length-1){
                    add(0,"",",","",list[i],"");
                }
                if (i== list.length-1 && i!=0){
                    add(0,"",",","",list[i],") ");
                }
            }
        }
    }

    /**
     * 处理 is null
     * @param rule
     */
    private void processIsNull(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),"is null",null);
    }

    /**
     * 处理 is not null
     * @param rule
     */
    private void processIsNotNull(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),"is not null",null);

    }

    /**
     * 处理 =''
     * @param rule
     */
    private void processIsEmpty(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),"=","''");
    }

    /**
     * 处理 <>''
     * @param rule
     */
    private void processIsNotEmpty(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),"<>","''");
    }

    private void processLike(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())){
            return;
        }
        Object object = rule.getValues()[0];
        if (object!=null){
            String value = object.toString();
            if (!StringUtils.isEmpty(value)){
                value = value.replace('*','%');
                object = value;
            }
        }else{
            return;
        }
        add(rule.getAndOr(),rule.getPropertyName(),"like",object.toString());
    }

    private void add(int andOr, String key, String split ,Object value) {
        add(andOr, key, split, "",value,"");
    }

    private void add(int andOr, String key, String split, String prefix,Object value,String suffix) {
        String andOrStr = ((andOr==0)?"":(QueryRule.AND == andOr?" and ":" or "));
        properties.add(CURR_INDEX,andOrStr+key+" "+split+prefix+(null!=value?" ? ":" ")+suffix);
        if (null!=value){
            values.add(CURR_INDEX,value);
            CURR_INDEX++;
        }
    }

    private void processOrder(QueryRule.Rule rule) {
        switch (rule.getType()){
            case QueryRule.ASC_ORDER:
                if (!StringUtils.isEmpty(rule.getPropertyName())){
                    orders.add(Order.asc(rule.getPropertyName()));
                }
                break;
            case QueryRule.DESC_ORDER:
                if (!StringUtils.isEmpty(rule.getPropertyName())){
                    orders.add(Order.desc(rule.getPropertyName()));
                }
                break;
        }
    }

    /**
     * 处理 where
     */
    private void appendWhereSql() {
        StringBuffer whereSql = new StringBuffer();
        for (String p : properties) {
            whereSql.append(p);
        }
        this.whereSql = removeSelect(removeOrders(whereSql.toString()));
    }

    /**
     * 去掉select
     * @param sql
     * @return
     */
    private String removeSelect(String sql) {
        Pattern pattern = Pattern.compile("order\\s*by[\\w|\\W\\s|\\S]*",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()){
            matcher.appendReplacement(sb,"");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 去掉order
     * @param sql
     * @return
     */
    private String removeOrders(String sql) {
        if (sql.toLowerCase().matches("from\\s+")){
            int beginPos = sql.toLowerCase().indexOf("from");
            return sql.substring(beginPos);
        }
        return sql;
    }

    private void appendOrderSql() {
        StringBuilder orderSql = new StringBuilder();
        for (int i = 0; i < orders.size(); i++) {
            if (i>0&&i<orders.size()){
                orderSql.append(",");
            }
            orderSql.append(orders.get(i).toString());
        }
        this.orderSql = removeSelect(removeOrders(orderSql.toString()));
    }

    private void appendValues() {
        Object[] val = new Object[values.size()];
        for (int i = 0; i < values.size(); i++) {
            val[i] = values.get(i);
            valueMap.put(i,values.get(i));
        }
        this.valueArr = val;
    }

}
