/*
 * $Id$ Created on Apr 20, 2007
 * 
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to license terms.
 */
package is.idega.idegaweb.egov.course.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

public class CourseApplicationOverviewWindow extends Window {

	public CourseApplicationOverviewWindow() {
		setWidth(800);
		setHeight(600);
		setResizable(true);
		setScrollbar(true);
	}

	public void main(IWContext iwc) throws Exception {
		add(new CourseApplicationOverview(true));
	}
}