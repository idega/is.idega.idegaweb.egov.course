package is.idega.idegaweb.egov.course.business;


import java.util.Map;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import is.idega.idegaweb.egov.course.data.CourseHome;
import is.idega.idegaweb.egov.course.data.CourseCertificateType;
import is.idega.idegaweb.egov.course.data.CourseChoiceHome;
import is.idega.idegaweb.egov.course.data.CourseTypeHome;
import com.idega.block.process.data.Case;
import is.idega.idegaweb.egov.course.data.CourseCertificate;
import com.idega.util.IWTimestamp;
import is.idega.idegaweb.egov.course.data.CourseCategory;
import java.util.Locale;
import java.util.SortedSet;
import com.idega.block.process.business.CaseBusiness;
import java.util.Collection;
import is.idega.idegaweb.egov.accounting.business.AccountingEntry;
import is.idega.idegaweb.egov.course.data.CourseCategoryHome;
import com.idega.block.school.data.School;
import is.idega.idegaweb.egov.accounting.business.AccountingBusiness;
import com.idega.block.school.data.SchoolArea;
import java.util.List;
import is.idega.idegaweb.egov.course.data.Course;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.business.IBOService;
import is.idega.idegaweb.egov.course.data.CourseDiscountHome;
import com.idega.user.data.Gender;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.data.CoursePriceHome;
import java.sql.Date;
import javax.ejb.CreateException;
import is.idega.idegaweb.egov.course.data.CourseApplicationHome;
import is.idega.idegaweb.egov.course.data.CourseType;
import com.idega.user.data.User;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CourseApplication;
import javax.ejb.FinderException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.school.data.SchoolType;
import is.idega.idegaweb.egov.course.data.CourseDiscount;

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
	public AccountingEntry[] getAccountingEntries(String productCode,
			String providerCode, Date fromDate, Date toDate);

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getLocalizedCaseDescription
	 */
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
			String localizationKey, Object schoolTypePK, String accountingKey)
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
	public Collection getCourseTypes(Integer schoolTypePK)
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
	public Collection getCoursesDWR(int providerPK, int schoolTypePK,
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
	public Collection getCourses(int birthYear, Object schoolTypePK,
			Object courseTypePK, Date fromDate, Date toDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection getCourses(int birthYear, Object schoolTypePK,
			Object courseTypePK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection getCourses(int birthYear, Object providerPK,
			Object schoolTypePK, Object courseTypePK, Date fromDate, Date toDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection getCourses(Collection providers, Object schoolTypePK,
			Object courseTypePK, Date fromDate, Date toDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection getCourses(int birthYear, Object providerPK,
			Object schoolTypePK, Object courseTypePK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourses
	 */
	public Collection getCourses(Collection providers, Object schoolTypePK,
			Object courseTypePK) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoice
	 */
	public CourseChoice getCourseChoice(Object courseChoicePK)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoices
	 */
	public Collection getCourseChoices(Object coursePK, boolean waitingList)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoices
	 */
	public Collection getCourseChoices(Course course, boolean waitingList)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoices
	 */
	public Collection getCourseChoices(CourseApplication application)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseChoices
	 */
	public Collection getCourseChoices(User user) throws RemoteException;

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
	public Collection getCoursePrices(Date fromDate, Date toDate)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCourseDiscounts
	 */
	public Collection getCourseDiscounts(Date fromDate, Date toDate)
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
	public Collection getAllCourses() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCourses
	 */
	public Collection getAllCourses(School provider) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCourseTypes
	 */
	public Collection getAllCourseTypes() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCourseTypes
	 */
	public Collection getAllCourseTypes(Integer schoolTypePK)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllSchoolTypes
	 */
	public Collection getAllSchoolTypes() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllSchoolTypes
	 */
	public Collection getAllSchoolTypes(String category) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getSchoolAreas
	 */
	public Collection getSchoolAreas() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getSchoolTypes
	 */
	public Collection getSchoolTypes(School provider) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getAllCoursePrices
	 */
	public Collection getAllCoursePrices() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#isRegisteredAtProviders
	 */
	public boolean isRegisteredAtProviders(User user, Collection providers)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getNumberOfFreePlaces
	 */
	public int getNumberOfFreePlaces(Course course) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProviders
	 */
	public Collection getProviders() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProviders
	 */
	public Collection getProviders(String category) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProviders
	 */
	public Collection getProviders(SchoolArea area, SchoolType type)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProvidersDWR
	 */
	public Map getProvidersDWR(int userPK, int typePK, String country) throws RemoteException;
	
	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getProvidersForUser
	 */
	public Collection getProvidersForUser(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getApplicationMap
	 */
	public Map getApplicationMap(User user, Collection schools)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getApplicationMap
	 */
	public Map getApplicationMap(CourseApplication application)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getApplicationMap
	 */
	public Map getApplicationMap(CourseApplication application,
			Boolean waitingList) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#calculateRefund
	 */
	public float calculateRefund(CourseChoice choice) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#calculatePrices
	 */
	public SortedSet calculatePrices(Map applications) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCalculatedCourseCertificateFees
	 */
	public float getCalculatedCourseCertificateFees(Map applications)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getDiscounts
	 */
	public Map getDiscounts(SortedSet userPrices, Map applications)
			throws RemoteException;

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
	public CourseApplication saveApplication(Map applications, String prefix, User performer,
			Locale locale) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#saveApplication
	 */
	public CourseApplication saveApplication(Map applications, int merchantID,
			float amount, String merchantType, String paymentType,
			String referenceNumber, String payerName, String payerPersonalID,
			String prefix, User owner, User performer, Locale locale, float certificateFee)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#saveApplication
	 */
	public CourseApplication saveApplication(Map applications, int merchantID,
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
	public Collection getCourses(School provider, SchoolType schoolType,
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
	public List getCheckBoxesForCourseParticipants(IWResourceBundle iwrb)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#acceptChoice
	 */
	public boolean acceptChoice(Object courseChoicePK, Locale locale)
			throws RemoteException;

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
	public Collection getAllChoicesByCourseAndDate(Object coursePK,
			IWTimestamp fromDate, IWTimestamp toDate) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.egov.course.business.CourseBusinessBean#getCoursesByTypes
	 */
	public Collection<Course> getCoursesByTypes(Collection<String> typesIds)
			throws RemoteException, RemoteException;
}