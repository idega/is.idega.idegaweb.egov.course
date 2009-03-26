package is.idega.idegaweb.egov.course.data;


import com.idega.block.process.data.Case;
import com.idega.company.data.Company;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface CourseApplication extends IDOEntity, Case {

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getCaseCodeDescription
	 */
	public String getCaseCodeDescription();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getCaseCodeKey
	 */
	public String getCaseCodeKey();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getCreditCardMerchantID
	 */
	public int getCreditCardMerchantID();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getCreditCardMerchantType
	 */
	public String getCreditCardMerchantType();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getReferenceNumber
	 */
	public String getReferenceNumber();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getPaymentType
	 */
	public String getPaymentType();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#isPaid
	 */
	public boolean isPaid();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getPaymentTimestamp
	 */
	public Timestamp getPaymentTimestamp();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getPayerName
	 */
	public String getPayerName();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getPayerPersonalID
	 */
	public String getPayerPersonalID();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getAmount
	 */
	public float getAmount();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getCompany
	 */
	public Company getCompany();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getCardType
	 */
	public String getCardType();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getCardNumber
	 */
	public String getCardNumber();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getCardValidMonth
	 */
	public int getCardValidMonth();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getCardValidYear
	 */
	public int getCardValidYear();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setCreditCardMerchantID
	 */
	public void setCreditCardMerchantID(int merchantID);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setCreditCardMerchantType
	 */
	public void setCreditCardMerchantType(String merchantType);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setReferenceNumber
	 */
	public void setReferenceNumber(String referenceNumber);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setPaymentType
	 */
	public void setPaymentType(String paymentType);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setPaid
	 */
	public void setPaid(boolean paid);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setPaymentTimestamp
	 */
	public void setPaymentTimestamp(Timestamp timestamp);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setPayerName
	 */
	public void setPayerName(String name);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setPayerPersonalID
	 */
	public void setPayerPersonalID(String personalID);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setAmount
	 */
	public void setAmount(float amount);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setCompany
	 */
	public void setCompany(Company company);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setCardType
	 */
	public void setCardType(String type);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setCardNumber
	 */
	public void setCardNumber(String number);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setCardValidMonth
	 */
	public void setCardValidMonth(int month);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setCardValidYear
	 */
	public void setCardValidYear(int year);
}