package is.idega.idegaweb.egov.course.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;
import is.idega.block.family.data.Child;
import is.idega.block.family.data.Custodian;
import is.idega.idegaweb.egov.accounting.business.CitizenBusiness;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseApplication;
import is.idega.idegaweb.egov.course.data.CourseApplicationHome;
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
import is.idega.idegaweb.egov.message.business.CommuneMessageBusiness;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.trade.data.CreditCardInformation;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.Gender;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;
import com.idega.util.text.SocialSecurityNumber;

public class CourseBusinessBean extends CaseBusinessBean implements CaseBusiness, CourseBusiness {

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.egov.course";

	protected String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public String getLocalizedCaseDescription(Case theCase, Locale locale) {
		/*
		 * CourseApplication choice = getCourseApplicationInstance(theCase);
		 * 
		 * Object[] arguments = { "" };
		 * 
		 * String desc = super.getLocalizedCaseDescription(theCase, locale); return MessageFormat.format(desc, arguments);
		 */
		return super.getLocalizedCaseDescription(theCase, locale);
	}

	protected CourseApplication getCourseApplicationInstance(Case theCase) throws RuntimeException {
		try {
			return this.getCourseApplication(theCase.getPrimaryKey());
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void finishPayment(String properties) throws CreditCardAuthorizationException {
		try {
			CreditCardClient client = getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant());
			client.finishTransaction(properties);
		}
		catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException("Online payment failed. Unknown error.");
		}
	}

	public String authorizePayment(String nameOnCard, String cardNumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount, String currency, String referenceNumber) throws CreditCardAuthorizationException {
		try {
			CreditCardClient client = getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant());
			return client.creditcardAuthorization(nameOnCard, cardNumber, monthExpires, yearExpires, ccVerifyNumber, amount, currency, referenceNumber);
		}
		catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException("Online payment failed. Unknown error.");
		}
	}

	public String refundPayment(CourseApplication application, String cardNumber, String monthExpires, String yearExpires, String ccVerifyNumber, double amount) throws CreditCardAuthorizationException {
		try {
			CreditCardAuthorizationEntry ccAuthEntry = getCreditCardBusiness().getAuthorizationEntry(getCreditCardInformation(), application.getReferenceNumber(), new IWTimestamp(application.getCreated()));

			CreditCardClient client = getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant());
			return client.doRefund(cardNumber, monthExpires, yearExpires, ccVerifyNumber, amount, ccAuthEntry.getCurrency(), ccAuthEntry.getPrimaryKey(), ccAuthEntry.getExtraField());
		}
		catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException("Online payment failed. Unknown error.");
		}
	}

	private CreditCardInformation getCreditCardInformation() throws FinderException {
		String merchantPK = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_MERCHANT_PK);
		String merchantType = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_MERCHANT_TYPE);
		if (merchantPK != null && merchantType != null) {
			try {
				return getCreditCardBusiness().getCreditCardInformation(merchantPK, merchantType);
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return null;
	}

	private CreditCardMerchant getCreditCardMerchant() throws FinderException {
		String merchantPK = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_MERCHANT_PK);
		String merchantType = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_MERCHANT_TYPE);
		if (merchantPK != null && merchantType != null) {
			try {
				return getCreditCardBusiness().getCreditCardMerchant(merchantPK, merchantType);
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return null;
	}

	public Collection getCreditCardImages() {
		try {
			return getCreditCardBusiness().getCreditCardTypeImages(getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	private CreditCardBusiness getCreditCardBusiness() {
		try {
			return (CreditCardBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CreditCardBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public boolean deleteCourseType(Object pk) {
		CourseType type = null;
		try {
			type = getCourseTypeHome().findByPrimaryKey(new Integer(pk.toString()));
			type.remove();

			return true;
		}
		catch (javax.ejb.FinderException fe) {
			fe.printStackTrace();
		}
		catch (RemoveException re) {
			re.printStackTrace();
		}

		return false;
	}

	public void storeCourseType(Object pk, String name, String description, String localizationKey, Object schoolTypePK, String accountingKey) throws FinderException, CreateException {
		CourseType type = null;
		if (pk == null) {
			type = getCourseTypeHome().create();
		}
		else {
			type = getCourseTypeHome().findByPrimaryKey(new Integer(pk.toString()));
		}

		if (name != null && !"".equals(name)) {
			type.setName(name);
		}

		if (description != null && !"".equals(description)) {
			type.setDescription(description);
		}

		if (localizationKey != null && !"".equals(localizationKey)) {
			type.setLocalizationKey(localizationKey);
		}

		type.setAccountingKey(accountingKey);

		if (schoolTypePK != null) {
			try {
				SchoolType schoolType = getSchoolBusiness().getSchoolType(schoolTypePK);
				type.setSchoolType(schoolType);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		type.store();
	}

	public boolean deleteCourse(Object pk) throws RemoteException {
		Course course = null;
		try {
			course = getCourseHome().findByPrimaryKey(new Integer(pk.toString()));
			course.remove();
			return true;
		}
		catch (javax.ejb.FinderException fe) {
			fe.printStackTrace();
		}
		catch (RemoveException re) {
			re.printStackTrace();
		}

		return false;
	}

	public void storeCourse(Object pk, String name, String user, Object courseTypePK, Object providerPK, Object coursePricePK, IWTimestamp startDate, String accountingKey, int birthYearFrom, int birthYearTo, int maxPer) throws FinderException, CreateException {
		Course course = null;
		if (pk != null) {
			course = getCourseHome().findByPrimaryKey(new Integer(pk.toString()));
		}
		else {
			course = getCourseHome().create();
		}

		course.setName(name);

		if (courseTypePK != null) {
			CourseType type = getCourseType(courseTypePK);
			course.setCourseType(type);
		}

		if (providerPK != null) {
			try {
				School provider = getSchoolBusiness().getSchool(providerPK);
				course.setProvider(provider);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		course.setUser(user);

		if (coursePricePK != null) {
			course.setPrice(getCoursePrice(new Integer(coursePricePK.toString())));
		}

		if (startDate != null) {
			course.setStartDate(startDate.getTimestamp());
		}

		if (accountingKey != null) {
			course.setAccountingKey(accountingKey);
		}

		if (birthYearFrom > 0) {
			course.setBirthyearFrom(birthYearFrom);
		}

		if (birthYearTo > 0) {
			course.setBirthyearTo(birthYearTo);
		}

		if (maxPer > 0) {
			course.setMax(maxPer);
		}

		course.store();
	}

	public boolean deleteCoursePrice(Object pk) throws RemoteException {
		CoursePrice price = null;
		try {
			price = getCoursePriceHome().findByPrimaryKey(new Integer(pk.toString()));
			price.setValid(false);
			price.store();

			return true;
		}
		catch (javax.ejb.FinderException fe) {
			fe.printStackTrace();
		}

		return false;
	}

	public boolean deleteCourseDiscount(Object pk) throws RemoteException {
		CourseDiscount discount = null;
		try {
			discount = getCourseDiscountHome().findByPrimaryKey(new Integer(pk.toString()));
			discount.setValid(false);
			discount.store();

			return true;
		}
		catch (javax.ejb.FinderException fe) {
			fe.printStackTrace();
		}

		return false;
	}

	public void storeCoursePrice(Object pk, String name, int numberOfDays, Timestamp validFrom, Timestamp validTo, int iPrice, int preCarePrice, int postCarePrice, Object schoolAreaPK, Object courseTypePK) throws CreateException, NumberFormatException, FinderException {
		CoursePrice price = null;
		if (pk != null) {
			price = getCoursePriceHome().findByPrimaryKey(new Integer(pk.toString()));
			price.setValid(false);
			price.store();
		}

		price = getCoursePriceHome().create();

		price.setPreCarePrice(preCarePrice);
		price.setPostCarePrice(postCarePrice);

		if (name != null && !"".equals(name)) {
			price.setName(name);
		}

		if (numberOfDays > -1) {
			price.setNumberOfDays(numberOfDays);
		}

		if (validFrom != null) {
			price.setValidFrom(validFrom);
		}

		if (validTo != null) {
			price.setValidTo(validTo);
		}

		if (iPrice > -1) {
			price.setPrice(iPrice);
		}

		if (schoolAreaPK != null) {
			try {
				SchoolArea area = getSchoolBusiness().getSchoolArea(schoolAreaPK);
				price.setSchoolArea(area);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		if (courseTypePK != null) {
			CourseType type = getCourseType(courseTypePK);
			price.setCourseType(type);
		}

		price.store();
	}

	public void storeCourseDiscount(Object pk, String name, String type, Timestamp validFrom, Timestamp validTo, float discount) throws CreateException, NumberFormatException, FinderException {
		CourseDiscount courseDiscount = null;
		if (pk != null) {
			courseDiscount = getCourseDiscountHome().findByPrimaryKey(new Integer(pk.toString()));
			courseDiscount.setValid(false);
			courseDiscount.store();
		}

		courseDiscount = getCourseDiscountHome().create();

		if (name != null && !"".equals(name)) {
			courseDiscount.setName(name);
		}

		if (type != null && !"".equals(type)) {
			courseDiscount.setType(type);
		}

		if (validFrom != null) {
			courseDiscount.setValidFrom(validFrom);
		}

		if (validTo != null) {
			courseDiscount.setValidTo(validTo);
		}

		if (discount > -1) {
			courseDiscount.setDiscount(discount);
		}

		courseDiscount.store();
	}

	public Collection getCourseTypes(Integer schoolTypePK) {
		try {
			Collection coll = getCourseTypeHome().findAllBySchoolType(schoolTypePK);
			return coll;
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map getCourseTypesDWR(int schoolTypePK, String country) {
		Collection coll = getCourseTypes(new Integer(schoolTypePK));
		Map map = new LinkedHashMap();

		Locale locale = new Locale(country, country.toUpperCase());
		map.put(new Integer(-1), getLocalizedString("select_course_type", "Select course type", locale));

		if (coll != null) {
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				CourseType type = (CourseType) iter.next();
				map.put(type.getPrimaryKey(), type.getName());
			}
		}
		return map;
	}

	public Map getCourseMapDWR(int providerPK, int schoolTypePK, int courseTypePK, String country) {
		Collection coll = getCourses(-1, new Integer(providerPK), new Integer(schoolTypePK), new Integer(courseTypePK));
		Map map = new LinkedHashMap();
		if (coll != null) {
			Locale locale = new Locale(country, country.toUpperCase());
			map.put(new Integer(-1), getLocalizedString("select_course", "Select course", locale));

			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				Course course = (Course) iter.next();
				map.put(course.getPrimaryKey(), course.getName());
			}
		}
		return map;
	}

	public Map getCoursePricesDWR(String date, int providerPK, int courseTypePK, String country) {
		Map map = new LinkedHashMap();
		CourseType cType = getCourseType(new Integer(courseTypePK));
		try {
			School provider = getSchoolBusiness().getSchool(new Integer(providerPK));

			Collection prices = getCoursePriceHome().findAll(provider.getSchoolArea(), cType);
			IWTimestamp stamp = null;
			if (prices != null) {
				Locale locale = new Locale(country, country.toUpperCase());
				map.put("", getLocalizedString("select_course_price", "Select course price", locale));

				Iterator iter = prices.iterator();
				stamp = new IWTimestamp(date);
				while (iter.hasNext()) {
					CoursePrice price = (CoursePrice) iter.next();
					IWTimestamp from = new IWTimestamp(price.getValidFrom());
					IWTimestamp to = new IWTimestamp(price.getValidTo());
					if (stamp.isLaterThanOrEquals(from) && stamp.isEarlierThan(to)) {
						map.put(price.getPrimaryKey().toString(), price.getName());
					}
				}
			}
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		return map;
	}

	public CoursePriceDWR getPriceDWR(int pricePK) {
		try {
			CoursePrice price = getCoursePriceHome().findByPrimaryKey(new Integer(pricePK));

			CoursePriceDWR dwr = new CoursePriceDWR();
			dwr.setName(price.getName());
			dwr.setPk(price.getPrimaryKey().toString());
			dwr.setPrice(Integer.toString(price.getPrice()));
			dwr.setPreCarePrice(price.getPreCarePrice() > 0 ? Integer.toString(price.getPreCarePrice()) : "0");
			dwr.setPostCarePrice(price.getPostCarePrice() > 0 ? Integer.toString(price.getPostCarePrice()) : "0");

			return dwr;
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	public UserDWR getUserDWR(String personalID, int childPK, int minimumAge, String country) {
		return getUserDWR(personalID, childPK, minimumAge, country, null);
	}

	public UserDWR getUserDWR(String personalID, int childPK, int minimumAge, String country, String selectedRelation) {
		Locale locale = new Locale(country, country.toUpperCase());
		if (!SocialSecurityNumber.isValidSocialSecurityNumber(personalID, locale)) {
			return new UserDWR();
		}

		try {
			User user = getUserBusiness().getUser(personalID);
			Age age = new Age(user.getDateOfBirth());
			if (age.getYears() < minimumAge) {
				return new UserDWR();
			}

			User childUser = getUserBusiness().getUser(childPK);
			Child child = getFamilyLogic().getChild(childUser);

			Custodian custodian = getFamilyLogic().getCustodian(user);
			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
			Address address = getUserBusiness().getUsersMainAddress(user);
			PostalCode code = address != null ? address.getPostalCode() : null;

			Phone homePhone = null;
			try {
				homePhone = getUserBusiness().getUsersHomePhone(user);
			}
			catch (NoPhoneFoundException e) {
				// No phone found...
			}

			Phone workPhone = null;
			try {
				workPhone = getUserBusiness().getUsersWorkPhone(user);
			}
			catch (NoPhoneFoundException e1) {
				// No phone found...
			}

			Phone mobilePhone = null;
			try {
				mobilePhone = getUserBusiness().getUsersMobilePhone(user);
			}
			catch (NoPhoneFoundException e) {
				// No phone found...
			}

			Email email = null;
			try {
				email = getUserBusiness().getUsersMainEmail(user);
			}
			catch (NoEmailFoundException e) {
				// No email found...
			}

			UserDWR dwr = new UserDWR();
			dwr.setUserPK(user.getPrimaryKey().toString());
			dwr.setUserPersonalID(user.getPersonalID());
			dwr.setUserName(name.getName(locale));
			if (address != null) {
				dwr.setUserAddress(address.getStreetAddress());
			}
			if (code != null) {
				dwr.setUserPostalCode(code.getPostalAddress());
			}
			if (homePhone != null) {
				dwr.setUserHomePhone(homePhone.getNumber());
			}
			if (workPhone != null) {
				dwr.setUserWorkPhone(workPhone.getNumber());
			}
			if (mobilePhone != null) {
				dwr.setUserMobilePhone(mobilePhone.getNumber());
			}
			if (email != null) {
				dwr.setUserEmail(email.getEmailAddress());
			}
			if (child.getRelation(custodian) != null) {
				dwr.setUserRelation(child.getRelation(custodian));
			}
			else if (selectedRelation != null) {
				dwr.setUserRelation(selectedRelation);
			}
			dwr.setUserMaritalStatus(custodian.getMaritalStatus());

			return dwr;
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			UserDWR user = new UserDWR();
			user.setUserName(getLocalizedString("invalid_personal_id", "Invalid personal ID", locale));
			return user;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getCoursesDWR(int providerPK, int schoolTypePK, int courseTypePK, int applicantPK, String country) {
		try {
			User applicant = getUserBusiness().getUser(applicantPK);
			IWTimestamp birth = new IWTimestamp(applicant.getDateOfBirth());

			Integer iP = null;
			if (providerPK > -1) {
				iP = new Integer(providerPK);
			}
			Integer iST = null;
			if (schoolTypePK > -1) {
				iST = new Integer(schoolTypePK);
			}
			Integer iCT = null;
			if (courseTypePK > -1) {
				iCT = new Integer(courseTypePK);
			}
			else {
				return new ArrayList();
			}
			Locale locale = new Locale(country, country.toUpperCase());

			IWTimestamp stamp = new IWTimestamp();
			Map map = new LinkedHashMap();
			Collection courses = getCourses(birth.getYear(), iP, iST, iCT);
			if (courses != null) {
				Iterator iter = courses.iterator();
				while (iter.hasNext()) {
					Course course = (Course) iter.next();
					IWTimestamp start = new IWTimestamp(course.getStartDate());

					if (start.isLaterThan(stamp) && !isRegistered(applicant, course) && !isFull(course)) {
						CourseDWR cDWR = getCourseDWR(locale, course);
						map.put(course.getPrimaryKey(), cDWR);
					}
				}
			}

			return map.values();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private boolean isFull(Course course) {
		return course.getFreePlaces() > 0;
	}

	private boolean isRegistered(User user, Course course) {
		try {
			return getCourseChoiceHome().getCountByUserAndCourse(user, course) > 0;
		}
		catch (IDOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public CourseDWR getCourseDWR(Locale locale, Course course) {
		CourseDWR cDWR = new CourseDWR();
		cDWR.setName(course.getName());
		cDWR.setPk(course.getPrimaryKey().toString());
		cDWR.setFrom(Integer.toString(course.getBirthyearFrom()));
		cDWR.setTo(Integer.toString(course.getBirthyearTo()));
		cDWR.setDescription(course.getDescription());
		cDWR.setProvider(course.getProvider().getName());

		IWTimestamp from = new IWTimestamp(course.getStartDate());

		String toS = "";
		String dayS = "";
		CoursePrice price = course.getPrice();
		if (from != null && price != null) {
			IWTimestamp toDate = new IWTimestamp(getEndDate(price, from.getDate()));
			dayS = Integer.toString(price.getNumberOfDays());
			toS = toDate.getDateString("dd.MM.yyyy", locale);
			cDWR.setPrice(price.getPrice() + " ISK");
		}
		cDWR.setTimeframe(from.getDateString("dd.MM.yyyy", locale) + " - " + toS);
		cDWR.setDays(dayS);
		return cDWR;
	}

	public Collection getCourses(int birthYear, Object schoolTypePK, Object courseTypePK) {
		return getCourses(birthYear, null, schoolTypePK, courseTypePK);
	}

	public Collection getCourses(int birthYear, Object providerPK, Object schoolTypePK, Object courseTypePK) {
		Collection courses = new ArrayList();
		try {
			courses = getCourseHome().findAll(providerPK, schoolTypePK, courseTypePK, birthYear);
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return courses;
	}

	public Collection getCourses(Collection providers, Object schoolTypePK, Object courseTypePK) {
		Collection courses = new ArrayList();
		try {
			courses = getCourseHome().findAll(providers, schoolTypePK, courseTypePK);
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return courses;
	}

	public CourseChoice getCourseChoice(Object courseChoicePK) {
		try {
			return getCourseChoiceHome().findByPrimaryKey(courseChoicePK);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getCourseChoices(Object coursePK) {
		Course course = getCourse(coursePK);
		if (course != null) {
			return getCourseChoices(course);
		}

		return new ArrayList();
	}

	public Collection getCourseChoices(Course course) {
		try {
			return getCourseChoiceHome().findAllByCourse(course);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getCourseChoices(CourseApplication application) {
		try {
			return getCourseChoiceHome().findAllByApplication(application);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public CourseDiscount getCourseDiscount(Object pk) {
		if (pk != null) {
			try {
				return getCourseDiscountHome().findByPrimaryKey(new Integer(pk.toString()));
			}
			catch (javax.ejb.FinderException fe) {
			}
		}
		return null;
	}

	public CoursePrice getCoursePrice(Object pk) {
		if (pk != null) {
			try {
				return getCoursePriceHome().findByPrimaryKey(new Integer(pk.toString()));
			}
			catch (javax.ejb.FinderException fe) {
			}
		}
		return null;
	}

	public Collection getCoursePrices(Date fromDate, Date toDate) {
		try {
			return getCoursePriceHome().findAll(fromDate, toDate);
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public Collection getCourseDiscounts(Date fromDate, Date toDate) {
		try {
			return getCourseDiscountHome().findAll(fromDate, toDate);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public Course getCourse(Object pk) {
		if (pk != null) {
			try {
				return getCourseHome().findByPrimaryKey(new Integer(pk.toString()));
			}
			catch (javax.ejb.FinderException fe) {
			}
		}
		return null;
	}

	public CourseType getCourseType(Object pk) {
		if (pk != null) {
			try {
				return getCourseTypeHome().findByPrimaryKey(new Integer(pk.toString()));
			}
			catch (javax.ejb.FinderException fe) {
			}
		}
		return null;
	}

	public CourseApplication getCourseApplication(Object courseApplicationPK) {
		try {
			return getCourseApplicationHome().findByPrimaryKey(courseApplicationPK);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection getAllCourses() {
		try {
			return getCourseHome().findAll();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public Collection getAllCourses(School provider) {
		try {
			return getCourseHome().findAllByProvider(provider);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public Collection getAllCourseTypes() {
		try {
			return getCourseTypeHome().findAll();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public Collection getAllSchoolTypes() {
		try {
			Collection schoolTypes = getSchoolBusiness().findAllSchoolTypesInCategory(getSchoolBusiness().getAfterSchoolCareSchoolCategory());

			Object typePK = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_HIDDEN_SCHOOL_TYPE);
			if (typePK != null) {
				SchoolType type = getSchoolBusiness().getSchoolType(new Integer(typePK.toString()));
				schoolTypes.remove(type);
			}

			return schoolTypes;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getSchoolAreas() {
		try {
			return getSchoolBusiness().getSchoolAreaHome().findAllSchoolAreas(getSchoolBusiness().getCategoryAfterSchoolCare());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public Collection getSchoolTypes(School provider) {
		try {
			Collection types = new ArrayList();

			try {
				Collection schoolTypes = provider.getSchoolTypes();

				String typePK = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_HIDDEN_SCHOOL_TYPE);
				if (typePK != null) {
					SchoolType type = getSchoolBusiness().getSchoolType(new Integer(typePK));
					schoolTypes.remove(type);
				}

				Iterator iterator = schoolTypes.iterator();
				while (iterator.hasNext()) {
					SchoolType type = (SchoolType) iterator.next();
					if (type.getCategory().equals(getSchoolBusiness().getCategoryAfterSchoolCare())) {
						types.add(type);
					}
				}
			}
			catch (IDORelationshipException e) {
				e.printStackTrace();
			}

			return types;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getAllCoursePrices() {
		try {
			return getCoursePriceHome().findAll();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	public boolean isRegisteredAtProviders(User user, Collection providers) {
		try {
			return getCourseChoiceHome().getCountByUserAndProviders(user, providers) > 0;
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
	}

	public int getNumberOfFreePlaces(Course course) {
		try {
			return course.getMax() - getCourseChoiceHome().getCountByCourse(course);
		}
		catch (IDOException e) {
			e.printStackTrace();
		}

		return course.getMax();
	}

	public Collection getProviders() {
		try {
			return getSchoolBusiness().findAllSchoolsByType(getAllSchoolTypes());
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getProviders(SchoolArea area, SchoolType type) {
		try {
			return getSchoolBusiness().findAllSchoolsByAreaAndType(((Integer) area.getPrimaryKey()).intValue(), ((Integer) type.getPrimaryKey()).intValue());
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getProvidersForUser(User user) {
		try {
			SchoolUser schoolUser = getSchoolUserBusiness().getSchoolUserHome().findForUser(user);
			return schoolUser.getSchools();
		}
		catch (IDORelationshipException ire) {
			ire.printStackTrace();
			return new ArrayList();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Map getApplicationMap(User user, Collection schools) {
		Map map = new LinkedHashMap();
		try {
			Collection choices = getCourseChoiceHome().findAllByUserAndProviders(user, schools);
			Iterator iterator = choices.iterator();
			while (iterator.hasNext()) {
				CourseChoice choice = (CourseChoice) iterator.next();
				CourseApplication application = choice.getApplication();

				Collection applicationChoices;
				if (map.containsKey(application)) {
					applicationChoices = (Collection) map.get(application);
				}
				else {
					applicationChoices = new ArrayList();
				}

				applicationChoices.add(choice);
				map.put(application, applicationChoices);
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return map;
	}

	public Map getApplicationMap(CourseApplication application) {
		Map map = new HashMap();

		try {
			Collection choices = getCourseChoiceHome().findAllByApplication(application);
			Iterator iterator = choices.iterator();
			while (iterator.hasNext()) {
				CourseChoice choice = (CourseChoice) iterator.next();
				User user = choice.getUser();
				Course course = choice.getCourse();

				ApplicationHolder holder = new ApplicationHolder();
				holder.setCourse(course);
				holder.setUser(user);
				holder.setDaycare(choice.getDayCare());
				holder.setPickedUp(new Boolean(choice.isPickedUp()));
				holder.setChoicePK(choice.getPrimaryKey());

				Collection holders = null;
				if (map.containsKey(user)) {
					holders = (Collection) map.get(user);
				}
				else {
					holders = new ArrayList();
				}

				holders.add(holder);
				map.put(user, holders);
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return map;
	}

	public float calculateRefund(CourseChoice choice) {
		User user = choice.getUser();
		Course course = choice.getCourse();

		ApplicationHolder holder = new ApplicationHolder();
		holder.setCourse(course);
		holder.setUser(user);
		holder.setDaycare(choice.getDayCare());
		holder.setPickedUp(new Boolean(choice.isPickedUp()));
		holder.setChoicePK(choice.getPrimaryKey());

		CourseApplication application = choice.getApplication();
		Map applications = getApplicationMap(application);

		float price = holder.getPrice();
		if (hasSiblingInSet(applications.keySet(), user)) {
			price = price * 0.8f;
		}

		return price;
	}

	public SortedSet calculatePrices(Map applications) {
		SortedSet userPrices = new TreeSet();

		Iterator iterator = applications.keySet().iterator();
		while (iterator.hasNext()) {
			User user = (User) iterator.next();
			Collection userApplications = (Collection) applications.get(user);
			Iterator iter = userApplications.iterator();
			int totalPrice = 0;
			while (iter.hasNext()) {
				ApplicationHolder holder = (ApplicationHolder) iter.next();
				totalPrice += holder.getPrice();
			}

			PriceHolder priceHolder = new PriceHolder();
			priceHolder.setUser(user);
			priceHolder.setPrice(totalPrice);
			userPrices.add(priceHolder);
		}

		return userPrices;
	}

	public Map getDiscounts(SortedSet userPrices, Map applications) {
		Map discountPrices = new HashMap();
		Iterator iterator = userPrices.iterator();
		boolean first = true;
		while (iterator.hasNext()) {
			PriceHolder priceHolder = (PriceHolder) iterator.next();
			User applicant = priceHolder.getUser();
			float price = priceHolder.getPrice();

			PriceHolder discountHolder = new PriceHolder();
			discountHolder.setUser(applicant);
			discountHolder.setPrice(0);

			if (!first) {
				if (hasSiblingInSet(applications.keySet(), applicant)) {
					priceHolder.setPrice(price * 0.8f);
				}
			}
			else {
				first = false;
			}

			discountPrices.put(applicant, discountHolder);
		}

		return discountPrices;
	}

	private boolean hasSiblingInSet(Set set, User applicant) {
		Iterator iter = set.iterator();
		while (iter.hasNext()) {
			User user = (User) iter.next();
			if (!applicant.equals(user)) {
				try {
					if (getFamilyLogic().isSiblingOf(applicant, user)) {
						return true;
					}
				}
				catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
			}
		}

		return false;
	}

	public boolean hasAvailableCourses(User user, SchoolType type) {
		try {
			IWTimestamp stamp = new IWTimestamp(user.getDateOfBirth());
			IWTimestamp stampNow = new IWTimestamp();
			return getCourseHome().getCountBySchoolTypeAndBirthYear(type != null ? type.getPrimaryKey() : null, stamp.getYear(), stampNow.getDate()) > 0;
		}
		catch (IDOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private int getInvalidateInterval() {
		int interval = Integer.parseInt(getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_INVALIDATE_INTERVAL, "4"));
		return interval;
	}

	public boolean canInvalidate(CourseChoice choice) {
		IWTimestamp startDate = new IWTimestamp(choice.getCourse().getStartDate());
		IWTimestamp dateNow = new IWTimestamp();
		dateNow.addDays(getInvalidateInterval());

		if (dateNow.isEarlierThan(startDate)) {
			return true;
		}
		return false;
	}

	public boolean canInvalidate(CourseApplication application) {
		try {
			CourseChoice choice = getCourseChoiceHome().findFirstChoiceByApplication(application);
			return canInvalidate(choice);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void sendRefundMessage(CourseApplication application, CourseChoice choice, Locale locale) {
		String subject = "";
		String body = "";

		if (choice != null) {
			User applicant = choice.getUser();
			Course course = choice.getCourse();
			School provider = course.getProvider();
			Object[] arguments = { applicant.getName(), PersonalIDFormatter.format(applicant.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale()), course.getName(), provider.getName() };

			subject = getLocalizedString("course_choice.choice_refund_subject", "Choice invalidated", locale);
			body = MessageFormat.format(getLocalizedString("course_choice.choice_refund_body", "A choice for course {2} at {3} for {0}, {1} has been invalidated and needs to be refunded", locale), arguments);
		}
		else {
			User payer = null;
			if (application.getPayerPersonalID() != null) {
				try {
					payer = getUserBusiness().getUser(application.getPayerPersonalID());
				}
				catch (FinderException e) {
					e.printStackTrace();
					payer = application.getOwner();
				}
				catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
			}
			else {
				payer = application.getOwner();
			}
			Object[] arguments = { payer.getName(), PersonalIDFormatter.format(payer.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale()) };

			subject = getLocalizedString("course_choice.application_refund_subject", "Application invalidated", locale);
			body = MessageFormat.format(getLocalizedString("course_choice.application_refund_body", "An application has been invalidated and needs to be refunded to payer {0}, {1}", locale), arguments);
		}

		try {
			String refundEmail = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_REFUND_EMAIL, "fjarmaladeild@itr.is");
			getMessageBusiness().sendMessage(refundEmail, subject, body);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void invalidateApplication(CourseApplication application, User performer, Locale locale) {
		Collection choices = getCourseChoices(application);
		Iterator iterator = choices.iterator();
		while (iterator.hasNext()) {
			CourseChoice choice = (CourseChoice) iterator.next();
			invalidateChoice(application, choice, locale);
		}

		changeCaseStatus(application, getCaseStatusCancelled(), performer);
	}

	public void invalidateChoice(CourseApplication application, CourseChoice choice, Locale locale) {
		choice.setValid(false);
		choice.store();

		String subject = getLocalizedString("course_choice.registration_invalidated", "Your registration for course has been invalidated", locale);
		String body = "";
		if (application.getPaymentType().equals(CourseConstants.PAYMENT_TYPE_CARD)) {
			body = getLocalizedString("course_choice.card_registration_invalidated_body", "Your registration for course {2} at {3} for {0}, {1} has been invalidated and you have been refunded to your credit card", locale);
		}
		else if (application.getPaymentType().equals(CourseConstants.PAYMENT_TYPE_GIRO)) {
			body = getLocalizedString("course_choice.giro_registration_invalidated_body", "Your registration for course {2} at {3} for {0}, {1} has been invalidated.  If you have already paid for the course you will receive repayment shortly.", locale);
		}

		sendMessageToParents(application, choice, subject, body);
	}

	public CourseApplication saveApplication(Map applications, int merchantID, String merchantType, String paymentType, String referenceNumber, String payerName, String payerPersonalID, User performer, Locale locale) {
		try {
			CourseApplication application = getCourseApplicationHome().create();
			application.setCreditCardMerchantID(merchantID);
			application.setCreditCardMerchantType(merchantType);
			application.setPaymentType(paymentType);
			application.setPaid(paymentType.equals(CourseConstants.PAYMENT_TYPE_CARD));
			application.setReferenceNumber(referenceNumber);
			application.setPayerName(payerName);
			application.setPayerPersonalID(payerPersonalID);
			application.setOwner(performer);
			changeCaseStatus(application, getCaseStatusOpen(), performer);

			String subject = getLocalizedString("course_choice.registration_received", "Your registration for course has been received", locale);
			String body = "";
			if (paymentType.equals(CourseConstants.PAYMENT_TYPE_CARD)) {
				body = getLocalizedString("course_choice.card_registration_body", "Your registration for course {2} at {3} for {0}, {1} has been received and paid for with your credit card", locale);
			}
			else if (paymentType.equals(CourseConstants.PAYMENT_TYPE_GIRO)) {
				body = getLocalizedString("course_choice.giro_registration_body", "Your registration for course {2} at {3} for {0}, {1} has been received.  You will receive an invoice in a few days for the total amount of the registration.", locale);
			}

			Iterator iter = applications.values().iterator();
			while (iter.hasNext()) {
				Collection collection = (Collection) iter.next();
				Iterator iterator = collection.iterator();
				while (iterator.hasNext()) {
					ApplicationHolder holder = (ApplicationHolder) iterator.next();

					CourseChoice choice = getCourseChoiceHome().create();
					choice.setApplication(application);
					choice.setCourse(holder.getCourse());
					choice.setDayCare(holder.getDaycare());
					choice.setPickedUp(holder.getPickedUp().booleanValue());
					choice.setUser(holder.getUser());
					choice.store();

					sendMessageToParents(application, choice, subject, body);
				}
			}

			return application;
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void sendMessageToParents(CourseApplication application, CourseChoice choice, String subject, String body) {
		try {
			User applicant = choice.getUser();
			Course course = choice.getCourse();
			School provider = course.getProvider();
			Object[] arguments = { applicant.getName(), PersonalIDFormatter.format(applicant.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale()), course.getName(), provider.getName() };

			User appParent = application.getOwner();
			if (getFamilyLogic().isChildInCustodyOf(applicant, appParent)) {
				getMessageBusiness().createUserMessage(application, appParent, subject, MessageFormat.format(body, arguments), false, false);
			}

			try {
				Collection parents = getFamilyLogic().getCustodiansFor(applicant);
				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					if (!getUserBusiness().haveSameAddress(parent, appParent)) {
						getMessageBusiness().createUserMessage(application, parent, subject, MessageFormat.format(body, arguments), false, false);
					}
				}
			}
			catch (NoCustodianFound ncf) {
				getMessageBusiness().createUserMessage(application, applicant, subject, MessageFormat.format(body, arguments), false, false);
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	public int getNumberOfCourses(School provider, SchoolType schoolType, CourseType courseType, Date fromDate, Date toDate) {
		try {
			return getCourseHome().getCountByProviderAndSchoolTypeAndCourseType(provider, schoolType, courseType, fromDate, toDate);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int getNumberOfChoices(School provider, SchoolType schoolType, Gender gender, Date fromDate, Date toDate) {
		try {
			return getCourseChoiceHome().getCountByProviderAndSchoolTypeAndGender(provider, schoolType, gender, fromDate, toDate);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int getNumberOfChoices(Course course, Gender gender) {
		try {
			return getCourseChoiceHome().getCountByCourseAndGender(course, gender);
		}
		catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public Collection getCourses(School provider, SchoolType schoolType, Date fromDate, Date toDate) {
		try {
			return getCourseHome().findAllByProviderAndSchoolTypeAndCourseType(provider, schoolType, null, fromDate, toDate);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return new ArrayList();
		}
	}

	public Date getEndDate(CoursePrice price, Date startDate) {
		IWTimestamp stamp = new IWTimestamp(startDate);
		int days = price.getNumberOfDays() - 1;
		while (days > 0) {
			if (stamp.getDayOfWeek() != Calendar.SUNDAY && stamp.getDayOfWeek() != Calendar.SATURDAY) {
				days--;
			}
			stamp.addDays(1);
		}

		return stamp.getDate();
	}

	public CoursePriceHome getCoursePriceHome() {
		try {
			return (CoursePriceHome) IDOLookup.getHome(CoursePrice.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	public CourseDiscountHome getCourseDiscountHome() {
		try {
			return (CourseDiscountHome) IDOLookup.getHome(CourseDiscount.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	public CourseTypeHome getCourseTypeHome() {
		try {
			return (CourseTypeHome) IDOLookup.getHome(CourseType.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	public CourseHome getCourseHome() {
		try {
			return (CourseHome) IDOLookup.getHome(Course.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	public CourseChoiceHome getCourseChoiceHome() {
		try {
			return (CourseChoiceHome) IDOLookup.getHome(CourseChoice.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	public CourseApplicationHome getCourseApplicationHome() {
		try {
			return (CourseApplicationHome) IDOLookup.getHome(CourseApplication.class);
		}
		catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	private FamilyLogic getFamilyLogic() {
		try {
			return (FamilyLogic) IBOLookup.getServiceInstance(getIWApplicationContext(), FamilyLogic.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private CitizenBusiness getUserBusiness() {
		try {
			return (CitizenBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CitizenBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private SchoolUserBusiness getSchoolUserBusiness() {
		try {
			return (SchoolUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CommuneMessageBusiness getMessageBusiness() {
		try {
			return (CommuneMessageBusiness) this.getServiceInstance(CommuneMessageBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
}