package ca.uvic.lscholte.aes;

import static ca.uvic.lscholte.aes.AESConstants.INVERSE_S;
import static ca.uvic.lscholte.aes.AESConstants.NB;
import static ca.uvic.lscholte.aes.AESConstants.S;

/**
 * A set of methods required by the AES-256 cipher
 * @author lscholte
 */
public final class AESAlgorithms {
    
    private AESAlgorithms() { }
        
    /**
     * Converts an input array of bytes into a 2D state array that is easier to work with
     * @param input An array of bytes of length NB*NB
     * @return The corresponding state which is a 2D array of bytes 
     */
    static byte[][] generateState(byte[] input) {
        if (input.length != NB * NB) {
            throw new IllegalArgumentException("Input must be exactly " + (NB * NB) + " bytes long");
        }
        
        byte[][] formattedInput = new byte[NB][NB];
                
        for (int i = 0; i < NB; ++i) {
            for (int j = 0; j < NB; ++j) {
                formattedInput[j][i] = input[i*NB + j];
            }
        }
        return formattedInput;
    }
    
    /**
     * Each byte in the state array is replaced by a corresponding byte in
     * the substitution box (or inverse substitution box)
     * @param state The state array
     * @param inverse True if the inverse substitution box should be used
     * @return The state array after performing this transformation
     */
    static byte[][] subBytes(byte[][] state, boolean inverse) {
        byte[][] newState = new byte[NB][NB];
        int[][] sBox = inverse ? INVERSE_S : S;
        
        for (int i = 0; i < NB; ++i) {
            for (int j = 0; j < NB; ++j) {
                byte s = state[i][j];
                int x = ((int) s & 0xFF) >>> 4;
                int y = s & 0x0F;
                newState[i][j] = (byte) sBox[x][y];
            }
        }
        return newState;
    }
    
    /**
     * Each row in the state array is shifted by a specific amount depending on
     * the row
     * @param state The state array
     * @param inverse True if the transformation should be inversed, meaning that
     * rows should be shifted right. False if the rows should be shifted left
     * @return The state array after performing this transformation
     */
    static byte[][] shiftRows(byte[][] state, boolean inverse) {
        byte[][] newState = new byte[NB][NB];
        
        for (int i = 0; i < NB; ++i) {
            for (int j = 0; j < NB; ++j) {                
                int shiftedJ = inverse ?
                        (j + NB - i) % NB :
                        (j + NB + i) % NB;
                newState[i][j] = state[i][shiftedJ];
            }
        }
        
        return newState;
    }
    
    /**
     * Performs a matrix multiplication between the state and a particular
     * matrix of Galois Field elements
     * @param state The state array
     * @param inverse True if the transformation should be inversed
     * @return The state array after performing this transformation
     */
    static byte[][] mixColumns(byte[][] state, boolean inverse) {
        return inverse ? inverseMixColumns(state) : mixColumns(state);
    }
    
    /**
     * Performs a matrix multiplication between the state and a particular
     * matrix of Galois Field elements
     * @param state The state array
     * @return The state array after performing this transformation
     */
    static byte[][] mixColumns(byte[][] state) {
        byte[][] newState = new byte[NB][NB];
        
        for (int j = 0; j < NB; ++j) {
            
            newState[0][j] = GaloisField.add(
                    GaloisField.multiply(state[0][j], (byte) 2),
                    GaloisField.multiply(state[1][j], (byte) 3),
                    state[2][j],
                    state[3][j]
            );
            
            newState[1][j] = GaloisField.add(
                    state[0][j],
                    GaloisField.multiply(state[1][j], (byte) 2),
                    GaloisField.multiply(state[2][j], (byte) 3),
                    state[3][j]
            );
            
            newState[2][j] = GaloisField.add(
                    state[0][j],
                    state[1][j],
                    GaloisField.multiply(state[2][j], (byte) 2),
                    GaloisField.multiply(state[3][j], (byte) 3)
            );
            
            newState[3][j] = GaloisField.add(
                    GaloisField.multiply(state[0][j], (byte) 3),
                    state[1][j],
                    state[2][j],
                    GaloisField.multiply(state[3][j], (byte) 2)
            );            
        }
        return newState;
    }
    
    /**
     * Performs a matrix multiplication between the state and a particular
     * matrix of Galois Field elements
     * @param state The state array
     * @return The state array after performing this transformation
     */
    static byte[][] inverseMixColumns(byte[][] state) {
        byte[][] newState = new byte[NB][NB];
        
        for (int j = 0; j < NB; ++j) {
            
            newState[0][j] = GaloisField.add(
                    GaloisField.multiply(state[0][j], (byte) 14),
                    GaloisField.multiply(state[1][j], (byte) 11),
                    GaloisField.multiply(state[2][j], (byte) 13),
                    GaloisField.multiply(state[3][j], (byte) 9)
            );
            
            newState[1][j] = GaloisField.add(
                    GaloisField.multiply(state[0][j], (byte) 9),
                    GaloisField.multiply(state[1][j], (byte) 14),
                    GaloisField.multiply(state[2][j], (byte) 11),
                    GaloisField.multiply(state[3][j], (byte) 13)
            );
            
            newState[2][j] = GaloisField.add(
                    GaloisField.multiply(state[0][j], (byte) 13),
                    GaloisField.multiply(state[1][j], (byte) 9),
                    GaloisField.multiply(state[2][j], (byte) 14),
                    GaloisField.multiply(state[3][j], (byte) 11)
            );
            
            newState[3][j] = GaloisField.add(
                    GaloisField.multiply(state[0][j], (byte) 11),
                    GaloisField.multiply(state[1][j], (byte) 13),
                    GaloisField.multiply(state[2][j], (byte) 9),
                    GaloisField.multiply(state[3][j], (byte) 14)
            );            
        }
        return newState;
    }
    
    /**
     * XORs each byte of the state with the corresponding byte from the round key
     * @param state The state array
     * @param roundKey The round key
     * @return The state array after performing this transformation
     */
    static byte[][] addRoundKey(byte[][] state, byte[][] roundKey) {
        byte[][] newState = new byte[NB][NB];

        for (int i = 0; i < NB; ++i) {
            for (int j = 0; j < NB; ++j) {
                newState[i][j] = (byte) (state[i][j] ^ roundKey[i][j]);
            }
        }
        
        return newState;
    }
      
    /**
     * Converts a state array of bytes back into a 1D array of bytes
     * @param input The state array
     * @return The corresponding 1D array of bytes
     */
    static byte[] generateOutput(byte[][] state) {
        byte[] output = new byte[NB * NB];
        
        for (int i = 0; i < NB; ++i) {
            for (int j = 0; j < NB; ++j) {
                output[i*NB + j] = state[j][i];
            }
        }
        
        return output;
    }
}
