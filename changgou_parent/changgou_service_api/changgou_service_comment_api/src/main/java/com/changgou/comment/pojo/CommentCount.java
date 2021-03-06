package com.changgou.comment.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @Program: changgou_parent
 * @ClassName: CommentCount
 * @Description: 商品分类评价数量
 * @Author: KyleSun
 **/
@Document(collection = "tb_comment_count")
public class CommentCount implements Serializable {

    private Long allComment;

    private Long highComment;

    private Long mediumComment;

    private Long lowComment;

    public Long getAllComment() {
        return allComment;
    }

    public void setAllComment(Long allComment) {
        this.allComment = allComment;
    }

    public Long getHighComment() {
        return highComment;
    }

    public void setHighComment(Long highComment) {
        this.highComment = highComment;
    }

    public Long getMediumComment() {
        return mediumComment;
    }

    public void setMediumComment(Long mediumComment) {
        this.mediumComment = mediumComment;
    }

    public Long getLowComment() {
        return lowComment;
    }

    public void setLowComment(Long lowComment) {
        this.lowComment = lowComment;
    }

}
