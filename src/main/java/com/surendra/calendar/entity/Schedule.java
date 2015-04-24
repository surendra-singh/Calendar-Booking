/**
 * 
 */
package com.surendra.calendar.entity;


/**
 * @author surendra.singh
 *
 */
public class Schedule {

	private long startTime;
	
	private long endTime;

	public Schedule() {
		super();
	}
	
	public Schedule(long startTime, long endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * @param scheduleSet
	 * @return
	 */
	public boolean isScheduleConflict(final Schedule schedule) {
		if (this.getStartTime() >= schedule.getStartTime() && this.getStartTime() < schedule.getEndTime()) {
			return true;
		} else if (this.getEndTime() > schedule.getStartTime() && this.getEndTime() <= schedule.getEndTime()) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (endTime ^ (endTime >>> 32));
		result = prime * result + (int) (startTime ^ (startTime >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Schedule other = (Schedule) obj;
		if (endTime != other.endTime)
			return false;
		if (startTime != other.startTime)
			return false;
		return true;
	}
}
