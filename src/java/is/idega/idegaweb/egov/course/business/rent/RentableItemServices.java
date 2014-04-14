package is.idega.idegaweb.egov.course.business.rent;

import is.idega.idegaweb.egov.course.data.rent.RentableItem;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.idega.block.school.data.SchoolSeason;

public interface RentableItemServices {

	public boolean deleteItem(Class<? extends RentableItem> entityClass, Object primaryKey);

	public boolean editItem(
			Class<? extends RentableItem> entityClass,
			Object primaryKey,
			String name,
			Map<SchoolSeason, Double> prices,
			Integer quantity,
			Integer rented
	);

	public RentableItem createItem(
			Class<? extends RentableItem> entityClass,
			String type,
			String name,
			Map<SchoolSeason, Double> prices,
			Integer quantity,
			Integer rented
	);

	public RentableItem getItem(Class<? extends RentableItem> entityClass, Object primaryKey);

	public <I extends RentableItem> List<I> getItemsByType(Class<I> entityClass, String type);

	public <I extends RentableItem> Map<String, List<I>> getGroupedRentableItems(Collection<I> allItems);

}