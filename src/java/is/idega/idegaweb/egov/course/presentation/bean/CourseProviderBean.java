/**
 * @(#)CourseProviderBean.java    1.0.0 9:12:15 PM
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
package is.idega.idegaweb.egov.course.presentation.bean;

import is.idega.idegaweb.egov.course.data.CourseProvider;
import is.idega.idegaweb.egov.course.data.CourseProviderArea;
import is.idega.idegaweb.egov.course.data.CourseProviderAreaHome;
import is.idega.idegaweb.egov.course.data.CourseProviderCategory;
import is.idega.idegaweb.egov.course.data.CourseProviderCategoryHome;
import is.idega.idegaweb.egov.course.data.CourseProviderHome;
import is.idega.idegaweb.egov.course.presentation.CourseProviderEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.ejb.FinderException;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogic;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.CoreUtil;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

/**
 * <p>JSF managed bean for {@link CourseProvider}</p>
 * <p>You can report about problems to: 
 * <a href="mailto:martynas@idega.is">Martynas Stakė</a></p>
 *
 * @version 1.0.0 Oct 27, 2013
 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
 */
public class CourseProviderBean {

	public static final String COURSE_PROVIDER_ID = "course_provider_id";
	public static final String COURSE_PROVIDER_CATEGORY_ID = "course_provider_category";
	public static final String SUBMITTED = "submitted";

	private String id = null;

	private String providerId = null;

	private String organizationNumber = null;

	private String name = null;

	private String address = null;

	private String zipCode = null;

	private String zipArea = null;
	
	private String phone = null;

	private String webPage = null;

	private String info = null;

	private String municipalityId = null;

	private String courseProviderAreaId = null;
	
	private CourseProvider courseProvider = null;

	private String editorLink = null;

	public CourseProviderBean() {}
	
	public CourseProviderBean(CourseProvider courseProvider) {
		this.courseProvider = courseProvider;
	}

	public String getId() {
		if (getCourseProvider() == null) {
			return id;
		}

		return getCourseProvider().getPrimaryKey().toString();
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getMunicipalityId() {
		if (getCourseProvider() == null) {
			return municipalityId;
		}

		return String.valueOf(getCourseProvider().getCommuneId());
	}

	public String getMunicipality() {
		if (StringUtil.isEmpty(getMunicipalityId())) {
			return null;
		}

		Commune entity = null;
		try {
			entity = getCommuneHome().findByPrimaryKey(getMunicipalityId());
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, 
					"Failed to get " + Commune.class.getName() + 
					" by id: " + getMunicipalityId());
		}

		if (entity == null) {
			return null;
		}

		return entity.getCommuneName();
	}

	public Map<String, String> getMunicipalities() {
		Collection<Commune> communes = null;
		try {
			communes = getCommuneHome().findAllCommunes();
		} catch (FinderException e) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(
					Level.WARNING, "No communes found!");
		}

		if (ListUtil.isEmpty(communes)) {
			return Collections.emptyMap();
		}

		TreeMap<String, String> communesMap = new TreeMap<String, String>();
		for (Commune commune : communes) {
			communesMap.put(commune.getCommuneName(), commune.getPrimaryKey().toString());
		}

		return communesMap;
	}

	public void setMunicipalityId(String municipality) {
		if (StringUtil.isEmpty(municipality) || municipality.equals("-1")) {
			this.municipalityId = null;
		} else {
			this.municipalityId = municipality;
		}
	}

	public String getCourseProviderAreaId() {
		if (getCourseProvider() == null) {
			return courseProviderAreaId;
		}

		return String.valueOf(getCourseProvider().getSchoolAreaId());
	}

	public void setCourseProviderAreaId(String courseProviderArea) {
		if (StringUtil.isEmpty(courseProviderArea) || courseProviderArea.equals("-1")) {
			this.courseProviderAreaId = null;
		} else {
			this.courseProviderAreaId = courseProviderArea;
		}
	}

	public Map<String, String> getCourseProviderAreas() {
		String categoryId = CoreUtil.getIWContext()
				.getParameter(COURSE_PROVIDER_CATEGORY_ID);

		CourseProviderCategory courseProviderCategory = null;
		if (!StringUtil.isEmpty(categoryId)) {
			courseProviderCategory = getCourseProviderCategoryHome().find(categoryId);
		}

		Collection<CourseProviderArea> areas = getCourseProviderAreaHome()
				.findAllSchoolAreas(courseProviderCategory);
		if (ListUtil.isEmpty(areas)) {
			return Collections.emptyMap();
		}

		TreeMap<String, String> areasMap = new TreeMap<String, String>();
		for (CourseProviderArea area: areas) {
			areasMap.put(area.getName(), area.getPrimaryKey().toString());
		}

		return areasMap;
	}

	public String getProviderId() {
		if (getCourseProvider() == null) {
			return providerId;
		}

		return getCourseProvider().getProviderStringId();
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getOrganizationNumber() {
		if (getCourseProvider() == null) {
			return organizationNumber;
		}

		return getCourseProvider().getOrganizationNumber();
	}

	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	public String getName() {
		if (getCourseProvider() == null) {
			return name;
		}

		return getCourseProvider().getSchoolName();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		if (getCourseProvider() == null) {
			return address;
		}

		return getCourseProvider().getSchoolAddress();
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		if (getCourseProvider() == null) {
			return zipCode;
		}

		return getCourseProvider().getSchoolZipCode();
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getZipArea() {
		if (getCourseProvider() == null) {
			return zipArea;
		}

		return getCourseProvider().getSchoolZipArea();
	}

	public void setZipArea(String zipArea) {
		this.zipArea = zipArea;
	}

	public String getPhone() {
		if (getCourseProvider() == null) {
			return phone;
		}

		return getCourseProvider().getSchoolPhone();
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebPage() {
		if (getCourseProvider() == null) {
			return webPage;
		}

		return getCourseProvider().getSchoolWebPage();
	}

	public void setWebPage(String webPage) {
		this.webPage = webPage;
	}

	public String getInfo() {
		if (getCourseProvider() == null) {
			return info;
		}

		return getCourseProvider().getSchoolInfo();
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public CourseProvider getCourseProvider() {
		if (this.courseProvider == null) {
			String courseProviderId = CoreUtil.getIWContext()
					.getParameter(COURSE_PROVIDER_ID);
			if (!StringUtil.isEmpty(courseProviderId)) {
				this.courseProvider = getCourseProviderHome().find(
						courseProviderId
						);
			}
		}
		
		return this.courseProvider;
	}

	public void setCourseProvider(CourseProvider courseProvider) {
		this.courseProvider = courseProvider;
	}

	public boolean isSubmitted() {
		String submitted = CoreUtil.getIWContext().getParameter(SUBMITTED);
		if (Boolean.TRUE.toString().equals(submitted)) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	/**
	 * 
	 * <p>You are overriding default link to {@link CourseProviderEditor} component</p>
	 * @param editorLink to other component, which should be called on edit, if
	 * <code>null</code>, then default one will be used.
	 * @author <a href="mailto:martynas@idega.is">Martynas Stakė</a>
	 */
	public void setEditorLink(String editorLink) {
		this.editorLink = editorLink;
	}

	public String getEditorLink() {
		/* Overriding default value */
		if (!StringUtil.isEmpty(this.editorLink)) {
			return this.editorLink;
		}
		
		if (getCourseProvider() == null) {
			return null;
		}
		
		List<AdvancedProperty> parameters = new ArrayList<AdvancedProperty>();
		parameters.add(new AdvancedProperty(
				COURSE_PROVIDER_ID, 
				getCourseProvider().getPrimaryKey().toString()));
		return BuilderLogic.getInstance().getUriToObject(
				CourseProviderEditor.class, 
				parameters
				);
	}

	public void submit() {
		// Update
		if (getCourseProviderHome().update(this.id, this.name, this.providerId, 
				this.municipalityId, this.phone, this.webPage, this.info, 
				this.organizationNumber, this.zipArea, this.zipCode, 
				this.address, this.courseProviderAreaId, null, null, true) != null) {
			CoreUtil.getIWContext().setMultipartParameter(
					SUBMITTED, 
					Boolean.TRUE.toString());	
		}
	}

	private CourseProviderCategoryHome courseProviderCategory = null;

	protected CourseProviderCategoryHome getCourseProviderCategoryHome() {
		if (this.courseProviderCategory == null) {
			try {
				this.courseProviderCategory = (CourseProviderCategoryHome) IDOLookup
						.getHome(CourseProviderCategory.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, "Failed to get " + 
								CourseProviderCategoryHome.class.getName() + 
								" cause of: ", e);
			}
		}

		return this.courseProviderCategory;
	}

	private CourseProviderAreaHome courseProviderAreaHome = null;

	protected CourseProviderAreaHome getCourseProviderAreaHome() {
		if (this.courseProviderAreaHome == null) {
			try {
				this.courseProviderAreaHome = (CourseProviderAreaHome) IDOLookup
						.getHome(CourseProviderArea.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CourseProviderAreaHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderAreaHome;
	}

	private CourseProviderHome courseProviderHome = null;

	protected CourseProviderHome getCourseProviderHome() {
		if (this.courseProviderHome == null) {
			try {
				this.courseProviderHome = (CourseProviderHome) IDOLookup.getHome(
						CourseProvider.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CourseProviderHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return this.courseProviderHome;
	}

	private CommuneHome communeHome = null;

	protected CommuneHome getCommuneHome() {
		if (this.communeHome == null) {
			try {
				this.communeHome = (CommuneHome) IDOLookup.getHome(Commune.class);
			} catch (IDOLookupException e) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(
						Level.WARNING, 
						"Failed to get " + CommuneHome.class.getName() + 
						" cause of: ", e);
			}
		}

		return this.communeHome;
	}
}
