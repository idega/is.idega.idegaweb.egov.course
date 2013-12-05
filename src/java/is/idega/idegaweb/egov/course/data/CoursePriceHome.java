package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.data.IDORelationshipException;

public interface CoursePriceHome extends IDOHome {

	public CoursePrice create() throws CreateException;

	/**
	 * 
	 * @param pk is {@link CoursePrice#getPrimaryKey()}, not <code>null</code>;
	 * @return entity or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public CoursePrice findByPrimaryKey(Object pk);

	public Collection<CoursePrice> findAll() throws FinderException, IDORelationshipException;

	public Collection<CoursePrice> findAll(Date fromDate, Date toDate) throws FinderException, IDORelationshipException;

	public Collection<CoursePrice> findAll(CourseProviderArea area, CourseType cType) throws FinderException, IDORelationshipException;

	public Collection<CoursePrice> findAll(CourseProviderArea area, CourseType cType, Date fromDate, Date toDate) throws FinderException, IDORelationshipException;
}