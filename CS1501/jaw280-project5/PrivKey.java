import java.io.Serializable;

public class PrivKey implements Serializable {
    public LargeInteger n;
    public LargeInteger d;

    public PrivKey(LargeInteger n, LargeInteger d) {
        this.n = n;
        this.d = d;
    }
}
