package com.smartdevapp.smartnote;

import android.annotation.SuppressLint;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class EncryptsDecrypt {




    String Plain_email;
    String Plain_password;


    String Encrypted_email;
    String Encrypted_password;
    byte[] original_email;
    byte[] original_password;






    public void Lock()  {


        try {
            String CIPHER_TYPE = "AES";
            KeyGenerator generator = KeyGenerator.getInstance(CIPHER_TYPE);
            SecretKey secretKey = generator.generateKey();

            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE,secretKey);
            byte[] encryptionText1 = cipher.doFinal(original_email);
            byte[] encryptionText2 = cipher.doFinal(original_password);

            cipher.init(Cipher.DECRYPT_MODE,secretKey);
            byte [] decryptionText1 = cipher.doFinal(encryptionText1);
            byte [] decryptionText2 = cipher.doFinal(encryptionText2);
            Plain_email = new String(encryptionText1);
            Plain_password = new String(encryptionText2);
            Encrypted_email = new String(decryptionText1);
            Encrypted_password = new String(decryptionText2);

        }  catch (NoSuchAlgorithmException | InvalidKeyException
                | NoSuchPaddingException | BadPaddingException |
                IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }
    
    public String emailEncryption(String s){


        return s.replace("1","═╬").replace("2","▬").replace("3","╫╪").
                replace("4","╬═").replace("5","╨╤").replace("6","▀▐").
                replace("7","╗╝").replace("8","◘").replace("9","╟╚").
                replace("0","◙").
                replace("a","↕").replace("b","‼").replace("c","¶").
                replace("d","§").replace("e","■ⁿ").replace("f","↨").
                replace("g","↑").replace("h","⌠").replace("j","→").
                replace("k","←").replace("@gmail.com","▓╨╢").replace("@GMAIL.COM","▓╨").
                replace("l","∟").replace("m","↔").replace("n","▲").
                replace("o","▼").replace("p","½").replace("q","»").
                replace("r","░").replace("s","▒").replace("t","▓").
                replace("u","│").replace("v","┤").replace("w","╡").
                replace("x","╢").replace("y","╖").replace("z","╕");
    }

    public String emailDecryption(String s){


       return s.replace("═╬","1").replace("▬","2").replace("╫╪","3").
                replace("╬═","4").replace("╨╤","5").replace("▀▐","6").
                replace("╗╝","7").replace("◘","8").replace("╟╚","9").
                replace("◙","0").
                replace("↕","a").replace("‼","b").replace("¶","c").
                replace("§","d").replace("■ⁿ","e").replace("↨","f").
                replace("↑","g").replace("⌠","h").replace("→","j").
                replace("←","k").replace("▓╨╢","@gmail.com").replace("▓╨","@GMAIL.COM").
                replace("∟","l").replace("↔","m").replace("▲","n").
                replace("▼","o").replace("½","p").replace("»","q").
                replace("░","r").replace("▒","s").replace("▓","t").
                replace("│","u").replace("┤","v").replace("╡","w").
                replace("╢","x").replace("╖","y").replace("╕","z");
    }

    public String passwordEncryption(String s){


        return s.replace("1","!↕").replace("2","▬‼").replace("3","╩¶").
                replace("4","╣§").replace("5","╔▬").replace("6","╦↨").
                replace("7","•↑").replace("8","◘↓").replace("9","○→").
                replace("0","◙←");
    }
    
    public String passwordDecryption(String s){


       return s.replace("!↕","1").replace("▬‼","2").replace("╩¶","3").
                replace("╣§","4").replace("╔▬","5").replace("╦↨","6").
                replace("•↑","7").replace("◘↓","8").replace("○→","9").
                replace("◙←","0");
    }
}
