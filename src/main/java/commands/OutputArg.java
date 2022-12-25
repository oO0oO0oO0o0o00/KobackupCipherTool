package commands;

import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.Required;

import java.io.File;

public class OutputArg {
    @Required
    @Option(name = {"-o", "--output"}, title = "output",
            description = "Output path. Can not be identical with the input path even if force mode is on.")
    public String value;

    public boolean postValidateConflict(String input) {
        // Not quite reliable, but simple.
        if (new File(input).getAbsolutePath().startsWith(new File(value).getAbsolutePath())) {
            System.err.println("Output path shall not conflict with the input path.");
            return false;
        }
        return true;
    }

    public boolean postValidateExists(boolean force) {
        if (!force && new File(value).exists()) {
            System.err.println("Output path must not exist unless force mode is on.");
            return false;
        }
        return true;
    }
}
