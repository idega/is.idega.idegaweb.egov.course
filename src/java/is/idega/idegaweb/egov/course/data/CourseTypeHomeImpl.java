package is.idega.idegaweb.egov.course.data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

public class CourseTypeHomeImpl extends IDOFactory implements CourseTypeHome {

	private static final long serialVersionUID = 156516411744536846L;

	@Override
	public Class<CourseType> getEntityInterfaceClass() {
		return CourseType.class;
	}

	public CourseType create() throws CreateException {
		return (CourseType) super.createIDO();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeHome#findByPrimaryKey(java.lang.Object)
	 */
	@Override
	public CourseType findByPrimaryKey(Object pk) {
		if (pk == null || StringUtil.isEmpty(pk.toString())) {
			return null;
		}

		try {
			return (CourseType) findByPrimaryKeyIDO(pk);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).warning(
					"Failed to get " + getEntityInterfaceClass().getSimpleName() +
					" by id: '" + pk + "'");
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeHome#findAll(boolean)
	 */
	@Override
	public Collection<CourseType> findAll(boolean valid) {
		CourseTypeBMPBean entity = (CourseTypeBMPBean) idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<Object> ids = entity.ejbFindAll(valid);
		if (ListUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}

		try {
			return this.getEntityCollectionForPrimaryKeys(ids);
		} catch (FinderException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"Failed ot get " + CourseType.class.getSimpleName() + 
					" by id's: '" + ids + "'");
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeHome#findAllBySchoolType(java.lang.Object, boolean)
	 */
	@Override
	public Collection<CourseType> findAllBySchoolType(Object schoolTypePK, boolean valid) {
		if (schoolTypePK == null) {
			return Collections.emptyList();
		}

		return findByCourseProviderTypes(getCourseProviderTypeHome()
				.findByPrimaryKeys(
						Arrays.asList(schoolTypePK.toString())), valid);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeHome#findByCourseProviderTypes(java.util.Collection, boolean)
	 */
	@Override
	public Collection<CourseType> findByCourseProviderTypes(
			Collection<? extends CourseProviderType> types, boolean valid) {
		if (ListUtil.isEmpty(types)) {
			return Collections.emptyList();
		}

		ArrayList<String> primaryKeys = new ArrayList<String>(types.size());
		for (CourseProviderType type : types) {
			primaryKeys.add(type.getPrimaryKey().toString());
		}

		Collection<CourseCategory> categories = getCourseCategoryHome()
				.findByPrimaryKeys(primaryKeys);
		return findAllByCategory(categories, valid);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeHome#findAllByCategory(java.util.Collection, boolean)
	 */
	@Override
	public Collection<CourseType> findAllByCategory(
			Collection<? extends CourseCategory> courseCategories, 
			boolean valid) {
		if (ListUtil.isEmpty(courseCategories)) {
			return Collections.emptyList();
		}

		CourseTypeBMPBean entity = (CourseTypeBMPBean) idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<Object> primaryKeys = entity.ejbFindAllByCategories(
				courseCategories, valid);
		if (ListUtil.isEmpty(primaryKeys)) {
			return Collections.emptyList();
		}

		try {
			return getEntityCollectionForPrimaryKeys(primaryKeys);
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get " + getEntityInterfaceClass().getSimpleName() + 
					" by primary keys: '" + primaryKeys + "'");
		}

		return Collections.emptyList();
	}

	public CourseType findByAbbreviation(String abbreviation) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CourseTypeBMPBean) entity).ejbFindByAbbreviation(abbreviation);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public CourseType findByName(String name) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((CourseTypeBMPBean) entity).ejbFindByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	private CourseCategoryHome courseCategoryHome = null;

	protected CourseCategoryHome getCourseCategoryHome() {
		if (this.courseCategoryHome == null) {
			try {
				this.courseCategoryHome = (CourseCategoryHome) IDOLookup.getHome(
						CourseCategory.class);
			} catch (IDOLookupException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed ot get " + CourseCategoryHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.courseCategoryHome;
	}

	private CourseProviderTypeHome courseProviderTypeHome = null;

	protected CourseProviderTypeHome getCourseProviderTypeHome() {
		if (this.courseProviderTypeHome == null) {
			try {
				this.courseProviderTypeHome = (CourseProviderTypeHome) IDOLookup
						.getHome(CourseProviderType.class);
			} catch (IDOLookupException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to get " + CourseProviderTypeHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderTypeHome;
	}
}