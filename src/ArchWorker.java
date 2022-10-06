import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ArchWorker {
    private String currentDir;
    private ArrayList<String> paths;
    private ZipFile zipFile;

    public ArchWorker(ZipFile zipFile, Object[] paths) {
        convert(paths);
        this.zipFile = zipFile;
        currentDir = paths[0].toString();
    }

    public String pwd(String[] commands) {
        return currentDir;
    }

    public ArrayList<String> ls(String[] commands) {
        ArrayList<String> result = new ArrayList<>();

        if (commands.length == 1) {
            result = getDirContent();
        } else {
            // TODO: -l flag
        }
        return result;
    }

    private ArrayList<String> getDirContent() {
        ArrayList<String> result = new ArrayList<>();

        for (String curPath : paths) {
            if (curPath.endsWith("/")) curPath = curPath.substring(0, curPath.length() - 1);

            if (curPath.contains(currentDir) && (!curPath.equals(currentDir)) &&
                    ((getNesting(currentDir) == getNesting(curPath)) ||
                            (((getNesting(curPath) - getNesting(currentDir)) == 1) && curPath.endsWith("/")))
            ) {
                int tmpIndex = curPath.lastIndexOf("/");
                result.add(curPath.substring(tmpIndex));
            }
        }
        return result;
    }

    private int getNesting(String str) {
        int counter = 0;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == '/') counter++;
        return counter;
    }

    public String cd(String[] commands) {
        if (commands.length == 1) {
            currentDir = paths.get(0);
            return currentDir;
        } else {
            String targetDir = commands[1];
            targetDir = targetDir.replaceAll("\\\\", "/"); // 1-st correction

            if (!targetDir.contains(".")) {
                if (!targetDir.endsWith("/")) targetDir += "/"; // 2-d correction
            } else {
                return Main.ERROR_CODE_2;
            }
            String targetPath = currentDir + targetDir;

            if (paths.contains(targetPath)) {
                currentDir = targetPath;
                return currentDir;
            } else {
                return Main.ERROR_CODE_1;
            }
        }
    }

    public String cat(String[] commands) throws IOException {
        if (commands.length == 1) return "-1";

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            String result = "";
            ZipEntry entry = entries.nextElement();
            if (entry.getName().equals(currentDir + commands[1])) {
                InputStream stream = zipFile.getInputStream(entry);

                Scanner s = new Scanner(stream).useDelimiter(" ");
                while (s.hasNext()) result += (s.next() + " ");

                return result;
            }
        }
        return "-1";
    }

    private void convert(Object[] input) {
        paths = new ArrayList<>();
        for (Object o : input) {
            String tmp = o.toString();
            tmp = tmp.replaceAll("\\\\", "/");
            paths.add(tmp);
        }
    }

    private String corrected(String item) {
        if ((item.endsWith("/")) || (item.endsWith("\\"))) {
            item = item.substring(0, item.length() - 1);
        }
        return item;
    }
}
