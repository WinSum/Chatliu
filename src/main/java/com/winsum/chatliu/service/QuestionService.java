package com.winsum.chatliu.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winsum.chatliu.dto.PageResultDTO;
import com.winsum.chatliu.dto.QuestionDTO;
import com.winsum.chatliu.exception.CustomizeErrorCode;
import com.winsum.chatliu.exception.CustomizeException;
import com.winsum.chatliu.mapper.QuestionExtMapper;
import com.winsum.chatliu.mapper.QuestionMapper;
import com.winsum.chatliu.mapper.UserMapper;
import com.winsum.chatliu.model.Question;
import com.winsum.chatliu.model.QuestionExample;
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

    @Autowired
    private QuestionExtMapper questionExtMapper;


    public PageResultDTO<QuestionDTO> list(Integer page, Integer size) {

        PageHelper.startPage(page, size);

        List<Question> questionList = questionMapper.selectByExample(new QuestionExample());

        //得到分页的结果
        PageInfo<Question> questionPageInfo = new PageInfo<>(questionList);

        List<QuestionDTO> questionDTOList = new ArrayList<>();


        if (questionPageInfo.getList() != null && questionPageInfo.getList().size() > 0) // list 不为空
        {
            for (Question question : questionPageInfo.getList()) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        }

        return new PageResultDTO<>(questionPageInfo.getTotal(), questionPageInfo.getPages(),
                questionPageInfo.getPageNum(), questionDTOList);
    }

    public PageResultDTO<QuestionDTO> listById(Integer id, Integer page, Integer size) {
        PageHelper.startPage(page, size);

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(id);
        List<Question> questionList = questionMapper.selectByExample(questionExample);

        //得到分页的结果
        PageInfo<Question> questionPageInfo = new PageInfo<>(questionList);

        List<QuestionDTO> questionDTOList = new ArrayList<>();


        if (questionPageInfo.getList() != null && questionPageInfo.getList().size() > 0) // list 不为空
        {
            for (Question question : questionPageInfo.getList()) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
        }

        return new PageResultDTO<>(questionPageInfo.getTotal(), questionPageInfo.getPages(),
                questionPageInfo.getPageNum(), questionDTOList);

    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrupdate(Question question) {
        if (question.getId() == null) {
            //如果id为null
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        } else {
            //更新question
            question.setGmtModified(System.currentTimeMillis());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(question, questionExample);
            if (update != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id) {
        //获得原来的阅读数
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
}
