package is.idega.idegaweb.egov.course.data.rent;

import com.idega.data.IDOEntity;

public interface RentableItem extends IDOEntity {

	public String getType();
	public void setType(String type);
	
	public String getName();
	public void setName(String name);
	
	public Double getRentPrice();
	public void setRentPrice(Double rentPrice);
	
	public Integer getQuantity();
	public void setQuantity(Integer quantity);
	
	public Integer getRentedAmount();
	public void setRentedAmount(Integer rented);
	
}
