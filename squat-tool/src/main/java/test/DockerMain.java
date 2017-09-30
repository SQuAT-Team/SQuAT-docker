package test;

import java.io.IOException;

public class DockerMain {
    public static void main(String[] args) throws IOException {
        new SQuATMain(new DockerConstants());
        SQuATMain.main(args);
    }

}
