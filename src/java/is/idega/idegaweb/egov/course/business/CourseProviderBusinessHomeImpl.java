package is.idega.idegaweb.egov.course.business;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.idega.business.IBOHomeImpl;

public class CourseProviderBusinessHomeImpl extends IBOHomeImpl implements
		CourseProviderBusinessHome {

	private static final long serialVersionUID = 6565158191349873370L;

	@Override
	public CourseProviderBusiness create() throws CreateException,
			RemoteException {
		return (CourseProviderBusiness) super.createIBO();
	}

	@Override
	protected Class getBeanInterfaceClass() {
		return CourseProviderBusiness.class;
	}

}
