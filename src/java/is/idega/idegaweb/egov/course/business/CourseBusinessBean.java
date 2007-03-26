package is.idega.idegaweb.egov.course.business;

import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseHome;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.CoursePriceHome;
import is.idega.idegaweb.egov.course.data.CourseType;
import is.idega.idegaweb.egov.course.data.CourseTypeHome;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORuntimeException;
import com.idega.util.IWTimestamp;

public class CourseBusinessBean extends IBOServiceBean implements CourseBusiness {

	public static final String COURSE_SCHOOL_CATEGORY = "COURSES";
	public static final String CASE_CODE_KEY = "COURSEA";
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.egov.course";
	public void deleteCourseType(Object pk) throws RemoteException {
		CourseType type = null;
		try {
			type = getCourseTypeHome().findByPrimaryKey(new Integer(pk.toString()));
			type.remove();
		}
		catch (javax.ejb.FinderException fe) {
			throw new RemoteException(fe.getMessage());
		} catch (RemoveException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	public void storeCourseType(Object pk, String name, String description, String localizationKey, Object schoolTypePK) throws RemoteException {
		CourseType type = null;
		try {
			if (pk == null) {
				type = getCourseTypeHome().create();
			} else {
				type = getCourseTypeHome().findByPrimaryKey(new Integer(pk.toString()));
			}
		}
		catch (javax.ejb.FinderException fe) {
			throw new RemoteException(fe.getMessage());
		}
		catch (javax.ejb.CreateException ce) {
			throw new RemoteException(ce.getMessage());
		}
		
		if (name != null && !"".equals(name)) {
			type.setName(name);
		}
		
		if (description != null && !"".equals(description)) {
			type.setDescription(description);
		}
		
		if (localizationKey != null && !"".equals(localizationKey)) {
			type.setLocalizationKey(localizationKey);
		}
		
		if (schoolTypePK != null) {
			type.setSchoolTypePK(schoolTypePK);
		}
		
		type.store();
	}
	
	public void storeCourse(Object pk, String name, Object schoolTypePK, Object courseTypePK, Object coursePricePK, IWTimestamp startDate, 
			String accountingKey, int birthYearFrom, int birthYearTo, int maxPer) throws RemoteException {
		Course course = null;
		try {
			if (pk != null) {
				course = getCourseHome().findByPrimaryKey(new Integer(pk.toString()));
			} else {
				course = getCourseHome().create();
			}
		} catch (javax.ejb.FinderException fe) {
			throw new RemoteException(fe.getMessage());
		} catch (javax.ejb.CreateException ce) {
			throw new RemoteException(ce.getMessage());
		}
		
		course.setName(name);
		
		if (schoolTypePK != null) {
			course.setSchoolTypePK(new Integer(schoolTypePK.toString()));
		}
		
		if (courseTypePK != null) {
			course.setCourseTypePK(new Integer(courseTypePK.toString()));
		}
		
		if (coursePricePK != null) {
			course.setPrice(getCoursePrice(new Integer(coursePricePK.toString())));
		}
		
		if (startDate != null) {
			course.setStartDate(startDate.getTimestamp());
		}
		
		if (accountingKey != null) {
			course.setAccountingKey(accountingKey);
		}
		
		if (birthYearFrom > 0) {
			course.setBirthyearFrom(birthYearFrom);
		}
		
		if (birthYearTo > 0) {
			course.setBirthyearTo(birthYearTo);
		}

		if (maxPer > 0) {
			course.setMax(maxPer);
		}
		
		course.store();
	}
	
	public void deleteCoursePrice(Object pk) throws RemoteException {
		CoursePrice price = null;
		try {
			price = getCoursePriceHome().findByPrimaryKey(new Integer(pk.toString()));
			price.remove();
		}
		catch (javax.ejb.FinderException fe) {
			throw new RemoteException(fe.getMessage());
		} catch (RemoveException e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	public void storeCoursePrice(Object pk, String name, int numberOfDays, Timestamp validFrom, Timestamp validTo, int iPrice, String courseTypeID, String schoolTypeID) throws RemoteException {
		CoursePrice price = null;
		try {
			if (pk == null) {
				price = getCoursePriceHome().create();
			} else {
				price = getCoursePriceHome().findByPrimaryKey(new Integer(pk.toString()));
			}
		}
		catch (javax.ejb.FinderException fe) {
			throw new RemoteException(fe.getMessage());
		}
		catch (javax.ejb.CreateException ce) {
			throw new RemoteException(ce.getMessage());
		}
		
		if (name != null && !"".equals(name)) {
			price.setName(name);
		}
		
		if (numberOfDays > -1) {
			price.setNumberOfDays(numberOfDays);
		}
		
		if (validFrom != null) {
			price.setValidFrom(validFrom);
		}
		
		if (validTo != null) {
			price.setValidTo(validTo);
		}
		
		if (iPrice > -1) {
			price.setPrice(iPrice);
		}
		
		if (courseTypeID != null) {
			price.setCourseTypeID(new Integer(courseTypeID));
		}
		
		if (schoolTypeID != null) {
			price.setSchoolTypeID(new Integer(schoolTypeID));
		}
		
		price.store();
	}
	
	public Collection getCourseTypes(Integer schoolTypePK) {
		try {
			Collection coll = getCourseTypeHome().findAllBySchoolType(schoolTypePK);
			return coll;
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map getCourseTypesDWR(int schoolTypePK) {
		Collection coll = getCourseTypes(new Integer(schoolTypePK));
		Map map = new HashMap();
		if (coll != null) {
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				CourseType type = (CourseType) iter.next();
				map.put(type.getPrimaryKey(), type.getName());
			}
		}
		return map;
	}
	
	public Map getCoursePricesDWR(String date, int courseTypePK) {
		Map map = new HashMap();
		CourseType cType = getCourseType(new Integer(courseTypePK));
		SchoolType sType = cType.getSchoolType();
		try {
			Collection prices = getCoursePriceHome().findAll(sType, cType);
			IWTimestamp stamp = null;
			if (prices != null) {
				Iterator iter = prices.iterator();
				stamp = new IWTimestamp(date);
				while (iter.hasNext()) {
					CoursePrice price = (CoursePrice) iter.next();
					IWTimestamp from = new IWTimestamp(price.getValidFrom());
					IWTimestamp to = new IWTimestamp(price.getValidTo());
					if (stamp.isLaterThanOrEquals(from) && stamp.isEarlierThan(to)) {
						map.put(price.getPrimaryKey(), price.getName());
					}
				}
			}
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public Collection getCoursesDWR(int schoolTypePK, int courseTypePK, int birthYear, String country) {
		Integer iST = null;
		Integer iCT = null;
		if (schoolTypePK > -1) {
			iST = new Integer(schoolTypePK);
		}
		if (courseTypePK > -1) {
			iCT = new Integer(courseTypePK);
		}
		Locale locale =  new Locale(country);
		
		Map map = new HashMap();
		Collection courses = getCourses(birthYear, iST, iCT);
		if (courses != null) {
			Iterator iter = courses.iterator();
			while (iter.hasNext()) {
				Course course = (Course) iter.next();
				CourseDWR cDWR = getCourseDWR(locale, course);
				map.put(course.getPrimaryKey(), cDWR);
			}
		}

		return map.values();
	}

	public CourseDWR getCourseDWR(Locale locale, Course course) {
		CourseDWR cDWR = new CourseDWR();
		cDWR.setName(course.getName());
		cDWR.setPk(course.getPrimaryKey().toString());
		cDWR.setFrom(Integer.toString(course.getBirthyearFrom()));
		cDWR.setTo(Integer.toString(course.getBirthyearTo()));
		cDWR.setDescription(course.getDescription());
		
		IWTimestamp from = new IWTimestamp(course.getStartDate());

		String toS = "";
		String dayS = "";
		CoursePrice price = course.getPrice();
		if (from != null && price != null) {
			IWTimestamp toDate = new IWTimestamp(from);
			toDate.addDays(price.getNumberOfDays()-1);
			dayS = Integer.toString(price.getNumberOfDays());
			toS = toDate.getLocaleDate(locale);
			cDWR.setPrice(price.getPrice()+ " ISK");
		}				
		cDWR.setTimeframe(from.getLocaleDate(locale)+" - "+toS);
		cDWR.setDays(dayS);
		return cDWR;
	}

	public Collection getCourses(int birthYear, Integer iST, Integer iCT) {
		Collection courses = null;;
		try {
			courses = getCourseHome().findAll(iST, iCT, birthYear);
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return courses;
	}
	
	public CoursePrice getCoursePrice(Object pk) {
		if (pk != null) {
			try {
				return getCoursePriceHome().findByPrimaryKey(new Integer(pk.toString()));
			}
			catch (javax.ejb.FinderException fe) {
			}
		}
		return null;
	}
	
	
	public Course getCourse(Object pk) {
		if (pk != null) {
			try {
				return getCourseHome().findByPrimaryKey(new Integer(pk.toString()));
			}
			catch (javax.ejb.FinderException fe) {
			}
		}
		return null;
	}
	
	public CourseType getCourseType(Object pk) {
		if (pk != null) {
			try {
				return getCourseTypeHome().findByPrimaryKey(new Integer(pk.toString()));
			}
			catch (javax.ejb.FinderException fe) {
			}
		}
		return null;
	}
	
	public Collection getAllCourses() {
		try {
			return getCourseHome().findAll();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Collection getAllCourseTypes() {
		try {
			return getCourseTypeHome().findAll();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Collection getAllCourseCategories() throws RemoteException {
		Collection schoolTypes = getSchoolBusiness().findAllSchoolTypesInCategory(CourseBusinessBean.COURSE_SCHOOL_CATEGORY);
		return schoolTypes;
	}
	
	public Collection getAllCoursePrices() {
		try {
			return getCoursePriceHome().findAll();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CoursePriceHome getCoursePriceHome() {
		try {
			return (CoursePriceHome) IDOLookup.getHome(CoursePrice.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
	
	public CourseTypeHome getCourseTypeHome() {
		try {
			return (CourseTypeHome) IDOLookup.getHome(CourseType.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
	
	public CourseHome getCourseHome() {
		try {
			return (CourseHome) IDOLookup.getHome(Course.class);
		} catch (IDOLookupException e) {
			throw new IDORuntimeException(e);
		}
	}
	
	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}
