package tiw.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SecureUtils {

    public static String getSecurePassword(String password) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");//creazione digest

            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {     //creazione stringa hash
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }



    public static void main(String[] args) throws NoSuchAlgorithmException {

        // A TITOLO ESEMPLIFICATIVO, il codice seguente non verrà utilizzato 

        String password1 = getSecurePassword("PasswordINiNPUT");
        String password2 = getSecurePassword("PasswordINiNPUT");
        System.out.println(" Password 1 -> " + password1);
        System.out.println(" Password 2 -> " + password2);
        if (password1.equals(password2)) {
            System.out.println("passwords sono equal");
        }
    }
}