package is.idega.idegaweb.egov.course.data;

import com.idega.user.data.User;

public class ApplicationHolder {

	private User user = null;
	private Course course = null;
	private int daycare = -1;
	private int tripHome = -1;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public int getDaycare() {
		return daycare;
	}
	public void setDaycare(int daycare) {
		this.daycare = daycare;
	}
	public int getTripHome() {
		return tripHome;
	}
	public void setTripHome(int tripHome) {
		this.tripHome = tripHome;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof ApplicationHolder) {
			ApplicationHolder hol = (ApplicationHolder) obj;
			return user.equals(hol.user) && course.equals(hol.course);
		}
		return false;
	}
	
	public String toString() {
		return user+ " : " + course + " : " + daycare + " : " +tripHome;
	}
}
