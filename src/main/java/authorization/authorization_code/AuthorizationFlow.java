package authorization.authorization_code;

import com.volumecontrol.NowPlaying;

import java.util.concurrent.CompletableFuture;

public class AuthorizationFlow {
    public static void login() {
        AuthorizationCodeUri.authorizationCodeUri_Sync();

        CompletableFuture<String> authorizationCodeFuture = WebServer.start();

        // Wait for the authorization code
        String authorizationCode;
        try {
            authorizationCode = authorizationCodeFuture.get();
            System.out.println("Authorization Code: " + authorizationCode);
            AuthorizationData.authorizationCodeRequest = AuthorizationData.getSpotifyApi().authorizationCode(authorizationCode).build();
            AuthorizationCode.authorizationCode_Sync();

            // Now you can use the authorization code to continue with your logic
            // For example, exchange the code for an access token using the Spotify API
            // AuthorizationData.authorizationCodeRequest = spotifyApi.authorizationCode(authorizationCode).build();
            // AuthorizationCode.authorizationCode_Async();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
