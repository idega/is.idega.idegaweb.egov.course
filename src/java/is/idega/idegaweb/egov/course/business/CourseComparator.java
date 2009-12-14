/*
 * $Id$ Created on Apr 18, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.data.Course;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.idega.util.IWTimestamp;

public class CourseComparator implements Comparator {

	public static final int NAME_SORT = 1;
	public static final int REVERSE_NAME_SORT = 8;
	public static final int TYPE_SORT = 2;
	public static final int YEAR_SORT = 3;
	public static final int DATE_SORT = 4;
	public static final int PLACES_SORT = 5;
	public static final int FREE_PLACES_SORT = 6;
	public static final int ID_SORT = 7;
	public static final int REVERSE_ID_SORT = 9;

	private Locale locale;
	private int compareBy = NAME_SORT;
	private Collator collator;

	public CourseComparator(Locale locale, int compareBy) {
		this.locale = locale;
		this.compareBy = compareBy;
	}

	public int compare(Object arg0, Object arg1) {
		Course course1 = (Course) arg0;
		Course course2 = (Course) arg1;
		collator = Collator.getInstance(locale);

		int returner = 0;
		switch (this.compareBy) {
			case ID_SORT:
				returner = idSort(course1, course2);
				break;

			case REVERSE_ID_SORT:
				returner = -idSort(course1, course2);
				break;
				
			case NAME_SORT:
				returner = nameSort(course1, course2);
				break;

			case TYPE_SORT:
				returner = typeSort(course1, course2);
				break;

			case YEAR_SORT:
				returner = yearSort(course1, course2);
				break;

			case DATE_SORT:
				returner = dateSort(course1, course2);
				break;

			case PLACES_SORT:
				returner = placesSort(course1, course2);
				break;

			case FREE_PLACES_SORT:
				returner = freePlacesSort(course1, course2);
				break;

			case REVERSE_NAME_SORT:
				returner = -nameSort(course1, course2);
				break;

			default:
				returner = nameSort(course1, course2);
				break;
		}

		if (returner == 0) {
			returner = compareBy == REVERSE_NAME_SORT ? -idSort(course1, course2) : idSort(course1, course2);
		}

		return returner;
	}

	private int idSort(Course course1, Course course2) {
		return course1.getCourseNumber() - course2.getCourseNumber();
	}

	private int nameSort(Course course1, Course course2) {
		return collator.compare(course1.getName(), course2.getName());
	}

	private int typeSort(Course course1, Course course2) {
		return collator.compare(course1.getCourseType().getName(), course2.getCourseType().getName());
	}

	private int yearSort(Course course1, Course course2) {
		return course1.getBirthyearFrom() - course2.getBirthyearFrom();
	}

	private int dateSort(Course course1, Course course2) {
		IWTimestamp start1 = new IWTimestamp(course1.getStartDate());
		IWTimestamp start2 = new IWTimestamp(course2.getStartDate());

		if (start1.isEarlierThan(start2)) {
			return -1;
		}
		else if (start2.isEarlierThan(start1)) {
			return 1;
		}
		return 0;
	}

	private int placesSort(Course course1, Course course2) {
		return -(course1.getMax() - course2.getMax());
	}

	private int freePlacesSort(Course course1, Course course2) {
		return -(course1.getFreePlaces() - course2.getFreePlaces());
	}
}