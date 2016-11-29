package ca.uvic.lscholte.aes;

import static ca.uvic.lscholte.aes.AESConstants.NB;
import static ca.uvic.lscholte.aes.AESConstants.NR;
import javax.xml.bind.DatatypeConverter;

/**
 * Provides functionality for encrypting and decrypting data using AES-256
 * 
 * @author lscholte
 */
public final class AESCipher {

    private final byte[][][] roundKeys;
    
    /**
     * Initializes an AESCipher with a 32-byte (256-bit) key
     * @param key An array of bytes of length 32 to use as the key
     */
    public AESCipher(byte[] key) {
        if (key.length != 32) {
            throw new IllegalArgumentException("The key must be 32 bytes long");
        }
        byte[][] formattedKey = KeyExpansion.formatKey(key);
        roundKeys = KeyExpansion.generateRoundKeys(formattedKey);
    }
    
    /**
     * Initializes an AESCipher with a 32-byte (256-bit) key.
     * @param key A string of length 64 containing only hexadecimal characters
     * to use as the key
     */
    public AESCipher(String key) {
        this(DatatypeConverter.parseHexBinary(key));
    }
    
    /**
     * Encrypts the input data using AES-256
     * @param input An array of bytes of length 16
     * @return The encrypted array of bytes of length 16
     */
    public byte[] encrypt(byte[] input) {        
        return cipher(input, false);
    }
    
    /**
     * Decrypts the input data using AES-256
     * @param input An array of bytes of length 16
     * @return The decrypted array of bytes of length 16
     */
    public byte[] decrypt(byte[] input) {
        return cipher(input, true);
    }
    
    /**
     * Encrypts the input data using AES-256
     * @param input A string of length 32 of hexadecimal characters (16 bytes)
     * @return The encrypted string of length 16 of hexadecimal characters
     */
    public String encrypt(String input) {
        byte[] data = cipher(DatatypeConverter.parseHexBinary(input), false);
        return DatatypeConverter.printHexBinary(data);
    }
    
    /**
     * Decrypts the input data using AES-256
     * @param input A string of length 32 of hexadecimal characters (16 bytes)
     * @return The decrypted string of length 16 of hexadecimal characters
     */
    public String decrypt(String input) {
        byte[] data = cipher(DatatypeConverter.parseHexBinary(input), true);
        return DatatypeConverter.printHexBinary(data);
    }
    /**
     * The algorithm for encrypting/decrypting data
     * @param input An array of bytes of length 16
     * @param inverse True if the input should be decrypted. False if the input
     * should be encrypted
     * @return The encrypted or decrypted array of bytes of length 16
     */
    private byte[] cipher(byte[] input, boolean inverse) {
        if(input.length != NB * NB) {
            throw new IllegalArgumentException("The input must be " + (NB * NB) + " bytes long");
        }
        
        byte[][] state = AESAlgorithms.generateState(input);

        int i, stepSize;
        if (inverse) {
            i = NR;
            stepSize = -1;
        }
        else {
            i = 0;
            stepSize = 1;
        }

        state = AESAlgorithms.addRoundKey(state, roundKeys[i]);
        
        for (i = i + stepSize; i > 0 && i < NR; i += stepSize) {
            state = AESAlgorithms.subBytes(state, inverse);
            state = AESAlgorithms.shiftRows(state, inverse);
            state = AESAlgorithms.mixColumns(state, inverse);
            state = inverse ?
                    AESAlgorithms.addRoundKey(state, AESAlgorithms.inverseMixColumns(roundKeys[i])) :
                    AESAlgorithms.addRoundKey(state, roundKeys[i]);
        }
        
        state = AESAlgorithms.subBytes(state, inverse);
        state = AESAlgorithms.shiftRows(state, inverse);
        state = AESAlgorithms.addRoundKey(state, roundKeys[i]);
        
        return AESAlgorithms.generateOutput(state);
    }
   
}
