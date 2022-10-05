import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Main {
    public static final String ROOT = "root@username:";
    public static final String POINTER = "> ";
    public static final String INCORRECT_INPUT_CMD = "Seems like this command is incorrect or unavailable";

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        ZipFile zipFile = new ZipFile(args[0]); // get Zip file

        Object[] arr = zipFile.stream().toArray(); // get List of all direct-s
        ArchWorker archWorker = new ArchWorker(zipFile, arr);

        String currentDirectory = arr[0].toString();


        while (true) {
            System.out.print(ROOT + currentDirectory + POINTER);

            String cmd = in.nextLine(); // get command
            String[] commands = cmd.split(" ");

            if (commands.length > 2) {
                errorMsg();
                continue;
            }

            switch (commands[0]) {
                case "pwd" -> {
                    String curWrkPath = archWorker.pwd(commands);
                    System.out.println(ROOT + curWrkPath + POINTER);
                }
                case "ls" -> {
                    String[] lsRes = archWorker.ls(commands);
                    for (String lsRe : lsRes) {
                        System.out.println(lsRe);
                    }
                }
                case "cd" -> {
                    String tmp = archWorker.cd(commands);
                    if (tmp.equals("-1")) errorMsg();
                    else  currentDirectory = archWorker.cd(commands);
                }
                case "cat" -> {
                    String text = archWorker.cat(commands);
                    System.out.println(text);
                }
                case "q" -> {
                    System.exit(0);
                }
                default -> System.out.println(INCORRECT_INPUT_CMD);
            }
        }
    }

    private static void errorMsg() {
        System.out.println(INCORRECT_INPUT_CMD);
    }
}