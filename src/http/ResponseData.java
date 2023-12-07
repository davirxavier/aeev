package http;

public class ResponseData 
{
	private String statusMessage;
	private int status;
	private String body;
	
	public ResponseData() 
	{
		statusMessage = "";
		status = 200;
		body = "";
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
