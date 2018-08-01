package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.QiniuService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsService;

    @Autowired
    QiniuService qiniuService;

    @Autowired
    HostHolder hostHolder;


    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    //把资讯信息通过model放到前台页面detail中
    @RequestMapping(path = {"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){

        try{
            News news = newsService.getById(newsId);
            //当资讯不为空的时候，就可以查找这个资讯的评论
            if(news != null){
                //因为查是的资讯，所以要传入资讯的entityId和eneityType
                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
                List<ViewObject> commentVOs = new ArrayList<ViewObject>();
                //创建ViewObject，循环遍历每一条评论，把评论和对应的用户都保存到ViewObject这个对象中，
                // 这个对象专门用来把数据存放到前端，可以在前端进行显示
                for(Comment comment : comments){
                    ViewObject commentVO = new ViewObject();
                    commentVO.set("comment",comment);
                    commentVO.set("user", userService.getUser(comment.getUserId()));
                    //然后把commentVO添加到集合中
                    commentVOs.add(commentVO);
                }
                //最后把集合通过Model这个类传到前台进行显示，就是模型驱动
                model.addAttribute("comments",commentVOs);
            }
            model.addAttribute("news",news);
            //从表中得到用户，并通过model添加到前台页面上
            model.addAttribute("owner",userService.getUser(news.getUserId()));
        } catch (Exception e) {
            logger.error("获取资讯明细错误" + e.getMessage());
        }
        return "detail";

    }

    //添加评论
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setEntityId(newsId);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            // 更新评论数量，以后用异步实现
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(), count);

        } catch (Exception e) {
            logger.error("提交评论错误" + e.getMessage());
        }
        //评论成功后从定向到news/newsId页面
        return "redirect:/news/" + String.valueOf(newsId);
    }

    //把上传的图片进行展示，RequestParam（传递参数）表示：页面中没哟对应的请求参数，回去ur中的参数，其中name是url中name的取值
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    //从前台获得name传递给imageName
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            //设置响应的类型为image/jpeg
            response.setContentType("image/jpeg");
            //StreamUtils也封装了好了拷贝的方法,把上唇图片变成二进制流方法输出流中
            StreamUtils.copy(new FileInputStream(new
                    File(ToutiaoUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    //注解RequestParam表示从页面上获取参数,二进制文件
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = newsService.saveImage(file);
            //String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败");
            }
            //上传成功就返回图片的链接地址
            return ToutiaoUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            //如果出现异常就把异常信息打印出来
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }
    }

    //设置News对象，把把图片连接等信息添加到表news中
    //RequestParam获取URL中的相关参数
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            //如果当前用户是登录状态，就把这个用户的id添加到表中，否则就设置一个匿名用户3
            if (hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                // 设置一个匿名用户
                news.setUserId(3);
            }
            newsService.addNews(news);
            //最后把news添加到表中
            return ToutiaoUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布失败");
        }
    }

}
