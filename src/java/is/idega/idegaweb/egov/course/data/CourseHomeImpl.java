package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class CourseHomeImpl extends IDOFactory implements CourseHome {
	public Class getEntityInterfaceClass() {
		return Course.class;
	}

	public Course create() throws CreateException {
		return (Course) super.createIDO();
	}

	public Course findByPrimaryKey(Object pk) throws FinderException {
		return (Course) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException,
			IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll(Integer schoolTypeID, Integer courseTypeID,
			int birthYear) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseBMPBean) entity).ejbFindAll(schoolTypeID,
				courseTypeID, birthYear);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}