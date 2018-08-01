package com.nowcoder;

import com.nowcoder.service.LikeService;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2018/7/31.
 */
public class LikeServiceTests {

    @Autowired
    LikeService likeService;

    //测试用例，在初始化数据之后执行
    @Test
    public void testLike(){
        likeService.disLike(123, 1, 1);
        Assert.assertEquals(-1, likeService.getLikeStatus(123, 1, 1));
    }

    //最先执行，用来初始化数据
    @Before
    public void setUp(){
        System.out.println("setUp");
    }

    //测试用例结束清理数据
    @After
    public void tearDown(){
        System.out.println("tearDown");
    }

    //BeforeClass和AfterClass是全局的，所以要使用静态的方法
    //在所有测试用例执行之前只执行一次
    @BeforeClass
    public static void beforeClass(){
        System.out.println("beforeClass");
    }

    //在所有测试用例结束后只执行一次
    @AfterClass
    public static void afterClass(){
        System.out.println("afterClass");
    }

}
