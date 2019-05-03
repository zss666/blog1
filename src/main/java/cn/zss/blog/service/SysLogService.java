package cn.zss.blog.service;

import cn.zss.blog.entity.SysLog;

import java.util.List;

public interface SysLogService {
    void addsysLog(SysLog sysLog);

    List<SysLog> listLog();

    int getViewCount();
}
