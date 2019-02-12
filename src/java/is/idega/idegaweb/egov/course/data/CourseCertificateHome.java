package is.idega.idegaweb.egov.course.data;


import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;
import com.idega.user.data.User;

public interface CourseCertificateHome extends IDOHome {

	public CourseCertificate create() throws CreateException;

	public CourseCertificate findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllCertificatesByUser(User user) throws FinderException;

	public Collection findAllCertificatesByUserAndCourse(User user, Course course) throws FinderException;

	public CourseCertificate findByUserAndCourse(User user, Course course) throws FinderException;

	public Collection<CourseCertificate> findCertificatesByUsersAndValidityAndType(List usersIds, boolean onlyValidCertificates, String certificateTypeId) throws FinderException;

	public CourseCertificate findHighestNumber() throws FinderException;
}