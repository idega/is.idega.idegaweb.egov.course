package is.idega.idegaweb.egov.course.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

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

	public static final String	ENTITY_NAME = "COU_COURSE_PRICE",
								COLUMN_NAME = "NAME",
								COLUMN_SCHOOL_AREA = "SCHOOL_AREA",
								COLUMN_COURSE_TYPE = "COU_COURSE_TYPE_ID",
								COLUMN_VALID_FROM = "VALID_FROM",
								COLUMN_VALID_TO = "VALID_TO",
								COLUMN_VALID = "IS_VALID",
								COLUMN_NUMBER_OF_DAYS = "NUMBER_OF_DAYS",
								COLUMN_PRICE = "PRICE",
								COLUMN_PRE_CARE_PRICE = "PRE_CARE_PRICE",
								COLUMN_POST_CARE_PRICE = "POST_CARE_PRICE",
								COLUMN_SCHOOL_TYPE = "SCHOOL_TYPE",
								COLUMN_SCHOOL_SEASON = "SCHOOL_SEASON";

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

		addOneToOneRelationship(COLUMN_SCHOOL_TYPE, CourseProviderType.class);
		addManyToOneRelationship(COLUMN_SCHOOL_AREA, CourseProviderArea.class);
		addManyToOneRelationship(COLUMN_COURSE_TYPE, CourseType.class);
//		addManyToOneRelationship(COLUMN_SCHOOL_SEASON, SchoolSeason.class);
		getEntityDefinition().setBeanCachingActiveByDefault(true);
	}

	// Getters
	@Override
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public CourseProviderArea getSchoolArea() {
		return (CourseProviderArea) getColumnValue(COLUMN_SCHOOL_AREA);
	}

	@Override
	public CourseType getCourseType() {
		return (CourseType) getColumnValue(COLUMN_COURSE_TYPE);
	}

	@Override
	public Timestamp getValidFrom() {
		return getTimestampColumnValue(COLUMN_VALID_FROM);
	}

	@Override
	public Timestamp getValidTo() {
		return getTimestampColumnValue(COLUMN_VALID_TO);
	}

	@Override
	public int getNumberOfDays() {
		return getIntColumnValue(COLUMN_NUMBER_OF_DAYS);
	}

	@Override
	public int getPrice() {
		return getIntColumnValue(COLUMN_PRICE);
	}

	@Override
	public int getPreCarePrice() {
		return getIntColumnValue(COLUMN_PRE_CARE_PRICE);
	}

	@Override
	public int getPostCarePrice() {
		return getIntColumnValue(COLUMN_POST_CARE_PRICE);
	}

	// Setters
	@Override
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setSchoolArea(CourseProviderArea area) {
		setColumn(COLUMN_SCHOOL_AREA, area);
	}

	@Override
	public void setCourseType(CourseType type) {
		setColumn(COLUMN_COURSE_TYPE, type);
	}

	@Override
	public void setValidFrom(Timestamp stamp) {
		setColumn(COLUMN_VALID_FROM, stamp);
	}

	@Override
	public void setValidTo(Timestamp stamp) {
		setColumn(COLUMN_VALID_TO, stamp);
	}

	@Override
	public void setValid(boolean valid) {
		setColumn(COLUMN_VALID, valid);
	}

	@Override
	public void setNumberOfDays(int noDays) {
		setColumn(COLUMN_NUMBER_OF_DAYS, noDays);
	}

	@Override
	public void setPrice(int price) {
		setColumn(COLUMN_PRICE, price);
	}

	@Override
	public void setPreCarePrice(int price) {
		setColumn(COLUMN_PRE_CARE_PRICE, price);
	}

	@Override
	public void setPostCarePrice(int price) {
		setColumn(COLUMN_POST_CARE_PRICE, price);
	}

	// Finders
	public Collection<?> ejbFindAll() throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null, null, null);
	}

	public Collection<?> ejbFindAll(Date fromDate, Date toDate) throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null, fromDate, toDate);
	}

	public Collection<Object> ejbFindAll(CourseProviderArea area, CourseType cType) throws FinderException, IDORelationshipException {
		return ejbFindAll(area, cType, null, null);
	}

	public Collection<Object> ejbFindAll(CourseProviderArea area, CourseType cType, Date fromDate, Date toDate) throws FinderException, IDORelationshipException {
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

	public CourseProviderType getSchoolType() {
		Object type = getColumnValue(COLUMN_SCHOOL_TYPE);
		if (type instanceof CourseProviderType)
			return (CourseProviderType) type;

		return null;
	}

	public void setSchoolType(CourseProviderType schoolType) {
		setColumn(COLUMN_SCHOOL_TYPE, schoolType);
	}

//	@Override
//	public SchoolSeason getSchoolSeason() {
//		Object season = getColumnValue(COLUMN_SCHOOL_SEASON);
//		if (season instanceof SchoolSeason)
//			return (SchoolSeason) season;
//		return null;
//	}
//
//	@Override
//	public void setSchoolSeason(SchoolSeason season) {
//		setColumn(COLUMN_SCHOOL_SEASON, season);
//	}
}