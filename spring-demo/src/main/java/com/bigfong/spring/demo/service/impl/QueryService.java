package com.bigfong.spring.demo.service.impl;

import com.bigfong.spring.demo.service.IModifyService;
import com.bigfong.spring.demo.service.IQueryService;
import com.bigfong.spring.framework.annotation.Autowired;
import com.bigfong.spring.framework.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author bigfong
 * @since 2019/10/3
 */
@Service
public class QueryService implements IQueryService {
    private Logger logger = LoggerFactory.getLogger(QueryService.class);

    //触发循环依赖
    @Autowired
    private IModifyService modifyService;

    @Override
    public String query(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
        logger.info("业务方法内容：" + json);
        return json;
    }
}
