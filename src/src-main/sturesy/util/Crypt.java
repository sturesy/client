/*
 * StuReSy - Student Response System
 * Copyright (C) 2012-2014  StuReSy-Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sturesy.util;

import java.util.Formatter;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import sturesy.core.Log;

/**
 * Basic Encrypting and decrypting class using Triple-DES
 * 
 * @author w.posdorfer
 */
public class Crypt
{
    private static String _cryptKey = "VGhpc0lzU2VyaW91c1NoaXQhISExISEx";

    private static Cipher _encryptCipher;
    private static Cipher _decryptCipher;

    private static final String HMAC_SHA1 = "HmacSHA256";

    private static final String TRANSFORMATION = "DESede";
    static
    {
        try
        {
            SecretKey key = getSecretKey();
            _encryptCipher = Cipher.getInstance(TRANSFORMATION);
            _decryptCipher = Cipher.getInstance(TRANSFORMATION);
            _encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            _decryptCipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch (Exception e)
        {
            Log.error("error starting cyphers", e);
        }
    }

    public static String encrypt(String string)
    {
        try
        {
            byte[] utf8 = string.getBytes("UTF8");
            byte[] enc = _encryptCipher.doFinal(utf8);
            return encodeBase64(enc);
        }
        catch (Exception e)
        {
            return string;
        }
    }

    public static String decrypt(String string)
    {
        byte[] dec = decodeBase64(string);

        try
        {
            byte[] utf8 = _decryptCipher.doFinal(dec);
            return new String(utf8, "UTF8");
        }
        catch (Exception e)
        {
            Log.error("error decrypting", e);
        }
        return null;
    }

    private static String encodeBase64(byte[] data)
    {
        return Base64.encodeBase64String(data);
    }

    private static byte[] decodeBase64(String data)
    {
        return Base64.decodeBase64(data);
    }

    private static SecretKey getSecretKey() throws Exception
    {
        byte[] bytes = decodeBase64(_cryptKey);
        return new SecretKeySpec(bytes, TRANSFORMATION);
    }

    public static String toHex(byte[] bytes)
    {
        Formatter formatter = new Formatter();
        for (byte b : bytes)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String hmac_sha256(String data, String key)
    {
        String result = "";
        try
        {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1);

            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = toHex(rawHmac);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
