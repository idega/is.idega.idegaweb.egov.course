package is.idega.idegaweb.egov.course.data.rent;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public abstract class RentableItemBMPBean extends GenericEntity implements RentableItem {

	private static final long serialVersionUID = -8915541133992326249L;

	private static final String COLUMN_TYPE = "ITEM_TYPE";
	private static final String COLUMN_NAME = "ITEM_NAME";
	private static final String COLUMN_RENT_PRICE = "RENT_PRICE";
	private static final String COLUMN_QUANTITY = "QUANTITY";
	private static final String COLUMN_RENTED = "RENTED";
	
	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addAttribute(COLUMN_TYPE, "Item type", String.class);
		addAttribute(COLUMN_NAME, "Item name", String.class);
		addAttribute(COLUMN_RENT_PRICE, "Rent price", Double.class);
		addAttribute(COLUMN_QUANTITY, "Quantity", Integer.class);
		addAttribute(COLUMN_RENTED, "Rented", Integer.class);
	}
	
	public String getType() {
		return getRealValue(COLUMN_TYPE);
	}
	
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
	
	public Double getRentPrice() {
		return getRealValue(COLUMN_RENT_PRICE, 0.0);
	}

	public void setRentPrice(Double rentPrice) {
		setColumn(COLUMN_RENT_PRICE, rentPrice);
	}
	
	public Integer getQuantity() {
		return getRealValue(COLUMN_QUANTITY, 0);
	}

	public Integer getRentedAmount() {
		return getRealValue(COLUMN_RENTED, 0);
	}

	public void setQuantity(Integer quantity) {
		setColumn(COLUMN_QUANTITY, quantity);
	}

	public void setRentedAmount(Integer rented) {
		setColumn(COLUMN_RENTED, rented);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Integer> ejbFindAll() throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		return this.idoFindPKsByQuery(query);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Integer> ejbFindByType(String type) throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		
		query.addCriteria(new MatchCriteria(new Column(table, COLUMN_TYPE), MatchCriteria.EQUALS, type));
		
		return this.idoFindPKsByQuery(query);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Integer> ejbFindByIds(String[] ids) throws FinderException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		
		query.addCriteria(new InCriteria(new Column(table, getIDColumnName()), ids));
		
		return this.idoFindPKsByQuery(query);
	}
}