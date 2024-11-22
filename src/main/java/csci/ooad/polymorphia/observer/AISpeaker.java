package csci.ooad.polymorphia.observer;

//import okhttp3.*;
//import org.json.JSONObject;

import java.net.http.HttpClient;

public class AISpeaker {

    // TODO: Figure this out
    // See if I can use the Java 11 HttpClient instead of the OkHttp3 library


//    private static final HttpClient client = new OkHttpClient();
//    private static final String API_KEY = "PASTE_YOUR_OPENAI_API_KEY_HERE";
//    private static final String API_URL = "https://api.openai.com/v1/audio/speech";
//
//    public void synthesizeSpeech(String inputText, String outputFileName) throws IOException {
//        JSONObject json = new JSONObject();
//        json.put("model", "tts-1");
//        json.put("input", inputText);
//        json.put("voice", "alloy");
//        // Optional parameters like 'response_format' and 'speed' can be added if needed
//
//        RequestBody body = RequestBody.create(
//                json.toString(),
//                MediaType.parse("application/json; charset=utf-8")
//        );
//
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .header("Authorization", "Bearer " + API_KEY)
//                .header("Content-Type", "application/json")
//                .post(body)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                // Print error message from response body
//                System.err.println("Error: " + response.body().string());
//                throw new IOException("Unexpected code " + response);
//            }
//
//            // Ensure the output directory exists
//            File outputFile = new File(outputFileName);
//            outputFile.getParentFile().mkdirs();
//
//            // Save the audio file content to the output file
//            try (InputStream inputStream = response.body().byteStream();
//                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {
//
//                byte[] buffer = new byte[8192];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//
//                System.out.println("Audio file saved successfully: " + outputFileName);
//            }
//        }
//    }
}
