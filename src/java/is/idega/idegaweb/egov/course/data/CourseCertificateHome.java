package is.idega.idegaweb.egov.course.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.List;
import com.idega.user.data.User;

public interface CourseCertificateHome extends IDOHome {

	public CourseCertificate create() throws CreateException;

	public CourseCertificate findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllByUser(User user) throws FinderException;

	public Collection findAllByUserAndCourse(User user, Course course) throws FinderException;

	public CourseCertificate findByUserAndCourse(User user, Course course) throws FinderException;

	public Collection findByUsersAndValidityAndType(List usersIds, boolean onlyValidCertificates, String certificateTypeId) throws FinderException;

	public CourseCertificate findHighestNumberByType(CourseCertificateType type) throws FinderException;
}