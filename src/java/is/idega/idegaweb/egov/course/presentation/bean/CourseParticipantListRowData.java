package is.idega.idegaweb.egov.course.presentation.bean;

public class CourseParticipantListRowData {
	
	private String columnName = null;
	
	private boolean disabled = false;
	private boolean show = true;
	private boolean forceToCheck = false;
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public boolean isForceToCheck() {
		return forceToCheck;
	}
	public void setForceToCheck(boolean forceToCheck) {
		this.forceToCheck = forceToCheck;
	}
	
}
