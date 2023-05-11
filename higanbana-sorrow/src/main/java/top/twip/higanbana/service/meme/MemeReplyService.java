package top.twip.higanbana.service.meme;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import top.twip.common.entity.meme.MemeReply;
import top.twip.common.util.TokenRedisHandler;
import top.twip.higanbana.dao.MemeLikeDao;
import top.twip.higanbana.dao.MemeReplyDao;
import top.twip.higanbana.dao.WebsiteUserInfoDao;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemeReplyService {
    @Resource
    private MemeReplyDao memeReplyDao;

    @Resource
    private WebsiteUserInfoDao websiteUserInfoDao;

    @Resource
    private TokenRedisHandler tokenRedisHandler;

    public List<MemeReply> query(String memeId) {
        List<MemeReply> replyList = memeReplyDao.selectList(new QueryWrapper<MemeReply>()
                .eq("meme_id", memeId)
        );
        for (MemeReply reply: replyList){
            reply.setUser(websiteUserInfoDao.selectById(reply.getUserId()));
        }
        return replyList;
    }

    /**
     * 新增一个meme回复
     * @param memeId memeID
     * @param content 内容
     * @param token TOKEN
     */
    public void insert(String memeId, String content, String token) {
        String userId = tokenRedisHandler.getIdByToken(token);
        MemeReply one = new MemeReply();
        one.setContent(content);
        one.setUserId(userId);
        one.setMemeId(memeId);
        memeReplyDao.insert(one);
    }
}
