package is.idega.idegaweb.egov.course.data;

import is.idega.idegaweb.egov.course.CourseConstants;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.company.data.Company;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.User;

public class CourseApplicationBMPBean extends AbstractCaseBMPBean implements Case, CourseApplication {

	private static final String ENTITY_NAME = "cou_course_application";

	private static final String COLUMN_CREDITCARD_MERCHANT_ID = "merchant_id";
	private static final String COLUMN_CREDITCARD_MERCHANT_TYPE = "merchant_type";
	private static final String COLUMN_REFERENCE_NUMBER = "reference_number";
	private static final String COLUMN_PAYMENT_TYPE = "payment_type";
	private static final String COLUMN_PAID = "paid";
	private static final String COLUMN_PAYMENT_TIMESTAMP = "payment_timestamp";
	private final static String COLUMN_PAYER_NAME = "payer_name";
	private final static String COLUMN_PAYER_PERSONAL_ID = "payer_personal_id";
	private static final String COLUMN_AMOUNT = "amount";
	private static final String COLUMN_COMPANY = "company_id";
	private static final String COLUMN_PREFIX = "prefix";

	private final static String COLUMN_CARD_TYPE = "card_type";
	private final static String COLUMN_CARD_NUMBER = "card_number";
	private final static String COLUMN_CARD_VALID_MONTH = "card_valid_month";
	private final static String COLUMN_CARD_VALID_YEAR = "card_valid_year";

	@Override
	public String getCaseCodeDescription() {
		return "Case for courses";
	}

	@Override
	public String getCaseCodeKey() {
		return CourseConstants.CASE_CODE_KEY;
	}

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public void initializeAttributes() {
		addGeneralCaseRelation();

		addAttribute(COLUMN_CREDITCARD_MERCHANT_ID, "Creditcard merchant ID", Integer.class);
		addAttribute(COLUMN_CREDITCARD_MERCHANT_TYPE, "Creditcard merchant type", String.class, 20);
		addAttribute(COLUMN_REFERENCE_NUMBER, "Reference number", String.class, 255);
		addAttribute(COLUMN_PAYMENT_TYPE, "Payment type", String.class, 20);
		addAttribute(COLUMN_PAID, "Paid", Boolean.class);
		addAttribute(COLUMN_PAYMENT_TIMESTAMP, "Payment timestamp", Timestamp.class);
		addAttribute(COLUMN_AMOUNT, "Amount", Float.class);
		addAttribute(COLUMN_PAYER_PERSONAL_ID, "Payer personal ID", String.class);
		addAttribute(COLUMN_PAYER_NAME, "Payer name", String.class);
		addAttribute(COLUMN_PREFIX, "Prefix", String.class);

		addAttribute(COLUMN_CARD_TYPE, "Card type", String.class);
		addAttribute(COLUMN_CARD_NUMBER, "Card number", String.class);
		addAttribute(COLUMN_CARD_VALID_MONTH, "Valid month", Integer.class);
		addAttribute(COLUMN_CARD_VALID_YEAR, "Valid year", Integer.class);

		addManyToOneRelationship(COLUMN_COMPANY, Company.class);
	}

	// Getters
	public int getCreditCardMerchantID() {
		return getIntColumnValue(COLUMN_CREDITCARD_MERCHANT_ID);
	}

	public String getCreditCardMerchantType() {
		return getStringColumnValue(COLUMN_CREDITCARD_MERCHANT_TYPE);
	}

	public String getReferenceNumber() {
		return getStringColumnValue(COLUMN_REFERENCE_NUMBER);
	}

	public String getPaymentType() {
		return getStringColumnValue(COLUMN_PAYMENT_TYPE);
	}

	public boolean isPaid() {
		return getBooleanColumnValue(COLUMN_PAID, false);
	}

	public Timestamp getPaymentTimestamp() {
		return getTimestampColumnValue(COLUMN_PAYMENT_TIMESTAMP);
	}

	public String getPayerName() {
		return getStringColumnValue(COLUMN_PAYER_NAME);
	}

	public String getPayerPersonalID() {
		return getStringColumnValue(COLUMN_PAYER_PERSONAL_ID);
	}
	
	public String getPrefix() {
		return getStringColumnValue(COLUMN_PREFIX);
	}

	public float getAmount() {
		return getFloatColumnValue(COLUMN_AMOUNT);
	}

	public Company getCompany() {
		return (Company) getColumnValue(COLUMN_COMPANY);
	}

	public String getCardType() {
		return getStringColumnValue(COLUMN_CARD_TYPE);
	}

	public String getCardNumber() {
		return getStringColumnValue(COLUMN_CARD_NUMBER);
	}

	public int getCardValidMonth() {
		return getIntColumnValue(COLUMN_CARD_VALID_MONTH);
	}

	public int getCardValidYear() {
		return getIntColumnValue(COLUMN_CARD_VALID_YEAR);
	}

	// Setters
	public void setCreditCardMerchantID(int merchantID) {
		setColumn(COLUMN_CREDITCARD_MERCHANT_ID, merchantID);
	}

	public void setCreditCardMerchantType(String merchantType) {
		setColumn(COLUMN_CREDITCARD_MERCHANT_TYPE, merchantType);
	}

	public void setReferenceNumber(String referenceNumber) {
		setColumn(COLUMN_REFERENCE_NUMBER, referenceNumber);
	}

	public void setPaymentType(String paymentType) {
		setColumn(COLUMN_PAYMENT_TYPE, paymentType);
	}

	public void setPaid(boolean paid) {
		setColumn(COLUMN_PAID, paid);
	}

	public void setPaymentTimestamp(Timestamp timestamp) {
		setColumn(COLUMN_PAYMENT_TIMESTAMP, timestamp);
	}

	public void setPayerName(String name) {
		setColumn(COLUMN_PAYER_NAME, name);
	}

	public void setPayerPersonalID(String personalID) {
		setColumn(COLUMN_PAYER_PERSONAL_ID, personalID);
	}
	
	public void setPrefix(String prefix) {
		setColumn(COLUMN_PREFIX, prefix);
	}

	public void setAmount(float amount) {
		setColumn(COLUMN_AMOUNT, amount);
	}

	public void setCompany(Company company) {
		setColumn(COLUMN_COMPANY, company);
	}

	public void setCardType(String type) {
		setColumn(COLUMN_CARD_TYPE, type);
	}

	public void setCardNumber(String number) {
		setColumn(COLUMN_CARD_NUMBER, number);
	}

	public void setCardValidMonth(int month) {
		setColumn(COLUMN_CARD_VALID_MONTH, month);
	}

	public void setCardValidYear(int year) {
		setColumn(COLUMN_CARD_VALID_YEAR, year);
	}

	// Finders
	public Collection ejbFindAll(CaseStatus caseStatus, Date fromDate, Date toDate) throws FinderException {
		Table table = new Table(this);
		Table process = new Table(Case.class);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		try {
			query.addJoin(table, process);
		}
		catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
		query.addCriteria(new MatchCriteria(process.getColumn(getSQLGeneralCaseCaseStatusColumnName()), MatchCriteria.EQUALS, caseStatus));
		if (fromDate != null) {
			query.addCriteria(new MatchCriteria(process.getColumn(getSQLGeneralCaseCreatedColumnName()), MatchCriteria.GREATEREQUAL, fromDate));
		}
		if (toDate != null) {
			query.addCriteria(new MatchCriteria(process.getColumn(getSQLGeneralCaseCreatedColumnName()), MatchCriteria.LESSEQUAL, toDate));
		}
		query.addOrder(process, getSQLGeneralCaseCreatedColumnName(), true);

		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public void addSubscriber(User subscriber)
			throws IDOAddRelationshipException {
		throw new UnsupportedOperationException("This method is not implemented!");
	}

	public Collection<User> getSubscribers() {
		throw new UnsupportedOperationException("This method is not implemented!");
	}

	public void removeSubscriber(User subscriber)
			throws IDORemoveRelationshipException {
		throw new UnsupportedOperationException("This method is not implemented!");
	}
}