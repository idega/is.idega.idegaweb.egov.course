package is.idega.idegaweb.egov.course;

import is.idega.idegaweb.egov.accounting.business.AccountingBusinessManager;
import is.idega.idegaweb.egov.course.business.CourseBusiness;

import com.idega.block.process.business.CaseCodeManager;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;

public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		CaseCodeManager.getInstance().addCaseBusinessForCode(CourseConstants.CASE_CODE_KEY, CourseBusiness.class);
		AccountingBusinessManager.getInstance().addCaseBusinessForCode(CourseConstants.CASE_CODE_KEY, CourseBusiness.class);

		//fixCourseApplicationReferenceStrings();
	}

	public void stop(IWBundle starterBundle) {
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