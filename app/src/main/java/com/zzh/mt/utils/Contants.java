package com.zzh.mt.utils;

/**
 * Created by 腾翔信息 on 2017/5/16.
 */

public class Contants {
    //首页轮播url api
    public static final String BASEURL = "http://192.168.6.17:8802";
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
    //查询已选课程列表
    public static final String CurriculumChoice = "/mtms-gateway/appCurriculum/getCurriculumChoice";
    //查询可选选修课程列表
    public static final String CurriculumNoChoice = "/mtms-gateway/appCurriculum/getCurriculumNoChoice";
    //查询课程详情
    public static final String CurriculumDetails= "/mtms-gateway/appCurriculum/getCurriculumDetails";
    //查询课程培训场次
    public static final String CoursesTrainingSessions= "/mtms-gateway/appCurriculum/queryCoursesTrainingSessions";
    //修改课程培训场次
    public static final String CurriculumNo = "/mtms-gateway/appCurriculum/editCurriculumNo";
    //查询已选场次列表
    public static final String CurriculumNoByUserId = "/mtms-gateway/appCurriculum/getCurriculumNoByUserId";
    //查询课程活动安排
    public static final String CourseActivityArrangement = "/mtms-gateway/appActivity/queryCourseActivityArrangement";
    //查询活动备注
    public static final String AppRemarksByGroupId = "/mtms-gateway/appRemarks/getAppRemarksByGroupId";
    //修改活动备注
    public static final String ReditRemarks ="/mtms-gateway/appRemarks/editRemarks";
    //查询所属小组活动信息
    public static final String GroupActivityInformation = "/mtms-gateway/appActivity/queryGroupActivityInformation";
    //查询课程资料列表
    public static final String getCoursewareById = "/mtms-gateway/appCurriculum/getCoursewareById";
    //查询我的备注列表
    public static final String AppRemarks = "/mtms-gateway/appRemarks/getAppRemarks";

    //编辑界面
    public static String Deparmentid = null;
    public static String Deparmentname = null;
    //语言问题
    public static int LANGUAGENEM = 0;
}
