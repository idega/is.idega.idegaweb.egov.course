/*
 * $Id: CourseWaitingList.java,v 1.6 2009/05/25 14:58:07 laddi Exp $ Created on Mar 28, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.CourseParticipantsWriter;
import is.idega.idegaweb.egov.course.data.ChildCourse;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.data.CourseProviderType;
import is.idega.idegaweb.egov.course.data.CourseType;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.ArrayUtil;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.PresentationUtil;
import com.idega.util.StringUtil;
import com.idega.util.text.Name;

public class CourseWaitingList extends CourseBlock {

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_ACCEPT = 2;
	
	private CourseProviderType type = null;

	@Override
	public void present(IWContext iwc) {
		try {
			Form form = new Form();
			form.setID("courseList");
			form.setStyleClass("adminForm");
			form.setEventListener(this.getClass());
			form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));

			boolean success = parseAction(iwc);
			if (!success) {
				PresentationUtil.addJavascriptAlertOnLoad(iwc, localize(
						"accept_choices.max_reached", 
						"You can not accept beyond max for course."));
			}
			
			form.add(getNavigation(iwc));
			if (iwc.isParameterSet(PARAMETER_COURSE_PK)) {
				form.add(getPrintouts(iwc));
			}
			form.add(getParticipants(iwc));

			Layer buttonLayer = new Layer();
			buttonLayer.setStyleClass("buttonLayer");
			form.add(buttonLayer);

			if (iwc.isParameterSet(PARAMETER_COURSE_PK)) {
				SubmitButton accept = new SubmitButton(localize("accept_choices", "Accept choices"));
				accept.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_ACCEPT));
				accept.setSubmitConfirm(localize("accept_choices.confirm", "Are you sure you want to accept the selected choices?"));
				buttonLayer.add(accept);
			}
			
			if (getBackPage() != null) {
				GenericButton back = new GenericButton(localize("back", "Back"));
				back.setPageToOpen(getBackPage());
				buttonLayer.add(back);
			}

			add(form);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private boolean parseAction(IWContext iwc) throws RemoteException {
		int action = ACTION_VIEW;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		if (action == ACTION_ACCEPT) {
			String[] choices = iwc.getParameterValues(PARAMETER_CHOICE_PK);
			if (ArrayUtil.isEmpty(choices)) {
				return Boolean.FALSE;
			}

			Course course = getBusiness().getCourse(iwc.getParameter(PARAMETER_COURSE_PK));
			if (course == null) {
				return Boolean.FALSE;
			}

			if (course.getFreePlaces(false) >= choices.length) {
				for (String choice : choices) {
					getBusiness().acceptChoice(choice, iwc.getCurrentLocale());
				}

				return true;
			}

			return false;
		}
		
		return true;
	}

	protected Layer getNavigation(IWContext iwc) throws RemoteException {
		boolean showAllCourses = iwc.getApplicationSettings().getBoolean(
				CourseConstants.PROPERTY_SHOW_ALL_COURSES, false);

		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("formSection");

		List<String> scripts = new ArrayList<String>();
		scripts.add("/dwr/interface/CourseDWRUtil.js");
		scripts.add(CoreConstants.DWR_ENGINE_SCRIPT);
		scripts.add(CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

		DropdownMenu providers = getProvidersDropdown(iwc);
		if (providers != null) {
			providers.setToSubmit();

			Layer formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			Label label = new Label(getResourceBundle().getLocalizedString("provider", "Provider"), providers);
			formItem.add(label);
			formItem.add(providers);
			layer.add(formItem);
		}

		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\");\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_PK + "\");\n").append("\tdwr.util.addOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\", data);\n").append("}");
		StringBuffer script = new StringBuffer();
		script.append("function changeValues() {\n").append("\tvar val = +$(\"" + PARAMETER_SCHOOL_TYPE_PK + "\").value;\n").append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(val, '" + iwc.getCurrentLocale().getCountry() + "', setOptions);\n").append("}");
		StringBuffer script3 = new StringBuffer();
		script3.append("function setCourseOptions(data) {\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_PK + "\");\n").append("\tdwr.util.addOptions(\"" + PARAMETER_COURSE_PK + "\", data);\n").append("}");
		StringBuffer script4 = new StringBuffer();
		if (showAllCourses) {
			script4.append("function changeCourseValues() {\n").append("\tCourseDWRUtil.getCourseMapDWR('" + (getSession().getProvider() != null ? getSession().getProvider().getPrimaryKey().toString() : "-1") + "', dwr.util.getValue('" + PARAMETER_SCHOOL_TYPE_PK + "'), dwr.util.getValue('" + PARAMETER_COURSE_TYPE_PK + "'), '" + iwc.getCurrentLocale().getCountry() + "', setCourseOptions);\n").append("}");
		}
		else {
			script4.append("function changeCourseValues() {\n").append("\tCourseDWRUtil.getCoursesMapDWR('" + (getSession().getProvider() != null ? getSession().getProvider().getPrimaryKey().toString() : "-1") + "', dwr.util.getValue('" + PARAMETER_SCHOOL_TYPE_PK + "'), dwr.util.getValue('" + PARAMETER_COURSE_TYPE_PK + "'), dwr.util.getValue('" + PARAMETER_YEAR + "'), '" + iwc.getCurrentLocale().getCountry() + "', setCourseOptions);\n").append("}");
		}
		List<String> functions = new ArrayList<String>();
		functions.add(script2.toString());
		functions.add(script.toString());
		functions.add(script3.toString());
		functions.add(script4.toString());
		PresentationUtil.addJavaScriptActionsToBody(iwc, functions);

		DropdownMenu schoolType = new DropdownMenu(PARAMETER_SCHOOL_TYPE_PK);
		schoolType.setId(PARAMETER_SCHOOL_TYPE_PK);
		schoolType.setOnChange("changeValues();");
		schoolType.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_school_type", "Select school type"));
		schoolType.keepStatusOnAction(true);

		boolean showTypes = true;
		if (getSession().getProvider() != null) {
			Collection<CourseProviderType> schoolTypes = getBusiness()
					.getSchoolTypes(getSession().getProvider());
			if (schoolTypes.size() == 1) {
				showTypes = false;
				type = (CourseProviderType) schoolTypes.iterator().next();
				schoolType.setSelectedElement(type.getPrimaryKey().toString());
			}
			schoolType.addMenuElements(schoolTypes);
		}

		DropdownMenu courseType = new DropdownMenu(PARAMETER_COURSE_TYPE_PK);
		courseType.setId(PARAMETER_COURSE_TYPE_PK);
		courseType.setOnChange("changeCourseValues();");
		courseType.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_course_type", "Select course type"));
		courseType.keepStatusOnAction(true);

		Integer typePK = null;
		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_PK)) {
			typePK = new Integer(iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK));
			Collection<CourseType> courseTypes = getBusiness().getCourseTypes(typePK, true);
			courseType.addMenuElements(courseTypes);
		}
		else if (type != null) {
			typePK = new Integer(type.getPrimaryKey().toString());
			Collection<CourseType> courseTypes = getBusiness().getCourseTypes(typePK, true);
			courseType.addMenuElements(courseTypes);
		}

		int inceptionYear = Integer.parseInt(iwc.getApplicationSettings().getProperty(CourseConstants.PROPERTY_INCEPTION_YEAR, "-1"));
		int currentYear = new IWTimestamp().getYear();
		int year = showAllCourses ? -1 : currentYear;
		Date fromDate = null;
		Date toDate = null;
		if (iwc.isParameterSet(PARAMETER_YEAR)) {
			year = Integer.parseInt(iwc.getParameter(PARAMETER_YEAR));
		}
		if (year > 0) {
			fromDate = new IWTimestamp(1, 1, year).getDate();
			toDate = new IWTimestamp(31, 12, year).getDate();
		}
		
		DropdownMenu yearMenu = new DropdownMenu(PARAMETER_YEAR);
		if (inceptionYear > 0) {
			yearMenu.keepStatusOnAction(true);
			yearMenu.setID(PARAMETER_YEAR);
			yearMenu.setOnChange("changeCourseValues();");
			yearMenu.setSelectedElement(year);
			
			for (int i = inceptionYear; i <= currentYear + 4; i++) {
				yearMenu.addMenuElement(i, String.valueOf(i));
			}
		}

		DropdownMenu course = new DropdownMenu(PARAMETER_COURSE_PK);
		course.setId(PARAMETER_COURSE_PK);
		course.keepStatusOnAction(true);
		course.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_course", "Select course"));
		course.setToSubmit();

		if ((getSession().getProvider() != null && typePK != null) || showAllCourses) {
			boolean showIDInName = getIWApplicationContext().getApplicationSettings().getBoolean(CourseConstants.PROPERTY_SHOW_ID_IN_NAME, false);
			Collection<Course> courses = getBusiness().getCourses(
					-1, 
					getSession().getProvider() != null ? getSession().getProvider().getPrimaryKey().toString() : null,
					typePK.toString(), 
					iwc.isParameterSet(PARAMETER_COURSE_TYPE_PK) ? iwc.getParameter(PARAMETER_COURSE_TYPE_PK) : null, 
					fromDate, toDate);

			for (Course element: courses) {
				String name = "";
				if (showIDInName) {
					CourseType type = element.getCourseType();

					name += element.getCourseNumber() + " - ";

					if (type.getAbbreviation() != null && type.showAbbreviation()) {
						name += type.getAbbreviation() + " ";
					}
				}
				name += element.getName();
				course.addMenuElement(element.getPrimaryKey().toString(), name);
			}
		}

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

		if (!showAllCourses && inceptionYear > 0) {
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(getResourceBundle().getLocalizedString("year", "Year"), yearMenu);
			formItem.add(label);
			formItem.add(yearMenu);
			layer.add(formItem);
		}

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(getResourceBundle().getLocalizedString("course", "Course"), course);
		formItem.add(label);
		formItem.add(course);
		layer.add(formItem);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		layer.add(clearLayer);

		return layer;
	}

	public Layer getPrintouts(IWContext iwc) {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("printIcons");

		layer.add(getXLSLink(iwc));

		return layer;
	}

	protected Link getXLSLink(IWContext iwc) {
		DownloadLink link = new DownloadLink(getBundle().getImage("xls.gif"));
		link.setStyleClass("xls");
		link.setTarget(Link.TARGET_NEW_WINDOW);
		Course course = getCourse(
				iwc.getParameter(PARAMETER_COURSE_PK), 
				iwc.getParameter(PARAMETER_PROVIDER_PK));
		if (course != null) {
			link.addParameter(PARAMETER_COURSE_PK, course.getPrimaryKey().toString());
		}
		
		if (iwc.isParameterSet(PARAMETER_COURSE_PARTICIPANT_PK)) {
			link.maintainParameter(PARAMETER_COURSE_PARTICIPANT_PK, iwc);
		}
		link.addParameter(CourseParticipantsWriter.PARAMETER_WAITING_LIST, Boolean.FALSE.toString());
		link.setMediaWriterClass(CourseParticipantsWriter.class);

		return link;
	}
	
	protected Course getCourse(String primaryKey, String courseProviderId) {
		if (!StringUtil.isEmpty(primaryKey)) {
			Course course;
			try {
				course = getBusiness().getCourse(primaryKey);
			} catch (RemoteException e) {
				return null;
			}

			if (course != null) {
				Collection<String> privoderIds = null;
				if (!StringUtil.isEmpty(courseProviderId)) {
					privoderIds = Arrays.asList(courseProviderId);
				}

				Collection<ChildCourse> childCourses = getChildCourseHome()
						.findChildCourses(course, privoderIds);
				if (!ListUtil.isEmpty(childCourses)) {
					return childCourses.iterator().next();
				} else {
					return course;
				}
			}
		}

		return null;
	}

	protected Collection<CourseChoice> getCourseChoices(
			Course course) {
		if (course != null) {
			try {
				return getBusiness().getCourseChoices(course, true);
			} catch (RemoteException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + CourseChoice.class.getSimpleName() +
						"'s for course:" + course.getName() + 
						" cause of: ", e);
			}
		}

		return Collections.emptyList();
	}
	
	protected Table2 getParticipants(IWContext iwc) throws RemoteException {
		Table2 table = new Table2();
		table.setStyleClass("adminTable");
		table.setStyleClass("ruler");
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("number");
		cell.add(Text.getNonBrakingSpace());

		cell = row.createHeaderCell();
		cell.setStyleClass("name");
		cell.add(new Text(getResourceBundle().getLocalizedString("name", "Name")));

		cell = row.createHeaderCell();
		cell.setStyleClass("personalID");
		cell.add(new Text(getResourceBundle().getLocalizedString("personal_id", "Personal ID")));

		cell = row.createHeaderCell();
		cell.setStyleClass("address");
		cell.add(new Text(getResourceBundle().getLocalizedString("address", "Address")));

		cell = row.createHeaderCell();
		cell.setStyleClass("postalCode");
		cell.add(new Text(getResourceBundle().getLocalizedString("postal_code", "Postal code")));

		cell = row.createHeaderCell();
		cell.setStyleClass("homePhone");
		cell.add(new Text(getResourceBundle().getLocalizedString("home_phone", "Phone")));

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("select");
		cell.add(Text.getNonBrakingSpace());

		group = table.createBodyRowGroup();
		int iRow = 1;

		Course course = getCourse(
				iwc.getParameter(PARAMETER_COURSE_PK),
				iwc.getParameter(PARAMETER_PROVIDER_PK));

		Collection<CourseChoice> choices = getCourseChoices(course);
		for (CourseChoice choice : choices) {
			row = group.createRow();

			User user = choice.getUser();
			Address address = getUserBusiness().getUsersMainAddress(user);
			PostalCode postalCode = null;
			if (address != null) {
				postalCode = address.getPostalCode();
			}
			Phone phone = getUserBusiness().getChildHomePhone(user);
			
			CheckBox box = new CheckBox(
					PARAMETER_CHOICE_PK, 
					choice.getPrimaryKey().toString());
			box.setDisabled(choice.getUniqueID() != null);

			if (iRow == course.getMax()) {
				row.setStyleClass("lastAvailable");
			}
			else if (iRow == (course.getMax() + 1)) {
				row.setStyleClass("firstExceedingParticipant");
			}

			if (iRow > course.getMax()) {
				row.setStyleClass("exceedingParticipant");
			}
			if (choice.getUniqueID() != null) {
				row.setStyleClass("hasOffer");
			}

			cell = row.createCell();
			cell.setStyleClass("firstColumn");
			cell.setStyleClass("number");
			cell.add(new Text(String.valueOf(iRow)));

			Name name = null;
			if (user != null) {
				name = new Name(
						user.getFirstName(), 
						user.getMiddleName(), 
						user.getLastName());
			} else {
				name = new Name();
				name.setFirstName(CoreConstants.MINUS);
			}

			cell = row.createCell();
			cell.setStyleClass("name");
			if (getResponsePage() != null) {
				Link link = new Link(name.getName(iwc.getCurrentLocale()));
				link.addParameter(PARAMETER_CHOICE_PK, choice.getPrimaryKey().toString());
				link.setPage(getResponsePage());

				cell.add(link);
			}
			else {
				cell.add(new Text(name.getName(iwc.getCurrentLocale())));
			}

			cell = row.createCell();
			cell.setStyleClass("personalID");
			if (user != null) {
				cell.add(new Text(PersonalIDFormatter.format(
						user.getPersonalID(), iwc.getCurrentLocale())));
			} else {
				cell.add(new Text(CoreConstants.MINUS));
			}

			cell = row.createCell();
			cell.setStyleClass("address");
			if (address != null && !StringUtil.isEmpty(address.getStreetAddress())) {
				cell.add(new Text(address.getStreetAddress()));
			}
			else {
				cell.add(new Text(CoreConstants.MINUS));
			}

			cell = row.createCell();
			cell.setStyleClass("postalCode");
			if (postalCode != null && !StringUtil.isEmpty(postalCode.getPostalCode())) {
				cell.add(new Text(postalCode.getPostalCode()));
			} else {
				cell.add(new Text(CoreConstants.MINUS));
			}

			cell = row.createCell();
			cell.setStyleClass("homePhone");
			if (phone != null && !StringUtil.isEmpty(phone.getNumber())) {
				cell.add(new Text(phone.getNumber()));
			} else {
				cell.add(new Text(CoreConstants.MINUS));
			}

			cell = row.createCell();
			cell.setStyleClass("lastColumn");
			cell.setStyleClass("select");
			cell.add(box);

			if (iRow % 2 == 0) {
				row.setStyleClass("evenRow");
			}
			else {
				row.setStyleClass("oddRow");
			}
			iRow++;
		}

		group = table.createFooterRowGroup();
		row = group.createRow();

		cell = row.createCell();
		cell.setStyleClass("numberOfParticipants");
		cell.setColumnSpan(7);
		cell.add(new Text(getResourceBundle().getLocalizedString("number_of_participants", "Number of participants") + ": " + (iRow - 1)));

		return table;
	}
}