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

    public String[] ls(String[] commands) {
        String[] result = {"a", "b", "c"};
        return result;
    }

    public String cd(String[] commands) {
        if (commands.length == 1) {
            currentDir = paths.get(0);
            return currentDir;
        } else {
            String targetDir = corrected(commands[1]);
            String targetPath = currentDir + targetDir;

            if (paths.contains(targetPath) || paths.contains(targetPath + "/")) {
                if (isDirectory(targetPath)) {
                    currentDir = targetPath + "/";
                    return currentDir;
                } else {
                    return Main.ERROR_CODE_2;
                }
            } else {
                return Main.ERROR_CODE_1;
            }
        }
    }

    public String cat(String[] commands) throws IOException {
        if (commands.length == 1) return "-1";

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while(entries.hasMoreElements()) {
            String result = "";
            ZipEntry entry = entries.nextElement();
            if (entry.getName().equals(currentDir + commands[1])){
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
            paths.add(o.toString());
        }
    }

    private String corrected(String item) {
        if ((item.endsWith("/")) || (item.endsWith("\\"))) {
            item = item.substring(0, item.length() - 1);
        }
        return item;
    }

    private boolean isDirectory(String path) {
        int counter = 0;
        for (String s : paths) if (s.contains(path)) counter++;
        return counter != 1;
    }
}
