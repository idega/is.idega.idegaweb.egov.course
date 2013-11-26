package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;

public interface CoursePriceHome extends IDOHome {

	public CoursePrice create() throws CreateException;

	public CoursePrice findByPrimaryKey(Object pk) throws FinderException;

	public Collection<CoursePrice> findAll() throws FinderException, IDORelationshipException;

	public Collection<CoursePrice> findAll(Date fromDate, Date toDate) throws FinderException, IDORelationshipException;

	public Collection<CoursePrice> findAll(CourseProviderArea area, CourseType cType) throws FinderException, IDORelationshipException;

	public Collection<CoursePrice> findAll(CourseProviderArea area, CourseType cType, Date fromDate, Date toDate) throws FinderException, IDORelationshipException;
}