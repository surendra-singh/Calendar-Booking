/**
 * 
 */
package com.surendra.calendar.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.surendra.calendar.entity.Booking;
import com.surendra.calendar.entity.Organisation;
import com.surendra.calendar.entity.Schedule;
import com.surendra.calendar.enums.BookingStatus;
import com.surendra.calendar.util.Util;

/**
 * @author surendra.singh
 *
 */
public class BookingService {
	
	public static void main(String[] args) throws IOException {
		
		Map<Organisation, Set<Booking>> bookingMap = Util.readFile(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null);

		bookMeetings(bookingMap, args.length > 1 ? args[1] : null);
	}

	/**
	 * 
	 * @param bookingMap
	 * @param outputPath
	 */
	private static void bookMeetings(Map<Organisation, Set<Booking>> bookingMap, String outputPath) {
		for (Organisation organisation : bookingMap.keySet()) {
			Set<Schedule> approvedSet = new HashSet<Schedule>();
			for (Booking booking : bookingMap.get(organisation)) {
				if (isBookingInvalid(organisation, booking) ){//Outside office timings 
					booking.setStatus(BookingStatus.INVALID);
				} else if (isScheduleConflict(approvedSet, booking.getSchedule())){ //oVERLAP
					booking.setStatus(BookingStatus.PENDING);
				} else {
					booking.setStatus(BookingStatus.APPROVED);//BOOKED
					approvedSet.add(booking.getSchedule());
				}
			}
		}
		
		Util.writeToFile(bookingMap, outputPath);
		System.out.println("Meeting Scheduled Successfully.....................");
	}

	private static boolean isScheduleConflict(Set<Schedule> scheduleSet, Schedule scheduleToCheck) {
		for (Schedule schedule : scheduleSet) {//ALRADY BOOKED SLOTS
			if (scheduleToCheck.getStartTime() >= schedule.getStartTime() 
					&& scheduleToCheck.getStartTime() < schedule.getEndTime()) {
				return true;
			} else if (scheduleToCheck.getEndTime() > schedule.getStartTime() 
					&& scheduleToCheck.getEndTime() <= schedule.getEndTime()) {
				return true;
			}
		}
		return false;
	}

	private static boolean isBookingInvalid(Organisation organisation, Booking booking) {
		Calendar officeStart = Calendar.getInstance(); officeStart.setTimeInMillis(organisation.getStartTime());
		Calendar meetingStartTime = Calendar.getInstance();	meetingStartTime.setTimeInMillis(booking.getSchedule().getStartTime());
		
		if (officeStart.get(Calendar.HOUR_OF_DAY) <= meetingStartTime.get(Calendar.HOUR_OF_DAY)) {
			Calendar officeEnd = Calendar.getInstance(); officeEnd.setTimeInMillis(organisation.getEndTime());
			Calendar meetingEndTime = Calendar.getInstance();meetingEndTime.setTimeInMillis(booking.getSchedule().getEndTime());
			if (officeEnd.get(Calendar.HOUR_OF_DAY) < meetingEndTime.get(Calendar.HOUR_OF_DAY)) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}
}
