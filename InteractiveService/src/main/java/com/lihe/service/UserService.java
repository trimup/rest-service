package com.lihe.service;


import com.lihe.common.Constant;
import com.lihe.common.Msg;
import com.lihe.entity.UserInfo;
import com.lihe.persistence.UserInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;

/**
 * 用户管理服务
 * Created by leo on 8/4/15.
 */
@Service
public class UserService {

  private static final Logger L = LoggerFactory.getLogger(UserService.class);
  private static final SimpleDateFormat Format = new SimpleDateFormat("yyyyMMddHHmmss");

  @Autowired
  private UserInfoMapper userInfoMapper;
  /**
   * 验证 token
   *
   * @param userId 用户ID
   * @param token  用户token
   */
  public Msg checkToken(Integer userId, String token)
  {
    Msg msg=new Msg();
    UserInfo info =userInfoMapper.findUserById(userId);
    if(info ==null)
    {
      throw  new InvalidParameterException("未找到该用户");
    }
    if(info.getToken().equals(token))
    {
      msg.setCode(Constant.SUCCESS);
    }else
    {
      msg.setCode(Constant.REPEAT_LOGIN);
      msg.setMsg("此账户长时间未进行操作或已通过其他设备登录，请退出系统后重新登录");
    }

    return  msg;
  }


  public UserInfo getUserInfoById(Integer userId) {
    return userInfoMapper.findUserById(userId);
  }

}

