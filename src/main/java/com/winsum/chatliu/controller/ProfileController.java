package com.winsum.chatliu.controller;


import com.winsum.chatliu.dto.NotificationDTO;
import com.winsum.chatliu.dto.PageResultDTO;
import com.winsum.chatliu.dto.QuestionDTO;
import com.winsum.chatliu.mapper.UserMapper;
import com.winsum.chatliu.model.User;
import com.winsum.chatliu.service.NotificationService;
import com.winsum.chatliu.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "8") Integer size){

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 如果cookie 没有查询到user 跳转到 index
        if (user == null){
            return "redirect:/";
        }

        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("secitonName","我的问题");
            PageResultDTO<QuestionDTO> pageResultListById = questionService.listById(user.getId(), page, size);
            model.addAttribute("pageResultlist",pageResultListById);

        }else if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("secitonName","最新回复");
            PageResultDTO<NotificationDTO> pageResultListById = notificationService.list(user.getId(),page,size);
            model.addAttribute("pageResultlist",pageResultListById);

        }


        return "profile";
    }
}
