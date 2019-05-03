package cn.zss.blog.service;

import cn.zss.blog.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    void addUser(User user);

    void deleteUserById(int id);

    void updateUserPassword(User user);

    void updateUserEmail(User user);

    List<User> listAllUser();

    User getUserById(int id);

    User getUserByUserName(String username);

    void logout(String ticket);

    String addLoginTicket(int userId);

    Map<String,Object> login(String username,String password);

    Map<String,Object> register(String username,String password,String email);

}
