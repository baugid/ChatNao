package chatnao;

import configs.Config;
import configs.Rename;

public class MainConfig implements Config {
    String ipNao;
    int portNao;
    @Rename("audio_Source_path")
    String fileOnNao;
    @Rename("audio_Source_type")
    String fileTypeOnNao;
    int samplerate;
    @Rename("originalFileName(PC)")
    String originalFileName;
    String ftpFilePath;
    @Rename("convertedFileName(PC)")
    String convertedFileName;
    String botAddress;
    String textToSpeechAddress;
    String textToSpeechPassword;
    String textToSpeechUsername;

    @Override
    public String getKeyValueDivider() {
        return ":";
    }
}
