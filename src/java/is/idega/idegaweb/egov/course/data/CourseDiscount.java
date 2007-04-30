package is.idega.idegaweb.egov.course.data;


import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface CourseDiscount extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#getType
	 */
	public String getType();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#getValidFrom
	 */
	public Timestamp getValidFrom();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#getValidTo
	 */
	public Timestamp getValidTo();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#getDiscount
	 */
	public float getDiscount();

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#setType
	 */
	public void setType(String type);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#setValidFrom
	 */
	public void setValidFrom(Timestamp stamp);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#setValidTo
	 */
	public void setValidTo(Timestamp stamp);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#setValid
	 */
	public void setValid(boolean valid);

	/**
	 * @see is.idega.idegaweb.egov.course.data.CourseDiscountBMPBean#setDiscount
	 */
	public void setDiscount(float discount);
}