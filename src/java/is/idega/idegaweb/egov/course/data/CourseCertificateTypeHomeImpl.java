package is.idega.idegaweb.egov.course.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class CourseCertificateTypeHomeImpl extends IDOFactory implements CourseCertificateTypeHome {

	private static final long serialVersionUID = -8562089159663650487L;

	protected Class getEntityInterfaceClass() {
		return CourseCertificateType.class;
	}

	public CourseCertificateType create() throws CreateException {
		return (CourseCertificateType) super.idoCreate(getEntityInterfaceClass());
	}

	public CourseCertificateType findByPrimaryKey(Object pk) throws FinderException {
		return (CourseCertificateType) super.idoFindByPrimaryKey(Integer.valueOf(pk.toString()).intValue());
	}

	public CourseCertificateType findByPrimaryKey(int id) throws FinderException {
		return (CourseCertificateType) super.idoFindByPrimaryKey(id);
	}

	public CourseCertificateType findByPrimaryKeyLegacy(int id) throws SQLException {
		try {
			return findByPrimaryKey(id);
		}
		catch (FinderException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
	}

	public CourseCertificateType findByType(Integer type) {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		return ((CourseCertificateTypeBMPBean) entity).findByType(type);
	}

	public Collection findAllTypes() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CourseCertificateTypeBMPBean) entity).ejbFindAllTypes();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}