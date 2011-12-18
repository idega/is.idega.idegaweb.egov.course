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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.CSSSpacer;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Script;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BooleanInput;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.IWDatePicker;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.StringUtil;

public class CourseEditor extends CourseBlock {

	protected static final String PARAMETER_COURSE_PRICE_PK = "prm_course_price";
	protected static final String PARAMETER_COURSE_NUMBER = "prm_course_number";
	protected static final String PARAMETER_NAME = "prm_name";
	protected static final String PARAMETER_VALID_FROM = "prm_valid_from";
	protected static final String PARAMETER_VALID_TO = "prm_valid_to";
	protected static final String PARAMETER_ACCOUNTING_KEY = "prm_accounting_key";
	protected static final String PARAMETER_USER = "prm_user";
	protected static final String PARAMETER_YEAR_FROM = "prm_year_from";
	protected static final String PARAMETER_YEAR_TO = "prm_year_to";
	protected static final String PARAMETER_MAX_PER = "prm_max_participants";
	protected static final String PARAMETER_PRICE = "prm_price";
	protected static final String PARAMETER_COST = "prm_cost";
	protected static final String PARAMETER_OPEN_FOR_REGISTRATION = "prm_open_for_registration";
	private static final String PARAMETER_HAS_PRE_CARE = "prm_has_pre_care";
	private static final String PARAMETER_HAS_POST_CARE = "prm_has_pre_care";
	private static final String PARAMETER_REGISTRATION_END = "prm_registration_end";

	protected static final String PARAMETER_VALID_FROM_ID = "prm_valid_from_id";

	protected static final int ACTION_VIEW = 1;
	protected static final int ACTION_EDIT = 2;
	protected static final int ACTION_NEW = 3;
	protected static final int ACTION_SAVE = 4;
	protected static final int ACTION_DELETE = 5;

	private SchoolType type = null;
	
	private boolean showTypes = true;
	private boolean showCourseCategory = true;
	private boolean showCourseType = true;
	protected Boolean useFixedPrice = null;

	private Course course;

	@Override
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
				if (saveCourse(iwc) != null) {
					showList(iwc);
				} else {
					showEditor(iwc, null);
				}
				break;

			case ACTION_DELETE:
				if (!getCourseBusiness().deleteCourse(iwc.getParameter(PARAMETER_COURSE_PK))) {
					PresentationUtil.addJavascriptAlertOnLoad(iwc, getResourceBundle().getLocalizedString("course.remove_error", "You can not remove a course that has choices attached to it."));
				}
				showList(iwc);
				break;
			}
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	protected int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}

	public Course saveCourse(IWContext iwc) {
		String pk = iwc.getParameter(PARAMETER_COURSE_PK);
		String name = iwc.getParameter(PARAMETER_NAME);
		String user = iwc.getParameter(PARAMETER_USER);
		String courseTypePK = getCourseTypeId(iwc);
		String coursePricePK = iwc.getParameter(PARAMETER_COURSE_PRICE_PK);
		String accountingKey = iwc.getParameter(PARAMETER_ACCOUNTING_KEY);
		String sStartDate = iwc.getParameter(PARAMETER_VALID_FROM);
		String sEndDate = iwc.isParameterSet(PARAMETER_VALID_TO) ? iwc.getParameter(PARAMETER_VALID_TO) : null;
		String yearFrom = iwc.getParameter(PARAMETER_YEAR_FROM);
		String yearTo = iwc.getParameter(PARAMETER_YEAR_TO);
		String max = iwc.getParameter(PARAMETER_MAX_PER);
		String registrationEnd = iwc.isParameterSet(PARAMETER_REGISTRATION_END) ? iwc.getParameter(PARAMETER_REGISTRATION_END) : null;

		try {
			IWTimestamp startDate = new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(sStartDate));
			IWTimestamp endDate = sEndDate != null ? new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(sEndDate)) : null;
			IWTimestamp regEnd = registrationEnd != null ? new IWTimestamp(IWDatePickerHandler.getParsedTimestampByCurrentLocale(registrationEnd)) : null;
			int courseNumber = iwc.isParameterSet(PARAMETER_COURSE_NUMBER) ? Integer.parseInt(iwc.getParameter(PARAMETER_COURSE_NUMBER)) : -1;
			int birthYearFrom = StringUtil.isEmpty(yearFrom) ? -1 : Integer.parseInt(yearFrom);
			int birthYearTo = StringUtil.isEmpty(yearTo) ? -1 : Integer.parseInt(yearTo);
			int maxPer = StringUtil.isEmpty(max) ? -1 : Integer.parseInt(max);
			float price = iwc.isParameterSet(PARAMETER_PRICE) ? Float.parseFloat(iwc.getParameter(PARAMETER_PRICE)) : 0;
			float cost = iwc.isParameterSet(PARAMETER_COST) ? Float.parseFloat(iwc.getParameter(PARAMETER_COST)) : 0;
			boolean openForRegistration = iwc.isParameterSet(PARAMETER_OPEN_FOR_REGISTRATION);
			boolean hasPreCare = iwc.isParameterSet(PARAMETER_HAS_PRE_CARE) ? BooleanInput.getBooleanReturnValue(iwc.getParameter(PARAMETER_HAS_PRE_CARE)) : true;
			boolean hasPostCare = iwc.isParameterSet(PARAMETER_HAS_POST_CARE) ? BooleanInput.getBooleanReturnValue(iwc.getParameter(PARAMETER_HAS_POST_CARE)) : true;
			Object provider = getSession().getProvider() == null ? null : getSession().getProvider().getPrimaryKey();
			return getCourseBusiness().storeCourse(pk, courseNumber, name, user, courseTypePK, provider, coursePricePK, startDate, endDate, regEnd, accountingKey, birthYearFrom, birthYearTo, maxPer, price, cost, openForRegistration, hasPreCare, hasPostCare);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private Layer getNavigation(IWContext iwc) throws RemoteException {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("formSection");

		List<String> scriptFiles = new ArrayList<String>();
		scriptFiles.add("/dwr/interface/CourseDWRUtil.js");
		scriptFiles.add(CoreConstants.DWR_ENGINE_SCRIPT);
		scriptFiles.add(CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scriptFiles);

		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\");\n").append("\tdwr.util.addOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\", data);\n").append("}");

		StringBuffer script = new StringBuffer();
		script.append("function changeValues() {\n").append("\tvar val = dwr.util.getValue(\"" + PARAMETER_SCHOOL_TYPE_PK + "\");\n").append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(val, '" + iwc.getCurrentLocale().getCountry() + "', setOptions);\n").append("}");

		List<String> jsActions = new ArrayList<String>();
		jsActions.add(script2.toString());
		jsActions.add(script.toString());
		PresentationUtil.addJavaScriptActionsToBody(iwc, jsActions);

		if (!isSchoolUser()) {
			DropdownMenu providers = null;
			if (iwc.getAccessController().hasRole(CourseConstants.SUPER_ADMINISTRATOR_ROLE_KEY, iwc)) {
				providers = getAllProvidersDropdown(iwc);
			} else if (iwc.getAccessController().hasRole(CourseConstants.ADMINISTRATOR_ROLE_KEY, iwc)) {
				providers = getProvidersDropdown(iwc);
			}

			Collection<School> providersList = getBusiness().getProviders();
			if (providersList.size() == 1) {
				School school = providersList.iterator().next();
				getSession().setProvider(school);
				layer.add(new HiddenInput(PARAMETER_PROVIDER_PK, school.getPrimaryKey().toString()));
			} else if (providers != null) {
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
			Collection<SchoolType> schoolTypes = getBusiness().getSchoolTypes(getSession().getProvider());
			if (schoolTypes.size() == 1) {
				showTypes = false;
				type = schoolTypes.iterator().next();
				schoolType.setSelectedElement(type.getPrimaryKey().toString());
			}
			schoolType.addMenuElements(schoolTypes);
		} else {
			Collection<SchoolType> schoolTypes = getBusiness().getAllSchoolTypes();
			if (schoolTypes.size() == 1) {
				showTypes = false;
				type = schoolTypes.iterator().next();
				schoolType.setSelectedElement(type.getPrimaryKey().toString());
			}
			schoolType.addMenuElements(schoolTypes);
		}

		DropdownMenu courseType = new DropdownMenu(PARAMETER_COURSE_TYPE_PK);
		courseType.setId(PARAMETER_COURSE_TYPE_PK);
		courseType.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_course_type", "Select course type"));
		courseType.keepStatusOnAction(true);

		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_PK)) {
			Collection courseTypes = getBusiness().getCourseTypes(new Integer(iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK)), true);
			courseType.addMenuElements(courseTypes);
		} else if (type != null) {
			Collection courseTypes = getBusiness().getCourseTypes(new Integer(type.getPrimaryKey().toString()), true);
			courseType.addMenuElements(courseTypes);
		}

		IWTimestamp stamp = new IWTimestamp();
		stamp.addYears(1);

		IWDatePicker fromDate = new IWDatePicker(PARAMETER_FROM_DATE);
		fromDate.setShowYearChange(true);
		fromDate.keepStatusOnAction(true);

		IWDatePicker toDate = new IWDatePicker(PARAMETER_TO_DATE);
		toDate.setShowYearChange(true);
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
		} else if (type != null) {
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
			fromDate = new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(iwc.getParameter(PARAMETER_FROM_DATE))).getDate();
		}

		stamp.addMonths(1);
		stamp.addYears(1);
		Date toDate = stamp.getDate();
		if (iwc.isParameterSet(PARAMETER_TO_DATE)) {
			toDate = new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(iwc.getParameter(PARAMETER_TO_DATE))).getDate();
		}

		boolean showAllCourses = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_SHOW_ALL_COURSES, false);
		List<Course> courses = new ArrayList<Course>();
		if (getSession().getProvider() != null || showAllCourses) {
			try {
				courses = new ArrayList<Course>(getCourseBusiness().getCourses(-1, getSession().getProvider() != null ? getSession().getProvider().getPrimaryKey() : null, iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_PK) ? iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK) : (type != null ? type.getPrimaryKey() : null), iwc.isParameterSet(PARAMETER_COURSE_TYPE_PK) ? iwc.getParameter(PARAMETER_COURSE_TYPE_PK) : null, fromDate, toDate));
			} catch (RemoteException rex) {
				throw new IBORuntimeException(rex);
			}
		}
		if (showAllCourses) {
			Collections.reverse(courses);
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

		boolean showID = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_SHOW_ID_IN_NAME, false);
		boolean showLegend = false;

		group = table.createBodyRowGroup();
		int iRow = 1;
		Iterator<Course> iter = courses.iterator();
		while (iter.hasNext()) {
			Course course = iter.next();
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

				if (course.isOpenForRegistration()) {
					showLegend = true;
					row.setStyleClass("openForRegistration");
				}

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.setStyleClass("number");
				if (cType.getAbbreviation() != null) {
					cell.add(new Text(cType.getAbbreviation()));
				}
				cell.add(new Text(String.valueOf(showID ? course.getCourseNumber() : course.getPrimaryKey().toString())));

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
						} else {
							cell.add(new Text(sType.getName()));
						}
					}
				}

				cell = row.createCell();
				cell.setStyleClass("type");
				if (cType.getLocalizationKey() != null) {
					cell.add(new Text(localize(cType.getLocalizationKey(), cType.getName())));
				} else {
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
				} else {
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
				cell.setStyleClass("edit");
				cell.add(edit);

				cell = row.createCell();
				cell.setStyleClass("delete");
				cell.setStyleClass("lastColumn");
				cell.add(delete);

				if (iRow % 2 == 0) {
					row.setStyleClass("even");
				} else {
					row.setStyleClass("odd");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			iRow++;
		}
		form.add(table);

		if (showLegend) {
			Lists list = new Lists();
			list.setStyleClass("legend");
			form.add(list);

			ListItem item = new ListItem();
			item.setStyleClass("openForRegistration");
			item.add(new Text(getResourceBundle().getLocalizedString("open_for_registration", "Open for registration")));
			list.add(item);
		}

		if (getSession().getProvider() != null) {
			form.add(getCoursesListButtons());
		}

		add(form);
	}

	protected Layer getCoursesListButtons() {
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonLayer");

		if (getBackPage() != null) {
			GenericButton back = new GenericButton(localize("back", "Back"));
			back.setPageToOpen(getBackPage());
			buttonLayer.add(back);
		}

		SubmitButton newLink = new SubmitButton(localize("course.new", "New course"), PARAMETER_ACTION, String.valueOf(ACTION_NEW));
		buttonLayer.add(newLink);

		return buttonLayer;
	}

	protected boolean isUseFixedPrices() {
		if (useFixedPrice == null) {
			try {
				if (getSession().getProvider() != null) {
					Collection<SchoolType> schoolTypes = getBusiness().getSchoolTypes(getSession().getProvider());
					if (schoolTypes.size() == 1) {
						SchoolType type = schoolTypes.iterator().next();
						CourseCategory c = getBusiness().getCourseCategory(type.getPrimaryKey());
						useFixedPrice = c.useFixedPricing();
						return useFixedPrice;
					}
				}
			} catch (Exception e) {
			}
	
			useFixedPrice = IWMainApplication.getDefaultIWApplicationContext().getApplicationSettings()
				.getBoolean(CourseConstants.PROPERTY_USE_FIXED_PRICES, true);
		}
		return useFixedPrice;
	}

	protected Course getCourse(Object courseId) throws RemoteException {
		if (courseId == null) {
			return null;
		}

		if (course == null) {
			course = getCourseBusiness().getCourse(courseId);
			return course;
		}

		if (course.getPrimaryKey().toString().equals(courseId.toString())) {
			return course;
		}

		course = getCourseBusiness().getCourse(courseId);
		return course;
	}
	
	protected TextInput getUserInput(IWContext iwc, Course course) {
		TextInput user = new TextInput(PARAMETER_USER);
		if (course.getUser() != null) {
			user.setContent(course.getUser());
		}
		return user;
	}

	protected Form getEditorForm(IWContext iwc, Object coursePK) throws RemoteException {
		boolean useFixedPrices = isUseFixedPrices();
		boolean useBirthYears = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_BIRTHYEARS, true);
		boolean showIDInput = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_SHOW_ID_IN_NAME, false);
		boolean showOpenForRegistration = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_MANUALLY_OPEN_COURSES, false);
		boolean showCareOptions = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_SHOW_CARE_OPTIONS, false);

		Form form = new Form();
		form.setID("courseEditor");
		form.setStyleClass("adminForm");
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(coursePK != null ? ACTION_EDIT : ACTION_NEW)));
		form.maintainParameter(PARAMETER_FROM_DATE);
		form.maintainParameter(PARAMETER_TO_DATE);

		if (!useFixedPrices) {
			List<String> scripts = new ArrayList<String>();
			scripts.add("/dwr/interface/CourseDWRUtil.js");
			scripts.add(CoreConstants.DWR_ENGINE_SCRIPT);
			scripts.add(CoreConstants.DWR_UTIL_SCRIPT);
			PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

			StringBuffer script2 = new StringBuffer();
			script2.append("function setOptions(data) {\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\");\n").append("\tdwr.util.addOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\", data);\n").append("}");

			StringBuffer script = new StringBuffer();
			script.append("function changeValues() {\n").append("\tvar val = dwr.util.getValue(\"" + PARAMETER_SCHOOL_TYPE_PK + "\");\n").append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(val, '" + iwc.getCurrentLocale().getCountry() + "', setOptions);\n").append("}");

			StringBuffer script3 = new StringBuffer();
			script3.append("function setOptionsPrice(data) {\n").append("\tvar isEmpty = true;\n").append("\tfor (var prop in data) { isEmpty = false } \n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_PRICE_PK + "\");\n").append("\tdwr.util.addOptions(\"" + PARAMETER_COURSE_PRICE_PK + "\", data);\n").append("\tvar savebtn = document.getElementById(\"SAVE_BTN_ID\");\n").append("\tif (isEmpty == true) {\n").append("\t\tdwr.util.addOptions(\"" + PARAMETER_COURSE_PRICE_PK + "\",['" + localize("try_another_date", "Try another date") + "...']);\n").append("\t\tsavebtn.disabled=true;\n").append("\t\tdocument.getElementById(\"" + PARAMETER_COURSE_PRICE_PK + "\").disabled=true;\n").append("\t} else {\n").append("\t\tsavebtn.disabled=false;\n").append("\t\tdocument.getElementById(\"" + PARAMETER_COURSE_PRICE_PK + "\").disabled=false;\n").append("\t}\n").append("}");

			StringBuffer script4 = new StringBuffer();
			script4.append("function changeValuesPrice() {\n").append("\tvar date = dwr.util.getValue(\"" + PARAMETER_VALID_FROM_ID + "\");\n").append("\tvar val = " + getSession().getProvider().getPrimaryKey().toString() + ";\n").append("\tvar val2 = dwr.util.getValue(\"" + PARAMETER_COURSE_TYPE_PK + "\");\n").append("\tCourseDWRUtil.getCoursePricesDWR(date, val, val2, '" + iwc.getCurrentLocale().getCountry() + "', setOptionsPrice);").append("}");

			StringBuffer script5 = new StringBuffer();
			script5.append("function readPrice() {\n\tvar id = dwr.util.getValue(\"" + PARAMETER_COURSE_PRICE_PK + "\");\n\tCourseDWRUtil.getPriceDWR(id, fillPrice);\n}");

			StringBuffer script6 = new StringBuffer();
			script6.append("function fillPrice(aprice) {\n\tprice = aprice;\n\tdwr.util.setValues(price);\n}");

			Script formScript = new Script();
			formScript.addFunction("setOptions", script2.toString());
			formScript.addFunction("changeValues", script.toString());
			formScript.addFunction("setOptionsPrice", script3.toString());
			formScript.addFunction("changeValuesPrice", script4.toString());
			formScript.addFunction("readPrice", script5.toString());
			formScript.addFunction("fillPrice", script6.toString());
			form.add(formScript);
		}

		Course course = getCourse(coursePK);
		if (course != null && getSession().getProvider() == null) {
			getSession().setProvider(course.getProvider());
		}

		TextInput inputCourseNumber = new TextInput(PARAMETER_COURSE_NUMBER);
		TextInput inputName = new TextInput(PARAMETER_NAME);
		IWDatePicker inputFrom = new IWDatePicker(PARAMETER_VALID_FROM);
		inputFrom.setShowYearChange(true);
		IWDatePicker inputTo = new IWDatePicker(PARAMETER_VALID_TO);
		inputTo.setShowYearChange(true);
		TextInput inputAccounting = new TextInput(PARAMETER_ACCOUNTING_KEY);
		IntegerInput inputYearFrom = new IntegerInput(PARAMETER_YEAR_FROM);
		inputYearFrom.setMaxlength(4);
		IntegerInput inputYearTo = new IntegerInput(PARAMETER_YEAR_TO);
		inputYearTo.setMaxlength(4);
		IntegerInput inputMaxPer = new IntegerInput(PARAMETER_MAX_PER);
		TextInput inputUser = getUserInput(iwc, course);
		IWDatePicker registrationEnd = new IWDatePicker(PARAMETER_REGISTRATION_END);
		registrationEnd.setShowTime(true);
		registrationEnd.setUseCurrentDateIfNotSet(false);

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
		
		BooleanInput hasPreCare = new BooleanInput(PARAMETER_HAS_PRE_CARE);
		hasPreCare.setSelected(true);
		
		BooleanInput hasPostCare = new BooleanInput(PARAMETER_HAS_POST_CARE);
		hasPostCare.setSelected(true);

		DropdownMenu schoolTypeID = null;
		if (isShowCourseCategory()) {
			schoolTypeID = new DropdownMenu(PARAMETER_SCHOOL_TYPE_PK);
			schoolTypeID.setId(PARAMETER_SCHOOL_TYPE_PK);
			// schoolTypeID.setOnChange("changeValues();");
			schoolTypeID.setToSubmit(true);
			schoolTypeID.keepStatusOnAction(true);
			schoolTypeID.addMenuElementFirst("-1", getResourceBundle().getLocalizedString("select_school_type", "Select school type"));
		}

		DropdownMenu courseTypeID = null;
		if (isShowCourseType()) {
			courseTypeID = new DropdownMenu(PARAMETER_COURSE_TYPE_PK);
			courseTypeID.setId(PARAMETER_COURSE_TYPE_PK);
			courseTypeID.addMenuElementFirst("-1", getResourceBundle().getLocalizedString("select_course_type", "Select course type"));
		}

		boolean showTypes = true;
		Object schoolTypePK = null;

		Collection cargoTypes = null;
		Collection schoolTypes = getSession().getProvider() == null ? null : getCourseBusiness().getSchoolTypes(getSession().getProvider());
		if (schoolTypes != null) {
			if (schoolTypeID != null && schoolTypes.size() > 1) {
				schoolTypeID.addMenuElements(schoolTypes);
			} else if (schoolTypes.size() == 1) {
				SchoolType type = (SchoolType) schoolTypes.iterator().next();
				showTypes = false;
				form.add(new HiddenInput(PARAMETER_SCHOOL_TYPE_PK, type.getPrimaryKey().toString()));
				schoolTypePK = type.getPrimaryKey();
			}
		}

		DropdownMenu priceDrop = new DropdownMenu(PARAMETER_COURSE_PRICE_PK);
		priceDrop.setId(PARAMETER_COURSE_PRICE_PK);
		priceDrop.setOnChange("readPrice();");

		CheckBox openForRegistration = new CheckBox(PARAMETER_OPEN_FOR_REGISTRATION);

		if (course != null) {
			CourseType type = course.getCourseType();
			CourseCategory category = type.getCourseCategory();
			if (this.useFixedPrice == null) {
				useFixedPrices = category == null ? false : category.useFixedPricing();
			}
			
			School provider = course.getProvider();
			CoursePrice coursePrice = course.getPrice();

			if (course.getCourseNumber() > 0) {
				inputCourseNumber.setContent(String.valueOf(course.getCourseNumber()));
			}

			inputName.setContent(course.getName());
			
			inputFrom.setDate(course.getStartDate());
			if (course.getEndDate() != null) {
				inputTo.setDate(course.getEndDate());
			}
			String stID = category == null ? null : category.getPrimaryKey().toString();
			if (schoolTypeID != null) {
				schoolTypeID.setSelectedElement(stID);
			}
			inputAccounting.setValue(course.getAccountingKey());
			if (useBirthYears) {
				inputYearFrom.setValue(course.getBirthyearFrom());
				inputYearTo.setValue(course.getBirthyearTo());
			}
			inputMaxPer.setValue(course.getMax());

			if (isShowCourseType()) {
				cargoTypes = getCourseBusiness().getCourseTypes(new Integer(stID), true);
				courseTypeID.addMenuElements(cargoTypes);
				courseTypeID.setSelectedElement(type.getPrimaryKey().toString());
			}
			
			if (course.getRegistrationEnd() != null) {
				registrationEnd.setDate(new IWTimestamp(course.getRegistrationEnd()).getDate());
			}

			openForRegistration.setChecked(course.isOpenForRegistration());
			hasPreCare.setSelected(course.hasPreCare());
			hasPostCare.setSelected(course.hasPostCare());

			if (coursePrice != null) {
				if (!useFixedPrices) {
					price.setContent(Integer.toString(coursePrice.getPrice()));
					preCarePrice.setContent(coursePrice.getPreCarePrice() > 0 ? Integer.toString(coursePrice.getPreCarePrice()) : "0");
					postCarePrice.setContent(coursePrice.getPostCarePrice() > 0 ? Integer.toString(coursePrice.getPostCarePrice()) : "0");

					try {
						Collection prices = getCourseBusiness().getCoursePriceHome().findAll(provider.getSchoolArea(), type);
						priceDrop.addMenuElements(prices);
						priceDrop.setSelectedElement(course.getPrice().getPrimaryKey().toString());
					} catch (IDORelationshipException e) {
						e.printStackTrace();
					} catch (FinderException e) {
						e.printStackTrace();
					}
				} else if (course.getCoursePrice() >= 0) {
					price.setContent(String.valueOf((int) course.getCoursePrice()));
					price.setDisabled(false);

					courseCost.setContent(course.getCourseCost() > -1 ? String.valueOf((int) course.getCourseCost()) : "");
				}
			} else if (course.getCoursePrice() >= 0) {
				price.setContent(String.valueOf((int) course.getCoursePrice()));
				price.setDisabled(false);

				courseCost.setContent(course.getCourseCost() > -1 ? String.valueOf((int) course.getCourseCost()) : "");
			}

			form.add(new HiddenInput(PARAMETER_COURSE_PK, coursePK.toString()));
		} else {
			if (isShowCourseType() && schoolTypes != null && schoolTypes.iterator().hasNext()) {
				cargoTypes = getCourseBusiness().getCourseTypes((Integer) ((SchoolType) schoolTypes.iterator().next()).getPrimaryKey(), true);
				courseTypeID.addMenuElements(cargoTypes);
			}

			priceDrop.addMenuElement("-1", localize("select_a_date_and_search", "Select a date and search"));
			priceDrop.setDisabled(true);
			inputCourseNumber.setContent(String.valueOf(getCourseBusiness().getNextCourseNumber()));
		}

		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_PK)) {
			schoolTypePK = iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK);
		}

		if (isShowCourseType() && schoolTypePK != null) {
			CourseCategory category = getCourseBusiness().getCourseCategory(schoolTypePK);
			useFixedPrices = category.useFixedPricing();
			price.setDisabled(!useFixedPrices);

			cargoTypes = getCourseBusiness().getCourseTypes(new Integer(category.getPrimaryKey().toString()), true);
			courseTypeID.removeElements();
			courseTypeID.addMenuElements(cargoTypes);
			if (course != null) {
				courseTypeID.setSelectedElement(course.getCourseType().getPrimaryKey().toString());
			}
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

		if (showIDInput) {
			layer = new Layer(Layer.DIV);
			layer.setStyleClass("formItem");
			label = new Label(localize("id", "ID"), inputCourseNumber);
			layer.add(label);
			layer.add(inputCourseNumber);
			section.add(layer);
		}

		if (showTypes && isShowCourseCategory()) {
			layer = new Layer(Layer.DIV);
			layer.setID("category");
			layer.setStyleClass("formItem");
			label = new Label(localize("category", "Category"), schoolTypeID);
			layer.add(label);
			layer.add(schoolTypeID);
			section.add(layer);
		}

		if (isShowCourseType()) {
			layer = new Layer(Layer.DIV);
			layer.setID("type");
			layer.setStyleClass("formItem");
			label = new Label(localize("type", "Type"), courseTypeID);
			layer.add(label);
			layer.add(courseTypeID);
			section.add(layer);
		}

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
		} else {
			helpLayer.add(new Text(localize("course.length_search_explanation", "Select a start date and click \"Search for length\" to populate" + " the length dropdown. If nothing is found you will be prompted to search again. If a length is found you can select one and" + " proceed with the form.")));
		}
		section.add(helpLayer);

		layer = new Layer(Layer.DIV);
		layer.setID("start_date");
		layer.setStyleClass("formItem");
		label = new Label();
		label.setLabel(localize("start_date", "Start date"));
		inputFrom.setID(PARAMETER_VALID_FROM_ID);
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
		} else {
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
			layer.setStyleClass("formItem");
			label = new Label(localize("registration_end", "Registration end"), registrationEnd);
			layer.add(label);
			layer.add(registrationEnd);
			section.add(layer);

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

		if (showOpenForRegistration) {
			layer = new Layer();
			layer.setID("openForRegistration");
			layer.setStyleClass("formItem");
			layer.setStyleClass("checkboxFormItem");
			label = new Label(localize("open_for_registration", "Open for registration"), openForRegistration);
			layer.add(openForRegistration);
			layer.add(label);
			section.add(layer);
		}
		
		if (showCareOptions) {
			layer = new Layer();
			layer.setStyleClass("formItem");
			layer.setStyleClass("checkboxFormItem");
			label = new Label(localize("has_pre_care", "Has pre care"), hasPreCare);
			layer.add(label);
			layer.add(hasPreCare);
			section.add(layer);

			layer = new Layer();
			layer.setStyleClass("formItem");
			layer.setStyleClass("checkboxFormItem");
			label = new Label(localize("has_post_care", "Has post care"), hasPostCare);
			layer.add(label);
			layer.add(hasPostCare);
			section.add(layer);
		}

		section.add(new CSSSpacer());

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
		} else {
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
		} else {
			layer = new Layer(Layer.DIV);
			layer.setStyleClass("formItem");
			label = new Label(localize("course_cost", "Course cost"), courseCost);
			layer.add(label);
			layer.add(courseCost);
			section.add(layer);
		}

		return form;
	}

	public void showEditor(IWContext iwc, Object coursePK) throws RemoteException {
		Form form = getEditorForm(iwc, coursePK);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);
		section.add(new CSSSpacer());

		Course course = getCourse(coursePK);
		form.add(getEditorButtons(course));

		add(form);
	}

	protected Layer getEditorButtons(Course course) {
		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonLayer");

		SubmitButton cancel = new SubmitButton(localize("cancel", "Cancel"));
		cancel.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		buttonLayer.add(cancel);

		SubmitButton save = new SubmitButton(localize("save", "Save"));
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		if (!isUseFixedPrices()) {
			save.setId("SAVE_BTN_ID");
		}
		buttonLayer.add(save);

		return buttonLayer;
	}

	public CourseBusiness getCourseBusiness() {
		try {
			return IBOLookup.getServiceInstance(getIWApplicationContext(), CourseBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	public boolean isShowTypes() {
		return showTypes;
	}

	public void setShowTypes(boolean showTypes) {
		this.showTypes = showTypes;
	}

	public boolean isShowCourseCategory() {
		return showCourseCategory;
	}

	public void setShowCourseCategory(boolean showCourseCategory) {
		this.showCourseCategory = showCourseCategory;
	}

	public boolean isShowCourseType() {
		return showCourseType;
	}

	public void setShowCourseType(boolean showCourseType) {
		this.showCourseType = showCourseType;
	}

	protected String getCourseTypeId(IWContext iwc) {
		return iwc.isParameterSet(PARAMETER_COURSE_TYPE_PK) ? iwc.getParameter(PARAMETER_COURSE_TYPE_PK) : null;
	}

	public Boolean getUseFixedPrice() {
		return useFixedPrice;
	}

	public void setUseFixedPrice(Boolean useFixedPrice) {
		this.useFixedPrice = useFixedPrice;
	}
}