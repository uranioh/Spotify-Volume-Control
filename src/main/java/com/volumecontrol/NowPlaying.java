package com.volumecontrol;

import authorization.authorization_code.AuthorizationData;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class NowPlaying {

    private static final SpotifyApi spotifyApi = AuthorizationData.getSpotifyApi();
    private static final GetInformationAboutUsersCurrentPlaybackRequest getInformationAboutUsersCurrentPlaybackRequest =
            spotifyApi.getInformationAboutUsersCurrentPlayback()
//                  .market(CountryCode.SE)
//                  .additionalTypes("track,episode")
                    .build();

    public static void getInformationAboutUsersCurrentPlayback_Sync() {
        try {
            final CurrentlyPlayingContext currentlyPlayingContext = getInformationAboutUsersCurrentPlaybackRequest.execute();

            System.out.println("Currently playing track: " + currentlyPlayingContext.getItem().getName());
            System.out.println("Currently playing track id: " + currentlyPlayingContext.getItem().getId());
            System.out.println("Timestamp: " + currentlyPlayingContext.getTimestamp());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void getInformationAboutUsersCurrentPlayback_Async() {
        try {
            final CompletableFuture<CurrentlyPlayingContext> currentlyPlayingContextFuture = getInformationAboutUsersCurrentPlaybackRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final CurrentlyPlayingContext currentlyPlayingContext = currentlyPlayingContextFuture.join();

            System.out.println("Timestamp: " + currentlyPlayingContext.getTimestamp());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
}
