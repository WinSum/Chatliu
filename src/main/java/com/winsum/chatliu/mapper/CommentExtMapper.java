package com.winsum.chatliu.mapper;

import com.winsum.chatliu.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}