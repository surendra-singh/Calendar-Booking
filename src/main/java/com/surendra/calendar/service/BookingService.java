/**
 * 
 */
package com.surendra.calendar.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.surendra.calendar.entity.Booking;
import com.surendra.calendar.entity.Organisation;
import com.surendra.calendar.enums.BookingStatus;
import com.surendra.calendar.util.Util;

/**
 * @author surendra.singh
 *
 */
public class BookingService {
	
	public static void main(String[] args) throws IOException {
		
		if (args.length == 0) {
			System.out.println("Invalid number of arguments. Please provide input file name...");
			System.exit(0);
		}
		
		Map<Organisation, Set<Booking>> bookingMap = Util.readFile(args[0], args.length > 1 ? args[1] : null);
		bookMeetings(bookingMap, args.length > 1 ? args[1] : null);
	}

	/**
	 * 
	 * @param bookingMap
	 * @param outputPath
	 */
	private static void bookMeetings(final Map<Organisation, Set<Booking>> bookingMap, final String outputPath) {
		for (Organisation organisation : bookingMap.keySet()) {
			final Set<Booking> approvedBookings = new HashSet<Booking>();
			for (Booking booking : bookingMap.get(organisation)) {
				if (booking.isBookingOutsideOfficeHour(organisation.getStartTime(), organisation.getEndTime()) ){//Outside office timings 
					booking.setStatus(BookingStatus.INVALID);
				} else if (booking.isBookingConflict(approvedBookings)){ //oVERLAP
					booking.setStatus(BookingStatus.PENDING);
				} else {
					booking.setStatus(BookingStatus.APPROVED);//BOOKED
					approvedBookings.add(booking);
				}
			}
		}
		
		Util.writeToFile(bookingMap, outputPath);
		System.out.println("Meeting Scheduled Successfully.....................");
	}
}
