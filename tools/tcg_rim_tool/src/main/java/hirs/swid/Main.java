package hirs.swid;

import hirs.swid.utils.Commander;
import com.beust.jcommander.JCommander;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Commander commander = new Commander();
        JCommander jc = JCommander.newBuilder().addObject(commander).build();
        jc.parse(args);
        SwidTagGateway gateway;
        SwidTagValidator validator;

        if (commander.isHelp()) {
            jc.usage();
            System.out.println(commander.printHelpExamples());
        } else {
            if (!commander.getVerifyFile().isEmpty()) {
                validator = new SwidTagValidator();
                System.out.println(commander.toString());
                String verifyFile = commander.getVerifyFile();
                String rimel = commander.getRimEventLog();
                String certificateFile = commander.getPublicCertificate();
                if (!verifyFile.isEmpty()) {
                    if (!rimel.isEmpty()) {
                        validator.setRimEventLog(rimel);
                    }
                    if (!certificateFile.isEmpty()) {
                        validator.setCertificateFile(certificateFile);
                    }
                    try {
                        validator.validateSwidTag(verifyFile);
                    } catch (IOException e) {
                        System.out.println("Error validating RIM file: " + e.getMessage());
                        System.exit(1);
                    }
                } else {
                    System.out.println("Need a RIM file to validate!");
                    System.exit(1);
                }
            } else {
                gateway = new SwidTagGateway();
                System.out.println(commander.toString());
                String createType = commander.getCreateType().toUpperCase();
                String attributesFile = commander.getAttributesFile();
                String jksKeystoreFile = commander.getKeystoreFile();
                String certificateFile = commander.getPublicCertificate();
                String privateKeyFile = commander.getPrivateKeyFile();
                String rimEventLog = commander.getRimEventLog();
                switch (createType) {
                    case "BASE":
                        if (!attributesFile.isEmpty()) {
                            gateway.setAttributesFile(attributesFile);
                        }
                        if (!jksKeystoreFile.isEmpty()) {
                            gateway.setDefaultCredentials(true);
                            gateway.setJksKeystoreFile(jksKeystoreFile);
                        } else if (!certificateFile.isEmpty() && !privateKeyFile.isEmpty()) {
                            gateway.setDefaultCredentials(false);
                            gateway.setPemCertificateFile(certificateFile);
                            gateway.setPemPrivateKeyFile(privateKeyFile);
                        } else {
                            gateway.setDefaultCredentials(true);
                            gateway.setJksKeystoreFile(SwidTagConstants.DEFAULT_KEYSTORE_FILE);
                        }
                        if (rimEventLog.isEmpty()) {
                            System.out.println("Error: a support RIM is required!");
                            System.exit(1);
                        } else {
                            gateway.setRimEventLog(rimEventLog);
                        }
                        gateway.generateSwidTag(commander.getOutFile());
                        break;
                }
            }
        }
    }
}
