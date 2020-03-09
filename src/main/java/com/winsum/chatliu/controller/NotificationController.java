package com.winsum.chatliu.controller;


import com.winsum.chatliu.dto.NotificationDTO;
import com.winsum.chatliu.dto.PageResultDTO;
import com.winsum.chatliu.dto.QuestionDTO;
import com.winsum.chatliu.enums.NotificationTypeEnum;
import com.winsum.chatliu.model.Comment;
import com.winsum.chatliu.model.Notification;
import com.winsum.chatliu.model.User;
import com.winsum.chatliu.service.CommentService;
import com.winsum.chatliu.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 如果cookie 没有查询到user 跳转到 index
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id,user);

        if (NotificationTypeEnum.REPLAY_QUESTION.getType() == notificationDTO.getType()) {
            return "redirect:/question/" + notificationDTO.getOuterid();

        } else if (NotificationTypeEnum.REPLAY_COMMENT.getType() == notificationDTO.getType()){
            Comment parentComment = commentService.selectById(notificationDTO.getOuterid());
            return "redirect:/question/" + parentComment.getParentId();

        }else {
            return "redirect:/";

        }
    }
}
