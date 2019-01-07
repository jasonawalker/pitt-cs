import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class RsaSign {

    public static void sign(String file) throws Exception{
        Path path = Paths.get(file);
        byte[] data = Files.readAllBytes(path);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data);
        byte[] digest = md.digest();
        LargeInteger hash = new LargeInteger(digest);
//        System.out.println("Old Hash: "+ hash);


        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("privkey.rsa"));
            PrivKey pk = (PrivKey) in.readObject();
            LargeInteger n = pk.n;
            LargeInteger d = pk.d;
            LargeInteger sig = hash.modularExp(d,n);
//            System.out.println("Sig: "+ sig);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file + ".sig"));
            out.writeObject(sig);
        } catch (FileNotFoundException e) {
            throw new Exception("privkey not found");
        }

    }

    public static void verify(String file) throws Exception{
        Path path = Paths.get(file);
        byte[] data = Files.readAllBytes(path);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data);
        byte[] digest = md.digest();
        LargeInteger hash = new LargeInteger(digest);
//        System.out.println("OldHash again: " + hash);

        ObjectInputStream inHash = new ObjectInputStream(new FileInputStream(file + ".sig"));
        ObjectInputStream inPub = new ObjectInputStream(new FileInputStream("pubkey.rsa"));

        LargeInteger sig = (LargeInteger)inHash.readObject();
        PubKey pub = (PubKey)inPub.readObject();
        LargeInteger newHash = sig.modularExp(pub.e,pub.n);

//        System.out.println("Read Sig: "+ sig);

//        System.out.println("Decrypted hash: "+ newHash);


        LargeInteger diff = hash.subtract(newHash);
//        System.out.println(diff);
        if(diff.isZero()){
            System.out.println("Signature is valid.");
        } else {
            System.out.println("Signature is invalid.");
        }
    }

    public static void main(String args[]) throws Exception{
        if(args[0].equals("s")){
            sign(args[1]);
        } else if (args[0].equals("v")){
            verify(args[1]);
        } else {
            System.out.println("Invalid arguments");
        }
    }
}
