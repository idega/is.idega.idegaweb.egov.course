package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;

public interface CourseDiscountHome extends IDOHome {

	public CourseDiscount create() throws CreateException;

	public CourseDiscount findByPrimaryKey(Object pk) throws FinderException;

	public Collection<CourseDiscount> findAll() throws FinderException, IDORelationshipException;

	public Collection<CourseDiscount> findAll(Date fromDate, Date toDate) throws FinderException;
}