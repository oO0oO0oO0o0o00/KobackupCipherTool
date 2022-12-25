package commands;

import com.github.rvesse.airline.annotations.AirlineModule;
import com.github.rvesse.airline.annotations.Command;
import irsl.Main;
import irsl.crypto.backupsecurityv3.C0911b_CheckMsgV3;
import irsl.crypto.backupsecurityv3.KobackupBackupSecurityv3Cipher;

@Command(name = "encrypt", description = "Encrypt backup file(s) back so that it can be restored correctly.")
public class EncryptCommand extends CommonCommand implements Runnable {
    @AirlineModule
    public OutputArg output;

    @Override
    public void run() {
        if (!output.postValidateConflict(input) || !output.postValidateExists(force)) {
            System.exit(Main.EXIT_STATUS_ERROR);
        }
        try {
            KobackupBackupSecurityv3Cipher ksc = KobackupBackupSecurityv3Cipher.createNew(password, encryptionMode);
            ksc.EncryptFile(input, output.value);
            System.out.printf("encMsgV3: %s%n", ksc.getEncMsgV3());
            String checkMsgV3 = C0911b_CheckMsgV3.CalculateCheckMsgV3(password, output.value);
            System.out.printf("checkMsgV3: %s%n", checkMsgV3);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
