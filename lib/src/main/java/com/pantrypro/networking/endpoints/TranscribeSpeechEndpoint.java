package com.pantrypro.networking.endpoints;

import com.pantrypro.core.Endpoint;
import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.exceptions.InvalidFileTypeException;
import com.pantrypro.keys.Keys;
import com.pantrypro.networking.server.request.TranscribeSpeechRequest;
import com.pantrypro.networking.server.response.TranscribeSpeechResponse;
import com.pantrypro.openai.SpeechTranscriber;

public class TranscribeSpeechEndpoint implements Endpoint<TranscribeSpeechRequest> {

    private static String openAIKey = Keys.openAiAPI;

    @Override
    public Object getResponse(TranscribeSpeechRequest request) throws Exception {
        // Get userID from authRequest
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken());

        // Get transcription
        SpeechTranscriber.CompletedTranscription transcription = SpeechTranscriber.transcribe(
                request.getSpeechFileName(),
                request.getSpeechFile(),
                null,
                Keys.openAiAPI
        );

        // If transcription text is null throw InvalidFileTypeException, otherwise return transcription text in TranscribeSpeechResponse
        if (transcription.getText() == null) {
            throw new InvalidFileTypeException("Invalid file type", "flac, mp3, mp4, mpeg, mpga, m4a, ogg, wav, or webm");
        }

        // Transform into TranscribeSpeechResponse and return
        return new TranscribeSpeechResponse(
                transcription.getText()
        );
    }
}
