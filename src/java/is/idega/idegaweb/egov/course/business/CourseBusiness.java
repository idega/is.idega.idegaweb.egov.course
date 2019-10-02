package is.idega.idegaweb.egov.course.business;


import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolType;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBOService;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Image;
import com.idega.user.data.Gender;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

import is.idega.idegaweb.egov.accounting.business.AccountingBusiness;
import is.idega.idegaweb.egov.accounting.business.AccountingEntry;
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
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.data.CourseTypeHome;
import is.idega.idegaweb.egov.course.data.PriceHolder;

public interface CourseBusiness extends IBOService, CaseBusiness,
		AccountingBusiness {
	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#reserveCourse
	 */
	public void reserveCourse(Course course, User user) throws RemoteException;

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
	public Collection<Image> getCreditCardImages() throws RemoteException;

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
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseTypes
	 */
	public Collection<CourseType> getCourseTypes(Integer schoolTypePK, boolean valid)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseTypesDWR
	 */
	public Map getCourseTypesDWR(int schoolTypePK, String country)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseMapDWR
	 */
	public Map getCourseMapDWR(int providerPK, int schoolTypePK,
			int courseTypePK, String country) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursesMapDWR
	 */
	public Map getCoursesMapDWR(int providerPK, int schoolTypePK,
			int courseTypePK, int year, String country) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursePricesDWR
	 */
	public Map getCoursePricesDWR(String date, int providerPK,
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
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection<Course> getCourses(int birthYear, Object schoolTypePK,
			Object courseTypePK, Date fromDate, Date toDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection<Course> getCourses(int birthYear, Object schoolTypePK,
			Object courseTypePK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection<Course> getCourses(int birthYear, Object providerPK,
			Object schoolTypePK, Object courseTypePK, Date fromDate, Date toDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection<Course> getCourses(Collection<School> providers, Object schoolTypePK,
			Object courseTypePK, Date fromDate, Date toDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection<Course> getCourses(int birthYear, Object providerPK,
			Object schoolTypePK, Object courseTypePK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection<Course> getCourses(Collection<School> providers, Object schoolTypePK,
			Object courseTypePK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoice
	 */
	public CourseChoice getCourseChoice(Object courseChoicePK)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoices
	 */
	public Collection<CourseChoice> getCourseChoices(Object coursePK, Boolean waitingList)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoices
	 */
	public Collection<CourseChoice> getCourseChoices(Course course, Boolean waitingList)
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
	public Collection<Course> getAllCourses(School provider) throws RemoteException;

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
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllSchoolTypes
	 */
	public Collection<SchoolType> getAllSchoolTypes() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllSchoolTypes
	 */
	public Collection<SchoolType> getAllSchoolTypes(String category) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getSchoolAreas
	 */
	public Collection<SchoolArea> getSchoolAreas() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getSchoolTypes
	 */
	public Collection<SchoolType> getSchoolTypes(School provider) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCoursePrices
	 */
	public Collection<CoursePrice> getAllCoursePrices() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#isRegisteredAtProviders
	 */
	public boolean isRegisteredAtProviders(User user, Collection<School> providers)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getNumberOfFreePlaces
	 */
	public int getNumberOfFreePlaces(Course course) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProviders
	 */
	public Collection<School> getProviders() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProviders
	 */
	public Collection<School> getProviders(String category) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProviders
	 */
	public Collection<School> getProviders(SchoolArea area, SchoolType type)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProvidersDWR
	 */
	public List<AdvancedProperty> getProvidersDWR(int userPK, int typePK, String country) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProvidersForUser
	 */
	public Collection<School> getProvidersForUser(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getApplicationMap
	 */
	public Map getApplicationMap(User user, Collection<School> schools)
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
	public float getCalculatedCourseCertificateFees(Map applications)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getDiscounts
	 */
	public Map<User, PriceHolder> getDiscounts(String uuid, SortedSet<PriceHolder> userPrices, Map<User, Collection<ApplicationHolder>> applications) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#hasAvailableCourses
	 */
	public boolean hasAvailableCourses(User user, SchoolType type)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#hasAvailableCourses
	 */
	public boolean hasAvailableCourses(User user, CourseType type)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#hasAvailableCourses
	 */
	public boolean hasAvailableCourses(User user, School school) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#hasAvailableCourses
	 */
	public boolean hasAvailableCourses(User user, School school, CourseType type) throws RemoteException;

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
	public void invalidateApplication(CourseApplication application, User performer, Locale locale) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#invalidateChoice
	 */
	public void invalidateChoice(CourseApplication application, CourseChoice choice, Locale locale, User performer) throws RemoteException;

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
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getNumberOfCourses
	 */
	public int getNumberOfCourses(School provider, SchoolType schoolType,
			CourseType courseType, Date fromDate, Date toDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getNumberOfChoices
	 */
	public int getNumberOfChoices(School provider, SchoolType schoolType,
			Gender gender, Date fromDate, Date toDate) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getNumberOfChoices
	 */
	public int getNumberOfChoices(Course course, Gender gender)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection<Course> getCourses(School provider, SchoolType schoolType,
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
	public List getUserCertificates(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getUserCertificate
	 */
	public CourseCertificate getUserCertificate(User user, Course course)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getUserCertificatesByCourse
	 */
	public List getUserCertificatesByCourse(User user, Course course)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getLatestExpirationDateOfCertificate
	 */
	public IWTimestamp getLatestExpirationDateOfCertificate(List certificates)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getLatestValidCertificate
	 */
	public IWTimestamp getLatestValidCertificate(List certificates)
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
	public List getCourseParticipantListRowData(CourseChoice choice,
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

	public boolean isDiscountDisabled(String prefix, String id);

	public List<User> getParentsForApplicant(CourseApplication application, User applicant);

	public void doResetCourseDiscountInformationHolder(String uuid);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#findAllCoursesByGroupsIdsAndDates
	 */
	public Collection<Course> findAllCoursesByGroupsIdsAndDates(Collection<Integer> groupsIds, Date periodFrom, Date periodTo);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#findAllCoursesByCriteria
	 */
	public Collection<Course> findAllCoursesByCriteria(Collection<Integer> groupsIds,
														Collection<Integer> templateIds,
														java.util.Date periodFrom,
														java.util.Date periodTo,
														Integer birthYear,
														String sortBy,
														String nameOrNumber,
														Boolean openForRegistration,
														boolean findTemplates);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#findAllCoursesByCriteria
	 */
	public Collection<Course> findAllCoursesByCriteria(Collection<Integer> groupsIds,
														java.util.Date periodFrom,
														java.util.Date periodTo,
														Integer birthYear,
														String sortBy,
														String nameOrNumber,
														Boolean openForRegistration,
														Boolean birthYearShouldBeNull,
														Boolean checkByExactCourseName,
														Boolean onlyNotFinished);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#makePayment
	 */
	public String makePayment(String nameOnCard, String cardNumber,
			String monthExpires, String yearExpires, String ccVerifyNumber,
			double amount, String currency, String referenceNumber)
			throws CreditCardAuthorizationException, RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#createApplication
	 */
	public CourseApplication createApplication(Course course, User user, String prefix, User performer, Locale locale);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#createCourseChoice
	 */
	public CourseChoice createCourseChoice(Course course, User user);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#findAllCoursesByTemplateIds
	 */
	public Collection<Course> findAllCoursesByTemplateIds(Collection<Integer> templateIds);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#findAllCoursesByTemplateIds
	 */
	public Collection<Course> getAllCoursesWithoutTemplates();

	public void storeCourseType(Object pk, String name, String description,
			String localizationKey, Object schoolTypePK, String accountingKey, boolean disabled, String registrationMethod)
			throws FinderException, CreateException;

	public Course createCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration,
			IWTimestamp registrationEnd, boolean hasPreCare, boolean hasPostCare,
			IWTimestamp registrationStart)
			throws FinderException, CreateException;
}