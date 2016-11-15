package is.idega.idegaweb.egov.course.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.FinderException;

import org.apache.commons.lang.math.NumberUtils;
import org.hsqldb.lib.StringUtil;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.SimpleQuerier;
import com.idega.data.query.AND;
import com.idega.data.query.Column;
import com.idega.data.query.CountColumn;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.MaxColumn;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

import is.idega.idegaweb.egov.course.data.rent.RentableItem;

public class CourseBMPBean extends GenericEntity implements Course {

	private static final long serialVersionUID = 8629779260677160057L;

	public static final String ENTITY_NAME = "COU_COURSE";

	public static final String USERS_LIST = ENTITY_NAME + "_USERS";
	public static final String PARTICIPANTS_LIST = ENTITY_NAME + "_PARTICIPANTS";

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
	private static final String COLUMN_PRE_CARE = "HAS_PRE_CARE";
	private static final String COLUMN_POST_CARE = "HAS_POST_CARE";

	private static final String COLUMN_START_DATE = "START_DATE";
	private static final String COLUMN_END_DATE = "END_DATE";
	private static final String COLUMN_REGISTRATION_START = "REGISTRATION_START";
	private static final String COLUMN_REGISTRATION_END = "REGISTRATION_END";
	private static final String COLUMN_BIRTHYEAR_FROM = "BIRTHYEAR_FROM";
	private static final String COLUMN_BIRTHYEAR_TO = "BIRTHYEAR_TO";
	private static final String COLUMN_MAX_PARTICIPANTS = "MAX_PER";
	private static final String COLUMN_OPEN_FOR_REGISTRATION = "OPEN_FOR_REGISTRATION",
								COLUMN_COURSE_PRICES = "COURSE_PRICES",
								COLUMN_COURSE_SEASONS = "COURSE_SEASONS";
	protected final static String COLUMN_GROUP = "GROUP_ID";
	protected final static String COLUMN_COURSE_TEMPLATE = "TEMPLATE_ID";
	private static final String COLUMN_GENDER = "GENDER";
	private static final String COLUMN_COURSE_TYPE_STR = "COURSE_TYPE";

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_COURSE_NUMBER, "Course number", Integer.class);
		addAttribute(COLUMN_NAME, "Name", String.class, 250);
		addAttribute(COLUMN_USER, "User", String.class);
		addAttribute(COLUMN_DESCRIPTION, "Description", String.class);
		addAttribute(COLUMN_START_DATE, "Start date", Timestamp.class);
		addAttribute(COLUMN_END_DATE, "End date", Timestamp.class);
		addAttribute(COLUMN_REGISTRATION_START, "Registration start", Timestamp.class);
		addAttribute(COLUMN_REGISTRATION_END, "Registration end", Timestamp.class);
		addAttribute(COLUMN_ACCOUNTING_KEY, "Accounting key", String.class, 30);
		addAttribute(COLUMN_BIRTHYEAR_FROM, "Birthyear from", Integer.class);
		addAttribute(COLUMN_BIRTHYEAR_TO, "Birthyear from", Integer.class);
		addAttribute(COLUMN_MAX_PARTICIPANTS, "Max", Integer.class);
		addAttribute(COLUMN_PRICE, "Price", Float.class);
		addAttribute(COLUMN_COST, "Cost", Float.class);
		addAttribute(COLUMN_OPEN_FOR_REGISTRATION, "Open for registration", Boolean.class);
		addAttribute(COLUMN_PRE_CARE, "Has pre care", Boolean.class);
		addAttribute(COLUMN_POST_CARE, "Has post care", Boolean.class);
		addAttribute(COLUMN_GENDER, "Gender", String.class);
		addAttribute(COLUMN_COURSE_TYPE_STR, "Course type", String.class);

		addManyToOneRelationship(COLUMN_COURSE_TEMPLATE, Course.class);
		addManyToOneRelationship(COLUMN_COURSE_TYPE, CourseType.class);
		addManyToOneRelationship(COLUMN_COURSE_PRICE, CoursePrice.class);
		addManyToOneRelationship(COLUMN_PROVIDER, School.class);
		addManyToOneRelationship(COLUMN_GROUP, Group.class);
		addManyToManyRelationShip(CoursePrice.class, COLUMN_COURSE_PRICES);
		addManyToManyRelationShip(SchoolSeason.class, COLUMN_COURSE_SEASONS);
		addManyToManyRelationShip(User.class, USERS_LIST);
		addManyToManyRelationShip(User.class, PARTICIPANTS_LIST);

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

	@Override
	public String getUser() {
		return getStringColumnValue(COLUMN_USER);
	}

	@Override
	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}

	@Override
	public School getProvider() {
		return (School) getColumnValue(COLUMN_PROVIDER);
	}

	@Override
	public CourseType getCourseType() {
		return (CourseType) getColumnValue(COLUMN_COURSE_TYPE);
	}

	@Override
	public CoursePrice getPrice() {
		return (CoursePrice) getColumnValue(COLUMN_COURSE_PRICE);
	}

	@Override
	public float getCoursePrice() {
		return getFloatColumnValue(COLUMN_PRICE, -1);
	}

	@Override
	public float getCourseCost() {
		return getFloatColumnValue(COLUMN_COST, -1);
	}

	@Override
	public String getAccountingKey() {
		return getStringColumnValue(COLUMN_ACCOUNTING_KEY);
	}

	@Override
	public Timestamp getStartDate() {
		return getTimestampColumnValue(COLUMN_START_DATE);
	}

	@Override
	public Timestamp getEndDate() {
		return getTimestampColumnValue(COLUMN_END_DATE);
	}

	@Override
	public Timestamp getRegistrationStart() {
		return getTimestampColumnValue(COLUMN_REGISTRATION_START);
	}

	@Override
	public Timestamp getRegistrationEnd() {
		return getTimestampColumnValue(COLUMN_REGISTRATION_END);
	}

	@Override
	public int getBirthyearFrom() {
		return getIntColumnValue(COLUMN_BIRTHYEAR_FROM);
	}

	@Override
	public int getBirthyearTo() {
		return getIntColumnValue(COLUMN_BIRTHYEAR_TO);
	}

	@Override
	public int getMax() {
//		return getIntColumnValue(COLUMN_MAX_PARTICIPANTS);
		int courseFreePlaces = 0;
		try {
			courseFreePlaces = SimpleQuerier.executeIntQuery("select max_per from cou_course where cou_course_id = " + getPrimaryKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return courseFreePlaces;
	}

	@Override
	public int getFreePlaces(boolean countOffers) {
		try {
			CourseChoiceHome home = (CourseChoiceHome) getIDOHome(CourseChoice.class);
			return getMax() - home.getCountByCourse(this, countOffers);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (IDOException e) {
			e.printStackTrace();
		}

		return getMax();
	}

	@Override
	public int getFreePlaces() {
		return getFreePlaces(true);
	}

	@Override
	public int getCourseNumber() {
		return getIntColumnValue(COLUMN_COURSE_NUMBER);
	}

	@Override
	public String getGender() {
		return getStringColumnValue(COLUMN_GENDER);
	}

	@Override
	public String getCourseTypeStr() {
		return getStringColumnValue(COLUMN_COURSE_TYPE_STR);
	}

	@Override
	public boolean isOpenForRegistration() {
		return getBooleanColumnValue(COLUMN_OPEN_FOR_REGISTRATION, true);
	}

	@Override
	public boolean hasPreCare() {
		return getBooleanColumnValue(COLUMN_PRE_CARE, true);
	}

	@Override
	public boolean hasPostCare() {
		return getBooleanColumnValue(COLUMN_POST_CARE, true);
	}

	@Override
	public int getGroupId() {
		return getIntColumnValue(COLUMN_GROUP);
	}

	@Override
	public Group getGroup() {
		return (Group) getColumnValue(COLUMN_GROUP);
	}

	@Override
	public boolean hasPreAndPostCare() {
		return hasPreCare() && hasPostCare();
	}

	@Override
	public Course getTemplate() {
		return (Course) getColumnValue(COLUMN_COURSE_TEMPLATE);
	}


	// Setters
	@Override
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	@Override
	public void setUser(String user) {
		setColumn(COLUMN_USER, user);
	}

	@Override
	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	@Override
	public void setProvider(School provider) {
		setColumn(COLUMN_PROVIDER, provider);
	}

	@Override
	public void setProviderId(Integer providerId) {
		setColumn(COLUMN_PROVIDER, providerId);
	}

	@Override
	public void setCourseType(CourseType courseType) {
		setColumn(COLUMN_COURSE_TYPE, courseType);
	}

	@Override
	public void setPrice(CoursePrice price) {
		setColumn(COLUMN_COURSE_PRICE, price);
	}

	@Override
	public void setCoursePrice(float price) {
		setColumn(COLUMN_PRICE, price);
	}

	@Override
	public void setCourseCost(float cost) {
		setColumn(COLUMN_COST, cost);
	}

	@Override
	public void setAccountingKey(String key) {
		setColumn(COLUMN_ACCOUNTING_KEY, key);
	}

	@Override
	public void setStartDate(Timestamp startDate) {
		setColumn(COLUMN_START_DATE, startDate);
	}

	@Override
	public void setEndDate(Timestamp endDate) {
		setColumn(COLUMN_END_DATE, endDate);
	}

	@Override
	public void setRegistrationEnd(Timestamp registrationEnd) {
		setColumn(COLUMN_REGISTRATION_END, registrationEnd);
	}

	@Override
	public void setRegistrationStart(Timestamp registrationStart) {
		setColumn(COLUMN_REGISTRATION_START, registrationStart);
	}

	@Override
	public void setBirthyearFrom(int from) {
		setColumn(COLUMN_BIRTHYEAR_FROM, from);
	}

	@Override
	public void setBirthyearTo(int to) {
		setColumn(COLUMN_BIRTHYEAR_TO, to);
	}

	@Override
	public void setMax(int max) {
		setColumn(COLUMN_MAX_PARTICIPANTS, max);
	}

	@Override
	public void setCourseNumber(int number) {
		setColumn(COLUMN_COURSE_NUMBER, number);
	}

	@Override
	public void setOpenForRegistration(boolean openForRegistration) {
		setColumn(COLUMN_OPEN_FOR_REGISTRATION, openForRegistration);
	}

	@Override
	public void setHasPreCare(boolean hasPreCare) {
		setColumn(COLUMN_PRE_CARE, hasPreCare);
	}

	@Override
	public void setHasPostCare(boolean hasPostCare) {
		setColumn(COLUMN_POST_CARE, hasPostCare);
	}

	@Override
	public void setGroupId(int id) {
		setColumn(COLUMN_GROUP, id);
	}

	@Override
	public void setGroup(Group group) {
		setColumn(COLUMN_GROUP, group);
	}

	@Override
	public void setTemplate(Course template) {
		setColumn(COLUMN_COURSE_TEMPLATE, template);
	}

	@Override
	public void setTemplateId(Integer templateId) {
		setColumn(COLUMN_COURSE_TEMPLATE, templateId);
	}

	@Override
	public void setGender(String gender) {
		setColumn(COLUMN_GENDER, gender);
	}

	@Override
	public void setCourseTypeStr(String courseTypeStr) {
		setColumn(COLUMN_COURSE_TYPE_STR, courseTypeStr);
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

		if (providerPK instanceof Collection) {
			Collection<?> providerPKs = (Collection<?>) providerPK;
			if (!ListUtil.isEmpty(providerPKs)) {
				query.addCriteria(new InCriteria(new Column(table, COLUMN_PROVIDER), providerPKs));
			}
		} else if (providerPK != null && providerPK.toString().length() > 0) {
			query.addCriteria(new MatchCriteria(providerId, MatchCriteria.EQUALS, providerPK));
		}

		if (schoolTypePK != null && schoolTypePK.toString().length() > 0) {
			query.addCriteria(new MatchCriteria(schoolTypeId, MatchCriteria.EQUALS, schoolTypePK));
		}
		if (courseTypePK != null && courseTypePK.toString().length() > 0) {
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

		try {
			return this.idoFindPKsByQuery(query);
		} catch (IDOFinderException e) {
			getLogger().log(Level.WARNING, "Error executing query: '" + query + "'", e);
		}
		return new ArrayList<Integer>();
	}


	public Collection<Integer> ejbFindAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear) throws FinderException,
		IDORelationshipException {

		return ejbFindAll(providerPK, schoolTypePK, courseTypePK, birthYear, null, null);
	}

	public Collection<Integer> ejbFindAll(Collection<?> providers, Object schoolTypePK, Object courseTypePK) throws FinderException, IDORelationshipException {
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
			AND and1 = new AND(new MatchCriteria(table.getColumn(COLUMN_REGISTRATION_END), false), new MatchCriteria(table.getColumn(COLUMN_REGISTRATION_END), MatchCriteria.GREATER, fromDate));
			query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_START_DATE), MatchCriteria.GREATEREQUAL, fromDate), and1));
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

	public Collection<Integer> ejbFindAllWithNoCourseNumber() throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_COURSE_NUMBER)));

		return idoFindPKsByQuery(query);
	}

	public Collection<Integer> ejbFindAllByTypes(Collection<String> typesIds) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new InCriteria(new Column(table, COLUMN_COURSE_TYPE), typesIds));

		return idoFindPKsByQuery(query);
	}

	public Collection<?> ejbFindAllByUser(String user) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_USER), MatchCriteria.EQUALS, user));

		return idoFindPKsByQuery(query);
	}

	@Override
	public Collection<? extends RentableItem> getRentableItems(Class<? extends RentableItem> itemType) {
		try {
			return super.idoGetRelatedEntities(itemType);
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addRentableItem(RentableItem item) throws IDOAddRelationshipException {
		super.idoAddTo(item);
	}

	@Override
	public void removeRentableItem(RentableItem item) throws IDORemoveRelationshipException {
		super.idoRemoveFrom(item);
	}

	@Override
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

	@Override
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

	@Override
	public void addUser(User user) throws IDOAddRelationshipException {
		this.idoAddTo(user, USERS_LIST);
	}

	@Override
	public void removeUser(User user) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(user, USERS_LIST);
	}


	@Override
	public void removeAllUsers() throws IDORemoveRelationshipException {
		Collection<User> users = getAllUsers();
		if (ListUtil.isEmpty(users))
			return;

		for (User user : users) {
			removeUser(user);
		}

		store();
	}

	@Override
	public Collection<User> getAllUsers() {
		try {
			return this.idoGetRelatedEntitiesBySQL(User.class, "select users.ic_user_id from " + USERS_LIST + " users, " +
					ENTITY_NAME + " courses where users." + getIDColumnName() + " = courses." + getIDColumnName() +
					" and courses." + getIDColumnName() + " = " + getID());
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Collection<User> getAllParticipants() {
		try {
			return this.idoGetRelatedEntitiesBySQL(User.class, "select participants.ic_user_id from " + PARTICIPANTS_LIST + " participants, " +
					ENTITY_NAME + " courses where participants." + getIDColumnName() + " = courses." + getIDColumnName() +
					" and courses." + getIDColumnName() + " = " + getID());
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addParticipant(User participant) throws IDOAddRelationshipException {
		this.idoAddTo(participant, PARTICIPANTS_LIST);
	}

	@Override
	public void removeParticipant(User participant) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(participant, PARTICIPANTS_LIST);
	}

	@Override
	public void removeAllParticipants() throws IDORemoveRelationshipException {
		Collection<User> participants = getAllParticipants();
		if (ListUtil.isEmpty(participants))
			return;

		for (User user : participants) {
			removeParticipant(user);
		}

		store();
	}

	@Override
	public void addSeason(SchoolSeason season) throws IDOAddRelationshipException {
		this.idoAddTo(season);
	}

	@Override
	public Collection<SchoolSeason> getSeasons() {
		try {
			return super.idoGetRelatedEntities(SchoolSeason.class);
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void removeSeason(SchoolSeason season) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(season);
	}

	@Override
	public void removeAllSeasons() throws IDORemoveRelationshipException {
		Collection<SchoolSeason> seasons = getSeasons();
		if (ListUtil.isEmpty(seasons))
			return;

		for (SchoolSeason season: seasons) {
			removeSeason(season);
		}

		store();
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

	public Collection<Integer> ejbFindAllByGroupsIdsAndDates(Collection<Integer> groupsIds, java.util.Date periodFrom, java.util.Date periodTo) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		sql.appendWhere();
		sql.append(COLUMN_GROUP);
		sql.appendInCollection(groupsIds);

		if (periodFrom != null && periodTo != null) {
			IWTimestamp periodFromDate = new IWTimestamp(periodFrom);
			periodFromDate.setHour(0);
			periodFromDate.setMinute(0);
			periodFromDate.setSecond(0);
			periodFromDate.setMilliSecond(0);
			IWTimestamp periodToDate = new IWTimestamp(periodTo);
			periodToDate.setHour(0);
			periodToDate.setMinute(0);
			periodToDate.setSecond(0);
			periodToDate.setMilliSecond(0);
			sql.appendAnd();
			sql.append("(");
			//1 choice
			sql.append("(");
			sql.append(COLUMN_START_DATE);
			sql.appendLessThanOrEqualsSign();
			sql.append(periodFromDate.getDate());
			sql.appendAnd();
			sql.append(COLUMN_END_DATE);
			sql.appendGreaterThanOrEqualsSign();
			sql.append(periodFromDate.getDate());
			sql.append(")");
			sql.appendOr();
			//2 choice
			sql.append("(");
			sql.append(COLUMN_START_DATE);
			sql.appendGreaterThanOrEqualsSign();
			sql.append(periodFromDate.getDate());
			sql.appendAnd();
			sql.append(COLUMN_END_DATE);
			sql.appendLessThanOrEqualsSign();
			sql.append(periodToDate.getDate());
			sql.append(")");
			sql.appendOr();
			//3 choice
			sql.append("(");
			sql.append(COLUMN_START_DATE);
			sql.appendGreaterThanOrEqualsSign();
			sql.append(periodFromDate.getDate());
			sql.appendAnd();
			sql.append(COLUMN_START_DATE);
			sql.appendLessThanOrEqualsSign();
			sql.append(periodToDate.getDate());
			sql.append(")");
			sql.append(")");
		} else if (periodFrom != null) {
			IWTimestamp periodFromDate = new IWTimestamp(periodFrom);
			periodFromDate.setHour(0);
			periodFromDate.setMinute(0);
			periodFromDate.setSecond(0);
			periodFromDate.setMilliSecond(0);
			sql.appendAnd();
			sql.append(COLUMN_END_DATE);
			sql.appendGreaterThanOrEqualsSign();
			sql.append(periodFromDate.getDate());
		} else if (periodTo != null) {
			IWTimestamp periodToDate = new IWTimestamp(periodTo);
			periodToDate.setHour(0);
			periodToDate.setMinute(0);
			periodToDate.setSecond(0);
			periodToDate.setMilliSecond(0);
			sql.appendAnd();
			sql.append(COLUMN_START_DATE);
			sql.appendLessThanOrEqualsSign();
			sql.append(periodToDate.getDate());
		}

//		if (periodFrom != null) {
//			IWTimestamp periodFromDate = new IWTimestamp(periodFrom);
//			periodFromDate.setHour(0);
//			periodFromDate.setMinute(0);
//			periodFromDate.setSecond(0);
//			periodFromDate.setMilliSecond(0);
//			sql.appendAnd();
//			sql.append(COLUMN_START_DATE);
//			sql.appendGreaterThanOrEqualsSign();
//			sql.append(periodFromDate.getDate());
//		}
//		if (periodTo != null) {
//			IWTimestamp periodToDate = new IWTimestamp(periodTo);
//			periodToDate.setHour(0);
//			periodToDate.setMinute(0);
//			periodToDate.setSecond(0);
//			periodToDate.setMilliSecond(0);
//			sql.appendAnd();
//			sql.append(COLUMN_END_DATE);
//			sql.appendLessThanOrEqualsSign();
//			sql.append(periodToDate.getDate());
//		}

		return idoFindPKsByQuery(sql);
	}

	public Collection<Integer> ejbFindAllByCriteria(Collection<Integer> groupsIds,
			java.util.Date periodFrom,
			java.util.Date periodTo,
			Integer birthYear,
			String sortBy,
			String nameOrNumber,
			Boolean openForRegistration,
			Boolean birthYearShouldBeNull) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		sql.append(" selected_entity ");

		sql.appendWhere();
		sql.append(1);
		sql.appendEqualSign();
		sql.append(1);

		//End date is later than today's date
		//sql.appendAnd();
		//sql.append(" selected_entity.");
		//sql.append(COLUMN_END_DATE);
		//sql.appendIsNotNull();
		//sql.appendAnd();
		//sql.append(" selected_entity.");
		//sql.append(COLUMN_END_DATE);
		//sql.appendGreaterThanOrEqualsSign();
		//sql.append((new IWTimestamp()).getDate());

		//Registration end date is later than today's date
		//sql.appendAnd();
		//sql.append(" selected_entity.");
		//sql.append(COLUMN_REGISTRATION_END);
		//sql.appendIsNotNull();
		//sql.appendAnd();
		//sql.append(" selected_entity.");
		//sql.append(COLUMN_REGISTRATION_END);
		//sql.appendGreaterThanOrEqualsSign();
		//sql.append((new IWTimestamp()).getDate());

		//Group ids
		if (groupsIds != null && !groupsIds.isEmpty()) {
			sql.appendAnd();
			sql.append("selected_entity.");
			sql.append(COLUMN_GROUP);
			sql.appendInCollection(groupsIds);
		}

		//Birth year
		if (birthYear != null && birthYear != 0) {
			sql.appendAnd();
			sql.append(birthYear);
			sql.appendLessThanOrEqualsSign();
			sql.append(" selected_entity.");
			sql.append(COLUMN_BIRTHYEAR_TO);
			sql.appendAnd();
			sql.append(birthYear);
			sql.appendGreaterThanOrEqualsSign();
			sql.append(" selected_entity.");
			sql.append(COLUMN_BIRTHYEAR_FROM);
		}

		if (birthYearShouldBeNull) {
			sql.appendAnd();
			sql.append(" selected_entity.");
			sql.append(COLUMN_BIRTHYEAR_TO);
			sql.appendIsNull();
		}

		//Open for registration
		if (openForRegistration != null && openForRegistration) {
			sql.appendAnd();
			sql.append(" selected_entity.");
			sql.appendEquals(COLUMN_OPEN_FOR_REGISTRATION, openForRegistration);
		}

		//Period from
		if (periodFrom != null) {
			IWTimestamp periodFromDate = new IWTimestamp(periodFrom);
			periodFromDate.setHour(0);
			periodFromDate.setMinute(0);
			periodFromDate.setSecond(0);
			periodFromDate.setMilliSecond(0);
			sql.appendAnd();
			sql.append(" selected_entity.");
			sql.append(COLUMN_START_DATE);
			sql.appendGreaterThanOrEqualsSign();
			sql.append(periodFromDate.getDate());
		}

		//Period to
		if (periodTo != null) {
			IWTimestamp periodToDate = new IWTimestamp(periodTo);
			periodToDate.setHour(0);
			periodToDate.setMinute(0);
			periodToDate.setSecond(0);
			periodToDate.setMilliSecond(0);
			sql.appendAnd();
			sql.append(" selected_entity.");
			sql.append(COLUMN_END_DATE);
			sql.appendLessThanOrEqualsSign();
			sql.append(periodToDate.getDate());
		}

		//Name or number equals to the given one
		if (!StringUtil.isEmpty(nameOrNumber)) {
			sql.appendAnd();
			sql.appendLeftParenthesis();
			sql.append(" selected_entity.");
			sql.append(COLUMN_NAME);
			sql.appendLike();
			sql.append("'%" + nameOrNumber + "%'");
			try {
				if (NumberUtils.isNumber(nameOrNumber)) {
					sql.appendOr();
					sql.append(" selected_entity.");
					sql.append(COLUMN_COURSE_NUMBER);
					sql.appendEqualSign();
					sql.append(nameOrNumber);
				}
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Could not check, if search string is number or not: ", e);
			}
			sql.appendRightParenthesis();
		}

		//Sort by
		if (!StringUtil.isEmpty(sortBy)) {
			if (sortBy.equalsIgnoreCase("name")) {
				sql.appendOrderBy(" selected_entity." + COLUMN_NAME);
			}
		}

		getLogger().log(Level.INFO, "ejbFindAllByCriteria SQL: " + sql);

		return idoFindPKsByQuery(sql);
	}

	public Collection<Integer> ejbFindAllByCriteria(Collection<Integer> groupsIds,
													Collection<Integer> templateIds,
													java.util.Date periodFrom,
													java.util.Date periodTo,
													Integer birthYear,
													String sortBy,
													String nameOrNumber,
													Boolean openForRegistration,
													boolean findTemplates) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		sql.append(" selected_entity ");

		//Join course template
		sql.append(CoreConstants.SPACE);
		sql.appendJoin();
		sql.append(ENTITY_NAME);
		sql.append(" template ");
		sql.appendOnEquals("selected_entity." + COLUMN_COURSE_TEMPLATE, "template." + ENTITY_NAME + "_ID ");

		sql.appendWhere();
		sql.append(1);
		sql.appendEqualSign();
		sql.append(1);

		if (!findTemplates) {
			//End date is later than today's date
			sql.appendAnd();
			sql.append("selected_entity.");
			sql.append(COLUMN_END_DATE);
			sql.appendIsNotNull();
			sql.appendAnd();
			sql.append("selected_entity.");
			sql.append(COLUMN_END_DATE);
			sql.appendGreaterThanOrEqualsSign();
			sql.append((new IWTimestamp()).getDate());

			//Registration end date is later than today's date
			sql.appendAnd();
			sql.append("selected_entity.");
			sql.append(COLUMN_REGISTRATION_END);
			sql.appendIsNotNull();
			sql.appendAnd();
			sql.append("selected_entity.");
			sql.append(COLUMN_REGISTRATION_END);
			sql.appendGreaterThanOrEqualsSign();
			sql.append((new IWTimestamp()).getDate());
		}

		//Group ids
		if (groupsIds != null && !groupsIds.isEmpty()) {
			sql.appendAnd();
			sql.append("selected_entity.");
			sql.append(COLUMN_GROUP);
			sql.appendInCollection(groupsIds);
		}

		//Template ids
		if (templateIds != null && !templateIds.isEmpty()) {
			sql.appendAnd();
			sql.append("selected_entity.");
			sql.append(COLUMN_COURSE_TEMPLATE);
			sql.appendInCollection(templateIds);
		}

		//Birth year (ONLY FOR TEMPLATES)
		if (birthYear != null && birthYear != 0) {
			sql.appendAnd();
			sql.append(birthYear);
			sql.appendLessThanOrEqualsSign();
			sql.append("template.");
			sql.append(COLUMN_BIRTHYEAR_TO);
			sql.appendAnd();
			sql.append(birthYear);
			sql.appendGreaterThanOrEqualsSign();
			sql.append("template.");
			sql.append(COLUMN_BIRTHYEAR_FROM);
		}

		//Open for registration (ONLY FOR TEMPLATES)
		if (openForRegistration != null && openForRegistration) {
			sql.appendAnd();
			sql.append("template.");
			sql.appendEquals(COLUMN_OPEN_FOR_REGISTRATION, openForRegistration);
		}

		//Period from
		if (periodFrom != null) {
			IWTimestamp periodFromDate = new IWTimestamp(periodFrom);
			periodFromDate.setHour(0);
			periodFromDate.setMinute(0);
			periodFromDate.setSecond(0);
			periodFromDate.setMilliSecond(0);
			sql.appendAnd();
			sql.append("selected_entity.");
			sql.append(COLUMN_START_DATE);
			sql.appendGreaterThanOrEqualsSign();
			sql.append(periodFromDate.getDate());
		}

		//Period to
		if (periodTo != null) {
			IWTimestamp periodToDate = new IWTimestamp(periodTo);
			periodToDate.setHour(0);
			periodToDate.setMinute(0);
			periodToDate.setSecond(0);
			periodToDate.setMilliSecond(0);
			sql.appendAnd();
			sql.append("selected_entity.");
			sql.append(COLUMN_END_DATE);
			sql.appendLessThanOrEqualsSign();
			sql.append(periodToDate.getDate());
		}

		//Name or number equals to the given one
		if (!StringUtil.isEmpty(nameOrNumber)) {
			sql.appendAnd();
			sql.appendLeftParenthesis();
			sql.append("selected_entity.");
			sql.append(COLUMN_NAME);
			sql.appendLike();
			sql.append("'%" + nameOrNumber + "%'");
			try {
				if (NumberUtils.isNumber(nameOrNumber)) {
					sql.appendOr();
					sql.append("selected_entity.");
					sql.append(COLUMN_COURSE_NUMBER);
					sql.appendEqualSign();
					sql.append(nameOrNumber);
				}
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Could not check, if search string is number or not: ", e);
			}
			sql.appendRightParenthesis();
		}

		//Templates or courses
		sql.appendAnd();
		sql.append("selected_entity.");
		sql.append(COLUMN_COURSE_TEMPLATE);
		if (findTemplates) {
			sql.appendIsNull();
		} else {
			sql.appendIsNotNull();
		}

		//Sort by
		if (!StringUtil.isEmpty(sortBy)) {
			if (sortBy.equalsIgnoreCase("name")) {
				sql.appendOrderBy("selected_entity." + COLUMN_NAME);
			}
		}

		return idoFindPKsByQuery(sql);
	}


	public Collection<Integer> ejbFindAllByTemplateIds(Collection<Integer> templateIds) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		sql.append(" selected_entity ");

		sql.appendWhere();
		sql.append(1);
		sql.appendEqualSign();
		sql.append(1);

		//Template ids
		if (templateIds != null && !templateIds.isEmpty()) {
			sql.appendAnd();
			sql.append("selected_entity.");
			sql.append(COLUMN_COURSE_TEMPLATE);
			sql.appendInCollection(templateIds);
		}

		//Sort by
		sql.appendOrderBy("selected_entity." + COLUMN_NAME);

		return idoFindPKsByQuery(sql);
	}

	public Collection<Integer> ejbFindAllWithoutTemplates() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		sql.append(" selected_entity ");

		sql.appendWhere();
		sql.append(1);
		sql.appendEqualSign();
		sql.append(1);

		//Templates or courses
		sql.appendAnd();
		sql.append("selected_entity.");
		sql.append(COLUMN_COURSE_TEMPLATE);
		sql.appendIsNotNull();

		//Sort by
		sql.appendOrderBy("selected_entity." + COLUMN_NAME);

		return idoFindPKsByQuery(sql);
	}


}