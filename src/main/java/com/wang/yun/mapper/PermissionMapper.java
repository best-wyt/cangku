package com.wang.yun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.yun.pojo.Permission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface PermissionMapper extends BaseMapper<Permission> {
    void deleteRolePermissionById(@Param("id") Serializable id);
}
