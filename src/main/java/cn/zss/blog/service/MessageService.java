package cn.zss.blog.service;

import cn.zss.blog.entity.Message;

import java.util.List;

public interface MessageService {
    void addMessage(Message message);

    List<Message> listMessageByUserId(int userId);

    List<Message> listUnReadMessage();

    int getUnReadMessageCount();

    int getAllMessageCount();

    Message getMessageById(int id);

}
