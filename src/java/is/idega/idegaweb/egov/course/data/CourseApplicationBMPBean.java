package is.idega.idegaweb.egov.course.data;

import is.idega.idegaweb.egov.course.business.CourseBusinessBean;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.user.data.User;

public class CourseApplicationBMPBean extends AbstractCaseBMPBean implements Case, CourseApplication{

	private static final String TABLE_NAME				= "COU_COURSE_APPLICATION";
	
	private static final String COURSE_ID					= "COU_COURSE_ID";
	private static final String USER_ID						= "IC_USER_ID";
	private static final String CREDITCARD_MERCHANT_ID		= "CC_MERCHANT_ID";
	private static final String CREDITCARD_MERCHANT_TYPE	= "CC_MERCHANT_TYPE";
	private static final String PAYMENT_TYPE				= "PAYMENT_TYPE";
	
	public String getCaseCodeDescription() {
		return "Case for cources";
	}

	public String getCaseCodeKey() {
		return CourseBusinessBean.CASE_CODE_KEY;
	}

	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addGeneralCaseRelation();
		
		addManyToOneRelationship(COURSE_ID, Course.class);
		addManyToOneRelationship(USER_ID, User.class);
		addAttribute(CREDITCARD_MERCHANT_ID, "creditcard merchant id", Integer.class);
		addAttribute(CREDITCARD_MERCHANT_TYPE, "creditcard merchant type", String.class, 20);
		addAttribute(PAYMENT_TYPE, "payment type", String.class, 20);
	}

}
