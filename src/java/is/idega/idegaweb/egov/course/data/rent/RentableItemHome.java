package is.idega.idegaweb.egov.course.data.rent;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.IDOHome;

public interface RentableItemHome extends IDOHome {

	public RentableItem findByPrimaryKey(Object pk) throws FinderException;
	
	public Collection<? extends RentableItem> findAll() throws FinderException;

	public Collection<? extends RentableItem> findByType(String type) throws FinderException;
	
	public RentableItem create() throws CreateException;
	
	public Collection<? extends RentableItem> findByIds(String[] ids) throws FinderException;
}
