package com.zzh.mt.mode;

import java.util.LinkedList;

/**
 * Created by 腾翔信息 on 2017/5/15.
 */

public class BannerEntity {
    private String code;
    private String message;
    private ResultData result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultData getResult() {
        return result;
    }

    public void setResult(ResultData result) {
        this.result = result;
    }

    public class ResultData{
        private AdData ad;

        public AdData getAd() {
            return ad;
        }

        public void setAd(AdData ad) {
            this.ad = ad;
        }
    }

    public class AdData{
        private LinkedList<Head> head;

        public LinkedList<Head> getHead() {
            return head;
        }

        public void setHead(LinkedList<Head> head) {
            this.head = head;
        }
    }

    public class Head{
        private String id;
        private String img;
        private String link;
        private String pub_time;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getPub_time() {
            return pub_time;
        }

        public void setPub_time(String pub_time) {
            this.pub_time = pub_time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
