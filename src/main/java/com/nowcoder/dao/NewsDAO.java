package com.nowcoder.dao;

import com.nowcoder.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date, user_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    //根据newsId查询news
    @Select({"select ", SELECT_FIELDS , " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    //更新news表
    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);

    //更新表中喜欢的总量
    @Update({"update ", TABLE_NAME, " set like_count = #{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("id") int id, @Param("likeCount") int likeCount);

    //选择查询一批资讯，带分页的显示，offset是和limit相关的，@Param("userId") int userId等属性这样定义是可以写入到数据库中
    //使用配置文件的方式
    List<News> selectByUserIdAndOffset(@Param("userId") int userId, @Param("offset") int offset,
                                       @Param("limit") int limit);
}
