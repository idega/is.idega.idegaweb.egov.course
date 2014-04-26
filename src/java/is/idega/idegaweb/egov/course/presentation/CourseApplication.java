package is.idega.idegaweb.egov.course.presentation;

import is.idega.block.family.business.FamilyConstants;
import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;
import is.idega.block.family.data.Child;
import is.idega.block.family.data.Custodian;
import is.idega.block.family.data.Relative;
import is.idega.idegaweb.egov.application.presentation.ApplicationForm;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.CourseApplicationSession;
import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.business.CourseDWR;
import is.idega.idegaweb.egov.course.business.CourseSession;
import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseCategory;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.data.PriceHolder;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

import javax.ejb.FinderException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Script;
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
import com.idega.user.business.UserSession;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.PresentationUtil;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;
import com.idega.util.text.Name;
import com.idega.util.text.SocialSecurityNumber;
import com.idega.util.text.TextSoap;

public class CourseApplication extends ApplicationForm {

	protected static final int ACTION_PHASE_1 = 1;
	protected static final int ACTION_PHASE_2 = 2;
	protected static final int ACTION_PHASE_3 = 3;
	protected static final int ACTION_PHASE_4 = 4;
	protected static final int ACTION_PHASE_5 = 5;
	protected static final int ACTION_PHASE_6 = 6;
	protected static final int ACTION_PHASE_7 = 7;
	protected static final int ACTION_PHASE_8 = 8;
	protected static final int ACTION_SAVE = 0;

	protected static final String PARAMETER_ACTION = "prm_action";
	protected static final String PARAMETER_BACK = "prm_back";

	private static final String PARAMETER_CHILD_OPTION = "prm_child_option";
	private static final String PARAMETER_CHILD_PK = "prm_child_pk";
	private static final String PARAMETER_CHILD_PERSONAL_ID = "prm_child_personal_id";

	private static final String PARAMETER_USER = "prm_user_id";
	private static final String PARAMETER_REMOVE_CUSTODIAN = "prm_remove_custodian";
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

	private static final String PARAMETER_CATEGORY = "prm_sch_cat";
	private static final String PARAMETER_COURSE_TYPE = "prm_cou_type";
	private static final String PARAMETER_PROVIDER = "prm_provider_pk";
	private static final String PARAMETER_COURSE_TABLE_ID = "prm_cou_course_table";
	private static final String PARAMETER_COURSE = "prm_cou_course";
	private static final String PARAMETER_REMOVE_COURSE = "prm_cou_course_remove";
	private static final String PARAMETER_DAYCARE = "prm_cou_daycare";
	private static final String PARAMETER_TRIPHOME = "prm_cou_trip";

	private static final String PARAMETER_AGREEMENT = "prm_agreement";
	private static final String PARAMETER_CREDITCARD_PAYMENT = "prm_creditcard_payment";

	private static final String PARAMETER_PAYER_OPTION = "prm_payer_option";
	private static final String PARAMETER_PAYER_NAME = "prm_payer_name";
	private static final String PARAMETER_PAYER_PERSONAL_ID = "prm_payer_personal_id";
	private static final String PARAMETER_NAME_ON_CARD = "prm_name_on_card";
	private static final String PARAMETER_CARD_TYPE = "prm_card_type";
	private static final String PARAMETER_CARD_NUMBER = "prm_card_number";
	private static final String PARAMETER_VALID_MONTH = "prm_valid_month";
	private static final String PARAMETER_VALID_YEAR = "prm_valid_year";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	private static final String PARAMETER_CCV = "prm_ccv";

	private static final String SUB_ACTION = "prm_subact";
	private static final String SUB_ACTION_ADD = "prm_subact_add";
	private static final String SUB_ACTION_REMOVE = "prm_subact_rem";

	private boolean iUseSessionUser = false;
	private Object iSchoolTypePK = null;
	private Object iApplicationPK = null;
	private CourseCategory iCategory;
	private boolean hasCare = true;
	private boolean allowsAllChildren = true;
	private boolean showCreditCardPayment = true;
	private String prefix = "";

	private IWBundle iwb = null;
	private IWResourceBundle iwrb = null;

	@Override
	protected String getCaseCode() {
		return CourseConstants.CASE_CODE_KEY;
	}

	@Override
	public String getBundleIdentifier() {
		return CourseConstants.IW_BUNDLE_IDENTIFIER;
	}

	@Override
	protected void present(IWContext iwc) {
		this.iwb = getBundle(iwc);
		this.iwrb = getResourceBundle(iwc);
		PresentationUtil.addStyleSheetToHeader(iwc, iwb.getVirtualPathWithFileNameString("style/course.css"));

		try {
			switch (parseAction(iwc)) {
				case ACTION_PHASE_1:
					if (handleSubAction(iwc)) {
						showPhaseOne(iwc);
					}
					else {
						showPhaseSix(iwc);
					}
					break;

				case ACTION_PHASE_2:
					User applicant = getApplicant(iwc);
					boolean canContinue = true;
					if (applicant == null) {
						setError(PARAMETER_CHILD_PERSONAL_ID, iwrb.getLocalizedString("application_error.no_user_found_with_personal_id", "No user was found with the given personal ID"));
						canContinue = false;
					}
					else if (!iUseSessionUser && !getApplicationBusiness(iwc).canApplyForApplication(getCaseCode(), applicant)) {
						setError(PARAMETER_CHILD_PERSONAL_ID, iwrb.getLocalizedString("application_error.user_not_applicable", "The user you selected is not applicable for this application"));
						canContinue = false;
					}
					else if (!iUseSessionUser && !getCourseBusiness(iwc).hasAvailableCourses(applicant, (SchoolType) null)) {
						setError(PARAMETER_CHILD_PERSONAL_ID, iwrb.getLocalizedString("application_error.no_courses_available", "There are no courses available for the user you have selected."));
						canContinue = false;
					}

					if (canContinue) {
						showPhaseTwo(iwc);
					}
					else {
						showPhaseOne(iwc);
					}
					break;

				case ACTION_PHASE_3:
					showPhaseThree(iwc);
					break;

				case ACTION_PHASE_4:
					showPhaseFour(iwc);
					break;

				case ACTION_PHASE_5:
					if (handleSubAction(iwc)) {
						showPhaseFive(iwc);
					}
					else {
						showPhaseSix(iwc);
					}
					break;

				case ACTION_PHASE_6:
					handleSubAction(iwc);
					showPhaseSix(iwc);
					break;

				case ACTION_PHASE_7:
					if (handleSubAction(iwc)) {
						showPhaseSeven(iwc);
					}
					else {
						showPhaseSix(iwc);
					}
					break;

				case ACTION_SAVE:
					save(iwc);
					break;
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private boolean handleSubAction(IWContext iwc) throws RemoteException {
		String sub = iwc.getParameter(SUB_ACTION);
		if (SUB_ACTION_ADD.equals(sub)) {
			return addApplications(iwc);
		}
		else if (SUB_ACTION_REMOVE.equals(sub)) {
			removeApplication(iwc);
		}

		return true;
	}

	private int parseAction(IWContext iwc) throws RemoteException {
		if (getSchoolTypePK() != null) {
			iCategory = this.getCourseBusiness(iwc).getCourseCategory(getSchoolTypePK());
			hasCare = iCategory != null ? iCategory.hasCare() : true;
			allowsAllChildren = iCategory != null ? iCategory.allowsAllChildren() : true;
		}

		int action = ACTION_PHASE_1;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}

		return action;
	}

	private int getNumberOfPhases(IWContext iwc) {
		int numberOfPhases = 8;

		return numberOfPhases;
	}

	private User getApplicant(IWContext iwc) {
		String childID = iwc.getParameter(PARAMETER_CHILD_PK);
		String ssn = iwc.getParameter(PARAMETER_CHILD_PERSONAL_ID);
		if (childID != null && !childID.trim().equals("")) {
			try {
				return getUserBusiness(iwc).getUser(new Integer(childID));
			}
			catch (NumberFormatException e) {
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		else if (ssn != null && !ssn.trim().equals("")) {
			try {
				return getUserBusiness(iwc).getUser(ssn);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private Form getForm(int state) {
		Form form = new Form();
		switch (state) {
			case ACTION_PHASE_1:

				break;

			case ACTION_PHASE_2:
				form.maintainParameter(PARAMETER_CHILD_PK);
				form.maintainParameter(PARAMETER_CHILD_PERSONAL_ID);
				break;

			case ACTION_PHASE_3:
				form.maintainParameter(PARAMETER_CHILD_PK);
				form.maintainParameter(PARAMETER_CHILD_PERSONAL_ID);
				break;

			case ACTION_PHASE_4:
				form.maintainParameter(PARAMETER_CHILD_PK);
				form.maintainParameter(PARAMETER_CHILD_PERSONAL_ID);
				break;

			case ACTION_PHASE_5:
				form.maintainParameter(PARAMETER_CHILD_PK);
				form.maintainParameter(PARAMETER_CHILD_PERSONAL_ID);
				break;

			case ACTION_PHASE_6:
				form.maintainParameter(PARAMETER_CHILD_PK);
				form.maintainParameter(PARAMETER_CHILD_PERSONAL_ID);
				break;

			case ACTION_PHASE_7:
				form.maintainParameter(PARAMETER_CHILD_PK);
				form.maintainParameter(PARAMETER_CHILD_PERSONAL_ID);
				break;

			case ACTION_PHASE_8:
				form.maintainParameter(PARAMETER_CHILD_PK);
				form.maintainParameter(PARAMETER_CHILD_PERSONAL_ID);
				break;
		}

		return form;
	}

	private void showPhaseOne(IWContext iwc) throws RemoteException {
		User chosenUser = null;
		User user = getUser(iwc);

		Form form = getForm(ACTION_PHASE_1);
		form.setId("course_step_1");
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1)));
		form.maintainParameter(PARAMETER_COURSE);

		addErrors(iwc, form);

		form.add(getPhasesHeader(iwrb.getLocalizedString("applicant", "Applicant"), 1, getNumberOfPhases(iwc), true));

		Layer info = new Layer(Layer.DIV);
		info.setStyleClass("info");

		form.add(info);

		if (allowsAllChildren) {
			Lists lists = new Lists();
			lists.add(iwrb.getLocalizedString("help.one_applicant_is_registered_at_a_time", "One applicant is registered at a time."));
			lists.add(iwrb.getLocalizedString("help.select_an_applicant_from_the_dropdown_OR_type_in_a_social_security_number", "Select an applicant from the dropdown OR type in a social security number."));
			lists.add(iwrb.getLocalizedString("help.when_a_registration_is_complete_you_can_then_register_another_child", "When registration is complete you can go back and register another."));
			lists.add(iwrb.getLocalizedString("help.when_all_applicants_have_been_registered_you_pay_for_them_all_at_the_same_time", "When all applicants have been registered you pay for them all at the same time."));
			//lists.add(iwrb.getLocalizedString("help.only_three_weeks_possible", "It is only possible to register each participant to max three courses at a time."));
			lists.add(iwrb.getLocalizedString(
					getPhaseOneHelpPrefix() == null ? "help.joined_courses" : getPhaseOneHelpPrefix() + "help.joined_courses",
					"Courses can be joined to maximize usage.")
			);
			info.add(lists);

			Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.select_child", "Select child"));
			heading.setStyleClass("subHeader");
			heading.setStyleClass("topSubHeader");
			form.add(heading);

			Layer section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);

			Layer helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("application.select_child_help", "Please select if you want to register your own child or another child.")));
			section.add(helpLayer);

			RadioButton ownChild = new RadioButton(PARAMETER_CHILD_OPTION, Boolean.TRUE.toString());
			ownChild.setStyleClass("radiobutton");
			ownChild.keepStatusOnAction(true);
			ownChild.setToDisableOnClick(PARAMETER_CHILD_PERSONAL_ID, true);
			ownChild.setToDisableOnClick(PARAMETER_CHILD_PK, false, false);

			RadioButton otherChild = new RadioButton(PARAMETER_CHILD_OPTION, Boolean.FALSE.toString());
			otherChild.setStyleClass("radiobutton");
			otherChild.keepStatusOnAction(true);
			otherChild.setToDisableOnClick(PARAMETER_CHILD_PK, true, false);
			otherChild.setToDisableOnClick(PARAMETER_CHILD_PERSONAL_ID, false);

			if (iwc.isParameterSet(PARAMETER_CHILD_OPTION)) {
				boolean currentPayer = new Boolean(iwc.getParameter(PARAMETER_CHILD_OPTION)).booleanValue();
				ownChild.setSelected(currentPayer);
				otherChild.setSelected(!currentPayer);
			}
			else {
				ownChild.setSelected(true);
			}

			Layer formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("radioButtonItem");
			Label label = new Label(this.iwrb.getLocalizedString("application.own_child", "Own child"), ownChild);
			formItem.add(ownChild);
			formItem.add(label);
			section.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("radioButtonItem");
			label = new Label(this.iwrb.getLocalizedString("application.other_child", "Other child"), otherChild);
			formItem.add(otherChild);
			formItem.add(label);
			section.add(formItem);

			heading = new Heading1(iwrb.getLocalizedString("select_an_applicant", "Select an applicant"));
			heading.setStyleClass("subHeader");
			form.add(heading);

			section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);

			helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("select_applicant_helper_text", "Please select the applicant from the drop down list.")));
			section.add(helpLayer);

			try {
				DropdownMenu chooser = getUserChooser(iwc, user, chosenUser, PARAMETER_CHILD_PK, iwrb);
				chooser.setDisabled(!ownChild.getSelected());
				formItem = new Layer(Layer.DIV);
				formItem.setStyleClass("formItem");
				label = new Label(this.iwrb.getLocalizedString("name", "Name"), chooser);
				formItem.add(label);
				formItem.add(chooser);
				section.add(formItem);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}

			heading = new Heading1(iwrb.getLocalizedString("type_social_security_number", "Type in social security number"));
			heading.setStyleClass("subHeader");
			form.add(heading);

			section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);

			TextInput ssn = new TextInput(PARAMETER_CHILD_PERSONAL_ID);
			ssn.setMaxlength(10);
			ssn.setDisabled(!otherChild.getSelected());

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString("social_security_number", "Social security number"), ssn);
			formItem.add(label);
			formItem.add(ssn);
			section.add(formItem);

			helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("select_other_applicant_helper_text", "Please select an applicant by writing in the personal ID in the field to the left.")));
			section.add(helpLayer);
		}
		else {
			User applicant = getApplicant(iwc);

			Heading1 heading = new Heading1(this.iwrb.getLocalizedString("select_applicant", "Select applicant"));
			info.add(heading);

			DropdownMenu chooser = getUserChooser(iwc, iApplicationPK, getUser(iwc), applicant, PARAMETER_CHILD_PK, this.iwrb);
			chooser.setToSubmit();

			Layer formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			Label label = new Label(this.iwrb.getLocalizedString("name", "Name"), chooser);
			formItem.add(label);
			formItem.add(chooser);
			info.add(formItem);

			Layer helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("select_applicant_helper_text", "Please select the applicant from the drop down list.")));
			info.add(helpLayer);

			Layer clearLayer = new Layer(Layer.DIV);
			clearLayer.setStyleClass("Clear");
			info.add(clearLayer);

			heading = new Heading1(this.iwrb.getLocalizedString("applicant", "Applicant"));
			heading.setStyleClass("subHeader");
			heading.setStyleClass("topSubHeader");
			form.add(heading);

			Layer section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);

			Address address = null;
			PostalCode code = null;
			if (applicant != null) {
				address = getUserBusiness(iwc).getUsersMainAddress(applicant);
				if (address != null) {
					code = address.getPostalCode();
				}
			}

			TextInput name = new TextInput("name");
			if (applicant != null) {
				name.setContent(applicant.getName());
			}
			name.setDisabled(true);

			TextInput personalID = new TextInput("personalID");
			if (applicant != null) {
				personalID.setContent(PersonalIDFormatter.format(applicant.getPersonalID(), iwc.getCurrentLocale()));
			}
			personalID.setDisabled(true);

			TextInput addr = new TextInput("address");
			if (address != null) {
				addr.setContent(address.getStreetAddress());
			}
			addr.setDisabled(true);

			TextInput postal = new TextInput("postal");
			if (code != null) {
				postal.setContent(code.getPostalAddress());
			}
			postal.setDisabled(true);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString("name", "Name"), name);
			formItem.add(label);
			formItem.add(name);
			section.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString("personalID", "Personal ID"), personalID);
			formItem.add(label);
			formItem.add(personalID);
			section.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString("address", "Address"), addr);
			formItem.add(label);
			formItem.add(addr);
			section.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString("zip_code", "Zip code"), postal);
			formItem.add(label);
			formItem.add(postal);
			section.add(formItem);

			clearLayer = new Layer(Layer.DIV);
			clearLayer.setStyleClass("Clear");
			section.add(clearLayer);
		}

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link link = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		link.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2));
		link.setToFormSubmit(form);
		bottom.add(link);

		add(form);
	}

	private void showPhaseTwo(IWContext iwc) throws RemoteException {
		saveCustodianInfo(iwc, true);

		Form form = getForm(ACTION_PHASE_2);
		form.addParameter(PARAMETER_ACTION, ACTION_PHASE_2);
		form.maintainParameter(PARAMETER_COURSE);

		Custodian custodian = null;
		User applicant = getApplicant(iwc);
		Child child = getMemberFamilyLogic(iwc).getChild(applicant);
		if (iwc.isParameterSet(PARAMETER_REMOVE_CUSTODIAN)) {
			child.removeExtraCustodian();
		}
		else if (iwc.isParameterSet(PARAMETER_PERSONAL_ID)) {
			custodian = child.getExtraCustodian();
			saveCustodianInfo(iwc, false);

			String personalID = iwc.getParameter(PARAMETER_PERSONAL_ID);
			try {
				custodian = getMemberFamilyLogic(iwc).getCustodian(getUserBusiness(iwc).getUser(personalID));
			}
			catch (FinderException fe) {
				setError(PARAMETER_PERSONAL_ID, this.iwrb.getLocalizedString("no_user_found_with_personal_id", "No user found with personal ID") + ": " + personalID);
			}
		}

		addErrors(iwc, form);

		List<String> scripts = new ArrayList<String>();
		scripts.add("/dwr/interface/CourseDWRUtil.js");
		scripts.add(CoreConstants.DWR_ENGINE_SCRIPT);
		scripts.add(CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

		StringBuffer script = new StringBuffer();
		script.append("function readUser() {\n\tvar id = dwr.util.getValue(\"" + PARAMETER_PERSONAL_ID + "\");\n\tvar child = '" + applicant.getPrimaryKey().toString() + "';\n\tvar relation = dwr.util.getValue(\"userRelation\");\n\tCourseDWRUtil.getUserDWRByRelation(id, child, '1', '" + iwc.getCurrentLocale().getCountry() + "', relation, fillUser);\n}");

		StringBuffer script2 = new StringBuffer();
		script2.append("function fillUser(auser) {\n\tdwr.util.setValues(auser);\n}");

		Script formScript = new Script();
		formScript.addFunction("readUser", script.toString());
		formScript.addFunction("fillUser", script2.toString());
		form.add(formScript);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.custodian_information", "Custodian information"), 2, getNumberOfPhases(iwc)));

		form.add(getPersonInfo(iwc, applicant));

		Collection<Custodian> custodians = null;
		try {
			custodians = child.getCustodians();
		}
		catch (NoCustodianFound ncf) {
			custodians = new ArrayList<Custodian>();
		}

		User currentUser = getUser(iwc);
		int number = 1;
		Iterator<Custodian> iter = custodians.iterator();
		while (iter.hasNext()) {
			Custodian element = iter.next();
			boolean hasRelation = isSchoolAdministrator(iwc) || getMemberFamilyLogic(iwc).isRelatedTo(element, currentUser) || currentUser.getPrimaryKey().equals(element.getPrimaryKey());

			addParentToForm(form, iwc, child, element, false, number++, false, false, hasRelation);
		}

		boolean hasRelation = custodian != null && (isSchoolAdministrator(iwc) || getMemberFamilyLogic(iwc).isRelatedTo(custodian, currentUser) || currentUser.getPrimaryKey().equals(custodian.getPrimaryKey()));
		addParentToForm(form, iwc, child, custodian, true, number, true, false, hasRelation);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3));
		next.setToFormSubmit(form);
		bottom.add(next);

		Link back = getButtonLink(this.iwrb.getLocalizedString("previous", "Previous"));
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1));
		back.setToFormSubmit(form);
		bottom.add(back);

		add(form);
	}

	private void showPhaseThree(IWContext iwc) throws RemoteException {
		boolean saved = saveCustodianInfo(iwc, false);
		if (!saved) {
			showPhaseTwo(iwc);
			return;
		}
		if (iwc.isParameterSet(PARAMETER_BACK)) {
			saveChildInfo(iwc, getApplicant(iwc));
		}

		User owner = getUser(iwc);

		Form form = getForm(ACTION_PHASE_3);
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3));
		form.maintainParameter(PARAMETER_COURSE);

		addErrors(iwc, form);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.relative_information", "Relative information"), 3, getNumberOfPhases(iwc)));

		User applicant = getApplicant(iwc);
		Child child = getMemberFamilyLogic(iwc).getChild(applicant);
		// User user = iwc.getCurrentUser();
		form.add(getPersonInfo(iwc, applicant));

		List<Relative> relatives = new ArrayList<Relative>();
		relatives.add(child.getMainRelative(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey()));
		relatives.addAll(child.getRelatives(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey()));
		for (int a = 1; a <= 3; a++) {
			Relative relative = null;
			if (relatives.size() >= a) {
				relative = relatives.get(a - 1);
			}

			addRelativeToForm(form, relative, a, a == 1);
		}

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4));
		next.setToFormSubmit(form);
		bottom.add(next);

		Link back = getButtonLink(this.iwrb.getLocalizedString("previous", "Previous"));
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2));
		back.setToFormSubmit(form);
		bottom.add(back);

		add(form);
	}

	private void showPhaseFour(IWContext iwc) throws RemoteException {
		boolean saved = saveCustodianInfo(iwc, true);
		if (!saved) {
			showPhaseThree(iwc);
			return;
		}

		Form form = getForm(ACTION_PHASE_4);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4)));
		form.add(new HiddenInput(PARAMETER_BACK, ""));
		form.maintainParameter(PARAMETER_COURSE);

		addErrors(iwc, form);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.child_information", "Child information"), 4, getNumberOfPhases(iwc)));

		User applicant = getApplicant(iwc);
		// User user = iwc.getCurrentUser();
		form.add(getPersonInfo(iwc, applicant));

		addChildInformation(iwc, getApplicant(iwc), form);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		form.add(clearLayer);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		if (iwc.isParameterSet(PARAMETER_COURSE)) {
			next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6));
		}
		else {
			next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		}
		next.setToFormSubmit(form);
		bottom.add(next);

		Link back = getButtonLink(this.iwrb.getLocalizedString("previous", "Previous"));
		back.setValueOnClick(PARAMETER_BACK, Boolean.TRUE.toString());
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3));
		back.setToFormSubmit(form);
		bottom.add(back);

		add(form);
	}

	private void showPhaseFive(IWContext iwc) throws RemoteException {
		User applicant = getApplicant(iwc);
		if (!saveChildInfo(iwc, applicant)) {
			showPhaseFour(iwc);
			return;
		}

		if (isRequiredToSelectYesOrNoAboutChild(iwc.getApplicationSettings())) {
			if (!iwc.isParameterSet(PARAMETER_GROWTH_DEVIATION)) {
				setError(
						ACTION_PHASE_4,
						PARAMETER_GROWTH_DEVIATION,
						iwrb.getLocalizedString(
								"application_error.answer_must_be_provided_about_child_growth_deviation",
								"You must provide answer about child's growth deviation")
				);
			}
			if (!iwc.isParameterSet(PARAMETER_ALLERGIES)) {
				setError(
						ACTION_PHASE_4,
						PARAMETER_ALLERGIES,
						iwrb.getLocalizedString(
								"application_error.answer_must_be_provided_about_child_allergies",
								"You must provide answer about child's allergies")
				);
			}

			if (hasErrors(ACTION_PHASE_4)) {
				showPhaseFour(iwc);
				return;
			}
		}

		Integer applicantPK = (Integer) applicant.getPrimaryKey();

		List<String> scripts = new ArrayList<String>();
		scripts.add("/dwr/interface/CourseDWRUtil.js");
		scripts.add(CoreConstants.DWR_ENGINE_SCRIPT);
		scripts.add(CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

		boolean isCourseAdmin = iwc.hasRole(CourseConstants.SUPER_ADMINISTRATOR_ROLE_KEY);

		StringBuffer script2 = new StringBuffer();
		script2.append("function setOptions(data) {\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_TYPE + "\");\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_COURSE_TYPE + "\");\n").append("\tdwr.util.addOptions(\"" + PARAMETER_COURSE_TYPE + "\", data);\n").append("}");

		StringBuffer script = new StringBuffer();
		script.append("function changeValues() {\n").append(getSchoolTypePK() != null ? "\tvar val = '" + getSchoolTypePK() + "';\n" : "\tvar val = +$(\"" + PARAMETER_CATEGORY + "\").value;\n").append("\tvar TEST = CourseDWRUtil.getCourseTypesDWR(val, '" + iwc.getCurrentLocale().getCountry() + "', setOptions);\n").append("\tgetCourses();\n").append("}");

		StringBuffer script3 = new StringBuffer();
		script3.append("function getCourses() {\n").append("\tvar val = +$(\"" + PARAMETER_PROVIDER + "\") != null ? +$(\"" + PARAMETER_PROVIDER + "\").value : -1;\n").append(getSchoolTypePK() != null ? "\tvar val2 = '" + getSchoolTypePK() + "';\n" : "\tvar val2 = +$(\"" + PARAMETER_CATEGORY + "\").value;\n").append("\tvar val3 = +$(\"" + PARAMETER_COURSE_TYPE + "\") != null ? +$(\"" + PARAMETER_COURSE_TYPE + "\").value : -1;\n").append("\tvar TEST = CourseDWRUtil.getCoursesDWR(val, val2, val3, " + applicantPK.intValue() + ", '" + iwc.getCurrentLocale().getCountry() + "', " + String.valueOf(isCourseAdmin) + ", setCourses);\n").append("}");

		StringBuffer script6 = new StringBuffer();
		script6.append("function getProviders() {\n").append("\tvar val = +$(\"" + PARAMETER_COURSE_TYPE + "\") != null ? +$(\"" + PARAMETER_COURSE_TYPE + "\").value : -1;\n").append("\tvar TEST = CourseDWRUtil.getProvidersDWR(" + applicantPK.intValue() + ", val, '" + iwc.getCurrentLocale().getCountry() + "', setProviders);\n").append("}");

		StringBuffer script7 = new StringBuffer();
		script7.append("function setProviders(data) {\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_PROVIDER + "\");\n").append("\tdwr.util.removeAllOptions(\"" + PARAMETER_PROVIDER + "\");\n").append("\tdwr.util.addOptions(\"" + PARAMETER_PROVIDER + "\", data);\n").append("}");

		StringBuffer script4 = new StringBuffer();
		script4.append("var getName = function(course) { return course.name };\n");
		script4.append("var getPk = function(course) { return course.pk };");
		script4.append("var getFrom = function(course) { return course.from };\n");
		script4.append("var getTo = function(course) { return course.to };\n");
		script4.append("var getDescription = function(course) { return course.description };\n");
		script4.append("var getTimeframe = function(course) { return course.timeframe };\n");
		script4.append("var getDays = function(course) { return course.days };\n");
		script4.append("var getPrice = function(course) { return course.price };\n");
		script4.append("var getProvider = function(course) { return course.provider };\n");
		script4.append("var getRadioButton = function(course) { return getRadio(course); };\n");

		script4.append("function setCourses(data) {\n").append("\tvar isEmpty = true;\n").append("\tfor (var prop in data) { isEmpty = false } \n").append("\tif (isEmpty == true) {\n").append("\t}\n").append("\tdwr.util.removeAllRows(\"" + PARAMETER_COURSE_TABLE_ID + "\");\n").append("\tdwr.util.addRows(\"" + PARAMETER_COURSE_TABLE_ID + "\", data, [getRadio, getProvider, getName, getTimeframe, getDays], { rowCreator:function(options) { var row = document.createElement(\"tr\"); if (options.rowData.isfull) { row.className = \"isfull\" }; return row; }});\n").append("\tvar table = $(\"" + PARAMETER_COURSE_TABLE_ID + "\");\n").append("\tvar trs = table.childNodes;\n").append("\tfor (var rowNum = 0; rowNum < trs.length; rowNum++) {\n").append("\t\tvar currentRow = trs[rowNum];\n").append("\t\tvar tds = currentRow.childNodes;\n").append("\t\tfor (var colNum = 0; colNum < tds.length; colNum++) {\n").append("\t\t\tvar obj = tds[colNum].firstChild;\n").append("\t\t\tif (obj != null && obj.className == 'checkbox') {\n");

		Collection inrepps = getCourseApplicationSession(iwc).getUserApplications(getApplicant(iwc));
		if (inrepps != null && !inrepps.isEmpty()) {
			script4.append("\t\t\t\t if (");
			Iterator iter = inrepps.iterator();
			boolean first = true;
			while (iter.hasNext()) {
				if (!first) {
					script4.append(" || ");
				}
				script4.append("obj.id == " + ((ApplicationHolder) iter.next()).getCourse().getPrimaryKey().toString());
				first = false;
			}
			script4.append(") {obj.disabled=true;obj.checked=true}\n");
		}
		script4.append("\t\t\t}\n").append("\t\t\ttds[colNum].className=\"column\"+colNum;\n").append("\t\t}\n").append("\t\tvar tds3 = currentRow.childNodes;\n").append("\t}\n")

		.append("}");

		StringBuffer script5 = new StringBuffer();
		script5.append("function getRadio(course) { \n").append("\nvar sel = (course.pk == 0);\n");
		script5.append("\n\t").append("var radio = document.createElement(\"input\");");
		script5.append("\n\t").append("radio.id = course.pk;");
		script5.append("\n\t").append("radio.type = \"checkbox\";");
		//script5.append("\n\t").append("if (course.isfull) { radio.disabled = true; }");
		script5.append("\n\t").append("radio.className = \"checkbox\";");
		script5.append("\n\t").append("radio.name = \"" + PARAMETER_COURSE + "\";");
		script5.append("\n\t").append("radio.value = course.pk;");
		script5.append("\n\t").append("return radio;");
		//script5.append("\n").append("return '<input id=\"'+course.pk+'\" type=\"checkbox\" class=\"checkbox\" name=\"" + PARAMETER_COURSE + "\" value=\"'+course.pk+'\" />'; \n");
		script5.append("}\n");

		Script formScript = new Script();
		formScript.addFunction("setOptions", script2.toString());
		formScript.addFunction("changeValues", script.toString());
		formScript.addFunction("getCourses", script3.toString());
		formScript.addFunction("setCourses", script4.toString());
		formScript.addFunction("setCourseID", script5.toString());
		formScript.addFunction("getProviders", script6.toString());
		formScript.addFunction("setProviders", script7.toString());

		Form form = getForm(ACTION_PHASE_5);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5)));
		form.add(new HiddenInput(PARAMETER_BACK, ""));

		if (iwc.isParameterSet(PARAMETER_GROWTH_DEVIATION)) {
			form.maintainParameter(PARAMETER_GROWTH_DEVIATION);
		}
		if (iwc.isParameterSet(PARAMETER_ALLERGIES)) {
			form.maintainParameter(PARAMETER_ALLERGIES);
		}

		form.add(formScript);

		addErrors(5, iwc, form);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("course", "Course"), 5, getNumberOfPhases(iwc)));

		// User user = iwc.getCurrentUser();
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
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.course_navigation_help", "Select a school type and a course type.  You can also select a specific provider.")));
		section.add(helpLayer);

		boolean useDWR = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_DWR, true);

		Integer schoolTypePK = getSchoolTypePK() != null ? new Integer(getSchoolTypePK().toString()) : null;
		if (schoolTypePK == null) {
			if (iwc.isParameterSet(PARAMETER_CATEGORY)) {
				schoolTypePK = new Integer(iwc.getParameter(PARAMETER_CATEGORY));
				if (schoolTypePK.intValue() < 0) {
					schoolTypePK = null;
				}
			}

			Collection schoolTypes = getCourseBusiness(iwc).getAllSchoolTypes();
			schoolTypePK = (Integer) ((SchoolType) schoolTypes.iterator().next()).getPrimaryKey();
			DropdownMenu catMenu = new DropdownMenu(schoolTypes, PARAMETER_CATEGORY);
			catMenu.addMenuElementFirst("", iwrb.getLocalizedString("select_school_type", "Select school type"));
			catMenu.setId(PARAMETER_CATEGORY);
			catMenu.keepStatusOnAction(true);
			catMenu.setOnChange("changeValues();");

			Layer formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("category", "Category"))), catMenu);
			formItem.add(label);
			formItem.add(catMenu);
			section.add(formItem);
		}

		DropdownMenu typeMenu = new DropdownMenu(PARAMETER_COURSE_TYPE);
		typeMenu.setId(PARAMETER_COURSE_TYPE);
		Collection courseTypes = getCourseBusiness(iwc).getCourseTypes(schoolTypePK, true);
		Iterator iter = courseTypes.iterator();
		while (iter.hasNext()) {
			CourseType courseType = (CourseType) iter.next();
			if (iUseSessionUser || getCourseBusiness(iwc).hasAvailableCourses(applicant, courseType)) {
				typeMenu.addMenuElement(courseType.getPrimaryKey().toString(), courseType.getName());
			}
		}
		typeMenu.addMenuElementFirst("-1", iwrb.getLocalizedString("select_course_type", "Select course type"));
		if (useDWR) {
			typeMenu.setOnChange("getProviders();");
			typeMenu.setOnChange("getCourses();");
		}
		else {
			typeMenu.setToSubmit();
		}
		typeMenu.keepStatusOnAction(true);

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("type", "Type"))), typeMenu);
		formItem.add(label);
		formItem.add(typeMenu);
		section.add(formItem);

		Collection providers = getCourseBusiness(iwc).getProviders();
		CourseType type = null;
		if (iwc.isParameterSet(PARAMETER_COURSE_TYPE)) {
			type = getCourseBusiness(iwc).getCourseType(iwc.getParameter(PARAMETER_COURSE_TYPE));
		}
		Collection filtered = new ArrayList();

		iter = providers.iterator();
		while (iter.hasNext()) {
			School provider = (School) iter.next();
			if (getCourseBusiness(iwc).hasAvailableCourses(applicant, provider, type)) {
				filtered.add(provider);
			}
		}

		DropdownMenu providerMenu = new DropdownMenu(filtered, PARAMETER_PROVIDER);
		providerMenu.setId(PARAMETER_PROVIDER);
		providerMenu.addMenuElementFirst("-1", iwrb.getLocalizedString("all_providers", "All providers"));
		if (useDWR) {
			providerMenu.setOnChange("getCourses();");
		}
		else {
			providerMenu.setToSubmit();
		}
		providerMenu.setId(PARAMETER_PROVIDER);
		providerMenu.keepStatusOnAction(true);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("provider", "Provider"))), providerMenu);
		formItem.add(label);
		formItem.add(providerMenu);
		section.add(formItem);

		Integer courseTypePK = null;
		if (iwc.isParameterSet(PARAMETER_COURSE_TYPE)) {
			courseTypePK = new Integer(iwc.getParameter(PARAMETER_COURSE_TYPE));
			if (courseTypePK.intValue() < 0) {
				courseTypePK = null;
			}
		}

		Integer providerPK = null;
		if (iwc.isParameterSet(PARAMETER_PROVIDER)) {
			providerPK = new Integer(iwc.getParameter(PARAMETER_PROVIDER));
			if (providerPK.intValue() < 0) {
				providerPK = null;
			}
		}

		Collection courses = null;
		if (courseTypePK != null) {
			courses = getCourseBusiness(iwc).getCoursesDWR(providerPK != null ? providerPK.intValue() : -1, schoolTypePK.intValue(), courseTypePK.intValue(), ((Integer) getApplicant(iwc).getPrimaryKey()).intValue(), iwc.getCurrentLocale().getCountry(), this.iUseSessionUser);
		}

		Table2 table = new Table2();
		table.setStyleClass("courses");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.setStyleClass("header");
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("column0");

		cell = row.createHeaderCell();
		cell.setStyleClass("column1");
		cell.add(new Text(iwrb.getLocalizedString("provider", "Provider")));
		cell = row.createHeaderCell();
		cell.setStyleClass("column2");
		cell.add(new Text(iwrb.getLocalizedString("name", "Name")));
		cell = row.createHeaderCell();
		cell.setStyleClass("column3");
		cell.add(new Text(iwrb.getLocalizedString("timeframe_date", "Timeframe/Dates")));
		cell = row.createHeaderCell();
		if (hasCare) {
			cell.setStyleClass("column4");
			cell.add(new Text(iwrb.getLocalizedString("days", "Days")));
		}

		group = table.createBodyRowGroup();
		group.setId(PARAMETER_COURSE_TABLE_ID);
		if (courses != null) {
			iter = courses.iterator();
			int counter = 0;
			while (iter.hasNext()) {
				row = group.createRow();
				if (counter++ % 2 == 0) {
					row.setStyleClass("even");
				}
				else {
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
				cell.add(new Text(course.getProvider()));
				cell = row.createCell();
				cell.setStyleClass("column2");
				cell.add(new Text(course.getName()));
				cell = row.createCell();
				cell.setStyleClass("column3");
				cell.add(new Text(course.getTimeframe()));
				if (hasCare) {
					cell = row.createCell();
					cell.setStyleClass("column4");
					cell.add(new Text(course.getDays()));
				}
			}
		}

		heading = new Heading1(this.iwrb.getLocalizedString("available_courses", "Available courses"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString(getPrefixSafe() + (hasCare ? "application.available_courses_help" : "application.available_courses_help_nocare"), "If you have successfully selected from the navigation above you should see a list of courses to the right.  To select a course/s you check the checkbox for each course you want to select.")));
		section.add(helpLayer);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("formItemBig");
		formItem.add(table);
		section.add(formItem);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		form.add(clearLayer);

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

		IWTimestamp defaultStamp = new IWTimestamp();
		int backMonths = iwc.getApplicationSettings().getProperty(CourseConstants.PROPERTY_BACK_MONTHS) != null ? Integer.parseInt(iwc.getApplicationSettings().getProperty(CourseConstants.PROPERTY_BACK_MONTHS)) : -1;
		if (backMonths != -1) {
			defaultStamp.addMonths(backMonths);
		}
		else {
			defaultStamp = null;
		}
		IWTimestamp stamp = new IWTimestamp();

		boolean isFull = false;
		String[] courses = iwc.getParameterValues(PARAMETER_COURSE);
		for (int i = 0; courses != null && i < courses.length; i++) {
			ApplicationHolder h = new ApplicationHolder();
			Course course = getCourseBusiness(iwc).getCourse(new Integer(courses[i]));
			if (getCourseBusiness(iwc).isFull(course)) {
				isFull = true;
			}
			else {
				isFull = false;
			}

			IWTimestamp start = new IWTimestamp(course.getStartDate());
			if (course.getRegistrationEnd() != null) {
				start = new IWTimestamp(course.getRegistrationEnd());
			}
			else {
				if (getCourseBusiness(iwc).getTimeoutDay() > 0) {
					int day = getCourseBusiness(iwc).getTimeoutDay();
					while (start.getDayOfWeek() != day) {
						start.addDays(-1);
					}
				}
				if (getCourseBusiness(iwc).getTimeoutHour() > 0) {
					start.setHour(getCourseBusiness(iwc).getTimeoutHour());
					start.setMinute(0);
				}
			}

			if (!iUseSessionUser ? start.isLaterThan(stamp) : (defaultStamp != null ? start.isLaterThan(defaultStamp) : true) && getCourseBusiness(iwc).hasNotStarted(course, this.iUseSessionUser) && !getCourseBusiness(iwc).isRegistered(applicant, course) && getCourseBusiness(iwc).isOfAge(applicant, course)) {
				h.setUser(applicant);
				h.setCourse(course);
				h.setOnWaitingList(isFull);
				getCourseApplicationSession(iwc).addApplication(applicant, h);
			}

			if (!getCourseBusiness(iwc).hasNotStarted(course, this.iUseSessionUser) || (stamp.isLaterThan(start) && !iUseSessionUser)) {
				setError(ACTION_PHASE_5, PARAMETER_COURSE, iwrb.getLocalizedString("application_error.old_course_selected", "You have selected a course that has already started or finished: ") + course.getName());
			}
			if (getCourseBusiness(iwc).isRegistered(applicant, course)) {
				setError(ACTION_PHASE_5, PARAMETER_COURSE, iwrb.getLocalizedString("application_error.already_registered_course_selected", "You have selected a course that the participant is already registered to: ") + course.getName());
			}
			if (!getCourseBusiness(iwc).isOfAge(applicant, course)) {
				setError(ACTION_PHASE_5, PARAMETER_COURSE, iwrb.getLocalizedString("application_error.incorrect_age_course_selected", "You have selected a course that is not available for the participant: ") + course.getName());
			}
		}
		Collection applications = getCourseApplicationSession(iwc).getUserApplications(getApplicant(iwc));
		if (applications == null || applications.isEmpty() || hasErrors(ACTION_PHASE_5)) {
			showPhaseFive(iwc);
			return;
		}

		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, Arrays.asList(
				CoreConstants.DWR_ENGINE_SCRIPT,
				CoreConstants.DWR_UTIL_SCRIPT,
				"/dwr/interface/WebUtil.js"
		));

		Form form = getForm(ACTION_PHASE_6);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6)));
		form.add(new HiddenInput(PARAMETER_BACK, ""));
		form.add(new HiddenInput(SUB_ACTION, SUB_ACTION_ADD));
		if (iwc.isParameterSet(PARAMETER_GROWTH_DEVIATION)) {
			form.maintainParameter(PARAMETER_GROWTH_DEVIATION);
		}
		if (iwc.isParameterSet(PARAMETER_ALLERGIES)) {
			form.maintainParameter(PARAMETER_ALLERGIES);
		}
		add(form);
		addErrors(iwc, form);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("course", "Course"), 6, getNumberOfPhases(iwc)));

		// User user = iwc.getCurrentUser();
		form.add(getPersonInfo(iwc, applicant));

		Table2 table = new Table2();
		table.setStyleClass("courses");
		table.setStyleClass("coursesPhaseSix");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setWidth("100%");
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.setStyleClass("header");
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("column0");
		cell.add(new Text(iwrb.getLocalizedString("provider", "Provider")));
		cell = row.createHeaderCell();
		cell.setStyleClass("column1");
		cell.add(new Text(iwrb.getLocalizedString("type", "Type")));
		cell = row.createHeaderCell();
		cell.setStyleClass("column2");
		cell.add(new Text(iwrb.getLocalizedString("name", "Name")));
		cell = row.createHeaderCell();
		cell.setStyleClass("column3");
		cell.add(new Text(iwrb.getLocalizedString("date_short", "Date")));
		if (hasCare) {
			cell = row.createHeaderCell();
			cell.setStyleClass("column4");
			cell.add(new Text(iwrb.getLocalizedString("daycare", "Daycare")));
			cell = row.createHeaderCell();
			cell.setStyleClass("column5");
			cell.add(new Text(iwrb.getLocalizedString("trip_home", "Trip home")));
		}
		cell = row.createHeaderCell();
		cell.setStyleClass("column6");
		cell.add(Text.getNonBrakingSpace());

		group = table.createBodyRowGroup();

		Locale locale = iwc.getCurrentLocale();
		group.setId(PARAMETER_COURSE_TABLE_ID);

		NumberFormat format = NumberFormat.getCurrencyInstance(iwc.getCurrentLocale());
		int counter = 0;
		Iterator iter = applications.iterator();
		boolean showAlert = false;
		while (iter.hasNext()) {
			row = group.createRow();
			if (counter++ % 2 == 0) {
				row.setStyleClass("even");
			}
			else {
				row.setStyleClass("odd");
			}
			ApplicationHolder holder = (ApplicationHolder) iter.next();
			Course course = holder.getCourse();
			CoursePrice price = course.getPrice();
			CourseType type = course.getCourseType();
			School provider = course.getProvider();
			CourseDWR courseDWR = getCourseBusiness(iwc).getCourseDWR(locale, course, false);

			DropdownMenu daycare = new DropdownMenu(PARAMETER_DAYCARE);
			daycare.setStyleClass("dayCare");
			daycare.setOnFocus("this.style.width='225px';");
			daycare.setOnBlur("this.style.width='';");
			daycare.addMenuElement(CourseConstants.DAY_CARE_NONE, iwrb.getLocalizedString("none", "None"));
			if (provider != null && price != null) {
				if (provider.hasPreCare() && price.getPreCarePrice() > 0) {
					Object[] arguments = { format.format(price.getPreCarePrice()) };
					daycare.addMenuElement(CourseConstants.DAY_CARE_PRE, MessageFormat.format(iwrb.getLocalizedString("morning", "Morning"), arguments));
					showAlert = true;
				}
				if (provider.hasPostCare() && price.getPostCarePrice() > 0) {
					Object[] arguments = { format.format(price.getPostCarePrice()) };
					daycare.addMenuElement(CourseConstants.DAY_CARE_POST, MessageFormat.format(iwrb.getLocalizedString("afternoon", "Afternoon"), arguments));
					showAlert = true;
				}
				if (provider.hasPreCare() && provider.hasPostCare() && price.getPreCarePrice() > 0 && price.getPostCarePrice() > 0) {
					Object[] arguments = { format.format(price.getPreCarePrice() + price.getPostCarePrice()) };
					daycare.addMenuElement(CourseConstants.DAY_CARE_PRE_AND_POST, MessageFormat.format(iwrb.getLocalizedString("whole_day", "Whole day"), arguments));
					showAlert = true;
				}
			}
			daycare.keepStatusOnAction(true);
			String dayCareParamName = "cou_day_care_" + course.getPrimaryKey().toString();
			User user = holder.getUser();
			if (user != null) {
				dayCareParamName += "_" + user.getId();
			}
			daycare.setOnChange("WebUtil.setSessionProperty('" + iwc.getSessionId() + "', '" + dayCareParamName + "', dwr.util.getValue('" + daycare.getId() + "'));");
			Object selectedDayCare = iwc.getSessionAttribute(dayCareParamName);
			if (selectedDayCare instanceof String) {
				daycare.setSelectedElement((String) selectedDayCare);
			}

			DropdownMenu trip = new DropdownMenu(PARAMETER_TRIPHOME);
			trip.addMenuElement("", "");
			trip.addMenuElement(Boolean.TRUE.toString(), iwrb.getLocalizedString("picked_up", "Picked up"));
			trip.addMenuElement(Boolean.FALSE.toString(), iwrb.getLocalizedString("walks_home", "Walks home"));
			if (holder.getPickedUp() != null) {
				trip.setSelectedElement(holder.getPickedUp().toString());
			}
			trip.keepStatusOnAction(true);
			String tripParamName = "cou_trip_" + course.getPrimaryKey().toString();
			if (user != null) {
				tripParamName += "_" + user.getId();
			}
			trip.setOnChange("WebUtil.setSessionProperty('" + iwc.getSessionId() + "', '" + tripParamName + "', dwr.util.getValue('" + trip.getId() + "'));");
			Object selectedTrip = iwc.getSessionAttribute(tripParamName);
			if (selectedTrip instanceof String) {
				trip.setSelectedElement((String) selectedTrip);
			}

			Link link = new Link(iwb.getImage("delete.png", iwrb.getLocalizedString("remove_course", "Remove course")));
			link.addParameter(SUB_ACTION, SUB_ACTION_REMOVE);
			link.addParameter(PARAMETER_ACTION, ACTION_PHASE_6);
			link.maintainParameter(PARAMETER_CHILD_PK, iwc);
			link.maintainParameter(PARAMETER_CHILD_PERSONAL_ID, iwc);
			link.addParameter(PARAMETER_REMOVE_COURSE, course.getPrimaryKey().toString());

			cell = row.createCell();
			cell.setStyleClass("column0");
			cell.add(new Text(holder.getProvider().getName()));

			cell = row.createCell();
			cell.setStyleClass("column1");
			cell.add(new HiddenInput(PARAMETER_COURSE, courseDWR.getPk()));
			cell.add(new Text(type == null ? CoreConstants.MINUS : type.getLocalizationKey() != null ? iwrb.getLocalizedString(type.getLocalizationKey(), course.getCourseType().getName()) : type.getName()));

			cell = row.createCell();
			cell.setStyleClass("column2");
			cell.add(new Text(courseDWR.getName()));
			cell = row.createCell();
			cell.setStyleClass("column3");
			cell.add(new Text(courseDWR.getTimeframe()));
			cell = row.createCell();

			if (hasCare) {
				cell.setStyleClass("column4");
				if (holder.getDaycare() > 0) {
					daycare.setSelectedElement(holder.getDaycare());
				}
				cell.add(daycare);

				cell = row.createCell();
				cell.setStyleClass("column5");
				cell.add(trip);
			}

			cell = row.createCell();
			cell.setStyleClass("column6");
			cell.add(link);
		}

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("selected_courses", "Selected courses"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		section.setStyleClass("formSectionBig");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("select_daycare_and_trip_home_options_nocare", "Select daycare and trip home options.")));
		section.add(helpLayer);

		if (isFull) {
			section.add(getAttentionLayer(this.iwrb.getLocalizedString("selected_course_full", "The course/s you have selected is/are already full.  If you choose to continue with the registration they will be added to waiting list.")));
		}

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("formItemBig");
		formItem.add(table);
		section.add(formItem);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("next", "Next"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_7));
		if (showAlert && hasCare) {
			next.setClickConfirmation(this.iwrb.getLocalizedString("confirm.care_option", "Are you sure you have selected the correct care options?"));
		}
		next.setToFormSubmit(form);
		bottom.add(next);

		Link newAppl = getButtonLink(this.iwrb.getLocalizedString("another_applicant", "Another applicant"));
		newAppl.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1));
		if (showAlert && hasCare) {
			newAppl.setClickConfirmation(this.iwrb.getLocalizedString("confirm.care_option", "Are you sure you have selected the correct care options?"));
		}
		newAppl.setToFormSubmit(form);
		bottom.add(newAppl);

		Link newCourse = getButtonLink(this.iwrb.getLocalizedString("another_course", "Another course"));
		newCourse.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		newCourse.setToFormSubmit(form);
		bottom.add(newCourse);

		Link back = getButtonLink(this.iwrb.getLocalizedString("previous", "Previous"));
		back.setValueOnClick(PARAMETER_BACK, Boolean.TRUE.toString());
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		back.setToFormSubmit(form);
		bottom.add(back);
	}

	private void addParentToForm(
			Form form,
			IWContext iwc,
			Child child,
			Custodian custodian,
			boolean isExtraCustodian,
			int number,
			boolean editable,
			boolean showMaritalStatus,
			boolean hasRelation
	) throws RemoteException {
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
		if (isExtraCustodian) {
			relationMenu.setId("userRelation");
		}

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		if (!isExtraCustodian) {
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_RELATION)) {
				formItem.setStyleClass("hasError");
			}
		}
		Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("relation", "Relation"))), relationMenu);
		formItem.add(label);
		formItem.add(relationMenu);
		section.add(formItem);

		TextInput name = new TextInput("name", custodian != null ? custodian.getName() : null);
		name.setDisabled(true);
		if (isExtraCustodian) {
			name.setId("userName");
		}

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("name", "Name"), name);
		formItem.add(label);
		formItem.add(name);
		if (editable && custodian != null) {
			HiddenInput hidden = new HiddenInput(PARAMETER_REMOVE_CUSTODIAN, "");
			formItem.add(hidden);

			SubmitButton remove = new SubmitButton(this.iwrb.getLocalizedString("remove_custodian", "Remove custodian"));
			remove.setValueOnClick(PARAMETER_REMOVE_CUSTODIAN, Boolean.TRUE.toString());
			remove.setStyleClass("button");
			formItem.add(remove);
		}
		section.add(formItem);

		if (custodian != null || isExtraCustodian) {
			HiddenInput hidden = new HiddenInput(PARAMETER_USER, custodian != null ? custodian.getPrimaryKey().toString() : "");
			if (isExtraCustodian) {
				hidden.setId("userPK");
			}
			formItem.add(hidden);
		}

		TextInput personalID = null;
		if (!editable) {
			personalID = new TextInput("personalID", PersonalIDFormatter.format(custodian.getPersonalID(), iwc.getCurrentLocale()));
			personalID.setDisabled(true);
		}
		else {
			personalID = new TextInput(PARAMETER_PERSONAL_ID);
			personalID.setMaxlength(10);
			if (custodian != null) {
				personalID.setContent(custodian.getPersonalID());
			}
			personalID.setOnKeyUp("readUser();");
			personalID.setOnChange("readUser();");
		}

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		if (editable && !isExtraCustodian) {
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_PERSONAL_ID)) {
				formItem.setStyleClass("hasError");
			}
		}
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("personal_id", "Personal ID"))), personalID);
		formItem.add(label);
		formItem.add(personalID);
		section.add(formItem);

		TextInput addr = new TextInput("address");
		addr.setDisabled(true);
		if (isExtraCustodian) {
			addr.setId("userAddress");
		}

		TextInput zip = new TextInput("zipCode");
		zip.setDisabled(true);
		if (isExtraCustodian) {
			zip.setId("userPostalCode");
		}

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

		if (hasRelation || iUseSessionUser) {
			TextInput homePhone = new TextInput(PARAMETER_HOME_PHONE, null);
			if (phone != null) {
				homePhone.setContent(phone.getNumber());
			}
			homePhone.keepStatusOnAction(true, number - 1);
			if (isExtraCustodian) {
				homePhone.setId("userHomePhone");
			}

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
			if (isExtraCustodian) {
				workPhone.setId("userWorkPhone");
			}

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
			if (isExtraCustodian) {
				mobilePhone.setId("userMobilePhone");
			}

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
			if (isExtraCustodian) {
				mail.setId("userEmail");
			}

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label(this.iwrb.getLocalizedString("email", "E-mail"), mail);
			formItem.add(label);
			formItem.add(mail);
			section.add(formItem);
		}

		if (showMaritalStatus) {
			DropdownMenu maritalStatusMenu = getMaritalStatusDropdown(custodian);
			if (maritalStatus != null) {
				maritalStatusMenu.setSelectedElement(maritalStatus);
			}
			maritalStatusMenu.keepStatusOnAction(true);
			if (isExtraCustodian) {
				maritalStatusMenu.setId("userMaritalStatus");
			}

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

	private void addRelativeToForm(Form form, Relative relative, int number, boolean requiredRelation) {
		Heading1 heading = new Heading1(number == 1 ? this.iwrb.getLocalizedString("first_contact", "First contact") : this.iwrb.getLocalizedString("contact", "Contact"));
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
		if (number == 1) {
			helpLayer.add(new Text(this.iwrb.getLocalizedString("application.first_contact_help", "Add information about the first contact to the applicant. Please fill in the necessary information.  Please note that filling in the relation is mandatory.")));
		}
		else {
			helpLayer.add(new Text(this.iwrb.getLocalizedString("application.contact_help", "Add information about possible contacts to the applicant. Please fill in the necessary information.  Please note that filling in the relation is mandatory.")));
		}
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
		if (requiredRelation) {
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_RELATIVE_RELATION)) {
				formItem.setStyleClass("hasError");
			}
		}
		Label label = new Label(new Span(new Text(this.iwrb.getLocalizedString("relation", "Relation"))), relationMenu);
		formItem.add(label);
		formItem.add(relationMenu);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		if (requiredRelation) {
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_RELATIVE)) {
				formItem.setStyleClass("hasError");
			}
		}
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

	private void showPhaseSeven(IWContext iwc) throws RemoteException {
		Form form = getForm(ACTION_PHASE_7);
		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		form.add(new HiddenInput(PARAMETER_BACK, ""));

		if (iwc.isParameterSet(PARAMETER_GROWTH_DEVIATION)) {
			form.maintainParameter(PARAMETER_GROWTH_DEVIATION);
		}
		if (iwc.isParameterSet(PARAMETER_ALLERGIES)) {
			form.maintainParameter(PARAMETER_ALLERGIES);
		}

		addErrors(iwc, form);

		List<String> scripts = new ArrayList<String>();
		scripts.add("/dwr/interface/CourseDWRUtil.js");
		scripts.add(CoreConstants.DWR_ENGINE_SCRIPT);
		scripts.add(CoreConstants.DWR_UTIL_SCRIPT);
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);

		StringBuffer script = new StringBuffer();
		script.append("function readUser() {\n\tvar id = dwr.util.util.getValue(\"" + PARAMETER_PAYER_PERSONAL_ID + "\");\n\tvar child = '" + getUser(iwc).getPrimaryKey().toString() + "';\n\tCourseDWRUtil.getUserDWR(id, child, '1', '" + iwc.getCurrentLocale().getCountry() + "', fillUser);\n}");

		StringBuffer script2 = new StringBuffer();
		script2.append("function fillUser(auser) {\n\tuser = auser;\n\tdwr.util.setValues(user);\n}");

		Script formScript = new Script();
		formScript.addFunction("readUser", script.toString());
		formScript.addFunction("fillUser", script2.toString());
		form.add(formScript);

		form.add(getPhasesHeader(this.iwrb.getLocalizedString("application.payment_information", "Payment information"), 7, getNumberOfPhases(iwc)));

		form.add(getPersonInfo(iwc, null));

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("application.amount_information", "Amount information"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.amount_information_help", "This is the total amount due for all selected courses.")));
		section.add(helpLayer);

		Table2 table = new Table2();
		table.setStyleClass("courses");
		table.setStyleClass("coursesPhaseSeven");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setWidth("100%");
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.setStyleClass("header");

		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("name");
		cell.add(new Text(iwrb.getLocalizedString("name", "Name")));

		cell = row.createHeaderCell();
		cell.setStyleClass("personalID");
		cell.add(new Text(iwrb.getLocalizedString("personal_id", "Personal ID")));

		cell = row.createHeaderCell();
		cell.setStyleClass("amount");
		cell.add(new Text(iwrb.getLocalizedString("amount", "Amount")));

		group = table.createBodyRowGroup();

		Map applications = getCourseApplicationSession(iwc).getApplications();
		SortedSet prices = getCourseBusiness(iwc).calculatePrices(applications);
		Map discounts = getCourseBusiness(iwc).getDiscounts(prices, applications);

		NumberFormat format = NumberFormat.getInstance(iwc.getCurrentLocale());
		float totalPrice = 0;
		float discount = 0;
		int counter = 0;
		Iterator iter = prices.iterator();
		while (iter.hasNext()) {
			row = group.createRow();
			PriceHolder holder = (PriceHolder) iter.next();
			User user = holder.getUser();
			PriceHolder discountHolder = (PriceHolder) discounts.get(user);

			float price = holder.getPrice();
			totalPrice += price;
			discount += discountHolder.getPrice();

			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
			cell = row.createCell();
			cell.setStyleClass("name");
			cell.add(new Text(name.getName(iwc.getCurrentLocale())));

			cell = row.createCell();
			cell.setStyleClass("personalID");
			cell.add(new Text(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())));

			cell = row.createCell();
			cell.setStyleClass("amount");
			cell.add(new Text(format.format(price)));

			if (counter++ % 2 == 0) {
				row.setStyleClass("even");
			}
			else {
				row.setStyleClass("odd");
			}

			Collection<ApplicationHolder> holders = (Collection<ApplicationHolder>) applications.get(user);
			for (ApplicationHolder applicationHolder : holders) {
				row = group.createRow();
				row.setStyleClass("subRow");

				Course course = applicationHolder.getCourse();

				cell = row.createCell();
				cell.setStyleClass("name");
				cell.add(new Text(course.getProvider().getName() + CoreConstants.COMMA + CoreConstants.SPACE + course.getName()));

				cell = row.createCell();
				cell.setStyleClass("startDate");
				cell.add(new Text(new IWTimestamp(course.getStartDate()).getLocaleDate(iwc.getCurrentLocale())));

				cell = row.createCell();
				cell.setStyleClass("status");
				if (applicationHolder.isOnWaitingList()) {
					cell.setStyleClass("waitingList");
					cell.add(new Text(iwrb.getLocalizedString("application_status.waiting_list", "Waiting list")));
				}
				else {
					cell.setStyleClass("registered");
					cell.add(new Text(format.format(applicationHolder.getPrice())));
				}
			}
		}

		group = table.createFooterRowGroup();
		row = group.createRow();

		cell = row.createCell();
		cell.setColumnSpan(2);
		cell.setStyleClass("totalPrice");
		cell.add(new Text(iwrb.getLocalizedString("total_amount", "Total amount")));

		cell = row.createCell();
		cell.setStyleClass("totalPrice");
		cell.setStyleClass("price");
		cell.add(new Text(format.format(totalPrice)));

		float amountDue = totalPrice;
		if (discount > 0) {
			row = group.createRow();
			amountDue = totalPrice - discount;

			cell = row.createCell();
			cell.setColumnSpan(2);
			cell.setStyleClass("discount");
			cell.add(new Text(iwrb.getLocalizedString("discount", "Discount")));

			cell = row.createCell();
			cell.setStyleClass("discount");
			cell.setStyleClass("price");
			cell.add(new Text(format.format(discount)));

			row = group.createRow();

			cell = row.createCell();
			cell.setColumnSpan(2);
			cell.setStyleClass("amountDue");
			cell.add(new Text(iwrb.getLocalizedString("amount_due", "Amount due")));

			cell = row.createCell();
			cell.setStyleClass("amountDue");
			cell.setStyleClass("price");
			cell.add(new Text(format.format(totalPrice - discount)));
		}
		section.add(new HiddenInput(PARAMETER_AMOUNT, Float.toString(amountDue)));

		section.add(table);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		section.add(clearLayer);

		heading = new Heading1(this.iwrb.getLocalizedString("application.payer_information", "Payer information"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.payer_information_help", "Please enter informations about the destined payer, if other than yourself.")));
		section.add(helpLayer);

		RadioButton loggedInUser = new RadioButton(PARAMETER_PAYER_OPTION, Boolean.TRUE.toString());
		loggedInUser.setStyleClass("radiobutton");
		loggedInUser.keepStatusOnAction(true);
		loggedInUser.setToDisableOnClick(PARAMETER_PAYER_NAME, true);
		loggedInUser.setToDisableOnClick(PARAMETER_PAYER_PERSONAL_ID, true);

		RadioButton otherPayer = new RadioButton(PARAMETER_PAYER_OPTION, Boolean.FALSE.toString());
		otherPayer.setStyleClass("radiobutton");
		otherPayer.keepStatusOnAction(true);
		otherPayer.setToDisableOnClick(PARAMETER_PAYER_NAME, false);
		otherPayer.setToDisableOnClick(PARAMETER_PAYER_PERSONAL_ID, false);

		if (iwc.isParameterSet(PARAMETER_PAYER_OPTION)) {
			boolean currentPayer = new Boolean(iwc.getParameter(PARAMETER_PAYER_OPTION)).booleanValue();
			loggedInUser.setSelected(currentPayer);
			otherPayer.setSelected(!currentPayer);
		}

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		Label label = new Label(this.iwrb.getLocalizedString("payer.logged_in_user", "Logged in user"), loggedInUser);
		formItem.add(loggedInUser);
		formItem.add(label);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		label = new Label(this.iwrb.getLocalizedString("payer.other_payer", "Other payer"), otherPayer);
		formItem.add(otherPayer);
		formItem.add(label);
		section.add(formItem);

		TextInput payerName = new TextInput(PARAMETER_PAYER_NAME, null);
		payerName.setId("userName");
		payerName.keepStatusOnAction(true);
		payerName.setDisabled(loggedInUser.getSelected());

		TextInput payerPersonalID = new TextInput(PARAMETER_PAYER_PERSONAL_ID, null);
		payerPersonalID.setId(PARAMETER_PAYER_PERSONAL_ID);
		payerPersonalID.keepStatusOnAction(true);
		payerPersonalID.setMaxlength(10);
		payerPersonalID.setDisabled(loggedInUser.getSelected());
		payerPersonalID.setOnKeyUp("readUser();");
		payerPersonalID.setOnChange("readUser();");

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("application.payer_personal_id", "Personal ID"), payerPersonalID);
		formItem.add(label);
		formItem.add(payerPersonalID);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(this.iwrb.getLocalizedString("application.payer_name", "Name"), payerName);
		formItem.add(label);
		formItem.add(payerName);
		section.add(formItem);

		clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		section.add(clearLayer);

		heading = new Heading1(this.iwrb.getLocalizedString("application.payment_information", "Payment information"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		boolean showCreditCardPayment = isShowCreditCardPayment();
		boolean isCreditCardPayment = showCreditCardPayment;
		if (iwc.isParameterSet(PARAMETER_CREDITCARD_PAYMENT)) {
			isCreditCardPayment = new Boolean(iwc.getParameter(PARAMETER_CREDITCARD_PAYMENT)).booleanValue();
		}

		RadioButton giro = new RadioButton(PARAMETER_CREDITCARD_PAYMENT, Boolean.FALSE.toString());
		giro.setStyleClass("radiobutton");
		giro.setSelected(!isCreditCardPayment);
		giro.keepStatusOnAction(true);
		giro.setToDisableOnClick(PARAMETER_CARD_TYPE, true, false);
		giro.setToDisableOnClick(PARAMETER_CARD_NUMBER, true);
		giro.setToDisableOnClick(PARAMETER_VALID_MONTH, true, false);
		giro.setToDisableOnClick(PARAMETER_VALID_YEAR, true, false);

		RadioButton creditcard = new RadioButton(PARAMETER_CREDITCARD_PAYMENT, Boolean.TRUE.toString());
		creditcard.setStyleClass("radiobutton");
		creditcard.setSelected(isCreditCardPayment);
		creditcard.keepStatusOnAction(true);
		creditcard.setToDisableOnClick(PARAMETER_CARD_TYPE, false, false);
		creditcard.setToDisableOnClick(PARAMETER_CARD_NUMBER, false);
		creditcard.setToDisableOnClick(PARAMETER_VALID_MONTH, false, false);
		creditcard.setToDisableOnClick(PARAMETER_VALID_YEAR, false, false);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("application.payment_information_help", "Please select which type of payment you will be using to pay for the service.")));
		section.add(helpLayer);

		if (showCreditCardPayment) {
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("radioButtonItem");
			label = new Label(this.iwrb.getLocalizedString("application.payment_creditcard", "Payment with creditcard"), creditcard);
			formItem.add(creditcard);
			formItem.add(label);
			section.add(formItem);
		}

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		label = new Label(this.iwrb.getLocalizedString("application.payment_giro", "Payment with giro"), giro);
		formItem.add(giro);
		formItem.add(label);
		section.add(formItem);

		clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		section.add(clearLayer);

		if (showCreditCardPayment) {
			heading = new Heading1(this.iwrb.getLocalizedString("application.creditcard_information", "Creditcard information"));
			heading.setStyleClass("subHeader");
			form.add(heading);

			section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);

			helpLayer = new Layer(Layer.DIV);
			helpLayer.setStyleClass("helperText");
			helpLayer.add(new Text(this.iwrb.getLocalizedString("application.creditcard_information_help", "If you have selected to pay by creditcard, please fill in the creditcard information.  All the fields are required.")));
			section.add(helpLayer);

			Collection images = getCourseBusiness(iwc).getCreditCardImages();
			Iterator iterator = images.iterator();
			while (iterator.hasNext()) {
				Image image = (Image) iterator.next();
				image.setStyleClass("creditCardImage");
				helpLayer.add(image);
			}

			DropdownMenu cardType = getCardTypeDropdown(iwc.getCurrentLocale(), PARAMETER_CARD_TYPE);
			cardType.keepStatusOnAction(true);
			cardType.setDisabled(!showCreditCardPayment);

			TextInput cardOwner = new TextInput(PARAMETER_NAME_ON_CARD, null);
			cardOwner.keepStatusOnAction(true);
			cardOwner.setDisabled(!showCreditCardPayment);

			TextInput cardNumber = new TextInput(PARAMETER_CARD_NUMBER, null);
			cardNumber.setLength(16);
			cardNumber.setMaxlength(16);
			cardNumber.keepStatusOnAction(true);
			cardNumber.setDisabled(!showCreditCardPayment);

			TextInput ccNumber = new TextInput(PARAMETER_CCV, null);
			ccNumber.setLength(3);
			ccNumber.setMaxlength(3);
			ccNumber.keepStatusOnAction(true);
			ccNumber.setDisabled(!showCreditCardPayment);

			DropdownMenu validMonth = new DropdownMenu(PARAMETER_VALID_MONTH);
			validMonth.setWidth("45px");
			validMonth.keepStatusOnAction(true);
			for (int a = 1; a <= 12; a++) {
				validMonth.addMenuElement(TextSoap.addZero(a), TextSoap.addZero(a));
			}
			validMonth.setDisabled(!showCreditCardPayment);

			IWTimestamp stamp = new IWTimestamp();
			DropdownMenu validYear = new DropdownMenu(PARAMETER_VALID_YEAR);
			validYear.setWidth("60px");
			validYear.keepStatusOnAction(true);
			int year = stamp.getYear();
			for (int a = year; a <= year + 10; a++) {
				validYear.addMenuElement(String.valueOf(stamp.getYear()), String.valueOf(stamp.getYear()));
				stamp.addYears(1);
			}
			validYear.setDisabled(!showCreditCardPayment);

			boolean useDirectPayment = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_DIRECT_PAYMENT, false);

			if (useDirectPayment) {
				formItem = new Layer(Layer.DIV);
				formItem.setStyleClass("formItem");
				formItem.setStyleClass("required");
				label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.card_owner", "Card owner"))), cardOwner);
				formItem.add(label);
				formItem.add(cardOwner);
				section.add(formItem);
			}

			if (!useDirectPayment) {
				formItem = new Layer(Layer.DIV);
				formItem.setStyleClass("formItem");
				formItem.setStyleClass("required");
				label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.card_type", "Card type"))), cardType);
				formItem.add(label);
				formItem.add(cardType);
				section.add(formItem);
			}

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_CARD_NUMBER)) {
				formItem.setStyleClass("hasError");
			}
			label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.card_number", "Card number"))), cardNumber);
			formItem.add(label);
			formItem.add(cardNumber);
			section.add(formItem);

			if (useDirectPayment) {
				formItem = new Layer(Layer.DIV);
				formItem.setStyleClass("formItem");
				formItem.setStyleClass("required");
				label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.ccv_number", "Credit card verification number"))), ccNumber);
				formItem.add(label);
				formItem.add(ccNumber);
				section.add(formItem);
			}

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("required");
			label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.card_valid_time", "Card valid through"))), validMonth);
			formItem.add(label);
			formItem.add(validMonth);
			formItem.add(validYear);
			section.add(formItem);

			clearLayer = new Layer(Layer.DIV);
			clearLayer.setStyleClass("Clear");
			section.add(clearLayer);
		}

		heading = new Heading1(this.iwrb.getLocalizedString("application.agreement_info", "Agreement information"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		CheckBox agree = new CheckBox(PARAMETER_AGREEMENT, Boolean.TRUE.toString());
		agree.setStyleClass("checkbox");
		agree.keepStatusOnAction(true);

		Paragraph paragraph = new Paragraph();
		paragraph.setStyleClass("agreement");
		String agreementText = this.iwrb.getLocalizedString(getPrefixSafe() + "application.agreement", "Agreement text");
		String href = "href=\"";
		if (agreementText.indexOf(href) != -1) {
			String customLink = iwc.getIWMainApplication().getSettings().getProperty("cou_app_agree_link_" + iApplicationPK + "_" + iSchoolTypePK);
			if (!StringUtil.isEmpty(customLink)) {
				String pattern = agreementText.substring(agreementText.indexOf(href) + href.length());
				pattern = pattern.substring(0, pattern.indexOf("\""));
				agreementText = StringHandler.replace(agreementText, pattern, customLink);
			}
		}
		paragraph.add(new Text(agreementText));
		section.add(paragraph);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		formItem.setStyleClass("required");
		if (hasError(PARAMETER_AGREEMENT)) {
			formItem.setStyleClass("hasError");
		}
		label = new Label(new Span(new Text(this.iwrb.getLocalizedString("application.agree_terms", "Yes, I agree"))), agree);
		formItem.add(agree);
		formItem.add(label);
		section.add(formItem);

		clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		section.add(clearLayer);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link next = getButtonLink(this.iwrb.getLocalizedString("verify", "Verify"));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		next.setOnClick("this.style.display='none';");
		next.setToFormSubmit(form);
		bottom.add(next);

		Link back = getButtonLink(this.iwrb.getLocalizedString("previous", "Previous"));
		back.setValueOnClick(PARAMETER_BACK, Boolean.TRUE.toString());
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_6));
		back.setToFormSubmit(form);
		bottom.add(back);

		add(form);
	}

	private void save(IWContext iwc) throws RemoteException {
		Boolean payerOption = iwc.isParameterSet(PARAMETER_PAYER_OPTION) ? new Boolean(iwc.getParameter(PARAMETER_PAYER_OPTION)) : null;
		if (payerOption == null) {
			setError(ACTION_PHASE_7, PARAMETER_PAYER_OPTION, this.iwrb.getLocalizedString("must_select_payer_option", "You have to select a payer option."));
		}
		else if (!payerOption.booleanValue()) {
			if (!iwc.isParameterSet(PARAMETER_PAYER_PERSONAL_ID)) {
				setError(ACTION_PHASE_7, PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("payer_personal_id_empty", "Payer personal ID is empty"));
			}
			else if (!SocialSecurityNumber.isValidSocialSecurityNumber(iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID), iwc.getCurrentLocale()) || !SocialSecurityNumber.isIndividualSocialSecurityNumber(iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID), iwc.getCurrentLocale())) {
				setError(ACTION_PHASE_7, PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("invalid_personal_id", "Invalid personal ID"));
			}
			else {
				Date dateOfBirth = SocialSecurityNumber.getDateFromSocialSecurityNumber(iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID));
				Age age = new Age(dateOfBirth);
				if (age.getYears() < 18) {
					setError(ACTION_PHASE_7, PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("payer_must_be_at_least_18_years_old", "The payer you have selected is younger than 18 years old. Payers must be at least 18 years old or older."));
				}
				else {
					try {
						User currentUser = getUser(iwc);
						User paymentUser = getUserBusiness(iwc).getUser(iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID));

						Address address1 = currentUser.getUsersMainAddress();
						Address address2 = paymentUser.getUsersMainAddress();

						if (!address1.getStreetName().equals(address2.getStreetName()) || !address1.getStreetNumber().equals(address2.getStreetNumber()) || address1.getPostalCodeID() != address2.getPostalCodeID()) {
							setError(ACTION_PHASE_7, PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("payer_must_have_same_address", "The payer you select must live in the same building as you."));
						}
					} catch(FinderException fe) {
						setError(ACTION_PHASE_7, PARAMETER_PAYER_PERSONAL_ID, this.iwrb.getLocalizedString("application_error.no_user_found_with_personal_id", "No user found with the personal ID you entered."));
					}
				}
			}
			if (!iwc.isParameterSet(PARAMETER_PAYER_NAME)) {
				setError(ACTION_PHASE_7, PARAMETER_PAYER_NAME, this.iwrb.getLocalizedString("payer_name_empty", "Payer name is empty"));
			}
		}

		if (!iwc.isParameterSet(PARAMETER_AGREEMENT)) {
			setError(ACTION_PHASE_7, PARAMETER_AGREEMENT, this.iwrb.getLocalizedString("must_agree_terms", "You have to agree to the terms."));
		}

		if (hasErrors(ACTION_PHASE_7)) {
			showPhaseSeven(iwc);
			return;
		}

		String payerPersonalID = iwc.isParameterSet(PARAMETER_PAYER_PERSONAL_ID) ? iwc.getParameter(PARAMETER_PAYER_PERSONAL_ID) : "";
		String payerName = iwc.isParameterSet(PARAMETER_PAYER_NAME) ? iwc.getParameter(PARAMETER_PAYER_NAME) : "";
		double amount = Double.parseDouble(iwc.getParameter(PARAMETER_AMOUNT));

		String cardType = iwc.getParameter(PARAMETER_CARD_TYPE);
		String cardNumber = iwc.getParameter(PARAMETER_CARD_NUMBER);
		String expiresMonth = iwc.getParameter(PARAMETER_VALID_MONTH);
		String expiresYear = iwc.getParameter(PARAMETER_VALID_YEAR);

		boolean useDirectPayment = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_DIRECT_PAYMENT, false);
		boolean creditCardPayment = new Boolean(iwc.getParameter(PARAMETER_CREDITCARD_PAYMENT)).booleanValue();
		IWTimestamp paymentStamp = new IWTimestamp();
		String properties = null;
		if (creditCardPayment) {
			if (useDirectPayment) {
				String nameOnCard = iwc.getParameter(PARAMETER_NAME_ON_CARD);
				String ccVerifyNumber = iwc.getParameter(PARAMETER_CCV);
				String referenceNumber = paymentStamp.getDateString("yyyyMMddHHmmssSSSS");

				try {
					properties = getCourseBusiness(iwc).authorizePayment(nameOnCard, cardNumber, expiresMonth, expiresYear, ccVerifyNumber, amount, "ISK", referenceNumber);
				}
				catch (CreditCardAuthorizationException e) {
					setError("", e.getLocalizedMessage(iwrb));
				}
			}
			else {
				if (!iwc.isParameterSet(PARAMETER_CARD_NUMBER)) {
					setError(ACTION_PHASE_7, PARAMETER_CARD_NUMBER, this.iwrb.getLocalizedString("must_enter_card_number", "You must enter credit card number"));
				}
				else if (!validateCardNumber(iwc.getParameter(PARAMETER_CARD_NUMBER))) {
					setError(ACTION_PHASE_7, PARAMETER_CARD_NUMBER, this.iwrb.getLocalizedString("invalid_credit_card_number", "Invalid credit card number"));
				}

				IWTimestamp dateNow = new IWTimestamp();
				dateNow.setAsDate();

				IWTimestamp stamp = new IWTimestamp();
				stamp.setAsDate();
				stamp.setMonth(Integer.parseInt(iwc.getParameter(PARAMETER_VALID_MONTH)));
				stamp.setYear(Integer.parseInt(iwc.getParameter(PARAMETER_VALID_YEAR)));

				if (stamp.isEarlierThan(dateNow)) {
					setError(ACTION_PHASE_7, PARAMETER_VALID_MONTH, this.iwrb.getLocalizedString("invalid_credit_card_validity", "Invalid credit card validity"));
				}
			}
		}

		if (hasErrors(ACTION_PHASE_7)) {
			showPhaseSeven(iwc);
			return;
		}

		int merchantPK = Integer.parseInt(getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_MERCHANT_PK, "-1"));
		String merchantType = getIWApplicationContext().getApplicationSettings().getProperty(CourseConstants.PROPERTY_MERCHANT_TYPE);
		is.idega.idegaweb.egov.course.data.CourseApplication application = getCourseBusiness(iwc).saveApplication(
				getCourseApplicationSession(iwc).getApplications(),
				merchantPK,
				(float) amount,
				merchantType,
				creditCardPayment ? CourseConstants.PAYMENT_TYPE_CARD : CourseConstants.PAYMENT_TYPE_GIRO,
				null,
				payerName,
				payerPersonalID,
				prefix,
				getUser(iwc),
				iwc.isLoggedOn() ? iwc.getCurrentUser() : null,
				iwc.getCurrentLocale()
		);
		if (application != null && creditCardPayment) {
			if (useDirectPayment) {
				try {
					String authorizationCode = getCourseBusiness(iwc).finishPayment(properties);
					application.setReferenceNumber(authorizationCode);
					application.store();
				}
				catch (CreditCardAuthorizationException e) {
					setError("", e.getLocalizedMessage());
				}
			}
			else {
				application.setCardType(cardType);
				application.setCardNumber(cardNumber);
				application.setCardValidMonth(Integer.parseInt(expiresMonth));
				application.setCardValidYear(Integer.parseInt(expiresYear));
				application.store();
			}
		}

		if (application != null) {
			getCourseApplicationSession(iwc).clear(iwc);
			addPhasesReceipt(iwc, this.iwrb.getLocalizedString("application.receipt", "Application receipt"), this.iwrb.getLocalizedString("application.application_save_completed", "Application sent"), this.iwrb.getLocalizedString(getPrefixSafe() + "application.application_send_confirmation" + (hasCare ? "" : "_nocare"), "Your course application has been received and will be processed."), 8, getNumberOfPhases(iwc));

			Layer clearLayer = new Layer(Layer.DIV);
			clearLayer.setStyleClass("Clear");
			add(clearLayer);

			Layer bottom = new Layer(Layer.DIV);
			bottom.setStyleClass("bottom");
			add(bottom);

			try {
				ICPage page = getUserBusiness(iwc).getHomePageForUser(iwc.getCurrentUser());
				Link link = getButtonLink(this.iwrb.getLocalizedString("my_page", "My page"));
				link.setStyleClass("homeButton");
				link.setPage(page);
				bottom.add(link);
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}

			Link receipt = getButtonLink(this.iwrb.getLocalizedString("receipt", "Receipt"));
			receipt.setWindowToOpen(CourseApplicationOverviewWindow.class);
			receipt.addParameter(getCourseBusiness(iwc).getSelectedCaseParameter(), application.getPrimaryKey().toString());
			bottom.add(receipt);
		}
		else {
			add(getPhasesHeader(this.iwrb.getLocalizedString("application.submit_failed", "Application submit failed"), 7, 7));
			add(getStopLayer(this.iwrb.getLocalizedString("application.submit_failed", "Application submit failed"), this.iwrb.getLocalizedString("application.submit_failed_info", "Application submit failed")));
		}
	}

	private boolean addApplications(IWContext iwc) throws RemoteException {
		int action = ACTION_PHASE_1;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}

		String[] pks = iwc.getParameterValues(PARAMETER_COURSE);
		String[] daycare = iwc.getParameterValues(PARAMETER_DAYCARE);
		String[] pickedUp = iwc.getParameterValues(PARAMETER_TRIPHOME);
		if (pks != null) {
			for (int i = 0; i < pks.length; i++) {
				ApplicationHolder holder = new ApplicationHolder();
				Course course = getCourseBusiness(iwc).getCourse(new Integer(pks[i]));
				holder.setOnWaitingList(getCourseBusiness(iwc).isFull(course));
				holder.setCourse(course);
				holder.setUser(getApplicant(iwc));
				boolean addApplication = true;
				if (hasCare) {
					if (pickedUp[i].length() > 0) {
						holder.setDaycare(Integer.parseInt(daycare[i]));
					}
					else if (action != ACTION_PHASE_5) {
						setError(PARAMETER_TRIPHOME, iwrb.getLocalizedString("application_error.must_select_picked_up_option", "You must select all picked up options."));
						addApplication = false;
					}
					if (daycare[i].length() > 0) {
						holder.setPickedUp(new Boolean(pickedUp[i]));
					}
					else if (action != ACTION_PHASE_5) {
						setError(PARAMETER_TRIPHOME, iwrb.getLocalizedString("application_error.must_select_daycare_option", "You must select all daycare options."));
						addApplication = false;
					}
				}

				if (!addApplication) {
					return false;
				}
				getCourseApplicationSession(iwc).addApplication(getApplicant(iwc), holder);
			}
		}

		return true;
	}

	private void removeApplication(IWContext iwc) throws RemoteException {
		ApplicationHolder holder = new ApplicationHolder();
		holder.setUser(getApplicant(iwc));
		holder.setCourse(getCourseBusiness(iwc).getCourse(iwc.getParameter(PARAMETER_REMOVE_COURSE)));
		getCourseApplicationSession(iwc).removeApplication(getApplicant(iwc), holder);
	}

	private boolean isRequiredToSelectYesOrNoAboutChild(IWMainApplicationSettings settings) {
		return settings.getBoolean("cou_gd_yes_no_required_" + iApplicationPK + "_" + iSchoolTypePK, Boolean.FALSE);
	}

	private void addChildInformation(IWContext iwc, User user, Form form) throws RemoteException {
		Child child = getMemberFamilyLogic(iwc).getChild(user);
		User owner = getUser(iwc);

		IWMainApplicationSettings settings = iwc.getIWMainApplication().getSettings();
		boolean requiredYesNo = isRequiredToSelectYesOrNoAboutChild(settings);

		Heading1 heading = new Heading1(this.iwrb.getLocalizedString("child.has_growth_deviation", "Has growth deviation"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
		if (requiredYesNo) {
			heading.setStyleClass("required");
			if (hasError(PARAMETER_GROWTH_DEVIATION)) {
				heading.setStyleClass("hasError");
			}
		}
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("child.growth_deviation_help", "If the child has any growth deviations...")));
		section.add(helpLayer);

		RadioButton yes = new RadioButton(PARAMETER_GROWTH_DEVIATION, Boolean.TRUE.toString());
		yes.setStyleClass("radiobutton");
		yes.setToDisableOnClick(PARAMETER_GROWTH_DEVIATION_DETAILS, false);
		RadioButton no = new RadioButton(PARAMETER_GROWTH_DEVIATION, Boolean.FALSE.toString());
		no.setStyleClass("radiobutton");
		no.setToDisableOnClick(PARAMETER_GROWTH_DEVIATION_DETAILS, true);
		RadioButton noAnswer = null;
		if (settings.getBoolean("cou_growh_dev_no_ans_" + iApplicationPK + "_" + iSchoolTypePK, Boolean.TRUE)) {
			noAnswer = new RadioButton(PARAMETER_GROWTH_DEVIATION, "");
			noAnswer.setStyleClass("radiobutton");
			noAnswer.setToDisableOnClick(PARAMETER_GROWTH_DEVIATION_DETAILS, true);
		}
		Boolean hasGrowthDeviation = child.hasGrowthDeviation(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
		if (hasGrowthDeviation != null) {
			if (hasGrowthDeviation.booleanValue()) {
				yes.setSelected(true);
			}
			else {
				no.setSelected(true);
			}
		}
		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		if (requiredYesNo) {
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_GROWTH_DEVIATION)) {
				formItem.setStyleClass("hasError");
			}
		}
		Label label = new Label(this.iwrb.getLocalizedString("yes", "Yes"), yes);
		formItem.add(yes);
		formItem.add(label);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		if (requiredYesNo) {
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_GROWTH_DEVIATION)) {
				formItem.setStyleClass("hasError");
			}
		}
		label = new Label(this.iwrb.getLocalizedString("no", "No"), no);
		formItem.add(no);
		formItem.add(label);
		section.add(formItem);

		if (noAnswer != null) {
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("radioButtonItem");
			label = new Label(this.iwrb.getLocalizedString("no_answer", "Won't answer"), noAnswer);
			formItem.add(noAnswer);
			formItem.add(label);
			section.add(formItem);
		}

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		section.add(clearLayer);

		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(this.iwrb.getLocalizedString("details", "Details")));
		section.add(paragraph);

		TextArea details = new TextArea(PARAMETER_GROWTH_DEVIATION_DETAILS, child.getGrowthDeviationDetails(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey()));
		details.setStyleClass("details growth-deviation-details");
		details.setDisabled(!yes.getSelected());
		section.add(details);

		heading = new Heading1(this.iwrb.getLocalizedString("child.has_allergies", "Has allergies"));
		heading.setStyleClass("subHeader");
		if (requiredYesNo) {
			heading.setStyleClass("required");
			if (hasError(PARAMETER_ALLERGIES)) {
				heading.setStyleClass("hasError");
			}
		}
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("child.allergies_help", "If the child has any allergies...")));
		section.add(helpLayer);

		yes = new RadioButton(PARAMETER_ALLERGIES, Boolean.TRUE.toString());
		yes.setStyleClass("radiobutton");
		yes.setToDisableOnClick(PARAMETER_ALLERGIES_DETAILS, false);
		no = new RadioButton(PARAMETER_ALLERGIES, Boolean.FALSE.toString());
		no.setStyleClass("radiobutton");
		no.setToDisableOnClick(PARAMETER_ALLERGIES_DETAILS, true);

		noAnswer = null;
		if (settings.getBoolean("cou_aller_no_ans_" + iApplicationPK + "_" + iSchoolTypePK, Boolean.TRUE)) {
			noAnswer = new RadioButton(PARAMETER_ALLERGIES, "");
			noAnswer.setStyleClass("radiobutton");
			noAnswer.setToDisableOnClick(PARAMETER_ALLERGIES_DETAILS, true);
		}
		Boolean hasAllergies = child.hasAllergies(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
		if (hasAllergies != null) {
			if (hasAllergies.booleanValue()) {
				yes.setSelected(true);
			} else {
				no.setSelected(true);
			}
		}
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		if (requiredYesNo) {
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_ALLERGIES)) {
				formItem.setStyleClass("hasError");
			}
		}
		label = new Label(this.iwrb.getLocalizedString("yes", "Yes"), yes);
		formItem.add(yes);
		formItem.add(label);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("radioButtonItem");
		if (requiredYesNo) {
			formItem.setStyleClass("required");
			if (hasError(PARAMETER_ALLERGIES)) {
				formItem.setStyleClass("hasError");
			}
		}
		label = new Label(this.iwrb.getLocalizedString("no", "No"), no);
		formItem.add(no);
		formItem.add(label);
		section.add(formItem);

		if (noAnswer != null) {
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			formItem.setStyleClass("radioButtonItem");
			label = new Label(this.iwrb.getLocalizedString("no_answer", "Won't answer"), noAnswer);
			formItem.add(noAnswer);
			formItem.add(label);
			section.add(formItem);
		}

		clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		section.add(clearLayer);

		paragraph = new Paragraph();
		paragraph.add(new Text(this.iwrb.getLocalizedString("details", "Details")));
		section.add(paragraph);

		details = new TextArea(PARAMETER_ALLERGIES_DETAILS, child.getAllergiesDetails(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey()));
		details.setStyleClass("details allergies-details");
		details.setDisabled(!yes.getSelected());
		section.add(details);

		heading = new Heading1(this.iwrb.getLocalizedString(getPrefixSafe() + "child.other_information", "Other information"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(this.iwrb.getLocalizedString("child.other_information_help", "If there is anything else that you feel the school should know about the child, please fill in the details.")));
		section.add(helpLayer);

		details = new TextArea(PARAMETER_OTHER_INFORMATION, child.getOtherInformation(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey()));
		details.setStyleClass("details other-information-about-child");
		section.add(details);
	}

	private DropdownMenu getMaritalStatusDropdown(User user) {
		DropdownMenu maritalStatus = new DropdownMenu(PARAMETER_MARITAL_STATUS + (user != null ? "_" + user.getPrimaryKey().toString() : ""));
		maritalStatus.addMenuElement("", this.iwrb.getLocalizedString("select_marital_status", "Select marital status"));
		maritalStatus.addMenuElement(FamilyConstants.MARITAL_STATUS_MARRIED, this.iwrb.getLocalizedString("marital_status.married", "Married"));
		maritalStatus.addMenuElement(FamilyConstants.MARITAL_STATUS_SINGLE, this.iwrb.getLocalizedString("marital_status.single", "Single"));
		maritalStatus.keepStatusOnAction(true);

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
		relations.keepStatusOnAction(true);

		return relations;
	}

	private boolean saveCustodianInfo(IWContext iwc, boolean storeRelatives) throws RemoteException {
		String[] userPKs = storeRelatives ? iwc.getParameterValues(PARAMETER_RELATIVE) : iwc.getParameterValues(PARAMETER_USER);
		String[] homePhones = !storeRelatives ? iwc.getParameterValues(PARAMETER_HOME_PHONE) : iwc.getParameterValues(PARAMETER_RELATIVE_HOME_PHONE);
		String[] workPhones = !storeRelatives ? iwc.getParameterValues(PARAMETER_WORK_PHONE) : iwc.getParameterValues(PARAMETER_RELATIVE_WORK_PHONE);
		String[] mobilePhones = !storeRelatives ? iwc.getParameterValues(PARAMETER_MOBILE_PHONE) : iwc.getParameterValues(PARAMETER_RELATIVE_MOBILE_PHONE);
		String[] emails = !storeRelatives ? iwc.getParameterValues(PARAMETER_EMAIL) : iwc.getParameterValues(PARAMETER_RELATIVE_EMAIL);
		String[] relations = iwc.getParameterValues(storeRelatives ? PARAMETER_RELATIVE_RELATION : PARAMETER_RELATION);
		User owner = getUser(iwc);

		if (userPKs != null) {
			Child child = getMemberFamilyLogic(iwc).getChild(getApplicant(iwc));

			int index = 0;
			for (int a = 0; a < userPKs.length; a++) {
				String userPK = userPKs[a];
				if (userPK.length() > 0) {
					String relation = iwc.getParameter(PARAMETER_RELATION + "_" + userPK);
					String maritalStatus = iwc.getParameter(PARAMETER_MARITAL_STATUS + "_" + userPK);
					boolean storeMaritalStatus = false;// iwc.isParameterSet(PARAMETER_MARITAL_STATUS + "_" + userPK);

					if (storeRelatives) {
						if (userPK.length() > 0) {
							if (relations[a] == null || relations[a].length() == 0) {
								setError(PARAMETER_RELATIVE_RELATION, this.iwrb.getLocalizedString("must_select_relation", "You must select a relation to the child."));
								return false;
							}
							if (a == 0) {
								child.storeMainRelative(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey(), userPK, relations[a], homePhones[a], workPhones[a], mobilePhones[a], emails[a]);
							}
							else {
								child.storeRelative(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey(), userPK, relations[a], a + 1, homePhones[a], workPhones[a], mobilePhones[a], emails[a]);
							}
						}
					}
					else {
						Custodian custodian = getMemberFamilyLogic(iwc).getCustodian(getUserBusiness(iwc).getUser(new Integer(userPK)));
						User currentUser = getUser(iwc);
						boolean hasRelation = isSchoolAdministrator(iwc) || getMemberFamilyLogic(iwc).isRelatedTo(custodian, currentUser) || currentUser.getPrimaryKey().equals(custodian.getPrimaryKey());
						boolean isCustodian = getMemberFamilyLogic(iwc).isCustodianOf(custodian, child);

						if (relation == null || relation.length() == 0) {
							setError(PARAMETER_RELATION, this.iwrb.getLocalizedString("must_select_relation", "You must select a relation to the child."));
						}
						if (storeMaritalStatus && (maritalStatus == null || maritalStatus.length() == 0)) {
							setError(PARAMETER_MARITAL_STATUS, this.iwrb.getLocalizedString("application_error.marital_status_empty", "Please select marital status."));
						}
						if (homePhones != null && index < homePhones.length && (iUseSessionUser || hasRelation) && (homePhones[index] == null || homePhones[index].length() == 0)) {
							setError(PARAMETER_HOME_PHONE, this.iwrb.getLocalizedString("must_enter_home_phone", "You must enter a home phone for relative."));
						}
						if (hasErrors()) {
							return false;
						}
						if ((iUseSessionUser || hasRelation) && homePhones != null) {
							if (homePhones != null && homePhones.length > index) {
								custodian.setHomePhone(homePhones[index]);
							}
							if (workPhones != null && workPhones.length > index) {
								custodian.setWorkPhone(workPhones[index]);
							}
							if (mobilePhones != null && mobilePhones.length > index) {
								custodian.setMobilePhone(mobilePhones[index]);
							}
							if (emails != null && emails.length > index) {
								custodian.setEmail(emails[index]);
							}
						}
						if (storeMaritalStatus) {
							custodian.setMaritalStatus(maritalStatus);
						}
						custodian.store();

						if (isCustodian) {
							child.setRelation(custodian, relation);
							child.store();
						}
						else {
							child.setExtraCustodian(custodian, relation);
							child.store();
						}

						if (hasRelation) {
							index++;
						}
					}
				}
			}
		}
		return true;
	}

	private boolean saveChildInfo(IWContext iwc, User user) throws RemoteException {
		User owner = getUser(iwc);

		Boolean growthDeviation = iwc.isParameterSet(PARAMETER_GROWTH_DEVIATION) ? new Boolean(iwc.getParameter(PARAMETER_GROWTH_DEVIATION)) : null;
		Boolean allergies = iwc.isParameterSet(PARAMETER_ALLERGIES) ? new Boolean(iwc.getParameter(PARAMETER_ALLERGIES)) : null;

		String growthDeviationDetails = iwc.getParameter(PARAMETER_GROWTH_DEVIATION_DETAILS);
		String allergiesDetails = iwc.getParameter(PARAMETER_ALLERGIES_DETAILS);
		String otherInformation = iwc.getParameter(PARAMETER_OTHER_INFORMATION);

		Child child = getMemberFamilyLogic(iwc).getChild(user);
		child.setHasGrowthDeviation(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey(), growthDeviation);
		child.setGrowthDeviationDetails(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey(), growthDeviationDetails);
		child.setHasAllergies(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey(), allergies);
		child.setAllergiesDetails(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey(), allergiesDetails);
		child.setOtherInformation(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey(), otherInformation);
		child.store();

		return true;
	}

	private User getUser(IWContext iwc) throws RemoteException {
		if (this.iUseSessionUser) {
			return getUserSession(iwc).getUser();
		}
		else {
			return iwc.getCurrentUser();
		}
	}

	private UserSession getUserSession(IWUserContext iwuc) {
		try {
			return IBOLookup.getSessionInstance(iwuc, UserSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private FamilyLogic getMemberFamilyLogic(IWApplicationContext iwac) {
		try {
			return IBOLookup.getServiceInstance(iwac, FamilyLogic.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CourseBusiness getCourseBusiness(IWContext iwc) {
		try {
			return IBOLookup.getServiceInstance(iwc, CourseBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private CourseSession getCourseSession(IWContext iwc) {
		try {
			return IBOLookup.getSessionInstance(iwc, CourseSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	private CourseApplicationSession getCourseApplicationSession(IWContext iwc) {
		try {
			return IBOLookup.getSessionInstance(iwc, CourseApplicationSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected boolean isSchoolUser(IWContext iwc) {
		try {
			return getCourseSession(iwc).isSchoolProvider();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	protected boolean isSchoolAdministrator(IWContext iwc) {
		return isSchoolSuperAdministrator(iwc) || isSchoolUser(iwc);
	}

	protected boolean isSchoolSuperAdministrator(IWContext iwc) {
		return iwc.getAccessController().hasRole(CourseConstants.SUPER_ADMINISTRATOR_ROLE_KEY, iwc) || iwc.getAccessController().hasRole(CourseConstants.ADMINISTRATOR_ROLE_KEY, iwc) || isSchoolUser(iwc);
	}

	public void setUseSessionUser(boolean useSessionUser) {
		this.iUseSessionUser = useSessionUser;
	}

	private Object getSchoolTypePK() {
		return this.iSchoolTypePK;
	}

	public void setSchoolTypePK(String schoolTypePK) {
		this.iSchoolTypePK = schoolTypePK;
	}

	public void setApplicationPK(String applicationPK) {
		this.iApplicationPK = applicationPK;
	}

	private String phaseOneHelpPrefix = null;
	public String getPhaseOneHelpPrefix() {
		return phaseOneHelpPrefix;
	}

	public void setPhaseOneHelpPrefix(String phaseOneHelpPrefix) {
		this.phaseOneHelpPrefix = phaseOneHelpPrefix;
	}

	private String getPrefixSafe() {
		if (prefix.length() > 0) {
			return prefix + ".";
		}
		return prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isShowCreditCardPayment() {
		return showCreditCardPayment;
	}

	public void setShowCreditCardPayment(boolean showCreditCardPayment) {
		this.showCreditCardPayment = showCreditCardPayment;
	}
}