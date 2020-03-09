package com.winsum.chatliu.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winsum.chatliu.dto.PageResultDTO;
import com.winsum.chatliu.dto.QuestionDTO;
import com.winsum.chatliu.dto.ResultDTO;
import com.winsum.chatliu.exception.CustomizeErrorCode;
import com.winsum.chatliu.exception.CustomizeException;
import com.winsum.chatliu.mapper.QuestionExtMapper;
import com.winsum.chatliu.mapper.QuestionMapper;
import com.winsum.chatliu.mapper.UserMapper;
import com.winsum.chatliu.model.Question;
import com.winsum.chatliu.model.QuestionExample;
import com.winsum.chatliu.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");

        PageResultDTO<QuestionDTO> questionDTOPageResultDTO = getquestionPage(questionExample);
        return questionDTOPageResultDTO;
    }

    public PageResultDTO<QuestionDTO> listById(Integer id, Integer page, Integer size) {
        PageHelper.startPage(page, size);

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(id);

        PageResultDTO<QuestionDTO> questionDTOPageResultDTO = getquestionPage(questionExample);
        return questionDTOPageResultDTO;
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
            question.setLikeCount(0);
            question.setViewCount(0);
            question.setCommentCount(0);
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


    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDTO.getTag(), ",");
        String regTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regTag);
        List<Question> questions = questionExtMapper.selectRelated(question);

        //转换成questionDTO
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(q,dto);
            return dto;
        }).collect(Collectors.toList());
        return questionDTOS;
    }


    /**
     * 返回page问题方法
     * @param questionExample
     * @return
     */
    public PageResultDTO<QuestionDTO> getquestionPage(QuestionExample questionExample){
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
}
