package fengyb.phoenix.playground.spring;

import fengyb.phoenix.playground.spring.service.OrderService;
import fengyb.phoenix.playground.spring.service.UserService;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.SchedulingConfigurer;

/**
 * @author fengyibin
 */
public class SpringTest {

    public void test01() {
        Resource resource = new FileSystemResource(SpringTest.class.getClassLoader().getResource("beans.xml").getPath());
        BeanFactory factory = new XmlBeanFactory(resource);
        System.out.println(factory.containsBean("dog"));
        Object dog = factory.getBean("dog");
    }

    public void test02() {
        ClassPathResource resource = new ClassPathResource("beans.xml");
        BeanFactory factory = new XmlBeanFactory(resource);
        System.out.println(factory.containsBean("dog"));
    }

    public void test03() {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"beans.xml"});
        BeanFactory factory = context;
        System.out.println(factory.containsBean("dog"));
    }

    public void test04() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:DefaultAdvisorAutoProxy.xml");

        UserService userService = context.getBean(UserService.class);
        OrderService orderService = context.getBean(OrderService.class);

        userService.createUser("Tom", "Cruise", 55);
        userService.queryUser();

        orderService.createOrder("Leo", "随便买点什么");
        orderService.queryOrder("Leo");

        System.out.println(AopUtils.isAopProxy(userService));
        System.out.println(AopUtils.isJdkDynamicProxy(userService));
        System.out.println(AopUtils.isCglibProxy(userService));
    }

    public static void main(String[] args) {
        SpringTest springTest = new SpringTest();
        springTest.test04();
    }
}
