package is.idega.idegaweb.egov.course;

import is.idega.idegaweb.egov.accounting.business.AccountingBusinessManager;
import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseHome;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.process.business.CaseCodeManager;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.ExternalLink;
import com.idega.idegaweb.include.GlobalIncludeManager;

public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		GlobalIncludeManager.getInstance().addBundleStyleSheet("is.idega.idegaweb.egov.course", "/style/course.css", ExternalLink.MEDIA_SCREEN);
		CaseCodeManager.getInstance().addCaseBusinessForCode(CourseConstants.CASE_CODE_KEY, CourseBusiness.class);
		AccountingBusinessManager.getInstance().addCaseBusinessForCode(CourseConstants.CASE_CODE_KEY, CourseBusiness.class);

		//fixCourseApplicationReferenceStrings();
		addCourseNumbers();
	}

	public void stop(IWBundle starterBundle) {
	}

	private void addCourseNumbers() {
		try {
			CourseHome home = (CourseHome) IDOLookup.getHome(Course.class);
			Collection courses = home.findAllWithNoCourseNumber();
			Iterator iterator = courses.iterator();
			while (iterator.hasNext()) {
				Course course = (Course) iterator.next();
				course.setCourseNumber(new Integer(course.getPrimaryKey().toString()).intValue());
				course.store();
			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	/*private void fixCourseApplicationReferenceStrings() {
		try {
			System.out.println("====Course Statup start==========================");
			CourseApplicationHome courseHome = (CourseApplicationHome) IDOLookup.getHome(CourseApplication.class);
			Collection courses = courseHome.findAll();
			if (courses != null) {
				Iterator iter = courses.iterator();
				String key = "TPAuthorIdentifyResponse=";
				int keyLength = key.length();
				while (iter.hasNext()) {
					CourseApplication apppl = (CourseApplication) iter.next();
					String ref = apppl.getReferenceNumber();
					if (ref != null && ref.startsWith("TPPan")) {
						int index = ref.indexOf(key);
						String authCode = ref.substring(index+keyLength);
						System.out.println("==============================");
						System.out.println("found String = "+ref);
						System.out.println("    authCode = "+authCode);
						apppl.setReferenceNumber(authCode);
						apppl.store();
						System.out.println("    updated!");
					}
				}
			}
			System.out.println("****Course Statup end**************************");
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}*/
}