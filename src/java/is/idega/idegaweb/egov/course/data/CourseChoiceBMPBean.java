/*
 * $Id$ Created on Mar 27, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.CountColumn;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.Gender;
import com.idega.user.data.User;

public class CourseChoiceBMPBean extends GenericEntity implements CourseChoice {

	public static final String ENTITY_NAME = "cou_course_choice";

	private static final String COLUMN_APPLICATION = "application_id";
	private static final String COLUMN_COURSE = "course_id";
	private static final String COLUMN_USER = "user_id";
	private static final String COLUMN_DAY_CARE = "day_care";
	private static final String COLUMN_PICKED_UP = "picked_up";
	private static final String COLUMN_VALID = "is_valid";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_DAY_CARE, "Day care", Integer.class);
		addAttribute(COLUMN_PICKED_UP, "Picked up", Boolean.class);
		addAttribute(COLUMN_VALID, "Valid", Boolean.class);

		addManyToOneRelationship(COLUMN_APPLICATION, CourseApplication.class);
		addManyToOneRelationship(COLUMN_COURSE, Course.class);
		addManyToOneRelationship(COLUMN_USER, User.class);
	}

	// Getters
	public CourseApplication getApplication() {
		return (CourseApplication) getColumnValue(COLUMN_APPLICATION);
	}

	public Course getCourse() {
		return (Course) getColumnValue(COLUMN_COURSE);
	}

	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}

	public int getDayCare() {
		return getIntColumnValue(COLUMN_DAY_CARE);
	}

	public boolean isPickedUp() {
		return getBooleanColumnValue(COLUMN_PICKED_UP, false);
	}

	public boolean isValid() {
		return getBooleanColumnValue(COLUMN_PICKED_UP, true);
	}

	// Setters
	public void setApplication(CourseApplication application) {
		setColumn(COLUMN_APPLICATION, application);
	}

	public void setCourse(Course course) {
		setColumn(COLUMN_COURSE, course);
	}

	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}

	public void setDayCare(int dayCare) {
		setColumn(COLUMN_DAY_CARE, dayCare);
	}

	public void setPickedUp(boolean pickedUp) {
		setColumn(COLUMN_PICKED_UP, pickedUp);
	}

	public void setValid(boolean valid) {
		setColumn(COLUMN_VALID, valid);
	}

	// Finders
	public Collection ejbFindAllByApplication(CourseApplication application) throws FinderException {
		Table table = new Table(this);
		Table course = new Table(Course.class);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		try {
			query.addJoin(table, course);
		}
		catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_APPLICATION), MatchCriteria.EQUALS, application));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));
		query.addOrder(new Order(course.getColumn("START_DATE"), true));

		return idoFindPKsByQuery(query);
	}

	public Object ejbFindFirstChoiceByApplication(CourseApplication application) throws FinderException {
		Table table = new Table(this);
		Table course = new Table(Course.class);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		try {
			query.addJoin(table, course);
		}
		catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_APPLICATION), MatchCriteria.EQUALS, application));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));
		query.addOrder(new Order(course.getColumn("START_DATE"), true));

		return idoFindOnePKByQuery(query);
	}

	public Collection ejbFindAllByCourse(Course course) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_COURSE), MatchCriteria.EQUALS, course));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		return idoFindPKsByQuery(query);
	}

	public int ejbHomeGetCountByCourse(Course course) throws IDOException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_COURSE), MatchCriteria.EQUALS, course));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		return idoGetNumberOfRecords(query);
	}

	public Collection ejbFindAllByUser(User user) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_USER), MatchCriteria.EQUALS, user));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllByUserAndProviders(User user, Collection providers) throws FinderException {
		Table table = new Table(this);
		Table course = new Table(Course.class);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		try {
			query.addJoin(table, course);
		}
		catch (IDORelationshipException ire) {
			throw new FinderException(ire.getMessage());
		}
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_USER), MatchCriteria.EQUALS, user));
		query.addCriteria(new InCriteria(course.getColumn("PROVIDER_ID"), providers));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		return idoFindPKsByQuery(query);
	}

	public Object ejbFindByUserAndCourse(User user, Course course) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_COURSE), MatchCriteria.EQUALS, course));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_USER), MatchCriteria.EQUALS, user));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		return idoFindOnePKByQuery(query);
	}

	public int ejbHomeGetCountByUserAndProviders(User user, Collection providers) throws IDOException {
		Table table = new Table(this);
		Table course = new Table(Course.class);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));
		query.addJoin(table, course);
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_USER), MatchCriteria.EQUALS, user));
		query.addCriteria(new InCriteria(course.getColumn("PROVIDER_ID"), providers));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		return idoGetNumberOfRecords(query);
	}

	public int ejbHomeGetCountByProviderAndSchoolTypeAndGender(School provider, SchoolType type, Gender gender, Date fromDate, Date toDate) throws IDOException {
		Table table = new Table(this);
		Table course = new Table(Course.class);
		Table courseType = new Table(CourseType.class);
		Table user = new Table(User.class);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));
		query.addJoin(table, course);
		query.addJoin(course, courseType);
		query.addJoin(table, user);
		query.addCriteria(new MatchCriteria(course.getColumn("PROVIDER_ID"), MatchCriteria.EQUALS, provider));
		query.addCriteria(new MatchCriteria(courseType.getColumn("SCH_SCHOOL_TYPE_ID"), MatchCriteria.EQUALS, type));
		query.addCriteria(new MatchCriteria(user.getColumn("IC_GENDER_ID"), MatchCriteria.EQUALS, gender));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));
		if (fromDate != null) {
			query.addCriteria(new MatchCriteria(course.getColumn("START_DATE"), MatchCriteria.GREATEREQUAL, fromDate));
		}
		if (toDate != null) {
			query.addCriteria(new MatchCriteria(course.getColumn("START_DATE"), MatchCriteria.LESSEQUAL, toDate));
		}

		return idoGetNumberOfRecords(query);
	}

	public int ejbHomeGetCountByCourseAndGender(Course course, Gender gender) throws IDOException {
		Table table = new Table(this);
		Table user = new Table(User.class);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));
		query.addJoin(table, user);
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_COURSE), MatchCriteria.EQUALS, course));
		query.addCriteria(new MatchCriteria(user.getColumn("IC_GENDER_ID"), MatchCriteria.EQUALS, gender));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		return idoGetNumberOfRecords(query);
	}

	public int ejbHomeGetCountByUserAndCourse(User user, Course course) throws IDOException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_COURSE), MatchCriteria.EQUALS, course));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_USER), MatchCriteria.EQUALS, user));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		return idoGetNumberOfRecords(query);
	}
}