/*
 * This class is used to make sha1 hash with the message passed by parameter
 * */
package rssdroid.proyecto.Model;

    import java.security.MessageDigest;
    import java.security.NoSuchAlgorithmException;


public class Encrypter {

    private MessageDigest md;
    private byte[] buffer, digest;
    private String hash;
    
    public Encrypter(){}

    public String getHash(String message){
        hash = new String();
        buffer = message.getBytes();
        try{
            md = MessageDigest.getInstance("SHA1");
            md.update(buffer);
            digest = md.digest();

            for(byte aux : digest) {
                int b = aux & 0xff;
                if (Integer.toHexString(b).length() == 1) hash += "0";
                    hash += Integer.toHexString(b);
                }
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return hash;
    } 
}
