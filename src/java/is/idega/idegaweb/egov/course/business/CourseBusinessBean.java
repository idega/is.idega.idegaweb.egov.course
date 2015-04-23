package is.idega.idegaweb.egov.course.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;
import is.idega.block.family.data.Child;
import is.idega.block.family.data.Custodian;
import is.idega.idegaweb.egov.accounting.business.AccountingBusiness;
import is.idega.idegaweb.egov.accounting.business.AccountingEntry;
import is.idega.idegaweb.egov.accounting.business.AccountingKeyBusiness;
import is.idega.idegaweb.egov.accounting.business.CitizenBusiness;
import is.idega.idegaweb.egov.accounting.data.SchoolCode;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseApplication;
import is.idega.idegaweb.egov.course.data.CourseApplicationHome;
import is.idega.idegaweb.egov.course.data.CourseCategory;
import is.idega.idegaweb.egov.course.data.CourseCategoryHome;
import is.idega.idegaweb.egov.course.data.CourseCertificate;
import is.idega.idegaweb.egov.course.data.CourseCertificateHome;
import is.idega.idegaweb.egov.course.data.CourseCertificateType;
import is.idega.idegaweb.egov.course.data.CourseCertificateTypeHome;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean;
import is.idega.idegaweb.egov.course.data.CourseChoiceHome;
import is.idega.idegaweb.egov.course.data.CourseDiscount;
import is.idega.idegaweb.egov.course.data.CourseDiscountHome;
import is.idega.idegaweb.egov.course.data.CourseHome;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CoursePriceHome;
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.data.CourseTypeHome;
import is.idega.idegaweb.egov.course.data.PriceHolder;
import is.idega.idegaweb.egov.course.presentation.bean.CourseParticipantListRowData;
import is.idega.idegaweb.egov.message.business.CommuneMessageBusiness;
import is.idega.idegaweb.egov.message.business.MessageValue;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.TPosAuthorisationEntriesBean;
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
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.idgenerator.business.IdGeneratorFactory;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Image;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.Gender;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;
import com.idega.util.text.Name;
import com.idega.util.text.SocialSecurityNumber;

public class CourseBusinessBean extends CaseBusinessBean implements
		CaseBusiness, CourseBusiness, AccountingBusiness {

	private static final long serialVersionUID = 8639939641556682373L;

	private static final String EXTRA_ATTENTION_COURSE_TYPE = "Tegund";
	private static final String EXTRA_ATTENTION_COURSE_TYPE_ABBREVIATION = "B";

	@Override
	protected String getBundleIdentifier() {
		return CourseConstants.IW_BUNDLE_IDENTIFIER;
	}

	@Override
	public void reserveCourse(Course course, User user) {
		getLogger().info("Reserving course " + course.getName() + ", ID: " + course.getPrimaryKey() + " for " + user.getName());

		Map<Course, Integer> courseMap = (Map<Course, Integer>) getIWApplicationContext()
				.getApplicationAttribute(CourseConstants.APPLICATION_PROPERTY_COURSE_MAP);
		if (courseMap == null) {
			courseMap = new HashMap<Course, Integer>();
		}

		if (courseMap.containsKey(course)) {
			Integer numberOfChoices = courseMap.get(course);
			numberOfChoices = new Integer(numberOfChoices.intValue() + 1);
			courseMap.put(course, numberOfChoices);
		} else {
			courseMap.put(course, new Integer(1));
		}

		getIWApplicationContext().setApplicationAttribute(CourseConstants.APPLICATION_PROPERTY_COURSE_MAP, courseMap);
	}

	@Override
	public void removeReservation(Course course) {
		Map<Course, Integer> courseMap = (Map<Course, Integer>) getIWApplicationContext()
				.getApplicationAttribute(CourseConstants.APPLICATION_PROPERTY_COURSE_MAP);

		if (courseMap != null && courseMap.containsKey(course)) {
			Integer numberOfChoices = courseMap.get(course);
			numberOfChoices = new Integer(numberOfChoices.intValue() - 1);
			if (numberOfChoices.intValue() > 0) {
				courseMap.put(course, numberOfChoices);
				// System.out.println("Removing reservation for course " +
				// course.getName() + " (" + course.getPrimaryKey() + "): " +
				// numberOfChoices);
			} else {
				courseMap.remove(course);
				// System.out.println("Removing reservation for course " +
				// course.getName() + " (" + course.getPrimaryKey() + ")");
			}
		}

		getIWApplicationContext().setApplicationAttribute(CourseConstants.APPLICATION_PROPERTY_COURSE_MAP, courseMap);
	}

	@Override
	public int getNumberOfReservations(Course course) {
		Map<Course, Integer> courseMap = (Map<Course, Integer>) getIWApplicationContext()
				.getApplicationAttribute(CourseConstants.APPLICATION_PROPERTY_COURSE_MAP);

		if (courseMap != null && courseMap.containsKey(course)) {
			Integer numberOfChoices = courseMap.get(course);
			// System.out.println("Number of reservations for course " +
			// course.getName() + " (" + course.getPrimaryKey() + "): " +
			// numberOfChoices);
			return numberOfChoices.intValue();
		}

		return 0;
	}

	@Override
	public void printReservations() {
		Map<Course, Integer> courseMap = (Map<Course, Integer>) getIWApplicationContext()
				.getApplicationAttribute(CourseConstants.APPLICATION_PROPERTY_COURSE_MAP);
		if (courseMap != null) {
			getLogger().info("Reservations of courses: " + courseMap);
		}
	}

	private CourseCategory getAccountingSchoolType() {
		String typePK = getIWApplicationContext().getApplicationSettings()
				.getProperty(CourseConstants.PROPERTY_ACCOUNTING_TYPE_PK);
		if (typePK != null) {
			return getCourseCategory(typePK);
		}
		return null;
	}

	@Override
	public AccountingEntry[] getAccountingEntries(String productCode,
			String providerCode, Date fromDate, Date toDate) {
		Collection entries = new ArrayList();

		try {
			Class implementor = ImplementorRepository.getInstance()
					.getAnyClassImpl(AccountingEntry.class, this.getClass());
			CourseCategory category = getAccountingSchoolType();

			Collection applications = getCourseApplicationHome().findAll(
					getCaseStatusOpen(), fromDate, toDate);
			Iterator iterator = applications.iterator();
			while (iterator.hasNext()) {
				CourseApplication application = (CourseApplication) iterator
						.next();
				TPosAuthorisationEntriesBean ccAuthEntry = null;
				if (application.getPaymentType().equals(
						CourseConstants.PAYMENT_TYPE_CARD)) {
					IWTimestamp stamp = new IWTimestamp(application
							.getCreated());
					ccAuthEntry = (TPosAuthorisationEntriesBean) getCreditCardBusiness()
							.getAuthorizationEntry(getCreditCardInformation(),
									application.getReferenceNumber(), stamp);
					if (ccAuthEntry == null) {
						stamp.addDays(1);
						ccAuthEntry = (TPosAuthorisationEntriesBean) getCreditCardBusiness()
								.getAuthorizationEntry(
										getCreditCardInformation(),
										application.getReferenceNumber(), stamp);
					}
				}

				Map<User, Collection<ApplicationHolder>> applicationMap = getApplicationMap(application,
						new Boolean(false));
				SortedSet<PriceHolder> prices = calculatePrices(applicationMap);
				Map discounts = getDiscounts(prices, applicationMap);

				User owner = application.getOwner();
				Collection choices = getCourseChoices(application, new Boolean(
						false));
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					CourseChoice choice = (CourseChoice) iter.next();
					boolean noPayment = choice.isNoPayment();
					Course course = choice.getCourse();
					School provider = course.getProvider();
					SchoolArea area = provider.getSchoolArea();
					CourseType courseType = course.getCourseType();
					CourseCategory schoolType = courseType.getCourseCategory();
					if (category != null && !schoolType.equals(category)) {
						continue;
					}
					User student = choice.getUser();
					CoursePrice price = course.getPrice();
					String paymentType = application.getPaymentType();
					String batchNumber = ccAuthEntry != null ? ccAuthEntry
							.getBatchNumber() : null;
					// String cardName = ccAuthEntry != null ?
					// ccAuthEntry.getBrandName().substring(0, 4) : null;
					String courseName = course.getName();
					String uniqueID = application.getPrimaryKey().toString()
							+ "-" + choice.getPrimaryKey() + "-";
					Date startDate = new IWTimestamp(course.getStartDate())
							.getDate();
					Date endDate = price != null ? getEndDate(price, startDate)
							: new IWTimestamp(course.getEndDate()).getDate();

					float coursePrice = (price != null ? price.getPrice()
							: course.getCoursePrice())
							* (1 - ((PriceHolder) discounts.get(student))
									.getDiscount());

					String payerPId;
					if (application.getPayerPersonalID() != null
							&& application.getPayerPersonalID().length() > 0) {
						payerPId = application.getPayerPersonalID();
					} else {
						payerPId = owner.getPersonalID();
					}
					if (payerPId != null && payerPId.length() > 10) {
						payerPId = payerPId.trim();
						payerPId = StringHandler.replace(payerPId, "-", "");
						if (payerPId.length() > 10) {
							payerPId = payerPId.substring(0, 10);
						}
					}
					if (payerPId == null) {
						payerPId = "";
					}

					String studentPId = student.getPersonalID();
					if (studentPId.length() > 10) {
						studentPId = studentPId.trim();
						studentPId = StringHandler.replace(studentPId, "-", "");
						if (studentPId.length() > 10) {
							studentPId = studentPId.substring(0, 10);
						}
					}

					SchoolCode schoolCode = getAccountingBusiness()
							.getSchoolCode(provider, schoolType);
					providerCode = schoolCode != null ? schoolCode
							.getSchoolCode() : provider.getOrganizationNumber();

					String typeCode = courseType.getAccountingKey() != null ? courseType
							.getAccountingKey()
							: "NOTSET";
					String areaCode = area.getAccountingKey() != null ? area
							.getAccountingKey() : "NOTSET";

					try {
						Object o = implementor.newInstance();
						AccountingEntry entry = (AccountingEntry) o;
						entry
								.setProductCode(CourseConstants.PRODUCT_CODE_COURSE);
						entry.setProviderCode(providerCode);
						entry.setProjectCode(typeCode);
						entry.setPayerPersonalId(payerPId);
						entry.setPersonalId(studentPId);
						entry
								.setPaymentMethod(paymentType
										.equals(CourseConstants.PAYMENT_TYPE_CARD) ? application
										.getCardType().substring(0, 4)
										.toUpperCase()
										: "GIRO");
						if (paymentType
								.equals(CourseConstants.PAYMENT_TYPE_CARD)
								&& application.getCardNumber() != null) {
							entry.setCardExpirationMonth(application
									.getCardValidMonth());
							entry.setCardExpirationYear(application
									.getCardValidYear());
							entry.setCardNumber(application.getCardNumber());
							entry.setCardType(application.getCardType());
						}
						entry.setAmount((int) coursePrice);
						entry.setUnits(1);
						entry.setStartDate(startDate);
						entry.setEndDate(endDate);

						AccountingEntry extraEntry = (AccountingEntry) implementor
								.newInstance();
						extraEntry.setStartDate(application.getCreated());
						extraEntry.setProductCode(courseName);
						extraEntry.setProjectCode(batchNumber);
						extraEntry.setProviderCode(areaCode);
						extraEntry.setExtraInformation(uniqueID + "1");
						entry.setExtraInformation(extraEntry);

						if (entry.getAmount() > 0) {
							entries.add(entry);
							if (noPayment) {
								AccountingEntry cloneEntry = getClonedEntry(entry);
								if (cloneEntry != null) {
									entries.add(cloneEntry);
								}
							}
						}
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

					if (choice.getDayCare() != CourseConstants.DAY_CARE_NONE
							&& price != null) {
						float carePrice = 0;
						if (choice.getDayCare() == CourseConstants.DAY_CARE_PRE) {
							carePrice = price.getPreCarePrice();
						} else if (choice.getDayCare() == CourseConstants.DAY_CARE_POST) {
							carePrice = price.getPostCarePrice();
						} else if (choice.getDayCare() == CourseConstants.DAY_CARE_PRE_AND_POST) {
							carePrice = price.getPreCarePrice()
									+ price.getPostCarePrice();
						}
						carePrice = carePrice
								* (1 - ((PriceHolder) discounts.get(student))
										.getDiscount());

						try {
							Object o = implementor.newInstance();
							AccountingEntry entry = (AccountingEntry) o;
							entry
									.setProductCode(CourseConstants.PRODUCT_CODE_CARE);
							entry.setProviderCode(providerCode);
							entry.setProjectCode(typeCode);
							entry.setPayerPersonalId(payerPId);
							entry.setPersonalId(studentPId);
							entry
									.setPaymentMethod(paymentType
											.equals(CourseConstants.PAYMENT_TYPE_CARD) ? application
											.getCardType().toUpperCase()
											: "GIRO");
							if (paymentType
									.equals(CourseConstants.PAYMENT_TYPE_CARD)
									&& application.getCardNumber() != null) {
								entry.setCardExpirationMonth(application
										.getCardValidMonth());
								entry.setCardExpirationYear(application
										.getCardValidYear());
								entry
										.setCardNumber(application
												.getCardNumber());
								entry.setCardType(application.getCardType());
							}
							entry.setAmount((int) carePrice);
							entry.setUnits(1);
							entry.setStartDate(startDate);
							entry.setEndDate(endDate);

							AccountingEntry extraEntry = (AccountingEntry) implementor
									.newInstance();
							extraEntry.setStartDate(application.getCreated());
							extraEntry.setProductCode(courseName);
							extraEntry.setProjectCode(batchNumber);
							extraEntry.setProviderCode(areaCode);
							extraEntry.setExtraInformation(uniqueID + "2");
							entry.setExtraInformation(extraEntry);

							if (entry.getAmount() > 0) {
								entries.add(entry);
								if (noPayment) {
									AccountingEntry cloneEntry = getClonedEntry(entry);
									if (cloneEntry != null) {
										entries.add(cloneEntry);
									}
								}
							}
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return (AccountingEntry[]) entries.toArray(new AccountingEntry[0]);
	}

	private AccountingEntry getClonedEntry(AccountingEntry entry) {
		try {
			Class implementor = ImplementorRepository.getInstance().getAnyClassImpl(AccountingEntry.class, this.getClass());
			AccountingEntry clone = (AccountingEntry) implementor.newInstance();
			clone.setAmount(-entry.getAmount());
			clone.setCardExpirationMonth(entry.getCardExpirationMonth());
			clone.setCardExpirationYear(entry.getCardExpirationYear());
			clone.setCardNumber(entry.getCardNumber());
			clone.setCardType(entry.getCardType());
			clone.setEndDate(entry.getEndDate());
			clone.setName(entry.getName());
			clone.setPayerPersonalId(entry.getPayerPersonalId());
			clone.setPaymentMethod(entry.getPaymentMethod());
			clone.setProductCode(entry.getProductCode());
			clone.setProjectCode(entry.getProjectCode());
			clone.setProviderCode(entry.getProviderCode());
			clone.setStartDate(entry.getStartDate());
			clone.setUnitPrice(entry.getUnitPrice());
			clone.setUnits(entry.getUnits());

			AccountingEntry extraEntry = (AccountingEntry) entry.getExtraInformation();
			AccountingEntry extraClone = (AccountingEntry) implementor.newInstance();
			extraClone.setStartDate(extraEntry.getStartDate());
			extraClone.setProductCode(extraEntry.getProductCode());
			extraClone.setProjectCode(extraEntry.getProjectCode());
			extraClone.setProviderCode(extraEntry.getProviderCode());
			extraClone.setExtraInformation(extraEntry.getExtraInformation());
			clone.setExtraInformation(extraClone);
			return clone;
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getLocalizedCaseDescription(Case theCase, Locale locale) {
		/*
		 * CourseApplication choice = getCourseApplicationInstance(theCase);
		 *
		 * Object[] arguments = { "" };
		 *
		 * String desc = super.getLocalizedCaseDescription(theCase, locale);
		 * return MessageFormat.format(desc, arguments);
		 */
		return super.getLocalizedCaseDescription(theCase, locale);
	}

	protected CourseApplication getCourseApplicationInstance(Case theCase)
			throws RuntimeException {
		try {
			return this.getCourseApplication(theCase.getPrimaryKey());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public String finishPayment(String properties)
			throws CreditCardAuthorizationException {
		try {
			CreditCardClient client = getCreditCardBusiness()
					.getCreditCardClient(getCreditCardMerchant());
			return client.finishTransaction(properties);
		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException(
					"Online payment failed. Unknown error.");
		}
	}

	@Override
	public String authorizePayment(String nameOnCard, String cardNumber,
			String monthExpires, String yearExpires, String ccVerifyNumber,
			double amount, String currency, String referenceNumber)
			throws CreditCardAuthorizationException {
		try {
			CreditCardClient client = getCreditCardBusiness()
					.getCreditCardClient(getCreditCardMerchant());
			return client.creditcardAuthorization(nameOnCard, cardNumber,
					monthExpires, yearExpires, ccVerifyNumber, amount,
					currency, referenceNumber);
		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException(
					"Online payment failed. Unknown error.");
		}
	}

	@Override
	public String refundPayment(CourseApplication application,
			String cardNumber, String monthExpires, String yearExpires,
			String ccVerifyNumber, double amount)
			throws CreditCardAuthorizationException {
		try {
			CreditCardAuthorizationEntry ccAuthEntry = getCreditCardBusiness()
					.getAuthorizationEntry(getCreditCardInformation(),
							application.getReferenceNumber(),
							new IWTimestamp(application.getCreated()));

			CreditCardClient client = getCreditCardBusiness()
					.getCreditCardClient(getCreditCardMerchant());
			return client.doRefund(cardNumber, monthExpires, yearExpires,
					ccVerifyNumber, amount, ccAuthEntry.getCurrency(),
					ccAuthEntry.getPrimaryKey(), ccAuthEntry.getExtraField());
		} catch (CreditCardAuthorizationException ccae) {
			throw ccae;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new CreditCardAuthorizationException(
					"Online payment failed. Unknown error.");
		}
	}

	private CreditCardInformation getCreditCardInformation()
			throws FinderException {
		String merchantPK = getIWApplicationContext().getApplicationSettings()
				.getProperty(CourseConstants.PROPERTY_MERCHANT_PK);
		String merchantType = getIWApplicationContext()
				.getApplicationSettings().getProperty(
						CourseConstants.PROPERTY_MERCHANT_TYPE);
		if (merchantPK != null && merchantType != null) {
			try {
				return getCreditCardBusiness().getCreditCardInformation(
						merchantPK, merchantType);
			} catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return null;
	}

	private CreditCardMerchant getCreditCardMerchant() throws FinderException {
		String merchantPK = getIWApplicationContext().getApplicationSettings()
				.getProperty(CourseConstants.PROPERTY_MERCHANT_PK);
		String merchantType = getIWApplicationContext()
				.getApplicationSettings().getProperty(
						CourseConstants.PROPERTY_MERCHANT_TYPE);
		if (merchantPK != null && merchantType != null) {
			try {
				return getCreditCardBusiness().getCreditCardMerchant(
						merchantPK, merchantType);
			} catch (RemoteException re) {
				throw new IBORuntimeException(re);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public Collection<Image> getCreditCardImages() {
		try {
			return getCreditCardBusiness().getCreditCardTypeImages(getCreditCardBusiness().getCreditCardClient(getCreditCardMerchant()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Image>();
	}

	private CreditCardBusiness getCreditCardBusiness() {
		try {
			return IBOLookup.getServiceInstance(
					getIWApplicationContext(), CreditCardBusiness.class);
		} catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	@Override
	public boolean deleteCourseType(Object pk) {
		CourseType type = null;
		try {
			type = getCourseTypeHome().findByPrimaryKey(
					new Integer(pk.toString()));
			type.remove();

			return true;
		} catch (javax.ejb.FinderException fe) {
			fe.printStackTrace();
		} catch (RemoveException re) {
			re.printStackTrace();
		}

		return false;
	}

	@Override
	public void storeCourseType(Object pk, String name, String description,
			String localizationKey, Object schoolTypePK, String accountingKey, boolean disabled)
			throws FinderException, CreateException {
		CourseType type = null;
		if (pk == null) {
			type = getCourseTypeHome().create();
		} else {
			type = getCourseTypeHome().findByPrimaryKey(
					new Integer(pk.toString()));
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

		type.setDisabled(disabled);
		type.setAccountingKey(accountingKey);

		if (schoolTypePK != null) {
			CourseCategory courseCategory = getCourseCategory(schoolTypePK);
			type.setCourseCategory(courseCategory);
		}

		type.store();
	}

	@Override
	public boolean deleteCourse(Object pk) throws RemoteException {
		Course course = null;
		try {
			course = getCourseHome().findByPrimaryKey(
					new Integer(pk.toString()));
			course.remove();
			return true;
		} catch (javax.ejb.FinderException fe) {
			fe.printStackTrace();
		} catch (RemoveException re) {
			re.printStackTrace();
		}

		return false;
	}

	@Override
	public Course createCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price) throws FinderException, CreateException {
		return createCourse(pk, courseNumber, name, user, courseTypePK,
				providerPK, coursePricePK, startDate, endDate, accountingKey,
				birthYearFrom, birthYearTo, maxPer, price, -1, false);
	}

	@Override
	public Course createCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration)
			throws FinderException, CreateException {
		return createCourse(pk, courseNumber, name, user, courseTypePK, providerPK, coursePricePK, startDate, endDate, accountingKey, birthYearFrom, birthYearTo, maxPer, price, cost, openForRegistration, null);
	}

	public Course createCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration, IWTimestamp registrationEnd)
			throws FinderException, CreateException {
		return createCourse(pk, courseNumber, name, user, courseTypePK, providerPK, coursePricePK, startDate, endDate, accountingKey, birthYearFrom, birthYearTo, maxPer, price, cost, openForRegistration, registrationEnd, true, true);
	}

	@Override
	public Course createCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration, IWTimestamp registrationEnd, boolean hasPreCare, boolean hasPostCare)
			throws FinderException, CreateException {
		Course course = null;
		if (pk != null) {
			course = getCourseHome().findByPrimaryKey(
					new Integer(pk.toString()));
		} else {
			course = getCourseHome().create();
		}

		if (courseNumber > 0) {
			course.setCourseNumber(courseNumber);
		} else {
			course.setCourseNumber(getNextCourseNumber());
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
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		course.setUser(user);

		if (coursePricePK != null) {
			course
					.setPrice(getCoursePrice(new Integer(coursePricePK
							.toString())));
		}

		if (startDate != null) {
			course.setStartDate(startDate.getTimestamp());
		}

		if (endDate != null) {
			course.setEndDate(endDate.getTimestamp());
		}

		if (registrationEnd != null) {
			course.setRegistrationEnd(registrationEnd.getTimestamp());
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

		if (maxPer >= 0) {
			course.setMax(maxPer);
		}

		if (price >= 0 && coursePricePK == null) {
			course.setCoursePrice(price);
		}
		if (cost >= 0 && coursePricePK == null) {
			course.setCourseCost(cost);
		}
		course.setOpenForRegistration(openForRegistration);
		course.setHasPostCare(hasPostCare);
		course.setHasPreCare(hasPreCare);

		course.store();

		return course;
	}

	@Override
	public void storeCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price) throws FinderException, CreateException {
		createCourse(pk, courseNumber, name, user, courseTypePK, providerPK,
				coursePricePK, startDate, endDate, accountingKey,
				birthYearFrom, birthYearTo, maxPer, price);
	}

	@Override
	public Course storeCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration)
			throws FinderException, CreateException {
		return createCourse(pk, courseNumber, name, user, courseTypePK,
				providerPK, coursePricePK, startDate, endDate, accountingKey,
				birthYearFrom, birthYearTo, maxPer, price, cost,
				openForRegistration);
	}

	@Override
	public Course storeCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate, IWTimestamp registrationEnd,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration)
			throws FinderException, CreateException {
		return createCourse(pk, courseNumber, name, user, courseTypePK,
				providerPK, coursePricePK, startDate, endDate, accountingKey,
				birthYearFrom, birthYearTo, maxPer, price, cost,
				openForRegistration, registrationEnd);
	}

	@Override
	public Course storeCourse(Object pk, int courseNumber, String name,
			String user, Object courseTypePK, Object providerPK,
			Object coursePricePK, IWTimestamp startDate, IWTimestamp endDate, IWTimestamp registrationEnd,
			String accountingKey, int birthYearFrom, int birthYearTo,
			int maxPer, float price, float cost, boolean openForRegistration, boolean hasPreCare, boolean hasPostCare)
			throws FinderException, CreateException {
		return createCourse(pk, courseNumber, name, user, courseTypePK,
				providerPK, coursePricePK, startDate, endDate, accountingKey,
				birthYearFrom, birthYearTo, maxPer, price, cost,
				openForRegistration, registrationEnd, hasPreCare, hasPostCare);
	}

	@Override
	public boolean deleteCoursePrice(Object pk) throws RemoteException {
		CoursePrice price = null;
		try {
			price = getCoursePriceHome().findByPrimaryKey(
					new Integer(pk.toString()));
			price.setValid(false);
			price.store();

			return true;
		} catch (javax.ejb.FinderException fe) {
			fe.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean deleteCourseDiscount(Object pk) throws RemoteException {
		CourseDiscount discount = null;
		try {
			discount = getCourseDiscountHome().findByPrimaryKey(
					new Integer(pk.toString()));
			discount.setValid(false);
			discount.store();

			return true;
		} catch (javax.ejb.FinderException fe) {
			fe.printStackTrace();
		}

		return false;
	}

	@Override
	public void storeCoursePrice(Object pk, String name, int numberOfDays,
			Timestamp validFrom, Timestamp validTo, int iPrice,
			int preCarePrice, int postCarePrice, Object schoolAreaPK,
			Object courseTypePK) throws CreateException, NumberFormatException,
			FinderException {
		CoursePrice price = null;
		if (pk != null) {
			price = getCoursePriceHome().findByPrimaryKey(
					new Integer(pk.toString()));
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
				SchoolArea area = getSchoolBusiness().getSchoolArea(
						schoolAreaPK);
				price.setSchoolArea(area);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		if (courseTypePK != null) {
			CourseType type = getCourseType(courseTypePK);
			price.setCourseType(type);
		}

		price.store();
	}

	@Override
	public void storeCourseDiscount(Object pk, String name, String type,
			Timestamp validFrom, Timestamp validTo, float discount)
			throws CreateException, NumberFormatException, FinderException {
		CourseDiscount courseDiscount = null;
		if (pk != null) {
			courseDiscount = getCourseDiscountHome().findByPrimaryKey(
					new Integer(pk.toString()));
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

	@Override
	public Collection getCourseTypes(Integer schoolTypePK, boolean valid) {
		try {
			Collection coll = getCourseTypeHome().findAllBySchoolType(
					schoolTypePK, valid);
			return coll;
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map getCourseTypesDWR(int schoolTypePK, String country) {
		Collection coll = getCourseTypes(new Integer(schoolTypePK), true);
		Map map = new LinkedHashMap();

		Locale locale = new Locale(country, country.toUpperCase());
		map.put(new Integer(-1), getLocalizedString("select_course_type",
				"Select course type", locale));

		if (coll != null) {
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				CourseType type = (CourseType) iter.next();
				map.put(type.getPrimaryKey(), type.getName());
			}
		}
		return map;
	}

	@Override
	public List<AdvancedProperty> getProvidersDWR(int userPK, int typePK, String country) {
		Collection<School> coll = getProviders();

		Locale locale = new Locale(country, country.toUpperCase());

		CourseType type = null;
		if (typePK > 0) {
			type = getCourseType(typePK);
		}

		User user = null;
		try {
			user = getUser(new Integer(userPK));
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		List<AdvancedProperty> results = new ArrayList<AdvancedProperty>();
		if (coll != null) {
			Iterator<School> iter = coll.iterator();
			while (iter.hasNext()) {
				School provider = iter.next();
				if (hasAvailableCourses(user, provider, type)) {
					results.add(new AdvancedProperty(provider.getPrimaryKey().toString(), provider.getSchoolName()));
				}
			}
			results.add(new AdvancedProperty(String.valueOf(-1), getLocalizedString("all_providers", "All providers", locale)));
			results.add(0, new AdvancedProperty(String.valueOf(-2), getLocalizedString("select_provider", "Select provider", locale)));
		}
		return results;
	}

	@Override
	public Map getCourseMapDWR(int providerPK, int schoolTypePK,
			int courseTypePK, String country) {
		return getCoursesMapDWR(providerPK, schoolTypePK, courseTypePK, -1,
				country);
	}

	@Override
	public Map getCoursesMapDWR(int providerPK, int schoolTypePK,
			int courseTypePK, int year, String country) {
		boolean showIDInName = getIWApplicationContext()
				.getApplicationSettings().getBoolean(
						CourseConstants.PROPERTY_SHOW_ID_IN_NAME, false);

		Date fromDate = null;
		Date toDate = null;

		if (year > 0) {
			fromDate = new IWTimestamp(1, 1, year).getDate();
			toDate = new IWTimestamp(31, 12, year).getDate();
		}

		Collection coll = getCourses(-1, new Integer(providerPK),
				schoolTypePK > 0 ? new Integer(schoolTypePK) : null,
				courseTypePK > 0 ? new Integer(courseTypePK) : null, fromDate,
				toDate);
		Map map = new LinkedHashMap();
		if (coll != null) {
			Locale locale = new Locale(country, country.toUpperCase());
			map.put(new Integer(-1), getLocalizedString("select_course",
					"Select course", locale));

			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				Course course = (Course) iter.next();
				String name = "";
				if (showIDInName) {
					name += course.getCourseNumber() + " - ";

					CourseType type = course.getCourseType();
					if (type.getAbbreviation() != null
							&& type.showAbbreviation()) {
						name += type.getAbbreviation() + " ";
					}
				}
				name += course.getName();

				map.put(course.getPrimaryKey(), name);
			}
		}
		return map;
	}

	@Override
	public Map getCoursePricesDWR(String date, int providerPK,
			int courseTypePK, String country) {
		Map map = new LinkedHashMap();
		CourseType cType = getCourseType(new Integer(courseTypePK));
		try {
			School provider = getSchoolBusiness().getSchool(
					new Integer(providerPK));

			Collection prices = getCoursePriceHome().findAll(
					provider.getSchoolArea(), cType);
			IWTimestamp stamp = null;
			if (prices != null) {
				Locale locale = new Locale(country, country.toUpperCase());
				map.put("", getLocalizedString("select_course_price",
						"Select course price", locale));

				Iterator iter = prices.iterator();
				stamp = new IWTimestamp(IWDatePickerHandler.getParsedDate(date,
						locale));
				while (iter.hasNext()) {
					CoursePrice price = (CoursePrice) iter.next();
					IWTimestamp from = new IWTimestamp(price.getValidFrom());
					IWTimestamp to = new IWTimestamp(price.getValidTo());
					if (stamp.isLaterThanOrEquals(from)
							&& stamp.isEarlierThan(to)) {
						map.put(price.getPrimaryKey().toString(), price
								.getName());
					}
				}
			}
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public CoursePriceDWR getPriceDWR(int pricePK) {
		try {
			CoursePrice price = getCoursePriceHome().findByPrimaryKey(
					new Integer(pricePK));

			CoursePriceDWR dwr = new CoursePriceDWR();
			dwr.setName(price.getName());
			dwr.setPk(price.getPrimaryKey().toString());
			dwr.setPrice(Integer.toString(price.getPrice()));
			dwr.setPreCarePrice(price.getPreCarePrice() > 0 ? Integer
					.toString(price.getPreCarePrice()) : "0");
			dwr.setPostCarePrice(price.getPostCarePrice() > 0 ? Integer
					.toString(price.getPostCarePrice()) : "0");

			return dwr;
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public UserDWR getUserDWR(String personalID, int childPK, int minimumAge,
			String country) {
		return getUserDWRByRelation(personalID, childPK, minimumAge, country,
				null);
	}

	@Override
	public UserDWR getUserDWRByRelation(String personalID, int childPK,
			int minimumAge, String country, String selectedRelation) {
		Locale locale = new Locale(country, country.toUpperCase());
		if (!SocialSecurityNumber.isValidSocialSecurityNumber(personalID,
				locale)) {
			return new UserDWR();
		}

		try {
			User user = getUserBusiness().getUser(personalID);
			Date dateOfBirth = user.getDateOfBirth();
			if (dateOfBirth == null) {
				dateOfBirth = getUserBusiness()
						.getUserDateOfBirthFromPersonalId(personalID);
			}
			if (dateOfBirth == null) {
				return new UserDWR();
			}
			Age age = new Age(dateOfBirth);
			if (age.getYears() < minimumAge) {
				return new UserDWR();
			}

			User childUser = null;
			Child child = null;
			if (childPK != -1) {
				childUser = getUserBusiness().getUser(childPK);
				child = getFamilyLogic().getChild(childUser);
			}

			Custodian custodian = getFamilyLogic().getCustodian(user);
			Name name = new Name(user.getFirstName(), user.getMiddleName(),
					user.getLastName());
			Address address = getUserBusiness().getUsersMainAddress(user);
			PostalCode code = address != null ? address.getPostalCode() : null;

			Phone homePhone = null;
			try {
				homePhone = getUserBusiness().getUsersHomePhone(user);
			} catch (NoPhoneFoundException e) {
				// No phone found...
			}

			Phone workPhone = null;
			try {
				workPhone = getUserBusiness().getUsersWorkPhone(user);
			} catch (NoPhoneFoundException e1) {
				// No phone found...
			}

			Phone mobilePhone = null;
			try {
				mobilePhone = getUserBusiness().getUsersMobilePhone(user);
			} catch (NoPhoneFoundException e) {
				// No phone found...
			}

			Email email = null;
			try {
				email = getUserBusiness().getUsersMainEmail(user);
			} catch (NoEmailFoundException e) {
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
			if (child != null && child.getRelation(custodian) != null) {
				dwr.setUserRelation(child.getRelation(custodian));
			} else if (selectedRelation != null) {
				dwr.setUserRelation(selectedRelation);
			}
			dwr.setUserMaritalStatus(custodian.getMaritalStatus());

			return dwr;
		} catch (FinderException fe) {
			fe.printStackTrace();
			UserDWR user = new UserDWR();
			user.setUserName("");
			return user;
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public Collection<CourseDWR> getCoursesDWR(int providerPK, int schoolTypePK, int courseTypePK, int applicantPK, String country, boolean isAdmin) {
		try {
			User applicant = applicantPK != -1 ? getUserBusiness().getUser(applicantPK) : null;
			IWTimestamp birth = applicant != null ? new IWTimestamp(applicant.getDateOfBirth()) : null;

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
			} else {
				return new ArrayList<CourseDWR>();
			}

			Locale locale = new Locale(country, country.toUpperCase());
			IWTimestamp defaultStamp = new IWTimestamp();
			int backMonths = getIWMainApplication().getSettings().getProperty(
					CourseConstants.PROPERTY_BACK_MONTHS) != null ? Integer
					.parseInt(getIWMainApplication().getSettings().getProperty(
							CourseConstants.PROPERTY_BACK_MONTHS)) : -1;
			if (backMonths != -1) {
				defaultStamp.addMonths(backMonths);
			} else {
				defaultStamp = null;
			}
			boolean useCourseOpen = getIWMainApplication().getSettings().getBoolean(CourseConstants.PROPERTY_MANUALLY_OPEN_COURSES, false);

			IWTimestamp stamp = new IWTimestamp();
			Map<Object, CourseDWR> map = new LinkedHashMap<Object, CourseDWR>();
			Collection<Course> courses = getCourses(
					birth != null ? birth.getYear() : 0, iP, iST, iCT, null,
					null);
			if (courses != null) {
				Iterator<Course> iter = courses.iterator();
				while (iter.hasNext()) {
					Course course = iter.next();
					IWTimestamp start = getRegistrationTimeoutForCourse(course);

					if (useCourseOpen) {
						if ((course.isOpenForRegistration() || isAdmin)
								&& ((applicant != null && !isRegistered(
										applicant, course)) || applicant == null)) {
							CourseDWR cDWR = getCourseDWR(locale, course);
							map.put(course.getPrimaryKey(), cDWR);
						}
					} else if (((!isAdmin ? start.isLaterThan(stamp)
							: (defaultStamp != null ? start
									.isLaterThan(defaultStamp) : true)) && ((applicant != null && !isRegistered(
							applicant, course)) || applicant == null))) {
						CourseDWR cDWR = getCourseDWR(locale, course);
						map.put(course.getPrimaryKey(), cDWR);
					}
				}
			}

			return map.values();
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private IWTimestamp getRegistrationTimeoutForCourse(Course course) {
		if (course.getRegistrationEnd() != null) {
			return new IWTimestamp(course.getRegistrationEnd());
		}

		IWTimestamp start = new IWTimestamp(course.getStartDate());
		if (getTimeoutDay() > 0) {
			int day = getTimeoutDay();
			while (start.getDayOfWeek() != day) {
				start.addDays(-1);
			}
		}
		if (getTimeoutHour() > 0) {
			start.setHour(getTimeoutHour());
			start.setMinute(0);
		}

		return start;
	}

	private int getNumberOfInvitations(Course course) {
		Collection<?> applications = getCourseChoices(course, true);
		return ListUtil.isEmpty(applications) ? 0 : applications.size();
	}

	@Override
	public boolean isFull(Course course) {
		int maxPlaces = course.getMax();
		Collection<?> applications = getCourseChoices(course, false);
		int applicationsForTheCourse = ListUtil.isEmpty(applications) ? 0 : applications.size();
		int freePlaces = maxPlaces - applicationsForTheCourse;
		int numberOfReservations = getNumberOfReservations(course);
		int numberOfInvitations = getNumberOfInvitations(course);
		int totalFreeCapacity = freePlaces - numberOfReservations - numberOfInvitations;
		boolean full = totalFreeCapacity <= 0;
		getLogger().info("Course (" + course.getName() + ", ID: " + course.getPrimaryKey() + ") can have " + maxPlaces +
				" students. Currently there are " + applicationsForTheCourse + " applications, " + numberOfReservations +
				" reservations to this course (the ones residents are currently filling in) and " + numberOfInvitations +
				" invitation(s) sent to resident(s). Free places: " + totalFreeCapacity);
		return full;
	}

	@Override
	public boolean isRegistered(User user, Course course) {
		try {
			return getCourseChoiceHome().getCountByUserAndCourse(user, course) > 0;
		} catch (IDOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean hasNotStarted(Course course, boolean isAdmin) {
		IWTimestamp stamp = new IWTimestamp();
		IWTimestamp start = new IWTimestamp(course.getStartDate());

		return !isAdmin ? start.isLaterThan(stamp) : true;
	}

	@Override
	public boolean isOfAge(User user, Course course) {
		IWTimestamp dateOfBirth = new IWTimestamp(user.getDateOfBirth());
		return dateOfBirth.getYear() <= course.getBirthyearTo()
				&& dateOfBirth.getYear() >= course.getBirthyearFrom();
	}

	@Override
	public CourseDWR getCourseDWR(Locale locale, Course course) {
		return getCourseDWR(locale, course, true);
	}

	@Override
	public CourseDWR getCourseDWR(Locale locale, Course course, boolean showYear) {
		CourseDWR cDWR = new CourseDWR();
		cDWR.setName(course.getName());
		cDWR.setPk(course.getPrimaryKey().toString());
		cDWR.setFrom(Integer.toString(course.getBirthyearFrom()));
		cDWR.setTo(Integer.toString(course.getBirthyearTo()));
		cDWR.setDescription(course.getDescription());
		cDWR.setProvider(course.getProvider().getName());
		cDWR.setFull(isFull(course));

		IWTimestamp from = new IWTimestamp(course.getStartDate());

		String toS = "";
		String dayS = "";
		CoursePrice price = course.getPrice();
		if (from != null && price != null) {
			IWTimestamp toDate = new IWTimestamp(getEndDate(price, from.getDate()));
			dayS = Integer.toString(price.getNumberOfDays());
			if (showYear) {
				toS = toDate.getDateString("dd.MM.yyyy", locale);
			} else {
				toS = toDate.getDateString("dd.MM", locale);
			}
			cDWR.setPrice(price.getPrice() + " ISK");
			if (showYear) {
				cDWR.setTimeframe(from.getDateString("dd.MM.yyyy", locale) + " - " + toS);
			} else {
				cDWR.setTimeframe(from.getDateString("dd.MM", locale) + " - " + toS);
			}
		} else if (from != null && course.getEndDate() != null) {
			IWTimestamp toDate = new IWTimestamp(course.getEndDate());
			toS = toDate.getDateString("d. MMMM yyyy", locale);

			NumberFormat format = NumberFormat.getCurrencyInstance(locale);
			format.setMaximumFractionDigits(0);
			cDWR.setPrice(format.format(course.getCoursePrice()));
			cDWR.setTimeframe(from.getDateString("d. MMMM yyyy", locale) + (from.equals(toDate) ? "" : " - " + toS));
		}
		cDWR.setDays(dayS);
		return cDWR;
	}

	@Override
	public Collection getCourses(int birthYear, Object schoolTypePK,
			Object courseTypePK, Date fromDate, Date toDate) {
		return getCourses(birthYear, null, schoolTypePK, courseTypePK,
				fromDate, toDate);
	}

	@Override
	public Collection getCourses(int birthYear, Object schoolTypePK,
			Object courseTypePK) {
		return getCourses(birthYear, null, schoolTypePK, courseTypePK);
	}

	@Override
	public Collection getCourses(int birthYear, Object providerPK,
			Object schoolTypePK, Object courseTypePK, Date fromDate, Date toDate) {
		Collection courses = new ArrayList();
		try {
			courses = getCourseHome().findAll(providerPK, schoolTypePK,
					courseTypePK, birthYear, fromDate, toDate);
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return courses;
	}

	@Override
	public Collection getCourses(Collection providers, Object schoolTypePK,
			Object courseTypePK, Date fromDate, Date toDate) {
		Collection courses = new ArrayList();
		try {
			courses = getCourseHome().findAll(providers, schoolTypePK,
					courseTypePK, -1, fromDate, toDate);
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return courses;
	}

	@Override
	public Collection getCourses(int birthYear, Object providerPK,
			Object schoolTypePK, Object courseTypePK) {
		Collection courses = new ArrayList();
		try {
			courses = getCourseHome().findAll(providerPK, schoolTypePK,
					courseTypePK, birthYear, null, null);
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return courses;
	}

	@Override
	public Collection getCourses(Collection providers, Object schoolTypePK,
			Object courseTypePK) {
		Collection courses = new ArrayList();
		try {
			courses = getCourseHome().findAll(providers, schoolTypePK,
					courseTypePK, -1, null, null);
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return courses;
	}

	@Override
	public CourseChoice getCourseChoice(Object courseChoicePK) {
		try {
			return getCourseChoiceHome().findByPrimaryKey(
					new Integer(courseChoicePK.toString()));
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Collection getCourseChoices(Object coursePK, boolean waitingList) {
		Course course = getCourse(coursePK);
		if (course != null) {
			return getCourseChoices(course, waitingList);
		}

		return new ArrayList();
	}

	@Override
	public Collection getCourseChoices(Course course, boolean waitingList) {
		try {
			return getCourseChoiceHome().findAllByCourse(course, waitingList);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Collection getCourseChoices(CourseApplication application) {
		return getCourseChoices(application, null);
	}

	private Collection getCourseChoices(CourseApplication application,
			Boolean waitingList) {
		try {
			return getCourseChoiceHome().findAllByApplication(application,
					waitingList);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Collection getCourseChoices(User user) {
		try {
			return getCourseChoiceHome().findAllByUser(user);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public CourseDiscount getCourseDiscount(Object pk) {
		if (pk != null) {
			try {
				return getCourseDiscountHome().findByPrimaryKey(
						new Integer(pk.toString()));
			} catch (javax.ejb.FinderException fe) {
			}
		}
		return null;
	}

	@Override
	public CoursePrice getCoursePrice(Object pk) {
		if (pk != null) {
			try {
				return getCoursePriceHome().findByPrimaryKey(
						new Integer(pk.toString()));
			} catch (javax.ejb.FinderException fe) {
			}
		}
		return null;
	}

	@Override
	public Collection getCoursePrices(Date fromDate, Date toDate) {
		try {
			return getCoursePriceHome().findAll(fromDate, toDate);
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	@Override
	public Collection getCourseDiscounts(Date fromDate, Date toDate) {
		try {
			return getCourseDiscountHome().findAll(fromDate, toDate);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	@Override
	public Course getCourse(Object pk) {
		if (pk != null) {
			try {
				return getCourseHome().findByPrimaryKey(
						new Integer(pk.toString()));
			} catch (javax.ejb.FinderException fe) {
			}
		}
		return null;
	}

	@Override
	public int getNextCourseNumber() {
		try {
			return getCourseHome().getHighestCourseNumber() + 1;
		} catch (IDOException ie) {
			log(ie);
			return 1;
		}
	}

	@Override
	public CourseType getCourseType(Object pk) {
		if (pk != null) {
			try {
				return getCourseTypeHome().findByPrimaryKey(
						new Integer(pk.toString()));
			} catch (FinderException fe) {
				log(fe);
			}
		}

		return null;
	}

	@Override
	public CourseCategory getCourseCategory(Object pk) {
		if (pk != null) {
			try {
				return getCourseCategoryHome().findByPrimaryKey(
						new Integer(pk.toString()));
			} catch (FinderException fe) {
				log(fe);
			}
		}

		return null;
	}

	@Override
	public CourseApplication getCourseApplication(Object courseApplicationPK) {
		try {
			return getCourseApplicationHome().findByPrimaryKey(
					courseApplicationPK);
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Collection getAllCourses() {
		try {
			return getCourseHome().findAll();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	@Override
	public Collection getAllCourses(School provider) {
		try {
			return getCourseHome().findAllByProvider(provider);
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	@Override
	public Collection getAllCourseTypes(boolean valid) {
		try {
			return getCourseTypeHome().findAll(valid);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	@Override
	public Collection getAllCourseTypes(Integer schoolTypePK, boolean valid) {
		try {
			return getCourseTypeHome().findAllBySchoolType(schoolTypePK, valid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	@Override
	public Collection getAllSchoolTypes() {
		try {
			Collection schoolTypes = getSchoolBusiness()
					.findAllSchoolTypesInCategory(
							getSchoolBusiness()
									.getAfterSchoolCareSchoolCategory());

			Object typePK = getIWApplicationContext().getApplicationSettings()
					.getProperty(CourseConstants.PROPERTY_HIDDEN_SCHOOL_TYPE);
			if (typePK != null) {
				SchoolType type = getSchoolBusiness().getSchoolType(
						new Integer(typePK.toString()));
				schoolTypes.remove(type);
			}

			return schoolTypes;
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public Collection getAllSchoolTypes(String category) {
		try {
			Collection schoolTypes = getSchoolBusiness()
					.findAllSchoolTypesInCategory(category);

			Object typePK = getIWApplicationContext().getApplicationSettings()
					.getProperty(CourseConstants.PROPERTY_HIDDEN_SCHOOL_TYPE);
			if (typePK != null) {
				SchoolType type = getSchoolBusiness().getSchoolType(
						new Integer(typePK.toString()));
				schoolTypes.remove(type);
			}

			return schoolTypes;
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}


	@Override
	public Collection getSchoolAreas() {
		try {
			return getSchoolBusiness().getSchoolAreaHome().findAllSchoolAreas(
					getSchoolBusiness().getCategoryAfterSchoolCare());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	@Override
	public Collection getSchoolTypes(School provider) {
		try {
			Collection types = new ArrayList();

			try {
				Collection schoolTypes = provider.getSchoolTypes();

				String typePK = getIWApplicationContext()
						.getApplicationSettings().getProperty(
								CourseConstants.PROPERTY_HIDDEN_SCHOOL_TYPE);
				if (typePK != null) {
					SchoolType type = getSchoolBusiness().getSchoolType(
							new Integer(typePK));
					schoolTypes.remove(type);
				}

				Iterator iterator = schoolTypes.iterator();
				while (iterator.hasNext()) {
					SchoolType type = (SchoolType) iterator.next();
					/*if (type.getCategory().equals(
							getSchoolBusiness().getCategoryAfterSchoolCare())) {*/
						types.add(type);
					//}
				}
			} catch (IDORelationshipException e) {
				e.printStackTrace();
			}

			return types;
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public Collection getAllCoursePrices() {
		try {
			return getCoursePriceHome().findAll();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}

	@Override
	public boolean isRegisteredAtProviders(User user, Collection providers) {
		try {
			return getCourseChoiceHome().getCountByUserAndProviders(user,
					providers) > 0;
		} catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
	}

	@Override
	public int getNumberOfFreePlaces(Course course) {
		try {
			return course.getMax()
					- getCourseChoiceHome().getCountByCourse(course);
		} catch (IDOException e) {
			e.printStackTrace();
		}

		return course.getMax();
	}

	@Override
	public Collection getProviders() {
		try {
			return getSchoolBusiness()
					.findAllSchoolsByType(getAllSchoolTypes());
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public Collection getProviders(String category) {
		try {
			return getSchoolBusiness()
					.findAllSchoolsByType(getAllSchoolTypes(category));
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public Collection getProviders(SchoolArea area, SchoolType type) {
		try {
			return getSchoolBusiness().findAllSchoolsByAreaAndType(
					((Integer) area.getPrimaryKey()).intValue(),
					((Integer) type.getPrimaryKey()).intValue());
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public Collection getProvidersForUser(User user) {
		try {
			SchoolUser schoolUser = getSchoolUserBusiness().getSchoolUserHome()
					.findForUser(user);
			return schoolUser.getSchools();
		} catch (IDORelationshipException ire) {
			ire.printStackTrace();
			return new ArrayList();
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Map getApplicationMap(User user, Collection schools) {
		Map map = new LinkedHashMap();
		try {
			Collection choices = getCourseChoiceHome()
					.findAllByUserAndProviders(user, schools);
			Iterator iterator = choices.iterator();
			while (iterator.hasNext()) {
				CourseChoice choice = (CourseChoice) iterator.next();
				CourseApplication application = choice.getApplication();

				Collection applicationChoices;
				if (map.containsKey(application)) {
					applicationChoices = (Collection) map.get(application);
				} else {
					applicationChoices = new ArrayList();
				}

				applicationChoices.add(choice);
				map.put(application, applicationChoices);
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public Map getApplicationMap(CourseApplication application) {
		return getApplicationMap(application, null);
	}

	@Override
	public Map<User, Collection<ApplicationHolder>> getApplicationMap(CourseApplication application,
			Boolean waitingList) {
		Map<User, Collection<ApplicationHolder>> map = new HashMap<User, Collection<ApplicationHolder>>();

		try {
			Collection<CourseChoice> choices = getCourseChoiceHome().findAllByApplication(application, waitingList);
			Iterator<CourseChoice> iterator = choices.iterator();
			while (iterator.hasNext()) {
				CourseChoice choice = iterator.next();
				User user = choice.getUser();
				Course course = choice.getCourse();

				ApplicationHolder holder = new ApplicationHolder();
				holder.setCourse(course);
				holder.setUser(user);
				holder.setDaycare(choice.getDayCare());
				holder.setPickedUp(new Boolean(choice.isPickedUp()));
				holder.setChoice(choice);
				holder.setOnWaitingList(choice.isOnWaitingList());

				Collection<ApplicationHolder> holders = null;
				if (map.containsKey(user)) {
					holders = map.get(user);
				} else {
					holders = new ArrayList<ApplicationHolder>();
				}

				holders.add(holder);
				map.put(user, holders);
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return map;
	}

	@Override
	public float calculateRefund(CourseChoice choice) {
		User user = choice.getUser();
		Course course = choice.getCourse();

		ApplicationHolder holder = new ApplicationHolder();
		holder.setCourse(course);
		holder.setUser(user);
		holder.setDaycare(choice.getDayCare());
		holder.setPickedUp(new Boolean(choice.isPickedUp()));
		holder.setChoice(choice);

		CourseApplication application = choice.getApplication();
		Map applications = getApplicationMap(application);

		float price = holder.getPrice();
		if (hasSiblingInSet(applications.keySet(), user)) {
			price = price * 0.8f;
		}

		return price;
	}

	@Override
	public SortedSet calculatePrices(Map applications) {
		SortedSet<PriceHolder> userPrices = new TreeSet();

		Iterator iterator = applications.keySet().iterator();
		while (iterator.hasNext()) {
			User user = (User) iterator.next();
			Collection userApplications = (Collection) applications.get(user);
			Iterator iter = userApplications.iterator();
			int totalPrice = 0;
			int totalCost = 0;
			String name = null;
			while (iter.hasNext()) {
				ApplicationHolder holder = (ApplicationHolder) iter.next();
				name = holder.getCourse() == null ? null : holder.getCourse().getProvider().getName();
				if (!holder.isOnWaitingList()) {
					totalPrice += holder.getPrice();
					totalCost += holder.getCourse().getCourseCost() > -1 ? holder
							.getCourse().getCourseCost() : 0;
				}
			}

			PriceHolder priceHolder = new PriceHolder(name);
			priceHolder.setUser(user);
			priceHolder.setPrice(totalPrice);
			priceHolder.setCost(totalCost);
			userPrices.add(priceHolder);
		}

		return userPrices;
	}

	@Override
	public float getCalculatedCourseCertificateFees(Map applications) {
		if (applications == null || applications.isEmpty()) {
			return 0;
		}

		float fees = 0;
		User user = null;
		CourseChoice choise = null;
		Collection userChoises = null;
		Iterator keysIt = applications.keySet().iterator();
		CourseChoiceHome choiseHome = getCourseChoiceHome();
		for (Iterator it = keysIt; it.hasNext();) {
			user = (User) it.next();

			userChoises = null;
			try {
				userChoises = choiseHome.findAllByUser(user);
			} catch (FinderException e) {
			}

			if (userChoises != null && !userChoises.isEmpty()) {
				for (Iterator choises = userChoises.iterator(); choises
						.hasNext();) {
					choise = (CourseChoice) choises.next();

					fees += choise.getCourseCertificateFee();
				}
			}
		}

		return fees;
	}

	@Override
	public Map getDiscounts(SortedSet<PriceHolder> userPrices, Map<User, Collection<ApplicationHolder>> applications) {
		Map discountPrices = new HashMap();
		Iterator iterator = userPrices.iterator();
		boolean first = true;
		boolean firstOnWaitingList = true;
		while (iterator.hasNext()) {
			PriceHolder priceHolder = (PriceHolder) iterator.next();
			User applicant = priceHolder.getUser();
			float price = priceHolder.getPrice();

			PriceHolder discountHolder = new PriceHolder();
			discountHolder.setUser(applicant);
			discountHolder.setPrice(0);

			if (!first) {
				if (hasSiblingInSet(applications.keySet(), applicant)) {
					boolean getsDiscount = false;
					String name = null;
					Collection<ApplicationHolder> userApplications = applications.get(applicant);
					for (ApplicationHolder applicationHolder : userApplications) {
						if (!applicationHolder.isOnWaitingList() && !firstOnWaitingList) {
							getsDiscount = true;
							name = applicationHolder.getCourse() == null ? null : applicationHolder.getCourse().getProvider().getName();
						}
					}

					if (getsDiscount) {
						discountHolder.setPrice(price * 0.2f);
						discountHolder.setDiscount(0.2f);
						discountHolder.setName(name);
					}
				}
			} else {
				first = false;
				String name = null;
				Collection<ApplicationHolder> userApplications = applications.get(applicant);
				for (ApplicationHolder applicationHolder : userApplications) {
					if (!applicationHolder.isOnWaitingList()) {
						firstOnWaitingList = false;
						name = applicationHolder.getCourse() == null ? null : applicationHolder.getCourse().getProvider().getName();
					}
				}
				if (name != null) {
					discountHolder.setName(name);
				}
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
				} catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
			}
		}

		return false;
	}

	@Override
	public boolean hasAvailableCourses(User user, SchoolType type) {
		try {
			IWTimestamp stamp = new IWTimestamp(user.getDateOfBirth());
			IWTimestamp stampNow = new IWTimestamp();
			return getCourseHome().getCountBySchoolTypeAndBirthYear(
					type != null ? type.getPrimaryKey() : null,
					stamp.getYear(), stampNow.getDate()) > 0;
		} catch (IDOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean hasAvailableCourses(User user, CourseType type) {
		try {
			IWTimestamp stamp = new IWTimestamp(user.getDateOfBirth());
			IWTimestamp stampNow = new IWTimestamp();
			return getCourseHome().getCountByCourseTypeAndBirthYear(
					type != null ? type.getPrimaryKey() : null,
					stamp.getYear(), stampNow.getDate()) > 0;
		} catch (IDOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean hasAvailableCourses(User user, School school) {
		try {
			IWTimestamp stamp = new IWTimestamp(user.getDateOfBirth());
			IWTimestamp stampNow = new IWTimestamp();
			return getCourseHome().getCountBySchoolAndBirthYear(
					school != null ? school.getPrimaryKey() : null,
					stamp.getYear(), stampNow.getDate()) > 0;
		} catch (IDOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean hasAvailableCourses(User user, School school, CourseType type) {
		try {
			IWTimestamp stamp = new IWTimestamp(user.getDateOfBirth());
			IWTimestamp stampNow = new IWTimestamp();
			return getCourseHome().getCountBySchoolAndCourseTypeAndBirthYear(
					school != null ? school.getPrimaryKey() : null,
					type != null ? type.getPrimaryKey() : null,
					stamp.getYear(), stampNow.getDate()) > 0;
		} catch (IDOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private int getInvalidateInterval() {
		int interval = Integer.parseInt(getIWApplicationContext()
				.getApplicationSettings().getProperty(
						CourseConstants.PROPERTY_INVALIDATE_INTERVAL, "4"));
		return interval;
	}

	@Override
	public int getTimeoutDay() {
		int day = Integer.parseInt(getIWApplicationContext()
				.getApplicationSettings().getProperty(
						CourseConstants.PROPERTY_TIMEOUT_DAY_OF_WEEK, "-1"));
		return day;
	}

	@Override
	public int getTimeoutHour() {
		int hour = Integer.parseInt(getIWApplicationContext()
				.getApplicationSettings().getProperty(
						CourseConstants.PROPERTY_TIMEOUT_HOUR, "-1"));
		return hour;
	}

	@Override
	public boolean canInvalidate(CourseChoice choice) {
		IWTimestamp startDate = new IWTimestamp(choice.getCourse()
				.getStartDate());
		IWTimestamp dateNow = new IWTimestamp();
		dateNow.addDays(getInvalidateInterval());

		if (dateNow.isEarlierThan(startDate)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canInvalidate(CourseApplication application) {
		try {
			CourseChoice choice = getCourseChoiceHome()
					.findFirstChoiceByApplication(application);
			return canInvalidate(choice);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void sendRefundMessage(CourseApplication application, CourseChoice choice, Locale locale) {
		String subject = CoreConstants.EMPTY;
		String body = CoreConstants.EMPTY;

		User payer = null;
		if (application.getPayerPersonalID() != null) {
			try {
				payer = getUserBusiness().getUser(application.getPayerPersonalID());
			} catch (FinderException e) {
				e.printStackTrace();
				payer = application.getOwner();
			} catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		} else {
			payer = application.getOwner();
		}

		if (choice != null) {
			User applicant = choice.getUser();
			Course course = choice.getCourse();
			School provider = course.getProvider();
			Object[] arguments = {
					applicant.getName(),
					PersonalIDFormatter.format(applicant.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale()),
					course.getName(),
					provider.getName(),
					payer.getName(),
					PersonalIDFormatter.format(payer.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale())
			};

			subject = MessageFormat.format(getLocalizedString("course_choice.choice_refund_subject", "Choice invalidated", locale), arguments);
			body = MessageFormat.format(getLocalizedString(
									"course_choice.choice_refund_body",
									"A choice for course {2} at {3} for {0}, {1} has been invalidated and needs to be refunded",
									locale), arguments);
		} else {
			Object[] arguments = {
					payer.getName(),
					PersonalIDFormatter.format(payer.getPersonalID(),
							getIWApplicationContext().getApplicationSettings()
									.getDefaultLocale()) };

			subject = getLocalizedString(
					"course_choice.application_refund_subject",
					"Application invalidated", locale);
			body = MessageFormat
					.format(
							getLocalizedString(
									"course_choice.application_refund_body",
									"An application has been invalidated and needs to be refunded to payer {0}, {1}",
									locale), arguments);
		}

		try {
			String refundEmail = getIWApplicationContext()
					.getApplicationSettings().getProperty(
							CourseConstants.PROPERTY_REFUND_EMAIL + (application.getPrefix() != null ? "." + application.getPrefix() : ""),
							"fjarmaladeild@itr.is");
			getMessageBusiness().sendMessage(refundEmail, subject, body);
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public void sendRegistrationMessage(CourseApplication application,
			CourseChoice choice, Locale locale) {
		String subject = "";
		String body = "";

		User payer = null;
		if (application.getPayerPersonalID() != null) {
			try {
				payer = getUserBusiness().getUser(
						application.getPayerPersonalID());
			} catch (FinderException e) {
				e.printStackTrace();
				payer = application.getOwner();
			} catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		} else {
			payer = application.getOwner();
		}

		User applicant = choice.getUser();
		Course course = choice.getCourse();
		School provider = course.getProvider();
		Object[] arguments = {
				applicant.getName(),
				PersonalIDFormatter.format(applicant.getPersonalID(),
						getIWApplicationContext().getApplicationSettings()
								.getDefaultLocale()),
				course.getName(),
				provider.getName(),
				payer.getName(),
				PersonalIDFormatter.format(payer.getPersonalID(),
						getIWApplicationContext().getApplicationSettings()
								.getDefaultLocale()) };

		subject = getLocalizedString(
				"course_choice.choice_registration_subject1",
				"Choice registered", locale);
		body = MessageFormat.format(getLocalizedString(
				"course_choice.choice_registration_body1",
				"A choice for course {2} at {3} for {0}, {1} has been sent in",
				locale), arguments);

		try {
			String refundEmail = getIWApplicationContext()
					.getApplicationSettings().getProperty(
							CourseConstants.PROPERTY_REGISTRATION_EMAIL,
							"fjarmaladeild@itr.is");
			getMessageBusiness().sendMessage(refundEmail, subject, body);
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public void invalidateApplication(CourseApplication application, User performer, Locale locale) {
		Collection choices = getCourseChoices(application);
		Iterator iterator = choices.iterator();
		while (iterator.hasNext()) {
			CourseChoice choice = (CourseChoice) iterator.next();
			invalidateChoice(application, choice, locale, performer);
		}

		changeCaseStatus(application, getCaseStatusCancelled(), performer);
	}

	@Override
	public void invalidateChoice(CourseApplication application, CourseChoice choice, Locale locale, User performer) {
		choice.setValid(false);
		if (performer == null) {
			CourseApplication app = choice.getApplication();
			if (app != null) {
				performer = app.getOwner();
				if (performer == null) {
					performer = app.getCreator();
				}
			}
			getLogger().info("Need to invalidate (refund) course application (course choice ID: " + choice.getPrimaryKey() +
					"). User is not logged in, selecting perfomer as owner of application (" + performer + ")");
		}
		choice.setInvalidatedBy(performer);
		choice.setInvalidatedAt(IWTimestamp.RightNow().getTimestamp());
		choice.store();

		Collection certificates = getUserCertificatesByCourse(choice.getUser(), choice.getCourse());
		if (certificates != null && !certificates.isEmpty()) {
			Iterator iterator = certificates.iterator();
			while (iterator.hasNext()) {
				CourseCertificate certificate = (CourseCertificate) iterator.next();
				try {
					certificate.remove();
				} catch (RemoveException re) {
					log(re);
				}
			}
		}

		String subject = getLocalizedString(
				"course_choice.registration_invalidated",
				"Your registration for course has been invalidated", locale);
		String body = "";
		if (application.getPaymentType() != null) {
			if (application.getPaymentType().equals(CourseConstants.PAYMENT_TYPE_CARD)) {
				body = getLocalizedString(
						"course_choice.card_registration_invalidated_body",
						"Your registration for course {2} at {3} for {0}, {1} has been invalidated and you have been refunded to your credit card",
						locale);
			} else if (application.getPaymentType().equals(CourseConstants.PAYMENT_TYPE_GIRO)) {
				body = getLocalizedString(
						"course_choice.giro_registration_invalidated_body",
						"Your registration for course {2} at {3} for {0}, {1} has been invalidated.  If you have already paid for the course you will receive repayment shortly.",
						locale);
			}
		}

		sendMessageToParents(application, choice, subject, body, locale);
	}

	@Override
	public CourseApplication saveApplication(Map<User, Collection<ApplicationHolder>> applications, String prefix, User performer, Locale locale) {
		try {
			CourseApplication application = getCourseApplicationHome().create();
			if (performer == null) {
				try {
					performer = getIWApplicationContext().getIWMainApplication().getAccessController().getAdministratorUserLegacy();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			application.setPrefix(prefix);
			application.setOwner(performer);
			changeCaseStatus(application, getCaseStatusOpen(), performer);

			String subject = getLocalizedString(
					(prefix.length() > 0 ? prefix + "." : "") + "course_choice.registration_received",
					"Your registration for course has been received", locale);
			String body = getLocalizedString(
					(prefix.length() > 0 ? prefix + "." : "") + "course_choice.card_registration_body",
					"Your registration for course {2} at {3} for {0}, {1} has been received and paid for with your credit card",
					locale);

			Iterator<Collection<ApplicationHolder>> iter = applications == null ? null : applications.values().iterator();
			if (iter != null) {
				for (Iterator<Collection<ApplicationHolder>> it = iter; it.hasNext();) {
					Collection<ApplicationHolder> collection = it.next();
					Iterator<ApplicationHolder> iterator = collection.iterator();
					while (iterator.hasNext()) {
						ApplicationHolder holder = iterator.next();

						CourseChoice choice = getCourseChoiceHome().create();
						choice.setApplication(application);
						choice.setCourse(holder.getCourse());
						choice.setUser(holder.getUser());
						choice.store();

						sendMessageToApplicationOwner(application, choice, subject, body, locale);
					}
				}
			}

			return application;
		} catch (CreateException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void sendMessageToApplicationOwner(CourseApplication application,
			CourseChoice choice, String subject, String body, Locale locale) {
		try {
			if (locale == null) {
				locale = getIWApplicationContext().getApplicationSettings()
						.getDefaultLocale();
			}

			User applicant = choice.getUser();
			Course course = choice.getCourse();
			School provider = course.getProvider();
			IWTimestamp startDate = new IWTimestamp(course.getStartDate());
			IWTimestamp applicationDate = new IWTimestamp(application
					.getCreated());
			Object[] arguments = { applicationDate.getDateString("dd.MM.yyyy"),
					applicant.getName(), startDate.getDateString("dd.MM.yyyy"),
					startDate.getDateString("HH:mm"), provider.getName() };

			User appParent = application.getOwner();
			if (appParent != null) {
				getMessageBusiness().createUserMessage(application, appParent,
						subject, MessageFormat.format(body, arguments), false,
						false);
			}
		} catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	private boolean isWaitingListTurnedOn() {
		return getIWApplicationContext().getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_WAITING_LIST, false);
	}

	@Override
	public CourseApplication saveApplication(Map<User, Collection<ApplicationHolder>> applications, int merchantID,
			float amount, String merchantType, String paymentType,
			String referenceNumber, String payerName, String payerPersonalID,
			String prefix, User owner, User performer, Locale locale, float certificateFee) {
		try {
			boolean useWaitingList = isWaitingListTurnedOn();
			boolean useDirectPayment = getIWApplicationContext().getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_DIRECT_PAYMENT, false);

			CourseApplication application = getCourseApplicationHome().create();
			if (merchantID > 0) {
				application.setCreditCardMerchantID(merchantID);
			}
			if (merchantType != null) {
				application.setCreditCardMerchantType(merchantType);
			}
			application.setPrefix(prefix);
			application.setPaymentType(paymentType);
			if (paymentType != null) {
				application.setPaid(paymentType
						.equals(CourseConstants.PAYMENT_TYPE_CARD)
						&& useDirectPayment);
			}
			if (application.isPaid()) {
				application.setPaymentTimestamp(IWTimestamp
						.getTimestampRightNow());
			}
			application.setReferenceNumber(referenceNumber);
			application.setPayerName(payerName);
			application.setPayerPersonalID(payerPersonalID);
			if (owner == null) {
				try {
					owner = getIWApplicationContext().getIWMainApplication()
							.getAccessController().getAdministratorUserLegacy();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			application.setOwner(owner);
			if (performer != null) {
				application.setCreator(performer);
			}
			application.setAmount(amount);
			changeCaseStatus(application, getCaseStatusOpen(), owner);

			String subject = getLocalizedString(
					(prefix.length() > 0 ? prefix + "." : "") + "course_choice.registration_received",
					"Your registration for course has been received", locale);
			String body = "";
			if (paymentType != null) {
				if (paymentType.equals(CourseConstants.PAYMENT_TYPE_CARD)) {
					body = getLocalizedString(
							"course_choice.card_registration_body",
							"Your registration for course {2} at {3} for {0}, {1} has been received and paid for with your credit card",
							locale);
				} else if (paymentType
						.equals(CourseConstants.PAYMENT_TYPE_GIRO)) {
					body = getLocalizedString(
							(prefix.length() > 0 ? prefix + "." : "") + "course_choice.giro_registration_body",
							"Your registration for course {2} at {3} for {0}, {1} has been received.  You will receive an invoice in a few days for the total amount of the registration.",
							locale);
				} else if (paymentType
						.equals(CourseConstants.PAYMENT_TYPE_BANK_TRANSFER)) {
					body = getLocalizedString(
							"course_choice.bank_registration_body",
							"Your registration for course {2} at {3} for {0}, {1} has been received.  If you have not already paid for the course, please do so within ten days of the course start.",
							locale);
				}
			}

			String waitingListSubject = getLocalizedString(
					(prefix.length() > 0 ? prefix + "." : "") + "course_choice.waitinglist_registration_subject",
					"Registration for courses received - NOTE child is on the waiting list",
					locale);
			String waitingListBody = getLocalizedString(
					(prefix.length() > 0 ? prefix + "." : "") + "course_choice.waitinglist_registration_body",
					"Your registration for course {2} at {3} for {0}, {1} has been received and added to the waiting list",
					locale);

			IWTimestamp stamp = new IWTimestamp();

			Iterator<Collection<ApplicationHolder>> iter = applications == null ? null : applications.values()
					.iterator();
			if (iter != null) {
				for (Iterator<Collection<ApplicationHolder>> it = iter; it.hasNext();) {
					Collection<ApplicationHolder> collection = it.next();
					Iterator<ApplicationHolder> iterator = collection.iterator();
					while (iterator.hasNext()) {
						ApplicationHolder holder = iterator.next();
						boolean waitingList = false;

						CourseChoice choice = getCourseChoiceHome().create();
						choice.setApplication(application);
						choice.setCourse(holder.getCourse());
						choice.setDayCare(holder.getDaycare());

						if (useWaitingList) {
							waitingList = holder.isOnWaitingList();
							choice.setWaitingList(waitingList);
						}

						if (holder.getPickedUp() != null) {
							choice.setPickedUp(holder.getPickedUp().booleanValue());
						}
						choice.setUser(holder.getUser());
						choice.setHasDyslexia(holder.hasDyslexia());
						if (application.isPaid()) {
							choice.setPaymentTimestamp(IWTimestamp.getTimestampRightNow());
						}
						choice.setCourseCertificateFee(certificateFee);
						choice.store();

						sendMessageToParents(
								application,
								choice,
								waitingList ? waitingListSubject : subject,
								waitingList ? waitingListBody : body,
								locale
						);

						if (getRegistrationTimeoutForCourse(holder.getCourse()).isEarlierThan(stamp)) {
							String timeoutSubject = getLocalizedString("course_choice.timeout_registration_subject", "A timeout registration has been received", locale);
							String timeoutBody = getLocalizedString("course_choice.timeout_registration_body", "A registration has been received for {0}, {1} for {2}.", locale);
							sendMessageToProvider(choice, timeoutSubject, timeoutBody, null, locale);
						}

						/* Hack for NeSt */
						if (holder != null && holder.getCourse() != null && holder.getCourse().getCourseType() != null && holder.getCourse().getCourseType().getAbbreviation() != null) {
							sendRegistrationMessage(application, choice, locale);
						}
					}
				}
			}

			return application;
		} catch (CreateException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CourseApplication saveApplication(Map<User, Collection<ApplicationHolder>> applications, int merchantID,
			float amount, String merchantType, String paymentType,
			String referenceNumber, String payerName, String payerPersonalID,
			String prefix, User owner, User performer, Locale locale) {
		return saveApplication(applications, merchantID, amount, merchantType,
				paymentType, referenceNumber, payerName, payerPersonalID,
				prefix, owner, performer, locale, 0);
	}

	private void sendMessageToParents(CourseApplication application,
			CourseChoice choice, String subject, String body, Locale locale) {
		sendMessageToParents(application, choice, subject, body, locale, null);
	}

	private void sendMessageToParents(CourseApplication application,
			CourseChoice choice, String subject, String body, Locale locale, String bcc) {
		try {
			if (locale == null) {
				locale = getIWApplicationContext().getApplicationSettings()
						.getDefaultLocale();
			}

			String acceptURL = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_ACCEPT_URL, CoreConstants.EMPTY);

			User applicant = choice.getUser();
			Course course = choice.getCourse();
			School provider = course.getProvider();
			IWTimestamp startDate = new IWTimestamp(course.getStartDate());
			Object[] arguments = {
					applicant.getName(),											//	0: name
					PersonalIDFormatter.format(applicant.getPersonalID(), locale),	//	1: personal id
					course.getName(),												//	2: course name
					provider.getName(),												//	3: provider name
					startDate.getLocaleDate(locale, IWTimestamp.SHORT),				//	4: start date
					new IWTimestamp(course.getEndDate() != null ? course			//	5: end date
							.getEndDate() : getEndDate(course.getPrice(),
							startDate.getDate())).getLocaleDate(locale,
							IWTimestamp.SHORT),
					acceptURL,														//	6: URL
					choice.getUniqueID(),											//	7: unique id
					provider.getSchoolEmail(),										//	8: provider email
					provider.getSchoolWebPage()										//	9: provider web page
			};

			User appParent = application.getOwner();
			if (appParent != null) {
				if (getFamilyLogic().isChildInCustodyOf(applicant, appParent)) {
					MessageValue msgValue = getMessageBusiness().createUserMessageValue(application, appParent, null, null, subject, MessageFormat.format(body, arguments), MessageFormat.format(body, arguments), null, false, null, false, true);
					msgValue.setBcc(bcc);

					getMessageBusiness().createUserMessage(msgValue);
				}

				try {
					Collection<User> parents = getFamilyLogic().getCustodiansFor(applicant);
					Iterator<User> iter = parents.iterator();
					while (iter.hasNext()) {
						User parent = iter.next();
						if (!getUserBusiness().haveSameAddress(parent, appParent)) {
							MessageValue msgValue = getMessageBusiness().createUserMessageValue(application, parent, null, null, subject, MessageFormat.format(body, arguments), MessageFormat.format(body, arguments), null, false, null, false, true);
							msgValue.setBcc(bcc);

							getMessageBusiness().createUserMessage(msgValue);
						}
					}
				} catch (NoCustodianFound ncf) {
					MessageValue msgValue = getMessageBusiness().createUserMessageValue(application, applicant, null, null, subject, MessageFormat.format(body, arguments), MessageFormat.format(body, arguments), null, false, null, false, true);
					msgValue.setBcc(bcc);

					getMessageBusiness().createUserMessage(msgValue);
				}
			}
		} catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	public void sendMessageToProvider(CourseChoice choice,
			String subject, String message, User sender, Locale locale) {
		try {
			CourseApplication application = choice.getApplication();
			Course course = choice.getCourse();
			School school = choice.getCourse().getProvider();
			User child = choice.getUser();

			Object[] arguments = {
					child.getName(),
					PersonalIDFormatter.format(child.getPersonalID(),
							locale),
					course.getName()
			};

			Collection users = getSchoolBusiness().getAllSchoolUsers(school);
			if (users != null) {
				CommuneMessageBusiness messageBiz = getMessageBusiness();
				Iterator it = users.iterator();
				while (it.hasNext()) {
					SchoolUser providerUser = (SchoolUser) it.next();
					User user = providerUser.getUser();
					messageBiz.createUserMessage(application, user, sender,
							MessageFormat.format(subject, arguments),
							MessageFormat.format(message, arguments), false);
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	@Override
	public int getNumberOfCourses(School provider, SchoolType schoolType,
			CourseType courseType, Date fromDate, Date toDate) {
		try {
			return getCourseHome()
					.getCountByProviderAndSchoolTypeAndCourseType(provider,
							schoolType, courseType, fromDate, toDate);
		} catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int getNumberOfChoices(School provider, SchoolType schoolType,
			Gender gender, Date fromDate, Date toDate) {
		try {
			return getCourseChoiceHome()
					.getCountByProviderAndSchoolTypeAndGender(provider,
							schoolType, gender, fromDate, toDate);
		} catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int getNumberOfChoices(Course course, Gender gender) {
		try {
			return getCourseChoiceHome().getCountByCourseAndGender(course,
					gender);
		} catch (IDOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Collection getCourses(School provider, SchoolType schoolType,
			Date fromDate, Date toDate) {
		try {
			return getCourseHome().findAllByProviderAndSchoolTypeAndCourseType(
					provider, schoolType, null, fromDate, toDate);
		} catch (FinderException e) {
			e.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Date getEndDate(CoursePrice price, Date startDate) {
		Collection<IWTimestamp> holidays = getPublicHolidays(CoreUtil.getCurrentLocale());

		IWTimestamp stamp = new IWTimestamp(startDate);
		int days = price == null ? 0 : price.getNumberOfDays() - 1;
		while (days > 0) {
			if (stamp.getDayOfWeek() != Calendar.SUNDAY && stamp.getDayOfWeek() != Calendar.SATURDAY) {
				//	Course take place only during week days, not during weekends
				days--;
			}

			stamp.addDays(1);

			if (!holidays.isEmpty()) {
				Iterator iterator = holidays.iterator();
				while (iterator.hasNext()) {
					IWTimestamp holiday = (IWTimestamp) iterator.next();
					if (stamp.isEqualTo(holiday)) {
						stamp.addDays(1);
					}
				}
			}
		}

		return stamp.getDate();
	}

	private Collection<IWTimestamp> getPublicHolidays(Locale locale) {
		Collection<IWTimestamp> collection = new ArrayList<IWTimestamp>();

		String holidays = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_PUBLIC_HOLIDAYS);
		if (holidays != null) {
			StringTokenizer tokens = new StringTokenizer(holidays, ",");
			while (tokens.hasMoreTokens()) {
				String holiday = tokens.nextToken();

				IWTimestamp stamp = new IWTimestamp();
				stamp.setAsDate();
				stamp.setDay(Integer.parseInt(holiday.substring(0, holiday
						.indexOf("."))));
				stamp.setMonth(Integer.parseInt(holiday.substring(holiday
						.indexOf(".") + 1)));
				collection.add(stamp);
			}
		}

		if (locale != null && locale.toString().equals("is_IS")) {
			IWTimestamp august = IWTimestamp.RightNow();
			august.setMonth(8);
			august.setDay(1);
			IWTimestamp commerceDay = null;
			while (commerceDay == null && august.getMonth() == 8) {
				commerceDay = august;
				if (commerceDay.getDayOfWeek() != Calendar.MONDAY) {
					august.addDays(1);
					commerceDay = null;
				} else {
					break;
				}
			}
			if (commerceDay != null) {
				collection.add(commerceDay);
			}
		}

		return collection;
	}

	@Override
	public CoursePriceHome getCoursePriceHome() {
		try {
			return (CoursePriceHome) IDOLookup.getHome(CoursePrice.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	@Override
	public CourseDiscountHome getCourseDiscountHome() {
		try {
			return (CourseDiscountHome) IDOLookup.getHome(CourseDiscount.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	@Override
	public CourseCategoryHome getCourseCategoryHome() {
		try {
			return (CourseCategoryHome) IDOLookup.getHome(CourseCategory.class);
		} catch (IDOLookupException ile) {
			throw new IDORuntimeException(ile);
		}
	}

	@Override
	public CourseTypeHome getCourseTypeHome() {
		try {
			return (CourseTypeHome) IDOLookup.getHome(CourseType.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	@Override
	public CourseHome getCourseHome() {
		try {
			return (CourseHome) IDOLookup.getHome(Course.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	@Override
	public CourseChoiceHome getCourseChoiceHome() {
		try {
			return (CourseChoiceHome) IDOLookup.getHome(CourseChoice.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	@Override
	public CourseApplicationHome getCourseApplicationHome() {
		try {
			return (CourseApplicationHome) IDOLookup
					.getHome(CourseApplication.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}

	private FamilyLogic getFamilyLogic() {
		try {
			return IBOLookup.getServiceInstance(
					getIWApplicationContext(), FamilyLogic.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private CitizenBusiness getUserBusiness() {
		try {
			return IBOLookup.getServiceInstance(
					getIWApplicationContext(), CitizenBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private SchoolBusiness getSchoolBusiness() {
		try {
			return IBOLookup.getServiceInstance(
					getIWApplicationContext(), SchoolBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private SchoolUserBusiness getSchoolUserBusiness() {
		try {
			return IBOLookup.getServiceInstance(
					getIWApplicationContext(), SchoolUserBusiness.class);
		} catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CommuneMessageBusiness getMessageBusiness() {
		try {
			return this
					.getServiceInstance(CommuneMessageBusiness.class);
		} catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private AccountingKeyBusiness getAccountingBusiness() {
		try {
			return IBOLookup.getServiceInstance(
					getIWApplicationContext(), AccountingKeyBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private CourseCertificateHome getCertificateHome() {
		try {
			return (CourseCertificateHome) getIDOHome(CourseCertificate.class);
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	@Override
	public CourseCertificate getCertificate(Object certificatePK) {
		try {
			return getCertificateHome().findByPrimaryKey(
					new Integer(certificatePK.toString()));
		} catch (FinderException fe) {
			log(fe);
			return null;
		}
	}

	@Override
	public CourseCertificateType getCourseCertificateType(String id) {
		if (id == null) {
			return null;
		}

		try {
			return (CourseCertificateType) getIDOHome(
					CourseCertificateType.class).findByPrimaryKeyIDO(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public CourseCertificateType getCourseCertificateTypeByType(String type) {
		if (type == null) {
			return null;
		}

		try {
			return ((CourseCertificateTypeHome) getIDOHome(CourseCertificateType.class))
					.findByType(Integer.valueOf(type));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List getUserCertificates(User user) {
		if (user == null) {
			return null;
		}

		Collection userCertificates = null;
		try {
			userCertificates = ((CourseCertificateHome) getIDOHome(CourseCertificate.class))
					.findAllCertificatesByUser(user);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		} catch (FinderException e) {
			e.printStackTrace();
			return null;
		}

		if (userCertificates == null) {
			return null;
		}
		return new ArrayList(userCertificates);
	}

	@Override
	public CourseCertificate getUserCertificate(User user, Course course) {
		try {
			return ((CourseCertificateHome) getIDOHome(CourseCertificate.class))
					.findByUserAndCourse(user, course);
		} catch (FinderException fe) {
			// Nothing found...
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}

		return null;
	}

	@Override
	public List getUserCertificatesByCourse(User user, Course course) {
		if (user == null || course == null) {
			return null;
		}

		Collection certificates = null;
		try {
			certificates = ((CourseCertificateHome) getIDOHome(CourseCertificate.class))
					.findAllCertificatesByUserAndCourse(user, course);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		if (certificates == null) {
			return null;
		}
		return new ArrayList(certificates);
	}

	@Override
	public IWTimestamp getLatestExpirationDateOfCertificate(List certificates) {
		if (certificates == null || certificates.isEmpty()) {
			return null;
		}

		CourseCertificate certificate = null;
		IWTimestamp time = null;
		for (int i = 0; i < certificates.size(); i++) {
			certificate = (CourseCertificate) certificates.get(i);

			IWTimestamp certificateExpireDate = certificate.getValidThru();
			if (certificateExpireDate != null) {
				if (time == null) {
					time = certificateExpireDate;
				} else {
					time = time.isEarlierThan(certificateExpireDate) ? certificateExpireDate
							: time;
				}
			}
		}

		return time;
	}

	@Override
	public IWTimestamp getLatestValidCertificate(List certificates) {
		if (certificates == null || certificates.isEmpty()) {
			return null;
		}

		CourseCertificate certificate = null;
		IWTimestamp time = null;
		for (int i = 0; i < certificates.size(); i++) {
			certificate = (CourseCertificate) certificates.get(i);

			IWTimestamp certificateValidDate = certificate.getValidThru();
			if (certificateValidDate != null) {
				if (time == null) {
					time = certificateValidDate;
				} else {
					time = time.isLaterThan(certificateValidDate) ? time
							: certificateValidDate;
				}
			}
		}

		return time;
	}

	@Override
	public boolean storeNotes(Integer courseChoiceID, String notes) {
		CourseChoice choice = getCourseChoice(courseChoiceID);
		if (choice == null) {
			return false;
		}

		choice.setNotes(notes);
		choice.store();
		return true;
	}

	@Override
	public boolean manageCourseChoiceSettings(String courseChoiceId,
			String columnName, boolean value) {
		if (courseChoiceId == null || columnName == null) {
			return false;
		}

		CourseChoice choice = getCourseChoice(courseChoiceId);
		if (choice == null) {
			return false;
		}

		choice.setBooleanValueForColumn(value, columnName);
		choice.store();

		if (columnName.equals(CourseChoiceBMPBean.COLUMN_CREATE_LOGIN)) {
			User user = choice.getUser();
			String login = "vm-" + choice.getUser().getPersonalID();
			String password = LoginDBHandler.getGeneratedPasswordForUser(user);
			String idString = getIWApplicationContext()
					.getApplicationSettings().getProperty(
							"WEIGHERS_HOME_GROUP_ID", "383503");
			int id = Integer.parseInt(idString);

			try {
				if (!this.getUserBusiness().isMemberOfGroup(id, user)) {
					this.getUserBusiness().createUserLogin(choice.getUser(),
							login, password, new Boolean(true),
							new IWTimestamp(), 2000, new Boolean(false),
							new Boolean(true), new Boolean(false), "MD5");

					try {
						this.getUserBusiness().getGroupBusiness().addUser(id,
								user);
						user.setPrimaryGroupID(id);
					} catch (EJBException e) {
						e.printStackTrace();
					}

					Locale locale = this.getIWApplicationContext()
							.getApplicationSettings().getDefaultLocale();
					String subject = getLocalizedString(
							"course_choice.password_created_subject",
							"Access to weighers portal", locale);
					String body = getLocalizedString(
							"course_choice.password_created_body",
							"Login {0}, password {1}", locale);

					Object[] arguments = { login, password };
					getMessageBusiness()
							.createUserMessage(choice.getApplication(), user,
									subject,
									MessageFormat.format(body, arguments),
									false, false);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	@Override
	public List getCourseParticipantListRowData(CourseChoice choice,
			IWResourceBundle iwrb) {
		if (choice == null) {
			return null;
		}

		List checkBoxesInfo = getCheckBoxesForCourseParticipants(iwrb);
		if (checkBoxesInfo == null || checkBoxesInfo.isEmpty()) {
			return null;
		}

		boolean show = true;
		boolean disabled = false;
		List data = new ArrayList();
		AdvancedProperty prop = null;
		CourseType courseType = null;
		CourseParticipantListRowData cellData = null;
		for (int i = 0; i < checkBoxesInfo.size(); i++) {
			disabled = false;
			show = true;
			prop = (AdvancedProperty) checkBoxesInfo.get(i);

			cellData = new CourseParticipantListRowData();
			cellData.setColumnName(prop.getValue());

			if (i + 2 == checkBoxesInfo.size()) {
				// The last checkbox
				// 1. If checkboxes 3, 4 and 5 are check THEN the last one
				// should be enabled, otherwise disabled
				disabled = !(choice
						.getBooleanValueFromColumn(((AdvancedProperty) checkBoxesInfo
								.get(2)).getValue())
						&& choice
								.getBooleanValueFromColumn(((AdvancedProperty) checkBoxesInfo
										.get(3)).getValue()) && choice
						.getBooleanValueFromColumn(((AdvancedProperty) checkBoxesInfo
								.get(4)).getValue())); // Have
				// to
				// get
				// "right papers" to
				// mark as passed

				// 2. If checkbox 6 is checked then the last one should never be
				// enabled
				disabled = choice
						.getBooleanValueFromColumn(((AdvancedProperty) checkBoxesInfo
								.get(5)).getValue());
			}

			if (i < 2 || i == 5) {
				if (courseType == null) {
					try {
						courseType = choice.getCourse().getCourseType();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (courseType == null) {
					show = false;
				} else {
					// 3a. Checkboxes 1 and 2 should only be shown when 'Tegund'
					// = 'Bráðabirgðalöggilding'
					show = EXTRA_ATTENTION_COURSE_TYPE
							.equalsIgnoreCase(courseType.getName())
							&& EXTRA_ATTENTION_COURSE_TYPE_ABBREVIATION
									.equalsIgnoreCase(courseType
											.getAbbreviation());

					if (i == 5) {
						// 3b. If 'Tegund' = 'Bráðabirgðalöggilding' then
						// checkbox 6 should not be shown at all
						show = !show;
					}
				}
			} else {
				show = true;
				courseType = null;
			}

			if (i == 1) {
				// 3c. If checkbox 1 is checked, then checkbox 2 should be
				// enabled, otherwise false
				disabled = !choice
						.getBooleanValueFromColumn(((AdvancedProperty) checkBoxesInfo
								.get(0)).getValue());
			}

			if (i == 1 && !disabled) {
				// 3d. If checkbox 2 is enabled it has to be checked for the
				// last one to be enabled
				cellData.setForceToCheck(true);
			}

			// cellData.setDisabled(disabled);
			// cellData.setShow(show);

			if (i + 1 == checkBoxesInfo.size()) {
				disabled = choice
						.getBooleanValueFromColumn(((AdvancedProperty) checkBoxesInfo
								.get(i)).getValue());
			}

			data.add(cellData);
		}

		return data;
	}

	@Override
	public List getCheckBoxesForCourseParticipants(IWResourceBundle iwrb) {
		List info = new ArrayList();

		info.add(new AdvancedProperty("need_verification_from_goverment_office", CourseChoiceBMPBean.COLUMN_NEED_VERIFICATION_FROM_GOVERMENT_OFFICE));
		info.add(new AdvancedProperty("verification_from_goverment_office", CourseChoiceBMPBean.COLUMN_VERIFICATION_FROM_GOVERMENT_OFFICE));
		info.add(new AdvancedProperty("certificate_of_property", CourseChoiceBMPBean.COLUMN_CERTIFICATE_OF_PROPERTY));
		info.add(new AdvancedProperty("criminal_record", CourseChoiceBMPBean.COLUMN_CRIMINAL_RECORD));
		info.add(new AdvancedProperty("verification_of_payment", CourseChoiceBMPBean.COLUMN_VERIFICATION_OF_PAYMENT));
		info.add(new AdvancedProperty("limited_certificate", CourseChoiceBMPBean.COLUMN_LIMITED_CERTIFICATE));
		info.add(new AdvancedProperty("did_not_show_up", CourseChoiceBMPBean.COLUMN_DID_NOT_SHOW_UP));
		info.add(new AdvancedProperty("passed_course", CourseChoiceBMPBean.COLUMN_PASSED));
		info.add(new AdvancedProperty("create_login", CourseChoiceBMPBean.COLUMN_CREATE_LOGIN));

		return info;
	}

	@Override
	public boolean acceptChoice(Object courseChoicePK, Locale locale) {
		CourseChoice choice = getCourseChoice(courseChoicePK);
		if (choice == null) {
			return false;
		}

		if (StringUtil.isEmpty(choice.getUniqueID())) {
			choice.setUniqueID(IdGeneratorFactory.getUUIDGenerator().generateId());
			choice.store();
		}
		if (choice.isOnWaitingList() && isWaitingListTurnedOn()) {
			//	Will send email to parents to accept offer
			if (locale == null) {
				locale = getIWApplicationContext().getApplicationSettings().getDefaultLocale();
			}

			String subject = getLocalizedString(
					"course_choice.offered_to_parent_subject", "Course choice now available",
					locale
			);

			CourseApplication application = choice.getApplication();
			String prefix = application.getPrefix();
			prefix = prefix != null && prefix.length() > 0 ? prefix + "." : "";

			String body = getLocalizedString(prefix +
					"course_choice.course_offered_to_parent_body",
					"Your registration to course {2} at {3} for {0}, {1} now is available and needs to be confirmed at {6}.",
					locale
			);

			String bcc = getIWApplicationContext().getApplicationSettings().getProperty(
						CourseConstants.PROPERTY_BCC_EMAIL + (application.getPrefix() != null ? "." + application.getPrefix() : ""),
						CoreConstants.EMPTY
			);

			sendMessageToParents(application, choice, subject, body, locale, bcc);
		} else {
			//	Choice was not on a waiting list, can be accepted without parent's confirmation
			choice.setWaitingList(false);
			choice.store();

			CourseApplication application = choice.getApplication();
			String subject = getLocalizedString(
					"course_choice.accepted_subject", "Course choice accepted",
					locale
			);

			String prefix = application.getPrefix();
			prefix = prefix != null && prefix.length() > 0 ? prefix + "." : "";

			String body = getLocalizedString(prefix +
					"course_choice.accepted_body",
					"Your registration to course {2} at {3} for {0}, {1} has been accepted.",
					locale
			);

			String bcc = getIWApplicationContext().getApplicationSettings().getProperty(
						CourseConstants.PROPERTY_BCC_EMAIL + (application.getPrefix() != null ? "." + application.getPrefix() : ""),
						CoreConstants.EMPTY
			);

			sendMessageToParents(application, choice, subject, body, locale, bcc);
		}

		return true;
	}

	@Override
	public boolean parentsAcceptChoice(Object courseChoicePK, User performer, Locale locale) {
		CourseChoice choice = getCourseChoice(courseChoicePK);
		choice.setWaitingList(false);
		choice.setUniqueID(null);
		choice.store();

		Course course = choice.getCourse();
		course.getProvider();

		CourseApplication application = choice.getApplication();
		String subject = getLocalizedString(
				"course_choice.parent_accepted_subject", "Course choice accepted",
				locale
		);

		String prefix = application.getPrefix();
		prefix = prefix != null && prefix.length() > 0 ? prefix + "." : "";

		String body = getLocalizedString(prefix +
				"course_choice.parent_accepted_body",
				"Registration to course {2} at for {0}, {1} has been accepted by parents.",
				locale
		);

		sendMessageToProvider(choice, subject, body, performer, locale);

		return true;
	}

	@Override
	public void setNoPayment(Object courseChoicePK, boolean noPayment) {
		CourseChoice choice = getCourseChoice(courseChoicePK);
		choice.setNoPayment(noPayment);
		choice.store();
	}

	@Override
	public void removeCertificate(Object certificatePK) {
		CourseCertificate certificate = getCertificate(certificatePK);
		if (certificate != null) {
			try {
				certificate.remove();
			} catch (RemoveException e) {
				log(e);
			}
		}
	}

	@Override
	public void sendNextCoursesMessages() {
		IWTimestamp fromDate = new IWTimestamp();
		if (fromDate.getDayOfWeek() == Calendar.SUNDAY) {
			fromDate.addDays(1);
		} else {
			fromDate.addDays(7 - (fromDate.getDayOfWeek() - Calendar.MONDAY));
		}

		IWTimestamp toDate = new IWTimestamp(fromDate);
		toDate.addDays(6);

		Locale locale = getIWApplicationContext().getApplicationSettings().getDefaultLocale();

		try {
			Collection<Course> courses = getCourseHome().findAll(null, null, null, -1, fromDate.getDate(), toDate.getDate());
			int count = 0;

			String locKey = "course_choice.reminder_body";

			IWResourceBundle iwrb = getIWMainApplication().getBundle(getBundleIdentifier()).getResourceBundle(locale);
			for (Course course: courses) {
				if (course == null) {
					continue;
				}

				CourseType type = course.getCourseType();
				String typePrKey = type == null ? null : type.getPrimaryKey().toString();
				CourseCategory category = type == null ? null : type.getCourseCategory();
				if (category == null) {
					continue;
				}

				if (category.sendReminderEmail()) {
					Collection<CourseChoice> choices = getCourseChoiceHome().findAllByCourse(course, false);
					for (CourseChoice choice: choices) {
						if (choice == null || !choice.isValid()) {
							continue;
						}

						if (!choice.hasReceivedReminder()) {
							CourseApplication application = choice.getApplication();

							String subject = typePrKey == null ? null : iwrb.getLocalizedString(typePrKey + ".course_choice.reminder_subject");
							if (StringUtil.isEmpty(subject)) {
								subject = getLocalizedString(
										(application.getPrefix() != null ? application.getPrefix() + "." : "") + "course_choice.reminder_subject",
										"A reminder for course choice",
										locale
								);
							}
							String body = typePrKey == null ? null : iwrb.getLocalizedString(typePrKey + CoreConstants.DOT + locKey);
							if (StringUtil.isEmpty(body)) {
								body = getLocalizedString(
										(application.getPrefix() != null ? application.getPrefix() + CoreConstants.DOT : CoreConstants.EMPTY) +	locKey,
										"This is a reminder for your registration to course {2} at {3} for {0}, {1}.",
										locale
								);
							}

							sendMessageToParents(application, choice, subject, body, locale);

							choice.setReceivedReminder(true);
							choice.store();
							count++;
						}
					}
				}
			}

			System.out.println("[CourseBusiness] "
					+ IWTimestamp.getTimestampRightNow().toString()
					+ " - Done sending reminder messages for courses between "
					+ fromDate.toSQLDateString() + " and "
					+ toDate.toSQLDateString() + ": " + count);
		} catch (IDORelationshipException ire) {
			log(ire);
		} catch (FinderException fe) {
			log(fe);
		}
	}

	@Override
	public Collection getAllChoicesByCourseAndDate(Object coursePK,
			IWTimestamp fromDate, IWTimestamp toDate) {
		Course course = null;
		if (coursePK != null) {
			course = getCourse(coursePK);
		}

		try {
			return getCourseChoiceHome().findAllByCourseAndDate(course,
					fromDate, toDate);
		} catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	@Override
	public Collection<Course> getCoursesByTypes(Collection<String> typesIds)
			throws RemoteException {
		if (ListUtil.isEmpty(typesIds)) {
			return null;
		}

		try {
			return getCourseHome().findAllByTypes(typesIds);
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}
}