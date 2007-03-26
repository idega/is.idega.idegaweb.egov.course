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
}