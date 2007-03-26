package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.block.school.data.SchoolType;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

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

	public Collection findAll() throws FinderException,
			IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CoursePriceBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll(SchoolType sType, CourseType cType)
			throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CoursePriceBMPBean) entity).ejbFindAll(sType, cType);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}