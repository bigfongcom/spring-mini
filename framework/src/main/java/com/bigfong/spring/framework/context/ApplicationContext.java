package com.bigfong.spring.framework.context;

import com.bigfong.spring.framework.annotation.Autowired;
import com.bigfong.spring.framework.annotation.Controller;
import com.bigfong.spring.framework.annotation.Service;
import com.bigfong.spring.framework.aop.AopConfig;
import com.bigfong.spring.framework.aop.AopProxy;
import com.bigfong.spring.framework.aop.CglibAopProxy;
import com.bigfong.spring.framework.aop.JdkDynamicAopProxy;
import com.bigfong.spring.framework.aop.support.AdvisedSupport;
import com.bigfong.spring.framework.beans.BeanWrapper;
import com.bigfong.spring.framework.beans.config.BeanDefinition;
import com.bigfong.spring.framework.beans.config.BeanPostProcessor;
import com.bigfong.spring.framework.context.support.BeanDefinitionReader;
import com.bigfong.spring.framework.context.support.DefaultListableBeanFactory;
import com.bigfong.spring.framework.core.BeanFactory;
import com.bigfong.spring.framework.utils.StringUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户入口，IOC,DI,MVC,AOP
 * @author bigfong
 * @since 2019/10/2
 */
public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {
    private String[] configLocations;
    private BeanDefinitionReader reader;

    //单例的IOC容器缓存,用来保证注册单例的容器,key是beanClassName(class的全类名)
    private Map<String,Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>();
    //通用的Ioc容器,保存Wrapper缓存，用来存储所有的被代理过的对象,key是beanName
    private Map<String,BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, BeanWrapper>();

    public ApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        //1.定位，定位配置文件
        reader = new BeanDefinitionReader(this.configLocations);

        //2.加载配置文件，扫描相关的类，把它们封装成BeanDefinition
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        //3.注册，把配置信息放到容器里(伪Ioc容器)
        doRegisterBeanDefinition(beanDefinitions);

        //4.把不是延时加载的类型提前初始化
        doAutowrited();
    }

    /**
     * 容器初始化-注册BeanDefinition
     * @param beanDefinitions
     * @throws Exception
     */
    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception{
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanName = beanDefinition.getFactoryBeanName();
            //factoryBeanName; 保存beanName,在Ioc容器中存储的key
            //判断是否已注册
            if (super.beanDefinitionMap.containsKey(beanName)){
                throw  new Exception("The "+beanName+" is exists!");
            }

            super.beanDefinitionMap.put(beanName,beanDefinition);
        }
        //end 容器初始化完毕
    }

    /**
     * 只处理非延时加载的bean
     */
    private void doAutowrited() {
        for (Map.Entry<String,BeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()){
            if (!beanDefinitionEntry.getValue().isLazyinit()){
                String beanName = beanDefinitionEntry.getKey();
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    /**
     * 依赖注入，从这里开始，读取BeanDefinition中的信息
     * 然后通过反射机制创建一个实例并返回
     * Spring做法是，不会把最原始的对象放出去，会用另一个BeanWrapper来进行一次封装
     *
     * 装饰器模块：
     * 1.保留原来的OOP关系
     * 2.需要对它进行扩展，增加（为以后AOP打基础）
     * @param beanName
     * @return
     * @throws Exception
     */
    @Override
    public Object getBean(String beanName) throws Exception {
        BeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);

        try {
            //通过成通知事件
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if (null == instance){
                return null;
            }

            //在实例初始化之前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            BeanWrapper beanWrapper = new BeanWrapper(instance);
            this.factoryBeanInstanceCache.put(beanName,beanWrapper);//记录所有被代理的类,提前设置，可以解决循环依赖问题
            //在实例初始化之后调用一次
            beanPostProcessor.postProcessAfterInitialization(instance,beanName);

            populateBean(beanName,instance);
            //通过这样调用，相当于给我们自己留有了可操作的空间
            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 处理依赖注入
     * @param beanName
     * @param instance
     */
    private void populateBean(String beanName, Object instance) {
        Class clazz = instance.getClass();

        //如果类注解不是Controller或Service
        if (!(clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class))){
            return;
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            //字段注解不是Autowired
            if (!field.isAnnotationPresent(Autowired.class)){
                continue;
            }

            Autowired autowired = field.getAnnotation(Autowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)){
                if (field.getType().isInterface()){
                    autowiredBeanName = field.getName();
                }else{
                    autowiredBeanName = StringUtil.toLowerFirstClass(field.getType().getSimpleName());//设置名称默认为字段类型名field.getType().getName();
                }
            }
            field.setAccessible(true);
            try {
                //如果注入的对象未实例化，则初始化它
                if (!this.factoryBeanInstanceCache.containsKey(autowiredBeanName)){
                    try {
                        this.getBean(autowiredBeanName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //为什么不用上面的beanName?
                //设置对象instance上变量field的值为this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance()
                field.set(instance,this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 传一个BeanDefinition，就返回一个实例Bean
     * @param beanDefinition
     * @return
     */
    private Object instantiateBean(BeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {
            //国为根据Class才能确定一个类是否有实例
            if (this.factoryBeanObjectCache.containsKey(className)){
                instance = this.factoryBeanObjectCache.get(className);
            }else{
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();

                //AOP
                AdvisedSupport config = instantionAopConfig(beanDefinition);
                config.setTargetClass(clazz);
                config.setTarget(instance);
                if (config.pointCutMatch()){
                    instance = createProxy(config).getProxy();
                }

                this.factoryBeanObjectCache.put(className,instance);
            }
            return instance;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private AopProxy createProxy(AdvisedSupport config) {
        Class targetClass = config.getTargetClass();
        if (targetClass.getInterfaces().length>0){
            return new JdkDynamicAopProxy(config);
        }
        return new CglibAopProxy(config);
    }


    private AdvisedSupport instantionAopConfig(BeanDefinition beanDefinition) throws Exception{
        AopConfig config = new AopConfig();
        config.setPointCut(reader.getConfig().getProperty("pointCut"));
        config.setAspectClass(reader.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(reader.getConfig().getProperty("aspectAfter"));
        config.setAspectAfterThrow(reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(reader.getConfig().getProperty("aspectAfterThrowingName"));

        return new AdvisedSupport(config);
    }

    public String[] getBeanDefinitionNames(){
        return super.beanDefinitionMap.keySet().toArray(new String[super.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount(){
        return super.beanDefinitionMap.size();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }

}
