package is.idega.idegaweb.egov.course.data;

import is.idega.idegaweb.egov.course.CourseConstants;

import com.idega.block.school.data.School;
import com.idega.user.data.User;

public class ApplicationHolder {

	private User user = null;
	private Course course = null;
	private int daycare = -1;
	private Boolean pickedUp = null;
	private float price = 0;
	private boolean hasDyslexia = false;
	private Object choicePK = null;

	public Object getChoicePK() {
		return choicePK;
	}

	public void setChoicePK(Object choicePK) {
		this.choicePK = choicePK;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Course getCourse() {
		return course;
	}

	public School getProvider() {
		return getCourse().getProvider();
	}

	public int getPrice() {
		if (course.getCoursePrice() > 0) {
			return (int) course.getCoursePrice();
		}

		CoursePrice price = course.getPrice();
		int totalPrice = price.getPrice();
		if (getDaycare() == CourseConstants.DAY_CARE_POST) {
			totalPrice += price.getPostCarePrice();
		}
		else if (getDaycare() == CourseConstants.DAY_CARE_PRE) {
			totalPrice += price.getPreCarePrice();
		}
		else if (getDaycare() == CourseConstants.DAY_CARE_PRE_AND_POST) {
			totalPrice += (price.getPreCarePrice() + price.getPostCarePrice());
		}
		return totalPrice;
	}

	public float getCalculatedPrice() {
		return price;
	}

	public void setCalculatedPrice(float price) {
		this.price = price;
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

	public Boolean getPickedUp() {
		return pickedUp;
	}

	public void setPickedUp(Boolean pickedUp) {
		this.pickedUp = pickedUp;
	}

	public boolean hasDyslexia() {
		return hasDyslexia;
	}

	public void setHasDyslexia(boolean hasDyslexia) {
		this.hasDyslexia = hasDyslexia;
	}

	public boolean equals(Object obj) {
		if (obj instanceof ApplicationHolder) {
			ApplicationHolder hol = (ApplicationHolder) obj;
			return user.equals(hol.user) && course.equals(hol.course);
		}
		return false;
	}

	public String toString() {
		return user + " : " + course + " : " + daycare + " : " + pickedUp;
	}
}