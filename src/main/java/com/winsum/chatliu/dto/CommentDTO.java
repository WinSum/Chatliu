package com.winsum.chatliu.dto;

import com.winsum.chatliu.model.User;
import lombok.Data;

@Data
public class CommentDTO {

    private Integer commentCount;

    private Long id;

    private Long parentId;

    private Integer type;

    private Integer commentator;

    private Long gmtCreate;

    private Long gmtModified;

    private Long likeCount;

    private String content;

    private User user;
}