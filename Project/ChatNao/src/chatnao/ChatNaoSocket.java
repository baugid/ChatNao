package chatnao;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.Tuple4;
import com.aldebaran.qi.helper.proxies.ALAudioRecorder;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import javaFlacEncoder.FLAC_FileEncoder;
import okhttp3.*;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.err;


/**
 * @author Kasukoi (Sebastian Grohs)
 * also uses code by: Gideon Baur, Leona Maehler, Max Krass
 */
public class ChatNaoSocket {

    private static String result;
    private static Application app;
    private static Session s;
    private static ALTextToSpeech tts;
    private static ALAudioRecorder rec;
    private String script;

    public static void main(String[] args) {
        String ip = args[0];

        // opening Nao Session with given ip and port
        app = new Application(args, "tcp://" + ip + ":9559");
        System.out.println("Trying to connect to 'tcp://" + ip + "'...");
        app.start();
        s = app.session();
        try {
            tts = new ALTextToSpeech(s);
        } catch (Exception ex) {
            Logger.getLogger(ChatNaoSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) try {
            //record audio with Nao (channel 3), saving it at it's local storage
            ALAudioRecorder rec = new ALAudioRecorder(s);
            System.out.println("Press a key to start recording!");
            int input = System.in.read();
            System.out.print("Recording...");
            rec.startMicrophonesRecording("/home/nao/record.wav", "wav", 16000, new Tuple4<>(0, 0, 1, 0));
            Thread.sleep(3000);
            rec.stopMicrophonesRecording();
            System.out.println("stopped.");

            //getting audio file from Nao
            URL url = new URL("ftp://nao:nao@" + ip + "/record.wav");
            URLConnection urlc = url.openConnection();
            InputStream is = urlc.getInputStream();

            //converting file from wav to flac
            File targetFile = new File("\\res\\audio-file.wav");
            FileUtils.copyInputStreamToFile(is, targetFile);

            FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();
            File outputFile = new File("\\res\\audio-file.flac");

            flacEncoder.encode(targetFile, outputFile);

            run();
        } catch (IOException e) {
            err.println(e);
        } catch (Exception ex) {
            Logger.getLogger(ChatNaoSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void run() {
        //POSTing audio file to Watson's STT API (see WatsonDeveloperCloud)
        SpeechToText service = new SpeechToText();
        service.setUsernameAndPassword("aef1eb82-3731-4160-97ba-06bfa8f3ee48", "OBGIlDZwGDEQ");

        InputStream audio0 = null;
        try {
            audio0 = new FileInputStream("\\res\\audio-file.flac");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RecognizeOptions options = new RecognizeOptions.Builder()
                .interimResults(true)
                .contentType(HttpMediaType.AUDIO_FLAC)
                .build();

        WebSocket script = service.recognizeUsingWebSocket(audio0,options,new BaseRecognizeCallback(){
            @Override
            public void onTranscription(SpeechResults speechResults){
                System.out.println(speechResults);
            }
        });

        // wait 20 seconds for the asynchronous response
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String s = script.toString();
        // end of POST Request. Got transcript as string

        String search = "transcript";
        boolean possible;

        for (int i = 0; i < s.length(); i++) {
            possible = true;
            for (int j = 0; j < search.length(); j++) {
                if (s.charAt(i + j) != search.charAt(j)) {
                    possible = false;
                    break;
                }
            }
            if (possible) { //found possible String in transcript
                result = s.substring(i + search.length() + 4, s.length() - 32); //cutting away space before and after String
                System.out.println(result);

                //POSTing String to local Chatbot server
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "text=" + result);
                Request request = new Request.Builder()
                        .url("http://localhost:2001/")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    //got response from Chatbot; calling say with response
                    say(response.body().string());
                } catch (IOException ex) {
                    Logger.getLogger(ChatNaoSocket.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            } else { //no possible String found => Watson didn't understand what user said
                say("Sorry, I didn't understand. Could you say that again, please?");
            }
        }
    }

    public static void say(String s) {
        //using Nao's TTS to let it say the response
        try {
            tts.say(s);
            System.out.println(s);
        } catch (CallError | InterruptedException ex) {
            Logger.getLogger(ChatNaoSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
