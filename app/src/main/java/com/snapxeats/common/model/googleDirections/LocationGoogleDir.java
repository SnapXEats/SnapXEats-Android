package com.snapxeats.common.model.googleDirections;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Prajakta Patil on 22/2/18.
 */
@Getter
@Setter
public class LocationGoogleDir {
    private GoogleDirOrigin googleDirOrigin;
    private GoogleDirDest googleDirDest;
/*
    public LocationGoogleDir(GoogleDirOrigin googleDirOrigin,GoogleDirDest googleDirDest) {
        this.googleDirOrigin = googleDirOrigin;
        this.googleDirDest = googleDirDest;
    }*/
}
