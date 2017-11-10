package com.zzh.mt.mode;

import java.util.LinkedList;

/**
 * Created by 腾翔信息 on 2017/6/5.
 */

public class AppRemarks {
    private String message;
    private String code;
    private LinkedList<remarkListData> remarkList;

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

    public LinkedList<remarkListData> getRemarkList() {
        return remarkList;
    }

    public void setRemarkList(LinkedList<remarkListData> remarkList) {
        this.remarkList = remarkList;
    }

    public class remarkListData{
        private String id;
        private String information;
        private String type;
        private String curriculumName;
        private String creator;
        private String updater;
        private String createTime;
        private String updateTime;
        private String curriculumNoId;
        private String isDisabled;
        private String chineseName;
        private String englishName;
        private String groupName;
        private String startTime;
        private String endTime;
        private String attendTime;
        private String attendEndTime;
        private String activityTypeName;
        private String groupId;
        private String activityId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCurriculumName() {
            return curriculumName;
        }

        public void setCurriculumName(String curriculumName) {
            this.curriculumName = curriculumName;
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

        public String getCurriculumNoId() {
            return curriculumNoId;
        }

        public void setCurriculumNoId(String curriculumNoId) {
            this.curriculumNoId = curriculumNoId;
        }

        public String getIsDisabled() {
            return isDisabled;
        }

        public void setIsDisabled(String isDisabled) {
            this.isDisabled = isDisabled;
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

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getAttendTime() {
            return attendTime;
        }

        public void setAttendTime(String attendTime) {
            this.attendTime = attendTime;
        }

        public String getAttendEndTime() {
            return attendEndTime;
        }

        public void setAttendEndTime(String attendEndTime) {
            this.attendEndTime = attendEndTime;
        }

        public String getActivityTypeName() {
            return activityTypeName;
        }

        public void setActivityTypeName(String activityTypeName) {
            this.activityTypeName = activityTypeName;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }
    }
}
