/*
 * $Id$ Created on Mar 28, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.business;

import is.idega.block.family.data.Child;
import is.idega.idegaweb.egov.accounting.business.CitizenBusiness;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.presentation.CourseBlock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.StringHandler;
import com.idega.util.text.Name;

public class CourseAttendanceWriter extends DownloadWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private CourseBusiness business;
	private CitizenBusiness userBusiness;
	private Locale locale;
	private IWResourceBundle iwrb;

	private String courseName;

	public CourseAttendanceWriter() {
	}

	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			this.locale = iwc.getApplicationSettings().getApplicationLocale();
			this.business = getCourseBusiness(iwc);
			this.userBusiness = getUserBusiness(iwc);
			this.iwrb = iwc.getIWMainApplication().getBundle(CourseConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(this.locale);

			if (req.getParameter(CourseBlock.PARAMETER_COURSE_PK) != null) {
				Course course = business.getCourse(iwc.getParameter(CourseBlock.PARAMETER_COURSE_PK));
				courseName = course.getName();

				Collection choices = business.getCourseChoices(course);

				this.buffer = writeXLS(iwc, choices);
				setAsDownload(iwc, "students.xls", this.buffer.length());

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMimeType() {
		if (this.buffer != null) {
			return this.buffer.getMimeType();
		}
		return super.getMimeType();
	}

	public void writeTo(OutputStream out) throws IOException {
		if (this.buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(this.buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else {
			System.err.println("buffer is null");
		}
	}

	public MemoryFileBuffer writeXLS(IWContext iwc, Collection choices) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(StringHandler.shortenToLength(this.courseName, 30));
		sheet.setColumnWidth((short) 0, (short) (30 * 256));
		sheet.setColumnWidth((short) 1, (short) (14 * 256));
		sheet.setColumnWidth((short) 2, (short) (10 * 256));
		sheet.setColumnWidth((short) 3, (short) (10 * 256));
		sheet.setColumnWidth((short) 4, (short) (10 * 256));
		sheet.setColumnWidth((short) 5, (short) (14 * 256));
		sheet.setColumnWidth((short) 6, (short) (14 * 256));
		sheet.setColumnWidth((short) 7, (short) (30 * 256));
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		HSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);

		HSSFFont bigFont = wb.createFont();
		bigFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		bigFont.setFontHeightInPoints((short) 13);
		HSSFCellStyle bigStyle = wb.createCellStyle();
		bigStyle.setFont(bigFont);

		int cellRow = 0;
		HSSFRow row = sheet.createRow(cellRow++);
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue(this.courseName);
		cell.setCellStyle(bigStyle);
		cell = row.createCell((short) 1);

		row = sheet.createRow(cellRow++);

		short iCell = 0;
		row = sheet.createRow(cellRow++);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("name", "Name"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("personal_id", "Personal ID"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("pre_care", "Pre care"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("post_care", "Post care"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("picked_up", "Picked up"));
		cell.setCellStyle(style);

		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("child.growth_deviation", "Growth deviation"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("child.allergies", "Allergies"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("child.other_information", "Other information"));
		cell.setCellStyle(style);

		User user;
		CourseChoice choice;

		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			row = sheet.createRow(cellRow++);
			choice = (CourseChoice) iter.next();
			user = choice.getUser();
			Child child = this.userBusiness.getMemberFamilyLogic().getChild(user);
			boolean preCare = choice.getDayCare() == CourseConstants.DAY_CARE_PRE || choice.getDayCare() == CourseConstants.DAY_CARE_PRE_AND_POST;
			boolean postCare = choice.getDayCare() == CourseConstants.DAY_CARE_POST || choice.getDayCare() == CourseConstants.DAY_CARE_PRE_AND_POST;

			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
			row.createCell((short) 0).setCellValue(name.getName(this.locale, true));
			row.createCell((short) 1).setCellValue(PersonalIDFormatter.format(user.getPersonalID(), this.locale));
			row.createCell((short) 2).setCellValue(preCare ? iwrb.getLocalizedString("yes", "Yes") : iwrb.getLocalizedString("no", "No"));
			row.createCell((short) 3).setCellValue(postCare ? iwrb.getLocalizedString("yes", "Yes") : iwrb.getLocalizedString("no", "No"));
			row.createCell((short) 4).setCellValue(choice.isPickedUp() ? iwrb.getLocalizedString("yes", "Yes") : iwrb.getLocalizedString("no", "No"));

			if (child.hasGrowthDeviation(CourseConstants.COURSE_PREFIX) != null && child.hasGrowthDeviation(CourseConstants.COURSE_PREFIX).booleanValue()) {
				row.createCell((short) 5).setCellValue(this.iwrb.getLocalizedString("yes", "Yes"));
			}
			else {
				row.createCell((short) 5).setCellValue(this.iwrb.getLocalizedString("no", "No"));
			}

			if (child.hasAllergies(CourseConstants.COURSE_PREFIX) != null && child.hasAllergies(CourseConstants.COURSE_PREFIX).booleanValue()) {
				row.createCell((short) 6).setCellValue(this.iwrb.getLocalizedString("yes", "Yes"));
			}
			else {
				row.createCell((short) 6).setCellValue(this.iwrb.getLocalizedString("no", "No"));
			}

			if (child.getOtherInformation(CourseConstants.COURSE_PREFIX) != null) {
				row.createCell((short) 7).setCellValue(child.getOtherInformation(CourseConstants.COURSE_PREFIX));
			}
		}
		wb.write(mos);

		buffer.setMimeType("application/x-msexcel");
		return buffer;
	}

	private CourseBusiness getCourseBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CourseBusiness) IBOLookup.getServiceInstance(iwc, CourseBusiness.class);
	}

	private CitizenBusiness getUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CitizenBusiness) IBOLookup.getServiceInstance(iwc, CitizenBusiness.class);
	}
}