/**
 * @(#)SocialServiceCenterEntityHome.java    1.0.0 10:35:49 AM
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.PostalCode;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * <p>Data access object for {@link SocialServiceCenterEntity}</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Dec 10, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public interface SocialServiceCenterEntityHome extends CourseProviderHome {

	/**
	 * 
	 * <p>Creates/updates entities</p>
	 * @param groupId is {@link Group#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param groupName is {@link Group#getName()}, not <code>null</code> if
	 * new entity is created and groupId is skipped;
	 * @param groupDescription is {@link Group#getDescription()}, 
	 * skipped if <code>null</code>;
	 * @param groupCity is {@link PostalCode#getName()}, 
	 * skipped if <code>null</code>;
	 * @param postalCodes is array of {@link PostalCode#getPostalCode()}, 
	 * not <code>null</code
	 * @param roleNames BPM process roles to be accessed by this service centers,
	 * skipped if <code>null</code>;
	 * @param providerId TODO
	 * @param organizationNumber TODO
	 * @param address is {@link Address#getStreetAddress()}, 
	 * skipped if <code>null</code>
	 * @param phone is {@link Phone#getNumber()}, skipped if <code>null</code>;
	 * @param webPageAddress for example: "http://www.google.com", 
	 * skipped if null;
	 * @param communeId is {@link Commune#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param courseProviderAreaId {@link CourseProviderArea#getPrimaryKey()},
	 * skipped if <code>null</code>;
	 * @return updated/created entities or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	ArrayList<SocialServiceCenterEntity> update(
			String groupId, 
			String groupName,
			String groupDescription, 
			String groupCity, 
			String[] postalCodes, 
			String[] roleNames, 
			String providerId, 
			String organizationNumber, 
			String address, 
			String phone, 
			String webPageAddress, 
			String communeId, 
			String courseProviderAreaId);

	/**
	 * 
	 * @param groupId is {@link Group#getPrimaryKey()}, not <code>null</code>;
	 * @return entity, connected to center or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<? extends SocialServiceCenterEntity> findByGroupId(String groupId);

	/**
	 * 
	 * <p>Removes {@link SocialServiceCenterEntity} connected to 
	 * given {@link Group}.</p>
	 * @param groupId is {@link Group#getPrimaryKey()}, not <code>null</code>;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	void removeByGroupId(String groupId);

	/**
	 * 
	 * @param postalCodes is {@link Collection} of 
	 * {@link PostalCode#getPostalCode()}, to search by, not <code>null</code>;
	 * @return {@link User}s of {@link Group}s connected to given
	 * {@link PostalCode} or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	List<User> getUsersByPostalCodes(Collection<String> postalCodes);

	/**
	 * 
	 * @param postalCodes to search by, not <code>null</code>;
	 * @return {@link User}s of {@link Group}s connected to given
	 * {@link PostalCode} or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	List<User> getHandlers(Collection<PostalCode> postalCodes);

	/**
	 * 
	 * @param postalCodes is {@link Collection} of {@link PostalCode#getPostalCode()} 
	 * to search by, not <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()};
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	List<SocialServiceCenterEntity> findByPostalCode(Collection<String> postalCodes);

	/**
	 * 
	 * @param postalCodes to search by, not <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()};
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	List<SocialServiceCenterEntity> findByPostalCodeEntity(Collection<PostalCode> postalCodes);

	/**
	 * <p>Note: users are taken from 
	 * {@link SocialServiceCenterEntity#getManagingGroup()}.</p>
	 * @param serviceCenter to get {@link User}s for, not <code>null</code>;
	 * @return {@link User}s of {@link SocialServiceCenterEntity} or 
	 * {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	List<User> getHandlers(SocialServiceCenterEntity serviceCenter);

	/**
	 * 
	 * @param group is {@link SocialServiceCenterEntity#getManagingGroup()} to
	 * take {@link User}s from, not <code>null</code>;
	 * @return {@link User}s or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	List<User> getHandlers(Group group);

	/**
	 * 
	 * @param handlers to search {@link SocialServiceCenterEntity}s by, 
	 * not <code>null</code>;
	 * @return entities or {@link Collections#emptyList()} on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	List<SocialServiceCenterEntity> findByHandlerEntities(
			Collection<? extends SocialServiceCenterHandlerEntity> handlers);

	/**
	 * 
	 * @param users are handlers of {@link SocialServiceCenterEntity}, not 
	 * <code>null</code>;
	 * @return entities by criteria or {@link Collections#emptyList()} on 
	 * failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	List<SocialServiceCenterEntity> findByHandlers(Collection<User> users);

	/**
	 * 
	 * <p>Creates/updates existing social service centers.</p>
	 * @param socialServiceCenter if <code>null</code>, then new entity 
	 * will be created;
	 * @param group of center, skipped if <code>null</code> and 
	 * {@link SocialServiceCenterEntity} not <code>null</code>;
	 * @param postalCodes
	 * @param providerId TODO
	 * @param organizationNumber TODO
	 * @param address is {@link Address#getStreetAddress()}, 
	 * skipped if <code>null</code>
	 * @param phone is {@link Phone#getNumber()}, skipped if <code>null</code>;
	 * @param webPageAddress for example: "http://www.google.com", 
	 * skipped if null;
	 * @param communeId is {@link Commune#getPrimaryKey()}, 
	 * skipped if <code>null</code>;
	 * @param courseProviderAreaId {@link CourseProviderArea#getPrimaryKey()},
	 * skipped if <code>null</code>;
	 * @return created/updated entities or {@link Collections#emptyList()}
	 * on failure;
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	Collection<? extends SocialServiceCenterEntity> update(
			SocialServiceCenterEntity socialServiceCenter, 
			Group group,
			Collection<PostalCode> postalCodes,
			String providerId, 
			String organizationNumber, 
			String address, 
			String phone, 
			String webPageAddress, 
			String communeId, 
			String courseProviderAreaId);
}
