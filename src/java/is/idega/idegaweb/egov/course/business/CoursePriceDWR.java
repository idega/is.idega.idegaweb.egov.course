package is.idega.idegaweb.egov.course.business;

public class CoursePriceDWR {

	private String priceName;
	private String coursePK;
	private String price;
	private String preCarePrice;
	private String postCarePrice;

	public String getName() {
		return priceName;
	}

	public void setName(String name) {
		this.priceName = name;
	}

	public String getPk() {
		return coursePK;
	}

	public void setPk(String pk) {
		this.coursePK = pk;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPreCarePrice() {
		return preCarePrice;
	}

	public void setPreCarePrice(String price) {
		this.preCarePrice = price;
	}

	public String getPostCarePrice() {
		return postCarePrice;
	}

	public void setPostCarePrice(String price) {
		this.postCarePrice = price;
	}
}