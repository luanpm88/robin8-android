package com.robin8.rb.module.find;

/**
 Created by zc on 2018/4/10. */

public interface SetResultCallBack {
    public abstract void onLike(boolean isLike);

    public abstract void onCollect(boolean isCollect);

    public abstract void onShare(boolean isShare);
}
