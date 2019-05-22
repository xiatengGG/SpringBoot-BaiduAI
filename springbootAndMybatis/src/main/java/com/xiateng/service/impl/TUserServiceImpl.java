package com.xiateng.service.impl;

import com.xiateng.dao.TUserMapper;
import com.xiateng.entity.TUser;
import com.xiateng.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TUserServiceImpl implements TUserService{
    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public List<TUser> selectByUserList(TUser example) {
        return null;
    }

    @Override
    public int deleteByPrimaryKey(Integer userId) {
        return tUserMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public int insertSelective(TUser record) {
        return tUserMapper.insertSelective(record);
    }

    @Override
    public TUser selectByPrimaryKey(Integer userId) {
        return tUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(TUser record) {
        return tUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<TUser> selectByTUser(TUser record) {
        return tUserMapper.selectByTUser(record);
    }
}
