/**
 * 
 */
package com.surendra.calendar.entity;

import java.util.Calendar;
import java.util.Set;

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
	
	/**
	 * @param officeStartTime
	 * @param officeEndTime
	 * @return
	 */
	public boolean isBookingOutsideOfficeHour(final long officeStartTime, final long officeEndTime) {
		Calendar officeStart = Calendar.getInstance(); officeStart.setTimeInMillis(officeStartTime);
		Calendar meetingStartTime = Calendar.getInstance();	meetingStartTime.setTimeInMillis(this.getSchedule().getStartTime());
		
		if (officeStart.get(Calendar.HOUR_OF_DAY) <= meetingStartTime.get(Calendar.HOUR_OF_DAY)) {
			Calendar officeEnd = Calendar.getInstance(); officeEnd.setTimeInMillis(officeEndTime);
			Calendar meetingEndTime = Calendar.getInstance();meetingEndTime.setTimeInMillis(this.getSchedule().getEndTime());
			if (officeEnd.get(Calendar.HOUR_OF_DAY) < meetingEndTime.get(Calendar.HOUR_OF_DAY)) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}
	
	/**
	 * @param scheduleSet
	 * @return
	 */
	public boolean isBookingConflict(final Set<Booking> bookings) {
		for (Booking booking : bookings) {
			if (this.getSchedule().isScheduleConflict(booking.getSchedule())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int compareTo(Booking o) {
		if (o == null) {
			return -1;
		}
		return this.requestedTime == o.requestedTime ? 0 : this.requestedTime < o.requestedTime ? -1 : 1;
	}
}
