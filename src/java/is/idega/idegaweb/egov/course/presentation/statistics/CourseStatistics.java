/*
 * $Id$ Created on Apr 20, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation.statistics;

import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.presentation.CourseBlock;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Heading2;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.IWDatePicker;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.util.IWTimestamp;

public class CourseStatistics extends CourseBlock {

	private static final String PARAMETER_FROM = "prm_from";
	private static final String PARAMETER_TO = "prm_to";

	private Object schoolTypePK;

	public void present(IWContext iwc) {
		try {
			if (schoolTypePK == null) {
				add("No school type set");
				return;
			}

			IWResourceBundle iwrb = getResourceBundle(iwc);

			Form form = new Form();
			form.setStyleClass("adminForm");
			add(form);

			form.add(getNavigation(iwc));

			Layer section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			section.setStyleClass("statisticsLayer");
			form.add(section);

			Heading1 heading = new Heading1(iwrb.getLocalizedString("course.course_statistics", "Course statistics"));
			section.add(heading);

			SchoolType type = getSchoolBusiness(iwc).getSchoolType(new Integer(schoolTypePK.toString()));
			Collection courseTypes = getBusiness().getCourseTypes(new Integer(schoolTypePK.toString()), false);
			Collection areas = getBusiness().getSchoolAreas();

			Collection userProviders = null;
			if (iwc.getAccessController().hasRole(CourseConstants.ADMINISTRATOR_ROLE_KEY, iwc)) {
				userProviders = getBusiness().getProvidersForUser(iwc.getCurrentUser());
			}

			Iterator iter = areas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();

				Collection providers = getBusiness().getProviders(area, type);
				if (userProviders != null) {
					providers.retainAll(userProviders);
				}
				
				addResults(iwc, iwrb, type, providers, courseTypes, section, area.getName());

				Layer clearLayer = new Layer(Layer.DIV);
				clearLayer.setStyleClass("Clear");
				section.add(clearLayer);
			}

			if (getBackPage() != null) {
				Layer buttonLayer = new Layer();
				buttonLayer.setStyleClass("buttonLayer");
				form.add(buttonLayer);

				GenericButton back = new GenericButton(localize("back", "Back"));
				back.setPageToOpen(getBackPage());
				buttonLayer.add(back);
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
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

	private void addResults(IWContext iwc, IWResourceBundle iwrb, SchoolType type, Collection providers, Collection courseTypes, Layer section, String header) throws RemoteException {
		Heading2 heading2 = new Heading2(header);
		section.add(heading2);

		Table2 table = new Table2();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setStyleClass("adminTable");
		table.setStyleClass("ruler");
		section.add(table);

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("type");
		cell.add(new Text(this.getResourceBundle(iwc).getLocalizedString("type", "Type")));

		Map providerTotals = new HashMap();
		Iterator iterator = providers.iterator();
		while (iterator.hasNext()) {
			School provider = (School) iterator.next();

			cell = row.createHeaderCell();
			cell.setStyleClass(provider.getPrimaryKey().toString());
			cell.add(new Text(provider.getName()));
		}

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("total");
		cell.add(new Text(this.getResourceBundle(iwc).getLocalizedString("total", "Total")));

		group = table.createBodyRowGroup();

		Date fromDate = iwc.isParameterSet(PARAMETER_FROM) ? new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(iwc.getParameter(PARAMETER_FROM))).getDate() : null;
		Date toDate = iwc.isParameterSet(PARAMETER_TO) ? new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(iwc.getParameter(PARAMETER_TO))).getDate() : null;
		int iRow = 1;

		int total = 0;
		Iterator iter = courseTypes.iterator();
		while (iter.hasNext()) {
			CourseType courseType = (CourseType) iter.next();

			row = group.createRow();
			cell = row.createCell();
			cell.setStyleClass("firstColumn");
			cell.setStyleClass("type");
			cell.add(new Text(courseType.getName()));

			int sum = 0;
			iterator = providers.iterator();
			while (iterator.hasNext()) {
				School provider = (School) iterator.next();
				if (!providerTotals.containsKey(provider)) {
					providerTotals.put(provider, new Integer(0));
				}
				int providerSum = getBusiness().getNumberOfCourses(provider, type, courseType, fromDate, toDate);
				sum += providerSum;
				providerTotals.put(provider, new Integer(((Integer) providerTotals.get(provider)).intValue() + providerSum));

				cell = row.createCell();
				cell.setStyleClass(courseType.getPrimaryKey().toString());
				cell.add(new Text(String.valueOf(providerSum)));
			}
			total += sum;

			cell = row.createCell();
			cell.setStyleClass("lastColumn");
			cell.setStyleClass("total");
			cell.add(new Text(String.valueOf(sum)));

			if (iRow % 2 == 0) {
				row.setStyleClass("even");
			}
			else {
				row.setStyleClass("odd");
			}

			iRow++;
		}

		group = table.createFooterRowGroup();
		row = group.createRow();

		cell = row.createCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("totals");
		cell.add(new Text(this.getResourceBundle(iwc).getLocalizedString("total", "Total")));

		iterator = providers.iterator();
		while (iterator.hasNext()) {
			School provider = (School) iterator.next();
			int providerSum = ((Integer) providerTotals.get(provider)).intValue();

			cell = row.createCell();
			cell.add(new Text(String.valueOf(providerSum)));
		}

		cell = row.createCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("total");
		cell.add(new Text(String.valueOf(total)));
	}

	public void setSchoolTypePK(String schoolTypePK) {
		this.schoolTypePK = schoolTypePK;
	}
}