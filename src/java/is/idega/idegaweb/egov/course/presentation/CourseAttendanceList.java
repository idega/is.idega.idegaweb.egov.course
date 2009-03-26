/*
 * $Id$ Created on Mar 28, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.CourseAttendanceWriter;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseCategory;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.data.CourseType;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.data.School;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

public class CourseAttendanceList extends CourseBlock {

	private static final String PARAMETER_COURSE_TYPE = "prm_course_type";
	private static final String PARAMETER_SCHOOL_TYPE = "prm_school_type";

	@Override
	public void present(IWContext iwc) {
		try {
			Form form = new Form();
			form.setID("courseList");
			form.setStyleClass("adminForm");
			form.setEventListener(this.getClass());

			form.add(getNavigation(iwc));
			if (iwc.isParameterSet(PARAMETER_COURSE_PK)) {
				form.add(getPrintouts(iwc));
			}
			form.add(getParticipants(iwc));

			if (getBackPage() != null) {
				Layer buttonLayer = new Layer();
				buttonLayer.setStyleClass("buttonLayer");
				form.add(buttonLayer);

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

	protected Layer getNavigation(IWContext iwc) throws RemoteException {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("formSection");

		super.getParentPage().addJavascriptURL("/dwr/interface/CourseDWRUtil.js");
		super.getParentPage().addJavascriptURL("/dwr/engine.js");
		super.getParentPage().addJavascriptURL("/dwr/util.js");

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

		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_TYPE + "\");\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_PK + "\");\n").append("\tdwr.util.addOptions(\"" + PARAMETER_COURSE_TYPE + "\", data);\n").append("}");

		StringBuffer script = new StringBuffer();
		script.append("function changeValues() {\n").append("\tvar val = +$(\"" + PARAMETER_SCHOOL_TYPE + "\").value;\n").append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(val, '" + iwc.getCurrentLocale().getCountry() + "', setOptions);\n").append("}");

		StringBuffer script3 = new StringBuffer();
		script3.append("function setCourseOptions(data) {\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_PK + "\");\n").append("\tdwr.util.addOptions(\"" + PARAMETER_COURSE_PK + "\", data);\n").append("}");

		StringBuffer script4 = new StringBuffer();
		script4.append("function changeCourseValues() {\n").append("\tCourseDWRUtil.getCourseMapDWR('" + (getSession().getProvider() != null ? getSession().getProvider().getPrimaryKey().toString() : "-1") + "', dwr.util.getValue('" + PARAMETER_SCHOOL_TYPE + "'), dwr.util.getValue('" + PARAMETER_COURSE_TYPE + "'), '" + iwc.getCurrentLocale().getCountry() + "', setCourseOptions);\n").append("}");

		super.getParentPage().getAssociatedScript().addFunction("setOptions", script2.toString());
		super.getParentPage().getAssociatedScript().addFunction("changeValues", script.toString());
		super.getParentPage().getAssociatedScript().addFunction("setCourseOptions", script3.toString());
		super.getParentPage().getAssociatedScript().addFunction("changeCourseValues", script4.toString());

		DropdownMenu schoolType = new DropdownMenu(PARAMETER_SCHOOL_TYPE);
		schoolType.setId(PARAMETER_SCHOOL_TYPE);
		schoolType.setOnChange("changeValues();");
		schoolType.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_school_type", "Select school type"));
		schoolType.keepStatusOnAction(true);

		if (getSession().getProvider() != null) {
			Collection schoolTypes = getBusiness().getSchoolTypes(getSession().getProvider());
			schoolType.addMenuElements(schoolTypes);
		}

		DropdownMenu courseType = new DropdownMenu(PARAMETER_COURSE_TYPE);
		courseType.setId(PARAMETER_COURSE_TYPE);
		courseType.setOnChange("changeCourseValues();");
		courseType.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_course_type", "Select course type"));
		courseType.keepStatusOnAction(true);

		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE)) {
			Collection courseTypes = getBusiness().getCourseTypes(new Integer(iwc.getParameter(PARAMETER_SCHOOL_TYPE)));
			courseType.addMenuElements(courseTypes);
		}

		DropdownMenu course = new DropdownMenu(PARAMETER_COURSE_PK);
		course.setId(PARAMETER_COURSE_PK);
		course.keepStatusOnAction(true);
		course.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_course", "Select course"));

		if (getSession().getProvider() != null && iwc.isParameterSet(PARAMETER_SCHOOL_TYPE) && iwc.isParameterSet(PARAMETER_COURSE_TYPE)) {
			Collection courses = getBusiness().getCourses(-1, getSession().getProvider().getPrimaryKey(), iwc.getParameter(PARAMETER_SCHOOL_TYPE), iwc.getParameter(PARAMETER_COURSE_TYPE), null, null);
			course.addMenuElements(courses);
		}

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Label label = new Label(getResourceBundle().getLocalizedString("category", "Category"), schoolType);
		formItem.add(label);
		formItem.add(schoolType);
		layer.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(getResourceBundle().getLocalizedString("type", "Type"), courseType);
		formItem.add(label);
		formItem.add(courseType);
		layer.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(getResourceBundle().getLocalizedString("course", "Course"), course);
		formItem.add(label);
		formItem.add(course);
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
		link.maintainParameter(PARAMETER_COURSE_PK, iwc);
		link.setMediaWriterClass(CourseAttendanceWriter.class);

		return link;
	}

	protected Table2 getParticipants(IWContext iwc) throws RemoteException {
		boolean hasCare = true;
		Collection choices = new ArrayList();
		if (getSession().getProvider() != null && iwc.isParameterSet(PARAMETER_COURSE_PK)) {
			Course course = getBusiness().getCourse(iwc.getParameter(PARAMETER_COURSE_PK));
			CourseType type = course.getCourseType();
			CourseCategory category = type.getCourseCategory();
			hasCare = category.hasCare();

			choices = getBusiness().getCourseChoices(iwc.getParameter(PARAMETER_COURSE_PK), false);
		}

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
		if (!hasCare) {
			cell.setStyleClass("lastColumn");
		}
		cell.add(new Text(getResourceBundle().getLocalizedString("personal_id", "Personal ID")));

		if (hasCare) {
			cell = row.createHeaderCell();
			cell.setStyleClass("preCare");
			cell.add(new Text(getResourceBundle().getLocalizedString("pre_care", "Pre care")));

			cell = row.createHeaderCell();
			cell.setStyleClass("postCare");
			cell.add(new Text(getResourceBundle().getLocalizedString("post_care", "Post care")));

			cell = row.createHeaderCell();
			cell.setStyleClass("lastColumn");
			cell.setStyleClass("pickedUp");
			cell.add(new Text(getResourceBundle().getLocalizedString("picked_up", "Picked up")));
		}

		group = table.createBodyRowGroup();
		int iRow = 1;

		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			row = group.createRow();

			CourseChoice choice = (CourseChoice) iter.next();
			User user = choice.getUser();
			boolean preCare = choice.getDayCare() == CourseConstants.DAY_CARE_PRE || choice.getDayCare() == CourseConstants.DAY_CARE_PRE_AND_POST;
			boolean postCare = choice.getDayCare() == CourseConstants.DAY_CARE_POST || choice.getDayCare() == CourseConstants.DAY_CARE_PRE_AND_POST;

			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());

			cell = row.createCell();
			cell.setStyleClass("firstColumn");
			cell.setStyleClass("number");
			cell.add(new Text(String.valueOf(iRow)));

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
			if (!hasCare) {
				cell.setStyleClass("lastColumn");
			}
			cell.add(new Text(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())));

			if (hasCare) {
				cell = row.createCell();
				cell.setStyleClass("preCare");
				cell.add(new Text(getBooleanValue(iwc, preCare)));

				cell = row.createCell();
				cell.setStyleClass("postCare");
				cell.add(new Text(getBooleanValue(iwc, postCare)));

				cell = row.createCell();
				cell.setStyleClass("lastColumn");
				cell.setStyleClass("pickedUp");
				cell.add(new Text(getBooleanValue(iwc, choice.isPickedUp())));
			}

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
		cell.setColumnSpan(hasCare ? 6 : 3);
		cell.add(new Text(getResourceBundle().getLocalizedString("number_of_participants", "Number of participants") + ": " + (iRow - 1)));

		return table;
	}
}