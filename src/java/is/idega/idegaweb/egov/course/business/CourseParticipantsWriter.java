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
import is.idega.idegaweb.egov.course.data.ApplicationHolder;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseApplication;
import is.idega.idegaweb.egov.course.data.CourseChoice;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.PriceHolder;
import is.idega.idegaweb.egov.course.presentation.CourseBlock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import com.idega.core.file.util.MimeTypeUtil;
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
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;
import com.idega.util.text.Name;

public class CourseParticipantsWriter extends DownloadWriter implements MediaWritable {

	private MemoryFileBuffer buffer = null;
	private CourseBusiness business;
	private CitizenBusiness userBusiness;
	private Locale locale;
	private IWResourceBundle iwrb;

	private String courseName;

	public static final String PARAMETER_WAITING_LIST = "prm_waiting_list";
	
	public CourseParticipantsWriter() {
	}

	private Collection<CourseChoice> getChoices(Course course, IWContext iwc, 
			boolean waitingList) {
		if (iwc.isParameterSet(CourseBlock.PARAMETER_COURSE_PARTICIPANT_PK)) {
			User participant = null;
			try {
				participant = userBusiness.getUser(Integer.valueOf(
						iwc.getParameter(CourseBlock.PARAMETER_COURSE_PARTICIPANT_PK)
						));
			} catch(Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"failed to get participant, cause of: ", e);
			}

			if (participant == null) {
				return Collections.emptyList();
			}

			CourseChoice choice = null;
			try {
				choice = business.getCourseChoiceHome()
						.findByUserAndCourse(participant, course);
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, 
						"Failed to get " + CourseChoice.class.getSimpleName() + 
						" cause of: ", e);
			}

			if (choice == null) {
				return Collections.emptyList();
			}

			return Arrays.asList(choice);
		}

		try {
			return business.getCourseChoices(course, waitingList);
		} catch (RemoteException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"Failed to get " + CourseChoice.class.getSimpleName() + 
					" cause of: ", e);
		}

		return Collections.emptyList();
	}
	
	@Override
	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			this.locale = iwc.getApplicationSettings().getApplicationLocale();
			this.business = getCourseBusiness(iwc);
			this.userBusiness = getUserBusiness(iwc);
			this.iwrb = iwc.getIWMainApplication().getBundle(CourseConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(this.locale);

			if (req.getParameter(CourseBlock.PARAMETER_COURSE_PK) != null) {
				Course course = business.getCourse(iwc.getParameter(CourseBlock.PARAMETER_COURSE_PK));
				courseName = course.getName();
				
				boolean waitingList = iwc.isParameterSet(PARAMETER_WAITING_LIST);

				Collection<CourseChoice> choices = getChoices(course, iwc, waitingList);

				if (iwc.hasRole(CourseConstants.COURSE_ACCOUNTING_ROLE_KEY)) {
					this.buffer = writeAccountingXLS(iwc, choices);
				}
				else {
					this.buffer = writeXLS(iwc, choices);
				}
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

	@Override
	public String getMimeType() {
		if (this.buffer != null) {
			return this.buffer.getMimeType();
		}
		return super.getMimeType();
	}

	@Override
	public void writeTo(OutputStream out) throws IOException {
		if (this.buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(this.buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
			mis.close();
		}
		else {
			System.err.println("buffer is null");
		}
	}

	public MemoryFileBuffer writeXLS(IWContext iwc, Collection<CourseChoice> choices) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(StringHandler.shortenToLength(this.courseName, 30));

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
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(this.courseName);
		cell.setCellStyle(bigStyle);
		
		row = sheet.createRow(cellRow++);

		boolean showAll = iwc.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_USE_BIRTHYEARS, true);
		if (showAll) {
			row = sheet.createRow(cellRow++);

			cell = row.createCell(0);
			cell.setCellValue(this.iwrb.getLocalizedString("participant", "Participant"));
			cell.setCellStyle(bigStyle);

			cell = row.createCell(13);
			cell.setCellValue(this.iwrb.getLocalizedString("custodians", "Custodians"));
			cell.setCellStyle(bigStyle);

			cell = row.createCell(43);
			cell.setCellValue(this.iwrb.getLocalizedString("relatives", "Relatives"));
			cell.setCellStyle(bigStyle);
		}
		
		int iCell = 0;
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
			cell.setCellValue(this.iwrb.getLocalizedString("register_date", "Register date"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("application.payer_personal_id", "Payer personal ID"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("application.payer_name", "Payer name"));
			cell.setCellStyle(style);
			cell = row.createCell(iCell++);
			cell.setCellValue(this.iwrb.getLocalizedString("application.reference_number", "Reference number"));
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

		/*
		 * Auto-size
		 */
		HSSFRow headerRow = sheet.getRow(sheet.getLastRowNum());
		short numberOfHeaderColumns = headerRow.getLastCellNum();
		for (short columnIndex = 0; columnIndex < numberOfHeaderColumns; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}
		
		User user = null;
		User owner = null;
		Address address = null;
		PostalCode postalCode = null;
		Phone phone = null;
		CourseChoice choice = null;
		CourseApplication application = null;

		Iterator<CourseChoice> iter = choices.iterator();
		while (iter.hasNext()) {
			row = sheet.createRow(cellRow++);
			choice = iter.next();
			
			/* 
			 * User name 
			 */
			user = choice.getUser();
			HSSFCell userName = row.createCell(0);
			if (user != null) {
				userName.setCellValue(user.getName());
			} else {
				userName.setCellValue(CoreConstants.MINUS);
			}

			/* 
			 * User personal id 
			 */
			HSSFCell personalId = row.createCell(1);
			if (user != null) {
				personalId.setCellValue(PersonalIDFormatter.format(
						user.getPersonalID(), this.locale));
			} else {
				personalId.setCellValue(CoreConstants.MINUS);
			}

			/* 
			 * Address 
			 */
			address = this.userBusiness.getUsersMainAddress(user);
			HSSFCell addressCell = row.createCell(2);
			if (address != null) {
				addressCell.setCellValue(address.getStreetAddress());
			} else {
				addressCell.setCellValue(CoreConstants.MINUS);
			}

			/* 
			 * Postal code 
			 */
			if (address != null) {
				postalCode = address.getPostalCode();
			}

			HSSFCell postalCodeCell = row.createCell(3);
			if (postalCode != null) {
				String postalAddress = postalCode.getPostalCode();
				if (!StringUtil.isEmpty(postalAddress)) {
					postalAddress = postalAddress + CoreConstants.SPACE + postalCode.getName();
				} else {
					postalAddress = postalCode.getName();
				}

				if (!StringUtil.isEmpty(postalAddress)) {
					postalCodeCell.setCellValue(postalAddress);
				} else {
					postalCodeCell.setCellValue(CoreConstants.MINUS);
				}
			} else {
				postalCodeCell.setCellValue(CoreConstants.MINUS);
			}

			/*
			 * Phone
			 */
			HSSFCell phoneCell = row.createCell(4);
			phone = this.userBusiness.getChildHomePhone(user);
			if (phone != null && !StringUtil.isEmpty(phone.getNumber())) {
				phoneCell.setCellValue(phone.getNumber());
			} else {
				phoneCell.setCellValue(CoreConstants.MINUS);
			}

			application = choice.getApplication();
			owner = application.getOwner();
			Child child = this.userBusiness.getMemberFamilyLogic().getChild(user);
			if (showAll) {
				/*
				 * Growth deviation
				 */
				Boolean hasGrowthDeviation = child.hasGrowthDeviation(
						CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
				if (hasGrowthDeviation == null) {
					hasGrowthDeviation = child.hasGrowthDeviation(
							CourseConstants.COURSE_PREFIX);
				}

				HSSFCell growthDeviationCell = row.createCell(5);
				if (hasGrowthDeviation != null && hasGrowthDeviation.booleanValue()) {
					growthDeviationCell.setCellValue(
							this.iwrb.getLocalizedString("yes", "Yes"));
				} else {
					growthDeviationCell.setCellValue(
							this.iwrb.getLocalizedString("no", "No"));
				}
	
				/*
				 * Allergies
				 */
				Boolean hasAllergies = child.hasAllergies(
						CourseConstants.COURSE_PREFIX + owner.getPrimaryKey());
				if (hasAllergies == null) {
					hasAllergies = child.hasAllergies(CourseConstants.COURSE_PREFIX);
				}
				
				if (hasAllergies != null && hasAllergies.booleanValue()) {
					row.createCell(6).setCellValue(this.iwrb.getLocalizedString("yes", "Yes"));
				} else {
					row.createCell(6).setCellValue(this.iwrb.getLocalizedString("no", "No"));
				}
	
				/*
				 * Other information
				 */
				HSSFCell otherInfo = row.createCell(7);
				if (child.getOtherInformation(CourseConstants.COURSE_PREFIX + owner.getPrimaryKey()) != null) {
					otherInfo.setCellValue(child.getOtherInformation(
							CourseConstants.COURSE_PREFIX + owner.getPrimaryKey()));
				} else if (child.getOtherInformation(CourseConstants.COURSE_PREFIX) != null) {
					otherInfo.setCellValue(child.getOtherInformation(
							CourseConstants.COURSE_PREFIX));
				} else {
					otherInfo.setCellValue(CoreConstants.MINUS);
				}
	
				/*
				 * Relations
				 */
				iCell = 8;	
				Collection<Custodian> custodians = new ArrayList<Custodian>();
				try {
					custodians = child.getCustodians();
				} catch(Exception e) {}

				Custodian extraCustodian = child.getExtraCustodian();
				if (extraCustodian != null) {
					custodians.add(extraCustodian);
				}
	
				Iterator<Custodian> iterator = custodians.iterator();
				while (iterator.hasNext()) {
					Custodian element = iterator.next();
					address = this.userBusiness.getUsersMainAddress(element);
					Phone work = null;
					Phone mobile = null;
					Email email = null;
					String relation = this.iwrb.getLocalizedString("relation." + child.getRelation(element), "relation." + child.getRelation(element));
					String maritalStatus = this.iwrb.getLocalizedString("marital_status." + element.getMaritalStatus(), "marital_status." + element.getMaritalStatus());
	
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
	
					row.createCell(iCell++).setCellValue(relation);
					row.createCell(iCell++).setCellValue(element.getName());
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
					String relation = this.iwrb.getLocalizedString("relation." + element.getRelation(), "relation." + element.getRelation());
	
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
				
				String payerName = null;
				String payerPersonalID = null;
				if (application.getPayerPersonalID() != null) {
					payerPersonalID = PersonalIDFormatter.format(application.getPayerPersonalID(), locale);
					payerName = application.getPayerName();
				}
				else {
					User payer = application.getOwner();
					payerName = new Name(payer.getFirstName(), payer.getMiddleName(), payer.getLastName()).getName(locale);
					payerPersonalID = PersonalIDFormatter.format(payer.getPersonalID(), locale);
				}

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
				row.createCell(iCell++).setCellValue(new IWTimestamp(application.getCreated()).getLocaleDate(locale, IWTimestamp.SHORT));
				row.createCell(iCell++).setCellValue(payerPersonalID);
				row.createCell(iCell++).setCellValue(payerName);
				if (application.getReferenceNumber() != null) {
					row.createCell(iCell++).setCellValue(application.getReferenceNumber());
				}
				else {
					iCell++;
				}
			}
		}

		/*
		 * Auto-sizing columns
		 */
		HSSFRow lastRow = sheet.getRow(sheet.getLastRowNum());
		short numberOfColumns = lastRow.getLastCellNum();
		for (short columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}

		wb.write(mos);
		buffer.setMimeType(MimeTypeUtil.MIME_TYPE_EXCEL_2);
		return buffer;
	}

	public MemoryFileBuffer writeAccountingXLS(IWContext iwc, 
			Collection<CourseChoice> choices) throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(StringHandler.shortenToLength(this.courseName, 30));
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
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(this.courseName);
		cell.setCellStyle(bigStyle);
		
		row = sheet.createRow(cellRow++);

		int iCell = 0;
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
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("price", "Price"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("payer_personal_id", "Payer personal ID"));
		cell.setCellStyle(style);
		cell = row.createCell(iCell++);
		cell.setCellValue(this.iwrb.getLocalizedString("payer_name", "Payer name"));
		cell.setCellStyle(style);

		User user;
		Address address;
		PostalCode postalCode = null;
		Phone phone;
		CourseChoice choice;
		CourseApplication application;

		Iterator<CourseChoice> iter = choices.iterator();
		while (iter.hasNext()) {
			row = sheet.createRow(cellRow++);
			choice = iter.next();
			application = choice.getApplication();
			user = choice.getUser();
			address = this.userBusiness.getUsersMainAddress(user);
			if (address != null) {
				postalCode = address.getPostalCode();
			}
			phone = this.userBusiness.getChildHomePhone(user);
			Course course = choice.getCourse();
			User owner = application.getOwner();
			if (application.getPayerPersonalID() != null) {
				User payer = getUserBusiness(iwc).getUser(application.getPayerPersonalID());
				if (payer != null) {
					owner = payer;
				}
			}

			application = choice.getApplication();
			float userPrice = 0;
			if (choice.isNoPayment()) {
				userPrice = 0;
			} else {
				Map<User, Collection<ApplicationHolder>> applicationMap = getCourseBusiness(iwc)
						.getApplicationMap(application, new Boolean(false));
				SortedSet<PriceHolder> prices = getCourseBusiness(iwc)
						.calculatePrices(applicationMap);
				Map<User, PriceHolder> discounts = getCourseBusiness(iwc)
						.getDiscounts(prices, applicationMap);
				CoursePrice price = course.getPrice();
				
				float coursePrice = (price != null ? price.getPrice() : course.getCoursePrice()) * (1 - ((PriceHolder) discounts.get(user)).getDiscount());			
				
				float carePrice = 0;
				if (choice.getDayCare() == CourseConstants.DAY_CARE_PRE) {
					carePrice = price.getPreCarePrice();
				} else if (choice.getDayCare() == CourseConstants.DAY_CARE_POST) {
					carePrice = price.getPostCarePrice();
				} else if (choice.getDayCare() == CourseConstants.DAY_CARE_PRE_AND_POST) {
					carePrice = price.getPreCarePrice() + price.getPostCarePrice();
				}
				carePrice = carePrice * (1 - ((PriceHolder) discounts.get(user)).getDiscount());
				
				userPrice = carePrice + coursePrice;
			}
			
			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
			row.createCell(0).setCellValue(name.getName(this.locale, true));
			row.createCell(1).setCellValue(PersonalIDFormatter.format(user.getPersonalID(), this.locale));
			if (address != null) {
				row.createCell(2).setCellValue(address.getStreetAddress());
				if (postalCode != null) {
					row.createCell(3).setCellValue(postalCode.getPostalAddress());
				}
			}
			if (phone != null) {
				row.createCell(4).setCellValue(phone.getNumber());
			}
			row.createCell(5).setCellValue(userPrice);
			
			if (owner != null) {
				row.createCell(6).setCellValue(owner.getPersonalID());
				row.createCell(7).setCellValue(new Name(user.getFirstName(), user.getMiddleName(), user.getLastName()).getName(this.locale, true));
			}
		}

		/*
		 * Auto-sizing columns
		 */
		HSSFRow lastRow = sheet.getRow(sheet.getLastRowNum());
		short numberOfColumns = lastRow.getLastCellNum();
		for (short columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}

		wb.write(mos);
		buffer.setMimeType(MimeTypeUtil.MIME_TYPE_EXCEL_2);
		return buffer;
	}

	private CourseBusiness getCourseBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CourseBusiness) IBOLookup.getServiceInstance(iwc, CourseBusiness.class);
	}

	private CitizenBusiness getUserBusiness(IWApplicationContext iwc) throws RemoteException {
		return (CitizenBusiness) IBOLookup.getServiceInstance(iwc, CitizenBusiness.class);
	}
}