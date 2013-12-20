package is.idega.idegaweb.egov.course.data;

import is.idega.idegaweb.egov.course.data.rent.RentableItem;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.FinderException;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
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
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;

public class CourseBMPBean extends GenericEntity implements Course {

	private static final long serialVersionUID = 8629779260677160057L;

	public static final String ENTITY_NAME = "COU_COURSE";

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
	private static final String COLUMN_REGISTRATION_END = "REGISTRATION_END";
	private static final String COLUMN_BIRTHYEAR_FROM = "BIRTHYEAR_FROM";
	private static final String COLUMN_BIRTHYEAR_TO = "BIRTHYEAR_TO";
	private static final String COLUMN_MAX_PARTICIPANTS = "MAX_PER";
	private static final String COLUMN_OPEN_FOR_REGISTRATION = "OPEN_FOR_REGISTRATION",
								COLUMN_COURSE_PRICES = "COURSE_PRICES";
	public static final String COLUMN_VISIBILITY = "IS_VISIBLE";

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

		addManyToOneRelationship(COLUMN_COURSE_TYPE, CourseType.class);
		addManyToOneRelationship(COLUMN_COURSE_PRICE, CoursePrice.class);

		/* TODO What exactly I should do with this, I mean here should be schools */
		addManyToOneRelationship(COLUMN_PROVIDER, CourseProvider.class);
		addManyToManyRelationShip(CoursePrice.class, COLUMN_COURSE_PRICES);

		/* Tells that course should be not visible to all users */
		addAttribute(COLUMN_VISIBILITY, "Is private", Boolean.class);

		/* Groups which can see the course when course is private */
		addManyToManyRelationShip(Group.class);

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
	public CourseProvider getProvider() {
		return (CourseProvider) getColumnValue(COLUMN_PROVIDER);
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
		return getIntColumnValue(COLUMN_MAX_PARTICIPANTS);
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
	public boolean hasPreAndPostCare() {
		return hasPreCare() && hasPostCare();
	}

	@Override
	public Collection<Group> getGroupsWithAccess() {
		try {
			return idoGetRelatedEntities(Group.class);
		} catch (IDORelationshipException e) {
			getLogger().log(Level.WARNING,
					"Failed to get related " + Group.class.getName() + "'s " +
					"for " + this.getClass().getName() +
					" by id: '" + getPrimaryKey() +
					"' cause of: ", e);
		}

		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.Course#isPrivate()
	 */
	@Override
	public boolean isPrivate() {
		return getBooleanColumnValue(COLUMN_VISIBILITY, false);
	}

	// Setters

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.Course#setGroupsWithAccess(java.util.Collection)
	 */
	@Override
	public void setGroupsWithAccess(Collection<Group> groups) {
		if (!ListUtil.isEmpty(groups)) {
			removeGroupsWithAccess();
			idoAddTo(groups);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.Course#removeGroupsWithAccess()
	 */
	@Override
	public void removeGroupsWithAccess() {
		try {
			idoRemoveFrom(Group.class);
			getLogger().info("All groups related to " + Course.class.getSimpleName() +
					" by id: '" + getPrimaryKey() + "'");
		} catch (IDORemoveRelationshipException e) {
			getLogger().log(Level.WARNING,
					"Failed to remove " + Course.class.getSimpleName() +
					" by id: '" + getPrimaryKey() + "' cause of: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see is.idega.idegaweb.egov.course.data.Course#setPrivate(boolean)
	 */
	@Override
	public void setPrivate(boolean isPrivate) {
		setColumn(COLUMN_VISIBILITY, isPrivate);
	}

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
	public void setProvider(CourseProvider provider) {
		setColumn(COLUMN_PROVIDER, provider);
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

	// Finders
	public Collection<Integer> ejbFindAll() throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null, null, -1, null, null);
	}

	public Collection<Integer> ejbFindAllByProvider(CourseProvider provider) throws FinderException, IDORelationshipException {
		return ejbFindAll(provider.getPrimaryKey(), null, null, -1, null, null);
	}

	public Collection<Integer> ejbFindAllByBirthYear(int birthYear) throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null, null, birthYear, null, null);
	}

	protected Integer getYear(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = java.util.GregorianCalendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 *
	 * <p>Constructs SQL query</p>
	 * @param courseProviders to filter by, skipped if <code>null</code>;
	 * @param couserProviderTypes to filter by, skipped if <code>null</code>;
	 * @param courseTypes to filter by, skipped if <code>null</code>;
	 * @param birthDateFrom is floor of age of course attender,
	 * skipped if <code>null</code>;
	 * @param birthDateTo is ceiling of age of course attender,
	 * skipped if <code>null</code>;
	 * @param fromDate is floor for course start date,
	 * skipped if <code>null</code>;
	 * @param toDate is ceiling for course start date,
	 * skipped if <code>null</code>;
	 * @param isPrivate tells if {@link Course}s can be viewed by all,
	 * skipped if <code>null</code>;
	 * @param groupsWithAccess is {@link Group}, which can view private
	 * {@link Course}s, skipped if <code>null</code>;
	 * @return query by criteria;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public IDOQuery getQuery(
			Collection<? extends CourseProvider> courseProviders,
			Collection<? extends CourseProviderType> couserProviderTypes,
			Collection<? extends CourseType> courseTypes,
			Date birthDateFrom,
			Date birthDateTo,
			Date fromDate,
			Date toDate,
			Boolean isPrivate,
			Collection<Group> groupsWithAccess) {

		/* This table */
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);

		/* Filtering by course providers */
		if (!ListUtil.isEmpty(courseProviders)) {
			query.appendJoinOn(courseProviders);
		}

		/* Filtering by course provider types */
		if (!ListUtil.isEmpty(couserProviderTypes)) {
			query.appendJoinOn(couserProviderTypes);
		}

		/* Filtering by course types */
		if (!ListUtil.isEmpty(courseTypes)) {
			query.append(courseTypes);
		}

		/* Filtering by groups, which has access */
		if (!ListUtil.isEmpty(groupsWithAccess)) {
			query.appendJoinOn(groupsWithAccess);
		}

		/* Filtering by local conditions */
		boolean appendAnd = Boolean.FALSE;

		/* Filtering by birth date */
		if (birthDateFrom != null && birthDateFrom.getTime() > 0) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.append(COLUMN_BIRTHYEAR_FROM)
			.appendLessThanOrEqualsSign()
			.append(getYear(birthDateFrom));
		}

		if (birthDateTo != null && birthDateTo.getTime() > 0) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.append(COLUMN_BIRTHYEAR_TO)
			.appendGreaterThanOrEqualsSign()
			.append(getYear(birthDateTo));
		}

		/* Filtering by course date */
		if (fromDate != null && fromDate.getTime() > 0) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.append(COLUMN_START_DATE)
			.appendLessThanOrEqualsSign()
			.append(getYear(fromDate));
		}

		if (toDate != null && toDate.getTime() > 0) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.append(COLUMN_START_DATE)
			.appendGreaterThanOrEqualsSign()
			.append(getYear(toDate));
		}

		/* Filtering public or private ones */
		if (isPrivate != null) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.appendEquals(COLUMN_VISIBILITY, isPrivate);
		}

		/* Ordered by: */
		query.appendOrderBy(COLUMN_START_DATE)
		.append(CoreConstants.COMMA)
		.append(CoreConstants.SPACE)
		.append(COLUMN_NAME);
		return query;
	}

	/**
	 *
	 * <p>Finds all primary keys by following criteria:</p>
	 * @param courseProviders to filter by, skipped if <code>null</code>;
	 * @param couserProviderTypes to filter by, skipped if <code>null</code>;
	 * @param courseTypes to filter by, skipped if <code>null</code>;
	 * @param birthDateFrom is floor of age of course attender,
	 * skipped if <code>null</code>;
	 * @param birthDateTo is ceiling of age of course attender,
	 * skipped if <code>null</code>;
	 * @param fromDate is floor for course start date,
	 * skipped if <code>null</code>;
	 * @param toDate is ceiling for course start date,
	 * skipped if <code>null</code>;
	 * @param isPrivate tells if {@link Course}s can be viewed by all,
	 * skipped if <code>null</code>;
	 * @param groupsWithAccess is {@link Group}, which can view private
	 * {@link Course}s, skipped if <code>null</code>;
	 * @return primary keys found data source or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindAll(
			Collection<? extends CourseProvider> courseProviders,
			Collection<? extends CourseProviderType> couserProviderTypes,
			Collection<? extends CourseType> courseTypes,
			Date birthDateFrom,
			Date birthDateTo,
			Date fromDate,
			Date toDate,
			Boolean isPrivate,
			Collection<Group> groupsWithAccess) {
		IDOQuery query = getQuery(courseProviders, couserProviderTypes,
				courseTypes, birthDateFrom, birthDateTo, fromDate, toDate,
				isPrivate, groupsWithAccess);
		try {
			return idoFindPKsByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING,
					"Failed to get primary keys for " + this.getClass().getName() +
					" by query: '" + query.toString() + "' cause of: ", e);
		}

		return Collections.emptyList();
	}

	// TODO can be merged
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

		if (providerPK != null && providerPK.toString().length() > 0) {
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


		return this.idoFindPKsByQuery(query);
	}


	public Collection<Integer> ejbFindAll(Object providerPK, Object schoolTypePK, Object courseTypePK, int birthYear) throws FinderException,
		IDORelationshipException {

		return ejbFindAll(providerPK, schoolTypePK, courseTypePK, birthYear, null, null);
	}

	// TODO can be merged
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

	/**
	 *
	 * @param keys to convert, not <code>null</code>;
	 * @return converted {@link Integer}s, where it was possible to convert;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	protected Collection<Integer> convertPrimaryKeys(Collection<Object> keys) {
		if (ListUtil.isEmpty(keys)) {
			return Collections.emptyList();
		}

		ArrayList<Integer> integers = new ArrayList<Integer>(keys.size());
		for (Object key : keys) {
			try {
				integers.add(Integer.valueOf(key.toString()));
			} catch (NullPointerException e) {
				getLogger().warning("Failed to convert: '" + key + "' to " + Integer.class.getSimpleName());
			}
		}

		return integers;
	}

	/**
	 *
	 * <p>Finds all primary keys by following criteria:</p>
	 * @param provider to filter by, skipped if <code>null</code>;
	 * @param type to filter by, skipped if <code>null</code>;
	 * @param courseType to filter by, skipped if <code>null</code>;
	 * @param fromDate is floor for course start date,
	 * skipped if <code>null</code>;
	 * @param toDate is ceiling for course start date,
	 * skipped if <code>null</code>;
	 * @return primary keys found data source or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Integer> ejbFindAll(
			CourseProvider provider,
			CourseProviderType type,
			CourseType courseType,
			Date fromDate,
			Date toDate) {

		return convertPrimaryKeys(ejbFindAll(
				provider != null ? Arrays.asList(provider) : null,
				type != null ? Arrays.asList(type) : null,
				courseType != null ? Arrays.asList(courseType) : null,
				null, null,
				fromDate, toDate,
				null, null));
	}

	// TODO can be merged
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

	/**
	 *
	 * <p>Finds number of all primary keys by following criteria:</p>
	 * @param provider to filter by, skipped if <code>null</code>;
	 * @param type to filter by, skipped if <code>null</code>;
	 * @param courseType to filter by, skipped if <code>null</code>;
	 * @param fromDate is floor for course start date,
	 * skipped if <code>null</code>;
	 * @param toDate is ceiling for course start date,
	 * skipped if <code>null</code>;
	 * @return number primary keys found data source or
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public int ejbHomeGetCountByProviderAndSchoolTypeAndCourseType(
			CourseProvider provider,
			CourseProviderType type,
			CourseType courseType,
			Date fromDate, Date toDate
	) {
		IDOQuery query = getQuery(
				provider != null ? Arrays.asList(provider) : null,
				type != null ? Arrays.asList(type) : null,
				courseType != null ? Arrays.asList(courseType) : null,
				null, null,
				fromDate, toDate,
				null, null);

		try {
			return this.idoGetNumberOfRecords(query);
		} catch (IDOException e) {
			getLogger().log(Level.WARNING,
					"Failed to get number of " + this.getClass().getSimpleName() +
					" by query: '" + query.toString() + "' cause of: ", e);
		}

		return 0;
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
	@SuppressWarnings("unchecked")
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
	public void addPrice(CoursePrice price) throws IDOAddRelationshipException {
		this.idoAddTo(price);
	}

	@Override
	@SuppressWarnings("unchecked")
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
}