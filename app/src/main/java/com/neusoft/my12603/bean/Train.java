package com.neusoft.my12603.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by star on 2016/9/28.
 */
public class Train implements Serializable{
    private String trainNo;
    private String startStationName;
    private String endStationName;
    private String fromStationName;
    private String startTime;
    private String arriveTime;
    private String toStationName;
    private int dayDifference;
    private String durationTime;
    private String startTrainDate;
    private Map<String, Seat> seats;

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public Map<String, Seat> getSeats() {
        return seats;
    }

    public void setSeats(Map<String, Seat> seats) {
        this.seats = seats;
    }

    public String getStartTrainDate() {
        return startTrainDate;
    }

    public void setStartTrainDate(String startTrainDate) {
        this.startTrainDate = startTrainDate;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public int getDayDifference() {
        return dayDifference;
    }

    public void setDayDifference(int dayDifference) {
        this.dayDifference = dayDifference;
    }

    public String getToStationName() {
        return toStationName;
    }

    public void setToStationName(String toStationName) {
        this.toStationName = toStationName;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFromStationName() {
        return fromStationName;
    }

    public void setFromStationName(String fromStationName) {
        this.fromStationName = fromStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    @Override
    public String toString() {
        return "Train [trainNo=" + trainNo + ", startStationName="
                + startStationName + ", endStationName=" + endStationName
                + ", fromStationName=" + fromStationName + ", startTime="
                + startTime + ", arriveTime=" + arriveTime + ", toStationName="
                + toStationName + ", dayDifference=" + dayDifference
                + ", durationTime=" + durationTime + ", startTrainDate="
                + startTrainDate + ", seats=" + seats + "]";
    }
}
