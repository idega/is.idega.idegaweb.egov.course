package is.idega.idegaweb.egov.course.presentation;

import is.idega.idegaweb.egov.course.data.CourseCategory;
import is.idega.idegaweb.egov.course.data.CourseProviderType;
import is.idega.idegaweb.egov.course.data.CourseType;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableColumn;
import com.idega.presentation.TableColumnGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.PresentationUtil;

public class CourseTypeEditor extends CourseBlock {

	private static final String PARAMETER_NAME = "CTE_n";
	private static final String PARAMETER_DESCRIPTION = "CTE_d";
	private static final String PARAMETER_LOCALIZATION_KEY = "CTE_l";
	private static final String PARAMETER_ACCOUNTING_KEY = "prm_accounting_key";
	private static final String PARAMETER_DISABLED = "prm_disabled";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	private static final int ACTION_DELETE = 5;

//	private String category = null;

	public void present(IWContext iwc) {
		try {
			switch (parseAction(iwc)) {
			case ACTION_VIEW:
				showList(iwc);
				break;

			case ACTION_EDIT:
				Object typePK = iwc.getParameter(PARAMETER_COURSE_TYPE_PK);
				showEditor(iwc, typePK);
				break;

			case ACTION_NEW:
				showEditor(iwc, null);
				break;

			case ACTION_SAVE:
				saveType(iwc);
				showList(iwc);
				break;

			case ACTION_DELETE:
				if (!getBusiness().deleteCourseType(
						iwc.getParameter(PARAMETER_COURSE_TYPE_PK))) {
					PresentationUtil
							.addJavascriptAlertOnLoad(
									iwc,
									getResourceBundle()
											.getLocalizedString(
													"course_type.remove_error",
													"You can not remove a course type that has courses or prices attached to it."));
				}
				showList(iwc);
				break;
			}
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}

	private void saveType(IWContext iwc) {
		String pk = iwc.getParameter(PARAMETER_COURSE_TYPE_PK);
		String name = iwc.getParameter(PARAMETER_NAME);
		String description = iwc.getParameter(PARAMETER_DESCRIPTION);
		String localizationKey = iwc.getParameter(PARAMETER_LOCALIZATION_KEY);
		String schoolTypePK = iwc.getParameter(PARAMETER_SCHOOL_TYPE_PK);
		String accountingKey = iwc.getParameter(PARAMETER_ACCOUNTING_KEY);
		boolean disabled = iwc.isParameterSet(PARAMETER_DISABLED);

		if (name != null && !"".equals(name.trim())) {
			try {
				getBusiness().storeCourseType(pk, name, description,
						localizationKey, schoolTypePK, accountingKey, disabled);
			} catch (CreateException ce) {
				add(ce.getMessage());
			} catch (FinderException fe) {
				fe.printStackTrace();
			} catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
	}
	
	protected Collection<CourseProviderType> getCourseProviderTypes(
			CourseProviderType courseProviderType) {
		if (getType() != null) {
			if (courseProviderType != null) {
				return Arrays.asList(courseProviderType, getType());
			}

			return Arrays.asList(getType());
		}

		return getBusiness().getAllAfterschoolCareSchoolTypes();
	}
	
	public void showEditor(IWContext iwc, Object courseTypePK)
			throws java.rmi.RemoteException {
		Form form = new Form();
		form.setStyleClass("adminForm");

		Layer section = new Layer(Layer.DIV);
		section.setStyleClass("formSection");
		form.add(section);

		Layer helpLayer = new Layer(Layer.DIV);
		helpLayer.setStyleClass("helperText");
		helpLayer.add(new Text(localize("course_type.course_type_editor_help",
				"Fill in the desired values and click 'Save'.")));
		section.add(helpLayer);

		TextInput inputName = new TextInput(PARAMETER_NAME);
		TextInput inputLocalization = new TextInput(PARAMETER_LOCALIZATION_KEY);
		TextArea inputDesc = new TextArea(PARAMETER_DESCRIPTION);
		TextInput inputAccounting = new TextInput(PARAMETER_ACCOUNTING_KEY);
		CheckBox disabled = new CheckBox(PARAMETER_DISABLED);

		DropdownMenu inputSchoolTypes = null;
		CourseType type = getBusiness().getCourseType(courseTypePK);
		if (type != null) {
			inputName.setContent(type.getName());
			inputLocalization.setContent(type.getLocalizationKey());
			inputDesc.setContent(type.getDescription());

			CourseCategory category = type.getCourseCategory();
			inputSchoolTypes = new DropdownMenu(
					getCourseProviderTypes(category),
					PARAMETER_SCHOOL_TYPE_PK);
			if (category != null) {
				inputSchoolTypes.setSelectedElement(category.getPrimaryKey()
						.toString());
			}

			if (type.getAccountingKey() != null) {
				inputAccounting.setValue(type.getAccountingKey());
			}

			disabled.setChecked(type.isDisabled());

			form.add(new HiddenInput(
					PARAMETER_COURSE_TYPE_PK, 
					courseTypePK.toString()));
		} else {
			inputSchoolTypes = new DropdownMenu(
					getCourseProviderTypes(null),
					PARAMETER_SCHOOL_TYPE_PK);
		}

		Layer layer;
		Label label;

		layer = new Layer(Layer.DIV);
		layer.setID("name");
		layer.setStyleClass("formItem");
		label = new Label(localize("name", "Name"), inputName);
		layer.add(label);
		layer.add(inputName);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("localization");
		layer.setStyleClass("formItem");
		label = new Label(localize("localization_key", "Key"),
				inputLocalization);
		layer.add(label);
		layer.add(inputLocalization);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("info");
		layer.setStyleClass("formItem");
		label = new Label(localize("info", "Info"), inputDesc);
		layer.add(label);
		layer.add(inputDesc);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("schoolTypes");
		layer.setStyleClass("formItem");
		label = new Label(localize("category", "Category"), inputSchoolTypes);
		layer.add(label);
		layer.add(inputSchoolTypes);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("accountingKey");
		layer.setStyleClass("formItem");
		label = new Label(localize("accounting_key", "Accounting key"),
				inputAccounting);
		layer.add(label);
		layer.add(inputAccounting);
		section.add(layer);

		layer = new Layer(Layer.DIV);
		layer.setID("disabled");
		layer.setStyleClass("formItem");
		layer.setStyleClass("radioButtonItem");
		label = new Label(localize("disabled", "Disabled"), disabled);
		layer.add(label);
		layer.add(disabled);
		section.add(layer);

		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		section.add(clearLayer);

		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonLayer");
		form.add(buttonLayer);

		SubmitButton save = new SubmitButton(localize("save", "Save"),
				PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		SubmitButton cancel = new SubmitButton(localize("cancel", "Cancel"),
				PARAMETER_ACTION, String.valueOf(ACTION_VIEW));

		buttonLayer.add(cancel);
		buttonLayer.add(save);
		add(form);
	}

	public void showList(IWContext iwc) {
		Form form = new Form();
		form.setStyleClass("adminForm");

		Table2 table = new Table2();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth("100%");
		table.setStyleClass("adminTable");
		table.setStyleClass("ruler");

		TableColumnGroup columnGroup = table.createColumnGroup();
		TableColumn column = columnGroup.createColumn();
		column.setSpan(5);
		column = columnGroup.createColumn();
		column.setSpan(2);
		column.setWidth("12");

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("name");
		cell.add(new Text(localize("name", "Name")));

		cell = row.createHeaderCell();
		cell.setStyleClass("description");
		cell.add(new Text(localize("info", "Info")));

		cell = row.createHeaderCell();
		cell.setStyleClass("schoolCategory");
		cell.add(new Text(localize("category", "Category")));

		cell = row.createHeaderCell();
		cell.setStyleClass("accountingKey");
		cell.add(new Text(localize("accounting_key", "Accounting key")));

		cell = row.createHeaderCell();
		cell.setStyleClass("edit");
		cell.add(new Text(localize("edit", "Edit")));

		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("delete");
		cell.add(new Text(localize("delete", "Delete")));

		group = table.createBodyRowGroup();

		int iRow = 1;
		Collection<CourseType> courseTypes = getBusiness().getAllCourseTypes(
				getType(), false);
		for (CourseType cType: courseTypes) {
			if (cType == null) {
				continue;
			}
			
			row = group.createRow();

			try {
				Link edit = new Link(getBundle().getImage("edit.png",
						localize("edit", "Edit")));
				edit.addParameter(PARAMETER_COURSE_TYPE_PK, cType
						.getPrimaryKey().toString());
				edit.addParameter(PARAMETER_ACTION, ACTION_EDIT);

				Link delete = new Link(getBundle().getImage("delete.png",
						localize("delete", "Delete")));
				delete.addParameter(PARAMETER_COURSE_TYPE_PK, cType
						.getPrimaryKey().toString());
				delete.addParameter(PARAMETER_ACTION, ACTION_DELETE);
				delete
						.setClickConfirmation(getResourceBundle()
								.getLocalizedString(
										"course_type.confirm_delete",
										"Are you sure you want to delete the course type selected?"));

				cell = row.createCell();
				cell.setStyleClass("firstColumn");
				cell.add(new Text(cType.getName()));

				cell = row.createCell();
				cell.setStyleClass("description");
				if (cType.getDescription() != null) {
					cell.add(new Text(cType.getDescription()));
				} else {
					cell.add(new Text("-"));
				}

				cell = row.createCell();
				cell.setStyleClass("category");
				CourseCategory category = cType.getCourseCategory();
				if (category != null) {
					if (category.getLocalizationKey() != null) {
						cell.add(new Text(localize(category
								.getLocalizationKey(), category.getName())));
					} else {
						cell.add(new Text(category.getName()));
					}
				}

				cell = row.createCell();
				cell.setStyleClass("accountingKey");
				if (cType.getAccountingKey() != null) {
					cell.add(new Text(cType.getAccountingKey()));
				} else {
					cell.add(new Text("-"));
				}

				cell = row.createCell();
				cell.setStyleClass("edit");
				cell.add(edit);

				cell = row.createCell();
				cell.setStyleClass("delete");
				cell.setStyleClass("lastColumn");
				cell.add(delete);

				if (iRow % 2 == 0) {
					row.setStyleClass("even");
				} else {
					row.setStyleClass("odd");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			iRow++;
		}

		form.add(table);

		Layer buttonLayer = new Layer(Layer.DIV);
		buttonLayer.setStyleClass("buttonLayer");
		form.add(buttonLayer);

		if (getBackPage() != null) {
			GenericButton back = new GenericButton(localize("back", "Back"));
			back.setPageToOpen(getBackPage());
			buttonLayer.add(back);
		}

		SubmitButton newLink = new SubmitButton(
				localize("type.new", "New type"), PARAMETER_ACTION, String
						.valueOf(ACTION_NEW));
		buttonLayer.add(newLink);

		add(form);
	}
}