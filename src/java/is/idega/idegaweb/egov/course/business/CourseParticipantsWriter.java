/*
 * $Id$ Created on Mar 28, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.business;

import is.idega.block.family.data.Child;
import is.idega.block.family.data.Custodian;
import is.idega.block.family.data.Relative;
import is.idega.idegaweb.egov.accounting.business.CitizenBusiness;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseApplication;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.presentation.CourseBlock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.StringHandler;
import com.idega.util.text.Name;

public class CourseParticipantsWriter extends DownloadWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private CourseBusiness business;
	private CitizenBusiness userBusiness;
	private Locale locale;
	private IWResourceBundle iwrb;

	private String courseName;

	public CourseParticipantsWriter() {
	}

	private Collection getChoises(Course course, IWContext iwc) {
		if (iwc.isParameterSet(CourseBlock.PARAMETER_COURSE_PARTICIPANT_PK)) {
			User participant = null;
			try {
				participant = userBusiness.getUser(Integer.valueOf(iwc.getParameter(CourseBlock.PARAMETER_COURSE_PARTICIPANT_PK)).intValue());
			} catch(Exception e) {
				e.printStackTrace();
			}
			if (participant == null) {
				return new ArrayList();
			}
			
			CourseChoice choise = null;
			try {
				choise = business.getCourseChoiceHome().findByUserAndCourse(participant, course);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
			if (choise == null) {
				return new ArrayList();
			}
			return Arrays.asList(new CourseChoice[] {choise});
		}
		try {
			return business.getCourseChoices(course);
		} catch (RemoteException e) {
			e.printStackTrace();
			return new ArrayList();
		}
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

				Collection choices = getChoises(course, iwc);

				this.buffer = writeXLS(iwc, choices);
				String fileName = "participants.xls";
				if (iwc.isParameterSet(CourseBlock.PARAMETER_COURSE_PARTICIPANT_PK)) {
					fileName = "participant.xls";
				}
				setAsDownload(iwc, fileName, this.buffer.length());
			}
		} catch(Exception e) {
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
		sheet.setColumnWidth((short) 2, (short) (30 * 256));
		sheet.setColumnWidth((short) 3, (short) (14 * 256));
		sheet.setColumnWidth((short) 4, (short) (14 * 256));
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
		
		row = sheet.createRow(cellRow++);

		boolean showAll = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_BIRTHYEARS, true);
		if (showAll) {
			row = sheet.createRow(cellRow++);
			cell = row.createCell((short) 0);
			cell.setCellValue(this.iwrb.getLocalizedString("participant", "Participant"));
			cell.setCellStyle(bigStyle);
			cell = row.createCell((short) 13);
			cell.setCellValue(this.iwrb.getLocalizedString("custodians", "Custodians"));
			cell.setCellStyle(bigStyle);
			cell = row.createCell((short) 43);
			cell.setCellValue(this.iwrb.getLocalizedString("relatives", "Relatives"));
			cell.setCellStyle(bigStyle);
		}
		
		short iCell = 0;
		row = sheet.createRow(cellRow++);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("name", "Name"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("personal_id", "Personal ID"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("address", "Address"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("postal_code", "Postal code"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("home_phone", "Home phone"));
		cell.setCellStyle(style);

		if (showAll) {
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("child_care.growth_deviation", "Growth deviation"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("child_care.allergies", "Allergies"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("child.other_information", "Other information"));
			cell.setCellStyle(style);
	
			for (int a = 1; a <= 3; a++) {
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("relation", "Relation"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("name", "Name"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("personal_id", "Personal ID"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("address", "Address"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("zip_code", "Zip code"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("home_phone", "Home phone"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("work_phone", "Work phone"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("mobile_phone", "Mobile phone"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("email", "E-mail"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("marital_status", "Marital status"));
				cell.setCellStyle(style);
			}
		}
		else {
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("work_phone", "Work phone"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("mobile_phone", "Mobile phone"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("email", "E-mail"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("application.has_dyslexia", "Has dyslexia"));
			cell.setCellStyle(style);
		}
		
		if (showAll) {
			for (int a = 1; a <= 2; a++) {
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("relation", "Relation"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("name", "Name"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("home_phone", "Home phone"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("work_phone", "Work phone"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("mobile_phone", "Mobile phone"));
				cell.setCellStyle(style);
				cell = row.createCell(iCell++);
				cell.setCellValue(this.iwrb.getLocalizedString("email", "E-mail"));
				cell.setCellStyle(style);
			}
		}
		
		User user;
		User owner;
		Address address;
		PostalCode postalCode = null;
		Phone phone;
		CourseChoice choice;
		CourseApplication application;

		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			row = sheet.createRow(cellRow++);
			choice = (CourseChoice) iter.next();
			application = choice.getApplication();
			user = choice.getUser();
			owner = application.getOwner();
			Child child = this.userBusiness.getMemberFamilyLogic().getChild(user);
			address = this.userBusiness.getUsersMainAddress(user);
			if (address != null) {
				postalCode = address.getPostalCode();
			}
			phone = this.userBusiness.getChildHomePhone(user);

			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
			row.createCell((short) 0).setCellValue(name.getName(this.locale, true));
			row.createCell((short) 1).setCellValue(PersonalIDFormatter.format(user.getPersonalID(), this.locale));
			if (address != null) {
				row.createCell((short) 2).setCellValue(address.getStreetAddress());
				if (postalCode != null) {
					row.createCell((short) 3).setCellValue(postalCode.getPostalAddress());
				}
			}
			if (phone != null) {
				row.createCell((short) 4).setCellValue(phone.getNumber());
			}

			if (showAll) {
				Boolean hasGrowthDeviation = child.hasGrowthDeviation(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
				if (hasGrowthDeviation == null) {
					hasGrowthDeviation = child.hasGrowthDeviation(CourseConstants.COURSE_PREFIX);
				}
				if (hasGrowthDeviation != null && hasGrowthDeviation.booleanValue()) {
					row.createCell((short) 5).setCellValue(this.iwrb.getLocalizedString("yes", "Yes"));
				}
				else {
					row.createCell((short) 5).setCellValue(this.iwrb.getLocalizedString("no", "No"));
				}
	
				Boolean hasAllergies = child.hasAllergies(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
				if (hasAllergies == null) {
					hasAllergies = child.hasAllergies(CourseConstants.COURSE_PREFIX);
				}
				if (hasAllergies != null && hasAllergies.booleanValue()) {
					row.createCell((short) 6).setCellValue(this.iwrb.getLocalizedString("yes", "Yes"));
				}
				else {
					row.createCell((short) 6).setCellValue(this.iwrb.getLocalizedString("no", "No"));
				}
	
				if (child.getOtherInformation(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey()) != null) {
					row.createCell((short) 7).setCellValue(child.getOtherInformation(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey()));
				}
				else if (child.getOtherInformation(CourseConstants.COURSE_PREFIX) != null) {
					row.createCell((short) 7).setCellValue(child.getOtherInformation(CourseConstants.COURSE_PREFIX));
				}
	
				iCell = 8;
	
				Collection custodians = new ArrayList();
				try {
					custodians = child.getCustodians();
				} catch(Exception e) {}
				Custodian extraCustodian = child.getExtraCustodian();
				if (extraCustodian != null) {
					custodians.add(extraCustodian);
				}
	
				Iterator iterator = custodians.iterator();
				while (iterator.hasNext()) {
					Custodian element = (Custodian) iterator.next();
					address = this.userBusiness.getUsersMainAddress(element);
					Phone work = null;
					Phone mobile = null;
					Email email = null;
					String relation = this.iwrb.getLocalizedString("relation." + child.getRelation(element));
					String maritalStatus = this.iwrb.getLocalizedString("marital_status." + element.getMaritalStatus());
	
					try {
						phone = this.userBusiness.getUsersHomePhone(element);
					}
					catch (NoPhoneFoundException npfe) {
						phone = null;
					}
	
					try {
						work = this.userBusiness.getUsersWorkPhone(element);
					}
					catch (NoPhoneFoundException npfe) {
						work = null;
					}
	
					try {
						mobile = this.userBusiness.getUsersMobilePhone(element);
					}
					catch (NoPhoneFoundException npfe) {
						mobile = null;
					}
	
					try {
						email = this.userBusiness.getUsersMainEmail(element);
					}
					catch (NoEmailFoundException nefe) {
						email = null;
					}
	
					name = new Name(element.getFirstName(), element.getMiddleName(), element.getLastName());
					row.createCell(iCell++).setCellValue(relation);
					row.createCell(iCell++).setCellValue(name.getName(this.locale, true));
					row.createCell(iCell++).setCellValue(PersonalIDFormatter.format(element.getPersonalID(), this.locale));
					if (address != null) {
						row.createCell(iCell++).setCellValue(address.getStreetAddress());
						if (postalCode != null) {
							row.createCell(iCell++).setCellValue(postalCode.getPostalAddress());
						}
						else {
							iCell++;
						}
					}
					else {
						iCell++;
					}
					if (phone != null) {
						row.createCell(iCell++).setCellValue(phone.getNumber());
					}
					else {
						iCell++;
					}
					if (work != null) {
						row.createCell(iCell++).setCellValue(work.getNumber());
					}
					else {
						iCell++;
					}
					if (mobile != null) {
						row.createCell(iCell++).setCellValue(mobile.getNumber());
					}
					else {
						iCell++;
					}
					if (email != null) {
						row.createCell(iCell++).setCellValue(email.getEmailAddress());
					}
					else {
						iCell++;
					}
					if (maritalStatus != null) {
						row.createCell(iCell++).setCellValue(maritalStatus);
					}
					else {
						iCell++;
					}
				}
	
				iCell = 38;
	
				List relatives = new ArrayList();
				Relative mainRelative = child.getMainRelative(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
				if (mainRelative == null) {
					mainRelative = child.getMainRelative(CourseConstants.COURSE_PREFIX);
				}
				if (mainRelative != null) {
					relatives.add(mainRelative);
				}
				Collection otherRelatives = child.getRelatives(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
				if (otherRelatives.isEmpty()) {
					otherRelatives = child.getRelatives(CourseConstants.COURSE_PREFIX);
				}
				relatives.addAll(otherRelatives);
				iterator = relatives.iterator();
				while (iterator.hasNext()) {
					Relative element = (Relative) iterator.next();
					String relation = this.iwrb.getLocalizedString("relation." + element.getRelation());
	
					row.createCell(iCell++).setCellValue(relation);
					row.createCell(iCell++).setCellValue(element.getName());
					row.createCell(iCell++).setCellValue(element.getHomePhone());
					row.createCell(iCell++).setCellValue(element.getWorkPhone());
					row.createCell(iCell++).setCellValue(element.getMobilePhone());
					row.createCell(iCell++).setCellValue(element.getEmail());
				}
			}
			else {
				iCell = 5;
				Phone work = null;
				Phone mobile = null;
				Email email = null;

				try {
					work = this.userBusiness.getUsersWorkPhone(child);
				}
				catch (NoPhoneFoundException npfe) {
					work = null;
				}

				try {
					mobile = this.userBusiness.getUsersMobilePhone(child);
				}
				catch (NoPhoneFoundException npfe) {
					mobile = null;
				}

				try {
					email = this.userBusiness.getUsersMainEmail(child);
				}
				catch (NoEmailFoundException nefe) {
					email = null;
				}

				if (work != null) {
					row.createCell(iCell++).setCellValue(work.getNumber());
				}
				else {
					iCell++;
				}
				if (mobile != null) {
					row.createCell(iCell++).setCellValue(mobile.getNumber());
				}
				else {
					iCell++;
				}
				if (email != null) {
					row.createCell(iCell++).setCellValue(email.getEmailAddress());
				}
				else {
					iCell++;
				}
				if (choice.hasDyslexia()) {
					row.createCell(iCell++).setCellValue(this.iwrb.getLocalizedString("yes", "Yes"));
				}
				else {
					row.createCell(iCell++).setCellValue(this.iwrb.getLocalizedString("no", "No"));
				}
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