package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2018/7/28.
 */
@Mapper
public interface CommentDAO {

    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    //增加一个评论
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    //返回int表示是否成功，失败会返回0，添加成功后comment中的id会被重新赋值，一般都是返回int
    int addComment(Comment comment);

    //根据entityId和entityType选择所有评论
    //entityId和entityType可以表示任何的实体，比如回复评论的话就直接传递评论的entityId和entityType
    @Select({"select ", INSERT_FIELDS,"from",TABLE_NAME,
            "where entity_type=#{entityType} and entity_id=#{entityId} order by id desc"})
    List<Comment> selectByEntity( @Param("entityId") int entityId , @Param("entityType") int entityType);

    //获取某个评论实体的总数
    @Select({"select count(id) from ", TABLE_NAME, " where entity_type=#{entityType} and entity_id=#{entityId}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    //删除评论
    @Update({"update", TABLE_NAME, "set status=#{status} where entity_type=#{entityType} and entity_id=#{entityId}"})
    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType , @Param("status") int status);
}

