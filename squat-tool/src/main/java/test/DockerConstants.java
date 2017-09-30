package test;

import java.io.File;

public class DockerConstants extends TestConstants{
    static {
        DESIGNDECISION_FILE_PATH = "file:/home/pcm/simpleexample.designdecision";
        //Does not work:
//        DESIGNDECISION_FILE_PATH = "file:///" +    System.getProperty("user.dir") + File.separator + "pcm" + File.separator + "simpleexample.designdecision";
        QML_FILE_PATH =                         File.separator + "home" + File.separator + "pcm" + File.separator + "default.profile.qmldeclarations";

        USAGE_FILE_PATH =                       File.separator + "home" + File.separator + "pcm" + File.separator + "default.usagemodel";
        REPOSITORY_FILE_PATH =                  File.separator + "home" + File.separator + "pcm" + File.separator + "default.repository";
        RESOURCE_ENVIRONMENT_FILE_PATH =        File.separator + "home" + File.separator + "pcm" + File.separator + "default.resourceenvironment";
        SYSTEM_FILE_PATH =                      File.separator + "home" + File.separator + "pcm" + File.separator + "default.system";
        ALLOCATION_FILE_PATH =                  File.separator + "home" + File.separator + "pcm" + File.separator + "default.allocation";
        ALTERNATIVE_REPOSITORY_PATH =           File.separator + "home" + File.separator + "pcm" + File.separator + "alternativeRepository.allocation";

        PCM_MODEL_FILES = "file:/" + "home" + 	File.separator + "pcm" + File.separator + "defaultModels" + File.separator;
        LQN_OUTPUT =                            File.separator + "home" + File.separator + "Bot_out" + File.separator + "pcmtests" + File.separator + "lqnout";
        PCM_STORAGE_PATH =                      File.separator + "home" + File.separator + "Bot_out" + File.separator + "pcmtests" + File.separator + "testspace";
    }
}
