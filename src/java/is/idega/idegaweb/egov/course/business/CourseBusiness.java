package is.idega.idegaweb.egov.course.business;


import is.idega.idegaweb.egov.accounting.business.AccountingBusiness;
import is.idega.idegaweb.egov.accounting.business.AccountingEntry;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseApplication;
import is.idega.idegaweb.egov.course.data.CourseApplicationHome;
import is.idega.idegaweb.egov.course.data.CourseCategory;
import is.idega.idegaweb.egov.course.data.CourseCategoryHome;
import is.idega.idegaweb.egov.course.data.CourseCertificate;
import is.idega.idegaweb.egov.course.data.CourseCertificateType;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.data.CourseChoiceHome;
import is.idega.idegaweb.egov.course.data.CourseDiscount;
import is.idega.idegaweb.egov.course.data.CourseDiscountHome;
import is.idega.idegaweb.egov.course.data.CourseHome;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CoursePriceHome;
import is.idega.idegaweb.egov.course.data.CourseProvider;
import is.idega.idegaweb.egov.course.data.CourseProviderArea;
import is.idega.idegaweb.egov.course.data.CourseProviderCategory;
import is.idega.idegaweb.egov.course.data.CourseProviderType;
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.data.CourseTypeHome;
import is.idega.idegaweb.egov.course.data.PriceHolder;
import is.idega.idegaweb.egov.course.presentation.bean.CourseParticipantListRowData;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBOService;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.Gender;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

public interface CourseBusiness extends IBOService, CaseBusiness,
		AccountingBusiness {
	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#reserveCourse
	 */
	public void reserveCourse(Course course) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#setNoPayment
	 */
	public void setNoPayment(Object courseChoicePK, boolean noPayment);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#removeReservation
	 */
	public void removeReservation(Course course) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getNumberOfReservations
	 */
	public int getNumberOfReservations(Course course) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#printReservations
	 */
	public void printReservations() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAccountingEntries
	 */
	@Override
	public AccountingEntry[] getAccountingEntries(String productCode,
			String providerCode, Date fromDate, Date toDate);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getLocalizedCaseDescription
	 */
	@Override
	public String getLocalizedCaseDescription(Case theCase, Locale locale)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#finishPayment
	 */
	public String finishPayment(String properties)
			throws CreditCardAuthorizationException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#authorizePayment
	 */
	public String authorizePayment(String nameOnCard, String cardNumber,
			String monthExpires, String yearExpires, String ccVerifyNumber,
			double amount, String currency, String referenceNumber)
			throws CreditCardAuthorizationException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#refundPayment
	 */
	public String refundPayment(CourseApplication application,
			String cardNumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount)
			throws CreditCardAuthorizationException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCreditCardImages
	 */
	public Collection getCreditCardImages() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#deleteCourseType
	 */
	public boolean deleteCourseType(Object pk) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeCourseType
	 */
	public void storeCourseType(Object pk, String name, String description,
			String localizationKey, Object schoolTypePK, String accountingKey, boolean disabled)
			throws FinderException, CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#deleteCourse
	 */
	public boolean deleteCourse(Object pk) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#createCourse
	 */
	public Course createCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price) throws FinderException, CreateException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#createCourse
	 */
	public Course createCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration)
			throws FinderException, CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#createCourse
	 */
	public Course createCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration, IWTimestamp registrationEnd, boolean hasPreCare, boolean hasPostCare)
			throws FinderException, CreateException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeCourse
	 */
	public void storeCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price) throws FinderException, CreateException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeCourse
	 */
	public Course storeCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration)
			throws FinderException, CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeCourse
	 */
	public Course storeCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate, IWTimestamp registrationEnd,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration)
			throws FinderException, CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeCourse
	 */
	public Course storeCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate, IWTimestamp registrationEnd,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration, boolean hasPreCare, boolean hasPostCare)
			throws FinderException, CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#deleteCoursePrice
	 */
	public boolean deleteCoursePrice(Object pk) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#deleteCourseDiscount
	 */
	public boolean deleteCourseDiscount(Object pk) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeCoursePrice
	 */
	public void storeCoursePrice(Object pk, String name, int numberOfDays,
			Timestamp validFrom, Timestamp validTo, int iPrice,
			int preCarePrice, int postCarePrice, Object schoolAreaPK,
			Object courseTypePK) throws CreateException, NumberFormatException,
			FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeCourseDiscount
	 */
	public void storeCourseDiscount(Object pk, String name, String type,
			Timestamp validFrom, Timestamp validTo, float discount)
			throws CreateException, NumberFormatException, FinderException,
			RemoteException;

	/**
	 * 
	 * @param schoolTypePK is {@link CourseCategory#getPrimaryKey()}, 
	 * not <code>null</code>;
	 * @param valid is not disabled;
	 * @return {@link Collection} of entities or {@link Collections#emptyList()};
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<CourseType> getCourseTypes(Integer schoolTypePK, boolean valid);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseTypesDWR
	 */
	public Map<String, String> getCourseTypesDWR(int schoolTypePK, String country)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseMapDWR
	 */
	public Map<Object, String> getCourseMapDWR(int providerPK, int schoolTypePK,
			int courseTypePK, String country) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursesMapDWR
	 */
	public Map<Object, String> getCoursesMapDWR(int providerPK, int schoolTypePK,
			int courseTypePK, int year, String country) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursePricesDWR
	 */
	public Map<String, String> getCoursePricesDWR(String date, int providerPK,
			int courseTypePK, String country) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getPriceDWR
	 */
	public CoursePriceDWR getPriceDWR(int pricePK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getUserDWR
	 */
	public UserDWR getUserDWR(String personalID, int childPK, int minimumAge,
			String country) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getUserDWRByRelation
	 */
	public UserDWR getUserDWRByRelation(String personalID, int childPK,
			int minimumAge, String country, String selectedRelation)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursesDWR
	 */
	public Collection<CourseDWR> getCoursesDWR(int providerPK, int schoolTypePK,
			int courseTypePK, int applicantPK, String country, boolean isAdmin)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#isFull
	 */
	public boolean isFull(Course course) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#isRegistered
	 */
	public boolean isRegistered(User user, Course course)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#hasNotStarted
	 */
	public boolean hasNotStarted(Course course, boolean isAdmin)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#isOfAge
	 */
	public boolean isOfAge(User user, Course course) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseDWR
	 */
	public CourseDWR getCourseDWR(Locale locale, Course course)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseDWR
	 */
	public CourseDWR getCourseDWR(Locale locale, Course course, boolean showYear)
			throws RemoteException;

	/**
	 * 
	 * @param birthYear is between {@link Course#getBirthyearFrom()} and
	 * {@link Course#getBirthyearTo()}, skipped if less that 0;
	 * @param schoolTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param courseTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param fromDate is {@link Course#getStartDate()} to filter by,
	 * skipped if <code>null</code>;
	 * @param toDate is {@link Course#getStartDate()} to filter by,
	 * skipped if <code>null</code>;
	 * @return entities or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> getCourses(
			int birthYear, 
			String schoolTypePK,
			String courseTypePK, 
			Date fromDate, 
			Date toDate);

	/**
	 * 
	 * @param birthYear is between {@link Course#getBirthyearFrom()} and
	 * {@link Course#getBirthyearTo()}, skipped if less that 0;
	 * @param schoolTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param courseTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @return entities or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> getCourses(
			int birthYear, 
			String schoolTypePK,
			String courseTypePK);

	/**
	 * 
	 * @param birthYear is between {@link Course#getBirthyearFrom()} and
	 * {@link Course#getBirthyearTo()}, skipped if less that 0;
	 * @param providerPK is {@link CourseProvider#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param schoolTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param courseTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param fromDate is {@link Course#getStartDate()} to filter by,
	 * skipped if <code>null</code>;
	 * @param toDate is {@link Course#getStartDate()} to filter by,
	 * skipped if <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> getCourses(
			int birthYear, 
			String providerPK,
			String schoolTypePK, 
			String courseTypePK, 
			Date fromDate, Date toDate);

	/**
	 * 
	 * @param providers to search by, skipped if <code>null</code>;
	 * @param schoolTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param courseTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param fromDate is {@link Course#getStartDate()} to filter by,
	 * skipped if <code>null</code>;
	 * @param toDate is {@link Course#getStartDate()} to filter by,
	 * skipped if <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> getCourses(
			Collection<CourseProvider> providers, 
			String schoolTypePK,
			String courseTypePK, 
			Date fromDate, 
			Date toDate);

	/**
	 * 
	 * @param birthYear is between {@link Course#getBirthyearFrom()} and
	 * {@link Course#getBirthyearTo()}, skipped if less that 0;
	 * @param providerPK is {@link CourseProvider#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param schoolTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param courseTypePK is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @return entities or {@link Collections#emptyList()};
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Course> getCourses(
			int birthYear, 
			String providerPK,
			String schoolTypePK, 
			String courseTypePK);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection<Course> getCourses(Collection<CourseProvider> providers, Object schoolTypePK,
			Object courseTypePK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoice
	 */
	public CourseChoice getCourseChoice(Object courseChoicePK)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoices
	 */
	public Collection<CourseChoice> getCourseChoices(Object coursePK, boolean waitingList)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoices
	 */
	public Collection<CourseChoice> getCourseChoices(Course course, boolean waitingList)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoices
	 */
	public Collection<CourseChoice> getCourseChoices(CourseApplication application)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoices
	 */
	public Collection<CourseChoice> getCourseChoices(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseDiscount
	 */
	public CourseDiscount getCourseDiscount(Object pk) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursePrice
	 */
	public CoursePrice getCoursePrice(Object pk) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursePrices
	 */
	public Collection<CoursePrice> getCoursePrices(Date fromDate, Date toDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseDiscounts
	 */
	public Collection<CourseDiscount> getCourseDiscounts(Date fromDate, Date toDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourse
	 */
	public Course getCourse(Object pk) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getNextCourseNumber
	 */
	public int getNextCourseNumber() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseType
	 */
	public CourseType getCourseType(Object pk) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseCategory
	 */
	public CourseCategory getCourseCategory(Object pk) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseApplication
	 */
	public CourseApplication getCourseApplication(Object courseApplicationPK)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCourses
	 */
	public Collection<Course> getAllCourses() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCourses
	 */
	public Collection<Course> getAllCourses(CourseProvider provider) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCourseTypes
	 */
	public Collection<CourseType> getAllCourseTypes(boolean valid) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCourseTypes
	 */
	public Collection<CourseType> getAllCourseTypes(Integer schoolTypePK, boolean valid)
			throws RemoteException;

	/**
	 * 
	 * @return all {@link CourseProviderType}s, 
	 * where {@link CourseProviderType#getCategory()} is equals to 
	 * {@link CourseProviderCategory#CATEGORY_AFTER_SCHOOL_CARE}
	 * and which are not in 
	 * {@link CourseConstants#PROPERTY_HIDDEN_SCHOOL_TYPE} and 
	 * or {@link Collections#emptyList()};
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public <T extends CourseProviderType> Collection<T> getAllAfterschoolCareSchoolTypes();


	/**
	 * 
	 * @param category is {@link CourseProviderCategory#getCategory()}, 
	 * not <code>null</code>;
	 * @return all {@link CourseProviderType}s, 
	 * where {@link CourseProviderType#getCategory()} is equals to given category 
	 * and which are not in 
	 * {@link CourseConstants#PROPERTY_HIDDEN_SCHOOL_TYPE} and 
	 * or {@link Collections#emptyList()};
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public <T extends CourseProviderType> Collection<T> getAllSchoolTypes(String category);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getSchoolAreas
	 */
	public Collection<CourseProviderArea> getSchoolAreas();

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getSchoolTypes
	 */
	public <T extends CourseProviderType> Collection<T> getSchoolTypes(CourseProvider provider) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCoursePrices
	 */
	public Collection<CoursePrice> getAllCoursePrices() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#isRegisteredAtProviders
	 */
	public boolean isRegisteredAtProviders(User user, Collection<CourseProvider> providers)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getNumberOfFreePlaces
	 */
	public int getNumberOfFreePlaces(Course course) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProviders
	 */
	public <P extends CourseProvider> Collection<P> getProviders() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProviders
	 */
	public Collection<? extends CourseProvider> getProviders(String category) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProviders
	 */
	public <P extends CourseProvider> Collection<P> getProviders(CourseProviderArea area, CourseProviderType type)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProvidersDWR
	 */
	public Map<String, String> getProvidersDWR(int userPK, int typePK, String country) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProvidersForUser
	 */
	public Collection<CourseProvider> getProvidersForUser(User user);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getApplicationMap
	 */
	public Map<CourseApplication, Collection<CourseChoice>> getApplicationMap(User user, Collection<CourseProvider> schools)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getApplicationMap
	 */
	public Map<User, Collection<ApplicationHolder>> getApplicationMap(CourseApplication application)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getApplicationMap
	 */
	public Map<User, Collection<ApplicationHolder>> getApplicationMap(CourseApplication application,
			Boolean waitingList) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#calculateRefund
	 */
	public float calculateRefund(CourseChoice choice) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#calculatePrices
	 */
	public SortedSet<PriceHolder> calculatePrices(Map<User, Collection<ApplicationHolder>> applications) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCalculatedCourseCertificateFees
	 */
	public float getCalculatedCourseCertificateFees(Map<User, Object> applications)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getDiscounts
	 */
	public Map<User, PriceHolder> getDiscounts(SortedSet<PriceHolder> userPrices, Map<User, Collection<ApplicationHolder>> applications)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#hasAvailableCourses
	 */
	public boolean hasAvailableCourses(User user, CourseProviderType type)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#hasAvailableCourses
	 */
	public boolean hasAvailableCourses(User user, CourseType type)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#hasAvailableCourses
	 */
	public boolean hasAvailableCourses(User user, CourseProvider school) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#hasAvailableCourses
	 */
	public boolean hasAvailableCourses(User user, CourseProvider school, CourseType type) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getTimeoutDay
	 */
	public int getTimeoutDay() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getTimeoutHour
	 */
	public int getTimeoutHour() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#canInvalidate
	 */
	public boolean canInvalidate(CourseChoice choice) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#canInvalidate
	 */
	public boolean canInvalidate(CourseApplication application)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#sendRefundMessage
	 */
	public void sendRefundMessage(CourseApplication application,
			CourseChoice choice, Locale locale) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#sendRegistrationMessage
	 */
	public void sendRegistrationMessage(CourseApplication application,
			CourseChoice choice, Locale locale) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#invalidateApplication
	 */
	public void invalidateApplication(CourseApplication application,
			User performer, Locale locale) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#invalidateChoice
	 */
	public void invalidateChoice(CourseApplication application,
			CourseChoice choice, Locale locale) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#saveApplication
	 */
	public CourseApplication saveApplication(Map<User, Collection<ApplicationHolder>> applications, String prefix, User performer,
			Locale locale) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#saveApplication
	 */
	public CourseApplication saveApplication(Map<User, Collection<ApplicationHolder>> applications, int merchantID,
			float amount, String merchantType, String paymentType,
			String referenceNumber, String payerName, String payerPersonalID,
			String prefix, User owner, User performer, Locale locale, float certificateFee)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#saveApplication
	 */
	public CourseApplication saveApplication(Map<User, Collection<ApplicationHolder>> applications, int merchantID,
			float amount, String merchantType, String paymentType,
			String referenceNumber, String payerName, String payerPersonalID,
			String prefix, User owner, User performer, Locale locale) throws RemoteException;

	/**
	 *
	 * <p>Finds all primary keys by following criteria and counts number of 
	 * results.</p>
	 * @param provider to filter by, skipped if <code>null</code>;
	 * @param type to filter by, skipped if <code>null</code>;
	 * @param courseType to filter by, skipped if <code>null</code>;
	 * @param fromDate is floor for course start date,
	 * skipped if <code>null</code>;
	 * @param toDate is ceiling for course start date,
	 * skipped if <code>null</code>;
	 * @return count of primary keys found data source or 0
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public int getNumberOfCourses(
			CourseProvider provider, 
			CourseProviderType schoolType,
			CourseType courseType, 
			Date fromDate, Date toDate);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getNumberOfChoices
	 */
	public int getNumberOfChoices(CourseProvider provider, CourseProviderType schoolType,
			Gender gender, Date fromDate, Date toDate) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getNumberOfChoices
	 */
	public int getNumberOfChoices(Course course, Gender gender)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection<Course> getCourses(CourseProvider provider, CourseProviderType schoolType,
			Date fromDate, Date toDate) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getEndDate
	 */
	public Date getEndDate(CoursePrice price, Date startDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursePriceHome
	 */
	public CoursePriceHome getCoursePriceHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseDiscountHome
	 */
	public CourseDiscountHome getCourseDiscountHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseCategoryHome
	 */
	public CourseCategoryHome getCourseCategoryHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseTypeHome
	 */
	public CourseTypeHome getCourseTypeHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseHome
	 */
	public CourseHome getCourseHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoiceHome
	 */
	public CourseChoiceHome getCourseChoiceHome() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseApplicationHome
	 */
	public CourseApplicationHome getCourseApplicationHome()
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCertificate
	 */
	public CourseCertificate getCertificate(Object certificatePK)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseCertificateType
	 */
	public CourseCertificateType getCourseCertificateType(String id)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseCertificateTypeByType
	 */
	public CourseCertificateType getCourseCertificateTypeByType(String type)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getUserCertificates
	 */
	public List<CourseCertificate> getUserCertificates(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getUserCertificate
	 */
	public CourseCertificate getUserCertificate(User user, Course course)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getUserCertificatesByCourse
	 */
	public List<CourseCertificate> getUserCertificatesByCourse(User user, Course course)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getLatestExpirationDateOfCertificate
	 */
	public IWTimestamp getLatestExpirationDateOfCertificate(List<CourseCertificate> certificates)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getLatestValidCertificate
	 */
	public IWTimestamp getLatestValidCertificate(List<CourseCertificate> certificates)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#storeNotes
	 */
	public boolean storeNotes(Integer courseChoiceID, String notes)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#manageCourseChoiceSettings
	 */
	public boolean manageCourseChoiceSettings(String courseChoiceId,
			String columnName, boolean value) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseParticipantListRowData
	 */
	public List<CourseParticipantListRowData> getCourseParticipantListRowData(CourseChoice choice,
			IWResourceBundle iwrb) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCheckBoxesForCourseParticipants
	 */
	public List<AdvancedProperty> getCheckBoxesForCourseParticipants(IWResourceBundle iwrb)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#acceptChoice
	 */
	public boolean acceptChoice(Object courseChoicePK, Locale locale)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#parentsAcceptChoice
	 */
	public boolean parentsAcceptChoice(Object courseChoicePK, User performer, Locale locale);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#removeCertificate
	 */
	public void removeCertificate(Object certificatePK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#sendNextCoursesMessages
	 */
	public void sendNextCoursesMessages() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllChoicesByCourseAndDate
	 */
	public Collection<CourseChoice> getAllChoicesByCourseAndDate(Object coursePK,
			IWTimestamp fromDate, IWTimestamp toDate) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursesByTypes
	 */
	public Collection<Course> getCoursesByTypes(Collection<String> typesIds)
			throws RemoteException, RemoteException;

	/**
	 * 
	 * <p>TODO</p>
	 * @param user
	 * @return
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public <P extends CourseProvider> Collection<P> getHandledCourseProviders(User user);
}