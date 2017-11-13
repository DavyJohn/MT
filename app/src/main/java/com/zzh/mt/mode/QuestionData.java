package com.zzh.mt.mode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 腾翔信息 on 2017/11/10.
 */

public class QuestionData {
    private String code;
    private List<QInfo> problems;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<QInfo> getProblems() {
        return problems;
    }

    public void setProblems(List<QInfo> problems) {
        this.problems = problems;
    }

    public class QInfo{
        private String id;
        private String problemTitle;
        private String problemContent;
        private String integral;
        private String userId;
        private String createTime;
        private String isAnswer;
        private List<AnswerData> answers;
        private String nickName;
        private String headUrl;
        private String sex;
        private String isFlag;
        private String isMyProblem;

        public List<AnswerData> getAnswers() {
            return answers;
        }

        public void setAnswers(List<AnswerData> answers) {
            this.answers = answers;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProblemTitle() {
            return problemTitle;
        }

        public void setProblemTitle(String problemTitle) {
            this.problemTitle = problemTitle;
        }

        public String getProblemContent() {
            return problemContent;
        }

        public void setProblemContent(String problemContent) {
            this.problemContent = problemContent;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getIsAnswer() {
            return isAnswer;
        }

        public void setIsAnswer(String isAnswer) {
            this.isAnswer = isAnswer;
        }


        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getIsFlag() {
            return isFlag;
        }

        public void setIsFlag(String isFlag) {
            this.isFlag = isFlag;
        }

        public String getIsMyProblem() {
            return isMyProblem;
        }

        public void setIsMyProblem(String isMyProblem) {
            this.isMyProblem = isMyProblem;
        }
    }
    public class  AnswerData{
        private String id;
        private String answer;
        private String problemId    ;
        private String userId;
        private String createTime;
        private String flag;
        private String nickName;
        private String headUrl;
        private String sex;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getProblemId() {
            return problemId;
        }

        public void setProblemId(String problemId) {
            this.problemId = problemId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
