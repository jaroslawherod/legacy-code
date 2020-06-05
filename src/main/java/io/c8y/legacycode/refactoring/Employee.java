package io.c8y.legacycode.refactoring;


public class Employee {
    private Type type;
    private double base;
    private double achievements;
    private double achievementsFactor;

    public Type getType() {
        return type;
    }

    enum Type {
        SALES, HR, WORKER, CEO
    }



    public void setType(Type type) {
        this.type = type;
    }

    public void setBase(double base) {
        this.base = base;
    }

    public double getBase() {
        return base;
    }

    public void setAchievements(double achievements) {
        this.achievements = achievements;
    }

    public double getAchievements() {
        return achievements;
    }


    public void setAchievementsFactor(double achievementsFactor) {
        this.achievementsFactor = achievementsFactor;
    }

    public double getAchievementsFactor() {
        return achievementsFactor;
    }
}