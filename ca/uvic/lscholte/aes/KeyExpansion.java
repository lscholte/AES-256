package ca.uvic.lscholte.aes;

import static ca.uvic.lscholte.aes.AESConstants.NB;
import static ca.uvic.lscholte.aes.AESConstants.NK;
import static ca.uvic.lscholte.aes.AESConstants.NR;
import static ca.uvic.lscholte.aes.AESConstants.S;
import java.util.Arrays;

/**
 * A set of methods relating to key expansion that are needed for the AES-256 cipher
 * @author lscholte
 */
public final class KeyExpansion {
    
    private KeyExpansion() { }
     
    static byte[][] formatKey(byte[] key) {
        if (key.length != NK * NB) {
            throw new IllegalArgumentException("Key must be exactly " + (NK * NB) + " bytes long");
        }
        
        byte[][] formattedKey = new byte[NK][NB];
        
        for (int i = 0; i < NK; ++i) {
            for (int j = 0; j < NB; ++j) {
                formattedKey[i][j] = key[i*NB + j];
            }
        }
        return formattedKey;
    }
    
    /**
     * Generates an array of round keys where a single round key is a 2D array
     * of bytes
     * @param key The starting key to use when generating the round keys
     * @return An array of round keys
     */
    static final byte[][][] generateRoundKeys(byte[][] key) {
        final int numWords = NB * (NR + 1);
        
        byte[][] expandedKey = new byte[numWords][4];
        
        for (int i = 0; i < NK; ++i) {
            expandedKey[i] = key[i];
        }

        for (int i = NK; i < numWords; ++i) {
            byte[] temp = expandedKey[i-1];
            if (i % NK == 0) {
                temp = sub(rotate(temp));
                temp[0] ^= rcon(i/NK);
            }
            else if (i % NK == 4) {
                temp = sub(temp);
            }
            for (int j = 0; j < NB; ++j) {
                expandedKey[i][j] = (byte) (expandedKey[i-NK][j] ^ temp[j]);
            }
        }
        
        byte[][][] result = new byte[NR+1][NB][NB];
        
        for (int i = 0; i < NR+1; ++i) {
            result[i] = transpose(Arrays.copyOfRange(expandedKey, i * NB, (i+1) * NB));
        }
        
        return result;
    }
    
    private static byte[] rotate(byte[] word) {
        byte[] rotatedWord = new byte[word.length];
        
        for (int i = 0; i < word.length; ++i) {
            int shiftedI = (i + word.length + 1) % word.length;
            rotatedWord[i] = word[shiftedI];
        }
        return rotatedWord;
    }
    
    private static byte[] sub(byte[] word) {
        byte[] subbedWord = new byte[word.length];
        
        for (int i = 0; i < word.length; ++i) {
            
            //Java seems to have an annoying feature where shifting operates on
            //ints instead of bytes, so byte operands are automatically cast to
            //ints. And since bytes are signed, the casting operation will keep
            //the sign. For example, casting 0x80 to an int will result in
            //0xFFFFFF80. I don't want the sign to be copied over, so I need
            //to mask the int value such that the first 24 bits are 0
            
            byte b = word[i];
            int x = ((int) b & 0xFF) >>> 4;
            int y = b & 0x0F;
            subbedWord[i] = (byte) S[x][y];
        }
        return subbedWord;
    }
    
    private static byte rcon(int in) {
        if(in == 0) {
            return 0;
        }        
        
        byte c = 1;
        for (int i = 0; i < in - 1; ++i) {
            c = GaloisField.multiply(c, (byte) 2);
        }
        
        return c;
    }
    
    /**
     * This is purely intended to transform a 2D array so that the words fit
     * into the columns instead of the rows
     * @param words Must be an NBxNB 2D array
     * @return The transpose of the 2D array
     */
    private static byte[][] transpose(byte[][] words) {
        byte[][] transpose = new byte[NB][NB];
        for (int i = 0; i < NB; ++i) {
            for (int j = 0; j < NB; ++j) {
                transpose[j][i] = words[i][j];
            }
        }
        return transpose;
    }
}
