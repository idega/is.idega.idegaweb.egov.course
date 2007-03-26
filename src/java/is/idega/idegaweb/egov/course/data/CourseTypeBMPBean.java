package is.idega.idegaweb.egov.course.data;


import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class CourseTypeBMPBean extends GenericEntity implements CourseType{

	private static final String TABLE_NAME				= "COU_COURSE_TYPE";
	private static final String COLUMN_NAME				= "NAME";
	private static final String COLUMN_DESCRIPTION		= "DESCRIPTION";
	private static final String COLUMN_LOCALIZATION_KEY	= "LOCALIZATION_KEY";
	private static final String COLUMN_SCHOOL_TYPE_ID	= "SCH_SCHOOL_TYPE_ID";
	private static final String ORDER 					= "TYPE_ORDER";
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "name", String.class, 50);
		addAttribute(COLUMN_DESCRIPTION, "description", String.class);
		addAttribute(COLUMN_LOCALIZATION_KEY, "localization_key" ,String.class, 50);
		addOneToOneRelationship(COLUMN_SCHOOL_TYPE_ID, SchoolType.class);
		addAttribute(ORDER, "type order", Integer.class);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}

	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}

	public void setLocalizationKey(String localizationKey) {
		setColumn(COLUMN_LOCALIZATION_KEY, localizationKey);
	}

	public SchoolType getSchoolType() {
		return (SchoolType) getColumnValue(COLUMN_SCHOOL_TYPE_ID);
	}

	public void setSchoolType(SchoolType type) {
		setColumn(COLUMN_SCHOOL_TYPE_ID, type);
	}

	public void setSchoolTypePK(Object primaryKey) {
		setColumn(COLUMN_SCHOOL_TYPE_ID, primaryKey);
	}

	public int getOrder(){
		return getIntColumnValue(ORDER);
	}

	public void setOrder(int order){
		setColumn(ORDER,order);
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindAllBySchoolType(Object schoolTypePK) throws FinderException, IDORelationshipException {
		Table table = new Table(this);
		Table schoolTypeTable = new Table(SchoolType.class);
		
		Column schoolTypeOrder = new Column(schoolTypeTable, "type_order");
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(new Column(table, COLUMN_SCHOOL_TYPE_ID), MatchCriteria.EQUALS, schoolTypePK));
		query.addOrder(new Order(schoolTypeOrder, true));
		query.addJoin(table, schoolTypeTable);

		return this.idoFindPKsByQuery(query);	
	}
}
