package pmr.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DevDataBean {
	private String projectName;
	private int ticketNo;
	private String ticketDiscription;
	private String issueType;
	private int totalTicketCount;
	private double totalEstimatedEfforts;
	private double totalActualEfforts;
	private double totalSize;
	private String createdOn;
	private String releaseName;
	
	private Map<String,List<Map<String,String>>> updatedDetails;
	private Map<String,List<Map<String,String>>> devReleaseWiseData;
	private Map<String,List<Map<String,String>>> devMonthWiseData;
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(int ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getTicketDiscription() {
		return ticketDiscription;
	}

	public void setTicketDiscription(String ticketDiscription) {
		this.ticketDiscription = ticketDiscription;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public int getTotalTicketCount() {
		return totalTicketCount;
	}

	public void setTotalTicketCount(int totalTicketCount) {
		this.totalTicketCount = totalTicketCount;
	}

	public double getTotalEstimatedEfforts() {
		return totalEstimatedEfforts;
	}

	public void setTotalEstimatedEfforts(double totalEstimatedEfforts) {
		this.totalEstimatedEfforts = totalEstimatedEfforts;
	}

	public double getTotalActualEfforts() {
		return totalActualEfforts;
	}

	public void setTotalActualEfforts(double totalActualEfforts) {
		this.totalActualEfforts = totalActualEfforts;
	}

	public double getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(double totalSize) {
		this.totalSize = totalSize;
	}

	public List<String> getFields() {
		List<String> lstFields = new ArrayList<String>();
		lstFields.add(String.valueOf(issueType));
		lstFields.add(String.valueOf(totalTicketCount));
		lstFields.add(String.valueOf(totalSize));
		lstFields.add(String.valueOf(totalActualEfforts));
		lstFields.add(String.valueOf(totalEstimatedEfforts));

		return lstFields;
	}
}
