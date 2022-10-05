import java.util.ArrayList;
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
        if (commands.length == 1){
            currentDir = paths.get(0);
            return currentDir;
        } else {
            String targetDir = corrected(commands[1]);
            //TODO: separate dir & files |

            String targetPath = currentDir + targetDir;

            if (paths.contains(targetPath)){
                return targetPath;
            }
            return "sww";
        }
    }

    public String cat(String[] commands) {
        return "cat";
    }

    private void convert(Object[] input){
        paths = new ArrayList<>();
        for (Object o : input) {
            paths.add(o.toString());
        }
    }

    private String corrected(String item){
        return "tmp";
    }
}
