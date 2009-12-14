/*
 * $Id$ Created on Apr 18, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.data.CourseApplication;
import is.idega.idegaweb.egov.course.data.CourseChoice;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class CourseChoiceComparator implements Comparator {

	public static final int NAME_SORT = 1;
	public static final int DATE_SORT = 2;
	public static final int ID_SORT = 3;

	private Locale locale;
	private int compareBy = NAME_SORT;
	private Collator collator;

	public CourseChoiceComparator(Locale locale, int compareBy) {
		this.locale = locale;
		this.compareBy = compareBy;
	}

	public int compare(Object arg0, Object arg1) {
		CourseChoice choice1 = (CourseChoice) arg0;
		CourseChoice choice2 = (CourseChoice) arg1;
		collator = Collator.getInstance(locale);

		int returner = 0;
		switch (this.compareBy) {
			case ID_SORT:
				returner = idSort(choice1, choice2);
				break;

			case NAME_SORT:
				returner = nameSort(choice1, choice2);
				break;

			case DATE_SORT:
				returner = dateSort(choice1, choice2);
				break;

			default:
				returner = idSort(choice1, choice2);
				break;
		}

		return returner;
	}

	private int idSort(CourseChoice choice1, CourseChoice choice2) {
		return ((Integer) choice1.getPrimaryKey()).intValue() - ((Integer) choice2.getPrimaryKey()).intValue();
	}

	private int nameSort(CourseChoice choice1, CourseChoice choice2) {
		User user1 = choice1.getUser();
		User user2 = choice2.getUser();
		return collator.compare(user1.getName(), user2.getName());
	}

	private int dateSort(CourseChoice choice1, CourseChoice choice2) {
		CourseApplication application1 = choice1.getApplication();
		CourseApplication application2 = choice2.getApplication();
		
		IWTimestamp start1 = new IWTimestamp(application1.getCreated());
		IWTimestamp start2 = new IWTimestamp(application2.getCreated());

		if (start1.isEarlierThan(start2)) {
			return -1;
		}
		else if (start2.isEarlierThan(start1)) {
			return 1;
		}
		return 0;
	}
}