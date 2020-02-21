package com.winsum.chatliu.service;

import com.winsum.chatliu.dto.QuestionDTO;
import com.winsum.chatliu.mapper.QuestionMapper;
import com.winsum.chatliu.mapper.UserMapper;
import com.winsum.chatliu.model.Question;
import com.winsum.chatliu.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;


    public List<QuestionDTO> list() {
        List<Question> questionList = questionMapper.list();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        if (questionList != null && questionList.size() > 0) // list 不为空
        {
            for (Question question : questionList) {
                User user = userMapper.findById(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        }

        return questionDTOList;
    }
}
