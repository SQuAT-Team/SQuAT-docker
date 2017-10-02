package test;

import java.io.File;

public class DockerConstants extends TestConstants{
    static {
        DESIGNDECISION_FILE_PATH = "file:/home/pcm/simpleexample.designdecision";
        //Does not work:
//        DESIGNDECISION_FILE_PATH = "file:///" +    System.getProperty("user.dir") + File.separator + "pcm" + File.separator + "simpleexample.designdecision";
    	BASIC_FILE_PATH = File.separator + "home" + File.separator + "pcm" + File.separator + "default";// + ENDING + "\\" + ENDING;
        ALTERNATIVE_REPOSITORY_PATH =           File.separator + "home" + File.separator + "pcm" + File.separator + "alternativeRepository.repository";

        QML_FILE_PATH =                         File.separator + "home" + File.separator + "pcm" + File.separator + "default.profile.qmldeclarations";

        PCM_MODEL_FILES = "file:/" + "home" + 	File.separator + "pcm" + File.separator + "defaultModels" + File.separator;
        LQN_OUTPUT =                            File.separator + "home" + File.separator + "Bot_out" + File.separator + "pcmtests" + File.separator + "lqnout";
        PCM_STORAGE_PATH =                      File.separator + "home" + File.separator + "Bot_out" + File.separator + "pcmtests" + File.separator + "testspace";
    }
}
