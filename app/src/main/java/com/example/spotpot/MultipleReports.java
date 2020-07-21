package com.example.spotpot;

public class MultipleReports {
    private String ReportID;
    private String Latitude;
    private String Longitude;
    private String ImageURL;



    public void setReportID(String reportID) {
        ReportID = reportID;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getReportID() {
        return ReportID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public MultipleReports() {
        ReportID = "";
        Latitude = "";
        Longitude = "";
        ImageURL = "";
    }

    public MultipleReports(String reportID, String latitude, String longitude, String imageURL) {
        ReportID = reportID;
        Latitude = latitude;
        Longitude = longitude;
        ImageURL = imageURL;
    }
}
