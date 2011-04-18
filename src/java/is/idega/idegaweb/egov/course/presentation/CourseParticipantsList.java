/*
 * $Id$ Created on Mar 28, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.citizen.presentation.CitizenFinder;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.CourseChoiceComparator;
import is.idega.idegaweb.egov.course.business.CourseParticipantsWriter;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseCertificate;
import is.idega.idegaweb.egov.course.data.CourseCertificateHome;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.presentation.bean.CourseParticipantListRowData;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Phone;
import com.idega.core.file.data.ICFile;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Abbreviation;
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
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.PresentationUtil;
import com.idega.util.text.Name;

public class CourseParticipantsList extends CourseBlock {

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_STORE = 2;

	public static final String PARAMETER_SHOW_COURSE_PARTICIPANT_INFO = "prm_show_course_participant_info";

	protected SchoolType type = null;
	
	protected ICPage changeEmailResponsePage = null;

	protected static final String PARAMETER_USER_PK = "cf_user_pk";
	protected static final String PARAMETER_USER_UNIQUE_ID = "cf_user_unique_id";
	protected static final String PARAMETER_SORTING = "prm_sorting";
	protected static final String PARAMETER_COURSE_PARTICIPANT_PAYMENT = "cf_participant_payment_";

	@Override
	public void present(IWContext iwc) {
		try {
			parseAction(iwc);
			
			Form form = new Form();
			form.setID("courseList");
			form.setStyleClass("adminForm");
			form.setEventListener(this.getClass());
			form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));

			form.add(getNavigation(iwc));
			if (iwc.isParameterSet(PARAMETER_COURSE_PK)) {
				form.add(getPrintouts(iwc));
			}
			form.add(getParticipants(iwc));

			Layer buttonLayer = new Layer();
			buttonLayer.setStyleClass("buttonLayer");
			form.add(buttonLayer);

			boolean showNoPayment = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_SHOW_NO_PAYMENT, false);
			if (getBackPage() != null) {
				GenericButton back = new GenericButton(localize("back", "Back"));
				back.setPageToOpen(getBackPage());
				buttonLayer.add(back);
			}
			if (iwc.isParameterSet(PARAMETER_COURSE_PK) && iwc.hasRole(CourseConstants.ADMINISTRATOR_ROLE_KEY) && showNoPayment) {
				SubmitButton store = new SubmitButton(localize("store", "Store"));
				store.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STORE));
				buttonLayer.add(store);
			}
			
			add(form);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	private void parseAction(IWContext iwc) throws RemoteException {
		int action = ACTION_VIEW;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		if (action == ACTION_STORE) {
			String[] choices = iwc.getParameterValues(PARAMETER_COURSE_PARTICIPANT_PK);
			for (String choice : choices) {
				boolean noPayment = iwc.isParameterSet(PARAMETER_COURSE_PARTICIPANT_PAYMENT + choice);
				getBusiness().setNoPayment(choice, noPayment);
			}
		}
	}

	protected Layer getNavigation(IWContext iwc) throws RemoteException {
		boolean showAllCourses = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_SHOW_ALL_COURSES, false);

		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("formSection");

		List scripts = new ArrayList();
		scripts.add("/dwr/interface/CourseDWRUtil.js");
		scripts.add(CoreConstants.DWR_ENGINE_SCRIPT);
		scripts.add(CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

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
		List functions = new ArrayList();
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
			Collection schoolTypes = getBusiness().getSchoolTypes(getSession().getProvider());
			if (schoolTypes.size() == 1) {
				showTypes = false;
				type = (SchoolType) schoolTypes.iterator().next();
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
			Collection courseTypes = getBusiness().getCourseTypes(typePK, true);
			courseType.addMenuElements(courseTypes);
		}
		else if (type != null) {
			typePK = new Integer(type.getPrimaryKey().toString());
			Collection courseTypes = getBusiness().getCourseTypes(typePK, true);
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
			
			for (int i = inceptionYear; i <= currentYear + 1; i++) {
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
			List courses = new ArrayList(getBusiness().getCourses(-1, getSession().getProvider() != null ? getSession().getProvider().getPrimaryKey() : null, typePK, iwc.isParameterSet(PARAMETER_COURSE_TYPE_PK) ? iwc.getParameter(PARAMETER_COURSE_TYPE_PK) : null, fromDate, toDate));

			if (showAllCourses) {
				Collections.reverse(courses);
			}
			
			Iterator iter = courses.iterator();
			while (iter.hasNext()) {
				Course element = (Course) iter.next();
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

		DropdownMenu sorting = new DropdownMenu(PARAMETER_SORTING);
		sorting.addMenuElement(CourseChoiceComparator.NAME_SORT, getResourceBundle().getLocalizedString("sort.name", "Name (A-Z)"));
		sorting.addMenuElement(CourseChoiceComparator.DATE_SORT, getResourceBundle().getLocalizedString("sort.date", "Date"));
		sorting.keepStatusOnAction(true);
		
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

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(getResourceBundle().getLocalizedString("sorting", "Sorting"), sorting);
		formItem.add(label);
		formItem.add(sorting);
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
		link.setMediaWriterClass(CourseParticipantsWriter.class);

		return link;
	}

	protected Table2 getParticipants(IWContext iwc, boolean addViewParticipantLink, boolean addCheckboxes) throws RemoteException {
		if (addCheckboxes) {
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, getBundle().getVirtualPathWithFileNameString("javascript/CourseParticipantsListHelper.js"));
		}

		boolean showCertificates = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_SHOW_CERTIFICATES, false);
		boolean showNoPayment = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_SHOW_NO_PAYMENT, false) && iwc.hasRole(CourseConstants.ADMINISTRATOR_ROLE_KEY);

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

		int columns = 1;

		cell = row.createHeaderCell();
		cell.setStyleClass("name");
		cell.add(new Text(getResourceBundle().getLocalizedString("name", "Name")));
		columns++;

		cell = row.createHeaderCell();
		cell.setStyleClass("personalID");
		cell.add(new Text(getResourceBundle().getLocalizedString("personal_id", "Personal ID")));
		columns++;

		if (!addCheckboxes) {
			cell = row.createHeaderCell();
			cell.setStyleClass("address");
			cell.add(new Text(getResourceBundle().getLocalizedString("address", "Address")));
			columns++;

			cell = row.createHeaderCell();
			cell.setStyleClass("postalCode");
			cell.add(new Text(getResourceBundle().getLocalizedString("postal_code", "Postal code")));
			columns++;

			cell = row.createHeaderCell();
			if (!addViewParticipantLink && !showCertificates && !showNoPayment) {
				cell.setStyleClass("lastColumn");
			}
			cell.setStyleClass("homePhone");
			cell.add(new Text(getResourceBundle().getLocalizedString("home_phone", "Phone")));
			columns++;
			
			if (showNoPayment) {
				cell = row.createHeaderCell();
				if (!addViewParticipantLink && !showCertificates) {
					cell.setStyleClass("lastColumn");
				}
				cell.setStyleClass("noPayment");
				cell.add(new Text(getResourceBundle().getLocalizedString("no_payment", "No payment")));
				columns++;
			}

			if (showCertificates) {
				cell = row.createHeaderCell();
				cell.setStyleClass("created");
				cell.add(new Text(getResourceBundle().getLocalizedString("register_date", "Register date")));
				columns++;

				cell = row.createHeaderCell();
				if (!addViewParticipantLink) {
					cell.setStyleClass("lastColumn");
				}
				cell.setStyleClass("certificate");
				cell.add(new Text(getResourceBundle().getLocalizedString("pdf", "PDF")));
				columns++;
			}
		}

		List checkboxesInfo = null;
		if (addCheckboxes) {
			cell = row.createHeaderCell();
			cell.setStyleClass("created");
			cell.add(new Text(getResourceBundle().getLocalizedString("register_date", "Register date")));
			columns++;

			checkboxesInfo = getBusiness().getCheckBoxesForCourseParticipants(getResourceBundle());
			AdvancedProperty info = null;
			for (int i = 0; i < checkboxesInfo.size(); i++) {
				info = (AdvancedProperty) checkboxesInfo.get(i);

				cell = row.createHeaderCell();
				if (i + 1 == checkboxesInfo.size()) {
					cell.setStyleClass("lastColumn");
				}
				cell.setStyleClass(info.getValue());
				cell.setStyleClass("courseChoiceManagement");
				cell.add(new Abbreviation(getResourceBundle().getLocalizedString(info.getId() + "_abbr", info.getId() + "_abbr"), getResourceBundle().getLocalizedString(info.getId(), info.getId())));
				columns++;
			}

			if (showCertificates) {
				cell = row.createHeaderCell();
				if (!addViewParticipantLink) {
					cell.setStyleClass("lastColumn");
				}
				cell.setStyleClass("certificate");
				cell.add(new Text(getResourceBundle().getLocalizedString("pdf", "PDF")));
				columns++;
			}
		}

		if (addViewParticipantLink) {
			cell = row.createHeaderCell();
			if (!addCheckboxes) {
				cell.setStyleClass("lastColumn");
			}
			cell.setStyleClass("view");
			cell.add(new Text(getResourceBundle().getLocalizedString("view", "View")));
			columns++;
		}

		group = table.createBodyRowGroup();
		int iRow = 1;

		Course course = null;
		CourseType type = null;
		List choices = new ArrayList();
		if (iwc.isParameterSet(PARAMETER_COURSE_PK)) {
			choices = new ArrayList(getBusiness().getCourseChoices(iwc.getParameter(PARAMETER_COURSE_PK), false));
			course = getBusiness().getCourse(iwc.getParameter(PARAMETER_COURSE_PK));
			type = course.getCourseType();
			if (type.getAbbreviation() != null) {
				table.setStyleClass("abbr_" + type.getAbbreviation());
			}
			Collections.sort(choices, new CourseChoiceComparator(iwc.getCurrentLocale(), iwc.isParameterSet(PARAMETER_SORTING) ? Integer.parseInt(iwc.getParameter(PARAMETER_SORTING)) : CourseChoiceComparator.DATE_SORT));
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
			cell.add(new HiddenInput(PARAMETER_COURSE_PARTICIPANT_PK, choice.getPrimaryKey().toString()));

			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());

			cell = row.createCell();
			cell.setStyleClass("name");
			if (getResponsePage() != null) {
				Link link = new Link(name.getName(iwc.getCurrentLocale()));
				link.addParameter(PARAMETER_CHOICE_PK, choice.getPrimaryKey().toString());
				link.setPage(getResponsePage());

				cell.add(link);
			}
			else if (addViewParticipantLink) {
				Link view = new Link(name.getName(iwc.getCurrentLocale()));
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
				view.addParameter(PARAMETER_CHOICE_PK, choice.getPrimaryKey().toString());
				view.addParameter(PARAMETER_ACTION, 1);
				view.addParameter(PARAMETER_SHOW_COURSE_PARTICIPANT_INFO, Boolean.TRUE.toString());
				cell.add(view);
			}
			else {
				cell.add(new Text(name.getName(iwc.getCurrentLocale())));
			}

			cell = row.createCell();
			cell.setStyleClass("personalID");
			if (getChangeEmailResponsePage() != null) {
				Link link = new Link(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale()));
				link.setEventListener(CitizenFinder.class);
				if (user.getUniqueId() != null) {
					link.addParameter(PARAMETER_USER_UNIQUE_ID, user.getUniqueId());
				}
				else {
					link.addParameter(PARAMETER_USER_PK, user.getPrimaryKey().toString());
				}
				link.setPage(getChangeEmailResponsePage());
				
				cell.add(link);
			} else {
				cell.add(new Text(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())));
			}

			if (!addCheckboxes) {
				cell = row.createCell();
				cell.setStyleClass("address");
				if (address != null) {
					cell.add(new Text(address.getStreetAddress()));
				}
				else {
					cell.add(new Text(CoreConstants.MINUS));
				}

				cell = row.createCell();
				cell.setStyleClass("postalCode");
				if (postalCode != null) {
					cell.add(new Text(postalCode.getPostalAddress()));
				}
				else {
					cell.add(new Text(CoreConstants.MINUS));
				}

				cell = row.createCell();
				if (!addViewParticipantLink && !showCertificates && !showNoPayment) {
					cell.setStyleClass("lastColumn");
				}
				cell.setStyleClass("homePhone");
				if (phone != null) {
					cell.add(new Text(phone.getNumber()));
				}
				else {
					cell.add(new Text(CoreConstants.MINUS));
				}

				if (showNoPayment) {
					cell = row.createCell();
					if (!addViewParticipantLink && !showCertificates) {
						cell.setStyleClass("lastColumn");
					}
					cell.setStyleClass("noPayment");
					
					CheckBox box = new CheckBox(PARAMETER_COURSE_PARTICIPANT_PAYMENT + choice.getPrimaryKey().toString());
					box.setChecked(choice.isNoPayment());
					cell.add(box);
				}

				if (showCertificates) {
					cell = row.createCell();
					cell.setStyleClass("created");
					cell.add(new Text(new IWTimestamp(choice.getApplication().getCreated()).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)));

					cell = row.createCell();
					if (!addViewParticipantLink) {
						cell.setStyleClass("lastColumn");
					}
					cell.setStyleClass("certificate");

					ICFile file = null;
					CourseCertificate certificate = getBusiness().getUserCertificate(user, course);
					if (certificate != null) {
						file = certificate.getCertificateFile();
					}

					if (file == null) {
						cell.add(new Text("-"));
					}
					else {
						Link printCertificate = new Link(getBundle().getImage("pdf-small.gif", getResourceBundle().getLocalizedString("print_certificate", "Print certificate")));
						printCertificate.setTarget(Link.TARGET_BLANK_WINDOW);
						printCertificate.setFile(file);
						cell.add(printCertificate);
					}
				}
			}
			if (addCheckboxes) {
				cell = row.createCell();
				cell.setStyleClass("created");
				cell.add(new Text(new IWTimestamp(choice.getApplication().getCreated()).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)));

				AdvancedProperty info = null;
				CheckBox box = null;
				boolean disabled = false;
				boolean show = true;
				List rowData = getBusiness().getCourseParticipantListRowData(choice, getResourceBundle());
				if (rowData == null || checkboxesInfo.size() != rowData.size()) {
					throw new RemoteException("Can not add checkboxes to list!");
				}
				CourseParticipantListRowData data = null;
				for (int i = 0; i < checkboxesInfo.size(); i++) {
					info = (AdvancedProperty) checkboxesInfo.get(i);
					data = (CourseParticipantListRowData) rowData.get(i);

					cell = row.createCell();
					cell.setStyleClass("courseChoiceManagement");
					cell.setStyleClass(info.getValue());

					disabled = data.isDisabled();
					show = data.isShow();

					box = getCourseChoiseManagementCheckbox(info, choice, loadingMessage, disabled);

					if (data.isForceToCheck()) {
						box.setChecked(true);
					}

					if (show) {
						cell.add(box);
					}
					else {
						cell.add(new Text(CoreConstants.MINUS));
					}
				}

				if (showCertificates) {
					cell = row.createCell();
					if (!addViewParticipantLink) {
						cell.setStyleClass("lastColumn");
					}
					cell.setStyleClass("certificate");

					ICFile file = null;
					CourseCertificate certificate = getBusiness().getUserCertificate(user, course);
					if (certificate != null) {
						file = certificate.getCertificateFile();
					}

					if (file == null) {
						cell.add(new Text("-"));
					}
					else {
						Link printCertificate = new Link(getBundle().getImage("pdf-small.gif", getResourceBundle().getLocalizedString("print_certificate", "Print certificate")));
						printCertificate.setTarget(Link.TARGET_BLANK_WINDOW);
						printCertificate.setFile(file);
						cell.add(printCertificate);
					}
				}
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
				view.addParameter(PARAMETER_CHOICE_PK, choice.getPrimaryKey().toString());
				view.addParameter(PARAMETER_ACTION, 1);
				view.addParameter(PARAMETER_SHOW_COURSE_PARTICIPANT_INFO, Boolean.TRUE.toString());
				cell.add(view);
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
		cell.setColumnSpan(columns);
		cell.add(new Text(getResourceBundle().getLocalizedString("number_of_participants", "Number of participants") + ": " + (iRow - 1)));

		return table;
	}

	private CheckBox getCourseChoiseManagementCheckbox(AdvancedProperty info, CourseChoice choise, String message, boolean disabled) {
		CheckBox box = new CheckBox(info.getValue());
		box.setDisabled(disabled);
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

	protected CourseCertificateHome getCourseCertificateHome() {
		CourseCertificateHome home = null;
		try {
			home = (CourseCertificateHome) IDOLookup.getHome(CourseCertificate.class);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		return home;
	}
	
	protected ICPage getChangeEmailResponsePage() {
		return this.changeEmailResponsePage;
	}

	public void setChangeEmailResponsePage(ICPage responsePage) {
		this.changeEmailResponsePage = responsePage;
	}

}