package com.changgou.comment.controller;

import com.changgou.comment.config.TokenDecode;
import com.changgou.comment.pojo.Comment;
import com.changgou.comment.pojo.CommentCount;
import com.changgou.comment.pojo.CommentInfo;
import com.changgou.comment.service.CommentService;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Program: changgou_parent
 * @ClassName: CommentController
 * @Description:
 * @Author: KyleSun
 **/
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private CommentService commentService;


    /**
     * @description: //TODO 添加评论
     * @param: [comment]
     * @return: com.changgou.entity.Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody Comment comment) {
        String username = tokenDecode.getUserInfo().get("username");
        comment.setUsername(username);
        commentService.add(comment);
        return new Result(true, StatusCode.OK, "添加评论成功");
    }


    /**
     * @description: //TODO 添加评论
     * @param: [map]
     * @return: com.changgou.entity.Result
     */
    @RequestMapping("/addC")
    public Result addC(@RequestBody Map map) {

        Map dataList = (Map) map.get("dataList");
        String level = (String) dataList.get("score");
        String content = (String) dataList.get("comment");

        String orderId = String.valueOf((Long) map.get("orderId"));
        String spuId = String.valueOf((Long) map.get("spuId"));
        String skuId = String.valueOf((Long) map.get("skuId"));


        Comment comment = new Comment();
        comment.setLevel(level);
        comment.setContent(content);
        comment.setSpuId(spuId);
        comment.setOrderId(orderId);
        comment.setSkuId(skuId);

        String username = tokenDecode.getUserInfo().get("username");
        comment.setUsername(username);

        commentService.add(comment);
        return new Result(true, StatusCode.OK, "添加评论成功");

    }


    /**
     * @description: //TODO 根据spuId查询评论数量
     * @param: [spuId]
     * @return: com.changgou.entity.Result<com.changgou.comment.pojo.CommentCount>
     */
    @GetMapping("/getCommentCountBySpuId/{spuId}")
    public Result<CommentCount> getCommentCountBySpuId(@PathVariable("spuId") String spuId) {
        CommentCount commentCount = commentService.getCommentCountBySpuId(spuId);
        return new Result<>(true, StatusCode.OK, "获取评论数量成功", commentCount);
    }


    /**
     * @description: //TODO 根据spuId查询评论集合（用于商品详情页面评价展示）
     * @param: [spuId, level]
     * @return: com.changgou.entity.Result<java.util.List < com.changgou.comment.pojo.CommentInfo>>
     */
    @GetMapping("/getCommentInfoList/{spuId}/{level}")
    public Result<List<CommentInfo>> getCommentInfoList(@PathVariable("spuId") String spuId, @PathVariable("level") String level) {
        List<CommentInfo> commentInfoList = commentService.getCommentInfoList(spuId, level);
        return new Result<>(true, StatusCode.OK, "查询成功", commentInfoList);
    }


    /**
     * @description: //TODO 根据skuId查询评论列表
     * @param: [skuId]
     * @return: com.changgou.entity.Result<java.util.List < com.changgou.comment.pojo.Comment>>
     */
    @GetMapping("/findBySkuId/{skuId}")
    public Result<List<Comment>> findBySkuId(@PathVariable("skuId") String skuId) {
        List<Comment> commentList = commentService.findBySkuId(skuId);
        return new Result<>(true, StatusCode.OK, "查询成功", commentList);
    }

}
