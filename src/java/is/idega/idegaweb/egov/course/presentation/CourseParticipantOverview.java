/*
 * $Id$ Created on Mar 30, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CourseType;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.user.business.UserSession;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

public class CourseParticipantOverview extends CourseBlock {

	private ICPage iChoicePage;

	public void present(IWContext iwc) {
		try {
			User participant = getUserSession(iwc).getUser();

			if (participant != null) {
				getViewerForm(iwc, participant);
			}
			else {
				add(new Text("No application found..."));
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void getViewerForm(IWContext iwc, User participant) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(getBusiness().getSelectedCaseParameter());

		form.add(getHeader(getResourceBundle().getLocalizedString("application.course_application_overview", "Course application overview")));

		form.add(getPersonInfo(iwc, participant, true));

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		int count = 1;
		Collection providers = getSession().getSchoolsForUser();
		Map applications = getBusiness().getApplicationMap(participant, providers);
		Iterator iterator = applications.keySet().iterator();
		while (iterator.hasNext()) {
			is.idega.idegaweb.egov.course.data.CourseApplication application = (is.idega.idegaweb.egov.course.data.CourseApplication) iterator.next();
			Collection choices = (Collection) applications.get(application);

			Heading1 heading = new Heading1(getResourceBundle().getLocalizedString("application.application_information", "Application information"));
			heading.setStyleClass("subHeader");
			if (count == 1) {
				heading.setStyleClass("topSubHeader");
			}
			form.add(heading);

			Layer section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);

			Layer formItem = new Layer(Layer.DIV);
			Label label = new Label();
			Layer span = new Layer();

			IWTimestamp created = new IWTimestamp(application.getCreated());

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label();
			label.add(new Text(getResourceBundle().getLocalizedString("application.created_date", "Created date")));
			span = new Layer(Layer.SPAN);
			span.add(new Text(created.getLocaleDateAndTime(iwc.getCurrentLocale(), IWTimestamp.SHORT, IWTimestamp.SHORT)));
			formItem.add(label);
			formItem.add(span);
			section.add(formItem);

			User payer = null;
			if (application.getPayerPersonalID() != null) {
				try {
					payer = getUserBusiness().getUser(application.getPayerPersonalID());
				}
				catch (FinderException e) {
					e.printStackTrace();
					payer = application.getOwner();
				}
				catch (RemoteException re) {
					throw new IBORuntimeException(re);
				}
			}
			else {
				payer = application.getOwner();
			}
			Name payerName = new Name(payer.getFirstName(), payer.getMiddleName(), payer.getLastName());

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label();
			label.add(new Text(getResourceBundle().getLocalizedString("application.payer_name", "Payer name")));
			span = new Layer(Layer.SPAN);
			span.add(new Text(payerName.getName(iwc.getCurrentLocale())));
			formItem.add(label);
			formItem.add(span);
			section.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label();
			label.add(new Text(getResourceBundle().getLocalizedString("application.payer_personal_id", "Payer personal ID")));
			span = new Layer(Layer.SPAN);
			span.add(new Text(PersonalIDFormatter.format(payer.getPersonalID(), iwc.getCurrentLocale())));
			formItem.add(label);
			formItem.add(span);
			section.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label();
			label.add(new Text(getResourceBundle().getLocalizedString("application.payment_type", "Payment type")));
			span = new Layer(Layer.SPAN);
			span.add(new Text(getResourceBundle().getLocalizedString("payment_type." + application.getPaymentType(), application.getPaymentType())));
			formItem.add(label);
			formItem.add(span);
			section.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label();
			label.add(new Text(getResourceBundle().getLocalizedString("application.is_paid", "Is paid")));
			span = new Layer(Layer.SPAN);
			span.add(new Text(getBooleanValue(iwc, application.isPaid())));
			formItem.add(label);
			formItem.add(span);
			section.add(formItem);

			section.add(clearLayer);

			Table2 table = new Table2();
			table.setStyleClass("choices");
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setBorder(0);

			TableRowGroup group = table.createHeaderRowGroup();
			TableRow row = group.createRow();

			TableCell2 cell = row.createHeaderCell();
			cell.setStyleClass("course");
			cell.add(new Text(getResourceBundle().getLocalizedString("course", "Course")));

			cell = row.createHeaderCell();
			cell.setStyleClass("provider");
			cell.add(new Text(getResourceBundle().getLocalizedString("provider", "Provider")));

			cell = row.createHeaderCell();
			cell.setStyleClass("courseType");
			cell.add(new Text(getResourceBundle().getLocalizedString("course_type", "Course type")));

			cell = row.createHeaderCell();
			cell.setStyleClass("timeframe");
			cell.add(new Text(getResourceBundle().getLocalizedString("timeframe_date", "Timeframe/Dates")));

			cell = row.createHeaderCell();
			cell.setStyleClass("days");
			cell.add(new Text(getResourceBundle().getLocalizedString("days", "Days")));

			group = table.createBodyRowGroup();

			int counter = 0;
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				row = group.createRow();
				CourseChoice choice = (CourseChoice) iter.next();

				Course course = choice.getCourse();
				School provider = course.getProvider();
				CourseType type = course.getCourseType();
				CoursePrice coursePrice = course.getPrice();
				IWTimestamp startDate = new IWTimestamp(course.getStartDate());
				IWTimestamp endDate = new IWTimestamp(coursePrice != null ? getBusiness().getEndDate(coursePrice, startDate.getDate()) : new IWTimestamp(course.getEndDate()).getDate());
				int numberOfDays = coursePrice != null ? coursePrice.getNumberOfDays() : 0;

				cell = row.createCell();
				cell.setStyleClass("course");
				if (getChoicePage() != null) {
					Link link = new Link(course.getName());
					link.addParameter(PARAMETER_CHOICE_PK, choice.getPrimaryKey().toString());
					link.setPage(getChoicePage());

					cell.add(link);
				}
				else {
					cell.add(new Text(course.getName()));
				}

				cell = row.createCell();
				cell.setStyleClass("provider");
				cell.add(new Text(provider.getName()));

				cell = row.createCell();
				cell.setStyleClass("courseType");
				cell.add(new Text(type.getName()));

				cell = row.createCell();
				cell.setStyleClass("timeframe");
				cell.add(new Text(startDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT) + " - " + endDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)));

				cell = row.createCell();
				cell.setStyleClass("days");
				if (numberOfDays > 0) {
					cell.add(new Text(String.valueOf(numberOfDays)));
				}
				else {
					cell.add(new Text("-"));
				}

				if (counter++ % 2 == 0) {
					row.setStyleClass("even");
				}
				else {
					row.setStyleClass("odd");
				}
			}

			section.add(table);
		}

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link home = getButtonLink(getResourceBundle().getLocalizedString("back", "Back"));
		home.setStyleClass("buttonHome");
		if (getResponsePage() != null) {
			home.setPage(getResponsePage());
		}
		bottom.add(home);

		add(form);
	}

	private UserSession getUserSession(IWUserContext iwuc) {
		try {
			return (UserSession) IBOLookup.getSessionInstance(iwuc, UserSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public ICPage getChoicePage() {
		return this.iChoicePage;
	}

	public void setChoicePage(ICPage choicePage) {
		this.iChoicePage = choicePage;
	}
}