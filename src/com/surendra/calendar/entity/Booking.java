/**
 * 
 */
package com.surendra.calendar.entity;

import com.surendra.calendar.enums.BookingStatus;

/**
 * @author surendra.singh
 *
 */
public class Booking implements Comparable<Booking> {

	private String employeeId;
	
	private long requestedTime;
	
	private Schedule schedule;
	
	private BookingStatus status;

	public Booking() {
		super();
		this.schedule = new Schedule();
	}
	
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public long getRequestedTime() {
		return requestedTime;
	}

	public void setRequestedTime(long requestedTime) {
		this.requestedTime = requestedTime;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}
	
	@Override
	public int compareTo(Booking o) {
		if (o == null) {
			return -1;
		}
		return this.requestedTime == o.requestedTime ? 0 : this.requestedTime < o.requestedTime ? -1 : 1;
	}
}
