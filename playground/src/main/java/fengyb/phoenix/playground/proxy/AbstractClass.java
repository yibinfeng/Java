package fengyb.phoenix.playground.proxy;

import fengyb.phoenix.playground.Mark;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fengyibin
 */
@Mark(desc = "我是被注解在抽象类中的Mark!")
public abstract class AbstractClass {
    @Mark
    @Transactional
    public abstract void sysout();

    /**
     * 此方法继承以后,子类没有重写,所以可以获取到注解(此结论仅针对非代理对象,以及jdk动态代理对象(不针对cglib代理对象))
     */
    @Mark
    public void sysout2(){
        System.out.println("sysout2");
    }

    public abstract boolean isAopProxy();

    public abstract boolean isCglibProxy();

    public abstract boolean isJdkDynamicProxy();
}
