package com.zzh.mt.mode;

import java.util.LinkedList;

/**
 * Created by 腾翔信息 on 2017/6/1.
 */

public class DepartmentData  {
    private String message;
    private String code;
    private LinkedList<DeList> departmentList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LinkedList<DeList> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(LinkedList<DeList> departmentList) {
        this.departmentList = departmentList;
    }

    public class DeList{
        private String id;
        private String departmentName;
        private String code;
        private String isDisabled;
        private String creator;
        private String updater;
        private String create_time;
        private String update_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getIsDisabled() {
            return isDisabled;
        }

        public void setIsDisabled(String isDisabled) {
            this.isDisabled = isDisabled;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getUpdater() {
            return updater;
        }

        public void setUpdater(String updater) {
            this.updater = updater;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }
}
