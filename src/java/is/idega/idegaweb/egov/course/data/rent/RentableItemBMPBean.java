package is.idega.idegaweb.egov.course.data.rent;

import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CoursePriceHome;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolSeason;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.util.ListUtil;

public abstract class RentableItemBMPBean extends GenericEntity implements RentableItem {

	private static final long serialVersionUID = -8915541133992326249L;

	private static final String COLUMN_TYPE = "ITEM_TYPE",
								COLUMN_NAME = "ITEM_NAME",
								COLUMN_RENT_PRICE = "RENT_PRICE",
								COLUMN_QUANTITY = "QUANTITY",
								COLUMN_RENTED = "RENTED",
								COLUMN_PRICES = "PRICES";

	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());

		addAttribute(COLUMN_TYPE, "Item type", String.class);
		addAttribute(COLUMN_NAME, "Item name", String.class);
		addAttribute(COLUMN_RENT_PRICE, "Rent price", Double.class);
		addAttribute(COLUMN_QUANTITY, "Quantity", Integer.class);
		addAttribute(COLUMN_RENTED, "Rented", Integer.class);

		addManyToManyRelationShip(CoursePrice.class, COLUMN_PRICES);
	}

	@Override
	public String getType() {
		return getRealValue(COLUMN_TYPE);
	}

	@Override
	public void setType(String type) {
		setColumn(COLUMN_TYPE, type);
	}

	@Override
	public String getName() {
		return getRealValue(COLUMN_NAME);
	}

	@Override
	public void setName(String name) {
		setStringColumn(COLUMN_NAME, name);
	}

	@Override
	public Double getRentPrice(SchoolSeason season) {
		if (season == null) {
			return getRealValue(COLUMN_RENT_PRICE, 0.0);
		}

		CoursePrice price = getPrice(season);
		int priceInt = price == null ? -1 : price.getPrice();
		return priceInt <= 0 ? getRealValue(COLUMN_RENT_PRICE, 0.0) : Double.valueOf(priceInt);
	}

	private CoursePrice getPrice(SchoolSeason season) {
		Collection<CoursePrice> allPrices = getAllPrices();
		if (ListUtil.isEmpty(allPrices)) {
			return null;
		}

		for (CoursePrice price: allPrices) {
			SchoolSeason priceSeason = price.getSchoolSeason();
			if (priceSeason != null && priceSeason.getPrimaryKey().toString().equals(season.getPrimaryKey().toString())) {
				return price;
			}
		}

		return null;
	}

	@Override
	public void setRentPrice(SchoolSeason season, Double rentPrice) {
		if (season == null) {
			setColumn(COLUMN_RENT_PRICE, rentPrice);
			return;
		}

		CoursePrice price = getPrice(season);
		if (price == null) {
			try {
				CoursePriceHome priceHome = (CoursePriceHome) IDOLookup.getHome(CoursePrice.class);
				price = priceHome.create();
				price.setSchoolSeason(season);
				price.store();
				addPrice(price);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (price == null) {
			getLogger().warning("Unable to set price " + rentPrice + " for season " + season);
			return;
		}

		price.setPrice(rentPrice.intValue());
		price.store();
		store();
	}

	@Override
	public Integer getQuantity() {
		return getRealValue(COLUMN_QUANTITY, 0);
	}

	@Override
	public Integer getRentedAmount() {
		return getRealValue(COLUMN_RENTED, 0);
	}

	@Override
	public void setQuantity(Integer quantity) {
		setColumn(COLUMN_QUANTITY, quantity);
	}

	@Override
	public void setRentedAmount(Integer rented) {
		setColumn(COLUMN_RENTED, rented);
	}

	public Collection<Integer> ejbFindAll() throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		return this.idoFindPKsByQuery(query);
	}

	public Collection<Integer> ejbFindByType(String type) throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));

		query.addCriteria(new MatchCriteria(new Column(table, COLUMN_TYPE), MatchCriteria.EQUALS, type));

		return this.idoFindPKsByQuery(query);
	}

	public Collection<Integer> ejbFindByIds(String[] ids) throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));

		query.addCriteria(new InCriteria(new Column(table, getIDColumnName()), ids));

		return this.idoFindPKsByQuery(query);
	}

	@Override
	public void addPrice(CoursePrice price) throws IDOAddRelationshipException {
		this.idoAddTo(price);
	}

	@Override
	public Collection<CoursePrice> getAllPrices() {
		try {
			return super.idoGetRelatedEntities(CoursePrice.class);
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void removePrice(CoursePrice price) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(price);
	}

	@Override
	public void removeAllPrices() throws IDORemoveRelationshipException {
		Collection<CoursePrice> prices = getAllPrices();
		if (ListUtil.isEmpty(prices))
			return;

		for (CoursePrice price: prices) {
			removePrice(price);
		}

		store();
	}
}