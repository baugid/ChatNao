package chatnao;

import com.aldebaran.qi.Tuple4;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import configs.Configs;
import javaFlacEncoder.FLAC_FileEncoder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Kasukoi (Sebastian Grohs)
 * also uses code by: Gideon Baur, Leona Maehler, Max Krass
 * with support from Tilman Hoffbauer
 * and refactoring by Gideon Baur
 */
public class ChatNao {

    private NaoConnection nao;
    private boolean recording;
    private boolean calculating;
    private MainConfig config;

    public ChatNao(String[] args) {
        config = new MainConfig();
        try {
            Configs.loadFromFile(config, "config.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        nao = new NaoConnection(args, config.ipNao, config.portNao);
    }

    public static void main(String[] args) {
        if (!new ChatNao(args).start()) {
            System.err.println("Error: can´t start connection to NAO!");
        }
    }

    private void toggleRecording(float a) {
        if (a > 0) {
            if (recording) {
                recording = false;
                nao.endRecording();
                calculating = true;
                handleRecord();
                calculating = false;
            } else if (!calculating) {
                recording = true;
                nao.startRecording(config.fileOnNao, config.fileTypeOnNao, config.samplerate, new Tuple4<>(0, 0, 1, 0));
            }
        }
    }

    private void copyAudioFromNao(File fileOnPc) {
        try {
            FileUtils.copyInputStreamToFile(new URL("ftp://nao:nao@" + config.ipNao + config.ftpFilePath).openConnection().getInputStream(), fileOnPc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertToFlac(File output, File input) {
        new FLAC_FileEncoder().encode(input, output);
    }

    private String getResponse(String text) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "text=" + text);
        Request request = new Request.Builder()
                .url(config.botAddress)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .build();
        try {
            //noinspection ConstantConditions
            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "I don´t know what to say!";
    }

    private void handleRecord() {
        File encodedFile = new File(config.convertedFileName);
        File sourceFile = new File(config.originalFileName);
        copyAudioFromNao(sourceFile);
        convertToFlac(encodedFile, sourceFile);
        String text = extractResult(FlacToText(encodedFile));
        if (text == null) {
            System.err.println("Unable to understand what human being said!");
            nao.say("Sorry, I didn't understand. Could you say that again, please?");
        } else {
            System.out.println(text);
            nao.say(getResponse(text));
        }
    }

    private String extractResult(String text) {
        final String searchTag = "transcript";
        int pos = text.indexOf(searchTag);
        if (pos != -1)
            return text.substring(pos + searchTag.length() + 4, text.length() - 32);
        else
            return null;
    }

    private String FlacToText(File flacFile) {
        SpeechToText service = new SpeechToText();
        service.setUsernameAndPassword(config.textToSpeechUsername, config.textToSpeechPassword);
        service.setEndPoint(config.textToSpeechAddress);

        RecognizeOptions options = new RecognizeOptions.Builder()
                .contentType(HttpMediaType.AUDIO_FLAC)
                .build();

        SpeechResults transcript = service.recognize(flacFile, options).execute();
        return transcript.toString();
    }

    private boolean start() {
        if (!nao.start()) return false;
        nao.setTactileHeadHandler(this::toggleRecording);
        return true;
    }

}
