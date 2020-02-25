package com.winsum.chatliu.controller;


import com.winsum.chatliu.dto.PageResultDTO;
import com.winsum.chatliu.dto.QuestionDTO;
import com.winsum.chatliu.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "8") Integer size
                        ) {

        PageResultDTO<QuestionDTO> resultList = questionService.list(page, size);
        model.addAttribute("pageResultlist", resultList);
        return "index";
    }

}
