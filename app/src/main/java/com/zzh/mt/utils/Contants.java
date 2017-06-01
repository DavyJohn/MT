package com.zzh.mt.utils;

/**
 * Created by 腾翔信息 on 2017/5/16.
 */

public class Contants {
    //首页轮播url api
    public static final String BASEURL = "http://192.168.6.22:8080";
    //查询轮播图列表
    public static final String BANNERURL = "/mtms-gateway/appImage/getImage";

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
    //上传图片
    public static final String UPLOAD = "/mtms-gateway/appUser/upload";
    //查询部门列表
    public static final String DEPARTMENTLIST = "/mtms-gateway/appUser/departmentList";
    //查询我的课时目标
    public static final String CLASSGOALS = "/mtms-gateway/appCurriculum/queryClassGoals";
    //
    //编辑界面
    public static String Deparmentid = null;
    public static String Deparmentname = null;
    public static int LANGUAGENEM = 0;
}
