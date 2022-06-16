package com.example.tcp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.tcp.dao.UeUserMapper;
import com.example.tcp.pojo.UeUser;
import com.example.tcp.service.UeUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jay
 * @since 2022-05-27
 */
@Service
public class UeUserServiceImpl extends ServiceImpl<UeUserMapper, UeUser> implements UeUserService {

}
