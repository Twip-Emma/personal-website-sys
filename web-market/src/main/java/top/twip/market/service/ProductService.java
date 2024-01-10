package top.twip.market.service;

import org.springframework.stereotype.Component;
import top.twip.market.dao.ProductInfoDao;
import top.twip.api.entity.product.ProductInfo;
import top.twip.api.entity.user.UserInfo;
import top.twip.api.feign.HiganbanaClient;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 七画一只妖
 * @Date: 2022-05-23 20:59
 */
@Component
public class ProductService {

    @Resource
    private ProductInfoDao productInfoDao;

    @Resource
    private HiganbanaClient higanbanaClient;

    public Object getAllProduct() {
        List<ProductInfo> productInfos = productInfoDao.selectList(null);
        for (ProductInfo productInfo : productInfos) {
            UserInfo userInfo = higanbanaClient.getUserById(productInfo.getUserId());
            productInfo.setUserInfo(userInfo);
        }
        return productInfos;
    }
}
