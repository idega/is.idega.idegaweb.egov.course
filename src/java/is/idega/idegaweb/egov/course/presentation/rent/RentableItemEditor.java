package is.idega.idegaweb.egov.course.presentation.rent;

import is.idega.idegaweb.egov.application.IWBundleStarter;
import is.idega.idegaweb.egov.course.CourseConstants;
import is.idega.idegaweb.egov.course.business.rent.RentableItemServices;
import is.idega.idegaweb.egov.course.data.CoursePrice;
import is.idega.idegaweb.egov.course.data.rent.RentableItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.CSSSpacer;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableBodyRowGroup;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableHeaderCell;
import com.idega.presentation.TableHeaderRowGroup;
import com.idega.presentation.TableRow;
import com.idega.presentation.text.Heading2;
import com.idega.presentation.text.Heading3;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DoubleInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;
import com.idega.util.PresentationUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

public abstract class RentableItemEditor extends Block {

	public static final String PARAMETER_ACTION = "prmAction";
	public static final String PARAMETER_ITEM_ID = "prmRentableItemId";
	private static final String PARAMETER_ACTION_SAVE = "prmSaveRentableItem";
	private static final String PARAMETER_ACTION_DELETE = "prmDeleteRentableItem";

	private static final String PARAMETER_NAME = "prmRentableItemName";
	private static final String PARAMETER_RENT_PRICE = "prmRentableItemRentPrice";
	private static final String PARAMETER_QUANTITY = "prmRentableItemQuantity";
	private static final String PARAMETER_RENTED = "prmRentableItemRented";

	@Autowired
	private RentableItemServices rentableItemServices;

	private IWBundle bundle;
	private IWResourceBundle iwrb;

	private NumberFormat currencyFormatter;
	private Currency currency;

	private List<String> errorMessages;
	private List<String> successMessages;

	private Form form;

	private boolean showQuantity = Boolean.TRUE, showRented = Boolean.TRUE;

	@Override
	public void main(IWContext iwc) throws Exception {
		ELUtil.getInstance().autowire(this);

		PresentationUtil.addStyleSheetToHeader(iwc, iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER)
				.getVirtualPathWithFileNameString("style/application.css"));

		form = new Form();
		add(form);

		if (Boolean.TRUE.toString().equals(iwc.getParameter("hide_back_button")))
			form.add(new HiddenInput("hide_back_button", Boolean.TRUE.toString()));
		if (Boolean.TRUE.toString().equals(iwc.getParameter(CoreConstants.PARAMETER_CHECK_HTML_HEAD_AND_BODY)))
			form.add(new HiddenInput(CoreConstants.PARAMETER_CHECK_HTML_HEAD_AND_BODY, Boolean.TRUE.toString()));

		bundle = getBundle(iwc);
		iwrb = bundle.getResourceBundle(iwc);

		Locale locale = iwc.getCurrentLocale();
		currencyFormatter = NumberFormat.getCurrencyInstance(locale);
		try {
			currency = Currency.getInstance(locale);
		} catch (Exception e) {}

		Boolean showList = null;
		if (iwc.isParameterSet(PARAMETER_ACTION_SAVE)) {
			showList = doSave(iwc);
		} else if (iwc.isParameterSet(PARAMETER_ACTION_DELETE)) {
			doDelete(iwc);
		}

		printErrorMessages();
		printSuccessMessages();

		if (showList != null && !showList) {
			editOrCreateItem(iwc);
			return;
		}

		switch (getAction(iwc)) {
		case 1:
			editOrCreateItem(iwc);
			break;
		default:
			listItems(iwc);
			break;
		}
	}

	private void printErrorMessages() {
		printMessages("errorMessages", errorMessages);
	}

	private void printSuccessMessages() {
		printMessages("successMessages", successMessages);
	}

	private void printMessages(String styleClass, List<String> messages) {
		if (ListUtil.isEmpty(messages)) {
			return;
		}

		Layer messagesContainer = new Layer();
		form.add(messagesContainer);
		messagesContainer.setStyleClass(styleClass);

		for (String message: messages) {
			messagesContainer.add(new Heading2(message));
		}
	}

	private boolean doDelete(IWContext iwc) throws Exception {
		if (rentableItemServices.deleteItem(getItemClass(), iwc.getParameter(PARAMETER_ITEM_ID))) {
			addSuccessMessage(iwrb.getLocalizedString("rentable_item.success_deleting_item", "Item was successfully deleted"));
			return true;
		} else {
			addErrorMessage(iwrb.getLocalizedString("rentable_item.error_deleting_item", "Some error occurred while deleting item"));
			return false;
		}
	}

	protected Collection<SchoolSeason> getSeasons() throws Exception {
		SchoolCategoryHome categoryHome = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
		SchoolSeasonHome seasonHome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
		return seasonHome.findAllSchoolSeasons(categoryHome.findByPrimaryKey(getCategoryKey()));
	}

	private boolean doSave(IWContext iwc) throws Exception {
		String type = getRentableItemType();

		Collection<SchoolSeason> seasons = getSeasons();
		if (ListUtil.isEmpty(seasons)) {
			return false;
		}

		String name = iwc.getParameter(PARAMETER_NAME);
		if (StringUtil.isEmpty(name)) {
			addErrorMessage(iwrb.getLocalizedString("rentable_item.name_must_be_provided", "Please, provide item's name"));
			return false;
		}

		Map<SchoolSeason, Double> prices = new HashMap<SchoolSeason, Double>();
		for (SchoolSeason season: seasons) {
			String param = PARAMETER_RENT_PRICE.concat(CoreConstants.HASH).concat(season.getPrimaryKey().toString());
			Double price = iwc.isParameterSet(param) ? Double.valueOf(iwc.getParameter(param)) : Double.valueOf(0);
			prices.put(season, price);
		}

		Integer quantity = null;
		try {
			quantity = iwc.isParameterSet(PARAMETER_QUANTITY) ? Integer.valueOf(iwc.getParameter(PARAMETER_QUANTITY)) : null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		Integer rented = null;
		try {
			rented = iwc.isParameterSet(PARAMETER_RENTED) ? Integer.valueOf(iwc.getParameter(PARAMETER_RENTED)) : null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (iwc.isParameterSet(PARAMETER_ITEM_ID)) {
			if (rentableItemServices.editItem(getItemClass(), iwc.getParameter(PARAMETER_ITEM_ID), name, prices, quantity, rented)) {
				addSuccessMessage(iwrb.getLocalizedString("rentable_item.success_editing_item", "Item was successfully modified"));
			} else {
				addErrorMessage(iwrb.getLocalizedString("rentable_item.error_editing_item", "Some error occured while editing item"));
				return false;
			}
		} else {
			if (rentableItemServices.createItem(getItemClass(), type, name, prices, quantity, rented) == null) {
				addErrorMessage(iwrb.getLocalizedString("rentable_item.error_creating_item", "Some error occurred while creating item"));
				return false;
			} else {
				addSuccessMessage(iwrb.getLocalizedString("rentable_item.success_creating_item", "Item was created successfully"));
			}
		}

		return true;
	}

	private void addErrorMessage(String message) {
		if (errorMessages == null) {
			errorMessages = new ArrayList<String>();
		}
		errorMessages.add(message);
	}

	private void addSuccessMessage(String message) {
		if (successMessages == null) {
			successMessages = new ArrayList<String>();
		}
		successMessages.add(message);
	}

	protected abstract String getCategoryKey();

	protected RentableItem editOrCreateItem(IWContext iwc) throws Exception {
		RentableItem item = null;
		if (iwc.isParameterSet(PARAMETER_ITEM_ID)) {
			item = rentableItemServices.getItem(getItemClass(), iwc.getParameter(PARAMETER_ITEM_ID));
		}

		if (item != null) {
			form.addParameter(PARAMETER_ITEM_ID, item.getPrimaryKey().toString());
		}

		Layer inputs = new Layer();
		form.add(inputs);
		inputs.setStyleClass("formSection");

		Collection<SchoolSeason> seasons = getSeasons();
		if (ListUtil.isEmpty(seasons)) {
			return item;
		}

		Layer formItem = new Layer();
		formItem.setStyleClass("formItem");
		formItem.setStyleClass("required");
		inputs.add(formItem);
		TextInput name = new TextInput(PARAMETER_NAME, item == null ? CoreConstants.EMPTY : item.getName());
		Label nameLabel = new Label(iwrb.getLocalizedString("rentable_item.name", "Name"), name);
		formItem.add(nameLabel);
		formItem.add(name);
		inputs.add(new CSSSpacer());

		String seasonLabel = iwrb.getLocalizedString("season", "Season");
		for (SchoolSeason season: seasons) {
			Layer seasonContainer = new Layer();
			inputs.add(seasonContainer);
			seasonContainer.add(new Heading3(seasonLabel.concat(CoreConstants.SPACE).concat(season.getName()).concat(CoreConstants.COLON)));
			seasonContainer.add(new CSSSpacer());

			formItem = new Layer();
			formItem.setStyleClass("formItem");
			seasonContainer.add(formItem);
			Double rentPrice = item == null ? null : item.getRentPrice(season);
			DoubleInput rentPriceInput = new DoubleInput(
					PARAMETER_RENT_PRICE.concat(CoreConstants.HASH).concat(season.getPrimaryKey().toString()),
					rentPrice == null ? 0 : rentPrice
			);
			rentPriceInput.setAsDouble(
					iwrb.getLocalizedString("rentable_item.please_use_numbers_only", "Please, use correct value for rent price"),
					currency == null ? -1 : currency.getDefaultFractionDigits()
			);
			StringBuilder rentPriceTitle = new StringBuilder(iwrb.getLocalizedString("rentable_item.rent_price", "Rent price"));
			if (currency != null) {
				rentPriceTitle.append(" (").append(currency.getCurrencyCode()).append(")");
			}
			Label rentPriceLabel = new Label(rentPriceTitle.toString(), rentPriceInput);
			formItem.add(rentPriceLabel);
			formItem.add(rentPriceInput);
		}

		if (isShowQuantity()) {
			formItem = new Layer();
			formItem.setStyleClass("formItem");
			inputs.add(formItem);
			Integer quantity = item == null ? null : item.getQuantity();
			IntegerInput quantityInput = new IntegerInput(PARAMETER_QUANTITY, quantity == null ? 100 : quantity);
			quantityInput.setAsIntegers(iwrb.getLocalizedString("rentable_item.please_use_numbers_only_quantity", "Please, use correct value for quantity"));
			Label quantityLabel = new Label(iwrb.getLocalizedString("rentable_item.quantity", "Quantity"), quantityInput);
			formItem.add(quantityLabel);
			formItem.add(quantityInput);
		}

		if (isShowRented()) {
			formItem = new Layer();
			formItem.setStyleClass("formItem");
			inputs.add(formItem);
			Integer rented = item == null ? null : item.getRentedAmount();
			IntegerInput rentedInput = new IntegerInput(PARAMETER_RENTED, rented == null ? 0 : rented);
			rentedInput.setAsIntegers(iwrb.getLocalizedString("rentable_item.please_use_numbers_only_rented", "Please, use correct value for rented amount"));
			Label rentedLabel = new Label(iwrb.getLocalizedString("rentable_item.rented", "Rented"), rentedInput);
			formItem.add(rentedLabel);
			formItem.add(rentedInput);
		}

		Layer buttons = new Layer();
		form.add(buttons);
		if (!Boolean.TRUE.toString().equals(iwc.getParameter("hide_back_button"))) {
			BackButton back = new BackButton(iwrb.getLocalizedString("back", "Back"));
			buttons.add(back);
		}

		if (item != null) {
			SubmitButton delete = new SubmitButton(iwrb.getLocalizedString("delete", "Delete"), PARAMETER_ACTION_DELETE, Boolean.TRUE.toString());
			delete.setOnClick("if (!window.confirm('" + iwrb.getLocalizedString("are_you_sure", "Are you sure?") + "')) return false;");
			buttons.add(delete);
		}

		SubmitButton save = new SubmitButton(iwrb.getLocalizedString("save", "Save"), PARAMETER_ACTION_SAVE, Boolean.TRUE.toString());
		buttons.add(save);

		return item;
	}

	public abstract String getRentableItemType();

	public abstract <I extends RentableItem> Class<I> getItemClass();

	private void listItems(IWContext iwc) throws Exception {
		Collection<RentableItem> allItems = rentableItemServices.getItemsByType(getItemClass(), getRentableItemType());
		if (ListUtil.isEmpty(allItems)) {
			form.add(new Heading2(iwrb.getLocalizedString("rentable_item.there_are_no_items_yet", "There are no items yet")));
		} else {
			String editTitle = iwrb.getLocalizedString("rentable_item.edit", "Edit");
			String deleteTitle = iwrb.getLocalizedString("rentable_item.delete", "Delete");

			Layer tableContainer = new Layer();
			form.add(tableContainer);
			Table2 table = new Table2();
			tableContainer.add(table);
			TableHeaderRowGroup header = table.createHeaderRowGroup();
			TableRow headerRow = header.createRow();
			TableHeaderCell headerCell = headerRow.createHeaderCell();
			headerCell.add(new Text(iwrb.getLocalizedString("rentable_item.nr", "Nr.")));

			//	Name
			headerCell = headerRow.createHeaderCell();
			headerCell.add(new Text(iwrb.getLocalizedString("rentable_item.name", "Name")));
//
//			//	Season
//			headerCell = headerRow.createHeaderCell();
//			headerCell.add(new Text(iwrb.getLocalizedString("season", "Season")));
//
//			//	Price
//			headerCell = headerRow.createHeaderCell();
//			headerCell.add(new Text(iwrb.getLocalizedString("rentable_item.rent_price", "Rent price")));
//
			//	Quantity
			if (isShowQuantity()) {
				headerCell = headerRow.createHeaderCell();
				headerCell.add(new Text(iwrb.getLocalizedString("rentable_item.quantity", "Quantity")));
			}

			//	Rented
			if (isShowRented()) {
				headerCell = headerRow.createHeaderCell();
				headerCell.add(new Text(iwrb.getLocalizedString("rentable_item.rented", "Rented")));
			}

			headerCell = headerRow.createHeaderCell();
			headerCell.add(new Text(editTitle));

			headerCell = headerRow.createHeaderCell();
			headerCell.add(new Text(deleteTitle));

			String editImageUri = bundle.getVirtualPathWithFileNameString("edit.png");
			String deleteImageUri = bundle.getVirtualPathWithFileNameString("delete.png");

			String itemId = null;

			int index = 0;
			TableBodyRowGroup body = table.createBodyRowGroup();
			boolean hideBackButton = Boolean.TRUE.toString().equals(iwc.getParameter("hide_back_button"));
			boolean checkHTML = Boolean.TRUE.toString().equals(iwc.getParameter(CoreConstants.PARAMETER_CHECK_HTML_HEAD_AND_BODY));

			int columnSpan = 4;
			if (isShowQuantity()) {
				columnSpan++;
			}
			if (isShowRented()) {
				columnSpan++;
			}
			for (RentableItem item: allItems) {
				itemId = item.getPrimaryKey().toString();
				TableRow row = body.createRow();
				TableCell2 cell = row.createCell();
				cell.add(new Text(String.valueOf(index + 1).concat(CoreConstants.DOT)));
				cell.setStyleAttribute("text-align", "center");

				cell = row.createCell();
				cell.add(new Text(item.getName()));
				cell.setStyleAttribute("text-align", "center");

				if (isShowQuantity()) {
					cell = row.createCell();
					Integer quantity = item.getQuantity();
					cell.add(new Text(quantity == null ? CoreConstants.EMPTY : quantity.toString()));
					cell.setStyleAttribute("text-align", "center");
				}

				if (isShowRented()) {
					cell = row.createCell();
					Integer rented = item.getRentedAmount();
					cell.add(new Text(rented == null ? CoreConstants.EMPTY : rented.toString()));
					cell.setStyleAttribute("text-align", "center");
				}

				Link edit = new Link(new Image(editImageUri));
				edit.setParameter(PARAMETER_ACTION, String.valueOf(1));
				edit.setParameter(PARAMETER_ITEM_ID, itemId);
				if (hideBackButton)
					edit.setParameter("hide_back_button", Boolean.TRUE.toString());
				if (checkHTML)
					edit.setParameter(CoreConstants.PARAMETER_CHECK_HTML_HEAD_AND_BODY, Boolean.TRUE.toString());
				edit.setTitle(editTitle);
				cell = row.createCell();
				cell.add(edit);
				cell.setStyleAttribute("text-align", "center");

				Link delete = new Link(new Image(deleteImageUri));
				delete.setParameter(PARAMETER_ACTION_DELETE, Boolean.TRUE.toString());
				delete.setParameter(PARAMETER_ITEM_ID, itemId);
				if (hideBackButton)
					delete.setParameter("hide_back_button", Boolean.TRUE.toString());
				if (checkHTML)
					delete.setParameter(CoreConstants.PARAMETER_CHECK_HTML_HEAD_AND_BODY, Boolean.TRUE.toString());
				delete.setTitle(deleteTitle);
				delete.setOnClick("if (!window.confirm('" + iwrb.getLocalizedString("are_you_sure", "Are you sure?") + "')) return false;");
				cell = row.createCell();
				cell.add(delete);
				cell.setStyleAttribute("text-align", "center");

				Collection<CoursePrice> prices = item.getAllPrices();
				if (ListUtil.isEmpty(prices)) {
					row = body.createRow();
					cell = row.createCell();
					cell.setColumnSpan(columnSpan);
					cell.add(
						new Text(
							iwrb.getLocalizedString("price_for_all_seasons", "Price for all seasons").concat(CoreConstants.COLON)
							.concat(CoreConstants.SPACE).concat(currencyFormatter.format(item.getRentPrice(null))).concat(CoreConstants.SPACE)
							.concat(currencyFormatter.getCurrency().getCurrencyCode())
						)
					);
				} else {
					row = body.createRow();
					cell = row.createCell();
					cell.setColumnSpan(columnSpan);
					cell.add(new Text(iwrb.getLocalizedString("prices", "Prices").concat(CoreConstants.COLON)));
					for (CoursePrice price: prices) {
						row = body.createRow();
						cell = row.createCell();
						cell.setColumnSpan(columnSpan);
						SchoolSeason priceSeason = price.getSchoolSeason();
						cell.add(
							new Text(priceSeason.getName()
								.concat(CoreConstants.COLON).concat(CoreConstants.SPACE).concat(currencyFormatter.format(item.getRentPrice(priceSeason)))
								.concat(CoreConstants.SPACE).concat(currencyFormatter.getCurrency().getCurrencyCode())
							)
						);
					}
				}

				index++;
			}

//			SchoolSeasonHome seasonHome = (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
//			Map<String, List<RentableItem>> groupedItems = rentableItemServices.getGroupedRentableItems(allItems);
//			for (String seasonId: groupedItems.keySet()) {
//				List<RentableItem> items = groupedItems.get(seasonId);
//				if (ListUtil.isEmpty(items)) {
//					continue;
//				}
//
//				SchoolSeason season = seasonHome.findByPrimaryKey(seasonId);
//				for (RentableItem item: items) {
//					itemId = item.getPrimaryKey().toString();
//
//					TableRow row = body.createRow();
//
//					TableCell2 cell = row.createCell();
//					cell.add(new Text(String.valueOf(index + 1)));
//
//					cell = row.createCell();
//					cell.add(new Text(item.getName()));
//
//					cell = row.createCell();
//					cell.add(new Text(season.getName()));
//
//					cell = row.createCell();
//					cell.add(new Text(currencyFormatter.format(item.getRentPrice(season))));
//
//					if (isShowQuantity()) {
//						cell = row.createCell();
//						Integer quantity = item.getQuantity();
//						cell.add(new Text(quantity == null ? CoreConstants.EMPTY : quantity.toString()));
//					}
//
//					if (isShowRented()) {
//						cell = row.createCell();
//						Integer rented = item.getRentedAmount();
//						cell.add(new Text(rented == null ? CoreConstants.EMPTY : rented.toString()));
//					}
//
//					Link edit = new Link(new Image(editImageUri));
//					edit.setParameter(PARAMETER_ACTION, String.valueOf(1));
//					edit.setParameter(PARAMETER_ITEM_ID, itemId);
//					if (hideBackButton)
//						edit.setParameter("hide_back_button", Boolean.TRUE.toString());
//					if (checkHTML)
//						edit.setParameter(CoreConstants.PARAMETER_CHECK_HTML_HEAD_AND_BODY, Boolean.TRUE.toString());
//					edit.setTitle(editTitle);
//					cell = row.createCell();
//					cell.add(edit);
//
//					Link delete = new Link(new Image(deleteImageUri));
//					delete.setParameter(PARAMETER_ACTION_DELETE, Boolean.TRUE.toString());
//					delete.setParameter(PARAMETER_ITEM_ID, itemId);
//					if (hideBackButton)
//						delete.setParameter("hide_back_button", Boolean.TRUE.toString());
//					if (checkHTML)
//						delete.setParameter(CoreConstants.PARAMETER_CHECK_HTML_HEAD_AND_BODY, Boolean.TRUE.toString());
//					delete.setTitle(deleteTitle);
//					delete.setOnClick("if (!window.confirm('" + iwrb.getLocalizedString("are_you_sure", "Are you sure?") + "')) return false;");
//					cell = row.createCell();
//					cell.add(delete);
//
//					index++;
//				}
//			}
		}

		Layer buttons = new Layer();
		form.add(buttons);
		if (!Boolean.TRUE.toString().equals(iwc.getParameter("hide_back_button"))) {
			BackButton back = new BackButton(iwrb.getLocalizedString("back", "Back"));
			buttons.add(back);
		}
		SubmitButton create = new SubmitButton(iwrb.getLocalizedString("create_new", "Create"), PARAMETER_ACTION, String.valueOf(1));
		buttons.add(create);
	}

	private int getAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			try {
				return Integer.valueOf(iwc.getParameter(PARAMETER_ACTION));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public boolean isShowQuantity() {
		return showQuantity;
	}

	public void setShowQuantity(boolean showQuantity) {
		this.showQuantity = showQuantity;
	}

	public boolean isShowRented() {
		return showRented;
	}

	public void setShowRented(boolean showRented) {
		this.showRented = showRented;
	}

	@Override
	public String getBundleIdentifier() {
		return CourseConstants.IW_BUNDLE_IDENTIFIER;
	}

	/**
	 * <p>Adds {@link Layer} to {@link RentableItemEditor} form.</p>
	 * @param layer {@link Layer} to add.
	 */
	public void addToForm(Layer layer) {
		this.form.add(layer);
	}
}