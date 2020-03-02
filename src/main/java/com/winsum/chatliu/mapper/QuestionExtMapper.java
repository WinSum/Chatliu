package com.winsum.chatliu.mapper;

import com.winsum.chatliu.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question record);
}