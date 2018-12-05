package fengyb.phoenix.playground;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author fengyibin
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class AopApplication {
}
