package fengyb.phoenix.playground.spring.service;

import fengyb.phoenix.playground.pojo.User;

public interface UserService {

    User createUser(String firstName, String lastName, int age);

    User queryUser();
}
