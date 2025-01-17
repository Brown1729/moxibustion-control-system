package com.smoxisys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smoxisys.domain.Users;
import com.smoxisys.service.UsersService;
import com.smoxisys.mapper.UsersMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Administrator
 * @description 针对表【users】的数据库操作Service实现
 * @createDate
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService {
    /*
    1. 使用 MyBatis-Plus 提供的条件构造器
    QueryWrapper<Users>：
    用于构建查询条件。
    eq("username", username) 表示查询条件为：username = <输入的用户名>。
    eq("password", password) 表示查询条件为：password = <输入的密码>。
    2. 调用 getOne 方法
    this.getOne()：
    MyBatis-Plus 提供的 getOne 方法，用于根据条件查询数据库中的单条记录。
    如果查询到多条记录，getOne 默认会抛出异常（可以通过配置改变此行为）。
    */
    public Users login(String username, String password) {
        return this.getOne(new QueryWrapper<Users>()
                .eq("username", username)
                .eq("password", password));
    }
}
