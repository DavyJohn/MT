package com.zzh.mt.mode;

import java.util.LinkedList;

/**
 * Created by 腾翔信息 on 2017/5/31.
 */

public class ClassMateData {

    private String message;
    private String code;
    private LinkedList<PersonListData> personList;

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

    public LinkedList<PersonListData> getPersonList() {
        return personList;
    }

    public void setPersonList(LinkedList<PersonListData> personList) {
        this.personList = personList;
    }

    public class PersonListData{
        private String id;
        private String englishName;
        private String chineseName;
        private String nickName;
        private String sex;
        private String initial;
        private String birthday;
        private String brandName;
        private String departmentId;
        private String companyEmail;
        private String state;
        private String headUrl;
        private String password;
        private String entryYear;
        private String creator;
        private String createTime;
        private String updater;
        private String updateTime;
        private String haveClassHoursElective;
        private String haveClassHoursRequired;
        private Department department;
        private String validataCode;
        private String registerDate;

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(Department department) {
            this.department = department;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public String getChineseName() {
            return chineseName;
        }

        public void setChineseName(String chineseName) {
            this.chineseName = chineseName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getInitial() {
            return initial;
        }

        public void setInitial(String initial) {
            this.initial = initial;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        public String getCompanyEmail() {
            return companyEmail;
        }

        public void setCompanyEmail(String companyEmail) {
            this.companyEmail = companyEmail;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEntryYear() {
            return entryYear;
        }

        public void setEntryYear(String entryYear) {
            this.entryYear = entryYear;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdater() {
            return updater;
        }

        public void setUpdater(String updater) {
            this.updater = updater;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getHaveClassHoursElective() {
            return haveClassHoursElective;
        }

        public void setHaveClassHoursElective(String haveClassHoursElective) {
            this.haveClassHoursElective = haveClassHoursElective;
        }

        public String getHaveClassHoursRequired() {
            return haveClassHoursRequired;
        }

        public void setHaveClassHoursRequired(String haveClassHoursRequired) {
            this.haveClassHoursRequired = haveClassHoursRequired;
        }

        public String getValidataCode() {
            return validataCode;
        }

        public void setValidataCode(String validataCode) {
            this.validataCode = validataCode;
        }

        public String getRegisterDate() {
            return registerDate;
        }

        public void setRegisterDate(String registerDate) {
            this.registerDate = registerDate;
        }

        public class Department{
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
}
