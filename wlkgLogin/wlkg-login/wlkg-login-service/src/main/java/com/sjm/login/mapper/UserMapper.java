package com.sjm.login.mapper;

import com.sjm.login.pojo.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    @Select("select * from tb_user where username = #{username} and password = #{password}")
    User selectByUsernameAndPassword(String username, String password);

    @Select("select * from tb_user where phone = #{phone} and password = #{password}")
    User selectByPhoneAndPassword(String phone, String password);

    @Select("select * from tb_user where email = #{email} and password = #{password}")
    User selectByEmailAndPassword(String email, String password);
}
