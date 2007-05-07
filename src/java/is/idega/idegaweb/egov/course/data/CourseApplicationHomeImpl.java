package is.idega.idegaweb.egov.course.data;


import java.util.Collection;
import com.idega.block.process.data.CaseStatus;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.sql.Date;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class CourseApplicationHomeImpl extends IDOFactory implements
		CourseApplicationHome {
	public Class getEntityInterfaceClass() {
		return CourseApplication.class;
	}

	public CourseApplication create() throws CreateException {
		return (CourseApplication) super.createIDO();
	}

	public CourseApplication findByPrimaryKey(Object pk) throws FinderException {
		return (CourseApplication) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll(CaseStatus caseStatus, Date fromDate, Date toDate)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseApplicationBMPBean) entity).ejbFindAll(
				caseStatus, fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseApplicationBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}