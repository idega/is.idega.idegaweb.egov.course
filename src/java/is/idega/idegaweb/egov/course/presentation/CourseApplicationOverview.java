/*
 * $Id$ Created on Mar 30, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.data.PriceHolder;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;

import javax.ejb.FinderException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.school.data.School;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Span;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.PresentationUtil;
import com.idega.util.text.Name;
import com.idega.util.text.TextSoap;

public class CourseApplicationOverview extends CourseBlock {

	private static final String PARAMETER_CARD_NUMBER = "prm_card_number";
	private static final String PARAMETER_VALID_MONTH = "prm_valid_month";
	private static final String PARAMETER_VALID_YEAR = "prm_valid_year";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	private static final String PARAMETER_CCV = "prm_ccv";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_REFUND_FORM = 2;
	private static final int ACTION_REFUND = 3;

	private ICPage iChoicePage;
	private boolean useInWindow = false;

	public CourseApplicationOverview() {
		this(false);
	}

	public CourseApplicationOverview(boolean useInWindow) {
		this.useInWindow = useInWindow;
	}

	@Override
	public void present(IWContext iwc) {
		PresentationUtil.addStyleSheetToHeader(iwc, getBundle(iwc).getVirtualPathWithFileNameString("style/course.css"));

		try {
			is.idega.idegaweb.egov.course.data.CourseApplication application = null;
			if (iwc.isParameterSet(getBusiness().getSelectedCaseParameter())) {
				application = getBusiness().getCourseApplication(iwc.getParameter(getBusiness().getSelectedCaseParameter()));
			}

			if (application != null) {
				switch (parseAction(iwc)) {
					case ACTION_VIEW:
						getViewerForm(iwc, application);
						break;

					case ACTION_REFUND_FORM:
						getRefundForm(iwc, application);
						break;

					case ACTION_REFUND:
						boolean refund = refund(iwc, application);
						if (!refund) {
							getRefundForm(iwc, application);
						}
						break;
				}
			}
			else {
				add(new Text("No application found..."));
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_VIEW;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}

		return action;
	}

	private void getViewerForm(IWContext iwc, is.idega.idegaweb.egov.course.data.CourseApplication application) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(getBusiness().getSelectedCaseParameter());
		form.addParameter(PARAMETER_ACTION, ACTION_VIEW);

		form.add(getHeader(getResourceBundle().getLocalizedString("application.course_application_overview", "Course application overview")));

		form.add(getPersonInfo(iwc, null, false));

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		Heading1 heading = new Heading1(getResourceBundle().getLocalizedString("application.application_information", "Application information"));
		heading.setStyleClass("subHeader");
		heading.setStyleClass("topSubHeader");
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
		
		boolean useFixedPrices = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_FIXED_PRICES, false);

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

		section.add(clearLayer);

		Map applications = getBusiness().getApplicationMap(application);
		SortedSet prices = getBusiness().calculatePrices(applications);
		Map discounts = getBusiness().getDiscounts(prices, applications);
		float certificateFees = getBusiness().getCalculatedCourseCertificateFees(applications);

		NumberFormat format = NumberFormat.getInstance(iwc.getCurrentLocale());
		float totalPrice = certificateFees;
		float discount = 0;

		Iterator iterator = prices.iterator();
		while (iterator.hasNext()) {
			PriceHolder holder = (PriceHolder) iterator.next();
			User user = holder.getUser();
			Collection userApplications = (Collection) applications.get(user);
			PriceHolder discountHolder = (PriceHolder) discounts.get(user);

			float price = holder.getPrice();
			totalPrice += price;
			discount += discountHolder.getPrice();

			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());

			heading = new Heading1(name.getName(iwc.getCurrentLocale()) + " - " + PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale()));
			heading.setStyleClass("subHeader");
			form.add(heading);

			section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			form.add(section);

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

			if (!useFixedPrices) {
				cell = row.createHeaderCell();
				cell.setStyleClass("days");
				cell.add(new Text(getResourceBundle().getLocalizedString("days", "Days")));
			}
			
			cell = row.createHeaderCell();
			cell.setStyleClass("certificateFee");
			cell.add(new Text(getResourceBundle().getLocalizedString("certificate_fee", "Certificate fee")));

			cell = row.createHeaderCell();
			cell.setStyleClass("amount");
			cell.add(new Text(getResourceBundle().getLocalizedString("amount", "Amount")));

			group = table.createBodyRowGroup();

			int counter = 0;
			Iterator iter = userApplications.iterator();
			while (iter.hasNext()) {
				row = group.createRow();
				ApplicationHolder appHolder = (ApplicationHolder) iter.next();

				Course course = appHolder.getCourse();
				School provider = course.getProvider();
				CourseType type = course.getCourseType();
				CoursePrice coursePrice = course.getPrice();
				IWTimestamp startDate = new IWTimestamp(course.getStartDate());
				IWTimestamp endDate = course.getCoursePrice() > 0 ? new IWTimestamp(course.getEndDate()) : new IWTimestamp(getBusiness().getEndDate(coursePrice, startDate.getDate()));

				if (course.getCourseCost() > 0) {
					certificateFees = course.getCourseCost();
					totalPrice += certificateFees;
				}
				
				cell = row.createCell();
				cell.setStyleClass("course");
				if (getChoicePage() != null && !useInWindow) {
					Link link = new Link(course.getName());
					link.addParameter(PARAMETER_CHOICE_PK, appHolder.getChoicePK().toString());
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

				if (!useFixedPrices) {
					cell = row.createCell();
					cell.setStyleClass("days");
					if (course.getCoursePrice() > 0) {
						cell.add(new Text(String.valueOf(IWTimestamp.getDaysBetween(startDate, endDate) + 1)));
					}
					else {
						cell.add(new Text(String.valueOf(coursePrice.getNumberOfDays())));
					}
				}
				
				cell = row.createCell();
				cell.setStyleClass("certificateFee");
				cell.add(new Text(format.format(certificateFees)));

				cell = row.createCell();
				cell.setStyleClass("amount");
				cell.add(new Text(format.format(appHolder.getPrice())));

				if (counter++ % 2 == 0) {
					row.setStyleClass("even");
				}
				else {
					row.setStyleClass("odd");
				}
			}

			group = table.createFooterRowGroup();
			row = group.createRow();

			cell = row.createCell();
			cell.setColumnSpan(6);
			cell.setStyleClass("totalPrice");
			cell.add(new Text(getResourceBundle().getLocalizedString("total_amount", "Total amount")));

			cell = row.createCell();
			cell.setStyleClass("totalPrice");
			cell.setStyleClass("price");
			cell.add(new Text(format.format(totalPrice)));

			section.add(table);
		}

		heading = new Heading1(getResourceBundle().getLocalizedString("application.application_information", "Application information"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label();
		label.add(new Text(getResourceBundle().getLocalizedString("application.total_price", "Total price")));
		span = new Layer(Layer.SPAN);
		span.add(new Text(format.format(totalPrice)));
		formItem.add(label);
		formItem.add(span);
		section.add(formItem);

		if (discount > 0) {
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label();
			label.add(new Text(getResourceBundle().getLocalizedString("application.payment_type", "Payment type")));
			span = new Layer(Layer.SPAN);
			span.add(new Text(format.format(discount)));
			formItem.add(label);
			formItem.add(span);
			section.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label();
			label.add(new Text(getResourceBundle().getLocalizedString("application.amount_due", "Amount due")));
			span = new Layer(Layer.SPAN);
			span.add(new Text(format.format(totalPrice - discount)));
			formItem.add(label);
			formItem.add(span);
			section.add(formItem);
		}

		section.add(clearLayer);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		if (!useInWindow) {
			Link home = getButtonLink(getResourceBundle().getLocalizedString("back", "Back"));
			home.setStyleClass("buttonHome");
			if (getResponsePage() != null) {
				home.setPage(getResponsePage());
			}
			bottom.add(home);

			Link receipt = getButtonLink(getResourceBundle().getLocalizedString("receipt", "Receipt"));
			receipt.setWindowToOpen(CourseApplicationOverviewWindow.class);
			receipt.addParameter(getBusiness().getSelectedCaseParameter(), application.getPrimaryKey().toString());
			bottom.add(receipt);

			if (isSchoolAdministrator(iwc) /*&& getBusiness().canInvalidate(application)*/) {
				Link invalidate = getButtonLink(getResourceBundle().getLocalizedString("invalidate", "Invalidate"));
				if (application.getPaymentType().equals(CourseConstants.PAYMENT_TYPE_CARD)) {
					invalidate.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_REFUND_FORM));
					invalidate.setToFormSubmit(form);
				}
				else {
					invalidate.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_REFUND));
					invalidate.maintainParameter(getBusiness().getSelectedCaseParameter(), iwc);
					invalidate.setClickConfirmation(getResourceBundle().getLocalizedString("confirm_invalidation", "Are you sure you want to invalidate this registration?"));
				}
				bottom.add(invalidate);
			}
		}
		else {
			Link home = getButtonLink(getResourceBundle().getLocalizedString("close", "Close"));
			home.setStyleClass("buttonHome");
			home.setAsCloseLink();
			bottom.add(home);

			Link print = getButtonLink(getResourceBundle().getLocalizedString("print", "Print"));
			print.setAsPrintLink();
			bottom.add(print);
		}

		add(form);
	}

	private void getRefundForm(IWContext iwc, is.idega.idegaweb.egov.course.data.CourseApplication application) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(getBusiness().getSelectedCaseParameter());
		form.addParameter(PARAMETER_ACTION, ACTION_REFUND);

		addErrors(iwc, form);

		form.add(getHeader(getResourceBundle().getLocalizedString("application.course_choice_overview", "Course choice overview")));

		form.add(getPersonInfo(iwc, null, false));

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");

		Heading1 heading = new Heading1(getResourceBundle().getLocalizedString("application.creditcard_information", "Creditcard information"));
		heading.setStyleClass("subHeader");
		form.add(heading);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(getResourceBundle().getLocalizedString("course_choice.creditcard_information_help", "If you have selected to pay by creditcard, please fill in the creditcard information.  All the fields are required.")));
		section.add(helpLayer);

		TextInput cardNumber = new TextInput(PARAMETER_CARD_NUMBER, null);
		cardNumber.setLength(16);
		cardNumber.setMaxlength(16);
		cardNumber.keepStatusOnAction(true);

		TextInput ccNumber = new TextInput(PARAMETER_CCV, null);
		ccNumber.setLength(3);
		ccNumber.setMaxlength(3);
		ccNumber.keepStatusOnAction(true);

		Map applications = getBusiness().getApplicationMap(application);
		SortedSet prices = getBusiness().calculatePrices(applications);
		Map discounts = getBusiness().getDiscounts(prices, applications);
		float certificateFees = getBusiness().getCalculatedCourseCertificateFees(applications);

		float totalPrice = certificateFees;
		float discount = 0;
		Iterator iterator = prices.iterator();
		while (iterator.hasNext()) {
			PriceHolder holder = (PriceHolder) iterator.next();
			User user = holder.getUser();
			PriceHolder discountHolder = (PriceHolder) discounts.get(user);

			float price = holder.getPrice();
			totalPrice += price;
			discount += discountHolder.getPrice();
		}
		float amount = totalPrice - discount;

		TextInput amountInput = new TextInput("hidden_amount", Float.toString(amount));
		amountInput.setDisabled(true);
		HiddenInput refundAmount = new HiddenInput(PARAMETER_AMOUNT, Float.toString(amount));

		DropdownMenu validMonth = new DropdownMenu(PARAMETER_VALID_MONTH);
		validMonth.setWidth("45px");
		validMonth.keepStatusOnAction(true);
		for (int a = 1; a <= 12; a++) {
			validMonth.addMenuElement(TextSoap.addZero(a), TextSoap.addZero(a));
		}

		IWTimestamp stamp = new IWTimestamp();
		DropdownMenu validYear = new DropdownMenu(PARAMETER_VALID_YEAR);
		validYear.setWidth("60px");
		validYear.keepStatusOnAction(true);
		int year = stamp.getYear();
		for (int a = year; a <= year + 10; a++) {
			validYear.addMenuElement(String.valueOf(stamp.getYear()).substring(2), String.valueOf(stamp.getYear()));
			stamp.addYears(1);
		}

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		if (hasError(PARAMETER_CARD_NUMBER)) {
			formItem.setStyleClass("hasError");
		}
		Label label = new Label(new Span(new Text(getResourceBundle().getLocalizedString("application.card_number", "Card number"))), cardNumber);
		formItem.add(label);
		formItem.add(cardNumber);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		label = new Label(new Span(new Text(getResourceBundle().getLocalizedString("application.ccv_number", "Credit card verification number"))), ccNumber);
		formItem.add(label);
		formItem.add(ccNumber);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		label = new Label(new Span(new Text(getResourceBundle().getLocalizedString("application.card_valid_time", "Card valid through"))), validMonth);
		formItem.add(label);
		formItem.add(validMonth);
		formItem.add(validYear);
		section.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(new Text(getResourceBundle().getLocalizedString("application.amount", "amount")), amountInput);
		formItem.add(label);
		formItem.add(amountInput);
		formItem.add(refundAmount);
		section.add(formItem);

		section.add(clearLayer);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		form.add(bottom);

		Link back = getButtonLink(getResourceBundle().getLocalizedString("back", "Back"));
		back.setStyleClass("buttonHome");
		back.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));
		back.setToFormSubmit(form);
		bottom.add(back);

		Link submit = getButtonLink(getResourceBundle().getLocalizedString("refund", "Refund"));
		submit.setToFormSubmit(form);
		bottom.add(submit);

		add(form);
	}

	private boolean refund(IWContext iwc, is.idega.idegaweb.egov.course.data.CourseApplication application) throws RemoteException {
		if (application.getPaymentType().equals(CourseConstants.PAYMENT_TYPE_CARD)) {
			String cardNumber = iwc.getParameter(PARAMETER_CARD_NUMBER);
			String expiresMonth = iwc.getParameter(PARAMETER_VALID_MONTH);
			String expiresYear = iwc.getParameter(PARAMETER_VALID_YEAR);
			String ccVerifyNumber = iwc.getParameter(PARAMETER_CCV);
			double amount = Double.parseDouble(iwc.getParameter(PARAMETER_AMOUNT));

			try {
				getBusiness().refundPayment(application, cardNumber, expiresMonth, expiresYear, ccVerifyNumber, amount);
			}
			catch (CreditCardAuthorizationException e) {
				setError("", e.getLocalizedMessage());
				return false;
			}
		}
		else {
			getBusiness().sendRefundMessage(application, null, iwc.getCurrentLocale());
		}

		getBusiness().invalidateApplication(application, iwc.getCurrentUser(), iwc.getCurrentLocale());

		String subject = localize("course_application.invalidated_receipt_subject", "Application invalidated");
		String body = "";
		if (application.getPaymentType().equals(CourseConstants.PAYMENT_TYPE_CARD)) {
			body = localize("course_application.invalidated_receipt_card_body", "The course application has been invalidated and refunded to the card you supplied.");
		}
		else if (application.getPaymentType().equals(CourseConstants.PAYMENT_TYPE_GIRO)) {
			body = localize("course_application.invalidated_receipt_giro_body", "The course application has been invalidated.  If the application was already paid for a message has been sent to the finance administration that will refund the payer.");
		}

		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("receipt");
		add(layer);

		Layer image = new Layer(Layer.DIV);
		image.setStyleClass("receiptImage");
		layer.add(image);

		Heading1 heading = new Heading1(subject);
		layer.add(heading);

		Paragraph paragraph = new Paragraph();
		paragraph.add(new Text(body));
		layer.add(paragraph);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		add(clearLayer);

		Layer bottom = new Layer(Layer.DIV);
		bottom.setStyleClass("bottom");
		add(bottom);

		ICPage backPage = null;
		if (getBackPage() != null) {
			backPage = getBackPage();
		}
		else {
			try {
				backPage = getUserBusiness(iwc).getHomePageForUser(iwc.getCurrentUser());
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}

		Link link = getButtonLink(localize("back", "Back"));
		link.setStyleClass("homeButton");
		link.setPage(backPage);
		bottom.add(link);

		return true;
	}

	public ICPage getChoicePage() {
		return this.iChoicePage;
	}

	public void setChoicePage(ICPage choicePage) {
		this.iChoicePage = choicePage;
	}
}