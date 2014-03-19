package is.idega.idegaweb.egov.course.data;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;

public class CourseTypeBMPBean extends GenericEntity implements CourseType {

	private static final long serialVersionUID = 6935005548199199138L;
	private static final String TABLE_NAME = "COU_COURSE_TYPE";
	private static final String COLUMN_NAME = "NAME";
	private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
	private static final String COLUMN_LOCALIZATION_KEY = "LOCALIZATION_KEY";
	private static final String COLUMN_SCHOOL_TYPE = "SCH_SCHOOL_TYPE_ID";
	private static final String COLUMN_ORDER = "TYPE_ORDER";
	private static final String COLUMN_ACCOUNTING_KEY = "ACCOUNTING_KEY";
	private static final String COLUMN_ABBREVIATION = "ABBREVIATION";
	private static final String COLUMN_SHOW_ABBREVIATION = "SHOW_ABBREVIATION";
	private static final String COLUMN_DISABLED = "IS_DISABLED";

	@Override
	public String getEntityName() {
		return TABLE_NAME;
	}

	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class, 50);
		addAttribute(COLUMN_DESCRIPTION, "Description", String.class);
		addAttribute(COLUMN_LOCALIZATION_KEY, "Localization key", String.class, 50);
		addAttribute(COLUMN_ORDER, "Type order", Integer.class);
		addAttribute(COLUMN_ACCOUNTING_KEY, "Accounting key", String.class, 30);
		addAttribute(COLUMN_ABBREVIATION, "Abbreviation", String.class, 1);
		addAttribute(COLUMN_SHOW_ABBREVIATION, "Show abbreviation", Boolean.class);
		addAttribute(COLUMN_DISABLED, "Is disabled", Boolean.class);

		addManyToOneRelationship(COLUMN_SCHOOL_TYPE, CourseCategory.class);
		getEntityDefinition().setBeanCachingActiveByDefault(true);
	}

	// Getters
	@Override
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}

	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}

	public CourseCategory getCourseCategory() {
		return (CourseCategory) getColumnValue(COLUMN_SCHOOL_TYPE);
	}

	public String getAccountingKey() {
		return getStringColumnValue(COLUMN_ACCOUNTING_KEY);
	}

	public String getAbbreviation() {
		return getStringColumnValue(COLUMN_ABBREVIATION);
	}

	public int getOrder() {
		return getIntColumnValue(COLUMN_ORDER);
	}

	public boolean showAbbreviation() {
		return getBooleanColumnValue(COLUMN_SHOW_ABBREVIATION, false);
	}

	public boolean isDisabled() {
		return getBooleanColumnValue(COLUMN_DISABLED, false);
	}

	// Setters
	@Override
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	public void setLocalizationKey(String localizationKey) {
		setColumn(COLUMN_LOCALIZATION_KEY, localizationKey);
	}

	public void setCourseCategory(CourseCategory category) {
		setColumn(COLUMN_SCHOOL_TYPE, category);
	}

	public void setAccountingKey(String key) {
		setColumn(COLUMN_ACCOUNTING_KEY, key);
	}

	public void setAbbreviation(String abbreviation) {
		setColumn(COLUMN_ABBREVIATION, abbreviation);
	}

	public void setOrder(int order) {
		setColumn(COLUMN_ORDER, order);
	}

	public void setShowAbbreviation(boolean showAbbreviation) {
		setColumn(COLUMN_SHOW_ABBREVIATION, showAbbreviation);
	}
	
	public void setDisabled(boolean disabled) {
		setColumn(COLUMN_DISABLED, disabled);
	}

	/**
	 * 
	 * @param valid is not {@link CourseType#isDisabled()};
	 * @return {@link Collection} of {@link CourseType#getPrimaryKey()} 
	 * by criteria or {@link Collections#emptyList()} on fialure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindAll(boolean valid) {
		IDOQuery query = idoQuery();
		query.useDefaultAlias = Boolean.TRUE;
		query.appendSelectAllFrom(this);

		if (valid) {
			query.appendWhereEquals(
					IDOQuery.ENTITY_TO_SELECT + CoreConstants.DOT + COLUMN_DISABLED, 
					"\'N\'");
			query.appendOrIsNull(COLUMN_DISABLED);
		}

		query.appendOrderBy(IDOQuery.ENTITY_TO_SELECT + CoreConstants.DOT + COLUMN_NAME);

		try {
			return idoFindPKsByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get primary keys for " + CourseType.class.getSimpleName() + 
					" by query: '" + query + "'");
		}

		return Collections.emptyList();
	}

	/**
	 * 
	 * @param courseCategories to search by, not <code>null</code>;
	 * @param valid tells that {@link CourseCategory} is not disabled;
	 * @return {@link Collection} of {@link CourseType#getPrimaryKey()} or
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<Object> ejbFindAllByCategories(
			Collection<? extends CourseCategory> courseCategories, 
			boolean valid) {
		if (ListUtil.isEmpty(courseCategories)) {
			return Collections.emptyList();
		}

		IDOQuery query = idoQuery();
		query.useDefaultAlias = Boolean.TRUE;
		query.appendSelectAllFrom(this);
		query.appendJoinOn(courseCategories);

		if (valid) {
			query.appendWhereEquals(
					IDOQuery.ENTITY_TO_SELECT + CoreConstants.DOT + COLUMN_DISABLED, 
					"\'N\'");
			query.appendOrIsNull(COLUMN_DISABLED);
		}

		query.appendOrderBy(IDOQuery.ENTITY_TO_SELECT + CoreConstants.DOT + COLUMN_NAME);

		try {
			return idoFindPKsByQuery(query);
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, 
					"Failed to get primary keys for " + getClass().getSimpleName() + 
					" by query: '" + query.toString());
		}

		return Collections.emptyList();
	}

	public Object ejbFindByAbbreviation(String abbreviation) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(new Column(table, COLUMN_ABBREVIATION), MatchCriteria.EQUALS, abbreviation));

		return this.idoFindOnePKByQuery(query);
	}
	
	public Object ejbFindByName(String name) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(new Column(table, COLUMN_NAME), MatchCriteria.EQUALS, name));

		return this.idoFindOnePKByQuery(query);
	}
}