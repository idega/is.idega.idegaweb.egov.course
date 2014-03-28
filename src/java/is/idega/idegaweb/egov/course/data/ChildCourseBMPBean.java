package is.idega.idegaweb.egov.course.data;

import is.idega.idegaweb.egov.course.data.rent.RentableItem;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOLookupException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.Group;

public class ChildCourseBMPBean extends CourseBMPBean implements ChildCourse {

	private static final long serialVersionUID = 7425610935661180112L;

	public static final String PARENT_COURSE_ID = "parent_course_id";

	@Override
	public void initializeAttributes() {
		super.initializeAttributes();
		addOneToOneRelationship(PARENT_COURSE_ID, Course.class);
	}

	@Override
	public Course getParentCourse() {
		return (Course) getColumnValue(PARENT_COURSE_ID);
	}

	@Override
	public void setParentCourse(Course parentCourse) {
		setColumn(PARENT_COURSE_ID, parentCourse);
	}

	public Collection<Integer> ejbFindChildCourses(Course parentCourse) {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(
				table.getColumn(PARENT_COURSE_ID), 
				MatchCriteria.EQUALS, 
				parentCourse.getPrimaryKey()));

		try {
			return idoFindPKsByQuery(query);
		} catch (FinderException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"Failed to get primary keys for " + getInterfaceClass().getSimpleName() + 
					"'s by query: '" + query + "' cause of: ", e);
		}

		return Collections.emptyList();
	}

	@Override
	public int getCourseNumber() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getCourseNumber() : parentCourse.getCourseNumber();
	}
	@Override
	public boolean isOpenForRegistration() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.isOpenForRegistration() : parentCourse.isOpenForRegistration();
	}
	@Override
	public boolean hasPreCare() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.hasPreCare() : parentCourse.hasPreCare();
	}
	@Override
	public boolean hasPostCare() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.hasPreCare() : parentCourse.hasPreCare();
	}
	@Override
	public String getName() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getName() : parentCourse.getName();
	}
	@Override
	public String getUser() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getUser() : parentCourse.getUser();
	}
	@Override
	public String getDescription() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getDescription() : parentCourse.getDescription();
	}
	@Override
	public String getProviderId() {
		return super.getProviderId();
	}
	@Override
	public CourseType getCourseType() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getCourseType() : parentCourse.getCourseType();
	}
	@Override
	public CoursePrice getPrice() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getPrice() : parentCourse.getPrice();
	}
	@Override
	public float getCoursePrice() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getCoursePrice() : parentCourse.getCoursePrice();
	}
	@Override
	public float getCourseCost() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getCourseCost() : parentCourse.getCourseCost();
	}
	@Override
	public String getAccountingKey() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getAccountingKey() : parentCourse.getAccountingKey();
	}
	@Override
	public Timestamp getStartDate() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getStartDate() : parentCourse.getStartDate();
	}
	@Override
	public Timestamp getEndDate() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getEndDate() : parentCourse.getEndDate();
	}
	@Override
	public Timestamp getRegistrationEnd() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getRegistrationEnd() : parentCourse.getRegistrationEnd();
	}
	@Override
	public int getBirthyearFrom() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getBirthyearFrom() : parentCourse.getBirthyearFrom();
	}
	@Override
	public int getBirthyearTo() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getBirthyearTo() : parentCourse.getBirthyearTo();
	}
	@Override
	public int getMax() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getMax() : parentCourse.getMax();
	}
	@Override
	public int getFreePlaces(boolean countOffers) {
		Course parentCourse = getParentCourse();
		try {
			CourseChoiceHome home = (CourseChoiceHome) getIDOHome(CourseChoice.class);
			return getMax() - home.getCountByCourse(parentCourse == null ? this : parentCourse, countOffers);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (IDOException e) {
			e.printStackTrace();
		}

		return getMax();
	}
	@Override
	public Collection<Group> getGroupsWithAccess() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getGroupsWithAccess() : parentCourse.getGroupsWithAccess();
	}
	@Override
	public boolean isPrivate() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.isPrivate() : parentCourse.isPrivate();
	}
	@Override
	public Collection<? extends RentableItem> getRentableItems(Class<? extends RentableItem> itemType) {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getRentableItems(itemType) : parentCourse.getRentableItems(itemType);
	}
	@Override
	public Collection<CoursePrice> getAllPrices() {
		Course parentCourse = getParentCourse();
		return parentCourse == null ? super.getAllPrices() : parentCourse.getAllPrices();
	}

	//	Setters
	@Override
	public void setPrivate(boolean isPrivate) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setPrivate(isPrivate);
		}
	}

	@Override
	public void setName(String name) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setName(name);
		}
	}

	@Override
	public void setUser(String user) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setUser(user);
		}
	}

	@Override
	public void setDescription(String description) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setDescription(description);
		}
	}

	@Override
	public void setCourseType(CourseType courseType) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setCourseType(courseType);
		}
	}

	@Override
	public void setPrice(CoursePrice price) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setPrice(price);
		}
	}

	@Override
	public void setCoursePrice(float price) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setCoursePrice(price);
		}
	}

	@Override
	public void setCourseCost(float cost) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setCourseCost(cost);
		}
	}

	@Override
	public void setAccountingKey(String key) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setAccountingKey(key);
		}
	}

	@Override
	public void setStartDate(Timestamp startDate) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setStartDate(startDate);
		}
	}

	@Override
	public void setEndDate(Timestamp endDate) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setEndDate(endDate);
		}
	}

	@Override
	public void setRegistrationEnd(Timestamp registrationEnd) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setRegistrationEnd(registrationEnd);
		}
	}

	@Override
	public void setBirthyearFrom(int from) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setBirthyearFrom(from);
		}
	}

	@Override
	public void setBirthyearTo(int to) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setBirthyearTo(to);
		}
	}

	@Override
	public void setMax(int max) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setMax(max);
		}
	}

	@Override
	public void setOpenForRegistration(boolean openForRegistration) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setOpenForRegistration(openForRegistration);
		}
	}

	@Override
	public void setHasPreCare(boolean hasPreCare) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setHasPreCare(hasPreCare);
		}
	}

	@Override
	public void setHasPostCare(boolean hasPostCare) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setHasPostCare(hasPostCare);
		}
	}
}