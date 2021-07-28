package com.wang.bus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.bus.domain.Customer;
import com.wang.bus.service.CustomerService;
import com.wang.bus.vo.CustomerVo;
import com.wang.yun.common.DataGridView;
import com.wang.yun.common.ResultObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 老雷
 * @since 2019-09-27
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Resource
	private CustomerService customerService;

	/**
	 * 查询
	 */
	@RequestMapping("loadAllCustomer")
	public DataGridView loadAllCustomer(CustomerVo customerVo) {
		IPage<Customer> page = new Page<>(customerVo.getPage(), customerVo.getLimit());
		QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(StringUtils.isNotBlank(customerVo.getCustomername()), "customername",
				customerVo.getCustomername());
		queryWrapper.like(StringUtils.isNotBlank(customerVo.getPhone()), "phone", customerVo.getPhone());
		queryWrapper.like(StringUtils.isNotBlank(customerVo.getConnectionperson()), "connectionperson",
				customerVo.getConnectionperson());
		this.customerService.page(page, queryWrapper);
		return new DataGridView(page.getTotal(), page.getRecords());
	}

	/**
	 * 添加
	 */
	@RequestMapping("addCustomer")
	public ResultObject addCustomer(CustomerVo customerVo) {
		try {
			this.customerService.save(customerVo);
			return ResultObject.ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.ADD_ERROR;
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping("updateCustomer")
	public ResultObject updateCustomer(CustomerVo customerVo) {
		try {
			this.customerService.updateById(customerVo);
			return ResultObject.UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.UPDATE_ERROR;
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping("deleteCustomer")
	public ResultObject deleteCustomer(Integer id) {
		try {
			this.customerService.removeById(id);
			return ResultObject.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.DELETE_ERROR;
		}
	}

	/**
	 * 批量删除
	 */
	@RequestMapping("batchDeleteCustomer")
	public ResultObject batchDeleteCustomer(CustomerVo customerVo) {
		try {
			Collection<Serializable> idList = new ArrayList<Serializable>();
			for (Integer id : customerVo.getIds()) {
				idList.add(id);
			}
			this.customerService.removeByIds(idList);
			return ResultObject.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.DELETE_ERROR;
		}
	}
}
