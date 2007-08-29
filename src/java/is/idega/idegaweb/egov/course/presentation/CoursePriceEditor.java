package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CourseType;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolType;
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
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

public class CoursePriceEditor extends CourseBlock {

	private static final String PARAMETER_ACTION = "CPE_ac";
	private static final String PARAMETER_COURSE_PRICE_ID = "CPE_cp";
	private static final String PARAMETER_NAME = "CPE_n";
	private static final String PARAMETER_DAYS = "CPE_d";
	private static final String PARAMETER_VALID_FROM = "CPE_vf";
	private static final String PARAMETER_VALID_TO = "CPE_vt";
	private static final String PARAMETER_PRICE = "CPE_p";
	private static final String PARAMETER_PRE_CARE_PRICE = "CPE_prcp";
	private static final String PARAMETER_POST_CARE_PRICE = "CPE_pocp";
	private static final String PARAMETER_SCHOOL_AREA_PK = "CPE_sa";
	private static final String PARAMETER_COURSE_TYPE_ID = "CPE_ct";
	private static final String PARAMETER_SCHOOL_TYPE_ID = "CPE_st";

	private static final String PARAMETER_FROM = "prm_from";
	private static final String PARAMETER_TO = "prm_to";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	private static final int ACTION_DELETE = 5;

	public void present(IWContext iwc) {
		try {
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
					if (!getCourseBusiness(iwc).deleteCoursePrice(iwc.getParameter(PARAMETER_COURSE_PRICE_ID))) {
						getParentPage().setAlertOnLoad(getResourceBundle().getLocalizedString("course_price.remove_error", "You can not remove a course price that has courses attached to it."));
					}
					showList(iwc);
					break;
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
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
		int preCarePrice = iwc.isParameterSet(PARAMETER_PRE_CARE_PRICE) ? Integer.parseInt(iwc.getParameter(PARAMETER_PRE_CARE_PRICE)) : 0;
		int postCarePrice = iwc.isParameterSet(PARAMETER_POST_CARE_PRICE) ? Integer.parseInt(iwc.getParameter(PARAMETER_POST_CARE_PRICE)) : 0;
		String schoolAreaPK = iwc.isParameterSet(PARAMETER_SCHOOL_AREA_PK) ? iwc.getParameter(PARAMETER_SCHOOL_AREA_PK) : null;
		String courseTypeID = iwc.getParameter(PARAMETER_COURSE_TYPE_ID);
		if (name != null && !"".equals(name.trim())) {
			try {
				getCourseBusiness(iwc).storeCoursePrice(pk, name, Integer.parseInt(length), new IWTimestamp(from).getTimestamp(), new IWTimestamp(to).getTimestamp(), Integer.parseInt(price), preCarePrice, postCarePrice, schoolAreaPK, courseTypeID);
			}
			catch (CreateException ce) {
				add(ce.getMessage());
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
	}

	public void showList(IWContext iwc) {
		Form form = new Form();
		form.setStyleClass("adminForm");

		try {
			form.add(getNavigation(iwc));
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		Table2 table = new Table2();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth("100%");
		table.setStyleClass("adminTable");
		table.setStyleClass("ruler");

		TableColumnGroup columnGroup = table.createColumnGroup();
		TableColumn column = columnGroup.createColumn();
		column.setSpan(5);
		column = columnGroup.createColumn();
		column.setSpan(2);
		column.setWidth("12");

		Collection coursePrices = null;
		try {
			Date fromDate = iwc.isParameterSet(PARAMETER_FROM) ? new IWTimestamp(iwc.getParameter(PARAMETER_FROM)).getDate() : null;
			Date toDate = iwc.isParameterSet(PARAMETER_TO) ? new IWTimestamp(iwc.getParameter(PARAMETER_TO)).getDate() : null;
			coursePrices = getCourseBusiness(iwc).getCoursePrices(fromDate, toDate);
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
		cell.setId("area");
		cell.add(new Text(localize("school_area", "School area")));

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
		cell.add(new Text(localize("edit", "Edit")));

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setId("delete");
		cell.add(new Text(localize("delete", "Delete")));

		group = table.createBodyRowGroup();
		int iRow = 1;
		java.util.Iterator iter = coursePrices.iterator();
		while (iter.hasNext()) {
			CoursePrice cPrice = (CoursePrice) iter.next();
			CourseType type = cPrice.getCourseType();
			SchoolType sType = type.getSchoolType();
			SchoolArea area = cPrice.getSchoolArea();
			row = group.createRow();

			try {
				Link edit = new Link(getBundle().getImage("edit.png", localize("edit", "Edit")));
				edit.addParameter(PARAMETER_COURSE_PRICE_ID, cPrice.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);

				Link delete = new Link(getBundle().getImage("delete.png", localize("delete", "Delete")));
				delete.addParameter(PARAMETER_COURSE_PRICE_ID, cPrice.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);
				delete.setClickConfirmation(getResourceBundle().getLocalizedString("course_price.confirm_delete", "Are you sure you want to delete the price selected?"));

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.add(new Text(cPrice.getName()));

				cell = row.createCell();
				cell.setId("length");
				cell.add(new Text(Integer.toString(cPrice.getNumberOfDays()) + " " + localize("days", "days")));

				cell = row.createCell();
				cell.setId("area");
				if (area != null) {
					cell.add(new Text(area.getSchoolAreaName()));
				}
				else {
					cell.add(new Text("-"));
				}

				cell = row.createCell();
				cell.setId("category");
				if (sType.getLocalizationKey() != null) {
					cell.add(new Text(localize(sType.getLocalizationKey(), sType.getName())));
				}
				else {
					cell.add(new Text(sType.getName()));
				}

				cell = row.createCell();
				cell.setId("type");
				if (type.getLocalizationKey() != null) {
					cell.add(new Text(localize(type.getLocalizationKey(), type.getName())));
				}
				else {
					cell.add(new Text(type.getName()));
				}

				cell = row.createCell();
				cell.setId("valid_from");
				cell.add(new Text(new IWTimestamp(cPrice.getValidFrom()).getDateString("dd.MM.yyyy", iwc.getCurrentLocale())));

				cell = row.createCell();
				cell.setId("valid_to");
				cell.add(new Text(new IWTimestamp(cPrice.getValidTo()).getDateString("dd.MM.yyyy", iwc.getCurrentLocale())));

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
					row.setStyleClass("even");
				}
				else {
					row.setStyleClass("odd");
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			iRow++;
		}
		form.add(table);

		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonLayer");
		form.add(buttonLayer);

		if (getBackPage() != null) {
			GenericButton back = new GenericButton(localize("back", "Back"));
			back.setPageToOpen(getBackPage());
			buttonLayer.add(back);
		}

		SubmitButton newLink = new SubmitButton(localize("new_price", "New price"), PARAMETER_ACTION, String.valueOf(ACTION_NEW));
		buttonLayer.add(newLink);

		add(form);
	}

	private Layer getNavigation(IWContext iwc) throws RemoteException {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("formSection");

		IWTimestamp from = new IWTimestamp();
		from.setDay(1);
		from.setMonth(1);

		IWTimestamp to = new IWTimestamp();
		to.setDay(1);
		to.setMonth(1);
		to.addYears(1);
		to.addDays(-1);

		DatePicker fromDate = new DatePicker(PARAMETER_FROM);
		fromDate.setDate(from.getDate());
		fromDate.keepStatusOnAction(true);

		DatePicker toDate = new DatePicker(PARAMETER_TO);
		toDate.setDate(to.getDate());
		toDate.keepStatusOnAction(true);

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Label label = new Label(getResourceBundle().getLocalizedString("from_date", "From date"), (TextInput) fromDate.getPresentationObject(iwc));
		formItem.add(label);
		formItem.add(fromDate);
		layer.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(getResourceBundle().getLocalizedString("to_date", "To date"), (TextInput) toDate.getPresentationObject(iwc));
		formItem.add(label);
		formItem.add(toDate);
		layer.add(formItem);

		SubmitButton fetch = new SubmitButton(getResourceBundle().getLocalizedString("get", "Get"));
		fetch.setStyleClass("indentedButton");
		fetch.setStyleClass("button");
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.add(fetch);
		layer.add(formItem);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		layer.add(clearLayer);

		return layer;
	}

	public void showEditor(IWContext iwc, Object courseTypePK) throws java.rmi.RemoteException {
		super.getParentPage().addJavascriptURL("/dwr/interface/CourseDWRUtil.js");
		super.getParentPage().addJavascriptURL("/dwr/engine.js");
		super.getParentPage().addJavascriptURL("/dwr/util.js");

		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n").append("\tDWRUtil.removeAllOptions(\"" + PARAMETER_COURSE_TYPE_ID + "\");\n").append("\tDWRUtil.addOptions(\"" + PARAMETER_COURSE_TYPE_ID + "\", data);\n").append("}");

		StringBuffer script = new StringBuffer();
		script.append("function changeValues() {\n").append("\tvar val = +$(\"" + PARAMETER_SCHOOL_TYPE_ID + "\").value;\n").append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(val, '" + iwc.getCurrentLocale().getCountry() + "', setOptions);").append("}");

		super.getParentPage().getAssociatedScript().addFunction("setOptions", script2.toString());
		super.getParentPage().getAssociatedScript().addFunction("changeValues", script.toString());

		Form form = new Form();
		form.setStyleClass("adminForm");
		form.maintainParameter(PARAMETER_FROM);
		form.maintainParameter(PARAMETER_TO);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(localize("course_price.help_text", "Please fill in all the information and then click 'Save'.  Please note that the fields for care must contain a price but can be left empty if no price should be used.")));
		section.add(helpLayer);

		CoursePrice price = getCourseBusiness(iwc).getCoursePrice(courseTypePK);

		TextInput inputName = new TextInput(PARAMETER_NAME);
		TextInput inputLength = new TextInput(PARAMETER_DAYS);
		DatePicker inputFrom = new DatePicker(PARAMETER_VALID_FROM);
		DatePicker inputTo = new DatePicker(PARAMETER_VALID_TO);
		TextInput inputPrice = new TextInput(PARAMETER_PRICE);
		TextInput inputPreCarePrice = new TextInput(PARAMETER_PRE_CARE_PRICE);
		TextInput inputPostCarePrice = new TextInput(PARAMETER_POST_CARE_PRICE);

		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));

		DropdownMenu schoolArea = new DropdownMenu(PARAMETER_SCHOOL_AREA_PK);
		schoolArea.addMenuElementFirst("", localize("all_school_areas", "All school areas"));
		Collection areas = getCourseBusiness(iwc).getSchoolAreas();
		schoolArea.addMenuElements(areas);

		DropdownMenu schoolTypeID = new DropdownMenu(PARAMETER_SCHOOL_TYPE_ID);
		schoolTypeID.setId(PARAMETER_SCHOOL_TYPE_ID);

		DropdownMenu courseTypeID = new DropdownMenu(PARAMETER_COURSE_TYPE_ID);
		courseTypeID.setId(PARAMETER_COURSE_TYPE_ID);
		schoolTypeID.setOnChange("changeValues();");

		Collection cargoTypes = null;
		Collection schoolTypes = getCourseBusiness(iwc).getAllSchoolTypes();
		schoolTypeID.addMenuElements(schoolTypes);

		if (price != null) {
			CourseType type = price.getCourseType();
			SchoolType sType = type.getSchoolType();
			SchoolArea area = price.getSchoolArea();

			inputName.setContent(price.getName());
			inputLength.setContent(Integer.toString(price.getNumberOfDays()));
			inputFrom.setDate(price.getValidFrom());
			inputTo.setDate(price.getValidTo());
			inputPrice.setContent(Integer.toString(price.getPrice()));
			inputPreCarePrice.setContent(price.getPreCarePrice() > 0 ? Integer.toString(price.getPreCarePrice()) : Integer.toString(0));
			inputPostCarePrice.setContent(price.getPostCarePrice() > 0 ? Integer.toString(price.getPostCarePrice()) : Integer.toString(0));
			String stID = sType.getPrimaryKey().toString();
			schoolTypeID.setSelectedElement(stID);

			if (area != null) {
				schoolArea.setSelectedElement(area.getPrimaryKey().toString());
			}

			cargoTypes = getCourseBusiness(iwc).getCourseTypes(new Integer(stID));
			courseTypeID.addMenuElements(cargoTypes);
			courseTypeID.setSelectedElement(type.getPrimaryKey().toString());

			form.add(new HiddenInput(PARAMETER_COURSE_PRICE_ID, courseTypePK.toString()));

		}
		else {
			cargoTypes = getCourseBusiness(iwc).getCourseTypes((Integer) ((SchoolType) schoolTypes.iterator().next()).getPrimaryKey());
			courseTypeID.addMenuElements(cargoTypes);
		}

		Layer layer;
		Label label;

		layer = new Layer(Layer.DIV);
		layer.setID("name");
		layer.setStyleClass("formItem");
		label = new Label(localize("name", "Name"), inputName);
		layer.add(label);
		layer.add(inputName);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("category");
		layer.setStyleClass("formItem");
		label = new Label(localize("category", "Category"), schoolTypeID);
		layer.add(label);
		layer.add(schoolTypeID);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("type");
		layer.setStyleClass("formItem");
		label = new Label(localize("type", "Type"), courseTypeID);
		layer.add(label);
		layer.add(courseTypeID);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("area");
		layer.setStyleClass("formItem");
		label = new Label(localize("school_area", "School area"), schoolArea);
		layer.add(label);
		layer.add(schoolArea);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("length");
		layer.setStyleClass("formItem");
		label = new Label(localize("number_of_days", "Number of days"), inputLength);
		layer.add(label);
		layer.add(inputLength);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("valid_from");
		layer.setStyleClass("formItem");
		label = new Label(localize("valid_from", "Valid from"), (TextInput) inputFrom.getPresentationObject(iwc));
		layer.add(label);
		layer.add(inputFrom);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("valid_to");
		layer.setStyleClass("formItem");
		label = new Label(localize("valid_to", "Valid to"), (TextInput) inputTo.getPresentationObject(iwc));
		layer.add(label);
		layer.add(inputTo);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("price");
		layer.setStyleClass("formItem");
		label = new Label(localize("price", "Price"), inputPrice);
		layer.add(label);
		layer.add(inputPrice);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("preCarePrice");
		layer.setStyleClass("formItem");
		label = new Label(localize("pre_care_price", "Pre care price"), inputPreCarePrice);
		layer.add(label);
		layer.add(inputPreCarePrice);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("postCarePrice");
		layer.setStyleClass("formItem");
		label = new Label(localize("post_care_price", "Post care price"), inputPostCarePrice);
		layer.add(label);
		layer.add(inputPostCarePrice);
		section.add(layer);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		section.add(clearLayer);

		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonLayer");
		form.add(buttonLayer);

		SubmitButton save = new SubmitButton(localize("save", "Save"));
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));

		SubmitButton cancel = new SubmitButton(localize("cancel", "Cancel"));
		cancel.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));

		buttonLayer.add(cancel);
		buttonLayer.add(save);

		add(form);
	}

	public CourseBusiness getCourseBusiness(IWContext iwc) {
		try {
			return (CourseBusiness) IBOLookup.getServiceInstance(iwc, CourseBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}