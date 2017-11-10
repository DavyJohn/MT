package com.zzh.mt.mode;

import java.util.LinkedList;

/**
 * Created by 腾翔信息 on 2017/6/3.
 */

public class CurriculumNoByUser {

    private String message;
    private String code;
    private LinkedList<courseNoListData> courseNoList;

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

    public LinkedList<courseNoListData> getCourseNoList() {
        return courseNoList;
    }

    public void setCourseNoList(LinkedList<courseNoListData> courseNoList) {
        this.courseNoList = courseNoList;
    }

    public class courseNoListData{
        private String flag;
        private String id;
        private String curriculumId;
        private String remarksId;
        private String groupId;
        private String attendTime;
        private String attendEndTime;
        private String attendPlace;
        private String creator;
        private String updater;
        private String isDisabled;
        private String createTime;
        private String updateTime;
        private String chineseName;
        private String englishName;
        private String pictureUrl;
        private String isArrange;
        private String classHours;
        private String isSelected;
        private String remainingSeats;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCurriculumId() {
            return curriculumId;
        }

        public void setCurriculumId(String curriculumId) {
            this.curriculumId = curriculumId;
        }

        public String getRemarksId() {
            return remarksId;
        }

        public void setRemarksId(String remarksId) {
            this.remarksId = remarksId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
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

        public String getAttendPlace() {
            return attendPlace;
        }

        public void setAttendPlace(String attendPlace) {
            this.attendPlace = attendPlace;
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

        public String getIsDisabled() {
            return isDisabled;
        }

        public void setIsDisabled(String isDisabled) {
            this.isDisabled = isDisabled;
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

        public String getPictureUrl() {
            return pictureUrl;
        }

        public void setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
        }

        public String getIsArrange() {
            return isArrange;
        }

        public void setIsArrange(String isArrange) {
            this.isArrange = isArrange;
        }

        public String getClassHours() {
            return classHours;
        }

        public void setClassHours(String classHours) {
            this.classHours = classHours;
        }

        public String getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(String isSelected) {
            this.isSelected = isSelected;
        }

        public String getRemainingSeats() {
            return remainingSeats;
        }

        public void setRemainingSeats(String remainingSeats) {
            this.remainingSeats = remainingSeats;
        }
    }
}
