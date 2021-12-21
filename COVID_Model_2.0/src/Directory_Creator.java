import java.io.File;
import java.util.Scanner;

public class Directory_Creator {
    public static void create_dir(String path) {
        File file = new File(path);
        file.mkdirs();
    }
}