package com.example.tcp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tcp.pojo.UeUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jay
 * @since 2022-05-27
 */
@Mapper
@Repository
public interface UeUserMapper extends BaseMapper<UeUser> {

}
