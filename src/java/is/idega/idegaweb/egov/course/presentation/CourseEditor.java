package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseCategory;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CourseType;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
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
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;

public class CourseEditor extends CourseBlock {

	private static final String PARAMETER_COURSE_PRICE_PK = "prm_course_price";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_VALID_FROM = "prm_valid_from";
	private static final String PARAMETER_VALID_TO = "prm_valid_to";
	private static final String PARAMETER_ACCOUNTING_KEY = "prm_accounting_key";
	private static final String PARAMETER_USER = "prm_user";
	private static final String PARAMETER_YEAR_FROM = "prm_year_from";
	private static final String PARAMETER_YEAR_TO = "prm_year_to";
	private static final String PARAMETER_MAX_PER = "prm_max_participants";
	private static final String PARAMETER_PRICE = "prm_price";
	private static final String PARAMETER_COST = "prm_cost";

	private static final String PARAMETER_VALID_FROM_ID = "prm_valid_from_id";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	private static final int ACTION_DELETE = 5;

	private SchoolType type = null;
	private boolean showTypes = true;

	public void present(IWContext iwc) {
		try {
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
					if (saveCourse(iwc)) {
						showList(iwc);
					}
					else {
						showEditor(iwc, null);
					}
					break;

				case ACTION_DELETE:
					if (!getCourseBusiness(iwc).deleteCourse(iwc.getParameter(PARAMETER_COURSE_PK))) {
						getParentPage().setAlertOnLoad(getResourceBundle().getLocalizedString("course.remove_error", "You can not remove a course that has choices attached to it."));
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

	public boolean saveCourse(IWContext iwc) {
		String pk = iwc.getParameter(PARAMETER_COURSE_PK);
		String name = iwc.getParameter(PARAMETER_NAME);
		String user = iwc.getParameter(PARAMETER_USER);
		String courseTypePK = iwc.getParameter(PARAMETER_COURSE_TYPE_PK);
		String coursePricePK = iwc.getParameter(PARAMETER_COURSE_PRICE_PK);
		String accountingKey = iwc.getParameter(PARAMETER_ACCOUNTING_KEY);
		String sStartDate = iwc.getParameter(PARAMETER_VALID_FROM);
		String sEndDate = iwc.isParameterSet(PARAMETER_VALID_TO) ? iwc.getParameter(PARAMETER_VALID_TO) : null;
		String yearFrom = iwc.getParameter(PARAMETER_YEAR_FROM);
		String yearTo = iwc.getParameter(PARAMETER_YEAR_TO);
		String max = iwc.getParameter(PARAMETER_MAX_PER);

		try {
			IWTimestamp startDate = new IWTimestamp(sStartDate);
			IWTimestamp endDate = sEndDate != null ? new IWTimestamp(sEndDate) : null;
			int birthYearFrom = yearFrom != null ? Integer.parseInt(yearFrom) : -1;
			int birthYearTo = yearTo != null ? Integer.parseInt(yearTo) : -1;
			int maxPer = Integer.parseInt(max);
			float price = iwc.isParameterSet(PARAMETER_PRICE) ? Float.parseFloat(iwc.getParameter(PARAMETER_PRICE)) : 0;
			float cost = iwc.isParameterSet(PARAMETER_COST) ? Float.parseFloat(iwc.getParameter(PARAMETER_COST)) : 0;
			getCourseBusiness(iwc).storeCourse(pk, name, user, courseTypePK, getSession().getProvider().getPrimaryKey(), coursePricePK, startDate, endDate, accountingKey, birthYearFrom, birthYearTo, maxPer, price, cost);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private Layer getNavigation(IWContext iwc) throws RemoteException {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("formSection");

		List scriptFiles = new ArrayList();
		scriptFiles.add("/dwr/interface/CourseDWRUtil.js");
		scriptFiles.add(CoreConstants.DWR_ENGINE_SCRIPT);
		scriptFiles.add(CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scriptFiles);

		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n").append("\tDWRUtil.removeAllOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\");\n").append("\tDWRUtil.addOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\", data);\n").append("}");

		StringBuffer script = new StringBuffer();
		script.append("function changeValues() {\n").append("\tvar val = +$(\"" + PARAMETER_SCHOOL_TYPE_PK + "\").value;\n").append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(val, '" + iwc.getCurrentLocale().getCountry() + "', setOptions);\n").append("}");

		List jsActions = new ArrayList();
		jsActions.add(script2.toString());
		jsActions.add(script.toString());
		PresentationUtil.addJavaScriptActionsToBody(iwc, jsActions);

		if (!isSchoolUser()) {
			DropdownMenu providers = null;
			if (iwc.getAccessController().hasRole(CourseConstants.SUPER_ADMINISTRATOR_ROLE_KEY, iwc)) {
				providers = getAllProvidersDropdown(iwc);
			}
			else if (iwc.getAccessController().hasRole(CourseConstants.ADMINISTRATOR_ROLE_KEY, iwc)) {
				providers = getProvidersDropdown(iwc);
			}

			Collection providersList = getBusiness().getProviders();
			if (providersList.size() == 1) {
				School school = (School) providersList.iterator().next();
				getSession().setProvider(school);
				layer.add(new HiddenInput(PARAMETER_PROVIDER_PK, school.getPrimaryKey().toString()));
			}
			else if (providers != null) {
				providers.setToSubmit();

				Layer formItem = new Layer(Layer.DIV);
				formItem.setStyleClass("formItem");
				Label label = new Label(getResourceBundle().getLocalizedString("provider", "Provider"), providers);
				formItem.add(label);
				formItem.add(providers);
				layer.add(formItem);
			}
		}

		DropdownMenu schoolType = new DropdownMenu(PARAMETER_SCHOOL_TYPE_PK);
		schoolType.setId(PARAMETER_SCHOOL_TYPE_PK);
		schoolType.setOnChange("changeValues();");
		schoolType.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_school_type", "Select school type"));
		schoolType.keepStatusOnAction(true);

		if (getSession().getProvider() != null) {
			Collection schoolTypes = getBusiness().getSchoolTypes(getSession().getProvider());
			if (schoolTypes.size() == 1) {
				showTypes = false;
				type = (SchoolType) schoolTypes.iterator().next();
				schoolType.setSelectedElement(type.getPrimaryKey().toString());
			}
			schoolType.addMenuElements(schoolTypes);
		}
		else {
			Collection schoolTypes = getBusiness().getAllSchoolTypes();
			if (schoolTypes.size() == 1) {
				showTypes = false;
				type = (SchoolType) schoolTypes.iterator().next();
				schoolType.setSelectedElement(type.getPrimaryKey().toString());
			}
			schoolType.addMenuElements(schoolTypes);
		}

		DropdownMenu courseType = new DropdownMenu(PARAMETER_COURSE_TYPE_PK);
		courseType.setId(PARAMETER_COURSE_TYPE_PK);
		courseType.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_course_type", "Select course type"));
		courseType.keepStatusOnAction(true);

		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_PK)) {
			Collection courseTypes = getBusiness().getCourseTypes(new Integer(iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK)));
			courseType.addMenuElements(courseTypes);
		}
		else if (type != null) {
			Collection courseTypes = getBusiness().getCourseTypes(new Integer(type.getPrimaryKey().toString()));
			courseType.addMenuElements(courseTypes);
		}

		IWTimestamp stamp = new IWTimestamp();
		stamp.addYears(1);

		DatePicker fromDate = new DatePicker(PARAMETER_FROM_DATE);
		fromDate.keepStatusOnAction(true);

		DatePicker toDate = new DatePicker(PARAMETER_TO_DATE);
		toDate.keepStatusOnAction(true);
		toDate.setDate(stamp.getDate());

		stamp.addMonths(-1);
		stamp.addYears(-1);
		fromDate.setDate(stamp.getDate());

		if (showTypes) {
			Layer formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			Label label = new Label(getResourceBundle().getLocalizedString("category", "Category"), schoolType);
			formItem.add(label);
			formItem.add(schoolType);
			layer.add(formItem);
		}
		else if (type != null) {
			layer.add(new HiddenInput(PARAMETER_SCHOOL_TYPE_PK, type.getPrimaryKey().toString()));
		}

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Label label = new Label(getResourceBundle().getLocalizedString("type", "Type"), courseType);
		formItem.add(label);
		formItem.add(courseType);
		layer.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label();
		label.setLabel(getResourceBundle().getLocalizedString("from", "From"));
		formItem.add(label);
		formItem.add(fromDate);
		layer.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label();
		label.setLabel(getResourceBundle().getLocalizedString("to", "To"));
		formItem.add(label);
		formItem.add(toDate);
		layer.add(formItem);

		SubmitButton fetch = new SubmitButton(getResourceBundle().getLocalizedString("get", "Get"));
		fetch.setStyleClass("indentedButton");
		fetch.setStyleClass("button");
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("buttonItem");
		formItem.add(fetch);
		layer.add(formItem);

		if (getSession().getProvider() != null) {
			SubmitButton newLink = new SubmitButton(localize("course.new", "New course"), PARAMETER_ACTION, String.valueOf(ACTION_NEW));
			newLink.setStyleClass("indentedButton");
			newLink.setStyleClass("button");
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("buttonItem");
			formItem.add(newLink);
			layer.add(formItem);
		}

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		layer.add(clearLayer);

		return layer;
	}

	public void showList(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setStyleClass("adminForm");
		form.setEventListener(this.getClass());

		form.add(getNavigation(iwc));

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

		IWTimestamp stamp = new IWTimestamp();
		stamp.addMonths(-1);

		Date fromDate = stamp.getDate();
		if (iwc.isParameterSet(PARAMETER_FROM_DATE)) {
			fromDate = new IWTimestamp(iwc.getParameter(PARAMETER_FROM_DATE)).getDate();
		}

		stamp.addMonths(1);
		stamp.addYears(1);
		Date toDate = stamp.getDate();
		if (iwc.isParameterSet(PARAMETER_TO_DATE)) {
			toDate = new IWTimestamp(iwc.getParameter(PARAMETER_TO_DATE)).getDate();
		}

		Collection courses = new ArrayList();
		if (getSession().getProvider() != null) {
			try {
				courses = getCourseBusiness(iwc).getCourses(-1, getSession().getProvider().getPrimaryKey(), iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_PK) ? iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK) : (type != null ? type.getPrimaryKey() : null), iwc.isParameterSet(PARAMETER_COURSE_TYPE_PK) ? iwc.getParameter(PARAMETER_COURSE_TYPE_PK) : null, fromDate, toDate);
			}
			catch (RemoteException rex) {
				throw new IBORuntimeException(rex);
			}
		}

		boolean useBirthYears = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_BIRTHYEARS, true);

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();

		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("number");
		cell.add(Text.getNonBrakingSpace());

		cell = row.createHeaderCell();
		cell.setStyleClass("name");
		cell.add(new Text(localize("name", "Name")));

		if (showTypes) {
			cell = row.createHeaderCell();
			cell.setStyleClass("category");
			cell.add(new Text(localize("category", "Category")));
		}

		cell = row.createHeaderCell();
		cell.setStyleClass("type");
		cell.add(new Text(localize("type", "Type")));

		if (useBirthYears) {
			cell = row.createHeaderCell();
			cell.setStyleClass("yearFrom");
			cell.add(new Text(localize("from", "From")));

			cell = row.createHeaderCell();
			cell.setStyleClass("yearTo");
			cell.add(new Text(localize("to", "To")));
		}

		cell = row.createHeaderCell();
		cell.setStyleClass("startDate");
		cell.add(new Text(localize("date_from", "Date from")));

		cell = row.createHeaderCell();
		cell.setStyleClass("endDate");
		cell.add(new Text(localize("date_to", "Date to")));

		cell = row.createHeaderCell();
		cell.setStyleClass("max");
		cell.add(new Text(localize("max", "Max")));

		cell = row.createHeaderCell();
		cell.setStyleClass("edit");
		cell.add(new Text(localize("edit", "Edit")));

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("delete");
		cell.add(new Text(localize("delete", "Delete")));

		group = table.createBodyRowGroup();
		int iRow = 1;
		java.util.Iterator iter = courses.iterator();
		while (iter.hasNext()) {
			Course course = (Course) iter.next();
			CourseType cType = course.getCourseType();
			row = group.createRow();

			try {
				Link edit = new Link(getBundle().getImage("edit.png", localize("edit", "Edit")));
				edit.addParameter(PARAMETER_COURSE_PK, course.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);
				edit.maintainParameter(PARAMETER_FROM_DATE, iwc);
				edit.maintainParameter(PARAMETER_TO_DATE, iwc);

				Link delete = new Link(getBundle().getImage("delete.png", localize("delete", "Delete")));
				delete.addParameter(PARAMETER_COURSE_PK, course.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);
				delete.setClickConfirmation(getResourceBundle().getLocalizedString("course.confirm_delete", "Are you sure you want to delete the course selected?"));
				delete.maintainParameter(PARAMETER_FROM_DATE, iwc);
				delete.maintainParameter(PARAMETER_TO_DATE, iwc);

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.setStyleClass("number");
				if (cType.getAbbreviation() != null) {
					cell.add(new Text(cType.getAbbreviation()));
				}
				cell.add(new Text(course.getPrimaryKey().toString()));

				cell = row.createCell();
				cell.setStyleClass("name");
				cell.add(new Text(course.getName()));

				if (showTypes) {
					cell = row.createCell();
					cell.setStyleClass("category");
					CourseCategory sType = cType.getCourseCategory();
					if (sType != null) {
						if (sType.getLocalizationKey() != null) {
							cell.add(new Text(localize(sType.getLocalizationKey(), sType.getName())));
						}
						else {
							cell.add(new Text(sType.getName()));
						}
					}
				}

				cell = row.createCell();
				cell.setStyleClass("type");
				if (cType.getLocalizationKey() != null) {
					cell.add(new Text(localize(cType.getLocalizationKey(), cType.getName())));
				}
				else {
					cell.add(new Text(cType.getName()));
				}

				if (useBirthYears) {
					cell = row.createCell();
					cell.setStyleClass("yearFrom");
					cell.add(new Text(String.valueOf(course.getBirthyearFrom())));

					cell = row.createCell();
					cell.setStyleClass("yearTo");
					cell.add(new Text(String.valueOf(course.getBirthyearTo())));
				}

				cell = row.createCell();
				cell.setStyleClass("startDate");
				Timestamp start = course.getStartDate();
				if (start != null) {
					cell.add(new Text(new IWTimestamp(start).getDateString("dd.MM.yyyy", iwc.getCurrentLocale())));
				}

				cell = row.createCell();
				cell.setStyleClass("endDate");
				if (course.getEndDate() != null) {
					cell.add(new Text(new IWTimestamp(course.getEndDate()).getDateString("dd.MM.yyyy", iwc.getCurrentLocale())));
				}
				else {
					CoursePrice price = course.getPrice();
					if (start != null && price != null) {
						IWTimestamp date = new IWTimestamp(getBusiness().getEndDate(price, new IWTimestamp(start).getDate()));
						cell.add(new Text(date.getDateString("dd.MM.yyyy", iwc.getCurrentLocale())));
					}
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

		if (getSession().getProvider() != null) {
			Layer buttonLayer = new Layer(Layer.DIV);
			buttonLayer.setStyleClass("buttonLayer");
			form.add(buttonLayer);

			if (getBackPage() != null) {
				GenericButton back = new GenericButton(localize("back", "Back"));
				back.setPageToOpen(getBackPage());
				buttonLayer.add(back);
			}

			SubmitButton newLink = new SubmitButton(localize("course.new", "New course"), PARAMETER_ACTION, String.valueOf(ACTION_NEW));
			buttonLayer.add(newLink);
		}

		add(form);
	}

	public void showEditor(IWContext iwc, Object coursePK) throws java.rmi.RemoteException {
		boolean useFixedPrices = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_FIXED_PRICES, true);
		boolean useBirthYears = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_BIRTHYEARS, true);

		if (!useFixedPrices) {
			super.getParentPage().addJavascriptURL("/dwr/interface/CourseDWRUtil.js");
			super.getParentPage().addJavascriptURL("/dwr/engine.js");
			super.getParentPage().addJavascriptURL("/dwr/util.js");

			StringBuffer script2 = new StringBuffer();
			script2.append("function setOptions(data) {\n").append("\tDWRUtil.removeAllOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\");\n").append("\tDWRUtil.addOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\", data);\n").append("}");

			StringBuffer script = new StringBuffer();
			script.append("function changeValues() {\n").append("\tvar val = +$(\"" + PARAMETER_SCHOOL_TYPE_PK + "\").value;\n").append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(val, '" + iwc.getCurrentLocale().getCountry() + "', setOptions);\n").append("}");

			StringBuffer script3 = new StringBuffer();
			script3.append("function setOptionsPrice(data) {\n").append("\tvar isEmpty = true;\n").append("\tfor (var prop in data) { isEmpty = false } \n").append("\tDWRUtil.removeAllOptions(\"" + PARAMETER_COURSE_PRICE_PK + "\");\n").append("\tDWRUtil.addOptions(\"" + PARAMETER_COURSE_PRICE_PK + "\", data);\n").append("\tvar savebtn = $(\"SAVE_BTN_ID\");\n").append("\tif (isEmpty == true) {\n").append("\t\tDWRUtil.addOptions(\"" + PARAMETER_COURSE_PRICE_PK + "\",['" + localize("try_another_date", "Try another date") + "...']);\n").append("\t\tsavebtn.disabled=true;\n").append("\t\t$(\"" + PARAMETER_COURSE_PRICE_PK + "\").disabled=true;\n").append("\t} else {\n").append("\t\tsavebtn.disabled=false;\n").append("\t\t$(\"" + PARAMETER_COURSE_PRICE_PK + "\").disabled=false;\n").append("\t}\n").append("}");

			StringBuffer script4 = new StringBuffer();
			script4.append("function changeValuesPrice() {\n").append("\tvar date = DWRUtil.getValue(\"" + PARAMETER_VALID_FROM_ID + "\");\n").append("\tvar val = " + getSession().getProvider().getPrimaryKey().toString() + ";\n").append("\tvar val2 = DWRUtil.getValue(\"" + PARAMETER_COURSE_TYPE_PK + "\");\n").append("\tCourseDWRUtil.getCoursePricesDWR(date, val, val2, '" + iwc.getCurrentLocale().getCountry() + "', setOptionsPrice);").append("}");

			StringBuffer script5 = new StringBuffer();
			script5.append("function readPrice() {\n\tvar id = DWRUtil.getValue(\"" + PARAMETER_COURSE_PRICE_PK + "\");\n\tCourseDWRUtil.getPriceDWR(id, fillPrice);\n}");

			StringBuffer script6 = new StringBuffer();
			script6.append("function fillPrice(aprice) {\n\tprice = aprice;\n\tDWRUtil.setValues(price);\n}");

			super.getParentPage().getAssociatedScript().addFunction("setOptions", script2.toString());
			super.getParentPage().getAssociatedScript().addFunction("changeValues", script.toString());
			super.getParentPage().getAssociatedScript().addFunction("setOptionsPrice", script3.toString());
			super.getParentPage().getAssociatedScript().addFunction("changeValuesPrice", script4.toString());
			super.getParentPage().getAssociatedScript().addFunction("readPrice", script5.toString());
			super.getParentPage().getAssociatedScript().addFunction("fillPrice", script6.toString());
		}

		Form form = new Form();
		form.setID("courseEditor");
		form.setStyleClass("adminForm");
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(coursePK != null ? ACTION_EDIT : ACTION_NEW)));
		form.maintainParameter(PARAMETER_FROM_DATE);
		form.maintainParameter(PARAMETER_TO_DATE);

		Course course = getCourseBusiness(iwc).getCourse(coursePK);

		TextInput inputName = new TextInput(PARAMETER_NAME);
		DatePicker inputFrom = new DatePicker(PARAMETER_VALID_FROM);
		DatePicker inputTo = new DatePicker(PARAMETER_VALID_TO);
		TextInput inputAccounting = new TextInput(PARAMETER_ACCOUNTING_KEY);
		IntegerInput inputYearFrom = new IntegerInput(PARAMETER_YEAR_FROM);
		inputYearFrom.setMaxlength(4);
		IntegerInput inputYearTo = new IntegerInput(PARAMETER_YEAR_TO);
		inputYearTo.setMaxlength(4);
		IntegerInput inputMaxPer = new IntegerInput(PARAMETER_MAX_PER);
		TextInput inputUser = new TextInput(PARAMETER_USER);

		TextInput price = new TextInput(PARAMETER_PRICE);
		price.setId("price");
		price.setDisabled(!useFixedPrices);

		TextInput courseCost = new TextInput(PARAMETER_COST);

		TextInput preCarePrice = new TextInput("preCarePrice");
		preCarePrice.setId("preCarePrice");
		preCarePrice.setDisabled(true);

		TextInput postCarePrice = new TextInput("postCarePrice");
		postCarePrice.setId("postCarePrice");
		postCarePrice.setDisabled(true);

		DropdownMenu schoolTypeID = new DropdownMenu(PARAMETER_SCHOOL_TYPE_PK);
		schoolTypeID.setId(PARAMETER_SCHOOL_TYPE_PK);
		//schoolTypeID.setOnChange("changeValues();");
		schoolTypeID.setToSubmit(true);
		schoolTypeID.keepStatusOnAction(true);
		schoolTypeID.addMenuElementFirst("-1", getResourceBundle().getLocalizedString("select_school_type", "Select school type"));

		DropdownMenu courseTypeID = new DropdownMenu(PARAMETER_COURSE_TYPE_PK);
		courseTypeID.setId(PARAMETER_COURSE_TYPE_PK);
		courseTypeID.addMenuElementFirst("-1", getResourceBundle().getLocalizedString("select_course_type", "Select course type"));

		boolean showTypes = true;
		Object schoolTypePK = null;
		
		Collection cargoTypes = null;
		Collection schoolTypes = getCourseBusiness(iwc).getSchoolTypes(getSession().getProvider());
		if (schoolTypes.size() > 1) {
			schoolTypeID.addMenuElements(schoolTypes);
		}
		else if (schoolTypes.size() == 1) {
			SchoolType type = (SchoolType) schoolTypes.iterator().next();
			showTypes = false;
			form.add(new HiddenInput(PARAMETER_SCHOOL_TYPE_PK, type.getPrimaryKey().toString()));
			schoolTypePK = type.getPrimaryKey();
		}

		DropdownMenu priceDrop = new DropdownMenu(PARAMETER_COURSE_PRICE_PK);
		priceDrop.setId(PARAMETER_COURSE_PRICE_PK);
		priceDrop.setOnChange("readPrice();");

		if (course != null) {
			CourseType type = course.getCourseType();
			CourseCategory category = type.getCourseCategory();
			useFixedPrices = category.useFixedPricing();

			School provider = course.getProvider();
			CoursePrice coursePrice = course.getPrice();

			inputName.setContent(course.getName());
			if (course.getUser() != null) {
				inputUser.setContent(course.getUser());
			}
			inputFrom.setDate(course.getStartDate());
			if (course.getEndDate() != null) {
				inputTo.setDate(course.getEndDate());
			}
			String stID = type.getCourseCategory().getPrimaryKey().toString();
			schoolTypeID.setSelectedElement(stID);
			inputAccounting.setValue(course.getAccountingKey());
			if (useBirthYears) {
				inputYearFrom.setValue(course.getBirthyearFrom());
				inputYearTo.setValue(course.getBirthyearTo());
			}
			inputMaxPer.setValue(course.getMax());

			cargoTypes = getCourseBusiness(iwc).getCourseTypes(new Integer(stID));
			courseTypeID.addMenuElements(cargoTypes);
			courseTypeID.setSelectedElement(course.getCourseType().getPrimaryKey().toString());

			if (!useFixedPrices) {
				price.setContent(Integer.toString(coursePrice.getPrice()));
				preCarePrice.setContent(coursePrice.getPreCarePrice() > 0 ? Integer.toString(coursePrice.getPreCarePrice()) : "0");
				postCarePrice.setContent(coursePrice.getPostCarePrice() > 0 ? Integer.toString(coursePrice.getPostCarePrice()) : "0");

				try {
					Collection prices = getCourseBusiness(iwc).getCoursePriceHome().findAll(provider.getSchoolArea(), type);
					priceDrop.addMenuElements(prices);
					priceDrop.setSelectedElement(course.getPrice().getPrimaryKey().toString());
				}
				catch (IDORelationshipException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
			}
			else if (course.getCoursePrice() > 0) {
				price.setContent(String.valueOf((int) course.getCoursePrice()));
				price.setDisabled(false);

				courseCost.setContent(course.getCourseCost() > -1 ? String.valueOf((int) course.getCourseCost()) : "");
			}

			form.add(new HiddenInput(PARAMETER_COURSE_PK, coursePK.toString()));
		}
		else {
			if (schoolTypes.iterator().hasNext()) {
				cargoTypes = getCourseBusiness(iwc).getCourseTypes((Integer) ((SchoolType) schoolTypes.iterator().next()).getPrimaryKey());
				courseTypeID.addMenuElements(cargoTypes);
			}

			priceDrop.addMenuElement("-1", localize("select_a_date_and_search", "Select a date and search"));
			priceDrop.setDisabled(true);
		}

		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_PK)) {
			schoolTypePK = iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK);
		}
		
		if (schoolTypePK != null) {
			CourseCategory category = getCourseBusiness(iwc).getCourseCategory(schoolTypePK);
			useFixedPrices = category.useFixedPricing();
			price.setDisabled(!useFixedPrices);

			cargoTypes = getCourseBusiness(iwc).getCourseTypes(new Integer(category.getPrimaryKey().toString()));
			courseTypeID.removeElements();
			courseTypeID.addMenuElements(cargoTypes);
		}

		Heading1 heading = new Heading1(localize("information", "Information"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(localize("course.course_editor_help", "Select a start date and click \"Search for length\" to populate the length dropdown. If nothing is found you will be prompted to search again. If a length is found you can select one and proceed with the form.")));
		section.add(helpLayer);

		Layer layer;
		Label label;

		if (showTypes) {
			layer = new Layer(Layer.DIV);
			layer.setID("category");
			layer.setStyleClass("formItem");
			label = new Label(localize("category", "Category"), schoolTypeID);
			layer.add(label);
			layer.add(schoolTypeID);
			section.add(layer);
		}
		
		layer = new Layer(Layer.DIV);
		layer.setID("type");
		layer.setStyleClass("formItem");
		label = new Label(localize("type", "Type"), courseTypeID);
		layer.add(label);
		layer.add(courseTypeID);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("courseName");
		layer.setStyleClass("formItem");
		label = new Label(localize("name", "Name"), inputName);
		layer.add(label);
		layer.add(inputName);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("user");
		layer.setStyleClass("formItem");
		label = new Label(localize("user", "User"), inputUser);
		layer.add(label);
		layer.add(inputUser);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("accounting_key");
		layer.setStyleClass("formItem");
		label = new Label(localize("accounting_key", "Accounting key"), inputAccounting);
		layer.add(label);
		layer.add(inputAccounting);
		section.add(layer);

		heading = new Heading1(localize("length_selection", "Length selection"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		if (useFixedPrices) {
			helpLayer.add(new Text(localize("course.length_and_max", "Select a start date and end date as well as maximum number of participants in the course.")));
		}
		else {
			helpLayer.add(new Text(localize("course.length_search_explanation", "Select a start date and click \"Search for length\" to populate" + " the length dropdown. If nothing is found you will be prompted to search again. If a length is found you can select one and" + " proceed with the form.")));
		}
		section.add(helpLayer);

		layer = new Layer(Layer.DIV);
		layer.setID("start_date");
		layer.setStyleClass("formItem");
		label = new Label();
		label.setLabel(localize("start_date", "Start date"));
		inputFrom.setTextInputId(PARAMETER_VALID_FROM_ID);
		layer.add(label);
		layer.add(inputFrom);
		section.add(layer);

		if (useFixedPrices) {
			layer = new Layer(Layer.DIV);
			layer.setID("end_date");
			layer.setStyleClass("formItem");
			label = new Label();
			label.setLabel(localize("end_date", "End date"));
			layer.add(label);
			layer.add(inputTo);
			section.add(layer);
		}
		else {
			layer = new Layer();
			layer.setID("search");
			layer.setStyleClass("formItem");
			GenericButton search = new GenericButton(localize("search_for_length", "Search for length"));
			search.setOnClick("changeValuesPrice();");
			label = new Label(Text.getNonBrakingSpace(), search);
			layer.add(label);
			layer.add(search);
			section.add(layer);

			layer = new Layer(Layer.DIV);
			layer.setID("length");
			layer.setStyleClass("formItem");
			label = new Label(localize("length", "Length"), priceDrop);
			layer.add(label);
			layer.add(priceDrop);
			section.add(layer);
		}

		if (useBirthYears) {
			layer = new Layer(Layer.DIV);
			layer.setID("year_from");
			layer.setStyleClass("formItem");
			label = new Label(localize("birthyear_from", "Birthyear from"), inputYearFrom);
			layer.add(label);
			layer.add(inputYearFrom);
			section.add(layer);

			layer = new Layer(Layer.DIV);
			layer.setID("year_to");
			layer.setStyleClass("formItem");
			label = new Label(localize("birthyear_to", "Birthyear to"), inputYearTo);
			layer.add(label);
			layer.add(inputYearTo);
			section.add(layer);
		}

		layer = new Layer(Layer.DIV);
		layer.setID("max");
		layer.setStyleClass("formItem");
		label = new Label(localize("max_per_course", "Max per course"), inputMaxPer);
		layer.add(label);
		layer.add(inputMaxPer);
		section.add(layer);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		section.add(clearLayer);

		heading = new Heading1(localize("price_selection", "Price selection"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		if (useFixedPrices) {
			helpLayer.add(new Text(localize("course.price_help", "Insert the price for the course")));
		}
		else {
			helpLayer.add(new Text(localize("course.price_explanation", "Select a start date and click \"Search for length\" to populate" + " the length dropdown. If nothing is found you will be prompted to search again. If a length is found you can select one and" + " proceed with the form.")));
		}
		section.add(helpLayer);

		layer = new Layer(Layer.DIV);
		layer.setID("coursePrice");
		layer.setStyleClass("formItem");
		label = new Label(localize("price", "Price"), price);
		layer.add(label);
		layer.add(price);
		section.add(layer);

		if (!useFixedPrices) {
			layer = new Layer(Layer.DIV);
			layer.setID("coursePeCarePrice");
			layer.setStyleClass("formItem");
			label = new Label(localize("pre_care_price", "Pre care price"), preCarePrice);
			layer.add(label);
			layer.add(preCarePrice);
			section.add(layer);

			layer = new Layer(Layer.DIV);
			layer.setID("coursePostCarePrice");
			layer.setStyleClass("formItem");
			label = new Label(localize("post_care_price", "Post care price"), postCarePrice);
			layer.add(label);
			layer.add(postCarePrice);
			section.add(layer);
		}
		else {
			layer = new Layer(Layer.DIV);
			layer.setStyleClass("formItem");
			label = new Label(localize("course_cost", "Course cost"), courseCost);
			layer.add(label);
			layer.add(courseCost);
			section.add(layer);
		}

		section.add(clearLayer);

		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonLayer");
		form.add(buttonLayer);

		SubmitButton cancel = new SubmitButton(localize("cancel", "Cancel"));
		cancel.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		buttonLayer.add(cancel);

		SubmitButton save = new SubmitButton(localize("save", "Save"));
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		if (!useFixedPrices) {
			save.setId("SAVE_BTN_ID");
			if (course == null) {
				save.setDisabled(true);
			}
		}
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