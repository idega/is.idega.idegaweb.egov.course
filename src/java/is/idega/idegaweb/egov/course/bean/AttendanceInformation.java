package is.idega.idegaweb.egov.course.bean;

import is.idega.idegaweb.egov.course.data.Course;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.idega.user.data.User;

public class AttendanceInformation implements Serializable {

	private static final long serialVersionUID = 3500960402161523500L;

	private String familyNr;

	private Map<String, String> usersFamilyNumbers = new HashMap<String, String>();

	private Map<User, Map<String, Course>> userCourses = new HashMap<User, Map<String,Course>>();

	public AttendanceInformation() {
		super();
	}

	public AttendanceInformation(String familyNr) {
		this();
		this.familyNr = familyNr;
	}

	public void setFamilyNr(String personalId) {
		usersFamilyNumbers.put(personalId, familyNr);
	}

	public Map<User, Map<String, Course>> getUserCourses() {
		return userCourses;
	}

}