package cn.zss.blog.service.impl;

import cn.zss.blog.dao.InformMapper;
import cn.zss.blog.entity.Inform;
import cn.zss.blog.entity.InformExample;
import cn.zss.blog.service.InformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InformServiceImpl implements InformService {
    @Autowired
    InformMapper informMapper;

    @Override
    public void addInform(Inform inform) {
        informMapper.insert(inform);
    }

    @Override
    public void deleteInform(int id) {
        informMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Inform> listAllInform() {
        InformExample example = new InformExample();

        return informMapper.selectByExample(example);
    }

    @Override
    public int getInformCount() {
        return listAllInform().size();
    }
}
