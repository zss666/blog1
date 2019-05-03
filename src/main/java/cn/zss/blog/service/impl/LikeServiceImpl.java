package cn.zss.blog.service.impl;

import cn.zss.blog.service.LikeService;
import cn.zss.blog.util.JedisAdapter;
import cn.zss.blog.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public int getLikeStatus(int userId, int entityId) {
        //先获取事件的行为，再判断：1是喜欢，０是没点，－１是不喜欢
        String likeKey = RedisKeyUtil.getLikeKey(entityId);
        if(jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId);
        return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }

    public long like(int userId, int entityId) {
        // 在喜欢集合里增加
        String likeKey = RedisKeyUtil.getLikeKey(entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        // 从不喜欢里删除
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));
        //获取态度集合元素数
        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId,int entityId) {
        // 在不喜欢集合里增加
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        // 从喜欢里删除
        String likeKey = RedisKeyUtil.getLikeKey(entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        //获取态度集合元素数
        return jedisAdapter.scard(likeKey);
    }
}
