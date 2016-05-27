package is.idega.idegaweb.egov.course.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class CourseTypeBMPBean extends GenericEntity implements CourseType {

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
	private static final String COLUMN_REGISTRATION_METHOD = "REGISTRATION_METHOD";

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
		addAttribute(COLUMN_REGISTRATION_METHOD, "Registration method", String.class, 50);

		addManyToOneRelationship(COLUMN_SCHOOL_TYPE, CourseCategory.class);
		getEntityDefinition().setBeanCachingActiveByDefault(true);
	}

	// Getters
	@Override
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	@Override
	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}

	@Override
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}

	@Override
	public CourseCategory getCourseCategory() {
		return (CourseCategory) getColumnValue(COLUMN_SCHOOL_TYPE);
	}

	@Override
	public String getAccountingKey() {
		return getStringColumnValue(COLUMN_ACCOUNTING_KEY);
	}

	@Override
	public String getAbbreviation() {
		return getStringColumnValue(COLUMN_ABBREVIATION);
	}

	@Override
	public int getOrder() {
		return getIntColumnValue(COLUMN_ORDER);
	}

	@Override
	public boolean showAbbreviation() {
		return getBooleanColumnValue(COLUMN_SHOW_ABBREVIATION, false);
	}

	@Override
	public boolean isDisabled() {
		return getBooleanColumnValue(COLUMN_DISABLED, false);
	}

	@Override
	public String getRegistrationMethod() {
		return getStringColumnValue(COLUMN_REGISTRATION_METHOD);
	}

	// Setters
	@Override
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	@Override
	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	@Override
	public void setLocalizationKey(String localizationKey) {
		setColumn(COLUMN_LOCALIZATION_KEY, localizationKey);
	}

	@Override
	public void setCourseCategory(CourseCategory category) {
		setColumn(COLUMN_SCHOOL_TYPE, category);
	}

	@Override
	public void setAccountingKey(String key) {
		setColumn(COLUMN_ACCOUNTING_KEY, key);
	}

	@Override
	public void setAbbreviation(String abbreviation) {
		setColumn(COLUMN_ABBREVIATION, abbreviation);
	}

	@Override
	public void setOrder(int order) {
		setColumn(COLUMN_ORDER, order);
	}

	@Override
	public void setShowAbbreviation(boolean showAbbreviation) {
		setColumn(COLUMN_SHOW_ABBREVIATION, showAbbreviation);
	}

	@Override
	public void setDisabled(boolean disabled) {
		setColumn(COLUMN_DISABLED, disabled);
	}

	@Override
	public void setRegistrationMethod(String registrationMethod) {
		setColumn(COLUMN_REGISTRATION_METHOD, registrationMethod);
	}

	// Finders
	public Collection ejbFindAll(boolean valid) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		if (valid) {
			query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_DISABLED), false), new MatchCriteria(table.getColumn(COLUMN_DISABLED), MatchCriteria.EQUALS, false)));
		}
		query.addOrder(new Order(table.getColumn(COLUMN_NAME), true));

		return this.idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllBySchoolType(Object schoolTypePK, boolean valid) throws FinderException, IDORelationshipException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(new Column(table, COLUMN_SCHOOL_TYPE), MatchCriteria.EQUALS, schoolTypePK));
		if (valid) {
			query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_DISABLED), false), new MatchCriteria(table.getColumn(COLUMN_DISABLED), MatchCriteria.EQUALS, false)));
		}
		query.addOrder(new Order(table.getColumn(COLUMN_NAME), true));

		return this.idoFindPKsByQuery(query);
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