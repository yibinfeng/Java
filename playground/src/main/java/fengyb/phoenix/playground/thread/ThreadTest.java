package fengyb.phoenix.playground.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fengyibin
 */
public class ThreadTest {
    ReentrantLock lock = new ReentrantLock();

    private void test() {
        System.out.print(1);
    }

    public static void main(String[] args) {
    }
}
