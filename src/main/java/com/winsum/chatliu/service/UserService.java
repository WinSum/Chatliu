package com.winsum.chatliu.service;

import com.winsum.chatliu.dto.GithubUser;
import com.winsum.chatliu.mapper.UserMapper;
import com.winsum.chatliu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String CreateOrUpdate(GithubUser githubUser){

        User dbuser = userMapper.findUserById(String.valueOf(githubUser.getId()));
        String token = UUID.randomUUID().toString();
        if (dbuser == null){
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
            //更新
            dbuser.setToken(token);
            dbuser.setAvatarUrl(githubUser.getAvatarUrl());
            dbuser.setName(githubUser.getName());
            dbuser.setGmtModified(System.currentTimeMillis());
            userMapper.updateUser(dbuser);
        }
        return token;
    }
}
