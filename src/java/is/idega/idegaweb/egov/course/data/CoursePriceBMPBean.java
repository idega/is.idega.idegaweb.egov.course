package is.idega.idegaweb.egov.course.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class CoursePriceBMPBean extends GenericEntity implements CoursePrice {

	private static final long serialVersionUID = 71798577475166280L;
	
	public static final String ENTITY_NAME = "COU_COURSE_PRICE";
	public static final String COLUMN_NAME = "NAME";
	public static final String COLUMN_SCHOOL_AREA = "SCHOOL_AREA";
	public static final String COLUMN_COURSE_TYPE = "COU_COURSE_TYPE_ID";
	public static final String COLUMN_VALID_FROM = "VALID_FROM";
	public static final String COLUMN_VALID_TO = "VALID_TO";
	public static final String COLUMN_VALID = "IS_VALID";
	public static final String COLUMN_NUMBER_OF_DAYS = "NUMBER_OF_DAYS";
	public static final String COLUMN_PRICE = "PRICE";
	public static final String COLUMN_PRE_CARE_PRICE = "PRE_CARE_PRICE";
	public static final String COLUMN_POST_CARE_PRICE = "POST_CARE_PRICE",
								COLUMN_SCHOOL_TYPE = "SCHOOL_TYPE";

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class, 50);
		addAttribute(COLUMN_VALID_FROM, "Valid from", Timestamp.class);
		addAttribute(COLUMN_VALID_TO, "Valid to", Timestamp.class);
		addAttribute(COLUMN_VALID, "Is valid", Boolean.class);
		addAttribute(COLUMN_NUMBER_OF_DAYS, "Number of days", Integer.class);
		addAttribute(COLUMN_PRICE, "Price", Integer.class);
		addAttribute(COLUMN_PRE_CARE_PRICE, "Pre care price", Integer.class);
		addAttribute(COLUMN_POST_CARE_PRICE, "Post care price", Integer.class);

		addOneToOneRelationship(COLUMN_SCHOOL_TYPE, SchoolType.class);
		addManyToOneRelationship(COLUMN_SCHOOL_AREA, SchoolArea.class);
		addManyToOneRelationship(COLUMN_COURSE_TYPE, CourseType.class);
		getEntityDefinition().setBeanCachingActiveByDefault(true);
	}

	// Getters
	@Override
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public SchoolArea getSchoolArea() {
		return (SchoolArea) getColumnValue(COLUMN_SCHOOL_AREA);
	}

	public CourseType getCourseType() {
		return (CourseType) getColumnValue(COLUMN_COURSE_TYPE);
	}

	public Timestamp getValidFrom() {
		return getTimestampColumnValue(COLUMN_VALID_FROM);
	}

	public Timestamp getValidTo() {
		return getTimestampColumnValue(COLUMN_VALID_TO);
	}

	public int getNumberOfDays() {
		return getIntColumnValue(COLUMN_NUMBER_OF_DAYS);
	}

	public int getPrice() {
		return getIntColumnValue(COLUMN_PRICE);
	}

	public int getPreCarePrice() {
		return getIntColumnValue(COLUMN_PRE_CARE_PRICE);
	}

	public int getPostCarePrice() {
		return getIntColumnValue(COLUMN_POST_CARE_PRICE);
	}

	// Setters
	@Override
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setSchoolArea(SchoolArea area) {
		setColumn(COLUMN_SCHOOL_AREA, area);
	}

	public void setCourseType(CourseType type) {
		setColumn(COLUMN_COURSE_TYPE, type);
	}

	public void setValidFrom(Timestamp stamp) {
		setColumn(COLUMN_VALID_FROM, stamp);
	}

	public void setValidTo(Timestamp stamp) {
		setColumn(COLUMN_VALID_TO, stamp);
	}

	public void setValid(boolean valid) {
		setColumn(COLUMN_VALID, valid);
	}

	public void setNumberOfDays(int noDays) {
		setColumn(COLUMN_NUMBER_OF_DAYS, noDays);
	}

	public void setPrice(int price) {
		setColumn(COLUMN_PRICE, price);
	}

	public void setPreCarePrice(int price) {
		setColumn(COLUMN_PRE_CARE_PRICE, price);
	}

	public void setPostCarePrice(int price) {
		setColumn(COLUMN_POST_CARE_PRICE, price);
	}

	// Finders
	public Collection ejbFindAll() throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null, null, null);
	}

	public Collection ejbFindAll(Date fromDate, Date toDate) throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null, fromDate, toDate);
	}

	public Collection ejbFindAll(SchoolArea area, CourseType cType) throws FinderException, IDORelationshipException {
		return ejbFindAll(area, cType, null, null);
	}

	public Collection ejbFindAll(SchoolArea area, CourseType cType, Date fromDate, Date toDate) throws FinderException, IDORelationshipException {
		Table table = new Table(this);
		Table courseTypeTable = new Table(CourseType.class);

		Column courseTypeOrder = new Column(courseTypeTable, "TYPE_ORDER");
		Column courseTypeId;
		try {
			courseTypeId = new Column(courseTypeTable, courseTypeTable.getPrimaryKeyColumnName());
		}
		catch (IDOCompositePrimaryKeyException e) {
			throw new FinderException(e.getMessage());
		}

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));

		if (area != null) {
			OR or = new OR(new MatchCriteria(table.getColumn(COLUMN_SCHOOL_AREA), MatchCriteria.EQUALS, area), new MatchCriteria(table.getColumn(COLUMN_SCHOOL_AREA)));
			query.addCriteria(or);
		}
		if (cType != null) {
			query.addCriteria(new MatchCriteria(courseTypeId, MatchCriteria.EQUALS, cType));
		}
		if (fromDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_VALID_FROM), MatchCriteria.GREATEREQUAL, fromDate));
		}
		if (toDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_VALID_TO), MatchCriteria.LESSEQUAL, toDate));
		}
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		query.addOrder(new Order(courseTypeId, true));
		query.addOrder(new Order(courseTypeOrder, true));
		query.addJoin(table, courseTypeTable);
		return this.idoFindPKsByQuery(query);
	}

	@Override
	public SchoolType getSchoolType() {
		Object type = getColumnValue(COLUMN_SCHOOL_TYPE);
		if (type instanceof SchoolType)
			return (SchoolType) type;
		return null;
	}

	@Override
	public void setSchoolType(SchoolType schoolType) {
		setColumn(COLUMN_SCHOOL_TYPE, schoolType);
	}
}