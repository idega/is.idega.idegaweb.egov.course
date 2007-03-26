package is.idega.idegaweb.egov.course.presentation;

import is.idega.block.family.business.FamilyConstants;
import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoCustodianFound;
import is.idega.block.family.data.Child;
import is.idega.block.family.data.Custodian;
import is.idega.block.family.data.Relative;
import is.idega.idegaweb.egov.application.data.Application;
import is.idega.idegaweb.egov.application.presentation.ApplicationForm;
import is.idega.idegaweb.egov.course.business.CourseApplicationSession;
import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.business.CourseBusinessBean;
import is.idega.idegaweb.egov.course.business.CourseDWR;
import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseType;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Span;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

public class CourseApplication extends ApplicationForm{

	protected static final int ACTION_PHASE_1 = 1;
	protected static final int ACTION_PHASE_2 = 2;
	protected static final int ACTION_PHASE_3 = 3;
	protected static final int ACTION_PHASE_4 = 4;
	protected static final int ACTION_PHASE_5 = 5;
	protected static final int ACTION_PHASE_6 = 6;
	protected static final int ACTION_PHASE_7 = 7;
	protected static final int ACTION_PHASE_8 = 8;
	protected static final int ACTION_PHASE_9 = 9;
	protected static final int ACTION_OVERVIEW = 10;
	protected static final int ACTION_SAVE = 0;

	protected static final String DISPLAY_PHONEBOOK_QUESTION_KEY = "display_phonebook_question";

	protected static final String PARAMETER_ACTION = "prm_action";
	protected static final String PARAMETER_BACK = "prm_back";

	private static final String PARAMETER_USER = "prm_user_id";
	private static final String PARAMETER_RELATIVE = "prm_relative_user_id";
	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_HOME_PHONE = "prm_home";
	private static final String PARAMETER_WORK_PHONE = "prm_work";
	private static final String PARAMETER_MOBILE_PHONE = "prm_mobile";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_MARITAL_STATUS = "prm_marital_status";

	private static final String PARAMETER_RELATIVE_HOME_PHONE = "prm_relative_home";
	private static final String PARAMETER_RELATIVE_WORK_PHONE = "prm_relative_work";
	private static final String PARAMETER_RELATIVE_MOBILE_PHONE = "prm_relative_mobile";
	private static final String PARAMETER_RELATIVE_EMAIL = "prm_relative_email";
	private static final String PARAMETER_RELATION = "prm_relation";
	private static final String PARAMETER_RELATIVE_RELATION = "prm_relative_relation";

	protected static final String PARAMETER_GROWTH_DEVIATION = "prm_growth_deviation";
	protected static final String PARAMETER_GROWTH_DEVIATION_DETAILS = "prm_growth_deviation_details";
	protected static final String PARAMETER_ALLERGIES = "prm_allergies";
	protected static final String PARAMETER_ALLERGIES_DETAILS = "prm_allergies_details";
	protected static final String PARAMETER_LAST_CARE_PROVIDER = "prm_last_care_provider";
	protected static final String PARAMETER_CAN_CONTACT_LAST_PROVIDER = "prm_can_contact_last_provider";
	protected static final String PARAMETER_OTHER_INFORMATION = "prm_other_information";
	protected static final String PARAMETER_CAN_DISPLAY_PARENT_INFORMATION = "prm_can_display_parent_information";

	private static final String PARAMETER_CATEGORY = "prm_sch_cat";
	private static final String PARAMETER_COURSE_TYPE = "prm_cou_type";
	private static final String PARAMETER_COURSE_TABLE_ID = "prm_cou_course_table";
	private static final String PARAMETER_COURSE = "prm_cou_course";
	private static final String PARAMETER_DAYCARE = "prm_cou_daycare";
	private static final String PARAMETER_TRIPHOME = "prm_cou_trip";
	
	private static final String DAYCARE_MORNING = "1";
	private static final String DAYCARE_AFTERNOON = "2";
	private static final String DAYCARE_WHOLE_DAY = "3";
	private static final String TRIPHOME_PICKED_UP = "1";
	private static final String TRIPHOME_WALKS_HOME = "2";
	
	private static final String SUB_ACTION = "prm_subact";
	private static final String SUB_ACTION_ADD = "prm_subact_add";
	private static final String SUB_ACTION_REMOVE = "prm_subact_rem";
	
	private IWResourceBundle iwrb =  null;
	
	protected String getCaseCode() {
		return CourseBusinessBean.CASE_CODE_KEY;
	}
	public String getBundleIdentifier() {
		return CourseBusinessBean.IW_BUNDLE_IDENTIFIER;
	}
	protected void present(IWContext iwc) {
		this.iwrb = getResourceBundle(iwc);
		try {
			switch (parseAction(iwc)) {
				case ACTION_PHASE_1 : 
					showPhaseOne(iwc);
					break;
				case ACTION_PHASE_3 : 
					if (getApplicant(iwc) != null) {
						showPhaseThree(iwc, ACTION_PHASE_4, ACTION_PHASE_1, 3, 7, new String[]{"CHILD", "SSN"}, false, false);
					} else {
						showPhaseOne(iwc);
					}
					break;
				case ACTION_PHASE_4 : 
					if (getApplicant(iwc) != null) {
						showPhaseFour(iwc, ACTION_PHASE_5, ACTION_PHASE_3, 4, 7, new String[]{"CHILD", "SSN"}, false);
					} else {
						showPhaseOne(iwc);
					}
					break;
				case ACTION_PHASE_5 :
					if (getApplicant(iwc) != null) {
						showPhaseFive(iwc);
					} else {
						showPhaseOne(iwc);
					}
					break;
				case ACTION_PHASE_6 :
					if (getApplicant(iwc) != null) {
						
						String sub = iwc.getParameter(SUB_ACTION);
						if (SUB_ACTION_ADD.equals(sub)) {
							String[] pks = iwc.getParameterValues(PARAMETER_COURSE);
							String[] daycare = iwc.getParameterValues(PARAMETER_DAYCARE);
							String[] tripHome = iwc.getParameterValues(PARAMETER_TRIPHOME);
							if (pks != null) {
								for (int i = 0; i < pks.length; i++) {
									ApplicationHolder holder = new ApplicationHolder();
									holder.setCourse(getCourseBusiness(iwc).getCourse(new Integer(pks[i])));
									holder.setUser(getApplicant(iwc));
									holder.setDaycare(Integer.parseInt(daycare[i]));
									holder.setTripHome(Integer.parseInt(tripHome[i]));
									getCourseApplicationSession(iwc).addApplication(holder);
								}
							}
						}
						
						showPhaseSix(iwc);
					} else {
						showPhaseOne(iwc);
					}
					break;
				case ACTION_PHASE_7 :
					if (iwc.isParameterSet(PARAMETER_COURSE) || !getCourseApplicationSession(iwc).getApplications().isEmpty()) {
						showPhaseSeven(iwc);
					} else {
						Form form = getForm(ACTION_PHASE_6);
						setError(PARAMETER_COURSE, iwrb.getLocalizedString("select_at_least_one_course", "Select at least one course"));
						addErrors(iwc, form);
						showPhaseSix(iwc);
					}
					break;
					
					//				default :
//					add("PHASE = "+iwc.getParameter(PARAMETER_ACTION));
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private int parseAction(IWContext iwc) {
		int action = ACTION_PHASE_1;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}

		return action;
	}
	
	private User getApplicant(IWContext iwc) {
		String childID = iwc.getParameter("CHILD");
		String ssn = iwc.getParameter("SSN");
		if (childID != null && !childID.trim().equals("")) {
			try {
				return getUserBusiness(iwc).getUser(new Integer(childID));
			} catch (NumberFormatException e) {
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else if (ssn != null && !ssn.trim().equals("")) {
			try {
				return getUserBusiness(iwc).getUser(ssn);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private Form getForm(int state) {
		Form form = new Form();
		switch (state) {
			case ACTION_PHASE_1 :
				
				break;
			case ACTION_PHASE_3 :
			case ACTION_PHASE_4 :
			case ACTION_PHASE_5 :
			case ACTION_PHASE_6 :
			case ACTION_PHASE_8 :
				form.maintainParameter("CHILD");
				form.maintainParameter("SSN");
				break;
			case ACTION_PHASE_7 :
				form.maintainParameter("CHILD");
				form.maintainParameter("SSN");
//				form.maintainParameter(PARAMETER_COURSE);
				break;
		
		}
		
		return form;
	}
	
	private void showPhaseOne(IWContext iwc) {
		add(getPhasesHeader(iwrb.getLocalizedString("applicant", "Applicant"), 1, 7, true));
		User chosenUser = null; // check  session OR use current IF employee view
		User user = iwc.getCurrentUser();//null; // PARENT

		Form form = getForm(ACTION_PHASE_1);
		form.setId("course_step_1");
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1)));

		Layer info = new Layer(Layer.DIV);
		info.setStyleClass("info");

		form.add(info);

		Lists lists = new Lists();
		lists.add(iwrb.getLocalizedString("help.one_applicant_is_registered_at_a_time", "One applicant is registered at a time."));
		lists.add(iwrb.getLocalizedString("help.select_an_applicant_from_the_dropdown_OR_type_in_a_social_security_number", 
				"Select an applicant from the dropdown OR type in a social security number."));
		lists.add(iwrb.getLocalizedString("help.when_a_registration_is_complete_you_can_then_register_another_child", 
				"When registration is complete you can go back and register another."));
		lists.add(iwrb.getLocalizedString("help.when_all_applicants_have_been_registered_you_pay_for_them_all_at_the_same_time",
				"When all applicants have been registered you pay for them all at the same time."));
		info.add(lists);
		
		Heading1 heading = new Heading1(iwrb.getLocalizedString("select_an_applicant", "Select an applicant"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		
		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);
		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Label label;

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("select_applicant_helper_text", "Please select the applicant from the drop down list.")));
		section.add(helpLayer);

		
		try {
			DropdownMenu chooser = getUserChooser(iwc, user, chosenUser, "CHILD", iwrb);
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString("name", "Name"), chooser);
			formItem.add(label);
			formItem.add(chooser);
			section.add(formItem);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		heading = new Heading1(iwrb.getLocalizedString("type_social_security_number", "Type in social security number"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);
		
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		TextInput ssn = new TextInput("SSN");
		label = new Label(this.iwrb.getLocalizedString("social_security_number", "Social security number"), ssn);
		formItem.add(label);
		formItem.add(ssn);
		section.add(formItem);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("select_applicant_helper_text", "Please select the applicant from the drop down list.")));
		section.add(helpLayer);
		
		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link link = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3));
		link.setToFormSubmit(form);
		bottom.add(link);

		
		add(form);
	}

	protected void showPhaseThree(IWContext iwc, int nextPhase, int previousPhase, int currentPhase, int totalPhases, String[] maintainParameters, boolean validateForm, boolean showMaritalStatus) throws RemoteException {

		if (validateForm) {
//			if (!this.iHomeSchoolChosen) {
//				boolean hasSchoolChoice = false;
//				for (int i = 0; i < this.iNumberOfSchools; i++) {
//					if (iwc.isParameterSet(PARAMETER_SCHOOLS + "_" + (i + 1))) {
//						hasSchoolChoice = true;
//						break;
//					}
//				}
//				if (!hasSchoolChoice) {
//					setError(PARAMETER_SCHOOLS + "_1", this.iwrb.getLocalizedString("application_error.schools_empty", "Please select at least one school"));
//				}
//
//				if (hasError(PARAMETER_SCHOOLS + "_1")) {
//					showPhaseOne(iwc);
//					return;
//				}
//			}
		}

		saveCustodianInfo(iwc, true);

		Form form = getForm(ACTION_PHASE_3);
		form.addParameter(PARAMETER_ACTION, String.valueOf(currentPhase));
		if (maintainParameters != null) {
			for (int i = 0; i < maintainParameters.length; i++) {
				form.maintainParameter(maintainParameters[i]);
			}
		}

		User applicant = getApplicant(iwc);
		Child child = getMemberFamilyLogic(iwc).getChild(applicant);
		Custodian custodian = child.getExtraCustodian();
		if (iwc.isParameterSet(PARAMETER_PERSONAL_ID)) {
			saveCustodianInfo(iwc, false);

			String personalID = iwc.getParameter(PARAMETER_PERSONAL_ID);
			try {
				custodian = getMemberFamilyLogic(iwc).getCustodian(getUserBusiness(iwc).getUser(personalID));
				if (custodian.getDateOfBirth() != null) {
					Age age = new Age(custodian.getDateOfBirth());
					if (age.getYears() < 18) {
						custodian = null;
						setError(PARAMETER_PERSONAL_ID, this.iwrb.getLocalizedString("custodian_must_be_at_least_18_years_old", "The custodian you have selected is younger than 18 years old. Custodians must be at least 18 years old or older."));
					}
				}
			}
			catch (FinderException fe) {
				setError(PARAMETER_PERSONAL_ID, this.iwrb.getLocalizedString("no_user_found_with_personal_id", "No user found with personal ID") + ": " + personalID);
			}
		}

		addErrors(iwc, form);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.custodian_information", "Custodian information"), 2, totalPhases));

		form.add(getPersonInfo(iwc, applicant));

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		Collection custodians = null;
		try {
			custodians = child.getCustodians();
		}
		catch (NoCustodianFound ncf) {
			custodians = new ArrayList();
		}

		int number = 1;
		Iterator iter = custodians.iterator();
		while (iter.hasNext()) {
			Custodian element = (Custodian) iter.next();
			addParentToForm(form, iwc, child, element, false, number++, false, showMaritalStatus);
		}

		addParentToForm(form, iwc, child, custodian, false, number, true, showMaritalStatus);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(nextPhase));
		next.setToFormSubmit(form);
		bottom.add(next);

		Link back = getButtonLink(this.iwrb.getLocalizedString("previous", "Previous"));
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(previousPhase));
		back.setToFormSubmit(form);
		bottom.add(back);

		add(form);
	}

	protected void showPhaseFour(IWContext iwc, int nextPhase, int previousPhase, int currentPhase, int totalPhases, String[] maintainParameters, boolean validateForm) throws RemoteException {
		boolean saved = saveCustodianInfo(iwc, false);
		if (!saved) {
			showPhaseThree(iwc, nextPhase - 1, previousPhase - 1, currentPhase - 1, totalPhases, maintainParameters, validateForm, false);
			return;
		}
		if (iwc.isParameterSet(PARAMETER_BACK)) {
			saveChildInfo(iwc, getApplicant(iwc));
		}

		Form form = getForm(ACTION_PHASE_4);
		form.addParameter(PARAMETER_ACTION, String.valueOf(currentPhase));
		if (maintainParameters != null) {
			for (int i = 0; i < maintainParameters.length; i++) {
				form.maintainParameter(maintainParameters[i]);
			}
		}

		addErrors(iwc, form);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.relative_information", "Relative information"), 3, totalPhases));

		User applicant = getApplicant(iwc);
		Child child = getMemberFamilyLogic(iwc).getChild(applicant);
		form.add(getPersonInfo(iwc, applicant));

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		List relatives = child.getRelatives();
		for (int a = 1; a <= 2; a++) {
			Relative relative = null;
			if (relatives.size() >= a) {
				relative = (Relative) relatives.get(a - 1);
			}

			addRelativeToForm(form, relative, a);
		}

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(nextPhase));
		next.setToFormSubmit(form);
		bottom.add(next);

		Link back = getButtonLink(this.iwrb.getLocalizedString("previous", "Previous"));
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(previousPhase));
		back.setToFormSubmit(form);
		bottom.add(back);

		add(form);
	}
	
	protected void showPhaseFive(IWContext iwc) throws RemoteException {
		boolean saved = saveCustodianInfo(iwc, true);
		if (!saved) {
			showPhaseFour(iwc, 5, 3, 4, 6, null, true);
			return;
		}

		Form form = getForm(ACTION_PHASE_5);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5)));
		form.add(new HiddenInput(PARAMETER_BACK, ""));

		addErrors(iwc, form);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.child_information", "Child information"), 4, 7));

		User applicant = getApplicant(iwc);
		form.add(getPersonInfo(iwc, applicant));

		addChildInformation(iwc, getApplicant(iwc), form, true);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		form.add(clearLayer);

//		boolean showAgreement = iwc.getApplicationSettings().getBoolean(SchoolConstants.PROPERTY_SHOW_AGREEMENT, false);
//		if (showAgreement) {
//			Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.agreement_info", "Agreement information"));
//			heading.setStyleClass("subHeader");
//			form.add(heading);
//
//			Layer section = new Layer(Layer.DIV);
//			section.setStyleClass("formSection");
//			form.add(section);
//
//			CheckBox agree = new CheckBox(PARAMETER_AGREEMENT, Boolean.TRUE.toString());
//			agree.setStyleClass("checkbox");
//			agree.keepStatusOnAction(true);
//
//			Paragraph paragraph = new Paragraph();
//			paragraph.setStyleClass("agreement");
//			paragraph.add(new Text(this.iwrb.getLocalizedString("application.agreement", "Agreement text")));
//			section.add(paragraph);
//
//			Layer formItem = new Layer(Layer.DIV);
//			formItem.setStyleClass("formItem");
//			formItem.setStyleClass("radioButtonItem");
//			formItem.setStyleClass("required");
//			if (hasError(PARAMETER_AGREEMENT)) {
//				formItem.setStyleClass("hasError");
//			}
//			Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.agree_terms", "Yes, I agree"))), agree);
//			formItem.add(agree);
//			formItem.add(label);
//			section.add(formItem);
//
//			section.add(clearLayer);
//		}

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6));
		next.setToFormSubmit(form);
		bottom.add(next);

		Link back = getButtonLink(this.iwrb.getLocalizedString("previous", "Previous"));
		back.setValueOnClick(PARAMETER_BACK, Boolean.TRUE.toString());
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4));
		back.setToFormSubmit(form);
		bottom.add(back);

		add(form);
	}
	
	private void showPhaseSix(IWContext iwc) throws RemoteException {
		User applicant = getApplicant(iwc);
		IWTimestamp birth = new IWTimestamp(applicant.getDateOfBirth());
		
		super.getParentPage().addJavascriptURL("/dwr/interface/CourseDWRUtil.js");
		super.getParentPage().addJavascriptURL("/dwr/engine.js");
		super.getParentPage().addJavascriptURL("/dwr/util.js");

		
		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n")
		.append("\tDWRUtil.removeAllOptions(\""+PARAMETER_COURSE_TYPE+"\");\n")
		.append("\tDWRUtil.addOptions(\""+PARAMETER_COURSE_TYPE+"\", data);\n")
		.append("}");
		
		StringBuffer script = new StringBuffer();
		script.append("function changeValues() {\n")
		.append("\tvar val = +$(\""+PARAMETER_CATEGORY+"\").value;\n")
		.append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(setOptions, val);\n")
		.append("\tgetCourses();\n")
		.append("}");

		StringBuffer script3 = new StringBuffer();
		script3.append("function getCourses() {\n")
		.append("\tvar val = +$(\""+PARAMETER_CATEGORY+"\").value;\n")
		.append("\tvar val2 = +$(\""+PARAMETER_COURSE_TYPE+"\").value;\n")
		.append("\tvar TEST = CourseDWRUtil.getCoursesDWR(setCourses, val, val2, "+birth.getYear()+", '"+iwc.getCurrentLocale().getCountry()+"');\n")
		.append("}");

		StringBuffer script4 = new StringBuffer();
		script4.append("var getName = function(course) { return course.name };\n");
		script4.append("var getPk = function(course) { return course.pk \n};");
		script4.append("var getFrom = function(course) { return course.from };\n");
		script4.append("var getTo = function(course) { return course.to };\n");
		script4.append("var getDescription = function(course) { return course.description };\n");
		script4.append("var getTimeframe = function(course) { return course.timeframe };\n");
		script4.append("var getDays = function(course) { return course.days };\n");
		script4.append("var getPrice = function(course) { return course.price };\n");
		script4.append("var getRadioButton = function(course) { return getRadio(course);};\n");
//		script4.append("var noCourse = { pk:-1, name:no course };\n");
		//onclick=\"setCourseID(\'+course.pk+\')\"
		
		script4.append("function setCourses(data) {\n")
		.append("\tvar isEmpty = true;\n")
		.append("\tfor (var prop in data) { isEmpty = false } \n")
		.append("\tif (isEmpty == true) {\n")
		.append("\t}\n")
		.append("\tDWRUtil.removeAllRows(\""+PARAMETER_COURSE_TABLE_ID+"\");\n")
		.append("\tDWRUtil.addRows(\""+PARAMETER_COURSE_TABLE_ID+"\", data, [getRadio, getName, getTimeframe, getDays, getDescription, getPrice]);\n")
		.append("\tvar table = $(\""+PARAMETER_COURSE_TABLE_ID+"\");\n")
		.append("\tvar trs = table.childNodes;\n")
		.append("\tfor (var rowNum = 0; rowNum < trs.length; rowNum++) {\n")
		.append("\t\tvar currentRow = trs[rowNum];\n")
		.append("\t\tif (rowNum % 2 == 0) {\n")
		.append("\t\t\tcurrentRow.className=\"even\";\n")
		.append("\t\t} else {\n")
		.append("\t\t\tcurrentRow.className=\"odd\";\n")
		.append("\t\t}\n")
		.append("\t\tvar tds = currentRow.childNodes;\n")
		.append("\t\tfor (var colNum = 0; colNum < tds.length; colNum++) {\n")
		.append("\t\t\tvar obj = tds[colNum].firstChild;\n")
		.append("\t\t\tif (obj != null && obj.className == 'checkbox') {\n");
		Collection inrepps = getCourseApplicationSession(iwc).getApplications();
		if (inrepps != null && !inrepps.isEmpty()) {
			script4.append("\t\t\t\t if (");
			Iterator iter = inrepps.iterator();
			boolean first = true;
			while (iter.hasNext()) {
				if (!first) {
					script4.append(" || ");
				}
				script4.append("obj.id == "+((ApplicationHolder) iter.next()).getCourse().getPrimaryKey().toString());
				first = false;
			}
			script4.append(") {obj.disabled=true;obj.checked=true}\n");
		}
		script4.append("\t\t\t}\n")
		.append("\t\t\ttds[colNum].className=\"column\"+colNum;\n")
		.append("\t\t}\n")
		.append("\t\tvar tds3 = currentRow.childNodes;\n")
		.append("\t}\n")
		
//		.append("\tDWRUtil.addRows(\""+PARAMETER_COURSE_TABLE_ID+"\", data, [getRadioButton, getName, getFrom, getTo, getTimeframe, getDays, getDescription, getPrice]);\n")
		.append("}");
		
		StringBuffer script5 = new StringBuffer();
		script5.append("function getRadio(course) { \n")
		.append("\nvar sel = (course.pk == 0);\n")
//		.append("\nalert(sel);\n")
		.append("\nreturn '<input id=\"'+course.pk+'\" type=\"checkbox\" class=\"checkbox\" name=\""+PARAMETER_COURSE+"\" value=\"'+course.pk+'\" />'; \n")
		.append("}\n");
		super.getParentPage().getAssociatedScript().addFunction("setOptions", script2.toString());
		super.getParentPage().getAssociatedScript().addFunction("changeValues", script.toString());
		super.getParentPage().getAssociatedScript().addFunction("getCourses", script3.toString());
		super.getParentPage().getAssociatedScript().addFunction("setCourses", script4.toString());
		super.getParentPage().getAssociatedScript().addFunction("setCourseID", script5.toString());
		
		
		Form form = getForm(ACTION_PHASE_6);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6)));
		form.add(new HiddenInput(PARAMETER_BACK, ""));

		addErrors(iwc, form);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("course", "Course"), 5, 7));
		form.add(getPersonInfo(iwc, applicant));
		
		
		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("select_a_course", "Select a course"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.extra_custodian_help", "Add a custodian by typing in its personal ID in the field and click 'Search'. Then fill in the necessary information.  Please note that filling in the relation is mandatory.")));
		section.add(helpLayer);

		Collection schoolTypes = getCourseBusiness(iwc).getAllCourseCategories();
		Integer schoolTypePK = (Integer) ((SchoolType)schoolTypes.iterator().next()).getPrimaryKey();
		DropdownMenu catMenu = new DropdownMenu(schoolTypes, PARAMETER_CATEGORY);
//		catMenu.keepStatusOnAction(true);
		catMenu.setOnChange("changeValues();");
		catMenu.setId(PARAMETER_CATEGORY);
		
		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("category", "Category"))), catMenu);
		formItem.add(label);
		formItem.add(catMenu);
		section.add(formItem);
		
		DropdownMenu  typeMenu = new DropdownMenu(PARAMETER_COURSE_TYPE);
		typeMenu.setId(PARAMETER_COURSE_TYPE);
		Collection courseTypes = getCourseBusiness(iwc).getCourseTypes(schoolTypePK);
		Integer courseTypeID = null;
		if (courseTypes !=  null && !courseTypes.isEmpty()) {
			courseTypeID = (Integer) ((CourseType) courseTypes.iterator().next()).getPrimaryKey();
		}
		typeMenu.addMenuElements(courseTypes);
		typeMenu.setOnChange("getCourses();");

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("type", "Type"))), typeMenu);
		formItem.add(label);
		formItem.add(typeMenu);
		section.add(formItem);
		
		Collection courses = getCourseBusiness(iwc).getCoursesDWR(schoolTypePK.intValue(), courseTypeID.intValue(), birth.getYear(), iwc.getCurrentLocale().getCountry());
//		formItem = new Layer(Layer.DIV);
//		formItem.setStyleClass("formItem");
//		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("course", "Course"))), courseMenu);
//		formItem.add(label);
//		formItem.add(courseMenu);
//		section.add(formItem);
		
		Table2 table = new Table2();
		table.setStyleClass("courses");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.setStyleClass("header");
		TableCell2 cell = row.createCell();
		cell.setStyleClass("column0");

		cell = row.createCell();
		cell.setStyleClass("column1");
		cell.add(new Text(iwrb.getLocalizedString("name", "Name")));
		cell = row.createCell();
		cell.setStyleClass("column2");
		cell.add(new Text(iwrb.getLocalizedString("timeframe_date", "Timeframe/Dates")));
		cell = row.createCell();
		cell.setStyleClass("column3");
		cell.add(new Text(iwrb.getLocalizedString("days", "Days")));
		cell = row.createCell();
		cell.setStyleClass("column4");
		cell.add(new Text(iwrb.getLocalizedString("description", "Description")));
		cell = row.createCell();
		cell.setStyleClass("column5");
		cell.add(new Text(iwrb.getLocalizedString("price", "Price")));
		group = table.createBodyRowGroup();
		group.setId(PARAMETER_COURSE_TABLE_ID);
		if (courses != null) {
			Iterator iter = courses.iterator();
			int counter = 0;
			while (iter.hasNext()) {
				row = group.createRow();
				if (counter++ % 2 == 0) {
					row.setStyleClass("even");
				} else {
					row.setStyleClass("odd");
				}
				CourseDWR course = (CourseDWR) iter.next();
				cell = row.createCell();
				cell.setStyleClass("column0");
				CheckBox checker = new CheckBox(PARAMETER_COURSE, course.getPk());
				checker.setStyleClass("checkbox");
				if (getCourseApplicationSession(iwc).contains(applicant, getCourseBusiness(iwc).getCourse(new Integer(course.getPk())))) {
					row.setStyleClass("selected");
					checker.setDisabled(true);
					checker.setChecked(true);
				}
				cell.add(checker);
				cell = row.createCell();
				cell.setStyleClass("column1");
				cell.add(new Text(course.getName()));
				cell = row.createCell();
				cell.setStyleClass("column2");
				cell.add(new Text(course.getTimeframe()));
				cell = row.createCell();
				cell.setStyleClass("column3");
				cell.add(new Text(course.getDays()));
				cell = row.createCell();
				cell.setStyleClass("column4");
				cell.add(new Text(course.getDescription()));
				cell = row.createCell();
				cell.setStyleClass("column5");
				cell.add(new Text(course.getPrice()));
			}
		}
//		if (courses == null || courses.isEmpty()) {
//			courseMenu.addMenuElement("-1", iwrb.getLocalizedString("°no_available_course", "No available course"));
//		} else {
//			courseMenu.addMenuElements(courses);
//		}

		heading = new Heading1(this.iwrb.getLocalizedString("available_courses", "Available courses"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.add(table);
		section.add(formItem);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.extra_custodian_help", "Add a custodian by typing in its personal ID in the field and click 'Search'. Then fill in the necessary information.  Please note that filling in the relation is mandatory.")));
		section.add(helpLayer);
		
		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		form.add(clearLayer);

		
		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_7));
		next.setToFormSubmit(form);
		bottom.add(next);

		Link back = getButtonLink(this.iwrb.getLocalizedString("previous", "Previous"));
		back.setValueOnClick(PARAMETER_BACK, Boolean.TRUE.toString());
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		back.setToFormSubmit(form);
		bottom.add(back);

		add(form);

	}
	
	public void showPhaseSeven(IWContext iwc) throws RemoteException {
		
		User applicant = getApplicant(iwc);
		
		Form form = getForm(ACTION_PHASE_7);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6)));
		form.add(new HiddenInput(PARAMETER_BACK, ""));
		add(form);
		addErrors(iwc, form);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("course", "Course"), 6, 7));
		form.add(getPersonInfo(iwc, applicant));
		
		String[] courses = iwc.getParameterValues(PARAMETER_COURSE);

		Table2 table = new Table2();
		table.setStyleClass("courses");
		table.setStyleClass("courses_phase_7");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.setStyleClass("header");
		TableCell2 cell = row.createCell();
		cell.setStyleClass("column0");
		cell.add(new Text(iwrb.getLocalizedString("applicant", "Applicant")));
		cell = row.createCell();
		cell.setStyleClass("column1");
		cell.add(new Text(iwrb.getLocalizedString("type", "Type")));
		cell = row.createCell();
		cell.setStyleClass("column2");
		cell.add(new Text(iwrb.getLocalizedString("name", "Name")));
		cell = row.createCell();
		cell.setStyleClass("column3");
		cell.add(new Text(iwrb.getLocalizedString("date_short", "Date")));
		cell = row.createCell();
		cell.setStyleClass("column4");
		cell.add(new Text(iwrb.getLocalizedString("daycare", "Daycare")));
		cell = row.createCell();
		cell.setStyleClass("column5");
		cell.add(new Text(iwrb.getLocalizedString("trip_home", "Trip home")));
		group = table.createBodyRowGroup();
		
		Locale locale = iwc.getCurrentLocale();
		group.setId(PARAMETER_COURSE_TABLE_ID);
		
		for (int i = 0; courses != null && i < courses.length; i++) {
			ApplicationHolder h = new ApplicationHolder();
			h.setUser(applicant);
			h.setCourse(getCourseBusiness(iwc).getCourse(new Integer(courses[i])));
			getCourseApplicationSession(iwc).addApplication(h);
		}
		Collection applications = getCourseApplicationSession(iwc).getApplications();
		
		int counter = 0;
		Iterator iter = applications.iterator();
		while (iter.hasNext()) {
//		for (int i = 0; i < courses.length; i++) {
			row = group.createRow();
			if (counter++ % 2 == 0) {
				row.setStyleClass("even");
			} else {
				row.setStyleClass("odd");
			}
			ApplicationHolder holder = (ApplicationHolder) iter.next();
			Course course = holder.getCourse();
			CourseDWR courseDWR = getCourseBusiness(iwc).getCourseDWR(locale, course);
			cell = row.createCell();
			cell.setStyleClass("column0");
			cell.add(new Text(holder.getUser().getName()));

			cell = row.createCell();
			cell.setStyleClass("column1");
			cell.add(new HiddenInput(PARAMETER_COURSE, courseDWR.getPk()));
			cell.add(new Text(iwrb.getLocalizedString(course.getCourseType().getLocalizationKey(), course.getCourseType().getName())));

			cell = row.createCell();
			cell.setStyleClass("column2");
			cell.add(new Text(courseDWR.getName()));
			cell = row.createCell();
			cell.setStyleClass("column3");
			cell.add(new Text(courseDWR.getTimeframe()));
			cell = row.createCell();
			cell.setStyleClass("column4");
			
			DropdownMenu daycare = new DropdownMenu(PARAMETER_DAYCARE);
			daycare.addMenuElement(DAYCARE_MORNING, iwrb.getLocalizedString("morning", "Morning"));
			daycare.addMenuElement(DAYCARE_AFTERNOON, iwrb.getLocalizedString("afternoon", "Afternoon"));
			daycare.addMenuElement(DAYCARE_WHOLE_DAY, iwrb.getLocalizedString("whole_day", "Whole day"));
			if (holder.getDaycare() > 0) {
				daycare.setSelectedElement(holder.getDaycare());
			}
			cell.add(daycare);
			
			cell = row.createCell();
			cell.setStyleClass("column5");
			DropdownMenu trip = new DropdownMenu(PARAMETER_TRIPHOME);
			trip.addMenuElement(TRIPHOME_PICKED_UP, iwrb.getLocalizedString("picked_up", "Picked up"));
			trip.addMenuElement(TRIPHOME_WALKS_HOME, iwrb.getLocalizedString("walks_home", "Walks home"));
			if (holder.getTripHome() > 0) {
				trip.setSelectedElement(holder.getTripHome());
			}
			cell.add(trip);
			
		}
		
		
		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("selected_courses", "Selected courses"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		
		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("formItemBig");
		formItem.add(table);
		section.add(formItem);
		
		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("select_daycare_and_trip_home_options", "Select daycare and trip home options.")));
		section.add(helpLayer);
		
		
		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_8));
		next.setToFormSubmit(form);
		bottom.add(next);

		Link newAppl = getButtonLink(this.iwrb.getLocalizedString("another_applicant", "Another applicant"));
		newAppl.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1));
		newAppl.setToFormSubmit(form);
		form.add(new HiddenInput("SUBACTION", ""));
		bottom.add(newAppl);

		Link newCourse = getButtonLink(this.iwrb.getLocalizedString("another_course", "Another course"));
		newCourse.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6));
		newCourse.setValueOnClick(SUB_ACTION, String.valueOf(SUB_ACTION_REMOVE));
		newCourse.setToFormSubmit(form);
		form.add(new HiddenInput("SUBACTION", ""));
		bottom.add(newCourse);

		Link back = getButtonLink(this.iwrb.getLocalizedString("previous", "Previous"));
		back.setValueOnClick(PARAMETER_BACK, Boolean.TRUE.toString());
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6));
		back.setToFormSubmit(form);
		bottom.add(back);



	}
	
	private void addParentToForm(Form form, IWContext iwc, Child child, Custodian custodian, boolean isExtraCustodian, int number, boolean editable, boolean showMaritalStatus) throws RemoteException {
		Address address = null;
		Phone phone = null;
		Phone work = null;
		Phone mobile = null;
		Email email = null;
		String maritalStatus = null;

		if (custodian != null) {
			address = getUserBusiness(iwc).getUsersMainAddress(custodian);
			maritalStatus = custodian.getMaritalStatus();

			try {
				phone = getUserBusiness(iwc).getUsersHomePhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				phone = null;
			}

			try {
				work = getUserBusiness(iwc).getUsersWorkPhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				work = null;
			}

			try {
				mobile = getUserBusiness(iwc).getUsersMobilePhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				mobile = null;
			}

			try {
				email = getUserBusiness(iwc).getUsersMainEmail(custodian);
			}
			catch (NoEmailFoundException nefe) {
				email = null;
			}
		}

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("custodian", "Custodian"));
		heading.setStyleClass("subHeader");
		if (number == 1) {
			heading.setStyleClass("topSubHeader");
		}
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		if (editable) {
			Layer helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("application.extra_custodian_help", "Add a custodian by typing in its personal ID in the field and click 'Search'. Then fill in the necessary information.  Please note that filling in the relation is mandatory.")));
			section.add(helpLayer);
		}
		else {
			Layer helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("application.custodian_help", "Please fill in the necessary information.  Please note that filling in the relation is mandatory.")));
			section.add(helpLayer);
		}

		DropdownMenu relationMenu = getRelationDropdown(custodian, false);
		if (custodian != null) {
			String relation = child.getRelation(custodian);
			if (relation != null) {
				relationMenu.setSelectedElement(relation);
			}
		}
		relationMenu.keepStatusOnAction(true);

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		if (hasError(PARAMETER_RELATION)) {
			formItem.setStyleClass("hasError");
		}
		Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("relation", "Relation"))), relationMenu);
		formItem.add(label);
		formItem.add(relationMenu);
		section.add(formItem);

		TextInput name = new TextInput("name", custodian != null ? custodian.getName() : null);
		name.setDisabled(true);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("name", "Name"), name);
		formItem.add(label);
		formItem.add(name);
		section.add(formItem);

		if (custodian != null) {
			formItem.add(new HiddenInput(!isExtraCustodian ? PARAMETER_USER : PARAMETER_RELATIVE, custodian.getPrimaryKey().toString()));
		}

		TextInput personalID = null;
		SubmitButton search = null;
		if (!editable) {
			personalID = new TextInput("personalID", PersonalIDFormatter.format(custodian.getPersonalID(), iwc.getCurrentLocale()));
			personalID.setDisabled(true);
		}
		else {
			personalID = new TextInput(PARAMETER_PERSONAL_ID + (isExtraCustodian ? "_" + number : ""));
			personalID.setLength(10);
			personalID.setAsPersonalID(iwc.getCurrentLocale(), this.iwrb.getLocalizedString("not_valid_personal_id", "Not a valid personal ID"));
			if (custodian != null) {
				personalID.setContent(custodian.getPersonalID());
			}

			search = new SubmitButton(this.iwrb.getLocalizedString("search", "Search"));
			search.setStyleClass("button");
		}

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		if (editable) {
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_PERSONAL_ID)) {
				formItem.setStyleClass("hasError");
			}
		}
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("personal_id", "Personal ID"))), personalID);
		formItem.add(label);
		formItem.add(personalID);
		if (search != null) {
			formItem.add(search);
		}
		section.add(formItem);

		TextInput addr = new TextInput("address");
		addr.setDisabled(true);
		TextInput zip = new TextInput("zipCode");
		zip.setDisabled(true);
		if (address != null) {
			addr.setContent(address.getStreetAddress());
			PostalCode code = address.getPostalCode();
			if (code != null) {
				zip.setContent(code.getPostalAddress());
			}
		}

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("address", "Address"), addr);
		formItem.add(label);
		formItem.add(addr);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("zip_code", "Zip code"), zip);
		formItem.add(label);
		formItem.add(zip);
		section.add(formItem);

		TextInput homePhone = new TextInput(PARAMETER_HOME_PHONE, null);
		if (phone != null) {
			homePhone.setContent(phone.getNumber());
		}
		homePhone.keepStatusOnAction(true, number - 1);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		if (hasError(PARAMETER_HOME_PHONE)) {
			formItem.setStyleClass("hasError");
		}
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("home_phone", "Home phone"))), homePhone);
		formItem.add(label);
		formItem.add(homePhone);
		section.add(formItem);

		TextInput workPhone = new TextInput(PARAMETER_WORK_PHONE);
		if (work != null) {
			workPhone.setContent(work.getNumber());
		}
		workPhone.keepStatusOnAction(true, number - 1);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("work_phone", "Work phone"), workPhone);
		formItem.add(label);
		formItem.add(workPhone);
		section.add(formItem);

		TextInput mobilePhone = new TextInput(PARAMETER_MOBILE_PHONE, null);
		if (mobile != null) {
			mobilePhone.setContent(mobile.getNumber());
		}
		mobilePhone.keepStatusOnAction(true, number - 1);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("mobile_phone", "Mobile phone"), mobilePhone);
		formItem.add(label);
		formItem.add(mobilePhone);
		section.add(formItem);

		TextInput mail = new TextInput(PARAMETER_EMAIL, null);
		if (email != null) {
			mail.setContent(email.getEmailAddress());
		}
		mail.keepStatusOnAction(true, number - 1);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("email", "E-mail"), mail);
		formItem.add(label);
		formItem.add(mail);
		section.add(formItem);

		if (showMaritalStatus) {
			DropdownMenu maritalStatusMenu = getMaritalStatusDropdown(custodian);
			if (maritalStatus != null) {
				maritalStatusMenu.setSelectedElement(maritalStatus);
			}
			maritalStatusMenu.keepStatusOnAction(true);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("required");
			label = new Label(new Span(new Text(this.iwrb.getLocalizedString("marital_status", "Marital status"))), maritalStatusMenu);
			formItem.add(label);
			formItem.add(maritalStatusMenu);
			section.add(formItem);
		}

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		section.add(clearLayer);
	}

	private void addRelativeToForm(Form form, Relative relative, int number) {
		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("contact", "Contact"));
		heading.setStyleClass("subHeader");
		if (number == 1) {
			heading.setStyleClass("topSubHeader");
		}
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.contact_help", "Add information about possible contacts to the applicant. Please fill in the necessary information.  Please note that filling in the relation is mandatory.")));
		section.add(helpLayer);

		DropdownMenu relationMenu = getRelationDropdown(null, true);
		if (relative != null && relative.getRelation() != null) {
			relationMenu.setSelectedElement(relative.getRelation());
		}
		relationMenu.keepStatusOnAction(true, number - 1);

		TextInput name = new TextInput(PARAMETER_RELATIVE);
		if (relative != null && relative.getName() != null) {
			name.setContent(relative.getName());
		}
		name.keepStatusOnAction(true, number - 1);

		TextInput homePhone = new TextInput(PARAMETER_RELATIVE_HOME_PHONE);
		if (relative != null && relative.getHomePhone() != null) {
			homePhone.setContent(relative.getHomePhone());
		}
		homePhone.keepStatusOnAction(true, number - 1);

		TextInput workPhone = new TextInput(PARAMETER_RELATIVE_WORK_PHONE);
		if (relative != null && relative.getWorkPhone() != null) {
			workPhone.setContent(relative.getWorkPhone());
		}
		workPhone.keepStatusOnAction(true, number - 1);

		TextInput mobilePhone = new TextInput(PARAMETER_RELATIVE_MOBILE_PHONE);
		if (relative != null && relative.getMobilePhone() != null) {
			mobilePhone.setContent(relative.getMobilePhone());
		}
		mobilePhone.keepStatusOnAction(true, number - 1);

		TextInput mail = new TextInput(PARAMETER_RELATIVE_EMAIL);
		if (relative != null && relative.getEmail() != null) {
			mail.setContent(relative.getEmail());
		}
		mail.keepStatusOnAction(true, number - 1);

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		if (hasError(PARAMETER_RELATIVE_RELATION)) {
			formItem.setStyleClass("hasError");
		}
		Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("relation", "Relation"))), relationMenu);
		formItem.add(label);
		formItem.add(relationMenu);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("name", "Name"))), name);
		formItem.add(label);
		formItem.add(name);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("home_phone", "Home phone"), homePhone);
		formItem.add(label);
		formItem.add(homePhone);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("work_phone", "Work phone"), workPhone);
		formItem.add(label);
		formItem.add(workPhone);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("mobile_phone", "Mobile phone"), mobilePhone);
		formItem.add(label);
		formItem.add(mobilePhone);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("email", "E-mail"), mail);
		formItem.add(label);
		formItem.add(mail);
		section.add(formItem);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		section.add(clearLayer);
	}

	protected void addChildInformation(IWContext iwc, User user, Form form, boolean showOtherInformation) throws RemoteException {
		this.iwrb = getResourceBundle(iwc);

		Child child = getMemberFamilyLogic(iwc).getChild(user);
//		Student student = getSchoolBusiness(iwc).getStudent(user);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("child.has_growth_deviation", "Has growth deviation"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		RadioButton yes = new RadioButton(PARAMETER_GROWTH_DEVIATION, Boolean.TRUE.toString());
		yes.setStyleClass("radiobutton");
		yes.setToDisableOnClick(PARAMETER_GROWTH_DEVIATION_DETAILS, false);
		RadioButton no = new RadioButton(PARAMETER_GROWTH_DEVIATION, Boolean.FALSE.toString());
		no.setStyleClass("radiobutton");
		no.setToDisableOnClick(PARAMETER_GROWTH_DEVIATION_DETAILS, true);
		RadioButton noAnswer = new RadioButton(PARAMETER_GROWTH_DEVIATION, "");
		noAnswer.setStyleClass("radiobutton");
		noAnswer.setToDisableOnClick(PARAMETER_GROWTH_DEVIATION_DETAILS, true);
		Boolean hasGrowthDeviation = child.hasGrowthDeviation();
		if (hasGrowthDeviation != null) {
			if (hasGrowthDeviation.booleanValue()) {
				yes.setSelected(true);
			}
			else {
				no.setSelected(true);
			}
		}
		else {
			no.setSelected(true);
		}
		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		Label label = new Label(this.iwrb.getLocalizedString("yes", "Yes"), yes);
		formItem.add(yes);
		formItem.add(label);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		label = new Label(this.iwrb.getLocalizedString("no", "No"), no);
		formItem.add(no);
		formItem.add(label);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		label = new Label(this.iwrb.getLocalizedString("no_answer", "Won't answer"), noAnswer);
		formItem.add(noAnswer);
		formItem.add(label);
		section.add(formItem);

		section.add(clearLayer);

		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(this.iwrb.getLocalizedString("details", "Details")));
		section.add(paragraph);

		TextArea details = new TextArea(PARAMETER_GROWTH_DEVIATION_DETAILS, child.getGrowthDeviationDetails());
		details.setStyleClass("details");
		details.setDisabled(!yes.getSelected());
		section.add(details);

		heading = new Heading1(this.iwrb.getLocalizedString("child.has_allergies", "Has allergies"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		yes = new RadioButton(PARAMETER_ALLERGIES, Boolean.TRUE.toString());
		yes.setStyleClass("radiobutton");
		yes.setToDisableOnClick(PARAMETER_ALLERGIES_DETAILS, false);
		no = new RadioButton(PARAMETER_ALLERGIES, Boolean.FALSE.toString());
		no.setStyleClass("radiobutton");
		no.setToDisableOnClick(PARAMETER_ALLERGIES_DETAILS, true);
		noAnswer = new RadioButton(PARAMETER_ALLERGIES, "");
		noAnswer.setStyleClass("radiobutton");
		noAnswer.setToDisableOnClick(PARAMETER_ALLERGIES_DETAILS, true);
		Boolean hasAllergies = child.hasAllergies();
		if (hasAllergies != null) {
			if (hasAllergies.booleanValue()) {
				yes.setSelected(true);
			}
			else {
				no.setSelected(true);
			}
		}
		else {
			no.setSelected(true);
		}
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		label = new Label(this.iwrb.getLocalizedString("yes", "Yes"), yes);
		formItem.add(yes);
		formItem.add(label);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		label = new Label(this.iwrb.getLocalizedString("no", "No"), no);
		formItem.add(no);
		formItem.add(label);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		label = new Label(this.iwrb.getLocalizedString("no_answer", "Won't answer"), noAnswer);
		formItem.add(noAnswer);
		formItem.add(label);
		section.add(formItem);

		section.add(clearLayer);

		paragraph = new Paragraph();
		paragraph.add(new Text(this.iwrb.getLocalizedString("details", "Details")));
		section.add(paragraph);

		details = new TextArea(PARAMETER_ALLERGIES_DETAILS, child.getAllergiesDetails());
		details.setStyleClass("details");
		details.setDisabled(!yes.getSelected());
		section.add(details);

		Layer helpLayer = new Layer(Layer.DIV);

//		heading = new Heading1(this.iwrb.getLocalizedString("child.can_contact_last_care_provider", "Can contact last care provider"));
//		heading.setStyleClass("subHeader");
//		form.add(heading);
//
//		section = new Layer(Layer.DIV);
//		section.setStyleClass("formSection");
//		form.add(section);
//
//		Layer helpLayer = new Layer(Layer.DIV);
//		helpLayer.setStyleClass("helperText");
//		helpLayer.add(new Text(this.iwrb.getLocalizedString("child.contact_last_care_provider_help", "If the school is allowed to contact the last care provider, please select 'Yes' and fill in the name of the provider.")));
//		section.add(helpLayer);
//
//		yes = new RadioButton(PARAMETER_CAN_CONTACT_LAST_PROVIDER, Boolean.TRUE.toString());
//		yes.setStyleClass("radiobutton");
//		no = new RadioButton(PARAMETER_CAN_CONTACT_LAST_PROVIDER, Boolean.FALSE.toString());
//		no.setStyleClass("radiobutton");
//		boolean canContactLastProvider = student.canContactLastProvider();
//		if (canContactLastProvider) {
//			yes.setSelected(true);
//		}
//		else {
//			no.setSelected(true);
//		}
//		formItem = new Layer(Layer.DIV);
//		formItem.setStyleClass("formItem");
//		formItem.setStyleClass("radioButtonItem");
//		label = new Label(this.iwrb.getLocalizedString("yes", "Yes"), yes);
//		formItem.add(yes);
//		formItem.add(label);
//		section.add(formItem);
//
//		formItem = new Layer(Layer.DIV);
//		formItem.setStyleClass("formItem");
//		formItem.setStyleClass("radioButtonItem");
//		label = new Label(this.iwrb.getLocalizedString("no", "No"), no);
//		formItem.add(no);
//		formItem.add(label);
//		section.add(formItem);
//
//		section.add(clearLayer);
//
//		paragraph = new Paragraph();
//		paragraph.add(new Text(this.iwrb.getLocalizedString("child.last_care_provider", "Last care provider")));
//		section.add(paragraph);
//
//		TextInput input = new TextInput(PARAMETER_LAST_CARE_PROVIDER, student.getLastProvider());
//		input.setStyleClass("input");
//		section.add(input);
//
//		section.add(clearLayer);

		if (showOtherInformation && iwc.getApplicationSettings().getBoolean(DISPLAY_PHONEBOOK_QUESTION_KEY, false)) {
			heading = new Heading1(this.iwrb.getLocalizedString("child.can_display_parent_information", "Can display parent information"));
			heading.setStyleClass("subHeader");
			form.add(heading);

			section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);

			helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("child.can_display_parent_information_help", "If the school is allowed to use the parent's names and phone numbers in phonebooks for the school/class, please select 'Yes'.")));
			section.add(helpLayer);

			yes = new RadioButton(PARAMETER_CAN_DISPLAY_PARENT_INFORMATION, Boolean.TRUE.toString());
			yes.setStyleClass("radiobutton");
			no = new RadioButton(PARAMETER_CAN_DISPLAY_PARENT_INFORMATION, Boolean.FALSE.toString());
			no.setStyleClass("radiobutton");
//			boolean canDisplayParentInformation = student.canDisplayParentInformation();
//			if (canDisplayParentInformation) {
//				yes.setSelected(true);
//			}
//			else {
//				no.setSelected(true);
//			}
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("radioButtonItem");
			label = new Label(this.iwrb.getLocalizedString("yes", "Yes"), yes);
			formItem.add(yes);
			formItem.add(label);
			section.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("radioButtonItem");
			label = new Label(this.iwrb.getLocalizedString("no", "No"), no);
			formItem.add(no);
			formItem.add(label);
			section.add(formItem);

			section.add(clearLayer);
		}

		if (showOtherInformation) {
			heading = new Heading1(this.iwrb.getLocalizedString("child.other_information", "Other information"));
			heading.setStyleClass("subHeader");
			form.add(heading);

			section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);

			helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("child.other_information_help", "If there is anything else that you feel the school should know about the child, please fill in the details.")));
			section.add(helpLayer);

			details = new TextArea(PARAMETER_OTHER_INFORMATION, child.getOtherInformation());
			details.setStyleClass("details");
			section.add(details);
		}
	}

	
	private DropdownMenu getMaritalStatusDropdown(User user) {
		DropdownMenu maritalStatus = new DropdownMenu(PARAMETER_MARITAL_STATUS + (user != null ? "_" + user.getPrimaryKey().toString() : ""));
		maritalStatus.addMenuElement("", this.iwrb.getLocalizedString("select_marital_status", "Select marital status"));
		maritalStatus.addMenuElement(FamilyConstants.MARITAL_STATUS_MARRIED, this.iwrb.getLocalizedString("marital_status.married", "Married"));
		maritalStatus.addMenuElement(FamilyConstants.MARITAL_STATUS_SINGLE, this.iwrb.getLocalizedString("marital_status.single", "Single"));
		// maritalStatus.addMenuElement(FamilyConstants.MARITAL_STATUS_COHABITANT, this.iwrb.getLocalizedString("marital_status.cohabitant",
		// "Cohabitant"));
		// maritalStatus.addMenuElement(FamilyConstants.MARITAL_STATUS_DIVORCED, this.iwrb.getLocalizedString("marital_status.divorced", "Divorced"));
		// maritalStatus.addMenuElement(FamilyConstants.MARITAL_STATUS_WIDOWED, this.iwrb.getLocalizedString("marital_status.widowed", "Widowed"));

		return maritalStatus;
	}
	
	private DropdownMenu getRelationDropdown(User relative, boolean isRelative) {
		DropdownMenu relations = new DropdownMenu((isRelative ? PARAMETER_RELATIVE_RELATION : PARAMETER_RELATION) + (relative != null ? "_" + relative.getPrimaryKey().toString() : ""));
		relations.addMenuElement("", this.iwrb.getLocalizedString("select_relation", "Select relation"));
		relations.addMenuElement(FamilyConstants.RELATION_MOTHER, this.iwrb.getLocalizedString("relation.mother", "Mother"));
		relations.addMenuElement(FamilyConstants.RELATION_FATHER, this.iwrb.getLocalizedString("relation.father", "Father"));
		relations.addMenuElement(FamilyConstants.RELATION_STEPMOTHER, this.iwrb.getLocalizedString("relation.stepmother", "Stepmother"));
		relations.addMenuElement(FamilyConstants.RELATION_STEPFATHER, this.iwrb.getLocalizedString("relation.stepfather", "Stepfather"));
		relations.addMenuElement(FamilyConstants.RELATION_GRANDMOTHER, this.iwrb.getLocalizedString("relation.grandmother", "Grandmother"));
		relations.addMenuElement(FamilyConstants.RELATION_GRANDFATHER, this.iwrb.getLocalizedString("relation.grandfather", "Grandfather"));
		relations.addMenuElement(FamilyConstants.RELATION_SIBLING, this.iwrb.getLocalizedString("relation.sibling", "Sibling"));
		relations.addMenuElement(FamilyConstants.RELATION_OTHER, this.iwrb.getLocalizedString("relation.other", "Other"));

		return relations;
	}
	
	protected boolean saveCustodianInfo(IWContext iwc, boolean storeRelatives) throws RemoteException {
		String[] userPKs = storeRelatives ? iwc.getParameterValues(PARAMETER_RELATIVE) : iwc.getParameterValues(PARAMETER_USER);
		String[] homePhones = !storeRelatives ? iwc.getParameterValues(PARAMETER_HOME_PHONE) : iwc.getParameterValues(PARAMETER_RELATIVE_HOME_PHONE);
		String[] workPhones = !storeRelatives ? iwc.getParameterValues(PARAMETER_WORK_PHONE) : iwc.getParameterValues(PARAMETER_RELATIVE_WORK_PHONE);
		String[] mobilePhones = !storeRelatives ? iwc.getParameterValues(PARAMETER_MOBILE_PHONE) : iwc.getParameterValues(PARAMETER_RELATIVE_MOBILE_PHONE);
		String[] emails = !storeRelatives ? iwc.getParameterValues(PARAMETER_EMAIL) : iwc.getParameterValues(PARAMETER_RELATIVE_EMAIL);
		String[] relations = iwc.getParameterValues(storeRelatives ? PARAMETER_RELATIVE_RELATION : PARAMETER_RELATION);

		if (userPKs != null) {
			Child child = getMemberFamilyLogic(iwc).getChild(getApplicant(iwc));

			for (int a = 0; a < userPKs.length; a++) {
				String userPK = userPKs[a];
				String relation = iwc.getParameter(PARAMETER_RELATION + "_" + userPK);
				String maritalStatus = iwc.getParameter(PARAMETER_MARITAL_STATUS + "_" + userPK);
				boolean storeMaritalStatus = iwc.isParameterSet(PARAMETER_MARITAL_STATUS + "_" + userPK);

				if (storeRelatives) {
					if (userPK.length() > 0) {
						if (relations[a] == null || relations[a].length() == 0) {
							setError(PARAMETER_RELATIVE_RELATION, this.iwrb.getLocalizedString("must_select_relation", "You must select a relation to the child."));
							return false;
						}
						child.storeRelative(userPK, relations[a], a + 1, homePhones[a], workPhones[a], mobilePhones[a], emails[a]);
					}
				}
				else {
					if (relation == null || relation.length() == 0) {
						setError(PARAMETER_RELATION, this.iwrb.getLocalizedString("must_select_relation", "You must select a relation to the child."));
					}
					if (storeMaritalStatus && (maritalStatus == null || maritalStatus.length() == 0)) {
						setError(PARAMETER_MARITAL_STATUS, this.iwrb.getLocalizedString("application_error.marital_status_empty", "Please select marital status."));
					}
					if (homePhones[a] == null || homePhones[a].length() == 0) {
						setError(PARAMETER_HOME_PHONE, this.iwrb.getLocalizedString("must_enter_home_phone", "You must enter a home phone for relative."));
					}
					if (hasErrors()) {
						return false;
					}
					Custodian custodian = getMemberFamilyLogic(iwc).getCustodian(getUserBusiness(iwc).getUser(new Integer(userPK)));
					custodian.setHomePhone(homePhones[a]);
					custodian.setWorkPhone(workPhones[a]);
					custodian.setMobilePhone(mobilePhones[a]);
					custodian.setEmail(emails[a]);
					if (storeMaritalStatus) {
						custodian.setMaritalStatus(maritalStatus);
					}
					custodian.store();

					if (custodian.isCustodianOf(child)) {
						child.setRelation(custodian, relation);
						child.store();
					}
					else {
						child.setExtraCustodian(custodian, relation);
						child.store();
					}
				}
			}
		}
		return true;
	}

	protected boolean saveChildInfo(IWContext iwc, User user) throws RemoteException {
		Boolean growthDeviation = iwc.isParameterSet(PARAMETER_GROWTH_DEVIATION) ? new Boolean(iwc.getParameter(PARAMETER_GROWTH_DEVIATION)) : null;
		Boolean allergies = iwc.isParameterSet(PARAMETER_ALLERGIES) ? new Boolean(iwc.getParameter(PARAMETER_ALLERGIES)) : null;

//		boolean canContactLastProvider = iwc.isParameterSet(PARAMETER_CAN_CONTACT_LAST_PROVIDER) ? new Boolean(iwc.getParameter(PARAMETER_CAN_CONTACT_LAST_PROVIDER)).booleanValue() : false;
//		boolean canDisplayParentInformation = iwc.isParameterSet(PARAMETER_CAN_DISPLAY_PARENT_INFORMATION) ? new Boolean(iwc.getParameter(PARAMETER_CAN_DISPLAY_PARENT_INFORMATION)).booleanValue() : false;

		String growthDeviationDetails = iwc.getParameter(PARAMETER_GROWTH_DEVIATION_DETAILS);
		String allergiesDetails = iwc.getParameter(PARAMETER_ALLERGIES_DETAILS);
//		String lastCareProvider = iwc.getParameter(PARAMETER_LAST_CARE_PROVIDER);
		String otherInformation = iwc.getParameter(PARAMETER_OTHER_INFORMATION);

		Child child = getMemberFamilyLogic(iwc).getChild(user);
		child.setHasGrowthDeviation(growthDeviation);
		child.setGrowthDeviationDetails(growthDeviationDetails);
		child.setHasAllergies(allergies);
		child.setAllergiesDetails(allergiesDetails);
		child.setOtherInformation(otherInformation);
		child.store();

		return true;
	}
	
	protected DropdownMenu getUserChooser(IWContext iwc, User user, User chosenUser, String parameterName, IWResourceBundle iwrb) throws RemoteException {
		Collection children = null;
		try {
			children = getMemberFamilyLogic(iwc).getChildrenInCustodyOf(user);
		}
		catch (NoChildrenFound e) {
			children = new ArrayList();
		}
//		children.add(user);

		Application application = null;
		try {
			application = getApplicationBusiness(iwc).getApplication(getCaseCode());
		}
		catch (FinderException fe) {
			// Nothing found, continuing...
		}

		DropdownMenu menu = new DropdownMenu(parameterName);
		menu.setStyleClass("userSelector");
		Iterator iter = children.iterator();
		while (iter.hasNext()) {
			User element = (User) iter.next();
			boolean addUser = true;

			if (application != null) {
				if (application.getAgeFrom() > -1 && application.getAgeTo() > -1) {
					if (element.getDateOfBirth() != null) {
						IWTimestamp stamp = new IWTimestamp(element.getDateOfBirth());
						stamp.setDay(1);
						stamp.setMonth(1);

						Age age = new Age(stamp.getDate());
						addUser = (application.getAgeFrom() <= age.getYears() && application.getAgeTo() >= age.getYears());
					}
					else {
						addUser = false;
					}
				}
			}

			if (!addUser && addOveragedUser(iwc, user)) {
				addUser = true;
			}

			if (addUser) {
				menu.addMenuElement(element.getPrimaryKey().toString(), element.getName());
			}
		}
		menu.addMenuElementFirst("", iwrb.getLocalizedString("select_applicant", "Select applicant"));

		if (chosenUser != null) {
			menu.setSelectedElement(chosenUser.getPrimaryKey().toString());
		}

		return menu;
	}
	
	private FamilyLogic getMemberFamilyLogic(IWApplicationContext iwac) {
		try {
			return (FamilyLogic) IBOLookup.getServiceInstance(iwac, FamilyLogic.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CourseBusiness getCourseBusiness(IWContext iwc) {
		try {
			return (CourseBusiness) IBOLookup.getServiceInstance(iwc, CourseBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private CourseApplicationSession getCourseApplicationSession(IWContext iwc) {
		try {
			return (CourseApplicationSession) IBOLookup.getSessionInstance(iwc, CourseApplicationSession.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

}
