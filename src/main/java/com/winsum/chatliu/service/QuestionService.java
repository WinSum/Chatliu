package com.winsum.chatliu.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winsum.chatliu.dto.PageResultDTO;
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


    public PageResultDTO<QuestionDTO> list(Integer page, Integer size) {

        PageHelper.startPage(page,size);

        List<Question> questionList = questionMapper.list();

        //得到分页的结果
        PageInfo<Question> questionPageInfo = new PageInfo<>(questionList);

        List<QuestionDTO> questionDTOList = new ArrayList<>();


        if (questionPageInfo.getList() != null && questionPageInfo.getList().size() > 0) // list 不为空
        {
            for (Question question : questionPageInfo.getList()) {
                User user = userMapper.findById(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        }

        return new PageResultDTO<>(questionPageInfo.getTotal(),questionPageInfo.getPages(),
                questionPageInfo.getPageNum(), questionDTOList);
    }

    public PageResultDTO<QuestionDTO> listById(Integer id, Integer page, Integer size) {
        PageHelper.startPage(page,size);

        List<Question> questionList = questionMapper.listById(id);

        //得到分页的结果
        PageInfo<Question> questionPageInfo = new PageInfo<>(questionList);

        List<QuestionDTO> questionDTOList = new ArrayList<>();


        if (questionPageInfo.getList() != null && questionPageInfo.getList().size() > 0) // list 不为空
        {
            for (Question question : questionPageInfo.getList()) {
                User user = userMapper.findById(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        }

        return new PageResultDTO<>(questionPageInfo.getTotal(),questionPageInfo.getPages(),
                questionPageInfo.getPageNum(), questionDTOList);

    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
