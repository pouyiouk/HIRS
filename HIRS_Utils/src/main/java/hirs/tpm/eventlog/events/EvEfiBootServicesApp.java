package hirs.tpm.eventlog.events;

import java.io.UnsupportedEncodingException;

import hirs.tpm.eventlog.uefi.UefiConstants;
import hirs.tpm.eventlog.uefi.UefiDevicePath;
import hirs.utils.HexUtils;

/**
 * Class to process the PC Client Firmware profile defined EV_EFI_BOOT_SERVICES_APPLICATION event.
 * The EV_EFI_BOOT_SERVICES_APPLICATION event data contains the UEFI_IMAGE_LOAD_EVENT structure:
 * struct tdUEFI_IMAGE_LOAD_EVENT {
 *    UEFI_PHYSICAL_ADDRESS   ImageLocationInMemory; // PE/COFF image same as UINT64
 *    UINT64                  ImageLengthInMemory;
 *    UINT64                  ImageLinkTimeAddress;
 *    UINT64                  LengthOfDevicePath;
 *    UEFI_DEVICE_PATH        DevicePath[LengthOfDevicePath];  // See UEFI spec for the encodings.
 *  } UEFI_IMAGE_LOAD_EVENT;
 *
 *  DEVICE_PATH_PROTOCOL from the UEFI spec Section 10.1 page 284 of v2.8
 *
 *  #define EFI_DEVICE_PATH_PROTOCOL_GUID \09576e91-6d3f-11d2-8e39-00a0c969723b
 *  typedef struct _EFI_DEVICE_PATH_PROTOCOL {
 *      UINT8 Type;
 *      UINT8 SubType;
 *      UINT8 Length[2];
 *    } EFI_DEVICE_PATH_PROTOCOL;    // ref page of the UEFI spec
 *
 *    Where Type and Subtype are defined the UEFI spec section 10.3.1
 *    Type 0x01  Hardware Device Path
 *    Type 0x02  ACPI Device Path
 *    Type 0x03  Messaging Device Path
 *    Type 0x04  Media Device Path
 *    Type 0x05  BIOS Boot Specification Device Path
 *    Type 0x7F  End of Hardware Device Path
 */
public class EvEfiBootServicesApp {
   /** UEFI Address. */
   private byte[] physicalAddress = null;
   /** UEFI Image Length. */
   private int imageLength = 0;
   /** UEFI Link Time image address. */
   private byte[] linkTimeAddress = null;
   /** UEFI Device Path Length. */
   private int devicePathLength = 0;
   /** UEFI Device path. */
   private UefiDevicePath devPath = null;
   /** Is the Device Path Valid. */
   private boolean devicePathValid = false;

   /**
    * EvEFIBootServicesApp constructor.
    * @param bootServices byte array holding the event data.
    * @throws UnsupportedEncodingException if parsing issues exists.
    */
   public EvEfiBootServicesApp(final byte[] bootServices) throws UnsupportedEncodingException {
     physicalAddress = new byte[UefiConstants.SIZE_8];
     System.arraycopy(bootServices, 0, physicalAddress, 0, UefiConstants.SIZE_8);
     byte[] lengthBytes = new byte[UefiConstants.SIZE_8];
     System.arraycopy(bootServices, UefiConstants.OFFSET_8, lengthBytes, 0, UefiConstants.SIZE_8);
     imageLength = HexUtils.leReverseInt(lengthBytes);
     linkTimeAddress = new byte[UefiConstants.SIZE_8];
     System.arraycopy(bootServices, UefiConstants.OFFSET_16, linkTimeAddress, 0,
                                                                           UefiConstants.SIZE_8);
     System.arraycopy(bootServices, UefiConstants.SIZE_24, lengthBytes, 0, UefiConstants.SIZE_8);
 //      if (imageLength != 0) {
       devicePathLength = HexUtils.leReverseInt(lengthBytes);
       if (devicePathLength != 0) {
           byte[] devPathBytes = new byte[devicePathLength];
           System.arraycopy(bootServices, UefiConstants.SIZE_32, devPathBytes,
                                                                        0, devicePathLength);
           devPath = new UefiDevicePath(devPathBytes);
           devicePathValid = true;
        }
  }

  /**
   * Returns the address of the physical image of the boot services application.
   * @return address of the physical image.
   */
   public byte[] getImagePhysicalAddress() {
     return java.util.Arrays.copyOf(physicalAddress, physicalAddress.length);
   }

  /**
   * Returns the length of a link time image referenced by this event.
   * @return length of the link time image.
   */
   public byte[] getImageLinkTimeAddress() {
     return java.util.Arrays.copyOf(linkTimeAddress, linkTimeAddress.length);
   }

  /**
   * Returns the length of an image referenced by this event.
   * @return length of the image.
   */
   public int getImageLength() {
     return imageLength;
   }

  /**
   * Returns the length of the device path.
   * @return device path length.
   */
   public int getDevicePathLength() {
    return devicePathLength;
   }

  /**
   * Provide a uefi device path object contained in the event.
   * @return UefiDevicePath object.
   */
   public UefiDevicePath getDevicePath() {
      return devPath;
   }

/**
 * Returns a human readable string of the Boot Service info.
 * @return  a human readable string.
 */
  public String toString() {
    String info = "Image info: ";
    info += "  Image physical address: " + HexUtils.byteArrayToHexString(physicalAddress);
    info += " Image length = " + imageLength;
    info += " Image link time address: " + HexUtils.byteArrayToHexString(physicalAddress);
    info += " Device path length = " + devicePathLength;
    if (devicePathValid) {
        info += "\n" + devPath.toString();
    } else {
        info += "\n   No uefi device paths were specified";
    }
    return info;
    }
}
