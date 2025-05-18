package com.ren.common.constant;

public class AppConstants {

    /*===================================================通用开始=======================================================*/
    /**通用-是*/
    public static final byte COMMON_BYTE_YES = 1;
    /**通用-否*/
    public static final byte COMMON_BYTE_NO = 0;

    /**通用-是*/
    public static final boolean COMMON_BOOLEAN_YES = true;
    /**通用-否*/
    public static final boolean COMMON_BOOLEAN_NO = false;
    /*===================================================通用结束=======================================================*/


    /*===================================================用户开始=======================================================*/
    /**用户-性别-男*/
    public static final byte USER_SEX_MAN = 0;
    /**用户-性别-女*/
    public static final byte USER_SEX_WOMAN = 1;
    /**用户-性别-未知*/
    public static final byte USER_SEX_UNKNOWN = 2;

    /**用户-用户类型-系统用户*/
    public static final String USER_USERTYPE_SYSTEM = "00";
    /*===================================================用户结束=======================================================*/


    /*===================================================菜单开始=======================================================*/
    /**菜单-菜单类型-目录*/
    public static final String MENU_TYPE_DIR = "M";
    /**菜单-菜单类型-菜单*/
    public static final String MENU_TYPE_MENU = "C";
    /**菜单-菜单类型-按钮*/
    public static final String MENU_TYPE_BUTTON = "F";
    /*===================================================菜单结束=======================================================*/


    /*===================================================角色开始=======================================================*/
    /**角色-超级管理员角色名称*/
    public static final String ROLE_SUPER_KEY = "admin";

    /**角色-可查看数据范围-全部数据权限*/
    public static final byte DATA_SCOPE_ALL = 1;
    /**角色-可查看数据范围-自定数据权限*/
    public static final byte DATA_SCOPE_CUSTOMIZE = 2;
    /**角色-可查看数据范围-本部门数据权限*/
    public static final byte DATA_SCOPE_THIS_DEPARTMENT = 3;
    /**角色-可查看数据范围-本部门及以下数据权限*/
    public static final byte DATA_SCOPE_THIS_DEPARTMENT_AND_BELOW = 4;
    /**角色-可查看数据范围-仅本人数据权限*/
    public static final byte DATA_SCOPE_MYSELF = 5;
    /*===================================================角色结束=======================================================*/


    /*===================================================公告开始=======================================================*/
    /**通知公告-公告类型-通知*/
    public static final byte NOTICE_TYPE_NOTICE = 1;
    /**通知公告-公告类型-公告*/
    public static final byte NOTICE_TYPE_BULLETIN = 2;
    /*===================================================公告结束=======================================================*/


    /*===================================================操作日志开始====================================================*/
    /**操作日志-业务类型-其它*/
    public static final byte BUSINESS_TYPE_OTHER = 0;
    /**操作日志-业务类型-新增*/
    public static final byte BUSINESS_TYPE_ADD = 1;
    /**操作日志-业务类型-修改*/
    public static final byte BUSINESS_TYPE_MODIFY = 2;
    /**操作日志-业务类型-删除*/
    public static final byte BUSINESS_TYPE_REMOVE = 3;

    /**操作日志-操作类别-其它*/
    public static final byte OPERATOR_TYPE_OTHER = 0;
    /**操作日志-操作类别-后台用户*/
    public static final byte OPERATOR_TYPE_BACK = 1;
    /**操作日志-操作类别-手机端用户*/
    public static final byte OPERATOR_TYPE_MOBILE = 2;
    /*==================================================操作日志结束=====================================================*/

}