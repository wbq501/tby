package com.baigu.dms.domain.model;

import java.io.Serializable;
import java.util.List;

public class Coupon implements Serializable{

    public static class Status {
        public static final int NO_USE = 1;
        public static final int YES_USE = 2;
        public static final int BE_OVERDUE = 3;
    }

    private int totalRow;
    private int pageSize;
    private int pageNumber;
    private int totalPage;
    private boolean firstPage;
    private boolean lastPage;
    private List<ListBean> list;

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        private CouponBean coupon;
        private CouponUserBean couponUser;

        public CouponBean getCoupon() {
            return coupon;
        }

        public void setCoupon(CouponBean coupon) {
            this.coupon = coupon;
        }

        public CouponUserBean getCouponUser() {
            return couponUser;
        }

        public void setCouponUser(CouponUserBean couponUser) {
            this.couponUser = couponUser;
        }

        public static class CouponBean {

            private String id;
            private String name;
            private int rule;
            private int lowestMoney;
            private int rewardMoney;
            private String createBy;
            private long createDate;
            private String updateBy;
            private long updateDate;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getRule() {
                return rule;
            }

            public void setRule(int rule) {
                this.rule = rule;
            }

            public int getLowestMoney() {
                return lowestMoney;
            }

            public void setLowestMoney(int lowestMoney) {
                this.lowestMoney = lowestMoney;
            }

            public int getRewardMoney() {
                return rewardMoney;
            }

            public void setRewardMoney(int rewardMoney) {
                this.rewardMoney = rewardMoney;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public String getUpdateBy() {
                return updateBy;
            }

            public void setUpdateBy(String updateBy) {
                this.updateBy = updateBy;
            }

            public long getUpdateDate() {
                return updateDate;
            }

            public void setUpdateDate(long updateDate) {
                this.updateDate = updateDate;
            }
        }

        public static class CouponUserBean {
            private String id;
            private String couponId;
            private long startDate;
            private long endDate;
            private String memberId;
            private int status;
            private long createDate;
            private long updateDate;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCouponId() {
                return couponId;
            }

            public void setCouponId(String couponId) {
                this.couponId = couponId;
            }

            public long getStartDate() {
                return startDate;
            }

            public void setStartDate(long startDate) {
                this.startDate = startDate;
            }

            public long getEndDate() {
                return endDate;
            }

            public void setEndDate(long endDate) {
                this.endDate = endDate;
            }

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public long getUpdateDate() {
                return updateDate;
            }

            public void setUpdateDate(long updateDate) {
                this.updateDate = updateDate;
            }
        }
    }
}
