package com.zzh.mt.utils;

/**
 * Created by 腾翔信息 on 2017/5/16.
 */

public class Contants {
    //首页轮播url api
    public static final String BASEURL = "http://192.168.6.22:8080";
    public static final String  BASEBANNERURL = "http://bangumi.bilibili.com/api/";
    public static final String BANNERURL = "app_index_page_v4?build=3940&device=phone&mobi_app=iphone&platform=ios";

    //登录
    public static final String LOGIN = "/mtms-gateway/appUser/userLogin";
    //登出
    public static final String LOGOUT = "/mtms-gateway/appUser/userLogout";
    //忘记密码
    public static final String FORGETPASSWORD = "/mtms-gateway/appUser/forgetUserLoginPwdEmail";
    //修改密码
    public static final String CHANGEPASSWORD = "/mtms-gateway/appUser/modUserLoginPwd";
    //修改个人资料
    public static final String CHANGEINFO = "/mtms-gateway/appUser/modifyPersonalData";
    //查询个人资料
    public static final String GETUSER = "/mtms-gateway/appUser/getUserById";
    //查询我的同期学员列表
    public static final String CLASSMATE = "/mtms-gateway/appUser/getClassmate";
    public static int LANGUAGENEM = 0;
}
