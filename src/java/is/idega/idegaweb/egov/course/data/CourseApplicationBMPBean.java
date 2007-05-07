package is.idega.idegaweb.egov.course.data;

import is.idega.idegaweb.egov.course.CourseConstants;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class CourseApplicationBMPBean extends AbstractCaseBMPBean implements Case, CourseApplication {

	private static final String ENTITY_NAME = "cou_course_application";

	private static final String COLUMN_CREDITCARD_MERCHANT_ID = "merchant_id";
	private static final String COLUMN_CREDITCARD_MERCHANT_TYPE = "merchant_type";
	private static final String COLUMN_REFERENCE_NUMBER = "reference_number";
	private static final String COLUMN_PAYMENT_TYPE = "payment_type";
	private static final String COLUMN_PAID = "paid";
	private final static String COLUMN_PAYER_NAME = "payer_name";
	private final static String COLUMN_PAYER_PERSONAL_ID = "payer_personal_id";
	private static final String COLUMN_AMOUNT = "amount";

	public String getCaseCodeDescription() {
		return "Case for courses";
	}

	public String getCaseCodeKey() {
		return CourseConstants.CASE_CODE_KEY;
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addGeneralCaseRelation();

		addAttribute(COLUMN_CREDITCARD_MERCHANT_ID, "Creditcard merchant ID", Integer.class);
		addAttribute(COLUMN_CREDITCARD_MERCHANT_TYPE, "Creditcard merchant type", String.class, 20);
		addAttribute(COLUMN_REFERENCE_NUMBER, "Reference number", String.class, 255);
		addAttribute(COLUMN_PAYMENT_TYPE, "Payment type", String.class, 20);
		addAttribute(COLUMN_PAID, "Paid", Boolean.class);
		addAttribute(COLUMN_AMOUNT, "Amount", Float.class);
		addAttribute(COLUMN_PAYER_PERSONAL_ID, "Payer personal ID", String.class);
		addAttribute(COLUMN_PAYER_NAME, "Payer name", String.class);
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

	public String getPayerName() {
		return getStringColumnValue(COLUMN_PAYER_NAME);
	}

	public String getPayerPersonalID() {
		return getStringColumnValue(COLUMN_PAYER_PERSONAL_ID);
	}

	public float getAmount() {
		return getFloatColumnValue(COLUMN_AMOUNT);
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

	public void setPayerName(String name) {
		setColumn(COLUMN_PAYER_NAME, name);
	}

	public void setPayerPersonalID(String personalID) {
		setColumn(COLUMN_PAYER_PERSONAL_ID, personalID);
	}

	public void setAmount(float amount) {
		setColumn(COLUMN_AMOUNT, amount);
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
}