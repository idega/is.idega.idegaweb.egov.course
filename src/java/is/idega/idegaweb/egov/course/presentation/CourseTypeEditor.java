package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.business.CourseBusinessBean;
import is.idega.idegaweb.egov.course.data.CourseType;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import com.idega.block.school.data.SchoolType;
import com.idega.block.school.presentation.SchoolBlock;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

public class CourseTypeEditor extends SchoolBlock {

	private static final String PARAMETER_ACTION			= "CTE_ac";
	private static final String PARAMETER_COURSE_TYPE_PK	= "CTE_ctid";
	private static final String PARAMETER_NAME 				= "CTE_n";
	private static final String PARAMETER_DESCRIPTION		= "CTE_d";
	private static final String PARAMETER_LOCALIZATION_KEY	= "CTE_l";
	private static final String PARAMETER_SCHOOL_TYPE_PK	= "CTE_s";
	
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	private static final int ACTION_DELETE = 5;
	
	
	protected void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
		case ACTION_VIEW:
			showList(iwc);
			break;

		case ACTION_EDIT:
			Object typePK = iwc.getParameter(PARAMETER_COURSE_TYPE_PK);
			showEditor(iwc, typePK);
			break;

		case ACTION_NEW:
			showEditor(iwc, null);
			break;

		case ACTION_SAVE:
			saveType(iwc);
			showList(iwc);
			break;

		case ACTION_DELETE:
			getCourseBusiness(iwc).deleteCourseType(iwc.getParameter(PARAMETER_COURSE_TYPE_PK));
			showList(iwc);
			break;
		}
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}
	
	private void saveType(IWContext iwc) {
		String pk = iwc.getParameter(PARAMETER_COURSE_TYPE_PK);
		String name = iwc.getParameter(PARAMETER_NAME);
		String description = iwc.getParameter(PARAMETER_DESCRIPTION);
		String localizationKey = iwc.getParameter(PARAMETER_LOCALIZATION_KEY);
		String schoolTypePK = iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK);
		
		if (name != null && !"".equals(name.trim())) {
			try {
				getCourseBusiness(iwc).storeCourseType(pk, name, description, localizationKey, schoolTypePK);
			} catch (RemoteException r) {
				add(r.getMessage());
			}
		}
	}
	
	public void showEditor(IWContext iwc, Object courseTypePK) throws java.rmi.RemoteException {
		Form form = new Form();
//		form.setID(this.iEditorID);
		form.setStyleClass(STYLENAME_SCHOOL_FORM);
		
		CourseType type = getCourseBusiness(iwc).getCourseType(courseTypePK);
		
		TextInput inputName = new TextInput(PARAMETER_NAME);
		TextInput inputLocalization = new TextInput(PARAMETER_LOCALIZATION_KEY);
		TextArea inputDesc = new TextArea(PARAMETER_DESCRIPTION);
		Collection schoolTypes = getBusiness().findAllSchoolTypesInCategory(CourseBusinessBean.COURSE_SCHOOL_CATEGORY);
		DropdownMenu inputSchoolTypes = new DropdownMenu(schoolTypes, PARAMETER_SCHOOL_TYPE_PK);
		
		if (type != null) {
			inputName.setContent(type.getName());
			inputLocalization.setContent(type.getLocalizationKey());
			inputDesc.setContent(type.getDatasource());
			SchoolType sType = type.getSchoolType();
			if (sType != null) {
				inputSchoolTypes.setSelectedElement(sType.getPrimaryKey().toString());
			}
			
			form.add(new HiddenInput(PARAMETER_COURSE_TYPE_PK, courseTypePK.toString()));
		}
		
		Layer layer;
		Label label;

		layer = new Layer(Layer.DIV);
		layer.setID("name");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("name", "Name"), inputName);
		layer.add(label);
		layer.add(inputName);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("localization");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("localization_key", "Key"), inputLocalization);
		layer.add(label);
		layer.add(inputLocalization);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("info");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("info", "Info"), inputDesc);
		layer.add(label);
		layer.add(inputDesc);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("schoolTypes");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("category", "Category"), inputSchoolTypes);
		layer.add(label);
		layer.add(inputSchoolTypes);
		form.add(layer);


		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel"), PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));

		form.add(cancel);
		form.add(save);
		add(form);
	}

	public void showList(IWContext iwc) {
		Form form = new Form();
		form.setStyleClass(STYLENAME_SCHOOL_FORM);
		
		Table2 table = new Table2();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth("100%");
		table.setStyleClass(STYLENAME_LIST_TABLE);

		TableColumnGroup columnGroup = table.createColumnGroup();
		TableColumn column = columnGroup.createColumn();
		column.setSpan(5);
		column = columnGroup.createColumn();
		column.setSpan(2);
		column.setWidth("12");

		Collection courseTypes = null;
		try {
			courseTypes = getCourseBusiness(iwc).getAllCourseTypes();
		}
		catch (RemoteException rex) {
			courseTypes = new ArrayList();
		}
		
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setId("name");
		cell.add(new Text(localize("name", "Name")));
		
		cell = row.createHeaderCell();
		cell.setId("description");
		cell.add(new Text(localize("info", "Info")));
		
		cell = row.createHeaderCell();
		cell.setId("schoolCategory");
		cell.add(new Text(localize("category", "Category")));
//
//		cell = row.createHeaderCell();
//		cell.setId("maxAge");
//		cell.add(new Text(localize("max_school_age", "Max school age")));
//
//		cell = row.createHeaderCell();
//		cell.setId("order");
//		cell.add(new Text(localize("order", "Order")));

		cell = row.createHeaderCell();
		cell.setId("edit");
		cell.add(Text.getNonBrakingSpace());

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setId("delete");
		cell.add(Text.getNonBrakingSpace());

		group = table.createBodyRowGroup();
		int iRow = 1;
		java.util.Iterator iter = courseTypes.iterator();
		while (iter.hasNext()) {
			CourseType cType = (CourseType) iter.next();
//			SchoolCategory category = cType.getCategory();
			row = group.createRow();
			
			try {
				Link edit = new Link(getEditIcon(localize("edit", "Edit")));
				edit.addParameter(PARAMETER_COURSE_TYPE_PK, cType.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);

				Link delete = new Link(getDeleteIcon(localize("delete", "Delete")));
				delete.addParameter(PARAMETER_COURSE_TYPE_PK, cType.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.add(new Text(cType.getName()));

				cell = row.createCell();
				cell.setId("description");
				cell.add(new Text(cType.getDescription()));
				
				cell = row.createCell();
				cell.setId("category");
				SchoolType sType = cType.getSchoolType();
				if (sType != null) {
					cell.add(new Text(localize(sType.getLocalizationKey(), sType.getName())));
				}
//				
//				cell = row.createCell();
//				cell.setId("maxAge");
//				cell.add(new Text(cType.getMaxSchoolAge() > 0 ? String.valueOf(cType.getMaxSchoolAge()) : "-"));
//				
//				cell = row.createCell();
//				cell.setId("order");
//				cell.add(new Text(cType.getOrder() > 0 ? String.valueOf(cType.getOrder()) : "-"));
				
				cell = row.createCell();
				cell.setId("edit");
				cell.add(edit);
				
				cell = row.createCell();
				cell.setId("delete");
				cell.setStyleClass("lastColumn");
				cell.add(delete);

				if (iRow % 2 == 0) {
					row.setStyleClass(STYLENAME_LIST_TABLE_EVEN_ROW);
				}
				else {
					row.setStyleClass(STYLENAME_LIST_TABLE_ODD_ROW);
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			iRow++;
		}
		form.add(table);
		form.add(new Break());

		SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("type.new", "New type"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
		form.add(newLink);

		add(form);
	}
	
	public CourseBusiness getCourseBusiness(IWContext iwc) {
		try {
			return (CourseBusiness) IBOLookup.getServiceInstance(iwc, CourseBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}
