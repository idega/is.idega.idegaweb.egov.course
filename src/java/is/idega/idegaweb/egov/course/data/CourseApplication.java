package is.idega.idegaweb.egov.course.data;


import com.idega.block.process.data.Case;
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
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getPayerName
	 */
	public String getPayerName();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#getPayerPersonalID
	 */
	public String getPayerPersonalID();

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
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setPayerName
	 */
	public void setPayerName(String name);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseApplicationBMPBean#setPayerPersonalID
	 */
	public void setPayerPersonalID(String personalID);
}