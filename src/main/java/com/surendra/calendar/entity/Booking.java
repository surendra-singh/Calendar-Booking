/**
 * 
 */
package com.surendra.calendar.entity;

import static com.surendra.calendar.util.Util.YYYY_MM_DD_HH_MM;
import static com.surendra.calendar.util.Util.YYYY_MM_DD_HH_MM_SS;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.CompareToBuilder;

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
	 * @param bookingString
	 * @return
	 * @throws ParseException
	 */
	public static Booking getInstance(final String bookingString) throws ParseException {
		final Booking booking = new Booking();

		String[] arr = bookingString.split(" ");
		if (arr.length != 6) {
			throw new ParseException("Not a valid formate", 1);
		}
		booking.setRequestedTime(YYYY_MM_DD_HH_MM_SS.parse(arr[0] + " " + arr[1]).getTime());

		String employeeId = arr[2];
		if (!Pattern.compile("EMP[0-9]{3}").matcher(employeeId).matches()) {
			throw new ParseException("Employee Id not in pattern", 1);
		}
		booking.setEmployeeId(employeeId);

		Schedule schedule = new Schedule();
		schedule.setStartTime(YYYY_MM_DD_HH_MM.parse(arr[3] + " " + arr[4]).getTime());
		try {
			int duration = Integer.valueOf(arr[5]);
			if (duration <= 0) {
				throw new ParseException("Not valid meeting duration", 1);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(schedule.getStartTime());
			calendar.add(Calendar.HOUR_OF_DAY, duration);
			schedule.setEndTime(calendar.getTimeInMillis());
		} catch (NumberFormatException nfe) {
			throw new ParseException("Not able to parse meeting duration", 1);
		}
		booking.setSchedule(schedule);
		booking.setStatus(BookingStatus.APPLIED);

		return booking;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Booking o) {
		if (o == null) {
			return -1;
		}
		return new CompareToBuilder().append(this.requestedTime, o.requestedTime).toComparison();
	}
}
