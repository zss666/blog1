package cn.zss.blog.cotroller;


import cn.zss.blog.entity.SysLog;
import cn.zss.blog.service.SysLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("SysLog")
public class SysLogController {
    //访客记录，浏览量
    @Autowired
    SysLogService sysLogService;

    @ApiOperation("返回所有的SysLog信息")
    @RequestMapping(value = "/listLog")
    public List<SysLog> listLog(){
        return sysLogService.listLog();
    }

}
