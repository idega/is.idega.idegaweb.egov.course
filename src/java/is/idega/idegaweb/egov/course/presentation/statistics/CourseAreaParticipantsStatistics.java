/*
 * $Id$ Created on Apr 20, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation.statistics;

import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseProvider;
import is.idega.idegaweb.egov.course.data.CourseProviderArea;
import is.idega.idegaweb.egov.course.data.CourseProviderType;
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.presentation.CourseBlock;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
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
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.IWDatePicker;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.handlers.IWDatePickerHandler;
import com.idega.user.business.GenderBusiness;
import com.idega.user.data.Gender;
import com.idega.util.IWTimestamp;

public class CourseAreaParticipantsStatistics extends CourseBlock {

	private static final String PARAMETER_AREA = "prm_area";
	private static final String PARAMETER_FROM = "prm_from";
	private static final String PARAMETER_TO = "prm_to";

	public void present(IWContext iwc) {
		try {
			if (getType() == null) {
				add("No school type set");
				return;
			}

			IWResourceBundle iwrb = getResourceBundle(iwc);

			Form form = new Form();
			form.setStyleClass("adminForm");
			add(form);

			form.add(getNavigation(iwc));

			if (iwc.isParameterSet(PARAMETER_AREA)) {
				Layer section = new Layer(Layer.DIV);
				section.setStyleClass("formSection");
				section.setStyleClass("statisticsLayer");
				form.add(section);

				Heading1 heading = new Heading1(iwrb.getLocalizedString("course.course_participants_statistics", "Course participants statistics"));
				section.add(heading);

				Date fromDate = iwc.isParameterSet(PARAMETER_FROM) ? new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(iwc.getParameter(PARAMETER_FROM))).getDate() : null;
				Date toDate = iwc.isParameterSet(PARAMETER_TO) ? new IWTimestamp(IWDatePickerHandler.getParsedDateByCurrentLocale(iwc.getParameter(PARAMETER_TO))).getDate() : null;

				CourseProviderType type = getType();
				CourseProviderArea selectedArea = getSchoolBusiness(iwc)
						.getSchoolArea(iwc.getParameter(PARAMETER_AREA));

				Collection<CourseProvider> providers = getCourseProviderBusiness()
						.getProvidersForCurrentUser(getType());
				for (CourseProvider provider : providers) {
					CourseProviderArea area = provider.getCourseProviderArea();
					if (selectedArea.equals(area)) {
						Collection<Course> courses = getBusiness().getCourses(
								provider, type, fromDate, toDate);

						addResults(iwc, iwrb, type, courses, section, 
								provider.getName(), fromDate, toDate);

						Layer clearLayer = new Layer(Layer.DIV);
						clearLayer.setStyleClass("Clear");
						section.add(clearLayer);
					}
				}
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

		DropdownMenu schoolArea = new DropdownMenu(PARAMETER_AREA);
		schoolArea.addMenuElementFirst("", localize("select_school_area", "Select school area"));
		schoolArea.keepStatusOnAction(true);
		Collection<CourseProviderArea> areas = getCourseProviderBusiness()
				.getAreasForCurrentUser(getType());
		schoolArea.addMenuElements(areas);

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
		Label label = new Label(localize("school_area", "School area"), schoolArea);
		formItem.add(label);
		formItem.add(schoolArea);
		layer.add(formItem);

		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		label = new Label(getResourceBundle().getLocalizedString("from_date", "From date"), fromDate);
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

	private void addResults(IWContext iwc, IWResourceBundle iwrb, CourseProviderType type, Collection<Course> courses, Layer section, String header, Date fromDate, Date toDate) throws RemoteException {
		Gender male = null;
		Gender female = null;
		try {
			male = getGenderBusiness(iwc).getMaleGender();
			female = getGenderBusiness(iwc).getFemaleGender();
		}
		catch (FinderException e) {
			e.printStackTrace();
			return;
		}

		Heading2 heading2 = new Heading2(header);
		section.add(heading2);

		Table2 table = new Table2();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth("100%");
		table.setStyleClass("adminTable");
		table.setStyleClass("ruler");
		section.add(table);

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("course");
		cell.add(new Text(this.getResourceBundle(iwc).getLocalizedString("course", "Course")));

		cell = row.createHeaderCell();
		cell.setStyleClass("courseType");
		cell.add(new Text(this.getResourceBundle(iwc).getLocalizedString("course_type", "Course type")));

		Map<Gender, Integer> genderTotals = new HashMap<Gender, Integer>();
		genderTotals.put(male, 0);
		genderTotals.put(female, 0);

		cell = row.createHeaderCell();
		cell.setStyleClass("male");
		cell.add(new Text(this.getResourceBundle(iwc).getLocalizedString("male", "Male")));

		cell = row.createHeaderCell();
		cell.setStyleClass("female");
		cell.add(new Text(this.getResourceBundle(iwc).getLocalizedString("female", "Female")));

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("total");
		cell.add(new Text(this.getResourceBundle(iwc).getLocalizedString("total", "Total")));

		group = table.createBodyRowGroup();

		int iRow = 1;

		int total = 0;
		for (Course course : courses) {
			CourseType courseType = course.getCourseType();

			row = group.createRow();
			cell = row.createCell();
			cell.setStyleClass("firstColumn");
			cell.setStyleClass("course");
			cell.add(new Text(course.getName()));

			cell = row.createCell();
			cell.setStyleClass("type");
			cell.add(new Text(courseType.getName()));

			int sum = 0;

			int maleSum = getBusiness().getNumberOfChoices(course, male);
			sum += maleSum;
			genderTotals.put(male, new Integer((genderTotals.get(male)).intValue() + maleSum));

			int femaleSum = getBusiness().getNumberOfChoices(course, female);
			sum += femaleSum;
			genderTotals.put(female, new Integer(((Integer) genderTotals.get(female)).intValue() + femaleSum));

			cell = row.createCell();
			cell.setStyleClass("male");
			cell.add(new Text(String.valueOf(maleSum)));

			cell = row.createCell();
			cell.setStyleClass("female");
			cell.add(new Text(String.valueOf(femaleSum)));

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
		cell.setColumnSpan(2);
		cell.add(new Text(this.getResourceBundle(iwc).getLocalizedString("total", "Total")));

		int maleSum = ((Integer) genderTotals.get(male)).intValue();
		cell = row.createCell();
		cell.setStyleClass("male");
		cell.add(new Text(String.valueOf(maleSum)));

		int femaleSum = ((Integer) genderTotals.get(female)).intValue();
		cell = row.createCell();
		cell.setStyleClass("female");
		cell.add(new Text(String.valueOf(femaleSum)));

		cell = row.createCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("total");
		cell.add(new Text(String.valueOf(total)));
	}

	private GenderBusiness getGenderBusiness(IWApplicationContext iwac) {
		try {
			return (GenderBusiness) IBOLookup.getServiceInstance(iwac, GenderBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public void setSchoolTypePK(String schoolTypePK) {
		setCourseProviderType(schoolTypePK);
	}
}