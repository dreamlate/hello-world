package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by MengHan on 2017/7/12.
 */
@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse saveUpdateProduct(Product product){
        if(product!=null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length>0){
                    product.setMainImage(subImageArray[0]);
                }
            }

            if(product.getId()!=null){
                int count = productMapper.updateByPrimaryKey(product);
                if(count > 0) {
                    return ServerResponse.createBySuccess("更新产品成功！");
                }
                return ServerResponse.createBySuccess("更新产品失败！");
            }else{
                int count = productMapper.insert(product);
                if(count > 0) {
                    return ServerResponse.createBySuccess("新增产品成功！");
                }
                return ServerResponse.createBySuccess("新增产品失败！");
            }
        }
        return ServerResponse.createByError("新增或者更新产品参数不正确。");
    }

    public ServerResponse<String> setSaleStatus(Integer productId,Integer status){
        if(productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int count = productMapper.updateByPrimaryKeySelective(product);
        if(count>0){
            return ServerResponse.createBySuccess("修改商品状态成功！");
        }
        return ServerResponse.createByError("修改商品状态失败！");
    }

    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if(productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByError("产品已经下架或者删除！");
        }
        ProductDetailVo vo = assembleProcuvtDetailVo(product);
        return ServerResponse.createBySuccess(vo);
    }

    private ProductDetailVo assembleProcuvtDetailVo(Product product){
        ProductDetailVo vo = new ProductDetailVo();
        vo.setId(product.getId());
        vo.setSubtitle(product.getSubtitle());
        vo.setPrice(product.getPrice());
        vo.setMainImage(product.getMainImage());
        vo.setSubImages(product.getSubImages());
        vo.setCategoryId(product.getCategoryId());
        vo.setDetail(product.getDetail());
        vo.setName(product.getName());
        vo.setStatus(product.getStatus());
        vo.setStock(product.getStock());
        vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            vo.setParentCategoryId(0);//默认根节点
        }else{
            vo.setParentCategoryId(category.getParentId());
        }
        vo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        vo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return vo;
    }

}
