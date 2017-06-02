package com.zzh.mt.mode;

import java.util.LinkedList;

/**
 * Created by 腾翔信息 on 2017/6/2.
 */

public class CurriculumData {

    private String message;
    private String code;
    private LinkedList<Curriculum> courseList;

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

    public LinkedList<Curriculum> getCourseList() {
        return courseList;
    }

    public void setCourseList(LinkedList<Curriculum> courseList) {
        this.courseList = courseList;
    }

    public class Curriculum{
        private String id;
        private String chineseName;
        private String englishName;
        private String introduce;
        private String details;
        private String coursewareId;
        private String pictureUrl;
        private String classHours;
        private String type;
        private String creator;
        private String updater;
        private String createTime;
        private String updateTime;
        private String isDisabled;
        private String canModify;
        private String curriculumNo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getChineseName() {
            return chineseName;
        }

        public void setChineseName(String chineseName) {
            this.chineseName = chineseName;
        }

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getCoursewareId() {
            return coursewareId;
        }

        public void setCoursewareId(String coursewareId) {
            this.coursewareId = coursewareId;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getClassHours() {
            return classHours;
        }

        public void setClassHours(String classHours) {
            this.classHours = classHours;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getIsDisabled() {
            return isDisabled;
        }

        public void setIsDisabled(String isDisabled) {
            this.isDisabled = isDisabled;
        }

        public String getCanModify() {
            return canModify;
        }

        public void setCanModify(String canModify) {
            this.canModify = canModify;
        }

        public String getCurriculumNo() {
            return curriculumNo;
        }

        public void setCurriculumNo(String curriculumNo) {
            this.curriculumNo = curriculumNo;
        }
    }
}
