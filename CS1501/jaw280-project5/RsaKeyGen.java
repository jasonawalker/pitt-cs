import java.util.Random;
import java.io.*;


public class RsaKeyGen {

    public static void main(String[] args) throws Exception{
        LargeInteger one = new LargeInteger(new byte[] {(byte)1});

        Random rand = new Random();
        LargeInteger p = new LargeInteger(255, rand);
        LargeInteger q = new LargeInteger(256, rand);
        LargeInteger n = p.multiply(q);
        LargeInteger phiN = p.subtract(one).multiply(q.subtract(one));
        LargeInteger e = new LargeInteger(new byte[] {(byte)1, (byte)0, (byte)1});
        LargeInteger[] gcd = phiN.XGCD(e);

        while(gcd[0].compareTo(one) != 0){
            e = new LargeInteger(phiN.length(), rand);
            gcd = phiN.XGCD(e);
        }

        LargeInteger d = gcd[2].divide(phiN)[1];
        if(d.isNegative()){
            d = d.add(phiN);
        }

        PubKey pub = new PubKey(n,e);
        PrivKey priv = new PrivKey(n,d);


        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("pubkey.rsa"));
        out.writeObject(pub);

        out = new ObjectOutputStream(new FileOutputStream("privkey.rsa"));
        out.writeObject(priv);

        out.close();

    }
}
