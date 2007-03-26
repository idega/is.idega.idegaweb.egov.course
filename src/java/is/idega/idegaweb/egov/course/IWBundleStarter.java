package is.idega.idegaweb.egov.course;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.ExternalLink;
import com.idega.idegaweb.include.GlobalIncludeManager;

public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		GlobalIncludeManager.getInstance().addBundleStyleSheet("is.idega.idegaweb.egov.course", "/style/course.css", ExternalLink.MEDIA_SCREEN);
	}

	public void stop(IWBundle starterBundle) {
	}

}
