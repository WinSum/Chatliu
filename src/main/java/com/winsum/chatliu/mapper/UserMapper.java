package com.winsum.chatliu.mapper;

import com.winsum.chatliu.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{creator}")
    User findById(@Param("creator") Integer creator);

    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findUserByToken(@Param("token") String token);

    @Select("select * from user where account_id = #{id}")
    User findUserById(@Param("id") String id);

    @Update("update user set name=#{name},token=#{token},gmt_modified = #{gmtModified},avatar_url = #{avatarUrl} where id = #{id}")
    void updateUser(User dbuser);
}
