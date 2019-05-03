package cn.zss.blog.service.impl;

import cn.zss.blog.dao.SysLogMapper;
import cn.zss.blog.entity.SysLog;
import cn.zss.blog.entity.SysLogExample;
import cn.zss.blog.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired(required = false)
    SysLogMapper sysLogMapper;


    @Override
    public void addsysLog(SysLog sysLog) {
        sysLogMapper.insertSelective(sysLog);
    }


    @Override
    public List<SysLog> listLog() {
        SysLogExample sysLogExample=new SysLogExample();
        return sysLogMapper.selectByExample(sysLogExample);
    }

    @Override
    public int getViewCount() {
        return listLog().size();
    }

}
