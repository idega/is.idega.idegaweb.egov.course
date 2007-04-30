package is.idega.idegaweb.egov.course.data;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.sql.Date;

public interface CourseDiscountHome extends IDOHome {

	public CourseDiscount create() throws CreateException;

	public CourseDiscount findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException, IDORelationshipException;

	public Collection findAll(Date fromDate, Date toDate) throws FinderException;
}