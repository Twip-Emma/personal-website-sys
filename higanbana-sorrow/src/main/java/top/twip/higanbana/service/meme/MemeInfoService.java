package top.twip.higanbana.service.meme;

import org.springframework.stereotype.Service;
import top.twip.common.entity.meme.MemeInfo;
import top.twip.higanbana.dao.MemeInfoDao;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemeInfoService {
    @Resource
    private MemeInfoDao memeInfoDao;

    /**
     * 查所有的meme
     * @return meme列表
     */
    public List<MemeInfo> query(){
        return memeInfoDao.selectList(null);
    }
}
