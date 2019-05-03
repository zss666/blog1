package cn.zss.blog.async;

import cn.zss.blog.util.JedisAdapter;
import cn.zss.blog.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    @Autowired
    private JedisAdapter jedisAdapter;

    private ApplicationContext applicationContext;
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    //事件统一处理，需要把一个事件给所有符合的handler处理，需要一个map来存储所有handler
    private Map<EventType,List<EventHandler>> config = new HashMap<>();

    //初始化完成后一直监听队列等待处理事件
    @Override
    public void afterPropertiesSet() throws Exception {
        //需要遍历所有handler，也就是找到所有实现handler接口的实现类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            //遍历所有eventhandler，找出每个handler支持的type。
            for (Map.Entry<String, EventHandler> entry: beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                //根据type和handler形成映射表，完成map。这是遍历了接口的所有能处理的事件
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                        config.get(type).add(entry.getValue());
                    }else {
                        config.get(type).add(entry.getValue());
                    }
                }

            }
        }

        //根据前面的map可以知道取出的时间由谁来处理
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    //队列里弹出事件，如果没有则阻塞
                    List<String> events = jedisAdapter.brpop(0, key);
                    for (String msg : events) {
                        //redis自带消息key要过滤掉
                        if (msg.equals(key)) {
                            continue;
                        }
                        EventModel eventModel = JSONObject.parseObject(msg, EventModel.class);
                        if (!config.containsKey(eventModel.getEventType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }
                        for (EventHandler eventHandler : config.get(eventModel.getEventType())) {
                            //处理事件
                            eventHandler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
