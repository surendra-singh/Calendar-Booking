/**
 * 
 */
package com.surendra.calendar.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.surendra.calendar.entity.Booking;
import com.surendra.calendar.entity.Organisation;
import com.surendra.calendar.entity.Schedule;
import com.surendra.calendar.enums.BookingStatus;

/**
 * @author surendra.singh
 *
 */
public class Util {

	public static final SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

	public static final SimpleDateFormat HH_COLON_MM = new SimpleDateFormat("HH:mm");

	private static final String INPUT_FILE = "/home/surendra/TestSample.txt";

	private static final String OUTPUT_FILE = "/home/surendra/SampleOutput.txt";

	private static Organisation organisation;
	
	public static boolean isValidString(String string) {
		return string != null && !string.trim().equals("");
	}

	public static Map<Organisation, Set<Booking>> readFile(String inputPath, String outputPath) throws IOException {
		organisation = null;
		
		Stream<String> stream = Files.lines(FileSystems.getDefault().getPath(Util.isValidString(inputPath) ? inputPath : INPUT_FILE), UTF_8);
		Map<Organisation, Set<Booking>> bookingMap = new HashMap<Organisation, Set<Booking>>();

		Pattern pattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9].*");
		stream.forEach(string -> {
			if (isValidString(string)){
				try {
					Matcher matcher = pattern.matcher(string);
					if (matcher.matches()) {
						organisation = createNewOrganisation(string);
					} else {
						if (organisation == null) {
							generateError(outputPath);
						} else {
							if (bookingMap.get(organisation) == null) {
								Set<Booking> bookingSet = new TreeSet<Booking>();
								bookingSet.add(createBookingInstance(string));
								bookingMap.put(organisation, bookingSet);
							} else {
								bookingMap.get(organisation).add(createBookingInstance(string));
							}
						}
					}
				} catch (ParseException pe) {
					generateError(outputPath);
				}
			}
		});
		stream.close();
		return bookingMap;
	}

	private static Organisation createNewOrganisation(String string) throws ParseException {
		String[] arr = string.split(" ");
		if (arr.length != 3) {
			throw new ParseException("Parsing Exception", 1);
		}
		Organisation organisation = new Organisation(arr[2]);
		organisation.setStartTime(HH_COLON_MM.parse(arr[0]).getTime());
		organisation.setEndTime(HH_COLON_MM.parse(arr[1]).getTime());
		return organisation;
	}

	public static void writeToFile(Map<Organisation, Set<Booking>> bookingMap, String outputPath) {
		try (BufferedWriter writer = Files.newBufferedWriter(FileSystems.getDefault().getPath(Util.isValidString(outputPath) ? outputPath : OUTPUT_FILE), UTF_8)) {
			for (Organisation organisation : bookingMap.keySet()) {
				writer.write(organisation.getName());
				writer.write("\n\n");

				List<Booking> bookingList = new ArrayList<Booking>(bookingMap.get(organisation));
				Collections.sort(bookingList, new Comparator<Booking>() {
					@Override
					public int compare(Booking o1, Booking o2) {
						return (int) (o1.getSchedule().getStartTime() - o2.getSchedule().getStartTime());
					}
				});
				for (Booking booking : bookingList) {
					writer.write(YYYY_MM_DD.format(new Date(booking.getSchedule().getStartTime())));
					writer.write(" ");
					writer.write(HH_COLON_MM.format(new Date(booking.getSchedule().getStartTime())));
					writer.write(" ");
					writer.write(HH_COLON_MM.format(new Date(booking.getSchedule().getEndTime())));
					writer.write(" " + booking.getEmployeeId() + " " + booking.getStatus().name() + "\n\n");
				}
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
	}

	private static void generateError(String path) {
		try (BufferedWriter writer = Files.newBufferedWriter(FileSystems.getDefault().getPath(Util.isValidString(path) ? path : OUTPUT_FILE), UTF_8)) {
			writer.write("INVALID INPUT");
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		System.exit(0);
	}

	private static Booking createBookingInstance(String string) throws ParseException {
		Booking booking = new Booking();

		String[] arr = string.split(" ");
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
}
