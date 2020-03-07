package com.winsum.chatliu.controller;


import com.winsum.chatliu.dto.CommentCreateDTO;
import com.winsum.chatliu.dto.CommentDTO;
import com.winsum.chatliu.dto.ResultDTO;
import com.winsum.chatliu.enums.CommentTypeEnum;
import com.winsum.chatliu.exception.CustomizeErrorCode;
import com.winsum.chatliu.model.Comment;
import com.winsum.chatliu.model.User;
import com.winsum.chatliu.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {


    @Autowired
    private CommentService commentService;

    @ResponseBody //可以把java对象转成json数据
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_NOT_NULL);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Integer id){
        List<CommentDTO> commentDTOS = commentService.listbByTargetId(id, CommentTypeEnum.COMMENT.getType());
        return ResultDTO.okOf(commentDTOS);
    }
}
