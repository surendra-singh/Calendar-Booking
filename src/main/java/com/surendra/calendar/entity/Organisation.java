/**
 * 
 */
package com.surendra.calendar.entity;

import static com.surendra.calendar.util.Util.HH_COLON_MM;

import java.text.ParseException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author surendra.singh
 *
 */
public class Organisation {

	private String name;

	private long startTime;

	private long endTime;

	public Organisation(String name) {
		super();
		this.name = name;
	}

	public static Organisation getInstance(final String string) throws ParseException {
		String[] arr = string.split(" ");
		if (arr.length != 3) {
			throw new ParseException("Parsing Exception", 1);
		}
		Organisation organisation = new Organisation(arr[2]);
		organisation.setStartTime(HH_COLON_MM.parse(arr[0]).getTime());
		organisation.setEndTime(HH_COLON_MM.parse(arr[1]).getTime());
		return organisation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.name).toHashCode();
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
		Organisation rhs = (Organisation) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj)).append(this.name, rhs.name).isEquals();
	}
}
