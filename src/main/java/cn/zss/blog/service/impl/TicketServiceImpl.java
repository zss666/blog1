package cn.zss.blog.service.impl;

import cn.zss.blog.dao.TicketMapper;
import cn.zss.blog.entity.Ticket;
import cn.zss.blog.entity.TicketExample;
import cn.zss.blog.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    TicketMapper ticketMapper;

    @Override
    public void addTicket(Ticket ticket) {
        ticketMapper.insert(ticket);
    }

    @Override
    public void deleteTicketById(int id) {
        ticketMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateTicketStatus(String ticket, int status) {
        Ticket ticket1=getTicketByTicket(ticket);
        ticket1.setStatus(status);
    }


    @Override
    public Ticket getTicketById(int id) {
        return ticketMapper.selectByPrimaryKey(id);
    }

    @Override
    public Ticket getTicketByUserId(int userid) {
        TicketExample example = new TicketExample();
        example.or().andUserIdEqualTo(userid);
        return ticketMapper.selectByExample(example).get(0);
    }

    @Override
    public Ticket getTicketByTicket(String ticket) {
        TicketExample example = new TicketExample();
        example.or().andTicketEqualTo(ticket);
        return ticketMapper.selectByExample(example).get(0);
    }
}
