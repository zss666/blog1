package cn.zss.blog.service;


import cn.zss.blog.entity.Ticket;

import java.util.List;

public interface TicketService {
    void addTicket(Ticket ticket);

    void deleteTicketById(int id);

    void updateTicketStatus(String ticket,int status);


    Ticket getTicketById(int id);

    Ticket getTicketByUserId(int userid);

    Ticket getTicketByTicket(String ticket);



}
