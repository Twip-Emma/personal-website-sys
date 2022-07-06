package top.twip.higanbana.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.twip.common.entity.image.*;
import top.twip.higanbana.dao.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-05 22:33
 */
@Service
@DS("image")
public class ApiService {
    @Resource
    private ImageSetuDaoS1 imageSetuDaoS1;
    @Resource
    private ImageSetuDaoS2 imageSetuDaoS2;
    @Resource
    private ImageSetuDaoS3 imageSetuDaoS3;
    @Resource
    private ImageSetuDaoS4 imageSetuDaoS4;
    @Resource
    private ImageSetuDaoS5 imageSetuDaoS5;
    @Resource
    private ImageSetuDaoS6 imageSetuDaoS6;
    @Resource
    private ImageSetuDaoS7 imageSetuDaoS7;
    @Resource
    private ImageSetuDaoS8 imageSetuDaoS8;
    @Resource
    private ImageSetuDaoS9 imageSetuDaoS9;

    @Resource
    private ImageSetuDao imageSetuDao;

    public List<ImageSetu> getOneSetu(){
//        int i = (int) (Math.random() * 500 + 1);
//        return imageSetuDaoS1.selectList(new QueryWrapper<ImageSetuS1>().last("limit " + i + ",1;"));
        Integer databaseNum = getRandomNum(1, 9);
//            Integer databaseNum = 1;
            Integer databaseCount;
            Integer i;
            databaseCount = getDatabaseCount(databaseNum);
            i = getRandomNum(databaseCount, 1);
            return imageSetuDao.userGetOneSetuByIndex(databaseNum,i);
    }

    //获取一个随机数
    private Integer getRandomNum(Integer max,Integer min){
        // Math.random()*（最大数-最小数+1）+最小数
        return (int)(Math.random() * (max - min + 1) + min);
    }

    //查询某个数据库的表记录数量
    private Integer getDatabaseCount(Integer databaseNum){
        return switch (databaseNum) {
            case 1 -> imageSetuDaoS1.selectCount(null);
            case 2 -> imageSetuDaoS2.selectCount(null);
            case 3 -> imageSetuDaoS3.selectCount(null);
            case 4 -> imageSetuDaoS4.selectCount(null);
            case 5 -> imageSetuDaoS5.selectCount(null);
            case 6 -> imageSetuDaoS6.selectCount(null);
            case 7 -> imageSetuDaoS7.selectCount(null);
            case 8 -> imageSetuDaoS8.selectCount(null);
            case 9 -> imageSetuDaoS9.selectCount(null);
            default -> 0;
        };
    }
}
