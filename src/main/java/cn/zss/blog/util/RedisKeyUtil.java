package cn.zss.blog.util;


public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";

    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

    public static String getLikeKey(int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityId);
    }
}
