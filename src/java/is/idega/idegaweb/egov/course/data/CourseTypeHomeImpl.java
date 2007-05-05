package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class CourseTypeHomeImpl extends IDOFactory implements CourseTypeHome {

	public Class getEntityInterfaceClass() {
		return CourseType.class;
	}

	public CourseType create() throws CreateException {
		return (CourseType) super.createIDO();
	}

	public CourseType findByPrimaryKey(Object pk) throws FinderException {
		return (CourseType) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseTypeBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySchoolType(Object schoolTypePK) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseTypeBMPBean) entity).ejbFindAllBySchoolType(schoolTypePK);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}