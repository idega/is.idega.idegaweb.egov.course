package is.idega.idegaweb.egov.course;

import is.idega.idegaweb.egov.accounting.business.AccountingBusinessManager;
import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.data.CourseApplication;
import is.idega.idegaweb.egov.course.data.CourseApplicationHome;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.process.business.CaseCodeManager;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.ExternalLink;
import com.idega.idegaweb.include.GlobalIncludeManager;

public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		GlobalIncludeManager.getInstance().addBundleStyleSheet("is.idega.idegaweb.egov.course", "/style/course.css", ExternalLink.MEDIA_SCREEN);
		CaseCodeManager.getInstance().addCaseBusinessForCode(CourseConstants.CASE_CODE_KEY, CourseBusiness.class);
		AccountingBusinessManager.getInstance().addCaseBusinessForCode(CourseConstants.CASE_CODE_KEY, CourseBusiness.class);
		
		fixCourseApplicationReferenceStrings();
	}

	public void stop(IWBundle starterBundle) {
	}
	
	private void fixCourseApplicationReferenceStrings() {
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
						System.out.println("******************************");
					}
				}
			}
			System.out.println("****Course Statup end**************************");
		} catch (IDOLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IDORelationshipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
