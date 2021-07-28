package com.wang.bus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wang.bus.domain.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 老雷
 * @since 2019-09-27
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}
