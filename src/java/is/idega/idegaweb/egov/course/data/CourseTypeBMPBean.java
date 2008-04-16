package is.idega.idegaweb.egov.course.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
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

	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class, 50);
		addAttribute(COLUMN_DESCRIPTION, "Description", String.class);
		addAttribute(COLUMN_LOCALIZATION_KEY, "Localization key", String.class, 50);
		addAttribute(COLUMN_ORDER, "Type order", Integer.class);
		addAttribute(COLUMN_ACCOUNTING_KEY, "Accounting key", String.class, 30);

		addManyToOneRelationship(COLUMN_SCHOOL_TYPE, CourseCategory.class);
		getEntityDefinition().setBeanCachingActiveByDefault(true);
	}

	// Getters
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

	public int getOrder() {
		return getIntColumnValue(COLUMN_ORDER);
	}

	// Setters
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

	public void setOrder(int order) {
		setColumn(COLUMN_ORDER, order);
	}

	// Finders
	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addOrder(new Order(table.getColumn(COLUMN_NAME), true));

		return this.idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllBySchoolType(Object schoolTypePK) throws FinderException, IDORelationshipException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(new Column(table, COLUMN_SCHOOL_TYPE), MatchCriteria.EQUALS, schoolTypePK));
		query.addOrder(new Order(table.getColumn(COLUMN_NAME), true));

		return this.idoFindPKsByQuery(query);
	}
}
