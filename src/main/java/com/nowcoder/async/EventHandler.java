package com.nowcoder.async;

import java.util.List;

/**
 * Created by Administrator on 2018/7/30.
 */
//每个Handler对事件的处理是不一样的所以要定义成接口
public interface EventHandler {

    //要进行处理的handle
    void doHandle(EventModel model);

    //在处理某一handle时，还要关注事件发生时的一些EventType
    List<EventType> getSupportEventTypes();

}
