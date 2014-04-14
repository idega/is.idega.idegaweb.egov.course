package is.idega.idegaweb.egov.course.data.rent;

import is.idega.idegaweb.egov.course.data.CoursePrice;

import java.util.Collection;

import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORemoveRelationshipException;

public interface RentableItem extends IDOEntity {

	public String getType();
	public void setType(String type);

	public String getName();
	public void setName(String name);

//	public Double getRentPrice(SchoolSeason season);
//	public void setRentPrice(SchoolSeason season, Double rentPrice);

	public Integer getQuantity();
	public void setQuantity(Integer quantity);

	public Integer getRentedAmount();
	public void setRentedAmount(Integer rented);

	public void addPrice(CoursePrice price) throws IDOAddRelationshipException;
	public Collection<CoursePrice> getAllPrices();
	public void removePrice(CoursePrice price) throws IDORemoveRelationshipException;
	public void removeAllPrices() throws IDORemoveRelationshipException;
}