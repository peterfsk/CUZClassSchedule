package com.asus.zhechuankebiao.data.bean;

/**
 * Created by lalala on 18-12-24.
 * used in network
 */

public class CsItem {
    private CsName mCsName;
    private int count;

    public CsName getCsName() {
        return mCsName;
    }

    public CsItem setCsName(CsName csName) {
        mCsName = csName;
        return this;
    }

    public int getCount() {
        return count;
    }

    public CsItem setCount(int count) {
        this.count = count;
        return this;
    }
}
