package com.winsum.chatliu.service;

import com.winsum.chatliu.dto.GithubUser;
import com.winsum.chatliu.mapper.UserMapper;
import com.winsum.chatliu.model.User;
import com.winsum.chatliu.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String CreateOrUpdate(GithubUser githubUser){

        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(String.valueOf(githubUser.getId()));
        List<User> users = userMapper.selectByExample(userExample);
        String token = UUID.randomUUID().toString();

        if (users.size() == 0){
            //插入
            User user = new User();
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setToken(token);
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userMapper.insert(user);
        }else{
            User user = users.get(0);
            //更新
            user.setToken(token);
            user.setAvatarUrl(githubUser.getAvatarUrl());
            user.setName(githubUser.getName());
            user.setGmtModified(System.currentTimeMillis());

            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(user.getId());
            userMapper.updateByExampleSelective(user,example);
        }
        return token;
    }
}
