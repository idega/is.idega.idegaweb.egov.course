package is.idega.idegaweb.egov.course.data;


import javax.ejb.CreateException;

import com.idega.data.IDOHome;

public interface CourseCategoryHome extends IDOHome {

	public CourseCategory create() throws CreateException;

	/**
	 * 
	 * @param pk is {@link CourseCategory#getPrimaryKey()}, not <code>null</code>;
	 * @return entity found by criteria or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public CourseCategory findByPrimaryKey(String pk);
}