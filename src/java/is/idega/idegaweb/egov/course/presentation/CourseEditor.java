package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.business.CourseBusinessBean;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CourseType;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolType;
import com.idega.block.school.presentation.SchoolBlock;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

public class CourseEditor extends SchoolBlock {

	private static final String PARAMETER_ACTION			= "CE_ac";
	private static final String PARAMETER_COURSE_PK			= "CE_pcpk";
	private static final String PARAMETER_COURSE_TYPE_ID	= "CP_ct";
	private static final String PARAMETER_SCHOOL_TYPE_ID	= "CP_st";
	private static final String PARAMETER_COURSE_PRICE_ID	= "CP_cp";
	private static final String PARAMETER_NAME				= "CP_n";
	private static final String PARAMETER_VALID_FROM		= "CP_vf";
	private static final String PARAMETER_ACCOUNTING_KEY	= "CP_ak";
	private static final String PARAMETER_USER				= "CP_us";
	private static final String PARAMETER_YEAR_FROM			= "CP_yf";
	private static final String PARAMETER_YEAR_TO			= "CP_yt";
	private static final String PARAMETER_MAX_PER			= "CP_mp";

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
		.append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(setOptions, val);\n")
		.append("}");

		StringBuffer script3 = new StringBuffer();
		script3.append("function setOptionsPrice(data) {\n")
		.append("\tvar isEmpty = true;\n")
		.append("\tfor (var prop in data) { isEmpty = false } \n")
		.append("\tDWRUtil.removeAllOptions(\""+PARAMETER_COURSE_PRICE_ID+"\");\n")
		.append("\tDWRUtil.addOptions(\""+PARAMETER_COURSE_PRICE_ID+"\", data);\n")
		.append("\tvar savebtn = $(\"SAVE_BTN_ID\");\n")
		.append("\tif (isEmpty == true) {\n")
		.append("\t\tDWRUtil.addOptions(\""+PARAMETER_COURSE_PRICE_ID+"\",['"+localize("try_another_date", "Try another date")+"...']);\n")
		.append("\t\tsavebtn.disabled=true;\n")
		.append("\t\t$(\""+PARAMETER_COURSE_PRICE_ID+"\").disabled=true;\n")
		.append("\t} else {\n")
		.append("\t\tsavebtn.disabled=false;\n")
		.append("\t\t$(\""+PARAMETER_COURSE_PRICE_ID+"\").disabled=false;\n")
		.append("\t}\n")
		.append("}");
		
		StringBuffer script4 = new StringBuffer();
		script4.append("function changeValuesPrice() {\n")
		.append("\tvar date = DWRUtil.getValue(\""+PARAMETER_VALID_FROM+"\");\n")
		.append("\tvar val = DWRUtil.getValue(\""+PARAMETER_COURSE_TYPE_ID+"\");\n")
		.append("\tvar TEST = CourseDWRUtil.getCoursePricesDWR(setOptionsPrice, date, val);")
		.append("}");


		super.getParentPage().getAssociatedScript().addFunction("setOptions", script2.toString());
		super.getParentPage().getAssociatedScript().addFunction("changeValues", script.toString());
		super.getParentPage().getAssociatedScript().addFunction("setOptionsPrice", script3.toString());
		super.getParentPage().getAssociatedScript().addFunction("changeValuesPrice", script4.toString());
		
		
		switch (parseAction(iwc)) {
		case ACTION_VIEW:
			showList(iwc);
			break;

		case ACTION_EDIT:
			Object coursePK = iwc.getParameter(PARAMETER_COURSE_PK);
			showEditor(iwc, coursePK);
			break;

		case ACTION_NEW:
			showEditor(iwc, null);
			break;

		case ACTION_SAVE:
			saveCourse(iwc);
			showList(iwc);
			break;

		case ACTION_DELETE:
			getCourseBusiness(iwc).deleteCourseType(iwc.getParameter(PARAMETER_COURSE_PK));
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
	
	public void saveCourse(IWContext iwc) {
		String pk = iwc.getParameter(PARAMETER_COURSE_PK);
		String name = iwc.getParameter(PARAMETER_NAME);
		String schoolTypePK = iwc.getParameter(PARAMETER_SCHOOL_TYPE_ID);
		String courseTypePK = iwc.getParameter(PARAMETER_COURSE_TYPE_ID);
		String coursePricePK = iwc.getParameter(PARAMETER_COURSE_PRICE_ID);
		String accountingKey = iwc.getParameter(PARAMETER_ACCOUNTING_KEY);
		String sStartDate = iwc.getParameter(PARAMETER_VALID_FROM);
		String yearFrom = iwc.getParameter(PARAMETER_YEAR_FROM);
		String yearTo = iwc.getParameter(PARAMETER_YEAR_TO);
		String max = iwc.getParameter(PARAMETER_MAX_PER);
		
		try {
			IWTimestamp startDate = new IWTimestamp(sStartDate);
			int birthYearFrom = Integer.parseInt(yearFrom);
			int birthYearTo = Integer.parseInt(yearTo);
			int maxPer = Integer.parseInt(max);
			getCourseBusiness(iwc).storeCourse(pk, name, schoolTypePK, courseTypePK, coursePricePK, startDate, accountingKey, birthYearFrom, birthYearTo, maxPer);
		} catch (Exception e) {
			e.printStackTrace();
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

		Collection courses = null;
		try {
			courses = getCourseBusiness(iwc).getAllCourses();
		}
		catch (RemoteException rex) {
			courses = new ArrayList();
		}
		
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setId("category");
		cell.setStyleClass("firstColumn");
		cell.add(new Text(localize("category", "Category")));

		cell = row.createHeaderCell();
		cell.setId("type");
		cell.add(new Text(localize("type", "Type")));

		cell = row.createHeaderCell();
		cell.setId("name");
		cell.add(new Text(localize("name", "Name")));

		cell = row.createHeaderCell();
		cell.setId("yearFrom");
		cell.add(new Text(localize("from", "From")));

		cell = row.createHeaderCell();
		cell.setId("yearTo");
		cell.add(new Text(localize("to", "To")));

		cell = row.createHeaderCell();
		cell.setId("startDate");
		cell.add(new Text(localize("date_from", "Date from")));

		cell = row.createHeaderCell();
		cell.setId("endDate");
		cell.add(new Text(localize("date_to", "Date to")));

		cell = row.createHeaderCell();
		cell.setId("max");
		cell.add(new Text(localize("max", "Max")));

		cell = row.createHeaderCell();
		cell.setId("edit");
		cell.add(Text.getNonBrakingSpace());

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setId("delete");
		cell.add(Text.getNonBrakingSpace());

		group = table.createBodyRowGroup();
		int iRow = 1;
		java.util.Iterator iter = courses.iterator();
		while (iter.hasNext()) {
			Course course = (Course) iter.next();
//			SchoolCategory category = cType.getCategory();
			row = group.createRow();
			
			try {
				Link edit = new Link(getEditIcon(localize("edit", "Edit")));
				edit.addParameter(PARAMETER_COURSE_PK, course.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);

				Link delete = new Link(getDeleteIcon(localize("delete", "Delete")));
				delete.addParameter(PARAMETER_COURSE_PK, course.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.setId("category");
				SchoolType sType = course.getSchoolType();
				if (sType != null) {
					cell.add(new Text(localize(sType.getLocalizationKey(), sType.getName())));
				}

				cell = row.createCell();
				cell.setStyleClass("type");
				CourseType cType = course.getCourseType();
				cell.add(new Text(localize(cType.getLocalizationKey(), cType.getName())));

				cell = row.createCell();
				cell.setStyleClass("name");
				cell.add(new Text(course.getName()));

				cell = row.createCell();
				cell.setStyleClass("yearFrom");
				cell.add(new Text(String.valueOf(course.getBirthyearFrom())));
				
				cell = row.createCell();
				cell.setStyleClass("yearTo");
				cell.add(new Text(String.valueOf(course.getBirthyearTo())));
				
				cell = row.createCell();
				cell.setStyleClass("startDate");
				Timestamp start = course.getStartDate();
				if (start != null) {
					cell.add(new Text(new IWTimestamp(start).getLocaleDate(iwc.getLocale())));
				}

				cell = row.createCell();
				cell.setStyleClass("endDate");
				CoursePrice price = course.getPrice();
				if (start != null && price != null) {
					IWTimestamp toDate = new IWTimestamp(start);
					toDate.addDays(price.getNumberOfDays());
					cell.add(new Text(toDate.getLocaleDate(iwc.getLocale())));
				}
				
				cell = row.createCell();
				cell.setStyleClass("max");
				cell.add(new Text(String.valueOf(course.getMax())));
				
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

		SubmitButton newLink = (SubmitButton) getButton(new SubmitButton(localize("course.new", "New course"), PARAMETER_ACTION, String.valueOf(ACTION_NEW)));
		form.add(newLink);

		add(form);
	}
	
	public void showEditor(IWContext iwc, Object coursePK) throws java.rmi.RemoteException {
		Form form = new Form();
		form.setStyleClass(STYLENAME_SCHOOL_FORM);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));
		
		Course course = getCourseBusiness(iwc).getCourse(coursePK);
		
		TextInput inputName = new TextInput(PARAMETER_NAME);
		DatePicker inputFrom = new DatePicker(PARAMETER_VALID_FROM);
		TextInput inputAccounting = new TextInput(PARAMETER_ACCOUNTING_KEY);
		IntegerInput inputYearFrom = new IntegerInput(PARAMETER_YEAR_FROM);
//		inputYearFrom.setAsNotEmpty(localize("please_fill_in_the_field", "Please fill in the field")+" "+localize("birthyear_from", "Birthyear from"));
		IntegerInput inputYearTo = new IntegerInput(PARAMETER_YEAR_TO);
//		inputYearTo.setAsNotEmpty(localize("please_fill_in_the_field", "Please fill in the field")+" "+localize("birthyear_to", "Birthyear to"));
		IntegerInput inputMaxPer = new IntegerInput(PARAMETER_MAX_PER);
//		inputMaxPer.setAsNotEmpty(localize("please_fill_in_the_field", "Please fill in the field")+" "+localize("max_per_course", "Max per course"));
		TextInput inputUser = new TextInput(PARAMETER_USER);
		
		DropdownMenu schoolTypeID = new DropdownMenu(PARAMETER_SCHOOL_TYPE_ID);
		schoolTypeID.setId(PARAMETER_SCHOOL_TYPE_ID);
		schoolTypeID.setOnChange("changeValues();");

		DropdownMenu courseTypeID = new DropdownMenu(PARAMETER_COURSE_TYPE_ID);
		courseTypeID.setId(PARAMETER_COURSE_TYPE_ID);
		
		Collection schoolTypes = null;
		Collection cargoTypes = null;
		try {
			schoolTypes = getBusiness().getSchoolTypeHome().findAllByCategory(CourseBusinessBean.COURSE_SCHOOL_CATEGORY);
			schoolTypeID.addMenuElements(schoolTypes);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		DropdownMenu priceDrop = new DropdownMenu(PARAMETER_COURSE_PRICE_ID);
		priceDrop.setId(PARAMETER_COURSE_PRICE_ID);
		
		if (course != null) {
			inputName.setContent(course.getName());
			inputFrom.setDate(course.getStartDate());
			String stID = course.getSchoolType().getPrimaryKey().toString();
			schoolTypeID.setSelectedElement(stID);
			inputAccounting.setValue(course.getAccountingKey());
			inputYearFrom.setValue(course.getBirthyearFrom());
			inputYearTo.setValue(course.getBirthyearTo());
			inputMaxPer.setValue(course.getMax());
			
			cargoTypes = getCourseBusiness(iwc).getCourseTypes(new Integer(stID));
			courseTypeID.addMenuElements(cargoTypes);
			courseTypeID.setSelectedElement(course.getCourseType().getPrimaryKey().toString());

			form.add(new HiddenInput(PARAMETER_COURSE_PK, coursePK.toString()));
			
			try {
				Collection prices = getCourseBusiness(iwc).getCoursePriceHome().findAll(course.getSchoolType(), course.getCourseType());
				priceDrop.addMenuElements(prices);
				priceDrop.setSelectedElement(course.getPrice().getPrimaryKey().toString());
			} catch (IDORelationshipException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
			
		} else {
			cargoTypes = getCourseBusiness(iwc).getCourseTypes((Integer) ((SchoolType)schoolTypes.iterator().next()).getPrimaryKey());
			courseTypeID.addMenuElements(cargoTypes);

			priceDrop.addMenuElement(localize("select_a_date_and_search", "Select a date and search"));
			priceDrop.setDisabled(true);
		}
		

		Heading1 heading = new Heading1(localize("information", "Information"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);
		
		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer layer;
		Label label;

		layer = new Layer(Layer.DIV);
		layer.setID("category");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("category", "Category"), schoolTypeID);
		layer.add(label);
		layer.add(schoolTypeID);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("type");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("type", "Type"), courseTypeID);
		layer.add(label);
		layer.add(courseTypeID);
		section.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setID("name");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("name", "Name"), inputName);
		layer.add(label);
		layer.add(inputName);
		section.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setID("user");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("user", "User"), inputUser);
		layer.add(label);
		layer.add(inputUser);
		section.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setID("accounting_key");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("accounting_key", "Accounting key"), inputAccounting);
		layer.add(label);
		layer.add(inputAccounting);
		section.add(layer);

		heading = new Heading1(localize("price_selection", "Price selection"));
		heading.setStyleClass("subHeader");
		form.add(heading);
		
		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(localize("course.length_search_explanation", "Select a start date and click \"Search for length\" to populate" +
				" the length dropdown. If nothing is found you will be prompted to search again. If a length is found you can select one and" +
				" proceed with the form.")));
		section.add(helpLayer);
		
		layer = new Layer(Layer.DIV);
		layer.setID("start_date");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("start_date", "Start date"), (TextInput)inputFrom.getPresentationObject(iwc));
		inputFrom.setTextInputId(PARAMETER_VALID_FROM);
		inputFrom.setDisabled(false);
		layer.add(label);
		layer.add(inputFrom);
		section.add(layer);
		
		layer = new Layer();
		layer.setID("search");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		GenericButton search = new GenericButton(localize("search_for_length", "Search for length"));
		search.setOnClick("changeValuesPrice();");
		label = new Label(Text.getNonBrakingSpace(), search);
		layer.add(label);
		layer.add(search);
		section.add(layer);
		
		
		layer = new Layer(Layer.DIV);
		layer.setID("length");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("length", "Length"), priceDrop);
		layer.add(label);
		layer.add(priceDrop);
		section.add(layer);
		
		
		layer = new Layer(Layer.DIV);
		layer.setID("year_from");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("birthyear_from", "Birthyear from"), inputYearFrom);
		layer.add(label);
		layer.add(inputYearFrom);
		section.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setID("year_to");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("birthyear_to", "Birthyear to"), inputYearTo);
		layer.add(label);
		layer.add(inputYearTo);
		section.add(layer);
		
		layer = new Layer(Layer.DIV);
		layer.setID("max");
		layer.setStyleClass(STYLENAME_FORM_ELEMENT);
		label = new Label(localize("max_per_course", "Max per course"), inputMaxPer);
		layer.add(label);
		layer.add(inputMaxPer);
		section.add(layer);
		
		
		SubmitButton save = (SubmitButton) getButton(new SubmitButton(localize("save", "Save")));
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		save.setId("SAVE_BTN_ID");
		if (course == null) {
			save.setDisabled(true);
		}
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
