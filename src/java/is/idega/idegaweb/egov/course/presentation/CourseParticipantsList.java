/*
 * $Id$ Created on Mar 28, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.CourseParticipantsWriter;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.data.CourseChoiceBMPBean;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.block.school.data.School;
import com.idega.builder.bean.AdvancedProperty;
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
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.PresentationUtil;
import com.idega.util.text.Name;

public class CourseParticipantsList extends CourseBlock {

	protected String PARAMETER_SHOW_COURSE_PARTICIPANT_INFO = "prm_show_course_participant_info";
	
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

		List scripts = new ArrayList();
		scripts.add("/dwr/interface/CourseDWRUtil.js");
		scripts.add("/dwr/engine.js");
		scripts.add("/dwr/util.js");
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n").append("\tDWRUtil.removeAllOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\");\n").append("\tDWRUtil.removeAllOptions(\"" + PARAMETER_COURSE_PK + "\");\n").append("\tDWRUtil.addOptions(\"" + PARAMETER_COURSE_TYPE_PK + "\", data);\n").append("}");
		StringBuffer script = new StringBuffer();
		script.append("function changeValues() {\n").append("\tvar val = +$(\"" + PARAMETER_SCHOOL_TYPE_PK + "\").value;\n").append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(val, '" + iwc.getCurrentLocale().getCountry() + "', setOptions);\n").append("}");
		StringBuffer script3 = new StringBuffer();
		script3.append("function setCourseOptions(data) {\n").append("\tDWRUtil.removeAllOptions(\"" + PARAMETER_COURSE_PK + "\");\n").append("\tDWRUtil.addOptions(\"" + PARAMETER_COURSE_PK + "\", data);\n").append("}");
		StringBuffer script4 = new StringBuffer();
		script4.append("function changeCourseValues() {\n").append("\tCourseDWRUtil.getCourseMapDWR('" + (getSession().getProvider() != null ? getSession().getProvider().getPrimaryKey().toString() : "-1") + "', DWRUtil.getValue('" + PARAMETER_SCHOOL_TYPE_PK + "'), DWRUtil.getValue('" + PARAMETER_COURSE_TYPE_PK + "'), '" + iwc.getCurrentLocale().getCountry() + "', setCourseOptions);\n").append("}");
		List functions = new ArrayList();
		functions.add(script2.toString());
		functions.add(script.toString());
		functions.add(script3.toString());
		functions.add(script4.toString());
		PresentationUtil.addJavaScriptActionsToBody(iwc, functions);

		if (!isSchoolUser()) {
			DropdownMenu providers = null;
			if (iwc.getAccessController().hasRole(CourseConstants.SUPER_ADMINISTRATOR_ROLE_KEY, iwc)) {
				providers = getAllProvidersDropdown(iwc);
			}
			else if (iwc.getAccessController().hasRole(CourseConstants.ADMINISTRATOR_ROLE_KEY, iwc)) {
				providers = getProvidersDropdown(iwc);
			}

			if (providers != null) {
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
			schoolType.addMenuElements(schoolTypes);
		}

		DropdownMenu courseType = new DropdownMenu(PARAMETER_COURSE_TYPE_PK);
		courseType.setId(PARAMETER_COURSE_TYPE_PK);
		courseType.setOnChange("changeCourseValues();");
		courseType.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_course_type", "Select course type"));
		courseType.keepStatusOnAction(true);

		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_PK)) {
			Collection courseTypes = getBusiness().getCourseTypes(new Integer(iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK)));
			courseType.addMenuElements(courseTypes);
		}

		DropdownMenu course = new DropdownMenu(PARAMETER_COURSE_PK);
		course.setId(PARAMETER_COURSE_PK);
		course.keepStatusOnAction(true);
		course.addMenuElementFirst("", getResourceBundle().getLocalizedString("select_course", "Select course"));

		if (getSession().getProvider() != null && iwc.isParameterSet(PARAMETER_SCHOOL_TYPE_PK) && iwc.isParameterSet(PARAMETER_COURSE_TYPE_PK)) {
			Collection courses = getBusiness().getCourses(-1, getSession().getProvider().getPrimaryKey(), iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK), iwc.getParameter(PARAMETER_COURSE_TYPE_PK));
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
		if (iwc.isParameterSet(PARAMETER_COURSE_PARTICIPANT_PK)) {
			link.maintainParameter(PARAMETER_COURSE_PARTICIPANT_PK, iwc);
		}
		link.setMediaWriterClass(CourseParticipantsWriter.class);

		return link;
	}
	
	private List getCheckboxesInfo() {
		List info = new ArrayList();
		
		info.add(new AdvancedProperty(getResourceBundle().getLocalizedString("verification_from_goverment_office", "Verfication from government office"),
				CourseChoiceBMPBean.COLUMN_VERIFICATION_FROM_GOVERMENT_OFFICE));
		info.add(new AdvancedProperty(getResourceBundle().getLocalizedString("certificate_of_property", "Certificate of property"),
				CourseChoiceBMPBean.COLUMN_CERTIFICATE_OF_PROPERTY));
		info.add(new AdvancedProperty(getResourceBundle().getLocalizedString("criminal_record", "Criminal record"),
				CourseChoiceBMPBean.COLUMN_CRIMINAL_RECORD));
		info.add(new AdvancedProperty(getResourceBundle().getLocalizedString("verification_of_payment", "Verification of payment"),
				CourseChoiceBMPBean.COLUMN_VERIFICATION_OF_PAYMENT));
		info.add(new AdvancedProperty(getResourceBundle().getLocalizedString("need_verification_from_goverment_office", "Needs verification from goverment office"),
				CourseChoiceBMPBean.COLUMN_NEED_VERIFICATION_FROM_GOVERMENT_OFFICE));
		info.add(new AdvancedProperty(getResourceBundle().getLocalizedString("did_not_show_up", "Did not show up"),
				CourseChoiceBMPBean.COLUMN_DID_NOT_SHOW_UP));
		info.add(new AdvancedProperty(getResourceBundle().getLocalizedString("passed_course", "Has passed course"),
				CourseChoiceBMPBean.COLUMN_PASSED));
		
		return info;
	}
	
	protected Table2 getParticipants(IWContext iwc, boolean addViewParticipantLink, boolean addCheckboxes) throws RemoteException {
		if (addCheckboxes) {
			StringBuffer action = new StringBuffer("function manageCourseChoiceSettings(parameters) {showLoadingMessage(parameters[0]); var box = document.getElementById(parameters[1]); CourseDWRUtil.manageCourseChoiceSettings(parameters[2], box.name, box.checked, {callback: function(result) {closeAllLoadingMessages();}});}");
			PresentationUtil.addJavaScriptActionToBody(iwc, action.toString());
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
		cell.add(new Text(getResourceBundle().getLocalizedString("personal_id", "Personal ID")));

		cell = row.createHeaderCell();
		cell.setStyleClass("address");
		cell.add(new Text(getResourceBundle().getLocalizedString("address", "Address")));

		cell = row.createHeaderCell();
		cell.setStyleClass("postalCode");
		cell.add(new Text(getResourceBundle().getLocalizedString("postal_code", "Postal code")));

		cell = row.createHeaderCell();
		if (!addViewParticipantLink && !addCheckboxes) {
			cell.setStyleClass("lastColumn");
		}
		cell.setStyleClass("homePhone");
		cell.add(new Text(getResourceBundle().getLocalizedString("home_phone", "Phone")));
		
		if (addViewParticipantLink) {
			cell = row.createHeaderCell();
			if (!addCheckboxes) {
				cell.setStyleClass("lastColumn");
			}
			cell.setStyleClass("view");
			cell.add(new Text(getResourceBundle().getLocalizedString("view", "View")));
		}
		List checkboxesInfo = null;
		if (addCheckboxes) {
			checkboxesInfo = getCheckboxesInfo();
			AdvancedProperty info = null;
			for (int i = 0; i < checkboxesInfo.size(); i++) {
				info = (AdvancedProperty) checkboxesInfo.get(i);
				
				cell = row.createHeaderCell();
				if (i + 1 == checkboxesInfo.size()) {
					cell.setStyleClass("lastColumn");
				}
				cell.setStyleClass("courseChoiseManagement");
				cell.add(new Text(info.getId()));
			}
		}

		group = table.createBodyRowGroup();
		int iRow = 1;

		Course course = null;
		Collection choices = new ArrayList();
		if (getSession().getProvider() != null && iwc.isParameterSet(PARAMETER_COURSE_PK)) {
			choices = getBusiness().getCourseChoices(iwc.getParameter(PARAMETER_COURSE_PK));
			course = getBusiness().getCourse(iwc.getParameter(PARAMETER_COURSE_PK));
		}

		String courseId = course == null ? null : course.getPrimaryKey().toString();
		String schoolId = null;
		if (course != null) {
			School school = course.getProvider();
			schoolId = school == null ? null : school.getPrimaryKey().toString();
		}
		String schoolTypeId = iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK);
		String courseTypeId = iwc.getParameter(PARAMETER_COURSE_TYPE_PK);
		Iterator iter = choices.iterator();
		String loadingMessage = getResourceBundle().getLocalizedString("loading", "Loading");
		while (iter.hasNext()) {
			row = group.createRow();

			CourseChoice choice = (CourseChoice) iter.next();
			User user = choice.getUser();
			Address address = getUserBusiness().getUsersMainAddress(user);
			PostalCode postalCode = null;
			if (address != null) {
				postalCode = address.getPostalCode();
			}
			Phone phone = getUserBusiness().getChildHomePhone(user);

			if (iRow == course.getMax()) {
				row.setStyleClass("lastAvailable");
			}
			else if (iRow == (course.getMax() + 1)) {
				row.setStyleClass("firstExceedingParticipant");
			}

			if (iRow > course.getMax()) {
				row.setStyleClass("exceedingParticipant");
			}

			cell = row.createCell();
			cell.setStyleClass("firstColumn");
			cell.setStyleClass("number");
			cell.add(new Text(String.valueOf(iRow)));

			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());

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
			cell.add(new Text(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())));

			cell = row.createCell();
			cell.setStyleClass("address");
			if (address != null) {
				cell.add(new Text(address.getStreetAddress()));
			}
			else {
				cell.add(new Text("-"));
			}

			cell = row.createCell();
			cell.setStyleClass("postalCode");
			if (postalCode != null) {
				cell.add(new Text(postalCode.getPostalAddress()));
			}
			else {
				cell.add(new Text("-"));
			}

			cell = row.createCell();
			if (!addViewParticipantLink && !addCheckboxes) {
				cell.setStyleClass("lastColumn");
			}
			cell.setStyleClass("homePhone");
			if (phone != null) {
				cell.add(new Text(phone.getNumber()));
			}
			else {
				cell.add(new Text("-"));
			}
			
			if (addViewParticipantLink) {
				cell = row.createCell();
				Link view = new Link(getBundle().getImage("images/edit.png", getResourceBundle().getLocalizedString("view", "View")));
				if (courseId != null) {
					view.addParameter(PARAMETER_COURSE_PK, courseId);
				}
				view.addParameter(PARAMETER_COURSE_PARTICIPANT_PK, user.getId());
				if (schoolId != null) {
					view.addParameter(PARAMETER_PROVIDER_PK, schoolId);
				}
				if (schoolTypeId != null) {
					view.addParameter(PARAMETER_SCHOOL_TYPE_PK, schoolTypeId);
				}
				if (courseTypeId != null) {
					view.addParameter(PARAMETER_COURSE_TYPE_PK, courseTypeId);
				}
				view.addParameter(PARAMETER_SHOW_COURSE_PARTICIPANT_INFO, Boolean.TRUE.toString());
				cell.add(view);
			}
			
			if (addCheckboxes) {
				AdvancedProperty info = null;
				for (int i = 0; i < checkboxesInfo.size(); i++) {
					info = (AdvancedProperty) checkboxesInfo.get(i);
				
					cell = row.createCell();
					if (i + 1 == checkboxesInfo.size()) {
						cell.setStyleClass("lastColumn");
					}
					cell.setStyleClass("courseChoiseManagement");
					cell.add(getCourseChoiseManagementCheckbox(info, choice, loadingMessage));
				}
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
		cell.setColumnSpan(6);
		cell.add(new Text(getResourceBundle().getLocalizedString("number_of_participants", "Number of participants") + ": " + (iRow - 1)));

		return table;
	}
	
	private CheckBox getCourseChoiseManagementCheckbox(AdvancedProperty info, CourseChoice choise, String message) {
		CheckBox box = new CheckBox(info.getValue());
		box.setChecked(choise.getBooleanValueFromColumn(info.getValue()));
		StringBuffer action = new StringBuffer("manageCourseChoiceSettings(['").append(message).append("', '").append(box.getId()).append("', '");
		action.append(choise.getPrimaryKey().toString()).append("']);");
		box.setOnClick(action.toString());
		return box;
	}
	
	protected Table2 getParticipants(IWContext iwc, boolean addViewParticipantLink) throws RemoteException {
		return getParticipants(iwc, addViewParticipantLink, false);
	}

	protected Table2 getParticipants(IWContext iwc) throws RemoteException {
		return getParticipants(iwc, false);
	}
}