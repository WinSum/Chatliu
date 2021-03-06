package com.winsum.chatliu.service;

import com.winsum.chatliu.dto.CommentDTO;
import com.winsum.chatliu.enums.CommentTypeEnum;
import com.winsum.chatliu.enums.NotificationStatusEnum;
import com.winsum.chatliu.enums.NotificationTypeEnum;
import com.winsum.chatliu.exception.CustomizeErrorCode;
import com.winsum.chatliu.exception.CustomizeException;
import com.winsum.chatliu.mapper.*;
import com.winsum.chatliu.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    public List<CommentDTO> listbByTargetId(Integer id, Integer type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id.longValue())
                .andTypeEqualTo(type);
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0){
            return new ArrayList<>();
        }

        // 获取去重的评论人ID
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));


        //转换comment-commentDTO
        List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOs;
    }

    @Transactional
    public void insert(Comment comment, User commentor) {
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbcomment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            dbcomment.setCommentCount(1);
            commentExtMapper.incCommentCount(dbcomment);

            int type = NotificationTypeEnum.REPLAY_COMMENT.getType();
            //创建通知 notificaiton
            CreateNofify(comment, dbcomment.getCommentator().longValue(),type,commentor.getName(),dbcomment.getContent());
        }else{
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId().intValue());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            int type = NotificationTypeEnum.REPLAY_QUESTION.getType();
            CreateNofify(comment,question.getCreator().longValue(),type,commentor.getName(),question.getTitle());
        }
    }

    /**
     * 创建通知
     * @param comment
     * @param receiver
     */
    private void CreateNofify(Comment comment, Long receiver,Integer type ,String notificaterName,String title) {
        if (receiver == comment.getCommentator().longValue()){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(type);
        notification.setOuterid(comment.getParentId());
        notification.setNotifier(comment.getCommentator().longValue());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notificaterName);
        notification.setOuterTitle(title);
        notificationMapper.insert(notification);
    }

    /**
     * 查询上级parentId
     * @param outerid
     * @return
     */
    public Comment selectById(Long outerid) {
        Comment comment = commentMapper.selectByPrimaryKey(outerid);
        return comment;
    }



}
