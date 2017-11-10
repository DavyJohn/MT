package com.zzh.mt.mode;

import java.util.LinkedList;

/**
 * Created by 腾翔信息 on 2017/6/5.
 */

public class CoursewareById {
    private String message;
    private String code;
    private LinkedList<CoursewareByIdData> fileList;

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

    public LinkedList<CoursewareByIdData> getFileList() {
        return fileList;
    }

    public void setFileList(LinkedList<CoursewareByIdData> fileList) {
        this.fileList = fileList;
    }

    public class CoursewareByIdData{
        private String id;
        private String coursewareName;
        private String coursewareUrl;
        private String coursewareSize;
        private String coursewareType;
        private String creator;
        private String updater;
        private String createTime;
        private String updateTime;
        private String isDisabled;
        private String curriculumId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoursewareName() {
            return coursewareName;
        }

        public void setCoursewareName(String coursewareName) {
            this.coursewareName = coursewareName;
        }

        public String getCoursewareUrl() {
            return coursewareUrl;
        }

        public void setCoursewareUrl(String coursewareUrl) {
            this.coursewareUrl = coursewareUrl;
        }

        public String getCoursewareSize() {
            return coursewareSize;
        }

        public void setCoursewareSize(String coursewareSize) {
            this.coursewareSize = coursewareSize;
        }

        public String getCoursewareType() {
            return coursewareType;
        }

        public void setCoursewareType(String coursewareType) {
            this.coursewareType = coursewareType;
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

        public String getCurriculumId() {
            return curriculumId;
        }

        public void setCurriculumId(String curriculumId) {
            this.curriculumId = curriculumId;
        }
    }
}
