package com.example.xiaojun.shidianpad.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20.
 */

public class ChaXunBean {


    /**
     * createTime : 1508491239224
     * dtoResult : 0
     * modifyTime : 1508491239224
     * objects : [{"avatar":"","cardNumber":"","createTime":1501493821000,"department":"白云分公司","description":"","dtoResult":0,"gender":1,"id":10006619,"job_number":"44016429","modifyTime":1501493821000,"name":"孙付刚","oaCardNum":"1000002740","oaDepartmentId":"2A17BFA7-69C8-4B72-A3CF-E6E10D919787","oaEmployeeId":"32AD8564-FF15-4728-B389-19AF21E7FC9D","pageNum":0,"pageSize":0,"phone":"","photo_ids":"20170731/WvyO3FHK_0_138_10.jpeg","remark":"","sid":0,"status":1,"subject_type":0,"title":""}]
     * pageIndex : 0
     * pageNum : 1
     * pageSize : 10
     * sid : 0
     * totalNum : 1
     */

    private long createTime;
    private int dtoResult;
    private long modifyTime;
    private int pageIndex;
    private int pageNum;
    private int pageSize;
    private int sid;
    private int totalNum;
    private List<ObjectsBean> objects;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDtoResult() {
        return dtoResult;
    }

    public void setDtoResult(int dtoResult) {
        this.dtoResult = dtoResult;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<ObjectsBean> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectsBean> objects) {
        this.objects = objects;
    }

    public static class ObjectsBean {
        /**
         * avatar :
         * cardNumber :
         * createTime : 1501493821000
         * department : 白云分公司
         * description :
         * dtoResult : 0
         * gender : 1
         * id : 10006619
         * job_number : 44016429
         * modifyTime : 1501493821000
         * name : 孙付刚
         * oaCardNum : 1000002740
         * oaDepartmentId : 2A17BFA7-69C8-4B72-A3CF-E6E10D919787
         * oaEmployeeId : 32AD8564-FF15-4728-B389-19AF21E7FC9D
         * pageNum : 0
         * pageSize : 0
         * phone :
         * photo_ids : 20170731/WvyO3FHK_0_138_10.jpeg
         * remark :
         * sid : 0
         * status : 1
         * subject_type : 0
         * title :
         */

        private String avatar;
        private String cardNumber;
        private long createTime;
        private String department;
        private String description;
        private int dtoResult;
        private int gender;
        private int id;
        private String job_number;
        private long modifyTime;
        private String name;
        private String oaCardNum;
        private String oaDepartmentId;
        private String oaEmployeeId;
        private int pageNum;
        private int pageSize;
        private String phone;
        private String photo_ids;
        private String remark;
        private int sid;
        private int status;
        private int subject_type;
        private String title;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getDtoResult() {
            return dtoResult;
        }

        public void setDtoResult(int dtoResult) {
            this.dtoResult = dtoResult;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getJob_number() {
            return job_number;
        }

        public void setJob_number(String job_number) {
            this.job_number = job_number;
        }

        public long getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(long modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOaCardNum() {
            return oaCardNum;
        }

        public void setOaCardNum(String oaCardNum) {
            this.oaCardNum = oaCardNum;
        }

        public String getOaDepartmentId() {
            return oaDepartmentId;
        }

        public void setOaDepartmentId(String oaDepartmentId) {
            this.oaDepartmentId = oaDepartmentId;
        }

        public String getOaEmployeeId() {
            return oaEmployeeId;
        }

        public void setOaEmployeeId(String oaEmployeeId) {
            this.oaEmployeeId = oaEmployeeId;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhoto_ids() {
            return photo_ids;
        }

        public void setPhoto_ids(String photo_ids) {
            this.photo_ids = photo_ids;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSubject_type() {
            return subject_type;
        }

        public void setSubject_type(int subject_type) {
            this.subject_type = subject_type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
