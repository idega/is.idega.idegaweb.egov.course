package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.sql.Date;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class CourseDiscountHomeImpl extends IDOFactory implements CourseDiscountHome {

	public Class getEntityInterfaceClass() {
		return CourseDiscount.class;
	}

	public CourseDiscount create() throws CreateException {
		return (CourseDiscount) super.createIDO();
	}

	public CourseDiscount findByPrimaryKey(Object pk) throws FinderException {
		return (CourseDiscount) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseDiscountBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll(Date fromDate, Date toDate) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseDiscountBMPBean) entity).ejbFindAll(fromDate, toDate);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}