package fengyb.phoenix.playground;

import fengyb.phoenix.playground.proxy.AbstractClass;
import fengyb.phoenix.playground.proxy.ClassImplNoProxy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AopApplication.class)
public class AbstractClassTestsNoProxy {
    /**
     * 实现类接收
     * 这里直接new 出来一个非代理对象,不采用Spring 注入,
     */
    private ClassImplNoProxy notProxyClassImpl = new ClassImplNoProxy();
    /**
     * 抽象类接收
     * 这里直接new 出来一个非代理对象,不采用Spring 注入,
     */
    private AbstractClass abstractClass = new ClassImplNoProxy();

    /**
     * 测试是否代理对象,这里采用new,这里必然会抛出异常
     */
    @Test
    public void isProxy() {
        try {
            System.out.println(notProxyClassImpl.isAopProxy());
        } catch (Throwable throwable) {
            Assert.assertNotNull(throwable);
            return;
        }
        Assert.assertTrue(false);//如果代码能执行到这里,断言失败
    }


    /**
     * 测试是否cglib代理对象,这里采用new,这里必然会抛出异常
     */
    @Test
    public void isCglibProxy() {
        try {
            System.out.println(notProxyClassImpl.isCglibProxy());
        } catch (Throwable throwable) {
            Assert.assertNotNull(throwable);
            return;
        }
        Assert.assertTrue(false);//如果代码能执行到这里,断言失败
    }

    /**
     * 测试是否JDK动态代理对象,这里采用new,这里必然会抛出异常
     */
    @Test
    public void isJdkDynamicProxy() {
        try {
            System.out.println(notProxyClassImpl.isJdkDynamicProxy());
            ;
        } catch (Throwable throwable) {
            Assert.assertNotNull(throwable);
            return;
        }
        Assert.assertTrue(false);//如果代码能执行到这里,断言失败
    }

    //方法被重写,mark 为null
    @Test
    public void getSysoutMark() throws NoSuchMethodException {
        Mark mark = notProxyClassImpl.getClass().getMethod("sysout", null).getAnnotation(Mark.class);
        Assert.assertNull(mark);
    }

    //方法被重写,mark 为null
    @Test
    public void getAbstractClassSysoutMark() throws NoSuchMethodException {
        Mark mark = abstractClass.getClass().getMethod("sysout", null).getAnnotation(Mark.class);
        Assert.assertNull("方法被重写,mark 为null", mark);
    }

    //方法没有重写,测试通过
    @Test
    public void getSysout2Mark() throws NoSuchMethodException {
        Mark mark = notProxyClassImpl.getClass().getMethod("sysout2", null).getAnnotation(Mark.class);
        Assert.assertNotNull(mark);
    }

    //方法没有重写,测试通过
    @Test
    public void getAbstractClassSysout2Mark() throws NoSuchMethodException {
        Mark mark = abstractClass.getClass().getMethod("sysout2", null).getAnnotation(Mark.class);
        Assert.assertNotNull(mark);
    }

    //注解在类上是可以继承的
    @Test
    public void getImplClassMark() {
        Mark mark = notProxyClassImpl.getClass().getAnnotation(Mark.class);
        Assert.assertNotNull(mark);
    }

}
