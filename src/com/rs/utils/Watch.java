package com.rs.utils;

public class Watch {

    private long start;

    public void set() {
    	setStart(System.nanoTime());
    }

    public long elapsed() {
    	return System.nanoTime() - getStart();
    }

    public long elapsedMillis() {
    	return elapsed() / 1000000;
    }

    public long elapsedSeconds() {
    	return elapsed() / 1000000000L;
    }

    public double elapsedSecondsD() {
    	return (double) (System.nanoTime() - (double) getStart());
    }

    public void end() {
    	setStart(-1);
    }

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}
}