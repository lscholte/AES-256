package ca.uvic.lscholte.aes;

import javax.xml.bind.DatatypeConverter;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the KeyExpansion class
 * 
 * @author lscholte
 */
public class KeyExpansionTest {

    /**
     * Test of formatKey method, of class KeyExpansion.
     */
    @Test
    public void testFormatKey() {
        
        byte[] key = new byte[] {
            0, 1, 2, 3, 4, 5, 6, 7,
            8, 9, 10, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23,
            24, 25, 26, 27, 28, 29, 30, 31
        };
        
        byte[][] expResult = new byte[][] {
            { 0, 1, 2, 3 },
            { 4, 5, 6, 7 },
            { 8, 9, 10, 11 },
            { 12, 13, 14, 15 },
            { 16, 17, 18, 19 },
            { 20, 21, 22, 23 },
            { 24, 25, 26, 27 },
            { 28, 29, 30, 31 }
        };
        
        byte[][] result = KeyExpansion.formatKey(key);
        
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of generateRoundKeys method, of class KeyExpansion.
     *
     * Test data taken from http://csrc.nist.gov/publications/fips/fips197/fips-197.pdf
     */
    @Test
    public void testGenerateRoundKeys() {        
        byte[] key = DatatypeConverter.
                parseHexBinary("603deb1015ca71be2b73aef0857d77811f352c073b6108d72d9810a30914dff4");
        
        byte[][][] expResult = new byte[][][] {
            new byte[][] {
                DatatypeConverter.parseHexBinary("603deb10"),
                DatatypeConverter.parseHexBinary("15ca71be"),
                DatatypeConverter.parseHexBinary("2b73aef0"),
                DatatypeConverter.parseHexBinary("857d7781"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("1f352c07"),
                DatatypeConverter.parseHexBinary("3b6108d7"),
                DatatypeConverter.parseHexBinary("2d9810a3"),
                DatatypeConverter.parseHexBinary("0914dff4"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("9ba35411"),
                DatatypeConverter.parseHexBinary("8e6925af"),
                DatatypeConverter.parseHexBinary("a51a8b5f"),
                DatatypeConverter.parseHexBinary("2067fcde"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("a8b09c1a"),
                DatatypeConverter.parseHexBinary("93d194cd"),
                DatatypeConverter.parseHexBinary("be49846e"),
                DatatypeConverter.parseHexBinary("b75d5b9a"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("d59aecb8"),
                DatatypeConverter.parseHexBinary("5bf3c917"),
                DatatypeConverter.parseHexBinary("fee94248"),
                DatatypeConverter.parseHexBinary("de8ebe96"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("b5a9328a"),
                DatatypeConverter.parseHexBinary("2678a647"),
                DatatypeConverter.parseHexBinary("98312229"),
                DatatypeConverter.parseHexBinary("2f6c79b3"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("812c81ad"),
                DatatypeConverter.parseHexBinary("dadf48ba"),
                DatatypeConverter.parseHexBinary("24360af2"),
                DatatypeConverter.parseHexBinary("fab8b464"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("98c5bfc9"),
                DatatypeConverter.parseHexBinary("bebd198e"),
                DatatypeConverter.parseHexBinary("268c3ba7"),
                DatatypeConverter.parseHexBinary("09e04214"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("68007bac"),
                DatatypeConverter.parseHexBinary("b2df3316"),
                DatatypeConverter.parseHexBinary("96e939e4"),
                DatatypeConverter.parseHexBinary("6c518d80"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("c814e204"),
                DatatypeConverter.parseHexBinary("76a9fb8a"),
                DatatypeConverter.parseHexBinary("5025c02d"),
                DatatypeConverter.parseHexBinary("59c58239"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("de136967"),
                DatatypeConverter.parseHexBinary("6ccc5a71"),
                DatatypeConverter.parseHexBinary("fa256395"),
                DatatypeConverter.parseHexBinary("9674ee15"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("5886ca5d"),
                DatatypeConverter.parseHexBinary("2e2f31d7"),
                DatatypeConverter.parseHexBinary("7e0af1fa"),
                DatatypeConverter.parseHexBinary("27cf73c3"),

            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("749c47ab"),
                DatatypeConverter.parseHexBinary("18501dda"),
                DatatypeConverter.parseHexBinary("e2757e4f"),
                DatatypeConverter.parseHexBinary("7401905a"),
            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("cafaaae3"),
                DatatypeConverter.parseHexBinary("e4d59b34"),
                DatatypeConverter.parseHexBinary("9adf6ace"),
                DatatypeConverter.parseHexBinary("bd10190d"),
            },
            new byte[][] {
                DatatypeConverter.parseHexBinary("fe4890d1"),
                DatatypeConverter.parseHexBinary("e6188d0b"),
                DatatypeConverter.parseHexBinary("046df344"),
                DatatypeConverter.parseHexBinary("706c631e"),
            }
            
        };
                
        byte[][][] result = KeyExpansion.generateRoundKeys(KeyExpansion.formatKey(key));
        
        assertArrayEquals(expResult, result);
    }
    
}
