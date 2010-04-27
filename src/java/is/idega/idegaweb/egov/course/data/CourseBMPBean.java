package is.idega.idegaweb.egov.course.data;

import is.idega.idegaweb.egov.course.data.rent.RentableItem;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

import javax.ejb.FinderException;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.CountColumn;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.MaxColumn;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.ListUtil;

public class CourseBMPBean extends GenericEntity implements Course {

	private static final long serialVersionUID = 8629779260677160057L;

	private static final String ENTITY_NAME = "COU_COURSE";

	private static final String COLUMN_COURSE_NUMBER = "COURSE_NUMBER";
	private static final String COLUMN_NAME = "NAME";
	private static final String COLUMN_USER = "USER_NAME";
	private static final String COLUMN_DESCRIPTION = "DESCRIPTION";

	private static final String COLUMN_COURSE_TYPE = "COU_COURSE_TYPE_ID";
	private static final String COLUMN_PROVIDER = "PROVIDER_ID";
	private static final String COLUMN_ACCOUNTING_KEY = "ACCOUNTING_KEY";
	private static final String COLUMN_COURSE_PRICE = "COU_COURSE_PRICE_ID";
	private static final String COLUMN_PRICE = "COURSE_PRICE";
	private static final String COLUMN_COST = "COURSE_COST";

	private static final String COLUMN_START_DATE = "START_DATE";
	private static final String COLUMN_END_DATE = "END_DATE";
	private static final String COLUMN_BIRTHYEAR_FROM = "BIRTHYEAR_FROM";
	private static final String COLUMN_BIRTHYEAR_TO = "BIRTHYEAR_TO";
	private static final String COLUMN_MAX_PARTICIPANTS = "MAX_PER";
	private static final String COLUMN_OPEN_FOR_REGISTRATION = "OPEN_FOR_REGISTRATION";

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_COURSE_NUMBER, "Course number", Integer.class);
		addAttribute(COLUMN_NAME, "Name", String.class, 50);
		addAttribute(COLUMN_USER, "User", String.class);
		addAttribute(COLUMN_DESCRIPTION, "Description", String.class);
		addAttribute(COLUMN_START_DATE, "Start date", Timestamp.class);
		addAttribute(COLUMN_END_DATE, "End date", Timestamp.class);
		addAttribute(COLUMN_ACCOUNTING_KEY, "Accounting key", String.class, 30);
		addAttribute(COLUMN_BIRTHYEAR_FROM, "Birthyear from", Integer.class);
		addAttribute(COLUMN_BIRTHYEAR_TO, "Birthyear from", Integer.class);
		addAttribute(COLUMN_MAX_PARTICIPANTS, "Max", Integer.class);
		addAttribute(COLUMN_PRICE, "Price", Float.class);
		addAttribute(COLUMN_COST, "Cost", Float.class);
		addAttribute(COLUMN_OPEN_FOR_REGISTRATION, "Open for registration", Boolean.class);

		addManyToOneRelationship(COLUMN_COURSE_TYPE, CourseType.class);
		addManyToOneRelationship(COLUMN_COURSE_PRICE, CoursePrice.class);
		addManyToOneRelationship(COLUMN_PROVIDER, School.class);
		
		Map<String, ? extends RentableItem> entities = null;
		try {
			entities = WebApplicationContextUtils.getWebApplicationContext(IWMainApplication.getDefaultIWMainApplication().getServletContext())
				.getBeansOfType(RentableItem.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (entities == null || entities.isEmpty()) {
			return;
		}
		for (RentableItem entity: entities.values()) {
			addManyToManyRelationShip(entity.getClass());
		}
	}

	// Getters
	@Override
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getUser() {
		return getStringColumnValue(COLUMN_USER);
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}

	public School getProvider() {
		return (School) getColumnValue(COLUMN_PROVIDER);
	}

	public CourseType getCourseType() {
		return (CourseType) getColumnValue(COLUMN_COURSE_TYPE);
	}

	public CoursePrice getPrice() {
		return (CoursePrice) getColumnValue(COLUMN_COURSE_PRICE);
	}

	public float getCoursePrice() {
		return getFloatColumnValue(COLUMN_PRICE, -1);
	}

	public float getCourseCost() {
		return getFloatColumnValue(COLUMN_COST, -1);
	}

	public String getAccountingKey() {
		return getStringColumnValue(COLUMN_ACCOUNTING_KEY);
	}

	public Timestamp getStartDate() {
		return getTimestampColumnValue(COLUMN_START_DATE);
	}

	public Timestamp getEndDate() {
		return getTimestampColumnValue(COLUMN_END_DATE);
	}

	public int getBirthyearFrom() {
		return getIntColumnValue(COLUMN_BIRTHYEAR_FROM);
	}

	public int getBirthyearTo() {
		return getIntColumnValue(COLUMN_BIRTHYEAR_TO);
	}

	public int getMax() {
		return getIntColumnValue(COLUMN_MAX_PARTICIPANTS);
	}

	public int getFreePlaces() {
		try {
			CourseChoiceHome home = (CourseChoiceHome) getIDOHome(CourseChoice.class);
			return getMax() - home.getCountByCourse(this);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (IDOException e) {
			e.printStackTrace();
		}

		return getMax();
	}
	
	public int getCourseNumber() {
		return getIntColumnValue(COLUMN_COURSE_NUMBER);
	}
	
	public boolean isOpenForRegistration() {
		return getBooleanColumnValue(COLUMN_OPEN_FOR_REGISTRATION, true);
	}

	// Setters
	@Override
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setUser(String user) {
		setColumn(COLUMN_USER, user);
	}

	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	public void setProvider(School provider) {
		setColumn(COLUMN_PROVIDER, provider);
	}

	public void setCourseType(CourseType courseType) {
		setColumn(COLUMN_COURSE_TYPE, courseType);
	}

	public void setPrice(CoursePrice price) {
		setColumn(COLUMN_COURSE_PRICE, price);
	}

	public void setCoursePrice(float price) {
		setColumn(COLUMN_PRICE, price);
	}

	public void setCourseCost(float cost) {
		setColumn(COLUMN_COST, cost);
	}

	public void setAccountingKey(String key) {
		setColumn(COLUMN_ACCOUNTING_KEY, key);
	}

	public void setStartDate(Timestamp startDate) {
		setColumn(COLUMN_START_DATE, startDate);
	}

	public void setEndDate(Timestamp endDate) {
		setColumn(COLUMN_END_DATE, endDate);
	}

	public void setBirthyearFrom(int from) {
		setColumn(COLUMN_BIRTHYEAR_FROM, from);
	}

	public void setBirthyearTo(int to) {
		setColumn(COLUMN_BIRTHYEAR_TO, to);
	}

	public void setMax(int max) {
		setColumn(COLUMN_MAX_PARTICIPANTS, max);
	}
	
	public void setCourseNumber(int number) {
		setColumn(COLUMN_COURSE_NUMBER, number);
	}
	
	public void setOpenForRegistration(boolean openForRegistration) {
		setColumn(COLUMN_OPEN_FOR_REGISTRATION, openForRegistration);
	}
	
	// Finders
	public Collection<Integer> ejbFindAll() throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null, null, -1, null, null);
	}

	public Collection<Integer> ejbFindAllByProvider(School provider) throws FinderException, IDORelationshipException {
		return ejbFindAll(provider.getPrimaryKey(), null, null, -1, null, null);
	}

	public Collection<Integer> ejbFindAllByBirthYear(int birthYear) throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null, null, birthYear, null, null);
	}

	@SuppressWarnings("unchecked")
	public Collection<Integer> ejbFindAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear, Date fromDate, Date toDate)
		throws FinderException, IDORelationshipException {
		
		Table table = new Table(this);
		Table courseTypeTable = new Table(CourseType.class);
		Column courseTypeId = new Column(courseTypeTable, "COU_COURSE_TYPE_ID");
		Column schoolTypeId = new Column(courseTypeTable, "SCH_SCHOOL_TYPE_ID");
		Column providerId = new Column(table, COLUMN_PROVIDER);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addJoin(table, courseTypeTable);

		if (providerPK != null) {
			query.addCriteria(new MatchCriteria(providerId, MatchCriteria.EQUALS, providerPK));
		}
		if (schoolTypePK != null) {
			query.addCriteria(new MatchCriteria(schoolTypeId, MatchCriteria.EQUALS, schoolTypePK));
		}
		if (courseTypePK != null) {
			query.addCriteria(new MatchCriteria(courseTypeId, MatchCriteria.EQUALS, courseTypePK));
		}

		if (birthYear > 0) {
			Column bFrom = new Column(table, COLUMN_BIRTHYEAR_FROM);
			Column yFrom = new Column(table, COLUMN_BIRTHYEAR_TO);
			query.addCriteria(new MatchCriteria(bFrom, MatchCriteria.LESSEQUAL, birthYear));
			query.addCriteria(new MatchCriteria(yFrom, MatchCriteria.GREATEREQUAL, birthYear));
		}
		if (fromDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_START_DATE), MatchCriteria.GREATEREQUAL, fromDate));
		}
		if (toDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_START_DATE), MatchCriteria.LESSEQUAL, toDate));
		}
		query.addOrder(table, COLUMN_START_DATE, true);
		query.addOrder(table, COLUMN_NAME, true);

		return this.idoFindPKsByQuery(query);
	}

	public Collection<Integer> ejbFindAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear) throws FinderException,
		IDORelationshipException {
		
		return ejbFindAll(providerPK, schoolTypePK, courseTypePK, birthYear, null, null);
	}

	@SuppressWarnings("unchecked")
	public Collection<Integer> ejbFindAll(Collection providers, Object schoolTypePK, Object courseTypePK) throws FinderException, IDORelationshipException {
		Table table = new Table(this);
		Table courseTypeTable = new Table(CourseType.class);
		Column courseTypeId = new Column(courseTypeTable, "COU_COURSE_TYPE_ID");
		Column schoolTypeId = new Column(courseTypeTable, "SCH_SCHOOL_TYPE_ID");
		Column providerId = new Column(table, COLUMN_PROVIDER);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addJoin(table, courseTypeTable);

		if (providers != null) {
			query.addCriteria(new InCriteria(providerId, providers));
		}
		if (schoolTypePK != null) {
			query.addCriteria(new MatchCriteria(schoolTypeId, MatchCriteria.EQUALS, schoolTypePK));
		}
		if (courseTypePK != null) {
			query.addCriteria(new MatchCriteria(courseTypeId, MatchCriteria.EQUALS, courseTypePK));
		}
		query.addOrder(table, COLUMN_START_DATE, true);
		query.addOrder(table, COLUMN_NAME, true);

		return this.idoFindPKsByQuery(query);
	}

	@SuppressWarnings("unchecked")
	public Collection<Integer> ejbFindAllByProviderAndSchoolTypeAndCourseType(School provider, SchoolType type, CourseType courseType, Date fromDate,
			Date toDate) throws FinderException {
		
		Table table = new Table(this);
		Table courseTypeTable = new Table(CourseType.class);
		Column courseTypeId = new Column(courseTypeTable, "COU_COURSE_TYPE_ID");
		Column schoolTypeId = new Column(courseTypeTable, "SCH_SCHOOL_TYPE_ID");
		Column providerId = new Column(table, COLUMN_PROVIDER);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		try {
			query.addJoin(table, courseTypeTable);
		}
		catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}

		if (provider != null) {
			query.addCriteria(new MatchCriteria(providerId, MatchCriteria.EQUALS, provider));
		}
		if (type != null) {
			query.addCriteria(new MatchCriteria(schoolTypeId, MatchCriteria.EQUALS, type));
		}
		if (courseType != null) {
			query.addCriteria(new MatchCriteria(courseTypeId, MatchCriteria.EQUALS, courseType));
		}
		if (fromDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_START_DATE), MatchCriteria.GREATEREQUAL, fromDate));
		}
		if (toDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_START_DATE), MatchCriteria.LESSEQUAL, toDate));
		}
		query.addOrder(table, COLUMN_START_DATE, true);
		query.addOrder(table, COLUMN_NAME, true);

		return this.idoFindPKsByQuery(query);
	}

	public int ejbHomeGetCountBySchoolTypeAndBirthYear(Object schoolTypePK, int birthYear, Date fromDate) throws IDOException {
		Table table = new Table(this);
		Table courseTypeTable = new Table(CourseType.class);
		Column schoolTypeId = new Column(courseTypeTable, "SCH_SCHOOL_TYPE_ID");

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));
		query.addJoin(table, courseTypeTable);

		if (schoolTypePK != null) {
			query.addCriteria(new MatchCriteria(schoolTypeId, MatchCriteria.EQUALS, schoolTypePK));
		}

		if (birthYear > 0) {
			Column bFrom = new Column(table, COLUMN_BIRTHYEAR_FROM);
			Column yFrom = new Column(table, COLUMN_BIRTHYEAR_TO);
			query.addCriteria(new MatchCriteria(bFrom, MatchCriteria.LESSEQUAL, birthYear));
			query.addCriteria(new MatchCriteria(yFrom, MatchCriteria.GREATEREQUAL, birthYear));
		}

		if (fromDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_START_DATE), MatchCriteria.GREATEREQUAL, fromDate));
		}

		return this.idoGetNumberOfRecords(query);
	}

	public int ejbHomeGetCountByCourseTypeAndBirthYear(Object courseTypePK, int birthYear, Date fromDate) throws IDOException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));

		if (courseTypePK != null) {
			query.addCriteria(new MatchCriteria(table, COLUMN_COURSE_TYPE, MatchCriteria.EQUALS, courseTypePK));
		}

		if (birthYear > 0) {
			Column bFrom = new Column(table, COLUMN_BIRTHYEAR_FROM);
			Column yFrom = new Column(table, COLUMN_BIRTHYEAR_TO);
			query.addCriteria(new MatchCriteria(bFrom, MatchCriteria.LESSEQUAL, birthYear));
			query.addCriteria(new MatchCriteria(yFrom, MatchCriteria.GREATEREQUAL, birthYear));
		}

		if (fromDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_START_DATE), MatchCriteria.GREATEREQUAL, fromDate));
		}

		return this.idoGetNumberOfRecords(query);
	}

	public int ejbHomeGetCountBySchoolAndBirthYear(Object schoolPK, int birthYear, Date fromDate) throws IDOException {
		return ejbHomeGetCountBySchoolAndCourseTypeAndBirthYear(schoolPK, null, birthYear, fromDate);
	}
	
	public int ejbHomeGetCountBySchoolAndCourseTypeAndBirthYear(Object schoolPK, Object courseTypePK, int birthYear, Date fromDate) throws IDOException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));

		if (schoolPK != null) {
			query.addCriteria(new MatchCriteria(table, COLUMN_PROVIDER, MatchCriteria.EQUALS, schoolPK));
		}
		if (courseTypePK != null) {
			query.addCriteria(new MatchCriteria(table, COLUMN_COURSE_TYPE, MatchCriteria.EQUALS, courseTypePK));
		}

		if (birthYear > 0) {
			Column bFrom = new Column(table, COLUMN_BIRTHYEAR_FROM);
			Column yFrom = new Column(table, COLUMN_BIRTHYEAR_TO);
			query.addCriteria(new MatchCriteria(bFrom, MatchCriteria.LESSEQUAL, birthYear));
			query.addCriteria(new MatchCriteria(yFrom, MatchCriteria.GREATEREQUAL, birthYear));
		}

		if (fromDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_START_DATE), MatchCriteria.GREATEREQUAL, fromDate));
		}

		return this.idoGetNumberOfRecords(query);
	}

	public int ejbHomeGetCountByProviderAndSchoolTypeAndCourseType(School provider, SchoolType type, CourseType courseType, Date fromDate, Date toDate)
		throws IDOException {
		
		Table table = new Table(this);
		Table courseTypeTable = new Table(CourseType.class);
		Column courseTypeId = new Column(courseTypeTable, "COU_COURSE_TYPE_ID");
		Column schoolTypeId = new Column(courseTypeTable, "SCH_SCHOOL_TYPE_ID");
		Column providerId = new Column(table, COLUMN_PROVIDER);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, getIDColumnName()));
		query.addJoin(table, courseTypeTable);

		if (provider != null) {
			query.addCriteria(new MatchCriteria(providerId, MatchCriteria.EQUALS, provider));
		}
		if (type != null) {
			query.addCriteria(new MatchCriteria(schoolTypeId, MatchCriteria.EQUALS, type));
		}
		if (courseType != null) {
			query.addCriteria(new MatchCriteria(courseTypeId, MatchCriteria.EQUALS, courseType));
		}
		if (fromDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_START_DATE), MatchCriteria.GREATEREQUAL, fromDate));
		}
		if (toDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_START_DATE), MatchCriteria.LESSEQUAL, toDate));
		}

		return this.idoGetNumberOfRecords(query);
	}
	
	public int ejbHomeGetHighestCourseNumber() throws IDOException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new MaxColumn(table, COLUMN_COURSE_NUMBER));

		return this.idoGetNumberOfRecords(query);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Integer> ejbFindAllWithNoCourseNumber() throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_COURSE_NUMBER)));
		
		return idoFindPKsByQuery(query);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Integer> ejbFindAllByTypes(Collection<String> typesIds) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new InCriteria(new Column(table, COLUMN_COURSE_TYPE), typesIds));
		
		return idoFindPKsByQuery(query);
	}

	@SuppressWarnings("unchecked")
	public Collection<? extends RentableItem> getRentableItems(Class<? extends RentableItem> itemType) {
		try {
			return super.idoGetRelatedEntities(itemType);
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addRentableItem(RentableItem item) throws IDOAddRelationshipException {
		super.idoAddTo(item);
	}
	
	public void removeRentableItem(RentableItem item) throws IDORemoveRelationshipException {
		super.idoRemoveFrom(item);
	}

	public void removeAllRentableItems(Class<? extends RentableItem> itemClass) throws IDORemoveRelationshipException {
		Collection<? extends RentableItem> items = getRentableItems(itemClass);
		if (ListUtil.isEmpty(items)) {
			return;
		}
		
		for (RentableItem item: items) {
			removeRentableItem(item);
		}
		
		store();
	}

	public void setRentableItems(Collection<? extends RentableItem> items) throws IDOAddRelationshipException {
		if (ListUtil.isEmpty(items)) {
			return;
		}
		
		Collection<? extends RentableItem> currentItems = getRentableItems(items.iterator().next().getClass());
		
		for (RentableItem item: items) {
			boolean canAdd = true;
			if (!ListUtil.isEmpty(currentItems) && currentItems.contains(item)) {
				canAdd = false;
			}
			if (canAdd) {
				addRentableItem(item);
			}
		}
		
		store();
	}
}