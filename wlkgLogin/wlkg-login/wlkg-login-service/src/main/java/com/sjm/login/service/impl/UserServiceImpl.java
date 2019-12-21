package com.sjm.login.service.impl;

import com.sjm.login.mapper.UserMapper;
import com.sjm.login.pojo.User;
import com.sjm.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean checkUserData(String data, Integer type) {
        User user = new User();
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        switch (type) {
            case 1:
                user.setUsername(data);
                criteria.andEqualTo("username",user.getUsername());
                System.out.println(user);
                break;
            case 2:
                user.setPhone(data);
                criteria.andEqualTo("phone",user.getPhone());
                System.out.println(user);
                break;
            case 3:
                user.setEmail(data);
                criteria.andEqualTo("email",user.getEmail());
                System.out.println(user);
                break;
            default:
                return null;
        }
        List<User> users = this.userMapper.selectByExample(example);
        System.out.println(users);
        if(users != null && users.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public User loginUser(User user, Integer type) {
       System.out.println(user);
       System.out.println(type);
       User user1;
       if(type == 1){
           user.setUsername(user.getUsername());
           System.out.println(user);
           user1 = userMapper.selectByUsernameAndPassword(user.getUsername(),user.getPassword());
       }else if(type == 2){
           user.setPhone(user.getUsername());
           System.out.println(user);
           user1 = userMapper.selectByPhoneAndPassword(user.getPhone(),user.getPassword());
       }else if(type == 3){
           user.setEmail(user.getUsername());
           System.out.println(user);
           user1 = userMapper.selectByEmailAndPassword(user.getEmail(),user.getPassword());
       }else {
           return null;
       }
        System.out.println(user1);
        return user1;
    }


}
