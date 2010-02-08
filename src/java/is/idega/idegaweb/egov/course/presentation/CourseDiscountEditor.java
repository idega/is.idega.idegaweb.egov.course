package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.data.CourseDiscount;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.IWDatePicker;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;

public class CourseDiscountEditor extends CourseBlock {

	private static final String PARAMETER_COURSE_DISCOUNT_ID = "prm_course_discount";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_TYPE = "prm_type";
	private static final String PARAMETER_VALID_FROM = "prm_valid_from";
	private static final String PARAMETER_VALID_TO = "prm_valid_to";
	private static final String PARAMETER_DISCOUNT = "prm_discount";

	private static final String PARAMETER_FROM = "prm_from";
	private static final String PARAMETER_TO = "prm_to";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	private static final int ACTION_DELETE = 5;

	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
				case ACTION_VIEW:
					showList(iwc);
					break;

				case ACTION_EDIT:
					Object typePK = iwc.getParameter(PARAMETER_COURSE_DISCOUNT_ID);
					showEditor(iwc, typePK);
					break;

				case ACTION_NEW:
					showEditor(iwc, null);
					break;

				case ACTION_SAVE:
					savePrice(iwc);
					showList(iwc);
					break;

				case ACTION_DELETE:
					if (!getCourseBusiness(iwc).deleteCourseDiscount(iwc.getParameter(PARAMETER_COURSE_DISCOUNT_ID))) {
						PresentationUtil.addJavascriptAlertOnLoad(iwc, getResourceBundle().getLocalizedString("course_discount.remove_error", "You can not remove a course discount that has courses attached to it."));
					}
					showList(iwc);
					break;
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}

	private void savePrice(IWContext iwc) {
		String pk = iwc.getParameter(PARAMETER_COURSE_DISCOUNT_ID);
		String name = iwc.getParameter(PARAMETER_NAME);
		String type = iwc.getParameter(PARAMETER_TYPE);
		String from = iwc.getParameter(PARAMETER_VALID_FROM);
		String to = iwc.getParameter(PARAMETER_VALID_TO);
		float discount = iwc.isParameterSet(PARAMETER_DISCOUNT) ? Float.parseFloat(iwc.getParameter(PARAMETER_DISCOUNT)) : 0;

		if (name != null && !"".equals(name.trim())) {
			try {
				getCourseBusiness(iwc).storeCourseDiscount(pk, name, type, new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(from)).getTimestamp(), new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(to)).getTimestamp(), discount);
			}
			catch (CreateException ce) {
				add(ce.getMessage());
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
	}

	public void showList(IWContext iwc) {
		Form form = new Form();
		form.setStyleClass("adminForm");

		try {
			form.add(getNavigation(iwc));
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

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

		Collection coursePrices = null;
		try {
			Date fromDate = iwc.isParameterSet(PARAMETER_FROM) ? new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(iwc.getParameter(PARAMETER_FROM))).getDate() : null;
			Date toDate = iwc.isParameterSet(PARAMETER_TO) ? new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(iwc.getParameter(PARAMETER_TO))).getDate() : null;
			coursePrices = getCourseBusiness(iwc).getCourseDiscounts(fromDate, toDate);
		}
		catch (RemoteException rex) {
			coursePrices = new ArrayList();
		}

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setId("name");
		cell.add(new Text(localize("name", "Name")));

		cell = row.createHeaderCell();
		cell.setId("type");
		cell.add(new Text(localize("discount_type", "Type")));

		cell = row.createHeaderCell();
		cell.setId("valid_from");
		cell.add(new Text(localize("valid_from", "Valid from")));

		cell = row.createHeaderCell();
		cell.setId("valid_to");
		cell.add(new Text(localize("valid_to", "Valid to")));

		cell = row.createHeaderCell();
		cell.setId("discount");
		cell.add(new Text(localize("discount", "Discount")));

		cell = row.createHeaderCell();
		cell.setId("edit");
		cell.add(new Text(localize("edit", "Edit")));

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setId("delete");
		cell.add(new Text(localize("delete", "Delete")));

		group = table.createBodyRowGroup();
		int iRow = 1;
		java.util.Iterator iter = coursePrices.iterator();
		while (iter.hasNext()) {
			CourseDiscount cDiscount = (CourseDiscount) iter.next();
			row = group.createRow();

			try {
				Link edit = new Link(getBundle().getImage("edit.png", localize("edit", "Edit")));
				edit.addParameter(PARAMETER_COURSE_DISCOUNT_ID, cDiscount.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);

				Link delete = new Link(getBundle().getImage("delete.png", localize("delete", "Delete")));
				delete.addParameter(PARAMETER_COURSE_DISCOUNT_ID, cDiscount.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);
				delete.setClickConfirmation(getResourceBundle().getLocalizedString("course_discount.confirm_delete", "Are you sure you want to delete the discount selected?"));

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.add(new Text(cDiscount.getName()));

				cell = row.createCell();
				cell.setId("type");
				cell.add(new Text(localize("discount_type." + cDiscount.getType(), cDiscount.getType())));

				cell = row.createCell();
				cell.setId("valid_from");
				cell.add(new Text(new IWTimestamp(cDiscount.getValidFrom()).getDateString("dd.MM.yyyy", iwc.getCurrentLocale())));

				cell = row.createCell();
				cell.setId("valid_to");
				cell.add(new Text(new IWTimestamp(cDiscount.getValidTo()).getDateString("dd.MM.yyyy", iwc.getCurrentLocale())));

				cell = row.createCell();
				cell.setId("discount");
				cell.add(new Text(Float.toString(cDiscount.getDiscount())));

				cell = row.createCell();
				cell.setId("edit");
				cell.add(edit);

				cell = row.createCell();
				cell.setId("delete");
				cell.setStyleClass("lastColumn");
				cell.add(delete);

				if (iRow % 2 == 0) {
					row.setStyleClass("even");
				}
				else {
					row.setStyleClass("odd");
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			iRow++;
		}
		form.add(table);

		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonLayer");
		form.add(buttonLayer);

		if (getBackPage() != null) {
			GenericButton back = new GenericButton(localize("back", "Back"));
			back.setPageToOpen(getBackPage());
			buttonLayer.add(back);
		}

		SubmitButton newLink = new SubmitButton(localize("new_discount", "New discount"), PARAMETER_ACTION, String.valueOf(ACTION_NEW));
		buttonLayer.add(newLink);

		add(form);
	}

	private Layer getNavigation(IWContext iwc) throws RemoteException {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("formSection");

		IWTimestamp from = new IWTimestamp();
		from.setDay(1);
		from.setMonth(1);

		IWTimestamp to = new IWTimestamp();
		to.setDay(1);
		to.setMonth(1);
		to.addYears(1);
		to.addDays(-1);

		IWDatePicker fromDate = new IWDatePicker(PARAMETER_FROM);
		fromDate.setShowYearChange(true);
		fromDate.setDate(from.getDate());
		fromDate.keepStatusOnAction(true);

		IWDatePicker toDate = new IWDatePicker(PARAMETER_TO);
		toDate.setShowYearChange(true);
		toDate.setDate(to.getDate());
		toDate.keepStatusOnAction(true);

		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Label label = new Label(getResourceBundle().getLocalizedString("from_date", "From date"), fromDate);
		formItem.add(label);
		formItem.add(fromDate);
		layer.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(getResourceBundle().getLocalizedString("to_date", "To date"), toDate);
		formItem.add(label);
		formItem.add(toDate);
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

	public void showEditor(IWContext iwc, Object courseDiscountPK) throws java.rmi.RemoteException {
		Form form = new Form();
		form.setStyleClass("adminForm");
		form.maintainParameter(PARAMETER_FROM);
		form.maintainParameter(PARAMETER_TO);

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(localize("course_discount.help_text", "Please fill in all the information and then click 'Save'.  Please note that the fields for care must contain a price but can be left empty if no price should be used.")));
		section.add(helpLayer);

		CourseDiscount discount = getCourseBusiness(iwc).getCourseDiscount(courseDiscountPK);

		TextInput inputName = new TextInput(PARAMETER_NAME);
		IWDatePicker inputFrom = new IWDatePicker(PARAMETER_VALID_FROM);
		inputFrom.setShowYearChange(true);
		IWDatePicker inputTo = new IWDatePicker(PARAMETER_VALID_TO);
		inputTo.setShowYearChange(true);
		TextInput inputDiscount = new TextInput(PARAMETER_DISCOUNT);

		form.add(new HiddenInput(PARAMETER_ACTION, String.valueOf(ACTION_VIEW)));

		DropdownMenu inputType = new DropdownMenu(PARAMETER_TYPE);
		inputType.addMenuElement(CourseConstants.DISCOUNT_SIBLING, localize("discount_type." + CourseConstants.DISCOUNT_SIBLING, CourseConstants.DISCOUNT_SIBLING));
		inputType.addMenuElement(CourseConstants.DISCOUNT_QUANTITY, localize("discount_type." + CourseConstants.DISCOUNT_QUANTITY, CourseConstants.DISCOUNT_QUANTITY));
		inputType.addMenuElement(CourseConstants.DISCOUNT_SPOUSE, localize("discount_type." + CourseConstants.DISCOUNT_SPOUSE, CourseConstants.DISCOUNT_SPOUSE));

		if (discount != null) {
			inputName.setContent(discount.getName());
			inputType.setSelectedElement(discount.getType());
			inputFrom.setDate(discount.getValidFrom());
			inputTo.setDate(discount.getValidTo());
			inputDiscount.setContent(Float.toString(discount.getDiscount()));

			form.add(new HiddenInput(PARAMETER_COURSE_DISCOUNT_ID, courseDiscountPK.toString()));
		}

		Layer layer;
		Label label;

		layer = new Layer(Layer.DIV);
		layer.setID("name");
		layer.setStyleClass("formItem");
		label = new Label(localize("name", "Name"), inputName);
		layer.add(label);
		layer.add(inputName);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("type");
		layer.setStyleClass("formItem");
		label = new Label(localize("discount_type", "Type"), inputType);
		layer.add(label);
		layer.add(inputType);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("valid_from");
		layer.setStyleClass("formItem");
		label = new Label(localize("valid_from", "Valid from"), inputFrom);
		layer.add(label);
		layer.add(inputFrom);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("valid_to");
		layer.setStyleClass("formItem");
		label = new Label(localize("valid_to", "Valid to"), inputTo);
		layer.add(label);
		layer.add(inputTo);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("price");
		layer.setStyleClass("formItem");
		label = new Label(localize("discount", "Discount"), inputDiscount);
		layer.add(label);
		layer.add(inputDiscount);
		section.add(layer);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		section.add(clearLayer);

		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonLayer");
		form.add(buttonLayer);

		SubmitButton save = new SubmitButton(localize("save", "Save"));
		save.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));

		SubmitButton cancel = new SubmitButton(localize("cancel", "Cancel"));
		cancel.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_VIEW));

		buttonLayer.add(cancel);
		buttonLayer.add(save);

		add(form);
	}

	public CourseBusiness getCourseBusiness(IWContext iwc) {
		try {
			return (CourseBusiness) IBOLookup.getServiceInstance(iwc, CourseBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}