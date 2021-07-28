package com.wang.bus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.bus.domain.Goods;
import com.wang.bus.domain.Provider;
import com.wang.bus.service.GoodsService;
import com.wang.bus.service.ProviderService;
import com.wang.bus.vo.GoodsVo;
import com.wang.yun.common.AppFileUtils;
import com.wang.yun.common.Constant;
import com.wang.yun.common.DataGridView;
import com.wang.yun.common.ResultObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 老雷
 * @since 2019-09-27
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {


	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private ProviderService providerService;

	/**
	 * 查询
	 */
	@RequestMapping("loadAllGoods")
	public DataGridView loadAllGoods(GoodsVo goodsVo) {
		IPage<Goods> page = new Page<>(goodsVo.getPage(), goodsVo.getLimit());
		QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(goodsVo.getProviderid()!=null&&goodsVo.getProviderid()!=0,"providerid",goodsVo.getProviderid());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getGoodsname()), "goodsname", goodsVo.getGoodsname());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getProductcode()), "productcode", goodsVo.getProductcode());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getPromitcode()), "promitcode", goodsVo.getPromitcode());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getDescription()), "description", goodsVo.getDescription());
		queryWrapper.like(StringUtils.isNotBlank(goodsVo.getSize()), "size", goodsVo.getSize());
		this.goodsService.page(page, queryWrapper);
		List<Goods> records = page.getRecords();
		for (Goods goods : records) {
			Provider provider = this.providerService.getById(goods.getProviderid());
			if(null!=provider) {
				goods.setProvidername(provider.getProvidername());
			}
		}
		return new DataGridView(page.getTotal(), records);
	}

	/**
	 * 添加
	 */
	@RequestMapping("addGoods")
	public ResultObject addGoods(GoodsVo goodsVo) {
		try {
			if(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().endsWith("_temp")) {
				String newName=AppFileUtils.renameFile(goodsVo.getGoodsimg());
				goodsVo.setGoodsimg(newName);
			}
			this.goodsService.save(goodsVo);
			return ResultObject.ADD_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.ADD_ERROR;
		}
	}

	/**
	 * 修改
	 */
	@RequestMapping("updateGoods")
	public ResultObject updateGoods(GoodsVo goodsVo) {
		try {
			//说明是不默认图片
			if(!(goodsVo.getGoodsimg()!=null&&goodsVo.getGoodsimg().equals(Constant.IMAGES_DEFAULTGOODSIMG_PNG))) {
				if(goodsVo.getGoodsimg().endsWith("_temp")) {
					String newName= AppFileUtils.renameFile(goodsVo.getGoodsimg());
					goodsVo.setGoodsimg(newName);
					//删除原先的图片
					String oldPath=this.goodsService.getById(goodsVo.getId()).getGoodsimg();
					AppFileUtils.removeFileByPath(oldPath);
				}
			}
			this.goodsService.updateById(goodsVo);
			return ResultObject.UPDATE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.UPDATE_ERROR;
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping("deleteGoods")
	public ResultObject deleteGoods(Integer id,String goodsimg) {
		try {
			//删除原文件
			AppFileUtils.removeFileByPath(goodsimg);
			this.goodsService.removeById(id);
			return ResultObject.DELETE_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return ResultObject.DELETE_ERROR;
		}
	}
	
	/**
	 * 加载所有可用的供应商
	 */
	@RequestMapping("loadAllGoodsForSelect")
	public DataGridView loadAllGoodsForSelect() {
		QueryWrapper<Goods> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
		List<Goods> list = this.goodsService.list(queryWrapper);
		for (Goods goods : list) {
			Provider provider = this.providerService.getById(goods.getProviderid());
			if(null!=provider) {
				goods.setProvidername(provider.getProvidername());
			}
		}
		return new DataGridView(list);
	}
	
	/**
	 *根据供应商ID查询商品信息 
	 */
	@RequestMapping("loadGoodsByProviderId")
	public DataGridView loadGoodsByProviderId(Integer providerid) {
		QueryWrapper<Goods> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("available", Constant.AVAILABLE_TRUE);
		queryWrapper.eq(providerid!=null, "providerid", providerid);
		List<Goods> list = this.goodsService.list(queryWrapper);
		for (Goods goods : list) {
			Provider provider = this.providerService.getById(goods.getProviderid());
			if(null!=provider) {
				goods.setProvidername(provider.getProvidername());
			}
		}
		return new DataGridView(list);
	}
}
