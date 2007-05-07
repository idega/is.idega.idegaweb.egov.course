package is.idega.idegaweb.egov.course.data;


import java.util.Collection;
import com.idega.block.process.data.CaseStatus;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.sql.Date;

public interface CourseApplicationHome extends IDOHome {
	public CourseApplication create() throws CreateException;

	public CourseApplication findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll(CaseStatus caseStatus, Date fromDate, Date toDate)
			throws FinderException;

	public Collection findAll() throws FinderException;
}