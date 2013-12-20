/*
 * $Id$ Created on Aug 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.data.Child;
import is.idega.block.family.data.Custodian;
import is.idega.block.family.data.Relative;
import is.idega.idegaweb.egov.accounting.business.CitizenBusiness;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.business.CourseProviderBusiness;
import is.idega.idegaweb.egov.course.business.CourseSession;
import is.idega.idegaweb.egov.course.data.CourseProvider;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.PresentationUtil;
import com.idega.util.text.Name;

/**
 * Last modified: $Date$ by $Author$
 *
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision$
 */
public abstract class CourseBlock extends Block implements IWPageEventListener {

	public static final String ACTION_PRINT = "print";
	public static final String ACTION_CONFIRM_PRINT = "confirmPrint";
	public static final String ACTION_REMOVE_CERTIFICATE = "removeCertificate";

	public static final String PARAMETER_ACTION = "prm_action";
	public static final String PARAMETER_PRINT_CERTIFICATE = "prm_print_certificate";
	public static final String PARAMETER_PROVIDER_PK = "prm_provider_pk";
	public static final String PARAMETER_COURSE_PK = "prm_course_pk";
	public static final String PARAMETER_CHOICE_PK = "prm_choice_pk";
	public static final String PARAMETER_COURSE_TYPE_PK = "prm_course_type_pk";
	public static final String PARAMETER_COURSE_PARTICIPANT_PK = "prm_course_participant_pk";
	public static final String PARAMETER_SCHOOL_TYPE_PK = "prm_school_type_pk";
	public static final String PARAMETER_FROM_DATE = "prm_from_date";
	public static final String PARAMETER_TO_DATE = "prm_to_date";
	public static final String PARAMETER_YEAR = "prm_year";
	public static final String PARAMETER_CERTIFICATE_PK = "prm_certificate_pk";

	private CourseBusiness business;
	private CourseSession session;
	private CitizenBusiness uBusiness;

	private IWResourceBundle iwrb;
	private IWBundle iwb;

	private ICPage iResponsePage;
	private ICPage iBackPage;

	private boolean iHasErrors = false;
	private Map<String, String> iErrors;

	@Override
	public boolean actionPerformed(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_PROVIDER_PK)) {
			try {
				getSession(iwc).setProviderPK(new Integer(iwc.getParameter(PARAMETER_PROVIDER_PK)));
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		else {
			try {
				getSession(iwc).setProviderPK(null);
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return true;
	}

	@Override
	public void main(IWContext iwc) throws Exception {
		setBundle(getBundle(iwc));
		setResourceBundle(getResourceBundle(iwc));
		this.business = getBusiness(iwc);
		this.session = getSession(iwc);
		this.uBusiness = getUserBusiness(iwc);
		PresentationUtil.addStyleSheetToHeader(iwc, iwc.getIWMainApplication().getBundle("is.idega.idegaweb.egov.application").getVirtualPathWithFileNameString("style/application.css"));
		PresentationUtil.addStyleSheetToHeader(iwc, iwc.getIWMainApplication().getBundle("is.idega.idegaweb.egov.course").getVirtualPathWithFileNameString("style/course.css"));

		present(iwc);
	}

	protected IWResourceBundle getResourceBundle() {
		return this.iwrb;
	}

	private void setResourceBundle(IWResourceBundle iwrb) {
		this.iwrb = iwrb;
	}

	protected IWBundle getBundle() {
		return this.iwb;
	}

	private void setBundle(IWBundle iwb) {
		this.iwb = iwb;
	}

	protected String localize(String key, String defaultValue) {
		return getResourceBundle().getLocalizedString(key, defaultValue);
	}

	protected CourseBusiness getBusiness() {
		return this.business;
	}

	protected CourseSession getSession() {
		return this.session;
	}

	protected CitizenBusiness getUserBusiness() {
		return this.uBusiness;
	}

	private CourseBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return IBOLookup.getServiceInstance(iwac, CourseBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected CourseSession getSession(IWUserContext iwuc) {
		try {
			return IBOLookup.getSessionInstance(iwuc, CourseSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected FamilyLogic getFamilyLogic(IWApplicationContext iwac) {
		try {
			return IBOLookup.getServiceInstance(iwac, FamilyLogic.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected CitizenBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return IBOLookup.getServiceInstance(iwac, CitizenBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected CourseProviderBusiness getSchoolBusiness(IWApplicationContext iwac) {
		try {
			return IBOLookup.getServiceInstance(iwac, CourseProviderBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	@Override
	public String getBundleIdentifier() {
		return CourseConstants.IW_BUNDLE_IDENTIFIER;
	}

	public abstract void present(IWContext iwc);

	protected boolean isSchoolUser() {
		try {
			return getSession().isSchoolProvider();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	protected boolean isSchoolAdministrator(IWContext iwc) {
		return isSchoolSuperAdministrator(iwc) || isSchoolUser();
	}

	protected boolean isSchoolSuperAdministrator(IWContext iwc) {
		return iwc.getAccessController().hasRole(CourseConstants.SUPER_ADMINISTRATOR_ROLE_KEY, iwc) || iwc.getAccessController().hasRole(CourseConstants.ADMINISTRATOR_ROLE_KEY, iwc);
	}

	protected Layer getPersonInfo(IWContext iwc, User user, boolean showAddress) throws RemoteException {
		Layer layer = new Layer();
		layer.setID("personInfo");
		layer.setStyleClass("info");

		if (user != null) {
			Address address = getUserBusiness().getUsersMainAddress(user);
			PostalCode postal = null;
			if (address != null) {
				postal = address.getPostalCode();
			}

			Layer personInfo = new Layer(Layer.DIV);
			personInfo.setStyleClass("personInfo");
			personInfo.setID("name");
			personInfo.add(new Text(user.getName()));
			layer.add(personInfo);

			personInfo = new Layer(Layer.DIV);
			personInfo.setStyleClass("personInfo");
			personInfo.setID("personalID");
			personInfo.add(new Text(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())));
			layer.add(personInfo);

			personInfo = new Layer(Layer.DIV);
			personInfo.setStyleClass("personInfo");
			personInfo.setID("address");
			if (address != null && showAddress) {
				personInfo.add(new Text(address.getStreetAddress()));
			}
			layer.add(personInfo);

			personInfo = new Layer(Layer.DIV);
			personInfo.setStyleClass("personInfo");
			personInfo.setID("postal");
			if (postal != null && showAddress) {
				personInfo.add(new Text(postal.getPostalAddress()));
			}
			layer.add(personInfo);

			Layer clear = new Layer(Layer.DIV);
			clear.setStyleClass("Clear");
			layer.add(clear);
		}

		return layer;
	}

	protected DropdownMenu getProvidersDropdown(IWContext iwc) {
		try {
			IWResourceBundle iwrb = getResourceBundle(iwc);

			SelectorUtility util = new SelectorUtility();
			DropdownMenu providers = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_PROVIDER_PK), getBusiness(iwc).getProvidersForUser(iwc.getCurrentUser()), "getSchoolName");
			providers.addMenuElementFirst("", iwrb.getLocalizedString("select_provider", "Select provider"));
			if (getSession(iwc).getProvider() != null) {
				providers.setSelectedElement(getSession(iwc).getProvider().getPrimaryKey().toString());
			}

			return providers;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	protected DropdownMenu getAllProvidersDropdown(IWContext iwc) {
		try {
			IWResourceBundle iwrb = getResourceBundle(iwc);

			SelectorUtility util = new SelectorUtility();
			DropdownMenu providers = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(PARAMETER_PROVIDER_PK), getBusiness(iwc).getProviders(), "getSchoolName");
			providers.addMenuElementFirst("", iwrb.getLocalizedString("select_provider", "Select provider"));
			if (getSession(iwc).getProvider() != null) {
				providers.setSelectedElement(getSession(iwc).getProvider().getPrimaryKey().toString());
			}

			return providers;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	protected void addChildInformationOverview(IWContext iwc, Layer section, IWResourceBundle iwrb, User owner, Child child) throws RemoteException {
		Boolean hasGrowthDeviation = child.hasGrowthDeviation(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
		if (hasGrowthDeviation == null && isSchoolUser()) {
			hasGrowthDeviation = child.hasGrowthDeviation(CourseConstants.COURSE_PREFIX);
		}

		String growthDeviation = child.getGrowthDeviationDetails(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
		if (growthDeviation == null && isSchoolUser()) {
			growthDeviation = child.getGrowthDeviationDetails(CourseConstants.COURSE_PREFIX);
		}

		Boolean hasAllergies = child.hasAllergies(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
		if (hasAllergies == null && isSchoolUser()) {
			hasAllergies = child.hasAllergies(CourseConstants.COURSE_PREFIX);
		}

		String allergies = child.getAllergiesDetails(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
		if (allergies == null && isSchoolUser()) {
			allergies = child.getAllergiesDetails(CourseConstants.COURSE_PREFIX);
		}

		String otherInformation = child.getOtherInformation(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
		if (otherInformation == null && isSchoolUser()) {
			otherInformation = child.getOtherInformation(CourseConstants.COURSE_PREFIX);
		}

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("informationItem");
		Label label = new Label();
		label.add(new Text(iwrb.getLocalizedString("child.has_growth_deviation_overview", "Has growth deviation")));
		Layer span = new Layer(Layer.SPAN);

		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(getBooleanValue(iwc, hasGrowthDeviation)));
		span.add(paragraph);
		formItem.add(label);
		formItem.add(span);
		section.add(formItem);

		if (hasGrowthDeviation != null) {
			if (growthDeviation != null) {
				paragraph.add(new Text(", "));
				paragraph.add(growthDeviation);
			}
		}

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("informationItem");
		label = new Label();
		label.add(new Text(iwrb.getLocalizedString("child.has_allergies_overview", "Has allergies")));
		span = new Layer(Layer.SPAN);
		paragraph = new Paragraph();
		paragraph.add(new Text(getBooleanValue(iwc, hasAllergies)));
		span.add(paragraph);
		formItem.add(label);
		formItem.add(span);
		section.add(formItem);

		if (hasAllergies != null) {
			if (allergies != null) {
				paragraph.add(new Text(", "));
				paragraph.add(allergies);
			}
		}

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("informationItem");
		label = new Label();
		label.add(new Text(this.iwrb.getLocalizedString("child.other_information", "Other information")));
		span = new Layer(Layer.SPAN);
		paragraph = new Paragraph();
		paragraph.add(new Text(otherInformation));
		span.add(paragraph);
		formItem.add(label);
		formItem.add(span);
		section.add(formItem);
	}

	protected Layer getCustodians(IWContext iwc, IWResourceBundle iwrb, User owner, Child child, Collection<Custodian> custodians) throws RemoteException {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("formSection");

		Layer relation = new Layer(Layer.DIV);
		relation.setStyleClass("formItem");
		relation.setStyleClass("multiValueItem");
		Label label = new Label();
		label.add(iwrb.getLocalizedString("relation", "Relation"));
		relation.add(label);
		layer.add(relation);

		Layer name = new Layer(Layer.DIV);
		name.setStyleClass("formItem");
		name.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("name", "Name"));
		name.add(label);
		layer.add(name);

		Layer personalID = new Layer(Layer.DIV);
		personalID.setStyleClass("formItem");
		personalID.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("personal_id", "Personal ID"));
		personalID.add(label);
		layer.add(personalID);

		Layer address = new Layer(Layer.DIV);
		address.setStyleClass("formItem");
		address.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("address", "Address"));
		address.add(label);
		layer.add(address);

		Layer postal = new Layer(Layer.DIV);
		postal.setStyleClass("formItem");
		postal.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("zip_code", "Zip code"));
		postal.add(label);
		layer.add(postal);

		Layer homePhone = new Layer(Layer.DIV);
		homePhone.setStyleClass("formItem");
		homePhone.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("home_phone", "Home phone"));
		homePhone.add(label);
		layer.add(homePhone);

		Layer workPhone = new Layer(Layer.DIV);
		workPhone.setStyleClass("formItem");
		workPhone.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("work_phone", "Work phone"));
		workPhone.add(label);
		layer.add(workPhone);

		Layer mobile = new Layer(Layer.DIV);
		mobile.setStyleClass("formItem");
		mobile.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("mobile_phone", "Mobile phone"));
		mobile.add(label);
		layer.add(mobile);

		Layer email = new Layer(Layer.DIV);
		email.setStyleClass("formItem");
		email.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("email", "E-mail"));
		email.add(label);
		layer.add(email);

		/*
		 * Layer maritalStatus = new Layer(Layer.DIV); maritalStatus.setStyleClass("formItem"); maritalStatus.setStyleClass("multiValueItem"); label = new
		 * Label(); label.add(iwrb.getLocalizedString("marital_status", "Marital status")); maritalStatus.add(label); layer.add(maritalStatus);
		 */

		for (Iterator<Custodian> iter = custodians.iterator(); iter.hasNext();) {
			Custodian custodian = iter.next();
			boolean hasRelation = isSchoolAdministrator(iwc) || getFamilyLogic(iwc).isRelatedTo(custodian, owner) || owner.getPrimaryKey().equals(custodian.getPrimaryKey());

			Address userAddress = getUserBusiness(iwc).getUsersMainAddress(custodian);
			Phone userPhone = null;
			Phone userWork = null;
			Phone userMobile = null;
			Email userEmail = null;

			try {
				userPhone = getUserBusiness(iwc).getUsersHomePhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				userPhone = null;
			}

			try {
				userWork = getUserBusiness(iwc).getUsersWorkPhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				userWork = null;
			}

			try {
				userMobile = getUserBusiness(iwc).getUsersMobilePhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				userMobile = null;
			}

			try {
				userEmail = getUserBusiness(iwc).getUsersMainEmail(custodian);
			}
			catch (NoEmailFoundException nefe) {
				userEmail = null;
			}

			Layer span = new Layer(Layer.SPAN);
			span.add(new Text(iwrb.getLocalizedString("relation." + child.getRelation(custodian), "")));
			relation.add(span);

			Name custodianName = new Name(custodian.getFirstName(), custodian.getMiddleName(), custodian.getLastName());
			span = new Layer(Layer.SPAN);
			span.add(new Text(custodianName.getName(iwc.getCurrentLocale())));
			name.add(span);

			span = new Layer(Layer.SPAN);
			span.add(new Text(PersonalIDFormatter.format(custodian.getPersonalID(), iwc.getCurrentLocale())));
			personalID.add(span);

			span = new Layer(Layer.SPAN);
			span.add(new Text(userAddress.getStreetAddress()));
			address.add(span);

			span = new Layer(Layer.SPAN);
			span.add(new Text(userAddress.getPostalAddress()));
			postal.add(span);

			span = new Layer(Layer.SPAN);
			if (userPhone != null && userPhone.getNumber() != null && hasRelation) {
				span.add(new Text(userPhone.getNumber()));
			}
			else {
				span.add(Text.getNonBrakingSpace());
			}
			homePhone.add(span);

			span = new Layer(Layer.SPAN);
			if (userWork != null && userWork.getNumber() != null && hasRelation) {
				span.add(new Text(userWork.getNumber()));
			}
			else {
				span.add(Text.getNonBrakingSpace());
			}
			workPhone.add(span);

			span = new Layer(Layer.SPAN);
			if (userMobile != null && userMobile.getNumber() != null && hasRelation) {
				span.add(new Text(userMobile.getNumber()));
			}
			else {
				span.add(Text.getNonBrakingSpace());
			}
			mobile.add(span);

			span = new Layer(Layer.SPAN);
			if (userEmail != null && userEmail.getEmailAddress() != null && hasRelation) {
				span.add(new Text(userEmail.getEmailAddress()));
			}
			else {
				span.add(Text.getNonBrakingSpace());
			}
			email.add(span);

			/*
			 * span = new Layer(Layer.SPAN); if (custodian.getMaritalStatus() != null) { span.add(new Text(iwrb.getLocalizedString("marital_status." +
			 * custodian.getMaritalStatus()))); } else { span.add(Text.getNonBrakingSpace()); } maritalStatus.add(span);
			 */
		}

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		layer.add(clearLayer);

		return layer;
	}

	protected Layer getRelatives(IWContext iwc, IWResourceBundle iwrb, Collection<Relative> custodians) {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("formSection");

		Layer relation = new Layer(Layer.DIV);
		relation.setStyleClass("formItem");
		relation.setStyleClass("multiValueItem");
		Label label = new Label();
		label.add(iwrb.getLocalizedString("relation", "Relation"));
		relation.add(label);
		layer.add(relation);

		Layer name = new Layer(Layer.DIV);
		name.setStyleClass("formItem");
		name.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("name", "Name"));
		name.add(label);
		layer.add(name);

		Layer homePhone = new Layer(Layer.DIV);
		homePhone.setStyleClass("formItem");
		homePhone.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("home_phone", "Home phone"));
		homePhone.add(label);
		layer.add(homePhone);

		Layer workPhone = new Layer(Layer.DIV);
		workPhone.setStyleClass("formItem");
		workPhone.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("work_phone", "Work phone"));
		workPhone.add(label);
		layer.add(workPhone);

		Layer mobile = new Layer(Layer.DIV);
		mobile.setStyleClass("formItem");
		mobile.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("mobile_phone", "Mobile phone"));
		mobile.add(label);
		layer.add(mobile);

		Layer email = new Layer(Layer.DIV);
		email.setStyleClass("formItem");
		email.setStyleClass("multiValueItem");
		label = new Label();
		label.add(iwrb.getLocalizedString("email", "E-mail"));
		email.add(label);
		layer.add(email);

		for (Iterator<Relative> iter = custodians.iterator(); iter.hasNext();) {
			Relative relative = iter.next();

			Layer span = new Layer(Layer.SPAN);
			span.add(new Text(iwrb.getLocalizedString("relation." + relative.getRelation(), "")));
			relation.add(span);

			span = new Layer(Layer.SPAN);
			span.add(new Text(relative.getName()));
			name.add(span);

			span = new Layer(Layer.SPAN);
			if (relative.getHomePhone() != null) {
				span.add(new Text(relative.getHomePhone()));
			}
			else {
				span.add(Text.getNonBrakingSpace());
			}
			homePhone.add(span);

			span = new Layer(Layer.SPAN);
			if (relative.getWorkPhone() != null) {
				span.add(new Text(relative.getWorkPhone()));
			}
			else {
				span.add(Text.getNonBrakingSpace());
			}
			workPhone.add(span);

			span = new Layer(Layer.SPAN);
			if (relative.getMobilePhone() != null) {
				span.add(new Text(relative.getMobilePhone()));
			}
			else {
				span.add(Text.getNonBrakingSpace());
			}
			mobile.add(span);

			span = new Layer(Layer.SPAN);
			if (relative.getEmail() != null) {
				span.add(new Text(relative.getEmail()));
			}
			else {
				span.add(Text.getNonBrakingSpace());
			}
			email.add(span);
		}

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		layer.add(clearLayer);

		return layer;
	}

	private Layer getPhases(int phase, int totalPhases) {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("phases");

		Lists list = new Lists();
		for (int a = 1; a <= totalPhases; a++) {
			ListItem item = new ListItem();
			item.add(new Text(String.valueOf(a)));
			if (a == phase) {
				item.setStyleClass("current");
			}

			list.add(item);
		}
		layer.add(list);

		return layer;
	}

	protected Layer getHeader(String text) {
		return getPhasesHeader(text, -1, -1);
	}

	protected Layer getPhasesHeader(String text, int phase, int totalPhases) {
		return getPhasesHeader(text, phase, totalPhases, true);
	}

	protected Layer getPhasesHeader(String text, int phase, int totalPhases, boolean showNumberInText) {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("header");

		if (phase != -1) {
			Heading1 heading = new Heading1((showNumberInText ? (String.valueOf(phase) + ". ") : "") + text);
			layer.add(heading);
			layer.add(getPhases(phase, totalPhases));
		}
		else {
			Heading1 heading = new Heading1(text);
			layer.add(heading);
		}

		return layer;
	}

	protected String getBooleanValue(IWContext iwc, boolean booleanValue) {
		return getBooleanValue(iwc, new Boolean(booleanValue));
	}

	protected String getBooleanValue(IWContext iwc, Boolean booleanValue) {
		if (this.iwrb == null) {
			this.iwrb = getResourceBundle(iwc);
		}

		if (booleanValue == null) {
			return this.iwrb.getLocalizedString("no_answer", "Won't answer");
		}
		else if (booleanValue.booleanValue()) {
			return this.iwrb.getLocalizedString("yes", "Yes");
		}
		else {
			return this.iwrb.getLocalizedString("no", "No");
		}
	}

	protected Link getButtonLink(String text) {
		Layer all = new Layer(Layer.SPAN);
		all.setStyleClass("buttonSpan");

		Layer left = new Layer(Layer.SPAN);
		left.setStyleClass("left");
		all.add(left);

		Layer middle = new Layer(Layer.SPAN);
		middle.setStyleClass("middle");
		middle.add(new Text(text));
		all.add(middle);

		Layer right = new Layer(Layer.SPAN);
		right.setStyleClass("right");
		all.add(right);

		Link link = new Link(all);
		link.setStyleClass("button");

		return link;
	}

	protected void setError(String parameter, String error) {
		if (this.iErrors == null) {
			this.iErrors = new HashMap<String, String>();
		}

		this.iHasErrors = true;
		this.iErrors.put(parameter, error);
	}

	protected boolean hasErrors() {
		return this.iHasErrors;
	}

	protected boolean hasError(String parameter) {
		if (hasErrors()) {
			return this.iErrors.containsKey(parameter);
		}
		return false;
	}

	/**
	 * Adds the errors encountered
	 *
	 * @param iwc
	 * @param errors
	 */
	protected void addErrors(IWContext iwc, UIComponent parent) {
		if (this.iHasErrors) {
			Layer layer = new Layer(Layer.DIV);
			layer.setStyleClass("errorLayer");

			Layer image = new Layer(Layer.DIV);
			image.setStyleClass("errorImage");
			layer.add(image);

			Heading1 heading = new Heading1(getResourceBundle(iwc).getLocalizedString("application_errors_occured", "There was a problem with the following items"));
			layer.add(heading);

			Lists list = new Lists();
			layer.add(list);

			for (Iterator<String> iter = this.iErrors.values().iterator(); iter.hasNext();) {
				String element = iter.next();
				ListItem item = new ListItem();
				item.add(new Text(element));

				list.add(item);
			}

			parent.getChildren().add(layer);
		}
	}

	protected ICPage getResponsePage() {
		return this.iResponsePage;
	}

	public void setResponsePage(ICPage responsePage) {
		this.iResponsePage = responsePage;
	}

	protected ICPage getBackPage() {
		return this.iBackPage;
	}

	public void setBackPage(ICPage responsePage) {
		this.iBackPage = responsePage;
	}

	protected <P extends CourseProvider> Collection<P> getHandledCourseProviders(User user) {
		return getBusiness().getHandledCourseProviders(user);
	}

}