package cn.zss.blog.service;

import cn.zss.blog.util.RedisKeyUtil;

public interface LikeService {
    int getLikeStatus(int userId, int entityId);

    long like(int userId, int entityId);

    long disLike(int userId, int entityId);
}
