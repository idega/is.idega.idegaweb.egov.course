package is.idega.idegaweb.egov.course.business.rent.impl;

import is.idega.idegaweb.egov.course.business.rent.RentableItemServices;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CoursePriceHome;
import is.idega.idegaweb.egov.course.data.rent.RentableItem;
import is.idega.idegaweb.egov.course.data.rent.RentableItemHome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.school.data.SchoolSeason;
import com.idega.core.business.DefaultSpringBean;
import com.idega.data.IDOLookup;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.datastructures.map.MapUtil;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class RentableItemServicesImpl extends DefaultSpringBean implements RentableItemServices {

	private static final Logger LOGGER = Logger.getLogger(RentableItemServicesImpl.class.getName());

	@Override
	public RentableItem createItem(
			Class<? extends RentableItem> entityClass,
			String type,
			String name,
			Map<SchoolSeason, Double> prices,
			Integer quantity,
			Integer rented
	) {
		RentableItem item = null;
		try {
			item = getRentableItemHome(entityClass).create();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Some error occurred while creating " + entityClass, e);
		}

		item.setType(type);

		if (editItem(item, name, prices, quantity, rented)) {
			return item;
		}

		return null;
	}

	@Override
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

	@Override
	public boolean editItem(
			Class<? extends RentableItem> entityClass,
			Object primaryKey,
			String name,
			Map<SchoolSeason, Double> prices,
			Integer quantity,
			Integer rented
	) {
		try {
			return editItem(getItem(entityClass, primaryKey), name, prices, quantity, rented);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Some error occurred while editing item: " + primaryKey, e);
		}
		return Boolean.FALSE;
	}

	public boolean editItem(RentableItem item, String name, Map<SchoolSeason, Double> prices, Integer quantity, Integer rented) {
		if (item == null) {
			return Boolean.FALSE;
		}

		try {
			item.setName(name);
			item.setQuantity(quantity);
			item.setRentedAmount(rented);
			item.store();

			item.removeAllPrices();
			if (!MapUtil.isEmpty(prices)) {
				CoursePriceHome priceHome = (CoursePriceHome) IDOLookup.getHome(CoursePrice.class);
				for (SchoolSeason season: prices.keySet()) {
					CoursePrice price = priceHome.create();
					price.setSchoolSeason(season);
					Double value = prices.get(season);
					price.setPrice(value.intValue());
					price.store();

					item.addPrice(price);
				}
			}

			item.store();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return Boolean.TRUE;
	}

	@Override
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

	@Override
	public <I extends RentableItem> List<I> getItemsByType(Class<I> entityClass, String type) {
		try {
			RentableItemHome home = getRentableItemHome(entityClass);
			Collection<I> items = home.findByType(type);
			return items == null ? null : new ArrayList<I>(items);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Some error occurred while trying to search by type: " + type, e);
		}
		return null;
	}

	private <I extends RentableItem> RentableItemHome getRentableItemHome(Class<I> entityClass) {
		return getHomeForEntity(entityClass);
	}

	@Override
	public <I extends RentableItem> Map<String, List<I>> getGroupedRentableItems(Collection<I> allItems) {
		if (ListUtil.isEmpty(allItems)) {
			return Collections.emptyMap();
		}

		Map<String, List<I>> groupedItems = new HashMap<String, List<I>>();
		for (I item: allItems) {
			if (item == null || StringUtil.isEmpty(item.getName())) {
				continue;
			}

			Collection<CoursePrice> prices = item.getAllPrices();
			if (ListUtil.isEmpty(prices)) {
				continue;
			}

			for (CoursePrice price: prices) {
				SchoolSeason season = price.getSchoolSeason();
				if (season == null) {
					continue;
				}

				String seasonId = season.getPrimaryKey().toString();
				List<I> items = groupedItems.get(seasonId);
				if (items == null) {
					items = new ArrayList<I>();
					groupedItems.put(seasonId, items);
				}
				if (!items.contains(item)) {
					items.add(item);
				}
			}
		}

		return groupedItems;
	}
}