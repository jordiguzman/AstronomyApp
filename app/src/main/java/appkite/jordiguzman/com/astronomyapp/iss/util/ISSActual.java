package appkite.jordiguzman.com.astronomyapp.iss.util;


import com.google.gson.annotations.SerializedName;

public class ISSActual {

    @SerializedName("iss_position")
    private ISSPosition issPosition;

    private String message;
    private long timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ISSPosition getIssPosition() {
        return issPosition;
    }

    public void setIssPosition(ISSPosition issPosition) {
        this.issPosition = issPosition;
    }

    @Override
    public String toString() {
        return "ISSNow{" +
                "issPosition=" + issPosition +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
