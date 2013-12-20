package is.idega.idegaweb.egov.course.data;


import java.sql.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.process.data.CaseStatus;
import com.idega.data.IDOHome;

public interface CourseApplicationHome extends IDOHome {

	public CourseApplication create() throws CreateException;

	public CourseApplication findByPrimaryKey(Object pk) throws FinderException;

	public Collection<CourseApplication> findAll(CaseStatus caseStatus, Date fromDate, Date toDate) throws FinderException;

	public Collection<CourseApplication> findAll() throws FinderException;
}