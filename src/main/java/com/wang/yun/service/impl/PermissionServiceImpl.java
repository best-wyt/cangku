package com.wang.yun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wang.yun.mapper.PermissionMapper;
import com.wang.yun.pojo.Permission;
import com.wang.yun.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Override
    public boolean removeById(Serializable id) {

        PermissionMapper baseMapper = this.getBaseMapper();
        baseMapper.deleteRolePermissionById(id);

        return super.removeById(id);
    }
}
