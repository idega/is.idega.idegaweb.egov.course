package is.idega.idegaweb.egov.course;

import is.idega.idegaweb.egov.accounting.business.AccountingBusinessManager;
import is.idega.idegaweb.egov.course.business.CourseBusiness;
import is.idega.idegaweb.egov.course.data.Course;
import is.idega.idegaweb.egov.course.data.CourseHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import com.idega.block.process.business.CaseCodeManager;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.GlobalIncludeManager;
import com.idega.idegaweb.include.PageResourceConstants;
import com.idega.util.timer.PastDateException;
import com.idega.util.timer.TimerEntry;
import com.idega.util.timer.TimerListener;
import com.idega.util.timer.TimerManager;

public class IWBundleStarter implements IWBundleStartable {

	private Logger LOGGER = Logger.getLogger(IWBundleStarter.class.getName());

	private static TimerManager tManager = null;
	private static TimerEntry courseTimerEntry = null;
	private static IWApplicationContext iwac = null;

	@Override
	public void start(IWBundle starterBundle) {
		CaseCodeManager.getInstance().addCaseBusinessForCode(CourseConstants.CASE_CODE_KEY, CourseBusiness.class);
		AccountingBusinessManager.getInstance().addCaseBusinessForCode(CourseConstants.CASE_CODE_KEY, CourseBusiness.class);

		GlobalIncludeManager.getInstance().addBundleStyleSheet(CourseConstants.IW_BUNDLE_IDENTIFIER, "/style/course-print.css", PageResourceConstants.MEDIA_PRINT);

		//fixCourseApplicationReferenceStrings();
		addCourseNumbers();
		startMessageDaemon(starterBundle);
	}

	@Override
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

	private void startMessageDaemon(IWBundle iwb) {
		if (tManager == null) {
			tManager = new TimerManager();
		}
		if (iwac == null) {
			iwac = iwb.getApplication().getIWApplicationContext();
		}

		int minute = Integer.parseInt(iwb.getProperty(CourseConstants.PROPERTY_TIMER_MINUTE, String.valueOf(0)));
		int hour = Integer.parseInt(iwb.getProperty(CourseConstants.PROPERTY_TIMER_HOUR, String.valueOf(13)));
		//int dayOfWeek = Integer.parseInt(iwb.getProperty(CourseConstants.PROPERTY_TIMER_DAYOFWEEK, String.valueOf(Calendar.MONDAY)));

		if (courseTimerEntry == null) {
			try {
				courseTimerEntry = tManager.addTimer(minute, hour, -1, -1, -1, -1, new TimerListener() {
					@Override
					public void handleTimer(TimerEntry entry) {
						sendMessages();
					}
				});
			}
			catch (PastDateException e) {
				courseTimerEntry = null;
				e.printStackTrace();
			}
		}
	}

	protected void sendMessages() {
		if (iwac.getApplicationSettings().getBoolean(CourseConstants.PROPERTY_SEND_REMINDERS, false)) {
			try {
				CourseBusiness business = IBOLookup.getServiceInstance(iwac, CourseBusiness.class);
				LOGGER.info("Sending reminders about next courses");
				business.sendNextCoursesMessages();
			}
			catch (IBOLookupException ile) {
				throw new IBORuntimeException(ile);
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		} else {
			LOGGER.warning("Not sending reminders");
		}
	}
}