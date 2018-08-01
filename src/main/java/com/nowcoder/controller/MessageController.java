package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    //显示最新的会话信息，并把相应的个数也显示出来
    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model) {
        try{
            List<ViewObject> conversations = new ArrayList<ViewObject>();
            //得到当前用户的id
            int localUserId = hostHolder.getUser().getId();
            List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
            for(Message msg : conversationList){
                ViewObject vo = new ViewObject();
               //把消息添加进来
                vo.set("conversation",msg);
                //获得和localUserId交互的用户,如果formId和当前用户id相等，就取和我交互的用户toid
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                //查询和localUserId交互的用户
                User user = userService.getUser(targetId);
                vo.set("user",user);
                vo.set("unread",messageService.getConversationUnReadCount(localUserId,msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
            return "letter";

        }catch (Exception e){
            logger.error("获取站内信信息失败" + e.getMessage());
        }
        return "letter";
    }


    //获取某个会话的详细消息
    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String messageDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try{
            List<ViewObject> messages = new ArrayList<ViewObject>();
            List<Message> conversationList = messageService.getConversationList(conversationId,0,10);
            for(Message msg : conversationList){
                ViewObject vo = new ViewObject();
                //把遍历的消息存进去
                vo.set("message",msg);
                //获取发送消息的用户
                User user = userService.getUser(msg.getFromId());
                if(user == null){
                    continue;//本次循环结束进入下次循环
                }
                //把用户的头像和名字也设置进去
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userName",user.getName());
                messages.add(vo);
            }
            //最后把集合通过Model这个类传到前台进行显示，就是模型驱动
            model.addAttribute("messages", messages);
            return "letterDetail";
        }catch (Exception e){
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letterDetail";
    }


    //把会话消息添加到数据库
    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        Message msg = new Message();
        msg.setContent(content);
        msg.setCreatedDate(new Date());
        msg.setToId(toId);
        msg.setFromId(fromId);
        msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
                String.format("%d_%d", toId, fromId));
        messageService.addMessage(msg);
        return ToutiaoUtil.getJSONString(msg.getId());
    }
}
