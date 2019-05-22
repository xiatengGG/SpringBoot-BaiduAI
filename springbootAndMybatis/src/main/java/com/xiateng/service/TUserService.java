package com.xiateng.service;

import com.xiateng.entity.TUser;

import java.util.List;

public interface TUserService {

    List<TUser> selectByUserList(TUser example);

    int deleteByPrimaryKey(Integer userId);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(TUser record);

    List<TUser> selectByTUser(TUser record);
}
