package is.idega.idegaweb.egov.course.bean;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.util.StringUtil;

@Scope(BeanDefinition.SCOPE_SINGLETON)
@Service(CourseDiscountInformationHolder.BEAN_NAME)
public class CourseDiscountInformationHolder implements Serializable {

	private static final long serialVersionUID = 2624640463794988759L;

	public static final String BEAN_NAME = "courseDiscountInformationHolder";

	private Map<String, Map<String, Boolean>>	discountsApplied = new ConcurrentHashMap<String, Map<String,Boolean>>(),
												discountsDoNotApply = new ConcurrentHashMap<String, Map<String,Boolean>>();
	private Map<String, Map<String, String>>	skippedFamilyMembers = new ConcurrentHashMap<String, Map<String,String>>();

	private void init(String uuid) {
		if (discountsApplied.get(uuid) == null) {
			discountsApplied.put(uuid, new ConcurrentHashMap<String, Boolean>());
		}
		if (discountsDoNotApply.get(uuid) == null) {
			discountsDoNotApply.put(uuid, new ConcurrentHashMap<String, Boolean>());
		}
		if (skippedFamilyMembers.get(uuid) == null) {
			skippedFamilyMembers.put(uuid, new ConcurrentHashMap<String, String>());
		}
	}

	public boolean isDiscountApplied(String uuid, String personalId) {
		init(uuid);

		if (StringUtil.isEmpty(personalId)) {
			return false;
		}

		Boolean discount = discountsApplied.get(uuid).get(personalId);
		return discount == null ? false : discount;
	}

	public void markDiscountApplied(String uuid, String personalId) {
		init(uuid);

		discountsApplied.get(uuid).put(personalId, Boolean.TRUE);
	}

	public boolean canApplyDiscount(String uuid, String familyNr, String personalId) {
		init(uuid);

		if (StringUtil.isEmpty(personalId)) {
			return false;
		}

		if (isDiscountApplied(uuid, personalId)) {
			return false;
		}

		if (isSkipped(uuid, familyNr, personalId)) {
			return false;
		}

		if (!isFamilyMemberSkipped(uuid, familyNr)) {
			return false;
		}

		Boolean discountDoesNotApply = discountsDoNotApply.get(uuid).get(personalId);
		return discountDoesNotApply == null ? true : discountDoesNotApply;
	}

	public void markDiscountDoesNotApply(String uuid, String personalId) {
		init(uuid);

		discountsDoNotApply.get(uuid).put(personalId, Boolean.TRUE);
	}

	public boolean isSkipped(String uuid, String familyNr, String personalId) {
		init(uuid);

		if (StringUtil.isEmpty(familyNr) || StringUtil.isEmpty(personalId)) {
			return false;
		}

		String skippedPersonalId = skippedFamilyMembers.get(uuid).get(familyNr);
		if (StringUtil.isEmpty(skippedPersonalId)) {
			return false;
		}

		return skippedPersonalId.equals(personalId);
	}

	public boolean isFamilyMemberSkipped(String uuid, String familyNr) {
		init(uuid);

		if (StringUtil.isEmpty(familyNr)) {
			return false;
		}

		String personalId = skippedFamilyMembers.get(uuid).get(familyNr);
		return StringUtil.isEmpty(personalId) ? false : true;
	}

	public void skipFamilyMember(String uuid, String familyNr, String personalId) {
		init(uuid);

		skippedFamilyMembers.get(uuid).put(familyNr, personalId);
	}

	public void reset(String uuid) {
		discountsApplied.remove(uuid);
		discountsDoNotApply.remove(uuid);
		skippedFamilyMembers.remove(uuid);
	}

}