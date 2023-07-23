package com.volumecontrol;

import authorization.authorization_code.AuthorizationFlow;

public class Main {
    public static void main(String[] args) {
        AuthorizationFlow.login();
//        Resume.startResumeUsersPlayback_Sync();
        NowPlaying.getInformationAboutUsersCurrentPlayback_Sync();
    }
}
