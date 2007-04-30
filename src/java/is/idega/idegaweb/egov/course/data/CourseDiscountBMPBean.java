package is.idega.idegaweb.egov.course.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class CourseDiscountBMPBean extends GenericEntity implements CourseDiscount {

	public static final String ENTITY_NAME = "cou_course_discount";

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TYPE = "discount_type";
	public static final String COLUMN_VALID_FROM = "valid_from";
	public static final String COLUMN_VALID_TO = "valid_to";
	public static final String COLUMN_VALID = "is_valid";
	public static final String COLUMN_DISCOUNT = "discount";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class, 50);
		addAttribute(COLUMN_TYPE, "Type", String.class, 50);
		addAttribute(COLUMN_VALID_FROM, "Valid from", Timestamp.class);
		addAttribute(COLUMN_VALID_TO, "Valid to", Timestamp.class);
		addAttribute(COLUMN_VALID, "Is valid", Boolean.class);
		addAttribute(COLUMN_DISCOUNT, "Discount", Float.class);

		getEntityDefinition().setBeanCachingActiveByDefault(true);
	}

	// Getters
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public String getType() {
		return getStringColumnValue(COLUMN_TYPE);
	}

	public Timestamp getValidFrom() {
		return getTimestampColumnValue(COLUMN_VALID_FROM);
	}

	public Timestamp getValidTo() {
		return getTimestampColumnValue(COLUMN_VALID_TO);
	}

	public float getDiscount() {
		return getFloatColumnValue(COLUMN_DISCOUNT);
	}

	// Setters
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setType(String type) {
		setColumn(COLUMN_TYPE, type);
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

	public void setDiscount(float discount) {
		setColumn(COLUMN_DISCOUNT, discount);
	}

	// Finders
	public Collection ejbFindAll() throws FinderException, IDORelationshipException {
		return ejbFindAll(null, null);
	}

	public Collection ejbFindAll(Date fromDate, Date toDate) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));

		if (fromDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_VALID_FROM), MatchCriteria.GREATEREQUAL, fromDate));
		}
		if (toDate != null) {
			query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_VALID_TO), MatchCriteria.LESSEQUAL, toDate));
		}
		query.addCriteria(new OR(new MatchCriteria(table.getColumn(COLUMN_VALID), MatchCriteria.EQUALS, true), new MatchCriteria(table.getColumn(COLUMN_VALID))));

		return this.idoFindPKsByQuery(query);
	}
}