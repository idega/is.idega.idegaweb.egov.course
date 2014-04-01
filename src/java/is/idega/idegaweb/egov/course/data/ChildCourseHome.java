package is.idega.idegaweb.egov.course.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;

import com.idega.user.data.Group;
import com.idega.user.data.User;

public interface ChildCourseHome extends CourseHome {

	@Override
	public ChildCourse create() throws CreateException;

	/**
	 * 
	 * @param parentCourse is {@link Course} which has {@link ChildCourse}s,
	 * not <code>null</code>;
	 * @return {@link Collection} of entities or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<ChildCourse> findChildCourses(Course parentCourse);

	/**
	 * 
	 * @param parentCourse is {@link Course} which has {@link ChildCourse}s,
	 * not <code>null</code>;
	 * @param courseProviderId is {@link CourseProvider#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @return {@link Collection} of entities or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<ChildCourse> findChildCourses(
			Course parentCourse, String courseProviderId);

	/**
	 * 
	 * @param parentCourse is original {@link Course} to update and the
	 * parent of {@link ChildCourse}.
	 * New one will be created if <code>null</code>;
	 * @param name is {@link Course#getName()}, skipped if <code>null</code>;
	 * @param courseHandlerPersonalId is {@link User#getPersonalID()} of handler of
	 * this {@link Course}, skipped if null;
	 * @param centers is {@link CourseProvider}s to assign for given parent
	 * {@link Course}. New {@link ChildCourse}s will be created for each center,
	 * and old {@link ChildCourse}s will be removed is certain 
	 * {@link CourseProvider} are not given. However, if {@link List}
	 * is empty, then no changes will be applied;
	 * @param courseTypeId is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param endDate when {@link Course} ends, skipped if <code>null</code>;
	 * @param startDate when {@link Course} starts, skipped if <code>null</code>;
	 * @param accountingKey
	 * @param birthYearFrom is floor of age for attendants, 
	 * skipped if <code>null</code>;
	 * @param birthYearTo is ceiling of age for attendants, 
	 * skipped if <code>null</code>;
	 * @param maximumParticipantsNumber is maximum number of attendants, 
	 * skipped if <code>null</code>;
	 * @param registrationEndDate when applications to {@link Course} are no longer
	 * acceptable, skipped if <code>null</code>;
	 * @param accessGroups tells which {@link Group} {@link User}s can
	 * attend/manage course, skipped if <code>null</code>;
	 * @return created/updated {@link ChildCourse} or <code>null</code>
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<ChildCourse> update(
			Course parentCourse, 
			String name,
			String courseHandlerPersonalId, 
			Collection<? extends CourseProvider> centers,
			String courseTypeId, 
			Date startDate, 
			Date endDate,
			String accountingKey, 
			Integer birthYearFrom, 
			Integer birthYearTo,
			Integer maximumParticipantsNumber, 
			Date registrationEndDate,
			ArrayList<Group> accessGroups);

	/**
	 * 
	 * @param childCourse to update, new one will be created if 
	 * <code>null</code>;
	 * @param parentCourse is original {@link Course} to update and the
	 * parent of {@link ChildCourse}.
	 * New one will be created if <code>null</code>;
	 * @param course to update, new one created if <code>null</code>;
	 * @param name is {@link Course#getName()}, skipped if <code>null</code>;
	 * @param courseHandlerPersonalId is {@link User#getPersonalID()} of handler of
	 * this {@link Course}, skipped if null;
	 * @param center to assign, skipped if <code>null</code>;
	 * when {@link Course} should be available for attending, 
	 * skipped if <code>null</code>;
	 * @param courseTypeId is {@link CourseType#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param endDate when {@link Course} ends, skipped if <code>null</code>;
	 * @param startDate when {@link Course} starts, skipped if <code>null</code>;
	 * @param accountingKey
	 * @param birthYearFrom is floor of age for attendants, 
	 * skipped if <code>null</code>;
	 * @param birthYearTo is ceiling of age for attendants, 
	 * skipped if <code>null</code>;
	 * @param maximumParticipantsNumber is maximum number of attendants, 
	 * skipped if <code>null</code>;
	 * @param registrationEndDate when applications to {@link Course} are no longer
	 * acceptable, skipped if <code>null</code>;
	 * @param accessGroups tells which {@link Group} {@link User}s can
	 * attend/manage course, skipped if <code>null</code>;
	 * @return created/updated {@link ChildCourse} or <code>null</code>
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	ChildCourse update(ChildCourse childCourse, Course parentCourse,
			String name, String courseHandlerPersonalId, CourseProvider center,
			String courseTypeId, Date startDate, Date endDate,
			String accountingKey, Integer birthYearFrom, Integer birthYearTo,
			Integer maximumParticipantsNumber, Date registrationEndDate,
			ArrayList<Group> accessGroups);
}