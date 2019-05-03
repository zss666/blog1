package cn.zss.blog.service.impl;

import cn.zss.blog.dao.MessageMapper;
import cn.zss.blog.entity.Message;
import cn.zss.blog.entity.MessageExample;
import cn.zss.blog.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    public void addMessage(Message message) {
        messageMapper.insert(message);
    }

    @Override
    public List<Message> listMessageByUserId(int userId) {
        MessageExample example = new MessageExample();
        example.or().andUserIdEqualTo(userId);
        return messageMapper.selectByExample(example);
    }

    @Override
    public List<Message> listUnReadMessage() {
        MessageExample example = new MessageExample();
        example.or().andHasReadEqualTo(0);
        return messageMapper.selectByExample(example);
    }

    @Override
    public int getUnReadMessageCount() {
        return listUnReadMessage().size();
    }

    @Override
    public int getAllMessageCount() {
        MessageExample example = new MessageExample();
        return messageMapper.selectByExample(example).size();
    }

    @Override
    public Message getMessageById(int id) {
        return messageMapper.selectByPrimaryKey(id);
    }

    //还可以增加列出所有私信，列出所有已读私信等等

}
