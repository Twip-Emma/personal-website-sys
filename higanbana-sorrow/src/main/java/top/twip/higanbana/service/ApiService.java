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
    private ImageSetuDao imageSetuDao;

    public List<ImageSetu> getOneSetu(){
        Integer databaseNum = getRandomNum(1, 9);
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
        return imageSetuDao.getCountByDatabaseName(databaseNum,500);
    }
}
