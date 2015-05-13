package is.idega.idegaweb.egov.course.data;


import is.idega.idegaweb.egov.course.data.rent.RentableItem;

import java.sql.Timestamp;
import java.util.Collection;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORemoveRelationshipException;

public interface Course extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getUser
	 */
	public String getUser();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getProvider
	 */
	public School getProvider();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getCourseType
	 */
	public CourseType getCourseType();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getPrice
	 */
	public CoursePrice getPrice();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getCoursePrice
	 */
	public float getCoursePrice();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getCourseCost
	 */
	public float getCourseCost();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getAccountingKey
	 */
	public String getAccountingKey();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getStartDate
	 */
	public Timestamp getStartDate();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getEndDate
	 */
	public Timestamp getEndDate();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getRegistrationEnd
	 */
	public Timestamp getRegistrationEnd();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getBirthyearFrom
	 */
	public int getBirthyearFrom();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getBirthyearTo
	 */
	public int getBirthyearTo();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getMax
	 */
	public int getMax();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getFreePlaces
	 */
	public int getFreePlaces();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getFreePlaces
	 */
	public int getFreePlaces(boolean countOffers);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#getCourseNumber
	 */
	public int getCourseNumber();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#isOpenForRegistration
	 */
	public boolean isOpenForRegistration();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#hasPreCare
	 */
	public boolean hasPreCare();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#hasPostCare
	 */
	public boolean hasPostCare();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#hasPreAndPostCarehasPreAndPostCare
	 */
	public boolean hasPreAndPostCare();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setUser
	 */
	public void setUser(String user);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setProvider
	 */
	public void setProvider(School provider);

	public void setProviderId(Integer providerId);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setCourseType
	 */
	public void setCourseType(CourseType courseType);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setPrice
	 */
	public void setPrice(CoursePrice price);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setCoursePrice
	 */
	public void setCoursePrice(float price);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setCourseCost
	 */
	public void setCourseCost(float cost);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setAccountingKey
	 */
	public void setAccountingKey(String key);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setStartDate
	 */
	public void setStartDate(Timestamp startDate);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setEndDate
	 */
	public void setEndDate(Timestamp endDate);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setRegistrationEnd
	 */
	public void setRegistrationEnd(Timestamp registrationEnd);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setBirthyearFrom
	 */
	public void setBirthyearFrom(int from);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setBirthyearTo
	 */
	public void setBirthyearTo(int to);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setMax
	 */
	public void setMax(int max);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setCourseNumber
	 */
	public void setCourseNumber(int number);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setOpenForRegistration
	 */
	public void setOpenForRegistration(boolean openForRegistration);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setHasPreCare
	 */
	public void setHasPreCare(boolean hasPreCare);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseBMPBean#setHasPostCare
	 */
	public void setHasPostCare(boolean hasPostCare);

	public void setRentableItems(Collection<? extends RentableItem> items) throws IDOAddRelationshipException;
	public Collection<? extends RentableItem> getRentableItems(Class<? extends RentableItem> itemClass);
	public void addRentableItem(RentableItem item) throws IDOAddRelationshipException;
	public void removeRentableItem(RentableItem item) throws IDORemoveRelationshipException;
	public void removeAllRentableItems(Class<? extends RentableItem> itemClass) throws IDORemoveRelationshipException;

	public void addPrice(CoursePrice price) throws IDOAddRelationshipException;
	public Collection<CoursePrice> getAllPrices();
	public void removePrice(CoursePrice price) throws IDORemoveRelationshipException;
	public void removeAllPrices() throws IDORemoveRelationshipException;

	public void addSeason(SchoolSeason season) throws IDOAddRelationshipException;
	public Collection<SchoolSeason> getSeasons();
	public void removeSeason(SchoolSeason season) throws IDORemoveRelationshipException;
	public void removeAllSeasons() throws IDORemoveRelationshipException;

}