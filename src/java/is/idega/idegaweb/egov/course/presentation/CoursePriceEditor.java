package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.business.CourseBusinessBean;
import is.idega.idegaweb.egov.course.data.CoursePrice;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.FinderException;

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
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

public class CoursePriceEditor extends SchoolBlock {

	private static final String PARAMETER_ACTION			= "CPE_ac";
	private static final String PARAMETER_COURSE_PRICE_ID	= "CPE_cp";
	private static final String PARAMETER_NAME				= "CPE_n";
	private static final String PARAMETER_DAYS				= "CPE_d";
	private static final String PARAMETER_VALID_FROM		= "CPE_vf";
	private static final String PARAMETER_VALID_TO			= "CPE_vt";
	private static final String PARAMETER_PRICE				= "CPE_p";
	private static final String PARAMETER_COURSE_TYPE_ID	= "CPE_ct";
	private static final String PARAMETER_SCHOOL_TYPE_ID	= "CPE_st";
	
	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	private static final int ACTION_DELETE = 5;
	
	
	protected void init(IWContext iwc) throws Exception {
		super.getParentPage().addJavascriptURL("/dwr/interface/CourseDWRUtil.js");
		super.getParentPage().addJavascriptURL("/dwr/engine.js");
		super.getParentPage().addJavascriptURL("/dwr/util.js");

		
		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n")
		.append("\tDWRUtil.removeAllOptions(\""+PARAMETER_COURSE_TYPE_ID+"\");\n")
		.append("\tDWRUtil.addOptions(\""+PARAMETER_COURSE_TYPE_ID+"\", data);\n")
		.append("}");
		
		StringBuffer script = new StringBuffer();
		script.append("function changeValues() {\n")
		.append("\tvar val = +$(\""+PARAMETER_SCHOOL_TYPE_ID+"\").value;\n")
		.append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(setOptions, val);")
		.append("}");


		super.getParentPage().getAssociatedScript().addFunction("setOptions", script2.toString());
		super.getParentPage().getAssociatedScript().addFunction("changeValues", script.toString());
		
		switch (parseAction(iwc)) {
		case ACTION_VIEW:
			showList(iwc);
			break;

		case ACTION_EDIT:
			Object typePK = iwc.getParameter(PARAMETER_COURSE_PRICE_ID);
			showEditor(iwc, typePK);
			break;

		case ACTION_NEW:
			showEditor(iwc, null);
			break;

		case ACTION_SAVE:
			savePrice(iwc);
			showList(iwc);
			break;

		case ACTION_DELETE:
			getCourseBusiness(iwc).deleteCoursePrice(iwc.getParameter(PARAMETER_COURSE_PRICE_ID));
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
	
	private void savePrice(IWContext iwc) {
		String pk = iwc.getParameter(PARAMETER_COURSE_PRICE_ID);
		String name = iwc.getParameter(PARAMETER_NAME);
		String length = iwc.getParameter(PARAMETER_DAYS);
		String from = iwc.getParameter(PARAMETER_VALID_FROM);
		String to = iwc.getParameter(PARAMETER_VALID_TO);
		String price = iwc.getParameter(PARAMETER_PRICE);
		String schoolTypeID = iwc.getParameter(PARAMETER_SCHOOL_TYPE_ID);
		String courseTypeID = iwc.getParameter(PARAMETER_COURSE_TYPE_ID);
		if (name != null && !"".equals(name.trim())) {
			try {
				getCourseBusiness(iwc).storeCoursePrice(pk, name, Integer.parseInt(length), new IWTimestamp(from).getTimestamp(), new IWTimestamp(to).getTimestamp(), Integer.parseInt(price), courseTypeID, schoolTypeID);
			} catch (RemoteException r) {
				add(r.getMessage());
			}
		}
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

		Collection coursePrices = null;
		try {
			coursePrices = getCourseBusiness(iwc).getAllCoursePrices();
		}
		catch (RemoteException rex) {
			coursePrices = new ArrayList();
		}
		
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setId("name");
		cell.add(new Text(localize("name", "Name")));
		
		cell = row.createHeaderCell();
		cell.setId("length");
		cell.add(new Text(localize("length", "Length")));
		
		cell = row.createHeaderCell();
		cell.setId("category");
		cell.add(new Text(localize("category", "Category")));

		cell = row.createHeaderCell();
		cell.setId("type");
		cell.add(new Text(localize("type", "Type")));

		cell = row.createHeaderCell();
		cell.setId("valid_from");
		cell.add(new Text(localize("valid_from", "Valid from")));

		cell = row.createHeaderCell();
		cell.setId("valid_to");
		cell.add(new Text(localize("valid_to", "Valid to")));

		cell = row.createHeaderCell();
		cell.setId("price");
		cell.add(new Text(localize("price", "Price")));

		cell = row.createHeaderCell();
		cell.setId("edit");
		cell.add(Text.getNonBrakingSpace());

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setId("delete");
		cell.add(Text.getNonBrakingSpace());

		group = table.createBodyRowGroup();
		int iRow = 1;
		java.util.Iterator iter = coursePrices.iterator();
		while (iter.hasNext()) {
			CoursePrice cPrice = (CoursePrice) iter.next();
//			SchoolCategory category = cType.getCategory();
			row = group.createRow();
			
			try {
				Link edit = new Link(getEditIcon(localize("edit", "Edit")));
				edit.addParameter(PARAMETER_COURSE_PRICE_ID, cPrice.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);

				Link delete = new Link(getDeleteIcon(localize("delete", "Delete")));
				delete.addParameter(PARAMETER_COURSE_PRICE_ID, cPrice.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.add(new Text(cPrice.getName()));

				cell = row.createCell();
				cell.setId("length");
				cell.add(new Text(Integer.toString(cPrice.getNumberOfDays())+ " "+localize("days", "days")));
				
				cell = row.createCell();
				cell.setId("category");
				cell.add(new Text(localize(cPrice.getSchoolTypeID().getLocalizationKey(), cPrice.getSchoolTypeID().getName())));

				cell = row.createCell();
				cell.setId("type");
				cell.add(new Text(localize(cPrice.getCourseTypeID().getLocalizationKey(), cPrice.getCourseTypeID().getName())));
				
				cell = row.createCell();
				cell.setId("valid_from");
				cell.add(new Text(new IWTimestamp(cPrice.getValidFrom()).getLocaleDate(iwc.getCurrentLocale())));

				cell = row.createCell();
				cell.setId("valid_to");
				cell.add(new Text(new IWTimestamp(cPrice.getValidTo()).getLocaleDate(iwc.getCurrentLocale())));

				cell = row.createCell();
				cell.setId("price");
				cell.add(new Text(Integer.toString(cPrice.getPrice())));
				
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
	
	public void showEditor(IWContext iwc, Object courseTypePK) throws java.rmi.RemoteException {
		Form form = new Form();
//		form.setID(this.iEditorID);
		form.setStyleClass(STYLENAME_SCHOOL_FORM);
		
		CoursePrice price = getCourseBusiness(iwc).getCoursePrice(courseTypePK);
		
		TextInput inputName = new TextInput(PARAMETER_NAME);
		TextInput inputLength = new TextInput(PARAMETER_DAYS);
		DatePicker inputFrom = new DatePicker(PARAMETER_VALID_FROM);
		DatePicker inputTo = new DatePicker(PARAMETER_VALID_TO);
		TextInput inputPrice = new TextInput(PARAMETER_PRICE);
		
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));
		
		DropdownMenu schoolTypeID = new DropdownMenu(PARAMETER_SCHOOL_TYPE_ID);
		schoolTypeID.setId(PARAMETER_SCHOOL_TYPE_ID);

		DropdownMenu courseTypeID = new DropdownMenu(PARAMETER_COURSE_TYPE_ID);
		courseTypeID.setId(PARAMETER_COURSE_TYPE_ID);
		schoolTypeID.setOnChange("changeValues();");
		
		Collection schoolTypes = null;
		Collection cargoTypes = null;
		try {
			schoolTypes = getBusiness().getSchoolTypeHome().findAllByCategory(CourseBusinessBean.COURSE_SCHOOL_CATEGORY);
			schoolTypeID.addMenuElements(schoolTypes);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		
		if (price != null) {
			inputName.setContent(price.getName());
			inputLength.setContent(Integer.toString(price.getNumberOfDays()));
			inputFrom.setDate(price.getValidFrom());
			inputTo.setDate(price.getValidTo());
			inputPrice.setContent(Integer.toString(price.getPrice()));
			String stID = price.getSchoolTypeID().getPrimaryKey().toString();
			schoolTypeID.setSelectedElement(stID);
			
			cargoTypes = getCourseBusiness(iwc).getCourseTypes(new Integer(stID));
			courseTypeID.addMenuElements(cargoTypes);
			courseTypeID.setSelectedElement(price.getCourseTypeID().getPrimaryKey().toString());

			form.add(new HiddenInput(PARAMETER_COURSE_PRICE_ID, courseTypePK.toString()));
			
		} else {
			cargoTypes = getCourseBusiness(iwc).getCourseTypes((Integer) ((SchoolType)schoolTypes.iterator().next()).getPrimaryKey());
			courseTypeID.addMenuElements(cargoTypes);
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
		layer.setID("category");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("category", "Category"), schoolTypeID);
		layer.add(label);
		layer.add(schoolTypeID);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("type");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("type", "Type"), courseTypeID);
		layer.add(label);
		layer.add(courseTypeID);
		form.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setID("length");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("number_of_days", "Number of days"), inputLength);
		layer.add(label);
		layer.add(inputLength);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("valid_from");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("valid_from", "Valid from"), (TextInput)inputFrom.getPresentationObject(iwc));
		layer.add(label);
		layer.add(inputFrom);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("valid_to");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("valid_to", "Valid to"), (TextInput)inputTo.getPresentationObject(iwc));
		layer.add(label);
		layer.add(inputTo);
		form.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("price");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("price", "Price"), inputPrice);
		layer.add(label);
		layer.add(inputPrice);
		form.add(layer);

		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save")));
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		SubmitButton cancel = (SubmitButton) getButton(new SubmitButton(localize("cancel", "Cancel")));
		cancel.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		form.add(cancel);
		form.add(save);
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
