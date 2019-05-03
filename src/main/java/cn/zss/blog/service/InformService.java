package cn.zss.blog.service;

import cn.zss.blog.entity.Inform;

import java.util.List;

public interface InformService {
    void addInform(Inform inform);

    void deleteInform(int id);

    List<Inform> listAllInform();

    int getInformCount();

    //还可以有扩展功能，如单独列出一些通知等等

}
