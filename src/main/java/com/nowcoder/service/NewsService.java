package com.nowcoder.service;

import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.News;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    //得到最新的limt条资讯
    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    //更新评论数量
    public int updateCommentCount(int id , int count){
        return newsDAO.updateCommentCount(id,count);
    }

    public int updateLikeCount(int id,int likeCount){
        return newsDAO.updateLikeCount(id,likeCount);
    }

    //添加资讯
    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    //根据newsId查询资讯
    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }
    //把图片保存到本地
    public String saveImage(MultipartFile file) throws IOException {

        //先调用方法判断上传的文件是不是图片，判断方法是判断文件后缀名是不是jp等
        //getOriginalFilename()方法是得到本地源文件
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        //如果doPos小于0表示文件不是图片
        if(dotPos < 0){
            return null;
        }

        //得到文件后缀名,变成小写
        String fileExt = file.getOriginalFilename().substring(dotPos+1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt)){
            return null;
        }
        //代码走到这一步说明上传的文件是图片
        //生成文件名
        String fileName = UUID.randomUUID().toString().replace("-","") + "." + fileExt;
        //把文件保存到本地，toPath()表示把字符串变成路径
        //StandardCopyOption.REPLACE_EXISTING如果图片存在则替换
        Files.copy(file.getInputStream(),new File(ToutiaoUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING );
        //因为图片已经上传过了，所以最后返回图片的连接地址可以在网页上访问
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;

    }
}
