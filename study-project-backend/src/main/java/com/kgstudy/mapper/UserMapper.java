package com.kgstudy.mapper;

import com.kgstudy.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author kg
 * @version 1.0
 * @description
 * @date 2023/4/13 23:16
 */
@Mapper
public interface UserMapper {

    @Select("select * from db_account where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);
}
