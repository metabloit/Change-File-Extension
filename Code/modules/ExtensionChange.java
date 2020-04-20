import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.logging.Formatter;

public class ExtensionChange {

    private int num_files=0;
    private FileHandler fileHandler;
    /*
     * Constructor
     */
    public ExtensionChange() {
        // Automatically create a custom file for writing logs and statuses
        try {
            // File used to log status and error messages
            String LOGS_FILENAME = "FILE_LOCATION";
            this.fileHandler=new FileHandler(LOGS_FILENAME);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void close()   
    {   
          this.fileHandler.close();
    }
    /*
     * Displays a message to the user.
     */
    public void display(String message, boolean isFullLine) {
        if (isFullLine) System.out.println(message);
        else System.out.print(message);
    }
    private final Logger logger=Logger.getLogger(ExtensionChange.class.getName());
    /*
     * Save a message to a logs file
     */
    public void DisplayAndLog(String message,boolean isFullLine)  {
        fileHandler.setFormatter(new CustomFormatter());

        //Check and each every file in the specified folder
        try (Stream<Path> walk = Files.walk(Paths.get("FOLDER_NAME"))) {
            List<String>original=walk.filter(Files::isDirectory)
                    .map(Path::toString).collect(Collectors.toList());

            num_files=original.size()-1;
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            logger.addHandler(fileHandler);

            for(int i=1;i<original.size();i++)
                logger.info(message+original.get(i)); //
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Check and each every file in the specified folder
        try (Stream<Path> walk = Files.walk(Paths.get("FOLDER_NAME"))) {
            List<String> result = walk.map(Path::toString)
                    //Look for the specified extension
                    .filter(f -> f.endsWith("EXTENSION")).collect(Collectors.toList());

            logger.setUseParentHandlers(false);  //Remove the console handler.
            logger.setLevel(Level.ALL);

            //Print info to the log file
            for (String s : result)
                logger.warning(message+s );
            logger.info("The following are the results after file processing: \n");
            logger.info("The number of checked folders are: "+num_files);
            logger.warning("The number of infected files are: "+result.size());

            if(isFullLine){
                System.out.println(" ");
                System.out.println("The number of checked folders are: "+num_files);
                System.out.println("The number of infected files are: "+result.size()+" (FIXED)");
            }
            else{
                System.out.print("The number of checked folders are: "+num_files);
                System.out.print("The number of infected files are: "+result.size()+" (FIXED)");
            }

        } catch (IOException e) {
            if(isFullLine)
                System.out.println("File(s) not found!");
        }

    }

    //Change the extensions of the files
    public void fix_files(){
        try (Stream<Path> walk = Files.walk(Paths.get("FOLDER_NAME"))) {
            List<String> result = walk.map(Path::toString)
                    .filter(f -> f.endsWith("OLD_EXTENSION")).collect(Collectors.toList());

            for(int i=0;i<result.size();i++) {
                File or_file=new File(result.get(i));
                int lastDot=result.get(i).lastIndexOf(".");
                String new_result=result.get(i).substring(0,lastDot)+"NEW_EXTENSION";
                File new_file=new File(new_result);
                or_file.renameTo(new_file);
            }

        } catch (IOException e) {
            System.out.println("File(s) not found!");
        }
    }

    //Print time on the log file
    private static class CustomFormatter extends Formatter {
        private static final String format = "[%1$tF %1$tT] [%2$-4s] %3$s %n";
        public String format(LogRecord record) {
            return String.format(format, record.getMillis(),
                    record.getLevel().getLocalizedName(), record.getMessage());
        }
    }

}