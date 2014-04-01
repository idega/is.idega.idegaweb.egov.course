package is.idega.idegaweb.egov.course.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;

import com.idega.data.IDOQuery;
import com.idega.util.StringUtil;

public class ChildCourseBMPBean extends CourseBMPBean implements ChildCourse {

	private static final long serialVersionUID = 7425610935661180112L;

	@Override
	public void initializeAttributes() {
		super.initializeAttributes();
	}

	@Override
	public Course getParentCourse() {
		return (Course) getColumnValue(PARENT_COURSE_ID);
	}

	@Override
	public void setParentCourse(Course parentCourse) {
		setColumn(PARENT_COURSE_ID, parentCourse);
	}

	/**
	 * 
	 * @param parentCourse is {@link Course} which has {@link ChildCourse}s,
	 * not <code>null</code>;
	 * @param courseProviderId is {@link CourseProvider#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @return {@link Collection} of {@link Course#getPrimaryKey()} or
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<String> ejbFindChildCourses(
			Course parentCourse,
			String courseProviderId) {
		if (parentCourse == null) {
			return Collections.emptyList();
		}
		
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(
				PARENT_COURSE_ID, parentCourse.getPrimaryKey().toString());
		if (!StringUtil.isEmpty(courseProviderId)) {
			query.appendAndEquals(COLUMN_PROVIDER, courseProviderId);
		}

		try {
			return idoFindPKsByQuery(query);
		} catch (FinderException e) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, 
					"Failed to get primary keys for " + getInterfaceClass().getSimpleName() + 
					"'s by query: '" + query + "' cause of: ", e);
		}

		return Collections.emptyList();
	}

	//	Setters
	@Override
	public void setPrivate(boolean isPrivate) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setPrivate(isPrivate);
		}
	}

	@Override
	public void setName(String name) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setName(name);
		}
	}

	@Override
	public void setUser(String user) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setUser(user);
		}
	}

	@Override
	public void setDescription(String description) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setDescription(description);
		}
	}

	@Override
	public void setCourseType(CourseType courseType) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setCourseType(courseType);
		}
	}

	@Override
	public void setPrice(CoursePrice price) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setPrice(price);
		}
	}

	@Override
	public void setCoursePrice(float price) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setCoursePrice(price);
		}
	}

	@Override
	public void setCourseCost(float cost) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setCourseCost(cost);
		}
	}

	@Override
	public void setAccountingKey(String key) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setAccountingKey(key);
		}
	}

	@Override
	public void setStartDate(Timestamp startDate) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setStartDate(startDate);
		}
	}

	@Override
	public void setEndDate(Timestamp endDate) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setEndDate(endDate);
		}
	}

	@Override
	public void setRegistrationEnd(Timestamp registrationEnd) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setRegistrationEnd(registrationEnd);
		}
	}

	@Override
	public void setBirthyearFrom(int from) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setBirthyearFrom(from);
		}
	}

	@Override
	public void setBirthyearTo(int to) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setBirthyearTo(to);
		}
	}

	@Override
	public void setMax(int max) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setMax(max);
		}
	}

	@Override
	public void setOpenForRegistration(boolean openForRegistration) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setOpenForRegistration(openForRegistration);
		}
	}

	@Override
	public void setHasPreCare(boolean hasPreCare) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setHasPreCare(hasPreCare);
		}
	}

	@Override
	public void setHasPostCare(boolean hasPostCare) {
		Course parentCourse = getParentCourse();
		if (parentCourse != null) {
			parentCourse.setHasPostCare(hasPostCare);
		}
	}
}