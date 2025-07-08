package com.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:TanAppplicationContext
 * @author: tanlianwang
 * @create: 2025−07-08 09:00
 **/
public class TanApplicationContext {

    private  Class configClass;

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    private Map<String, Object> singletonObjects = new HashMap<>();

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    public TanApplicationContext(Class configClass) {

        this.configClass = configClass;
        //扫描AppConfig下的包路径下的所有类，并将带有@Component注解的类注册到beanDefinitionMap中
        scan(configClass);

        //创建Bean实例
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();

            //如果是单例模式，创建Bean实例
            if (beanDefinition.getScope().equals("singleton")) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    /**
     * 创建Bean实例
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object createBean(String beanName,BeanDefinition beanDefinition) {

        Class<?> clazz = beanDefinition.getType();
        Object instance = null;

        try {
            // 使用反射创建Bean实例 默认使用无参构造函数   //TODO 有参构造函数的处理 ByType ByName
             instance = clazz.getConstructor().newInstance();

             //依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                if(declaredField.isAnnotationPresent(Autowired.class)){
                    declaredField.setAccessible(true); // 设置可访问性
                    //根据类型去查找bean实例 多个相同类型的bean时，使用ByName注入 //TODO 暂时只使用简单的BeanName去查找
                    String name = declaredField.getName();
                    declaredField.set(instance,getBean(name));
                }
            }


            if(instance instanceof BeanNameAware){
                // 如果Bean实现了BeanNameAware接口，调用setBeanName方法
                ((BeanNameAware) instance).setBeanName(beanName);
            }


            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            if(instance instanceof  InitializingBean){
                // 如果Bean实现了InitializingBean接口，调用afterPropertiesSet方法
               ((InitializingBean) instance).afterPropertiesSet();
            }


            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  instance;

    }



    public Object getBean(String beanName) {
      // 根据beanName获取Bean实例
        if(!beanDefinitionMap.containsKey(beanName)){
            throw new RuntimeException("bean name not found");
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        if(beanDefinition.getScope().equals("singleton")){
            // 如果是单例模式，直接从缓存中获取
            Object singletonBean = singletonObjects.get(beanName);

            if(singletonBean == null){
                // 如果缓存中没有，创建新的实例
                singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
            return singletonBean;
        }else{
            // 如果是非单例模式，创建新的实例
            Object prototypeBean = createBean(beanName, beanDefinition);
            return prototypeBean;
        }

    }


    private void scan(Class configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = (ComponentScan) configClass.getAnnotation(ComponentScan.class);

            String packageName = componentScan.value();
            packageName = packageName.replace('.', '/');

//            System.out.println("Scanning package: " + packageName);
            ClassLoader classLoader = TanApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(packageName);

            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    String absolutePath = f.getAbsolutePath();
                    String path = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                    path = path.replace("/", ".");

                    try {
                        Class<?> aClass = classLoader.loadClass(path);
                        if (aClass.isAnnotationPresent(Component.class)) {

                            if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                                BeanPostProcessor instance = (BeanPostProcessor) aClass.getConstructor().newInstance();
                                beanPostProcessors.add(instance);

                            }else{

                                Component componentAnnotation = aClass.getAnnotation(Component.class);
                                String beanName = componentAnnotation.value();

                                if ("".equals(beanName)) {
                                    beanName = Introspector.decapitalize(aClass.getSimpleName());
                                }

                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(aClass);

                                if (aClass.isAnnotationPresent(Scope.class)) {
                                    Scope annotation = aClass.getAnnotation(Scope.class);
                                    String scope = annotation.value();
                                    if (scope != null && !scope.isEmpty()) {
                                        beanDefinition.setScope(scope);
                                    }
                                } else {
                                    //默认是单例
                                    beanDefinition.setScope("singleton");
                                }

                                //创建Bean实例  如果beanName 重复则会覆盖
                                beanDefinitionMap.put(beanName, beanDefinition);
                            }

                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        } else {
            throw new RuntimeException("Configuration class must have @ComponentScan annotation");
        }
    }
}
