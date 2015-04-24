/**
 * 
 */
package com.surendra.calendar.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


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
		return new HashCodeBuilder(17, 37).append(this.startTime).append(this.endTime).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Schedule rhs = (Schedule) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj))
				.append(this.startTime, rhs.startTime).append(this.endTime, rhs.endTime).isEquals();
	}
}
