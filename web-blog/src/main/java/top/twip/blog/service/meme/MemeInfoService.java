package top.twip.blog.service.meme;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.twip.api.entity.meme.MemeInfo;
import top.twip.api.entity.meme.MemeLikeBind;
import top.twip.api.exception.BadRequestDataException;
import top.twip.api.exception.DatabaseHandlerException;
import top.twip.api.util.TokenRedisHandler;
import top.twip.blog.dao.MemeInfoDao;
import top.twip.blog.dao.MemeLikeDao;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemeInfoService {
    @Resource
    private MemeInfoDao memeInfoDao;

    @Resource
    private MemeLikeDao memeLikeDao;

    @Resource
    private TokenRedisHandler tokenRedisHandler;

    /**
     * 查所有的meme
     * @return meme列表
     */
    public List<MemeInfo> query(){
        // 按照ctime子段排序
        QueryWrapper<MemeInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ctime");
        return memeInfoDao.selectList(queryWrapper);
    }

    /**
     * 查询一个赞的绑定情况
     * @param memeId
     * @param token
     * @return
     */
    public MemeLikeBind queryLike(String memeId, String token) {
        String userId = tokenRedisHandler.getIdByToken(token);
        return memeLikeDao.selectOne(new QueryWrapper<MemeLikeBind>()
                .eq("meme_id", memeId)
                .eq("user_id", userId)
        );
    }

    /**
     * 更新meme的点赞数量
     * @param memeId ID
     * @param type 类型：inc/des
     * @param token TOKEN
     */
    public void updateLike(String memeId, String type, String token) throws Exception {
        String userId = tokenRedisHandler.getIdByToken(token);

        // 判断是否已经赞过
        MemeLikeBind one = memeLikeDao.selectOne(new QueryWrapper<MemeLikeBind>()
                .eq("meme_id", memeId)
                .eq("user_id", userId)
        );

        if ("inc".equals(type)){
            if (one != null) {
                throw new DatabaseHandlerException("你不能重复点赞");
            }
            // 更新数据
            MemeLikeBind bind = new MemeLikeBind();
            bind.setMemeId(memeId);
            bind.setUserId(userId);
            memeLikeDao.insert(bind);
            memeInfoDao.incLike(memeId);
        } else if ("des".equals(type)) {
            if (one == null) {
                throw new DatabaseHandlerException("请先点赞再取消");
            }
            // 更新数据
            memeLikeDao.delete(new QueryWrapper<MemeLikeBind>()
                    .eq("meme_id", memeId)
                    .eq("user_id", userId)
            );
            memeInfoDao.desLike(memeId);
        } else {
            throw new BadRequestDataException("你需要传入一个正确的type");
        }
    }
}
