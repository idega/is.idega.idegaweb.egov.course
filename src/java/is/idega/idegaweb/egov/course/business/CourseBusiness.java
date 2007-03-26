package is.idega.idegaweb.egov.course.business;


import java.util.Map;
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.data.CourseHome;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import java.rmi.RemoteException;
import is.idega.idegaweb.egov.course.data.Course;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Collection;
import com.idega.util.IWTimestamp;
import is.idega.idegaweb.egov.course.data.CoursePriceHome;
import com.idega.business.IBOService;
import is.idega.idegaweb.egov.course.data.CourseTypeHome;

public interface CourseBusiness extends IBOService {
	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#deleteCourseType
	 */
	public void deleteCourseType(Object pk) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeCourseType
	 */
	public void storeCourseType(Object pk, String name, String description,
			String localizationKey, Object schoolTypePK)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeCourse
	 */
	public void storeCourse(Object pk, String name, Object schoolTypePK,
			Object courseTypePK, Object coursePricePK, IWTimestamp startDate,
			String accountingKey, int birthYearFrom, int birthYearTo, int maxPer)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#deleteCoursePrice
	 */
	public void deleteCoursePrice(Object pk) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeCoursePrice
	 */
	public void storeCoursePrice(Object pk, String name, int numberOfDays,
			Timestamp validFrom, Timestamp validTo, int iPrice,
			String courseTypeID, String schoolTypeID) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseTypes
	 */
	public Collection getCourseTypes(Integer schoolTypePK)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseTypesDWR
	 */
	public Map getCourseTypesDWR(int schoolTypePK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursePricesDWR
	 */
	public Map getCoursePricesDWR(String date, int courseTypePK)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursesDWR
	 */
	public Collection getCoursesDWR(int schoolTypePK, int courseTypePK,
			int birthYear, String country) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseDWR
	 */
	public CourseDWR getCourseDWR(Locale locale, Course course)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection getCourses(int birthYear, Integer iST, Integer iCT)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursePrice
	 */
	public CoursePrice getCoursePrice(Object pk) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourse
	 */
	public Course getCourse(Object pk) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseType
	 */
	public CourseType getCourseType(Object pk) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCourses
	 */
	public Collection getAllCourses() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCourseTypes
	 */
	public Collection getAllCourseTypes() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCourseCategories
	 */
	public Collection getAllCourseCategories() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCoursePrices
	 */
	public Collection getAllCoursePrices() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursePriceHome
	 */
	public CoursePriceHome getCoursePriceHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseTypeHome
	 */
	public CourseTypeHome getCourseTypeHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseHome
	 */
	public CourseHome getCourseHome() throws RemoteException;
}