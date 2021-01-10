package hirs.tpm.eventlog;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
//import java.util.List;
//import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.hibernate.Session;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

//import hirs.data.persist.Baseline;
//import hirs.data.persist.Digest;
//import hirs.data.persist.SpringPersistenceTest;
//import hirs.data.persist.TpmWhiteListBaseline;
//import hirs.utils.HexUtils;

/**
 *  Class for testing TCG Event Log processing.
 */
//public class TCGEventLogTest extends SpringPersistenceTest {
public class TCGEventLogTest {
   private static final String DEFAULT_EVENT_LOG = "/tcgeventlog/TpmLog.bin";
   private static final String DEFAULT_EXPECTED_PCRS = "/tcgeventlog/TpmLogExpectedPcrs.txt";
   private static final String SHA1_EVENT_LOG = "/tcgeventlog/TpmLogSHA1.bin";
   private static final String SHA1_EXPECTED_PCRS = "/tcgeventlog/TpmLogSHA1ExpectedPcrs.txt";
   private static final Logger LOGGER
            = LogManager.getLogger(TCGEventLogTest.class);

   /**
    * Initializes a <code>SessionFactory</code>. The factory is used for an in-memory database that
    * is used for testing.
    */
   @BeforeClass
   public static final void setup() {
       LOGGER.debug("retrieving session factory");

   }

   /**
    * Closes the <code>SessionFactory</code> from setup.
    */
   @AfterClass
   public static final void tearDown() {
       LOGGER.debug("closing session factory");
   }

   /**
    * Resets the test state to a known good state. This currently only resets the database by
    * removing all <code>Baseline</code> objects.
    */
  // @AfterMethod
 /*
   public final void resetTestState() {
       LOGGER.debug("reset test state");
       LOGGER.debug("deleting all baselines");
       Session session = sessionFactory.getCurrentSession();
       session.beginTransaction();
       final List<?> baselines = session.createCriteria(Baseline.class).list();
       for (Object o : baselines) {
           LOGGER.debug("deleting baseline: {}", o);
           session.delete(o);
       }
       LOGGER.debug("all baselines removed");
       session.getTransaction().commit();
   }
*/
   /**
    * Tests the processing of a crypto agile event log.
    * @throws IOException when processing the test fails
    * @throws NoSuchAlgorithmException if an unknown algorithm is encountered.
    * @throws CertificateException if a certificate fails to parse.
    */
   @Test
   public final void testCryptoAgileTCGEventLog() throws IOException, CertificateException,
                                                                 NoSuchAlgorithmException {
     LOGGER.debug("Testing the parsing of a Crypto Agile formatted TCG Event Log");
     InputStream log, pcrs;
     boolean testPass = true;
     log = this.getClass().getResourceAsStream(DEFAULT_EVENT_LOG);
     byte[] rawLogBytes = IOUtils.toByteArray(log);
     TCGEventLog evlog = new TCGEventLog(rawLogBytes, false, false, false);
     String[] pcrFromLog = evlog.getExpectedPCRValues();
     pcrs = this.getClass().getResourceAsStream(DEFAULT_EXPECTED_PCRS);
     Object[] pcrObj = IOUtils.readLines(pcrs).toArray();
     String[] pcrTxt = Arrays.copyOf(pcrObj, pcrObj.length, String[].class);
     // Test 1 get all PCRs
     for (int i = 0; i < 24; i++) {
         if (pcrFromLog[i].compareToIgnoreCase(pcrTxt[i]) != 0) {
             testPass = false;
             LOGGER.error("\ntestTCGEventLogProcessorParser error with PCR " + i);
         }
      }
      Assert.assertTrue(testPass);
      // Test 2 get an individual PCR
      String pcr3 = evlog.getExpectedPCRValue(3);
      Assert.assertEquals(pcr3, pcrFromLog[3]);
      // Test 3 check the Algorithm Identifiers used in the log
      String algStr = evlog.getEventLogHashAlgorithm();
      Assert.assertEquals(algStr, "TPM_ALG_SHA256");
      int id = evlog.getEventLogHashAlgorithmID();
      Assert.assertEquals(id, TcgTpmtHa.TPM_ALG_SHA256);
      LOGGER.debug("OK. Parsing of a Crypto Agile Format Success");
    }

   /**
    * Tests the processing of a SHA1 formatted Event log.
    * @throws IOException when processing the test fails
    * @throws NoSuchAlgorithmException if an unknown algorithm is encountered.
    * @throws CertificateException if a certificate fails to parse.
    */
    @Test
    public final void testSHA1TCGEventLog() throws IOException, CertificateException,
                                                           NoSuchAlgorithmException {
      LOGGER.debug("Testing the parsing of a SHA1 formated TCG Event Log");
      InputStream log, pcrs;
      boolean testPass = true;
      log = this.getClass().getResourceAsStream(SHA1_EVENT_LOG);
      byte[] rawLogBytes = IOUtils.toByteArray(log);
      TCGEventLog evlog =  new TCGEventLog(rawLogBytes, false, false, false);
      String[] pcrFromLog = evlog.getExpectedPCRValues();
      pcrs = this.getClass().getResourceAsStream(SHA1_EXPECTED_PCRS);
      Object[] pcrObj = IOUtils.readLines(pcrs).toArray();
      String[] pcrTxt = Arrays.copyOf(pcrObj, pcrObj.length, String[].class);
      // Test 1 get all PCRs
       for (int i = 0; i < 24; i++) {
          if (pcrFromLog[i].compareToIgnoreCase(pcrTxt[i]) != 0) {
             testPass = false;
             LOGGER.error("\ntestTCGEventLogProcessorParser error with PCR " + i);
           }
       }
       Assert.assertTrue(testPass);
       // Test 2 get an individual PCR
       String pcr0 = evlog.getExpectedPCRValue(0);
       Assert.assertEquals(pcr0, pcrFromLog[0]);
       // Test 3 check the Algorithm Identifiers used in the log
       String algStr = evlog.getEventLogHashAlgorithm();
       Assert.assertEquals(algStr, "TPM_ALG_SHA1");
       int id = evlog.getEventLogHashAlgorithmID();
       Assert.assertEquals(id, TcgTpmtHa.TPM_ALG_SHA1);
       LOGGER.debug("OK. Parsing of a SHA1 formatted TCG Event Log Success");
      }

    /**
     * Tests TPM Baseline creation from a EventLog.
     * @throws IOException when processing the test fails
     */
    //@Test
    /*
    public final void testTPMBaselineCreate() throws IOException {
        LOGGER.debug("Create and save TPM baseline from TCG Event Log test started");
        InputStream log;
        boolean testPass = true;
        log = this.getClass().getResourceAsStream(DEFAULT_EVENT_LOG);
        byte[] rawLogBytes = IOUtils.toByteArray(log);
        TCGEventLogProcessor tlp = new TCGEventLogProcessor(rawLogBytes);
        String[] pcrFromLog = tlp.getExpectedPCRValues();
        // save it to the test db
        LOGGER.debug("Creating and saving a TPM baseline from TCG Event Log");
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        final TpmWhiteListBaseline b = tlp.createTPMBaseline("TcgEventLogTestBaseline");
        session.save(b);
        session.getTransaction().commit();
        // Check that the TPM Baseline contains he correct info
        for (int i = 0; i < 24; i++) {
            Set<Digest> records = b.getPCRHashes(i);
            for (Digest digest:records) {
                String pcrValue = HexUtils.byteArrayToHexString(digest.getDigest());
                if (pcrFromLog[i].compareToIgnoreCase(pcrValue) != 0) {
                    testPass = false;
                    LOGGER.error("\testTPMBaselineCreate error with PCR " + i);
                }
             }
         }
        Assert.assertTrue(testPass);
        LOGGER.debug("OK. Create and save TPM baseline from TCG Event Log was a success");
    }
    */
}
