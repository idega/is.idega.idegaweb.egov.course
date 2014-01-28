package is.idega.idegaweb.egov.course.data;


import java.util.Collection;
import java.util.Collections;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;

public interface CourseTypeHome extends IDOHome {

	public CourseType create() throws CreateException;

	/**
	 * 
	 * @param pk is {@link CourseType#getPrimaryKey()} to search by, 
	 * not <code>null</code>;
	 * @return found entity or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public CourseType findByPrimaryKey(Object pk);

	public Collection<CourseType> findAll(boolean valid) throws FinderException;

	public Collection<CourseType> findAllBySchoolType(Object schoolTypePK, boolean valid) throws FinderException, IDORelationshipException;

	public CourseType findByAbbreviation(String abbreviation) throws FinderException;
	
	public CourseType findByName(String name) throws FinderException;

	/**
	 * 
	 * @param courseCategories to search by, not <code>null</code>;
	 * @param valid tells that {@link CourseCategory} is not disabled;
	 * @return {@link CourseType}s by {@link CourseCategory}s or 
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<CourseType> findAllByCategory(
			Collection<CourseCategory> courseCategories, 
			boolean valid);
}