package cn.zss.blog.service.impl;

import cn.zss.blog.dao.TicketMapper;
import cn.zss.blog.dao.UserMapper;
import cn.zss.blog.entity.Ticket;
import cn.zss.blog.entity.User;
import cn.zss.blog.entity.UserExample;
import cn.zss.blog.service.TicketService;
import cn.zss.blog.service.UserService;
import cn.zss.blog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TicketService ticketService;

    @Override
    public void addUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public void deleteUserById(int id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateUserPassword(User user) {
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        user1.setPassword(user.getPassword());
        userMapper.updateByPrimaryKey(user1);
    }

    @Override
    public void updateUserEmail(User user) {
        User user1 = userMapper.selectByPrimaryKey(user.getId());
        user1.setEmail(user.getEmail());
        userMapper.updateByPrimaryKey(user1);
    }

    @Override
    public List<User> listAllUser() {
        UserExample userExample = new UserExample();
        userMapper.selectByExample(userExample);
        return userMapper.selectByExample(userExample);
    }

    @Override
    public User getUserById(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User getUserByUserName(String username) {
        UserExample userExample = new UserExample();
        userExample.or().andNameEqualTo(username);
        return userMapper.selectByExample(userExample).get(0);
    }

    @Override
    public void logout(String ticket) {
        //退出登录，状态改为１
        ticketService.updateTicketStatus(ticket,1);
    }

    @Override
    public String addLoginTicket(int userId) {
        //登录，ticket时限为１０天
        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        //uuid编码记录ticket
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        ticketService.addTicket(ticket);
        return ticket.getTicket();
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        UserExample userExample = new UserExample();
        userExample.or().andNameEqualTo(username);
        User user = userMapper.selectByExample(userExample).get(0);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }
        if (!user.getPassword().equals(MD5Util.MD5(password + user.getSalt()))) {
            map.put("msg", "密码错误");
            return map;
        }
        //传入ticket，也就是登录成功
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    @Override
    public Map<String, Object> register(String username, String password, String email) {
        Map<String, Object> map = new HashMap<>();

        //排除用户名为空，密码为空，邮箱为空，邮箱错误（待完成），已被注册

        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(email)) {
            map.put("msg", "邮箱不能为空");
            return map;
        }

        //邮箱有效验证？

        UserExample userExample = new UserExample();
        userExample.or().andNameEqualTo(username);
        User user = userMapper.selectByExample(userExample).get(0);
        if (user != null) {
            map.put("msg", "用户名已被注册");
            return map;
        }

        //用户名检验，敏感词，特殊符号等？

        //密码强度？


        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        //这里需要改
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",1));
        user.setPassword(MD5Util.MD5(password + user.getSalt()));
        user.setEmail(email);
        user.setRemark("注册");
        userMapper.insert(user);

        //传入ticket，也就是登录成功
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }
}
