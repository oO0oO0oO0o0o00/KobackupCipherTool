package commands;

import com.github.rvesse.airline.annotations.AirlineModule;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import irsl.Main;
import irsl.crypto.backupsecurityv3.KobackupBackupSecurityv3Cipher;

import java.security.InvalidAlgorithmParameterException;

@Command(name = "decrypt", description = "Decrypt the given file(s) so that the content can be extracted and modified.")
public class DecryptCommand extends CommonCommand implements Runnable {
    @AirlineModule
    public OutputArg output;

    @Option(name = "--enc_msg", description = "\"encMsgV3\" for the file, defined in \"info.xml\". " +
            DESCRIPTION_SINGLE_FILE_OPTIONS)
    public String encMsgV3;

    @Override
    public void run() {
        if (!output.postValidateConflict(input) || !output.postValidateExists(force)) {
            System.exit(Main.EXIT_STATUS_ERROR);
        }
        try {
            var ksc = KobackupBackupSecurityv3Cipher.init(password, encryptionMode, encMsgV3);
            System.out.printf("Decrypting file %s...\n", input);
            ksc.DecryptFile(input, output.value);
            System.out.printf("Output written to %s. Please check its validity.", output.value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
