package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * Created by MengHan on 2017/7/12.
 */
public interface IProductService {

    ServerResponse saveUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId,Integer status);

}
