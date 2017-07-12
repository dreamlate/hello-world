package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by MengHan on 2017/7/12.
 */
@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

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

}
