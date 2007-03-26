package is.idega.idegaweb.egov.course.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class CoursePriceBMPBean extends GenericEntity implements CoursePrice {

	public static final String TABLE_NAME		= "COU_COURSE_PRICE";
	public static final String NAME				= "NAME";
	public static final String SCHOOL_TYPE_ID	= "SCH_SCHOOL_TYPE_ID";
	public static final String COURSE_TYPE_ID	= "COU_COURSE_TYPE_ID";
	public static final String VALID_FROM		= "VALID_FROM";
	public static final String VALID_TO			= "VALID_TO";
	public static final String NUMBER_OF_DAYS	= "NUMBER_OF_DAYS";
	public static final String PRICE			= "PRICE";
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "name", String.class, 50);
		addAttribute(VALID_FROM, "valid from", Timestamp.class);
		addAttribute(VALID_TO, "valid to", Timestamp.class);
		addAttribute(NUMBER_OF_DAYS, "number of days", Integer.class);
		addAttribute(PRICE, "price", Integer.class);
		
		addManyToOneRelationship(SCHOOL_TYPE_ID, SchoolType.class);
		addManyToOneRelationship(COURSE_TYPE_ID, CourseType.class);
	}

	public String getName() {
		return getStringColumnValue(NAME);
	}

	public void setName(String name) {
		setColumn(NAME, name);
	}
	
	public SchoolType getSchoolTypeID() {
		return (SchoolType) getColumnValue(SCHOOL_TYPE_ID);
	}
	
	public void setSchoolTypeID(Integer pk) {
		setColumn(SCHOOL_TYPE_ID, pk);
	}

	public CourseType getCourseTypeID() {
		return (CourseType) getColumnValue(COURSE_TYPE_ID);
	}
	
	public void setCourseTypeID(Integer pk) {
		setColumn(COURSE_TYPE_ID, pk);
	}

	public Timestamp getValidFrom() {
		return getTimestampColumnValue(VALID_FROM);
	}

	public void setValidFrom(Timestamp stamp) {
		setColumn(VALID_FROM, stamp);
	}

	public Timestamp getValidTo() {
		return getTimestampColumnValue(VALID_TO);
	}

	public void setValidTo(Timestamp stamp) {
		setColumn(VALID_TO, stamp);
	}
	
	public int getNumberOfDays() {
		return getIntColumnValue(NUMBER_OF_DAYS);
	}
	
	public void setNumberOfDays(int noDays) {
		setColumn(NUMBER_OF_DAYS, noDays);
	}

	public int getPrice() {
		return getIntColumnValue(PRICE);
	}
	
	public void setPrice(int price) {
		setColumn(PRICE, price);
	}
	
	public Collection ejbFindAll() throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null);
	}
	
	public Collection ejbFindAll(SchoolType sType, CourseType cType) throws FinderException, IDORelationshipException {
		Table table = new Table(this);
		Table courseTypeTable = new Table(CourseType.class);
		Table schoolTypeTable = new Table(SchoolType.class);
		
		Column courseTypeOrder = new Column(courseTypeTable, "TYPE_ORDER");
		Column courseTypeId = new Column(courseTypeTable, "SCH_SCHOOL_TYPE_ID");
		Column schoolTypeOrder = new Column(schoolTypeTable, "type_order");
		Column schoolTypeId = new Column(courseTypeTable, "COU_COURSE_TYPE_ID");
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		
		if (sType != null) {
			query.addCriteria(new MatchCriteria(schoolTypeId, MatchCriteria.EQUALS, sType));
		}
		if (cType != null) {
			query.addCriteria(new MatchCriteria(courseTypeId, MatchCriteria.EQUALS, cType));
		}
		
		query.addOrder(new Order(schoolTypeOrder, true));
		query.addOrder(new Order(courseTypeId, true));
		query.addOrder(new Order(courseTypeOrder, true));
		query.addOrder(new Order(schoolTypeId, true));
		query.addJoin(table, courseTypeTable);
		query.addJoin(table, schoolTypeTable);
//		System.out.println(query);
		return this.idoFindPKsByQuery(query);
	}

}
