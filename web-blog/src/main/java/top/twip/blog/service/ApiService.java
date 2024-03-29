package top.twip.blog.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.twip.api.entity.image.*;
import top.twip.blog.dao.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 七画一只妖
 * @Date: 2022-07-05 22:33
 */
@Service
public class ApiService {

    @Resource
    private ImageSetuDao imageSetuDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 判断KEY是否合法1
    // 合法时自动减一
    public Boolean checkKey1(String key){
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        // 如果KEY不存在
        try {
            Integer time = (Integer) ops.get(key);
            if(time == null || time <= 0){
                return false;
            }else {
                time -= 1;
                ops.set(key, time);
                return true;
            }
        }catch (Exception e){
            return false;
        }
    }

    // 判断KEY是否合法2
    // 仅检查，不做处理
    public Boolean checkKey2(String key){
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        // 如果KEY不存在
        try {
            Integer time = (Integer) ops.get(key);
            if(time == null || time <= 0){
                return false;
            }else {
                return true;
            }
        }catch (Exception e){
            return false;
        }
    }

    // 新增一个KEY，默认5次，持续1小时
    public String addKey(Integer time){
        if(time == null){
            time = 5;
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(uuid,time,1, TimeUnit.HOURS);
        return uuid;
    }

    // 删除一个KEY
    public Boolean deleteKey(String key){
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        if(ops.get(key) == null){
            return false;
        }else{
            redisTemplate.delete(key);
            return true;
        }
    }

    // 获取一张图片
    public List<ImageSetu> getOneSetu(){
        Integer databaseNum = getRandomNum(1, 9);
        Integer databaseCount;
        Integer i;
        databaseCount = getDatabaseCount(databaseNum);
        i = getRandomNum(databaseCount, 1);
        return imageSetuDao.userGetOneSetuByIndex(databaseNum,i);
    }

    // 根据等级获取一张图片
    public List<ImageSetu> getOneSetuX(){
        Integer databaseNum = getRandomNum(1, 2);
        Integer databaseCount;
        Integer i;
        databaseCount = getDatabaseCountX(databaseNum);
        i = getRandomNum(databaseCount, 1);
        return imageSetuDao.userGetOneSetuByIndexX(databaseNum,i);
    }

    //获取一个随机数
    private Integer getRandomNum(Integer max,Integer min){
        // Math.random()*（最大数-最小数+1）+最小数
        return (int)(Math.random() * (max - min + 1) + min);
    }

    //查询某个数据库的表记录数量S
    private Integer getDatabaseCount(Integer databaseNum){
        return imageSetuDao.getCountByDatabaseName(databaseNum,500);
    }

    //查询某个数据库的表记录数量X
    private Integer getDatabaseCountX(Integer databaseNum){
        return imageSetuDao.getCountByDatabaseNameX(databaseNum,500);
    }
}
