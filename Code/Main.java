import ExtensionChange;

class Main {
    //Driver code
    public static void main(String[] args) {
        ExtensionChange vClean = new ExtensionChange();
        vClean.DisplayAndLog("Current Path: ",true);
        //Change extensions
        vClean.fix_files();
        vClean.display("Done! ", true);
        // Close open resources
        vClean.close();
    }
}
