package ca.uvic.lscholte.aes;

/**
 * A class for doing arithmetic in a Galois Field
 * @author lscholte
 */
public class GaloisField {
    
    /**
     * Algorithm detailed at http://www.samiam.org/galois.html
     * @param a The first byte
     * @param b The second byte
     * @return product of bytes a and b
     */
    static byte multiply(byte a, byte b) {
        byte product = 0;
        for (int i = 0; i < 8; ++i) {
           if ((b & 0x01) != 0) { //Check if the low bit of byte b is set to 1
              product ^= a;
           }
           boolean highBitA = (a & 0x80) != 0; //Check if the high bit of byte a is set to 1 
           a <<= 1;
           if (highBitA) {
              a ^= 0x1b;
           }
           b >>>= 1;
        }
        return product;
    }
    
    /**
     * Adds several bytes. Addition is simply an XOR operation
     * @param bytes The bytes to add together
     * @return The sum of all the bytes
     */
    static byte add(byte... bytes) {
        byte result = 0;
        for (byte b : bytes) {
            result ^= b;
        }
        return result;
    }
}
