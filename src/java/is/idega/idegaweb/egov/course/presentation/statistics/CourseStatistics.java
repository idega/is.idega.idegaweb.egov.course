/*
 * $Id$ Created on Apr 20, 2007
 *
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation.statistics;

import is.idega.idegaweb.egov.course.data.CourseProvider;
import is.idega.idegaweb.egov.course.data.CourseProviderArea;
import is.idega.idegaweb.egov.course.data.CourseProviderAreaHome;
import is.idega.idegaweb.egov.course.data.CourseProviderHome;
import is.idega.idegaweb.egov.course.data.CourseProviderType;
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.presentation.CourseBlock;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.AccessController;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
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
import com.idega.util.ListUtil;

public class CourseStatistics extends CourseBlock {

	private static final String PARAMETER_FROM = "prm_from";
	private static final String PARAMETER_TO = "prm_to";

	private Collection<CourseProvider> courseProviders = null;

	protected Collection<CourseProvider> getCourseProviders() {
		if (ListUtil.isEmpty(this.courseProviders)) {
			this.courseProviders = getCourseProviderBusiness()
					.getProvidersForCurrentUser(getType());
		}

		return this.courseProviders;
	}

	private Collection<CourseType> getCourseTypes() throws RemoteException {
		if (getType() != null) {
			return getBusiness().getCourseTypes(getType(), Boolean.TRUE);
		}

		Collection<CourseProviderType> providerTypes = getCourseProviderBusiness()
				.getCourseProviderTypes(getCourseProviders());
		if (ListUtil.isEmpty(providerTypes)) {
			return Collections.emptyList();
		}

		Map<String, CourseType> allTypes = new HashMap<String, CourseType>();
		for (CourseProviderType courseProviderType: providerTypes) {
			Collection<CourseType> types = getBusiness().getCourseTypes(
					new Integer(courseProviderType.getPrimaryKey().toString()), 
					Boolean.TRUE);
			for (CourseType type: types) {
				allTypes.put(type.getPrimaryKey().toString(), type);
			}
		}

		return allTypes.values();
	}

	private CourseProviderType getProviderType(IWContext iwc) {
		if (getType() != null) {
			return getType();
		}

		Collection<CourseProviderType> providerTypes = getCourseProviderBusiness()
				.getCourseProviderTypes(getCourseProviders());
		if (!ListUtil.isEmpty(providerTypes)) {
			return providerTypes.iterator().next();
		}

		return null;
	}

	private boolean isValidCourseProviderTypePK() {
		return getType() != null;
	}

	@Override
	public void present(IWContext iwc) {
		try {
			if (!isValidCourseProviderTypePK() && ListUtil.isEmpty(getCourseProviders())) {
				add("Course provider type is not set and no course providers " +
						"are available for the current user");
				return;
			}

			Form form = new Form();
			form.setStyleClass("adminForm");
			add(form);

			form.add(getNavigation(iwc));

			Layer section = new Layer(Layer.DIV);
			section.setStyleClass("formSection");
			section.setStyleClass("statisticsLayer");
			form.add(section);

			IWResourceBundle iwrb = getResourceBundle(iwc);
			Heading1 heading = new Heading1(iwrb.getLocalizedString(
					"course.course_statistics", "Course statistics"));
			section.add(heading);

			CourseProviderType type = getProviderType(iwc);
			Collection<CourseType> courseTypes = getCourseTypes();
			
			Map<CourseProviderArea, List<CourseProvider>> sortedProviders = getCourseProviderBusiness()
					.getProvidersByAreas(getCourseProviders());
			for (CourseProviderArea area : sortedProviders.keySet()) {
				addResults(iwc, iwrb, type, sortedProviders.get(area), 
						courseTypes, section, area.getName());

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
		} catch (RemoteException re) {
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

	private void addResults(
			IWContext iwc,
			IWResourceBundle iwrb,
			CourseProviderType type,
			Collection<CourseProvider> providers,
			Collection<CourseType> courseTypes,
			Layer section,
			String header
	) throws RemoteException {
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

		for (Iterator<CourseProvider> iterator = providers.iterator(); iterator.hasNext();) {
			CourseProvider provider = iterator.next();

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
		Map<CourseProvider, Integer> providerTotals = new HashMap<CourseProvider, Integer>();
		for (Iterator<CourseType> iter = courseTypes.iterator(); iter.hasNext();) {
			CourseType courseType = iter.next();

			row = group.createRow();
			cell = row.createCell();
			cell.setStyleClass("firstColumn");
			cell.setStyleClass("type");
			cell.add(new Text(courseType.getName()));

			int sum = 0;
			for (Iterator<CourseProvider> prIter = providers.iterator(); prIter.hasNext();) {
				CourseProvider provider = prIter.next();
				if (!providerTotals.containsKey(provider)) {
					providerTotals.put(provider, new Integer(0));
				}

				int providerSum = getBusiness().getNumberOfCourses(provider, type, courseType, fromDate, toDate);
				sum += providerSum;
				providerTotals.put(provider, new Integer(providerTotals.get(provider).intValue() + providerSum));

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

		for (Iterator<CourseProvider> prIter = providers.iterator(); prIter.hasNext();) {
			CourseProvider provider = prIter.next();
			int providerSum = providerTotals.get(provider).intValue();

			cell = row.createCell();
			cell.add(new Text(String.valueOf(providerSum)));
		}

		cell = row.createCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("total");
		cell.add(new Text(String.valueOf(total)));
	}

	public void setCourseProviderTypePK(String courseProviderTypePK) {
		setCourseProviderType(courseProviderTypePK);
	}

	@Deprecated
	/**
	 * Use method setCourseProviderTypePK
	 *
	 * @param courseProviderTypePK
	 */
	public void setSchoolTypePK(String courseProviderTypePK) {
		setCourseProviderTypePK(courseProviderTypePK);
	}

	protected boolean hasRole(String role, IWContext iwc) {
		AccessController controller = getAccessController(iwc);
		if (controller == null) {
			return Boolean.FALSE;
		}

		return controller.hasRole(iwc.getCurrentUser(), role);
	}

	private CourseProviderHome courseProviderHome = null;

	protected CourseProviderHome getCourseProviderHome() {
		if (this.courseProviderHome == null) {
			try {
				this.courseProviderHome = (CourseProviderHome) IDOLookup
						.getHome(CourseProvider.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + CourseProviderHome.class.getSimpleName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderHome;
	}

	private CourseProviderAreaHome courseProviderAreaHome = null;

	protected CourseProviderAreaHome getCourseProviderAreaHome() {
		if (this.courseProviderAreaHome == null) {
			try {
				this.courseProviderAreaHome = (CourseProviderAreaHome) IDOLookup.getHome(CourseProviderArea.class);
			} catch (IDOLookupException e) {
				getLogger().log(Level.WARNING, 
						"Failed to get " + CourseProviderAreaHome.class + 
						" cause of: ", e);
			}
		}

		return this.courseProviderAreaHome;
	}

	private AccessController accessController = null;

	protected AccessController getAccessController(IWContext iwc) {
		if (iwc != null) {
			this.accessController = iwc.getAccessController();
		}

		return this.accessController;
	}
}