package is.idega.idegaweb.egov.course;

import is.idega.idegaweb.egov.accounting.business.AccountingBusinessManager;
import is.idega.idegaweb.egov.course.business.CourseBusiness;

import com.idega.block.process.business.CaseCodeManager;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.ExternalLink;
import com.idega.idegaweb.include.GlobalIncludeManager;

public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		GlobalIncludeManager.getInstance().addBundleStyleSheet("is.idega.idegaweb.egov.course", "/style/course.css", ExternalLink.MEDIA_SCREEN);
		CaseCodeManager.getInstance().addCaseBusinessForCode(CourseConstants.CASE_CODE_KEY, CourseBusiness.class);
		AccountingBusinessManager.getInstance().addCaseBusinessForCode(CourseConstants.CASE_CODE_KEY, CourseBusiness.class);
	}

	public void stop(IWBundle starterBundle) {
	}

}
