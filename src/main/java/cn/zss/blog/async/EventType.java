package cn.zss.blog.async;


//事件类型枚举类
public enum EventType {
    //态度事件
    LIKE(0),
    //用户评论
    COMMENT(1),
    //用户登录
    LOGIN(2),
    //私信
    MASSAGE(3),
    //回复私信，后台管理员，有待扩展
    RETURN(4);

    private int value;
    EventType(int value) {
        this.value = value;
    }
}
