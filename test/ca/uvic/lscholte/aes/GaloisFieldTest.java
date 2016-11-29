package ca.uvic.lscholte.aes;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the GaloisField class
 * 
 * @author lscholte
 */
public class GaloisFieldTest {

    /**
     * Test of multiply method, of class GaloisField.
     */
    @Test
    public void testMultiply() {
        byte a, b, expResult, result;
        
        a = 27;
        b = 2;
        expResult = 0x36;
        result = GaloisField.multiply(a, b);
        assertEquals(expResult, result);
        
        a = 3;
        b = 100;
        expResult = (byte) 0xac;
        result = GaloisField.multiply(a, b);
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class GaloisField.
     */
    @Test
    public void testAdd() {
        byte a = 5, b = 3, c = 12, d = 127;
        byte expResult = (byte) (a ^ b ^ c ^ d);
        
        byte result = GaloisField.add(a, b, c, d);
        assertEquals(expResult, result);
    }
    
}
