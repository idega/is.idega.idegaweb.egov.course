package is.idega.idegaweb.egov.course.data;


import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOFactory;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

public class CourseCategoryHomeImpl extends IDOFactory implements CourseCategoryHome {

	public Class getEntityInterfaceClass() {
		return CourseCategory.class;
	}

	public CourseCategory create() throws CreateException {
		return (CourseCategory) super.createIDO();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseCategoryHome#findByPrimaryKeys(java.util.Collection)
	 */
	@Override
	public Collection<CourseCategory> findByPrimaryKeys(
			Collection<String> primaryKeys) {
		if (!ListUtil.isEmpty(primaryKeys)) {
			try {
				return getEntityCollectionForPrimaryKeys(primaryKeys);
			} catch (FinderException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed ot get " + CourseCategory.class.getSimpleName() + "'s "
						+ "by primary keys: '" + primaryKeys + "'");
			}
		}

		return Collections.emptyList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseCategoryHome#findByPrimaryKey(java.lang.String)
	 */
	@Override
	public CourseCategory findByPrimaryKey(String pk) {
		if (StringUtil.isEmpty(pk)) {
			return null;
		}
		
		try {
			return (CourseCategory) findByPrimaryKeyIDO(pk);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get " + CourseCategory.class.getSimpleName() + 
					" by id: '" + pk + "'");
		}

		return null;
	}
}