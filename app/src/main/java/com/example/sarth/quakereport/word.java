package com.example.sarth.quakereport;

/**
 * Created by sarth on 8/23/2017.
 */

public class word {
    private double mag;
    private String place;
    private long time;

    word(double mag,String place,long time)
    {
        this.mag=mag;
        this.place=place;
        this.time=time;
    }

    public double getmag()
    {
        return mag;
    }

    public String getplace()
    {
        return place;
    }

    public long gettime()
    {
        return time;
    }
}
