package com.nowcoder.service;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/7/28.
 */
@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;

    //业务层添加评论
    public  int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    //业务层，根据entityId和entityType选择所有评论
    public List<Comment> getCommentsByEntity(int entityId, int entityType){
        return commentDAO.selectByEntity(entityId,entityType);
    }

    //业务层，获取某个评论实体的总数
    public int getCommentCount(int entityId, int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }

    //业务层，删除评论
    public void deleteComment(int entityId, int entityType){
        commentDAO.updateStatus(entityId,entityType,1);
    }
}
