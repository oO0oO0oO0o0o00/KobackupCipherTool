package irsl;

import com.github.rvesse.airline.annotations.Cli;
import com.github.rvesse.airline.annotations.Parser;
import com.github.rvesse.airline.help.cli.CliGlobalUsageGenerator;
import com.github.rvesse.airline.help.cli.CliCommandUsageGenerator;
import commands.DecryptCommand;
import commands.EncryptCommand;
import commands.ExceptionHandler;

import java.io.IOException;

@Cli(name = "kobactool",
        commands = {EncryptCommand.class, DecryptCommand.class},
        description = "A commandline tool that decrypts backup files of Huawei's HiSuite and KoBackup. " +
                "Encryption and verification are also supported.",
        parserConfiguration = @Parser(errorHandler = ExceptionHandler.class)
)
public class Main {

    public static final int EXIT_STATUS_ERROR = 1;

    public static void main(String[] args) throws IOException {
        com.github.rvesse.airline.Cli<Runnable> cli = new com.github.rvesse.airline.Cli<>(Main.class);
        var result = cli.parseWithResult(args);
        if (result.wasSuccessful()) {
            result.getCommand().run();
            System.exit(0);
        }
        for (var error : result.getErrors()) System.err.println(error.getLocalizedMessage());
        var command = result.getState().getCommand();
        if (command == null) {
            new CliGlobalUsageGenerator<Runnable>().usage(cli.getMetadata(), System.err);
        } else {
            new CliCommandUsageGenerator().usage(command, result.getState().getParserConfiguration(), System.err);
        }
        System.exit(EXIT_STATUS_ERROR);
    }
}
