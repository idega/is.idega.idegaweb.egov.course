package is.idega.idegaweb.egov.course.data.rent;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public abstract class RentableItemHomeImpl extends IDOFactory implements RentableItemHome {

	private static final long serialVersionUID = -6776203614254829879L;

	@SuppressWarnings("unchecked")
	public Collection<? extends RentableItem> findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection<Integer> ids = ((RentableItemBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<? extends RentableItem> findByType(String type) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection<Integer> ids = ((RentableItemBMPBean) entity).ejbFindByType(type);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public RentableItem findByPrimaryKey(Object pk) throws FinderException {
		return (RentableItem) super.findByPrimaryKeyIDO(pk);
	}
}
