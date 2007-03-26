package is.idega.idegaweb.egov.course.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class CourseBMPBean extends GenericEntity implements Course{

	private static final String TABLE_NAME		= "COU_COURSE";
	private static final String NAME			= "NAME";
	private static final String DESCRIPTION		= "DESCRIPTION";
	private static final String START_DATE		= "START_DATE";
	private static final String END_DATE		= "END_DATE";
	private static final String SCHOOL_TYPE_ID	= "SCH_SCHOOL_TYPE_ID";
	private static final String COURSE_TYPE_ID	= "COU_COURSE_TYPE_ID";
	private static final String ACCOUNTING_KEY	= "ACCOUNTING_KEY";
	private static final String BIRTHYEAR_FROM	= "BIRTHYEAR_FROM";
	private static final String BIRTHYEAR_TO	= "BIRTHYEAR_TO";
	private static final String MAX_PER			= "MAX_PER";
	private static final String COURSE_PRICE_PK	= "COU_COURSE_PRICE_ID";
	private static final String PROVIDER_PK		= "PROVIDER_ID";
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "name", String.class, 50);
		addAttribute(DESCRIPTION, "description", String.class);
		addAttribute(START_DATE, "Start date", Timestamp.class);
		addAttribute(END_DATE, "End date", Timestamp.class);
		addManyToOneRelationship(SCHOOL_TYPE_ID, SchoolType.class);
		addManyToOneRelationship(COURSE_TYPE_ID, CourseType.class);
		addAttribute(ACCOUNTING_KEY, "accounting_key", String.class, 30);
		addAttribute(BIRTHYEAR_FROM, "birthyear from", Integer.class);
		addAttribute(BIRTHYEAR_TO, "birthyear from", Integer.class);
		addAttribute(MAX_PER, "max", Integer.class);
		
		addManyToOneRelationship(COURSE_PRICE_PK, CoursePrice.class);
		addManyToOneRelationship(PROVIDER_PK, School.class);
	}

	public String getName() {
		return getStringColumnValue(NAME);
	}
	
	public void setName(String name) {
		setColumn(NAME, name);
	}

	public String getDescription() {
		return getStringColumnValue(DESCRIPTION);
	}
	
	public void setDescription(String description) {
		setColumn(DESCRIPTION, description);
	}
	
	public Timestamp getStartDate() {
		return getTimestampColumnValue(START_DATE);
	}
	
	public void setStartDate(Timestamp startDate) {
		setColumn(START_DATE, startDate);
	}
	
	public Timestamp getEndDate() {
		return getTimestampColumnValue(END_DATE);
	}
	
	public void setEndDate(Timestamp endDate) {
		setColumn(END_DATE, endDate);
	}
	
	public SchoolType getSchoolType() {
		return (SchoolType) getColumnValue(SCHOOL_TYPE_ID);
	}
	
	public void setSchoolTypePK(Object PK) {
		setColumn(SCHOOL_TYPE_ID, PK);
	}
	
	public void setSchoolType(SchoolType schoolType) {
		setColumn(SCHOOL_TYPE_ID, schoolType);
	}
	
	public CourseType getCourseType() {
		return (CourseType) getColumnValue(COURSE_TYPE_ID);
	}
	
	public void setCourseTypePK(Object PK) {
		setColumn(COURSE_TYPE_ID, PK);
	}
	
	public void setCourseType(CourseType courseType) {
		setColumn(COURSE_TYPE_ID, courseType);
	}
	
	public String getAccountingKey() {
		return getStringColumnValue(ACCOUNTING_KEY);
	}
	
	public void setAccountingKey(String key) {
		setColumn(ACCOUNTING_KEY, key);
	}
	
	public int getBirthyearFrom() {
		return getIntColumnValue(BIRTHYEAR_FROM);
	}
	
	public void setBirthyearFrom(int from) {
		setColumn(BIRTHYEAR_FROM, from);
	}
	
	public int getBirthyearTo() {
		return getIntColumnValue(BIRTHYEAR_TO);
	}
	
	public void setBirthyearTo(int to) {
		setColumn(BIRTHYEAR_TO, to);
	}
	
	public int getMax() {
		return getIntColumnValue(MAX_PER);
	}
	
	public void setMax(int max) {
		setColumn(MAX_PER, max);
	}
	
	public CoursePrice getPrice() {
		return (CoursePrice) getColumnValue(COURSE_PRICE_PK);
	}
	
	public void setPrice(CoursePrice price) {
		setColumn(COURSE_PRICE_PK, price);
	}
	
	public Collection ejbFindAll() throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null, -1);
	}
	
	public Collection ejbFindAll(Integer schoolTypeID, Integer courseTypeID, int birthYear) throws FinderException, IDORelationshipException {
		Table table = new Table(this);
		Table courseTypeTable = new Table(CourseType.class);
		Table schoolTypeTable = new Table(SchoolType.class);
		Column courseTypeOrder = new Column(courseTypeTable, "TYPE_ORDER");
		Column courseTypeId = new Column(courseTypeTable, "SCH_SCHOOL_TYPE_ID");
		Column schoolTypeOrder = new Column(schoolTypeTable, "type_order");
		Column schoolTypeId = new Column(courseTypeTable, "COU_COURSE_TYPE_ID");
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addOrder(table, NAME, true);
		query.addOrder(new Order(schoolTypeOrder, true));
		query.addOrder(new Order(courseTypeId, true));
		query.addOrder(new Order(courseTypeOrder, true));
		query.addOrder(new Order(schoolTypeId, true));
		query.addJoin(table, courseTypeTable);
		query.addJoin(table, schoolTypeTable);
		
		if (schoolTypeID != null) {
			query.addCriteria(new MatchCriteria(schoolTypeId, MatchCriteria.EQUALS, schoolTypeID));
		}
		
		if (courseTypeID != null) {
			query.addCriteria(new MatchCriteria(courseTypeId, MatchCriteria.EQUALS, courseTypeID));
		}
		
		if (birthYear > 0) {
			Column bFrom = new Column(table, BIRTHYEAR_FROM);
			Column yFrom = new Column(table, BIRTHYEAR_TO);
			query.addCriteria(new MatchCriteria(bFrom, MatchCriteria.LESSEQUAL, birthYear));
			query.addCriteria(new MatchCriteria(yFrom, MatchCriteria.GREATEREQUAL, birthYear));
		}
		
		return this.idoFindPKsByQuery(query);
	}
	
}
