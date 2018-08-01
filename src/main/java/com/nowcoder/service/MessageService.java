package com.nowcoder.service;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nowcoder on 2016/7/7.
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    //业务层添加消息
    public int addMessage(Message message) {
        return messageDAO.addMessage(message);
    }

    //业务层分页查询消息
    public List<Message> getConversationList(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }
    //业务层查询最新的会话信息，及信息的个数,带分页的查询
    public List<Message> getConversationList(int userId ,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }
    //业务层查询未读信息
    public int getConversationUnReadCount(int userId , String conversationId){
        return messageDAO.getConversationUnReadCount(userId,conversationId);
    }

}
