package is.idega.idegaweb.egov.course.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;

public interface CourseCertificateTypeHome extends IDOHome {

	public CourseCertificateType create() throws CreateException;

	public CourseCertificateType findByPrimaryKey(Object pk) throws FinderException;

	public CourseCertificateType findByPrimaryKey(int id) throws FinderException;

	public CourseCertificateType findByPrimaryKeyLegacy(int id) throws SQLException;

	public CourseCertificateType findByType(Integer type);

	public Collection<CourseCertificateType> findAllTypes() throws FinderException;

}