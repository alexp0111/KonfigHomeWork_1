import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.ZipFile;

public class Main {

    // error codes
    public static final String ERROR_CODE_1 = "-1";
    public static final String ERROR_CODE_2 = "-2";

    // output
    public static final String ROOT = "root@username:";
    public static final String POINTER = "> ";

    // error messages
    public static final String INCORRECT_INPUT_CMD = "Seems like this command is incorrect or unavailable";
    public static final String INPUT_NOT_DIRECTORY = "Seems like argument is not a directory";
    public static final String DEFAULT_OUTPUT = "Unknown error";

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        ZipFile zipFile = new ZipFile(args[0]); // get Zip file

        Object[] arr = zipFile.stream().toArray(); // get List of all direct-s
        ArrayList<String> converted = convert(arr);
        ArchWorker archWorker = new ArchWorker(zipFile, converted);

        String currentDirectory = converted.get(0);


        while (true) {
            System.out.print(ROOT + currentDirectory + POINTER);

            String cmd = in.nextLine(); // get command
            String[] commands = cmd.split(" ");

            if (commands.length > 2) {
                errorMsg(INCORRECT_INPUT_CMD);
                continue;
            }

            switch (commands[0]) {
                case "pwd" -> {
                    String curWrkPath = archWorker.pwd(commands);
                    System.out.println("Current directory is: " + curWrkPath);
                }
                case "ls" -> {
                    ArrayList<String> lsRes = archWorker.ls(commands);
                    if (lsRes.size() == 0) {
                        errorMsg(ERROR_CODE_1);
                    }
                    for (String lsRe : lsRes) {
                        System.out.println(lsRe);
                    }
                }
                case "cd" -> {
                    String tmp = archWorker.cd(commands);
                    if (tmp.charAt(0) == '-') errorMsg(tmp);
                    else  currentDirectory = tmp;
                }
                case "cat" -> {
                    String text = archWorker.cat(commands);
                    if (text.equals(ERROR_CODE_1) || text.equals(ERROR_CODE_2)) errorMsg(text);
                    else System.out.println(text);
                }
                case "q" -> {
                    System.exit(0);
                }
                default -> System.out.println(INCORRECT_INPUT_CMD);
            }
        }
    }

    private static void errorMsg(String code) {
        switch (code) {
            case ERROR_CODE_1 -> System.out.println(INCORRECT_INPUT_CMD);
            case ERROR_CODE_2 -> System.out.println(INPUT_NOT_DIRECTORY);
            default -> System.out.println(DEFAULT_OUTPUT);
        }
    }

    private static ArrayList<String> convert(Object[] input) {
        ArrayList<String> paths = new ArrayList<>();

        for (Object o : input) {
            String tmp = o.toString();
            tmp = tmp.replaceAll("\\\\", "/");
            paths.add(tmp);
        }

        String rootDir = input[0].toString().substring(0, input[0].toString().indexOf("/")) + "/";
        paths.add(0, rootDir);

        return paths;
    }
}