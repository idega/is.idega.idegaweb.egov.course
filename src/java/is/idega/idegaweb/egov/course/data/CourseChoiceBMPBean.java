/*
 * $Id$ Created on Mar 27, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.data;

import java.sql.Date;
import java.sql.Timestamp;
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
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.data.Gender;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public class CourseChoiceBMPBean extends GenericEntity implements CourseChoice {

	private static final long serialVersionUID = 1846124987058471485L;

	public static final String ENTITY_NAME = "cou_course_choice";

	private static final String COLUMN_APPLICATION = "application_id";
	private static final String COLUMN_COURSE = "course_id";
	private static final String COLUMN_USER = "user_id";
	private static final String COLUMN_PAYMENT_TIMESTAMP = "payment_timestamp";
	private static final String COLUMN_DAY_CARE = "day_care";
	private static final String COLUMN_PICKED_UP = "picked_up";
	private static final String COLUMN_VALID = "is_valid";
	private static final String COLUMN_DYSLEXIA = "has_dyslexia";
	private static final String COLUMN_CERTIFICATE_FEE = "course_certificate_fee";
	private static final String COLUMN_WAITING_LIST = "waiting_list";
	private static final String COLUMN_NOTES = "notes";

	public static final String COLUMN_VERIFICATION_FROM_GOVERMENT_OFFICE = "verific_from_goverment";
	public static final String COLUMN_CERTIFICATE_OF_PROPERTY = "certificate_of_property";
	public static final String COLUMN_CRIMINAL_RECORD = "criminal_record";
	public static final String COLUMN_VERIFICATION_OF_PAYMENT = "verification_of_payment";
	public static final String COLUMN_NEED_VERIFICATION_FROM_GOVERMENT_OFFICE = "need_verific_from_goverment";
	public static final String COLUMN_LIMITED_CERTIFICATE = "limited_certificate";
	public static final String COLUMN_DID_NOT_SHOW_UP = "did_not_show_up";
	public static final String COLUMN_PASSED = "passed_course";
	public static final String COLUMN_CREATE_LOGIN = "create_login";
	public static final String COLUMN_HAS_RECEIVED_REMINDER = "received_reminder";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_PAYMENT_TIMESTAMP, "Payment timestamp", Timestamp.class);
		addAttribute(COLUMN_DAY_CARE, "Day care", Integer.class);
		addAttribute(COLUMN_PICKED_UP, "Picked up", Boolean.class);
		addAttribute(COLUMN_VALID, "Valid", Boolean.class);
		addAttribute(COLUMN_DYSLEXIA, "Has dyslexia", Boolean.class);
		addAttribute(COLUMN_PASSED, "Has passed course", Boolean.class);
		addAttribute(COLUMN_CREATE_LOGIN, "Create login for user", Boolean.class);
		addAttribute(COLUMN_CERTIFICATE_FEE, "Course certificate fee", Float.class);
		addAttribute(COLUMN_NOTES, "Notes", String.class, 4000);

		addAttribute(COLUMN_VERIFICATION_FROM_GOVERMENT_OFFICE, "Verfication from government office", Boolean.class);
		addAttribute(COLUMN_CERTIFICATE_OF_PROPERTY, "Certificate of property", Boolean.class);
		addAttribute(COLUMN_CRIMINAL_RECORD, "Criminal record", Boolean.class);
		addAttribute(COLUMN_VERIFICATION_OF_PAYMENT, "Verification of payment", Boolean.class);
		addAttribute(COLUMN_NEED_VERIFICATION_FROM_GOVERMENT_OFFICE, "Needs verification from goverment office", Boolean.class);
		addAttribute(COLUMN_LIMITED_CERTIFICATE, "Limited certificate", Boolean.class);
		addAttribute(COLUMN_DID_NOT_SHOW_UP, "Did not show up", Boolean.class);
		
		addAttribute(COLUMN_WAITING_LIST, "Waiting list", Boolean.class);
		addAttribute(COLUMN_HAS_RECEIVED_REMINDER, "Has received reminder", Boolean.class);

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

	public Timestamp getPaymentTimestamp() {
		return getTimestampColumnValue(COLUMN_PAYMENT_TIMESTAMP);
	}

	public boolean isPickedUp() {
		return getBooleanColumnValue(COLUMN_PICKED_UP, false);
	}

	public boolean isValid() {
		return getBooleanColumnValue(COLUMN_PICKED_UP, true);
	}

	public boolean hasDyslexia() {
		return getBooleanColumnValue(COLUMN_DYSLEXIA, false);
	}
	
	public boolean isOnWaitingList() {
		return getBooleanColumnValue(COLUMN_WAITING_LIST, false);
	}
	
	public boolean hasReceivedReminder() {
		return getBooleanColumnValue(COLUMN_HAS_RECEIVED_REMINDER, false);
	}

	public boolean hasCreateLogin() {
		return getBooleanColumnValue(COLUMN_CREATE_LOGIN, false);
	}
	
	public String getNotes() {
		return getStringColumnValue(COLUMN_NOTES);
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

	public void setPaymentTimestamp(Timestamp timestamp) {
		setColumn(COLUMN_PAYMENT_TIMESTAMP, timestamp);
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

	public void setHasDyslexia(boolean dyslexia) {
		setColumn(COLUMN_DYSLEXIA, dyslexia);
	}
	
	public void setWaitingList(boolean waitingList) {
		setColumn(COLUMN_WAITING_LIST, waitingList);
	}
	
	public void setReceivedReminder(boolean hasReceivedReminder) {
		setColumn(COLUMN_HAS_RECEIVED_REMINDER, hasReceivedReminder);
	}

	public void setHasCreateLogin(boolean createLogin) {
		setColumn(COLUMN_CREATE_LOGIN, createLogin);
	}
	
	public void setNotes(String notes) {
		setColumn(COLUMN_NOTES, notes);
	}
	
	//Finders
	public Collection ejbFindAllByApplication(CourseApplication application) throws FinderException {
		return ejbFindAllByApplication(application, null);
	}
	
	public Collection ejbFindAllByApplication(CourseApplication application, Boolean waitingList) throws FinderException {
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
		if (waitingList != null) {
			if (waitingList.booleanValue()) {
				query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_WAITING_LIST), MatchCriteria.EQUALS, true));
			}
			else {
				query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_WAITING_LIST), MatchCriteria.EQUALS, false), new MatchCriteria(table.getColumn(COLUMN_WAITING_LIST))));
			}
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
	
	public Collection ejbFindAllByCourse(Course course, Boolean waitingList) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_COURSE), MatchCriteria.EQUALS, course));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));
		if (waitingList != null) {
			if (waitingList.booleanValue()) {
				query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_WAITING_LIST), MatchCriteria.EQUALS, true));
			}
			else {
				query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_WAITING_LIST), MatchCriteria.EQUALS, false), new MatchCriteria(table.getColumn(COLUMN_WAITING_LIST))));
			}
		}
		query.addOrder(new Order(table.getColumn(COLUMN_PAYMENT_TIMESTAMP), true));
		query.addOrder(new Order(table.getColumn(getIDColumnName()), true));

		if (IWMainApplication.isDebugActive()) {
			System.out.println("query = " + query.toString());
		}
		
		return idoFindPKsByQuery(query);
	}

	public int ejbHomeGetCountByCourse(Course course) throws IDOException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_COURSE), MatchCriteria.EQUALS, course));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_WAITING_LIST), MatchCriteria.EQUALS, false), new MatchCriteria(table.getColumn(COLUMN_WAITING_LIST))));
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		return idoGetNumberOfRecords(query);
	}

	public Collection ejbFindAllByUser(User user) throws FinderException {
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
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));
		query.addOrder(new Order(course.getColumn("START_DATE"), true));

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

	public Collection ejbFindAllByCourseAndDate(Course course, IWTimestamp fromDate, IWTimestamp toDate) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		if (course != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_COURSE), MatchCriteria.EQUALS, course));
		}
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		query.addOrder(new Order(table.getColumn(COLUMN_COURSE), true));

		/*@TODO add handling for dates, join with course and use the date there */
		
		if (IWMainApplication.isDebugActive()) {
			System.out.println("sql = " + query.toString());
		}
		
		return idoFindPKsByQuery(query);
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

	public void setPassed(boolean passed) {
		setColumn(COLUMN_PASSED, passed);
	}

	public boolean hasPassed() {
		return getBooleanColumnValue(COLUMN_PASSED);
	}

	public void setCourseCertificateFee(float fee) {
		setColumn(COLUMN_CERTIFICATE_FEE, fee);
	}

	public float getCourseCertificateFee() {
		return getFloatColumnValue(COLUMN_CERTIFICATE_FEE, 0);
	}

	public boolean isCertificateOfProperty() {
		return getBooleanColumnValue(COLUMN_CERTIFICATE_OF_PROPERTY);
	}

	public boolean isCriminalRecord() {
		return getBooleanColumnValue(COLUMN_CRIMINAL_RECORD);
	}

	public boolean isDidNotShowUp() {
		return getBooleanColumnValue(COLUMN_DID_NOT_SHOW_UP);
	}

	public boolean isNeedVerificationFromGovermentOffice() {
		return getBooleanColumnValue(COLUMN_NEED_VERIFICATION_FROM_GOVERMENT_OFFICE);
	}

	public boolean isVerificationFromGovermentOffice() {
		return getBooleanColumnValue(COLUMN_VERIFICATION_FROM_GOVERMENT_OFFICE);
	}

	public boolean isVerificationOfPayment() {
		return getBooleanColumnValue(COLUMN_VERIFICATION_OF_PAYMENT);
	}

	public void setCertificateOfProperty(boolean certificateOfProperty) {
		setColumn(COLUMN_CERTIFICATE_OF_PROPERTY, certificateOfProperty);
	}

	public void setCriminalRecord(boolean criminalRecord) {
		setColumn(COLUMN_CRIMINAL_RECORD, criminalRecord);
	}

	public boolean isLimitedCertificate() {
		return getBooleanColumnValue(COLUMN_LIMITED_CERTIFICATE);
	}

	public void setDidNotShowUp(boolean didNotShowUp) {
		setColumn(COLUMN_DID_NOT_SHOW_UP, didNotShowUp);
	}

	public void setNeedsVerificationFromGovermentOffice(boolean needsVerificationFromOffice) {
		setColumn(COLUMN_NEED_VERIFICATION_FROM_GOVERMENT_OFFICE, needsVerificationFromOffice);
	}

	public void setVerificationFromGovermentOffice(boolean verificationFromGovermentOffice) {
		setColumn(COLUMN_VERIFICATION_FROM_GOVERMENT_OFFICE, verificationFromGovermentOffice);
	}

	public void setVerificationOfPayment(boolean verificationOfPayment) {
		setColumn(COLUMN_VERIFICATION_OF_PAYMENT, verificationOfPayment);
	}

	public void setLimitedCertificate(boolean limitedCertificate) {
		setColumn(COLUMN_LIMITED_CERTIFICATE, limitedCertificate);
	}

	public boolean getBooleanValueFromColumn(String columnName) {
		return getBooleanColumnValue(columnName);
	}

	public void setBooleanValueForColumn(boolean value, String columnName) {
		setColumn(columnName, value);
	}
}