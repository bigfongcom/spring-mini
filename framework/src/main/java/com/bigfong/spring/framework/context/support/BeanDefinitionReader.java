package com.bigfong.spring.framework.context.support;

import com.bigfong.spring.framework.beans.config.BeanDefinition;
import com.bigfong.spring.framework.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 对配置文件进行查找、读取、解析
 *
 * @author bigfong
 * @since 2019/10/2
 */
public class BeanDefinitionReader {

    private List<String> registyBeanClasses = new ArrayList<>();
    private Properties config = new Properties();

    //固定配置文件中的key,相对规范XML的规范
    private final String SCAN_PACKAGE = "scanPackage";

    public Properties getConfig() {
        return this.config;
    }

    public BeanDefinitionReader(String... configLocations) {
        //通过URL定位找到其所对应的文件，然后转换为文件流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(configLocations[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {
        //转换为文件路径，实际上是把.换为/
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = (scanPackage + "." + file.getName()).replace(".class", "");
                registyBeanClasses.add(className);
            }
        }
    }

    /**
     * 把配置文件中扫描到的所有配置信息转换为BeanDefinition对像，以便之后的IOC操作
     * @return
     */
    public List<BeanDefinition> loadBeanDefinitions() {
        List<BeanDefinition> result = new ArrayList<BeanDefinition>();
        try {
            for (String className : registyBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                //如果是接口跳过
                if (beanClass.isInterface()){
                    continue;
                }

                result.add(doCreateBeanDefinition(StringUtil.toLowerFirstClass(beanClass.getSimpleName()),beanClass.getName()));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 把每一个配置信息解析成一个BeanDefinition
     * @param factoryBeanName
     * @param beanClassName
     * @return
     */
    private BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }


}
