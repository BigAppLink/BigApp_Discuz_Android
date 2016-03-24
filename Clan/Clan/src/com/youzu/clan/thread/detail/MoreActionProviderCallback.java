package com.youzu.clan.thread.detail;

/**
 * Created by tangh on 2015/10/22.
 */
public interface MoreActionProviderCallback {
    boolean isDoNext(String doWhat);

    boolean isHaveShare();
    boolean isHaveFav();
    boolean isHaveJumpPage();
    boolean isHaveJumpPost();
    boolean isHaveReport();
    boolean isHaveDelete();
    boolean isFav();
    void doShare();
    void delFav();
    void addFav();
    void doJumpPage();
    void report();
    void doJumpPost();
    void delete();

    String getShareUrl();
}
