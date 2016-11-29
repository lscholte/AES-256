package ca.uvic.lscholte.aes;

import javax.xml.bind.DatatypeConverter;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the AESCipher class
 * 
 * @author lscholte
 */
public class AESCipherTest {
    
    /**
     * Test of encrypt method, of class AESCipher.
     *
     * Test data taken from http://csrc.nist.gov/publications/fips/fips197/fips-197.pdf
     */
    @Test
    public void testEncrypt_byteArr() {
        byte[] input = DatatypeConverter.parseHexBinary("00112233445566778899aabbccddeeff");
        byte[] key = DatatypeConverter.parseHexBinary("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");        
        byte[] expResult = DatatypeConverter.parseHexBinary("8ea2b7ca516745bfeafc49904b496089");
        
        AESCipher instance = new AESCipher(key);

        byte[] result = instance.encrypt(input);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of decrypt method, of class AESCipher.
     * 
     * Test data taken from http://csrc.nist.gov/publications/fips/fips197/fips-197.pdf
     */
    @Test
    public void testDecrypt_byteArr() {
        byte[] input = DatatypeConverter.parseHexBinary("8ea2b7ca516745bfeafc49904b496089");
        byte[] key = DatatypeConverter.parseHexBinary("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");        
        byte[] expResult = DatatypeConverter.parseHexBinary("00112233445566778899aabbccddeeff");
        
        AESCipher instance = new AESCipher(key);

        byte[] result = instance.decrypt(input);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of encrypt method, of class AESCipher.
     * 
     * Test data taken from http://csrc.nist.gov/publications/fips/fips197/fips-197.pdf
     */
    @Test
    public void testEncrypt_String() {
        String input = "00112233445566778899aabbccddeeff";
        String key = "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f";        
        String expResult = "8ea2b7ca516745bfeafc49904b496089";
        
        AESCipher instance = new AESCipher(key);

        String result = instance.encrypt(input);
        assertTrue(expResult.equalsIgnoreCase(result));
    }

    /**
     * Test of decrypt method, of class AESCipher.
     * 
     * Test data taken from http://csrc.nist.gov/publications/fips/fips197/fips-197.pdf
     */
    @Test
    public void testDecrypt_String() {
        String input = "8ea2b7ca516745bfeafc49904b496089";
        String key = "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f";        
        String expResult = "00112233445566778899aabbccddeeff";
        
        AESCipher instance = new AESCipher(key);

        String result = instance.decrypt(input);        
        assertTrue(expResult.equalsIgnoreCase(result));
    }
    
}
