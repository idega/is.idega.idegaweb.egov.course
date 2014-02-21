/**
 * @(#)CourseProviderHome.java    1.0.0 3:22:16 PM
 *
 * Idega Software hf. Source Code Licence Agreement x
 *
 * This agreement, made this 10th of February 2006 by and between
 * Idega Software hf., a business formed and operating under laws
 * of Iceland, having its principal place of business in Reykjavik,
 * Iceland, hereinafter after referred to as "Manufacturer" and Agura
 * IT hereinafter referred to as "Licensee".
 * 1.  License Grant: Upon completion of this agreement, the source
 *     code that may be made available according to the documentation for
 *     a particular software product (Software) from Manufacturer
 *     (Source Code) shall be provided to Licensee, provided that
 *     (1) funds have been received for payment of the License for Software and
 *     (2) the appropriate License has been purchased as stated in the
 *     documentation for Software. As used in this License Agreement,
 *     Licensee shall also mean the individual using or installing
 *     the source code together with any individual or entity, including
 *     but not limited to your employer, on whose behalf you are acting
 *     in using or installing the Source Code. By completing this agreement,
 *     Licensee agrees to be bound by the terms and conditions of this Source
 *     Code License Agreement. This Source Code License Agreement shall
 *     be an extension of the Software License Agreement for the associated
 *     product. No additional amendment or modification shall be made
 *     to this Agreement except in writing signed by Licensee and
 *     Manufacturer. This Agreement is effective indefinitely and once
 *     completed, cannot be terminated. Manufacturer hereby grants to
 *     Licensee a non-transferable, worldwide license during the term of
 *     this Agreement to use the Source Code for the associated product
 *     purchased. In the event the Software License Agreement to the
 *     associated product is terminated; (1) Licensee's rights to use
 *     the Source Code are revoked and (2) Licensee shall destroy all
 *     copies of the Source Code including any Source Code used in
 *     Licensee's applications.
 * 2.  License Limitations
 *     2.1 Licensee may not resell, rent, lease or distribute the
 *         Source Code alone, it shall only be distributed as a
 *         compiled component of an application.
 *     2.2 Licensee shall protect and keep secure all Source Code
 *         provided by this this Source Code License Agreement.
 *         All Source Code provided by this Agreement that is used
 *         with an application that is distributed or accessible outside
 *         Licensee's organization (including use from the Internet),
 *         must be protected to the extent that it cannot be easily
 *         extracted or decompiled.
 *     2.3 The Licensee shall not resell, rent, lease or distribute
 *         the products created from the Source Code in any way that
 *         would compete with Idega Software.
 *     2.4 Manufacturer's copyright notices may not be removed from
 *         the Source Code.
 *     2.5 All modifications on the source code by Licencee must
 *         be submitted to or provided to Manufacturer.
 * 3.  Copyright: Manufacturer's source code is copyrighted and contains
 *     proprietary information. Licensee shall not distribute or
 *     reveal the Source Code to anyone other than the software
 *     developers of Licensee's organization. Licensee may be held
 *     legally responsible for any infringement of intellectual property
 *     rights that is caused or encouraged by Licensee's failure to abide
 *     by the terms of this Agreement. Licensee may make copies of the
 *     Source Code provided the copyright and trademark notices are
 *     reproduced in their entirety on the copy. Manufacturer reserves
 *     all rights not specifically granted to Licensee.
 *
 * 4.  Warranty & Risks: Although efforts have been made to assure that the
 *     Source Code is correct, reliable, date compliant, and technically
 *     accurate, the Source Code is licensed to Licensee as is and without
 *     warranties as to performance of merchantability, fitness for a
 *     particular purpose or use, or any other warranties whether
 *     expressed or implied. Licensee's organization and all users
 *     of the source code assume all risks when using it. The manufacturers,
 *     distributors and resellers of the Source Code shall not be liable
 *     for any consequential, incidental, punitive or special damages
 *     arising out of the use of or inability to use the source code or
 *     the provision of or failure to provide support services, even if we
 *     have been advised of the possibility of such damages. In any case,
 *     the entire liability under any provision of this agreement shall be
 *     limited to the greater of the amount actually paid by Licensee for the
 *     Software or 5.00 USD. No returns will be provided for the associated
 *     License that was purchased to become eligible to receive the Source
 *     Code after Licensee receives the source code.
 */
package is.idega.idegaweb.egov.course.data;

import is.idega.idegaweb.egov.course.event.CourseProviderUpdatedEvent;

import java.util.Collection;
import java.util.Collections;

import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOEntity;
import com.idega.data.IDOHome;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * <p>Data access object for {@link CourseProvider}</p>
 * <p>You can report about problems to:
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 23, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 * @param <T>
 */
public interface CourseProviderHome extends IDOHome {

	/**
	 *
	 * <p>Creates or updates entity in data source.</p>
	 * @param primaryKey is {@link CourseProvider#getPrimaryKey()}, entity
	 * by given id will be updated, if not <code>null</code>;
	 * @param name is {@link CourseProvider#getName()}, not <code>null</code>;
	 * @param providerId
	 * @param communeId is {@link Commune#getPrimaryKey()},
	 * skipped if <code>null</code>;
	 * @param phone is {@link Phone#getNumber()}, skipped if <code>null</code>;
	 * @param webPage
	 * @param info
	 * @param organizationNumber
	 * @param zipArea is {@link PostalCode#getName()},
	 * skipped if <code>null</code>;
	 * @param zipCode is {@link PostalCode#getPostalCode()},
	 * skipped if <code>null</code>;
	 * @param address is {@link Address#getStreetAddress()},
	 * skipped if <code>null</code>;
	 * @param courseProviderArea id {@link CourseProviderArea#getPrimaryKey()},
	 * skipped if <code>null</code>;
	 * @param hasPreCare
	 * @param hasPostCare
	 * @param notify <code>true</code> if it is needed to fire
	 * {@link CourseProviderUpdatedEvent}, <code>false</code> otherwise;
	 * @return created/updated {@link CourseProvider} or
	 * <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public CourseProvider update(
			String primaryKey,
			String name,
			String providerId,
			String communeId,
			String phone,
			String webPage,
			String info,
			String organizationNumber,
			String zipArea,
			String zipCode,
			String address,
			String courseProviderArea,
			Boolean hasPreCare,
			Boolean hasPostCare, boolean notify
			);

	/**
	 *
	 * @return all {@link CourseProvider}s from data source or
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public <P extends CourseProvider> Collection<P> find();

	/**
	 * 
	 * <p>Tries searching in subclasses.</p>
	 * @return all {@link CourseProvider}s from data source or
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public <P extends CourseProvider> Collection<P> findAllRecursively();

	/**
	 * 
	 * @param primaryKey is {@link CourseProvider#getPrimaryKey()},
	 * not <code>null</code>;
	 * @return entity by primary key or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	<T extends IDOEntity> T findByPrimaryKey(String primaryKey);
	
	/**
	 *
	 * <p>Performs deep search in types and extending subtypes</p>
	 * @param primaryKey is {@link CourseProvider#getPrimaryKey()},
	 * not <code>null</code>;
	 * @return entity by primary key or <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 * @param <CP>
	 */
	<T extends CourseProvider> T findByPrimaryKeyRecursively(String primaryKey);

	/**
	 *
	 * <p>Searches in sub-types also</p>
	 * @param primaryKeys is {@link Collection} of {@link CourseProvider#getPrimaryKey()},
	 * not <code>null</code>;
	 * @return entities by primary keys or {@link Collections#emptyList()} on
	 * failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public <T extends CourseProvider> Collection<T> find(Collection<String> primaryKeys);

	/**
	 *
	 * @param primaryKeys is {@link Collection} of {@link CourseProvider#getPrimaryKey()},
	 * not <code>null</code>;
	 * @return entities by primary keys or {@link Collections#emptyList()} on
	 * failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<? extends CourseProvider> findByPrimaryKeys(
			Collection<Object> primaryKeys);

	/**
	 *
	 * <p>Creates or updates entity in data source.</p>
	 * @param provider to update, if <code>null</code> new one will be created;
	 * @param group
	 * @param postalCode
	 * @param providerArea
	 * @param notify <code>true</code> if it is needed to fire
	 * {@link CourseProviderUpdatedEvent}, <code>false</code> otherwise;
	 * @return created/updated {@link CourseProvider} or
	 * <code>null</code> on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public CourseProvider update(CourseProvider provider, Group group,
			PostalCode postalCode, CourseProviderArea providerArea,
			boolean notify);

	/**
	 *
	 * @param types to search by, not <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public <P extends CourseProvider> Collection<P> findByType(Collection<? extends CourseProviderType> types);

	/**
	 *
	 * @param areas to search by, not <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public Collection<? extends CourseProvider> findByArea(
			Collection<? extends CourseProviderArea> areas);

	/**
	 *
	 * @param areas to search by, not <code>null</code>;
	 * @param types to search by, not <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public <P extends CourseProvider> Collection<P> find(
			Collection<? extends CourseProviderArea> areas,
			Collection<? extends CourseProviderType> types);

	/**
	 *
	 * <p>Removes entity and all its relations!</p>
	 * @param provider to remove, not <code>null</code>;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public void remove(CourseProvider provider);

	/**
	 *
	 * <p>Removes entity and all its relations!</p
	 * @param primaryKey is {@link CourseProvider#getPrimaryKey()} of
	 * entity to remove, not <code>null</code>;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public void remove(String primaryKey);

	/**
	 * 
	 * @param handlers to search {@link SocialServiceCenterEntity}s by, 
	 * not <code>null</code>;
	 * @return entities or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	<T extends CourseProvider> Collection<T> findByHandlerEntities(
			Collection<? extends CourseProviderUser> handlers);

	/**
	 * 
	 * @param users are handlers of {@link SocialServiceCenterEntity}, not 
	 * <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()} on 
	 * failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	<T extends CourseProvider> Collection<T> findByHandlers(Collection<User> users);

	/**
	 * 
	 * @param schoolGroups to search by, not <code>null</code>;
	 * @return schools which are controlled by given group or empty list on 
	 * failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public <T extends CourseProvider> Collection<T> findAllBySchoolGroup(
			Collection<Group> schoolGroups);
}