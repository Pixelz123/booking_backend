package com.booking.application.entites;

public enum PersonType{
    CHILD,
    ADULT,
    SENIOR;

    public static PersonType fromAge(int age){
        if (age<13)return CHILD;
        else if (age<60)return ADULT;
        else return SENIOR;
    }
}
