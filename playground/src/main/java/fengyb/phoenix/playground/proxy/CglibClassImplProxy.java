package fengyb.phoenix.playground.proxy;

import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;

/**
 * @author fengyibin
 */
@Service("cglibClassImplProxy")
public class CglibClassImplProxy extends AbstractClass {
    @Override
    public void sysout() {

    }

    @Override
    public boolean isAopProxy(){
        return AopUtils.isAopProxy(AopContext.currentProxy());
    }


    @Override
    public boolean isCglibProxy(){
        return AopUtils.isCglibProxy(AopContext.currentProxy());
    }

    @Override
    public boolean isJdkDynamicProxy(){
        return AopUtils.isJdkDynamicProxy(AopContext.currentProxy());
    }

}
