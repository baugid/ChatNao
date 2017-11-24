package chatnao;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.Tuple4;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALAudioRecorder;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

/**
 * @author Gideon
 */
class NaoConnection {

    private Application app;
    private ALTextToSpeech tts;
    private ALAudioRecorder rec;
    private ALMemory mem;
    private TactileHeadHandler handler;
    private long subscriptionID[] = new long[3];
    private boolean recording;

    NaoConnection(String[] args, String ip, int port) {
        app = new Application(args, "tcp://" + ip + ":" + port);
    }

    boolean start() {
        app.start();
        Session s = app.session();
        try {
            tts = new ALTextToSpeech(s);
            rec = new ALAudioRecorder(s);
            mem = new ALMemory(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    void say(String s) {
        try {
            tts.say(s);
        } catch (CallError | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void startRecording(String filename, String filetype, int samplerate, Tuple4<Integer, Integer, Integer, Integer> channels) {
        try {
            rec.startMicrophonesRecording(filename, filetype, samplerate, channels);
        } catch (CallError | InterruptedException e) {
            e.printStackTrace();
        }
        recording = true;
    }

    void endRecording() {
        if (!recording) return;
        try {
            rec.stopMicrophonesRecording();
        } catch (CallError | InterruptedException e) {
            e.printStackTrace();
        }
    }

    void setTactileHeadHandler(TactileHeadHandler handler) {
        if (this.handler != null)
            removeTactileHeadHandler();
        this.handler = handler;
        try {
            subscriptionID[0] = mem.subscribeToEvent("FrontTactileTouched", (EventCallback<Float>) handler::onTap);
            subscriptionID[1] = mem.subscribeToEvent("MiddleTactileTouched", (EventCallback<Float>) handler::onTap);
            subscriptionID[2] = mem.subscribeToEvent("RearTactileTouched", (EventCallback<Float>) handler::onTap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeTactileHeadHandler() {
        handler = null;
        try {
            mem.unsubscribeToEvent(subscriptionID[0]);
            mem.unsubscribeToEvent(subscriptionID[1]);
            mem.unsubscribeToEvent(subscriptionID[2]);
        } catch (InterruptedException | CallError e) {
            e.printStackTrace();
        }
    }

}
