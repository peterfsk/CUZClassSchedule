package com.asus.zhechuankebiao.data.bean;

import java.util.ArrayList;

/**
 * Created by lalala on 18-12-24.
 * used in network
 */

public class CourseTime {
    public ArrayList<String> years = new ArrayList<>();
    public ArrayList<String> terms = new ArrayList<>();
    public String selectYear;
    public String selectTerm;

    @Override
    public String toString() {
        return "CourseTime{" +
                "years=" + years +
                ", terms=" + terms +
                ", selectYear='" + selectYear + '\'' +
                ", selectTerm='" + selectTerm + '\'' +
                '}';
    }
}
