package test;

import java.io.File;

public class DockerConstants extends TestConstants{
    static {
        DESIGNDECISION_FILE_PATH = "file:/home/pcm/simpleexample.designdecision";
        //Does not work:
//        DESIGNDECISION_FILE_PATH = "file:///" +    System.getProperty("user.dir") + File.separator + "pcm" + File.separator + "simpleexample.designdecision";
        QML_FILE_PATH =                         System.getProperty("user.dir") + File.separator + "pcm" + File.separator + "default.profile.qmldeclarations";

        USAGE_FILE_PATH =                       System.getProperty("user.dir") + File.separator + "pcm" + File.separator + "default.usagemodel";
        REPOSITORY_FILE_PATH =                  System.getProperty("user.dir") + File.separator + "pcm" + File.separator + "default.repository";
        RESOURCE_ENVIRONMENT_FILE_PATH =        System.getProperty("user.dir") + File.separator + "pcm" + File.separator + "default.resourceenvironment";
        SYSTEM_FILE_PATH =                      System.getProperty("user.dir") + File.separator + "pcm" + File.separator + "default.system";
        ALLOCATION_FILE_PATH =                  System.getProperty("user.dir") + File.separator + "pcm" + File.separator + "default.allocation";

        PCM_MODEL_FILES = "file:/" +            System.getProperty("user.dir") + File.separator + "pcm" + File.separator + "defaultModels" + File.separator;
        LQN_OUTPUT =                            System.getProperty("user.dir") + File.separator + "Bot_out" + File.separator + "pcmtests" + File.separator + "lqnout";
        PCM_STORAGE_PATH =                      System.getProperty("user.dir") + File.separator + "Bot_out" + File.separator + "pcmtests" + File.separator + "testspace";
    }
}
