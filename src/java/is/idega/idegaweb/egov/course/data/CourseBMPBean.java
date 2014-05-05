package is.idega.idegaweb.egov.course.data;

import is.idega.idegaweb.egov.course.data.rent.RentableItem;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDOUtil;
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
import com.idega.util.ArrayUtil;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;

public class CourseBMPBean extends GenericEntity implements Course {

	private static final long serialVersionUID = 8629779260677160057L;

	public static final String ENTITY_NAME = "COU_COURSE";

	public static final String COLUMN_COURSE_NUMBER = "COURSE_NUMBER";
	public static final String COLUMN_NAME = "NAME";
	public static final String COLUMN_USER = "USER_NAME";
	public static final String COLUMN_DESCRIPTION = "DESCRIPTION";

	public static final String COLUMN_COURSE_TYPE = "COU_COURSE_TYPE_ID";
	public static final String COLUMN_PROVIDER = "PROVIDER_ID";
	public static final String COLUMN_ACCOUNTING_KEY = "ACCOUNTING_KEY";
	public static final String COLUMN_COURSE_PRICE = "COU_COURSE_PRICE_ID";
	public static final String COLUMN_PRICE = "COURSE_PRICE";
	public static final String COLUMN_COST = "COURSE_COST";
	public static final String COLUMN_PRE_CARE = "HAS_PRE_CARE";
	public static final String COLUMN_POST_CARE = "HAS_POST_CARE";

	public static final String COLUMN_START_DATE = "START_DATE";
	public static final String COLUMN_END_DATE = "END_DATE";
	public static final String COLUMN_REGISTRATION_END = "REGISTRATION_END";
	public static final String COLUMN_BIRTHYEAR_FROM = "BIRTHYEAR_FROM";
	public static final String COLUMN_BIRTHYEAR_TO = "BIRTHYEAR_TO";
	public static final String COLUMN_MAX_PARTICIPANTS = "MAX_PER";
	public static final String COLUMN_OPEN_FOR_REGISTRATION = "OPEN_FOR_REGISTRATION",
								COLUMN_COURSE_PRICES = "COURSE_PRICES";
	public static final String COLUMN_VISIBILITY = "IS_VISIBLE";
	public static final String PARENT_COURSE_ID = "parent_course_id",
								COLUMN_COURSE_SEASONS = "COURSE_SEASONS";

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
//		addManyToManyRelationShip(SchoolSeason.class, COLUMN_COURSE_SEASONS);

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

		addOneToOneRelationship(PARENT_COURSE_ID, Course.class);
	}

	/**
	 * 
	 * @return parent {@link Course} if this {@link Course} has one;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Course getParentCourse() {
		Course parentCourse = (Course) getColumnValue(ChildCourseBMPBean.PARENT_COURSE_ID);
		if (parentCourse == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT cc.PARENT_COURSE_ID ");
			sb.append("FROM cou_course cc ");
			sb.append("WHERE cc.COU_COURSE_ID = ").append(getPrimaryKey().toString());
			
			String[] parentIds = null;
			try {
				parentIds = SimpleQuerier.executeStringQuery(sb.toString());
			} catch (Exception e) {
				getLogger().log(Level.WARNING, 
						"Failed to get parent course id by query: '" + sb.toString() + 
						"' cause of: ", e);
			}

			if (!ArrayUtil.isEmpty(parentIds)) {
				parentCourse = getChildCourseHome().findByPrimaryKey(parentIds[0]);
			}
		}

		return parentCourse;
	}

	// Getters
	@Override
	public String getName() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getName();
		}

		return getStringColumnValue(COLUMN_NAME);
	}

	@Override
	public String getUser() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getUser();
		}

		return getStringColumnValue(COLUMN_USER);
	}

	@Override
	public String getDescription() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getDescription();
		}

		return getStringColumnValue(COLUMN_DESCRIPTION);
	}

	@Override
	public CourseProvider getProvider() {
		return getCourseProviderHome().findByPrimaryKeyRecursively(getProviderId());
	}

	@Override
	public String getProviderId() {
		return getStringColumnValue(COLUMN_PROVIDER);
	}

	@Override
	public CourseType getCourseType() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getCourseType();
		}

		return (CourseType) getColumnValue(COLUMN_COURSE_TYPE);
	}

	@Override
	public CoursePrice getPrice() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getPrice();
		}

		return (CoursePrice) getColumnValue(COLUMN_COURSE_PRICE);
	}

	@Override
	public float getCoursePrice() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getCoursePrice();
		}
		
		return getFloatColumnValue(COLUMN_PRICE, -1);
	}

	@Override
	public float getCourseCost() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getCourseCost();
		}

		return getFloatColumnValue(COLUMN_COST, -1);
	}

	@Override
	public String getAccountingKey() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getAccountingKey();
		}

		return getStringColumnValue(COLUMN_ACCOUNTING_KEY);
	}

	@Override
	public Timestamp getStartDate() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getStartDate();
		}

		return getTimestampColumnValue(COLUMN_START_DATE);
	}

	@Override
	public Timestamp getEndDate() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getEndDate();
		}
		
		return getTimestampColumnValue(COLUMN_END_DATE);
	}

	@Override
	public Timestamp getRegistrationEnd() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getRegistrationEnd();
		}

		return getTimestampColumnValue(COLUMN_REGISTRATION_END);
	}

	@Override
	public int getBirthyearFrom() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getBirthyearFrom();
		}

		return getIntColumnValue(COLUMN_BIRTHYEAR_FROM);
	}

	@Override
	public int getBirthyearTo() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getBirthyearTo();
		}
		
		return getIntColumnValue(COLUMN_BIRTHYEAR_TO);
	}

	@Override
	public int getMax() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getMax();
		}

		return getIntColumnValue(COLUMN_MAX_PARTICIPANTS);
	}

	@Override
	public int getFreePlaces(boolean countOffers) {
		Course parentCourse = getParentCourse();

		try {
			CourseChoiceHome home = (CourseChoiceHome) getIDOHome(CourseChoice.class);
			return getMax() - home.getCountByCourse(
					parentCourse == null ? this : parentCourse, 
					countOffers);
		} catch (Exception e) {
			getLogger().log(Level.WARNING, 
					"Failed to get " + CourseChoice.class.getSimpleName() + 
					"'s cause of: ", e);
		}

		return getMax();
	}

	@Override
	public int getFreePlaces() {
		return getFreePlaces(true);
	}

	@Override
	public int getCourseNumber() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getCourseNumber();
		}

		return getIntColumnValue(COLUMN_COURSE_NUMBER);
	}

	@Override
	public boolean isOpenForRegistration() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.isOpenForRegistration();
		}

		return getBooleanColumnValue(COLUMN_OPEN_FOR_REGISTRATION, true);
	}

	@Override
	public boolean hasPreCare() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.hasPreCare();
		}
		
		return getBooleanColumnValue(COLUMN_PRE_CARE, true);
	}

	@Override
	public boolean hasPostCare() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.hasPreCare();
		}

		return getBooleanColumnValue(COLUMN_POST_CARE, true);
	}

	@Override
	public boolean hasPreAndPostCare() {
		return hasPreCare() && hasPostCare();
	}

	@Override
	public Collection<Group> getGroupsWithAccess() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getGroupsWithAccess();
		}

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
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.isPrivate();
		}

		return getBooleanColumnValue(COLUMN_VISIBILITY, false);
	}

	@Override
	public Collection<? extends RentableItem> getRentableItems(
			Class<? extends RentableItem> itemType) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			return parentCourse.getRentableItems(itemType);
		}

		try {
			return super.idoGetRelatedEntities(itemType);
		} catch (IDORelationshipException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get rentable items cause of: ", e);
		}

		return null;
	}

	@Override
	public Collection<CoursePrice> getAllPrices() {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.getAllPrices();
		}
		
		try {
			return super.idoGetRelatedEntities(CoursePrice.class);
		} catch (IDORelationshipException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get related " + CoursePrice.class.getSimpleName() + 
					"'s cause of: ", e);
		}

		return null;
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

	protected Integer getYear(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = java.util.GregorianCalendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.data.GenericEntity#remove()
	 */
	@Override
	public void remove() throws RemoveException {
		Collection<ChildCourse> childCourses = getChildCourseHome().findChildCourses(this);
		if (!ListUtil.isEmpty(childCourses)) {
			
			/*
			 * Removing child courses
			 */
			getLogger().info("Found child courses! They will be removed!");
			for (ChildCourse childCourse : childCourses) {
				childCourse.removeGroupsWithAccess();

				try {
					childCourse.remove();
					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"Removed " + childCourse.getClass().getSimpleName() +
							" by id: '" + childCourse.getPrimaryKey() + "'");
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.WARNING,
							"Failed to " + childCourse.getClass().getSimpleName() +
							" by id: '" + childCourse.getPrimaryKey() +
							"' cause of: ", e);
				}
			}
		}

		super.remove();
	}

	/**
	 *
	 * <p>Constructs SQL query</p>
	 * @param courseProvidersIds is {@link Collection} of 
	 * {@link CourseProvider#getPrimaryKey()} to filter by, 
	 * skipped if <code>null</code>;
	 * @param courseProviderTypes to filter by, skipped if <code>null</code>;
	 * @param courseTypesIds is {@link Collection} of {@link CourseType#getPrimaryKey()} 
	 * to filter by, skipped if <code>null</code>;
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
	 * @param notChild <code>true</code> if 
	 * {@link ChildCourse}s should be excluded;
	 * @return query by criteria;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public IDOQuery getQuery(
			Collection<String> courseProvidersIds,
			Collection<? extends CourseProviderType> courseProviderTypes,
			Collection<String> courseTypesIds,
			Date birthDateFrom,
			Date birthDateTo,
			Date fromDate,
			Date toDate,
			Boolean isPrivate,
			Collection<Group> groupsWithAccess) {

		/* This table */
		IDOQuery query = idoQuery();
		query.useDefaultAlias = Boolean.TRUE;
		query.appendSelectDistinctFrom(this);

		/* Filtering by groups, which has access */
		if (!ListUtil.isEmpty(groupsWithAccess)) {
			query.appendJoinOn(groupsWithAccess);
		}

		/* Filtering by course provider types */
		if (!ListUtil.isEmpty(courseProviderTypes)) {
			Collection<CourseProvider> providersByTypes = getCourseProviderHome()
					.findByTypeRecursively(courseProviderTypes);
			if (!ListUtil.isEmpty(providersByTypes)) {
				List<String> providerByTypesIDs = IDOUtil.getInstance().getPrimaryKeys(
						providersByTypes);
				if (ListUtil.isEmpty(courseProvidersIds)) {
					courseProvidersIds = new ArrayList<String>(providerByTypesIDs);
				} else {
					/* Avoids unsupported operation exception */
					courseProvidersIds = new ArrayList<String>(courseProvidersIds);
					courseProvidersIds.retainAll(providerByTypesIDs);
				}
			}
		}

		/* Filtering by course providers */
		if (!ListUtil.isEmpty(courseProvidersIds)) {			
			String primaryKeyColumnName;
			try {
				primaryKeyColumnName = getEntityDefinition()
						.getPrimaryKeyDefinition().getField().getSQLFieldName();
			} catch (IDOCompositePrimaryKeyException e) {
				primaryKeyColumnName = "COU_COURSE_ID";
				getLogger().log(Level.WARNING, 
						"Failed to get name primary key column. Will use: '" 
								+ primaryKeyColumnName + "' Problem: ", e);
			}

			/*
			 * selected_entity.COU_COURSE_ID
			 */
			StringBuilder primaryKeyColumnOfCurrentEntity = new StringBuilder();
			primaryKeyColumnOfCurrentEntity.append(IDOQuery.ENTITY_TO_SELECT)
					.append(CoreConstants.DOT).append(primaryKeyColumnName);

			/*
			 * selected_entity_1
			 */
			String joinedEntityName = IDOQuery.ENTITY_TO_SELECT + "_1";

			/*
			 * selected_entity_1.COU_COURSE_ID
			 */
			StringBuilder primaryKeyColumnOfExternalEntity = new StringBuilder();
			primaryKeyColumnOfExternalEntity.append(joinedEntityName)
					.append(CoreConstants.DOT).append(primaryKeyColumnName);

			/*
			 * selected_entity_1.PARENT_COURSE_ID
			 */
			StringBuilder parentKeyOfExternalEntity = new StringBuilder();
			parentKeyOfExternalEntity.append(joinedEntityName)
					.append(CoreConstants.DOT).append(ChildCourseBMPBean.PARENT_COURSE_ID);

			/*
			 * selected_entity_1.PROVIDER_ID
			 */
			StringBuilder providerIdOfExternalEntity = new StringBuilder();
			providerIdOfExternalEntity.append(joinedEntityName)
					.append(CoreConstants.DOT).append(COLUMN_PROVIDER);

			/*
			 * The big miracle...
			 */
			query.append(IDOQuery.JOIN).append(ENTITY_NAME).append(CoreConstants.SPACE)
			.append(joinedEntityName).append(CoreConstants.SPACE)
			.append(IDOQuery.ON).append(CoreConstants.BRACKET_LEFT)
				.append(CoreConstants.BRACKET_LEFT)
						.appendEquals(
								primaryKeyColumnOfCurrentEntity.toString(), 
								primaryKeyColumnOfExternalEntity.toString())
					.appendAnd()
						.append(getProviderIdColumn())
						.appendInForStringCollectionWithSingleQuotes(courseProvidersIds)
				.append(CoreConstants.BRACKET_RIGHT)
				.appendOr()
				.append(CoreConstants.BRACKET_LEFT)
						.appendEquals(
								primaryKeyColumnOfCurrentEntity.toString(), 
								parentKeyOfExternalEntity.toString())
					.appendAnd()
						.append(providerIdOfExternalEntity.toString())
						.appendInForStringCollectionWithSingleQuotes(courseProvidersIds)
				.append(CoreConstants.BRACKET_RIGHT)
			.append(CoreConstants.BRACKET_RIGHT);
		}

		/* Filtering by local conditions */
		boolean appendAnd = Boolean.FALSE;

		/* Filtering by birth date from */
		if (birthDateFrom != null && birthDateFrom.getTime() > 0) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.append(getBirthyearFromColumn())
			.appendLessThanOrEqualsSign()
			.append(getYear(birthDateFrom));
		}

		/* Filtering by birth date to */
		if (birthDateTo != null && birthDateTo.getTime() > 0) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.append(getBirthyearToColumn()).appendGreaterThanOrEqualsSign()
					.append(getYear(birthDateTo));
		}

		/* Filtering by course types */
		if (!ListUtil.isEmpty(courseTypesIds)) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.append(getTypeColumn())
					.appendInForStringCollectionWithSingleQuotes(courseTypesIds);
		}

		/* 
		 * Filtering by floor of course start date 
		 */
		if (fromDate != null && fromDate.getTime() > 0) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.append(getStartDateColumn())
					.appendGreaterThanOrEqualsSign()
					.append(fromDate);
		}

		/* 
		 * Filtering by ceiling of course start date
		 */
		if (toDate != null && toDate.getTime() > 0) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.append(getStartDateColumn())
					.appendLessThanOrEqualsSign()
					.append(toDate);
		}

		/* Filtering public or private ones */
		if (isPrivate != null) {
			if (appendAnd) {
				query.appendAnd();
			} else {
				query.appendWhere();
				appendAnd = Boolean.TRUE;
			}

			query.appendEquals(getVisiblityColumn(), isPrivate);
		}

		/*
		 * selected_entity.PARENT_COURSE_ID != NULL
		 */
		if (appendAnd) {
			query.appendAnd();
		} else {
			query.appendWhere();
			appendAnd = Boolean.TRUE;
		}

		query.append(getParentCourseIdColumn()).appendIsNull();

		/* Ordered by: */
		query.appendOrderBy(getStartDateColumn())
		.append(CoreConstants.COMMA)
		.append(CoreConstants.SPACE)
		.append(getNameColumn());
		return query;
	}
	
	/**
	 *
	 * <p>Finds all primary keys by following criteria:</p>
	 * @param courseProviders is {@link Collection} of 
	 * {@link CourseProvider#getPrimaryKey()}
	 * to filter by, skipped if <code>null</code>;
	 * @param couserProviderTypes to filter by, skipped if <code>null</code>;
	 * @param courseTypes is {@link Collection} of {@link CourseType#getPrimaryKey()} 
	 * to filter by, skipped if <code>null</code>;
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
			Collection<String> courseProviders,
			Collection<? extends CourseProviderType> couserProviderTypes,
			Collection<String> courseTypes,
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
				provider != null ? Arrays.asList(provider.getPrimaryKey().toString()) : null,
				type != null ? Arrays.asList(type) : null,
				courseType != null ? Arrays.asList(courseType.getPrimaryKey().toString()) : null,
				null, null,
				fromDate, toDate,
				null, null);

		try {
			return idoGetNumberOfRecords(query);
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

	/**
	 * 
	 * @return selected_entity.NAME;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	private String getNameColumn() {
		StringBuilder sb = new StringBuilder();
		sb.append(IDOQuery.ENTITY_TO_SELECT)
				.append(CoreConstants.DOT).append(COLUMN_NAME);
		return sb.toString();
	}

	/**
	 * 
	 * @return selected_entity.PROVIDER_ID;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	private String getProviderIdColumn() {
		StringBuilder sb = new StringBuilder();
		sb.append(IDOQuery.ENTITY_TO_SELECT)
				.append(CoreConstants.DOT).append(COLUMN_PROVIDER);
		return sb.toString();
	}

	/**
	 * 
	 * @return selected_entity.BIRTHYEAR_FROM;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	private String getBirthyearFromColumn() {
		StringBuilder sb = new StringBuilder();
		sb.append(IDOQuery.ENTITY_TO_SELECT)
				.append(CoreConstants.DOT).append(COLUMN_BIRTHYEAR_FROM);
		return sb.toString();
	}
	
	/**
	 * 
	 * @return selected_entity.BIRTHYEAR_TO;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	private String getBirthyearToColumn() {
		StringBuilder sb = new StringBuilder();
		sb.append(IDOQuery.ENTITY_TO_SELECT)
				.append(CoreConstants.DOT).append(COLUMN_BIRTHYEAR_TO);
		return sb.toString();
	}
	
	/**
	 * 
	 * @return selected_entity.COU_COURSE_TYPE_ID;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	private String getTypeColumn() {
		StringBuilder sb = new StringBuilder();
		sb.append(IDOQuery.ENTITY_TO_SELECT)
				.append(CoreConstants.DOT).append(COLUMN_COURSE_TYPE);
		return sb.toString();
	}

	/**
	 * 
	 * @return selected_entity.START_DATE;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	private String getStartDateColumn() {
		StringBuilder sb = new StringBuilder();
		sb.append(IDOQuery.ENTITY_TO_SELECT)
				.append(CoreConstants.DOT).append(COLUMN_START_DATE);
		return sb.toString();
	}

	/**
	 * 
	 * @return selected_entity.IS_VISIBLE;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	private String getVisiblityColumn() {
		StringBuilder sb = new StringBuilder();
		sb.append(IDOQuery.ENTITY_TO_SELECT)
				.append(CoreConstants.DOT).append(COLUMN_VISIBILITY);
		return sb.toString();
	}
	
	/**
	 * 
	 * @return selected_entity.PARENT_COURSE_ID
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	private String getParentCourseIdColumn() {
		StringBuilder sb = new StringBuilder();
		sb.append(IDOQuery.ENTITY_TO_SELECT)
				.append(CoreConstants.DOT).append(ChildCourseBMPBean.PARENT_COURSE_ID);
		return sb.toString();
	}

	private CourseProviderHome courseProviderHome;

	protected CourseProviderHome getCourseProviderHome() {
		if (this.courseProviderHome == null) {
			try {
				this.courseProviderHome = (CourseProviderHome) IDOLookup.getHome(
						CourseProvider.class);
			} catch (IDOLookupException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						"Failed to get " + CourseProviderHome.class.getSimpleName() +
						" cause of: ", e);
			}
		}

		return this.courseProviderHome;
	}

//	@Override
//	public void addSeason(SchoolSeason season) throws IDOAddRelationshipException {
//		this.idoAddTo(season);
//	}
//
//	@Override
//	public Collection<SchoolSeason> getSeasons() {
//		try {
//			return super.idoGetRelatedEntities(SchoolSeason.class);
//		} catch (IDORelationshipException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	@Override
//	public void removeSeason(SchoolSeason season) throws IDORemoveRelationshipException {
//		this.idoRemoveFrom(season);
//	}
//
//	@Override
//	public void removeAllSeasons() throws IDORemoveRelationshipException {
//		Collection<SchoolSeason> seasons = getSeasons();
//		if (ListUtil.isEmpty(seasons))
//			return;
//
//		for (SchoolSeason season: seasons) {
//			removeSeason(season);
//		}
//
//		store();
//	}

	private ChildCourseHome childCourseHome = null;

	protected ChildCourseHome getChildCourseHome() {
		if (this.childCourseHome == null) {
			try {
				this.childCourseHome = (ChildCourseHome) IDOLookup
						.getHome(ChildCourse.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + ChildCourseHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.childCourseHome;
	}
}