package fengyb.phoenix.playground.spring.service;

import fengyb.phoenix.playground.pojo.Order;

public interface OrderService {

    Order createOrder(String username, String product);

    Order queryOrder(String username);
}
