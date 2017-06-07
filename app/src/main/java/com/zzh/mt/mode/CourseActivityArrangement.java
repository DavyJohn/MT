package com.zzh.mt.mode;

import java.util.LinkedList;

/**
 * Created by 腾翔信息 on 2017/6/3.
 */

public class CourseActivityArrangement {

    private String message;
    private String code;
    private LinkedList<activityListData> activityList;

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

    public LinkedList<activityListData> getActivityList() {
        return activityList;
    }

    public void setActivityList(LinkedList<activityListData> activityList) {
        this.activityList = activityList;
    }

    public class activityListData{
        private String id;
        private String groupName;
        private String code;
        private String startTime;
        private String endTime;
        private String creator;
        private String updater;
        private String createTime;
        private String updateTime;
        private String isDisabled;
        private String curriculumNoId;
        private String type;
        private String colorLabel;
        private String activityTypeName;
        private String groupId;
        private String updatrt;
        private String hasRemark;

        public String getColorLabel() {
            return colorLabel;
        }

        public void setColorLabel(String colorLabel) {
            this.colorLabel = colorLabel;
        }

        public String getHasRemark() {
            return hasRemark;
        }

        public void setHasRemark(String hasRemark) {
            this.hasRemark = hasRemark;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public String getCurriculumNoId() {
            return curriculumNoId;
        }

        public void setCurriculumNoId(String curriculumNoId) {
            this.curriculumNoId = curriculumNoId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getUpdatrt() {
            return updatrt;
        }

        public void setUpdatrt(String updatrt) {
            this.updatrt = updatrt;
        }
    }
}
