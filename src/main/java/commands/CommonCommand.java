package commands;

import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.*;

public class CommonCommand {
    protected static final String DESCRIPTION_SINGLE_FILE_OPTIONS = "Must be provided if the input is a file, ignored otherwise.";
    @Required
    @Path(mustExist = true)
    @Option(name = {"-i", "--input"},
            description = "Path to a single backup file (e.g. tarball of a certain app's data)" +
                    " or backup directory (where \"info.xml\" and \"backupinfo.ini\" reside in).")
    protected String input;

    @Required
    @Option(name = {"-p", "--password"},
            description = "User-specified password of the backup.")
    protected String password;

    @Option(name = {"-f", "--force"},
            description = "Overwrite any existing files.")
    protected boolean force;

    @AllowedRawValues(allowedValues = {"0", "1"})
    @Option(name = {"-m", "--encryption-mode"},
            description = "Overwrite any existing files. " + DESCRIPTION_SINGLE_FILE_OPTIONS)
    protected int encryptionMode;
}
