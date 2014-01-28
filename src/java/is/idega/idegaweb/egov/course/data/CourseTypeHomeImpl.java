package is.idega.idegaweb.egov.course.data;


import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;
import com.idega.util.ListUtil;

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
		if (pk == null) {
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

	public Collection findAll(boolean valid) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseTypeBMPBean) entity).ejbFindAll(valid);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolType(Object schoolTypePK, boolean valid) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseTypeBMPBean) entity).ejbFindAllBySchoolType(schoolTypePK, valid);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.CourseTypeHome#findAllByCategory(java.util.Collection, boolean)
	 */
	@Override
	public Collection<CourseType> findAllByCategory(
			Collection<CourseCategory> courseCategories, 
			boolean valid) {
		if (ListUtil.isEmpty(courseCategories)) {
			return Collections.emptyList();
		}

		CourseTypeBMPBean entity = (CourseTypeBMPBean) idoCheckOutPooledEntity();
		if (entity == null) {
			return Collections.emptyList();
		}

		Collection<Object> primaryKeys = entity.ejbFindAllByCategories(courseCategories, valid);
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
}