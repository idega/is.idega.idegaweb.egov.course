package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;
import com.idega.data.IDORelationshipException;

public class CoursePriceHomeImpl extends IDOFactory implements CoursePriceHome {

	public Class getEntityInterfaceClass() {
		return CoursePrice.class;
	}

	public CoursePrice create() throws CreateException {
		return (CoursePrice) super.createIDO();
	}

	public CoursePrice findByPrimaryKey(Object pk) throws FinderException {
		return (CoursePrice) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CoursePriceBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll(Date fromDate, Date toDate) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CoursePriceBMPBean) entity).ejbFindAll(fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll(CourseProviderArea area, CourseType cType) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CoursePriceBMPBean) entity).ejbFindAll(area, cType);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll(CourseProviderArea area, CourseType cType, Date fromDate, Date toDate) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CoursePriceBMPBean) entity).ejbFindAll(area, cType, fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}