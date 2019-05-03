package cn.zss.blog.async;

import cn.zss.blog.util.JedisAdapter;
import cn.zss.blog.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//事件生产者，负责把事件放进消息队列。这里可以考虑使用生产者消费者模型代替实现
@Service
public class EventProducer {
    //日志记录消息生产者
    private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    private JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);

            return true;
        }catch (Exception e) {
            logger.error("事件放入队列失败" + e.getMessage());
            return false;
        }
    }
}
