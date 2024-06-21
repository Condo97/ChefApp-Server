package com.pantrypro.networking.server.response;

import com.pantrypro.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetCreatePanelsResponse {

    public GetCreatePanelsResponse() {

    }

    public String getCreatePanels() {
        try {
            return new String(Files.readString(Paths.get(Constants.Create_Panel_Spec_Filepath))).replace("\n","");
        } catch (IOException e) {
            // TODO: Handle errors
            System.out.println("Could not read create panels file in getCreatePanels in GetCreatePanelsResponse()");
            return "";
        }
    }

}
