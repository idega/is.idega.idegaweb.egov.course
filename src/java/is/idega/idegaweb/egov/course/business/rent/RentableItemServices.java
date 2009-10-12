package is.idega.idegaweb.egov.course.business.rent;

import is.idega.idegaweb.egov.course.data.rent.RentableItem;

import java.util.Collection;

public interface RentableItemServices {

	public boolean deleteItem(Class<? extends RentableItem> entityClass, Object primaryKey);
	
	public boolean editItem(Class<? extends RentableItem> entityClass, Object primaryKey, String name, Double price, Integer quantity, Integer rented);
	
	public RentableItem createItem(Class<? extends RentableItem> entityClass, String type, String name, Double price, Integer quantity, Integer rented);
	
	public RentableItem getItem(Class<? extends RentableItem> entityClass, Object primaryKey);
	
	public Collection<? extends RentableItem> getItemsByType(Class<? extends RentableItem> entityClass, String type);
}
