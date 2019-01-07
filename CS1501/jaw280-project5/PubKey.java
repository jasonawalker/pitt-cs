import java.io.Serializable;

public class PubKey implements Serializable {

    public LargeInteger n;
    public LargeInteger e;

    public PubKey(LargeInteger n, LargeInteger e){
        this.n = n;
        this.e = e;
    }
}
