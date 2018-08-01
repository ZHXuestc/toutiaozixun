package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/30.
 */
public class EventModel {

    //事件类型
    private EventType type;
    //触发者
    private int actorId;
    //触发对象用eneityId和entityType
    private int entityId;
    private int entityType;
    //触发对象的拥有者
    private int entityOwnerId;
    //触发事件的时候一些相应的参数存储到map集合中,叫做扩展性
    private Map<String,String> exts = new HashMap<String,String>();

    //产生get和set，构造方法
    public Map<String, String> getExts() {
        return exts;
    }
    public EventModel() {

    }
    public EventModel(EventType type) {
        this.type = type;
    }

    public String getExt(String name) {
        return exts.get(name);
    }

    public EventModel setExt(String name, String value) {
        exts.put(name, value);
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
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


}
