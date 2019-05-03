package cn.zss.blog.async;

import java.util.HashMap;
import java.util.Map;

//事件实体
public class EventModel {
    private EventType eventType;
    private int actorId;//触发者，用户或管理员
    //触发对象
    private int entityId;
    private int entityType;
    //触发对象拥有者，拥有者会收到通知
    private int entityOwnerId;//通知管理员，博主

    //触发现场的信息保存
    private Map<String, String> exts = new HashMap<>();

    public EventModel set(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String get(String key) {
        return exts.get(key);
    }


    public EventModel(EventType eventType) {
        this.eventType = eventType;
    }
    public EventModel() {

    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }

}
