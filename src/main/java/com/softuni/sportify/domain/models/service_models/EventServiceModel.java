package com.softuni.sportify.domain.models.service_models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EventServiceModel extends BaseServiceModel {

    private SportServiceModel sport;
    private String level;
    private int floor;
    private String hall;
    private int dayOfMonth;
    private int month;
    private int year;
    private String startTime;
    private int maxCapacity;

    public EventServiceModel() {
    }

    @NotNull
    public SportServiceModel getSport() {
        return sport;
    }

    public void setSport(SportServiceModel sport) {
        this.sport = sport;
    }

    @NotNull
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @NotNull
    @Min(-2)
    @Max(10)
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @NotNull
    @Size(min = 2, max = 30)
    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    @NotNull
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    @NotNull
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @NotNull
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @NotNull
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @NotNull
    @Min(1)
    @Max(100)
    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
