package com.example.andyapp.queries;

import com.google.gson.annotations.SerializedName;

public class CloudinaryResponse {
    @SerializedName("secure_url")
    public String secureUrl;
    // Add other fields if needed

    public CloudinaryResponse(String secureUrl) {
        this.secureUrl = secureUrl;
    }

    public String getSecureUrl() {
        return secureUrl;
    }
}