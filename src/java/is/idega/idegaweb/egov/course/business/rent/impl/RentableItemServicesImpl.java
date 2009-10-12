package is.idega.idegaweb.egov.course.business.rent.impl;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import is.idega.idegaweb.egov.course.business.rent.RentableItemServices;
import is.idega.idegaweb.egov.course.data.rent.RentableItem;
import is.idega.idegaweb.egov.course.data.rent.RentableItemHome;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.core.business.DefaultSpringBean;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class RentableItemServicesImpl extends DefaultSpringBean implements RentableItemServices {

	private static final Logger LOGGER = Logger.getLogger(RentableItemServicesImpl.class.getName());
	
	public RentableItem createItem(Class<? extends RentableItem> entityClass, String type, String name, Double price, Integer quantity, Integer rented) {
		RentableItem item = null;
		try {
			item = getRentableItemHome(entityClass).create();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Some error occurred while creating " + entityClass, e);
		}
		
		item.setType(type);
		
		if (editItem(item, name, price, quantity, rented)) {
			return item;
		}
		
		return null;
	}

	public boolean deleteItem(Class<? extends RentableItem> entityClass, Object primaryKey) {
		if (primaryKey == null) {
			LOGGER.warning("Primary key is not provided");
			return Boolean.FALSE;
		}
		
		RentableItem item = getItem(entityClass, primaryKey);
		if (item == null) {
			return Boolean.FALSE;
		}
		
		try {
			item.remove();
			return Boolean.TRUE;
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Unable to remove item: " + item.getName() + " ("+primaryKey+")", e);
		}
		
		return Boolean.FALSE;
	}

	public boolean editItem(Class<? extends RentableItem> entityClass, Object primaryKey, String name, Double price, Integer quantity, Integer rented) {
		try {
			return editItem(getItem(entityClass, primaryKey), name, price, quantity, rented);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Some error occurred while editing item: " + primaryKey, e);
		}
		return Boolean.FALSE;
	}
	
	public boolean editItem(RentableItem item, String name, Double price, Integer quantity, Integer rented) {
		if (item == null) {
			return Boolean.FALSE;
		}
		
		item.setName(name);
		item.setRentPrice(price);
		item.setQuantity(quantity);
		item.setRentedAmount(rented);
		item.store();
		
		return Boolean.TRUE;
	}

	public RentableItem getItem(Class<? extends RentableItem> entityClass, Object primaryKey) {
		if (primaryKey == null) {
			LOGGER.warning("Primary key is not provided!");
			return null;
		}
		
		try {
			return getRentableItemHome(entityClass).findByPrimaryKey(primaryKey);
		} catch (FinderException e) {
			LOGGER.warning("Nothing found by provided key: " + primaryKey);
		}
		
		return null;
	}

	public Collection<? extends RentableItem> getItemsByType(Class<? extends RentableItem> entityClass, String type) {
		try {
			return getRentableItemHome(entityClass).findByType(type);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Some error occurred while trying to search by type: " + type, e);
		}
		return null;
	}
	
	private RentableItemHome getRentableItemHome(Class<? extends RentableItem> entityClass) {
		return getHomeForEntity(entityClass);
	}
}