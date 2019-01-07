/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int R = 256;
    private static int width = 9;
    private static int length = 512;

    public static TST<Integer> createTST(int r){
        TST<Integer> temp = new TST<Integer>();
        for (int i = 0; i < R; i++){
            temp.put("" + (char) i, i);
        }
        return temp;
    }

    public static int resetCodebookValues(){
        width = 9;
        length = 512;
        return R+1;
    }

    public static void compress(int fillType) {
        String input = BinaryStdIn.readString();
        TST<Integer> st = createTST(R);

        int code = R+1;
        double uncompressedData = 0;
        double compressedData = 0;

        double startRatio = -1;

        BinaryStdOut.write(fillType, 2);

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);
            BinaryStdOut.write(st.get(s), width);

            uncompressedData += (8*s.length());
            compressedData += width;

            int t = s.length();
            if (t < input.length() && code < length){
                st.put(input.substring(0, t + 1), code++);
            } else if(t < input.length()) {
               if(width < 16) {
                   width++;
                   length *= 2;
                   st.put(input.substring(0, t + 1), code++);
               } else if(fillType == 2){
                        code = resetCodebookValues();
                        st = createTST(R);
                        st.put(input.substring(0, t + 1), code++);
               } else if (fillType == 3){
                   if(startRatio == -1){
                        startRatio = uncompressedData/compressedData;
                    }
                    double newRatio = uncompressedData/compressedData;

                    if((startRatio/newRatio) > 1.1){
                        code = resetCodebookValues();
                        st = createTST(R);
                        startRatio = -1;
                        st.put(input.substring(0, t + 1), code++);
                    }
                }
            }
            input = input.substring(t);
        }
        BinaryStdOut.write(R, width);
        BinaryStdOut.close();
    } 

    public static String[] createST(int R){
        String[] temp = new String[65536];
        int i;
        for (i = 0; i < R; i++)
            temp[i] = "" + (char) i;
        temp[i++] = "";

        return temp;
    }

    public static void expand() {
        String[] st = createST(R);
        int i = R+1;

        double compressedData = 0;
        double uncompressedData = 0;
        double startRatio = -1;

        int fillType = (int)(BinaryStdIn.readChar(2));

        int codeword = BinaryStdIn.readInt(width);
        if (codeword == R) return;
        String val = st[codeword];

        while (true) {
            compressedData += width;
            uncompressedData += (8 * val.length());
            BinaryStdOut.write(val);

            if (i >= length){
                if(width < 16) {
                    width++;
                    length *= 2;
                }
                else if (fillType == 2){
                    i = resetCodebookValues();
                    st = createST(R);
                } else if (fillType == 3){
                    if(startRatio == -1){
                        startRatio = uncompressedData/compressedData;
                    }
                    double newRatio = uncompressedData/compressedData;

                    if(startRatio/newRatio > 1.1){
                        i = resetCodebookValues();
                        startRatio = -1;
                        st = createST(R);
                    }
                }
            }
            codeword = BinaryStdIn.readInt(width);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);
            if (i < length) {
                st[i++] = val + s.charAt(0);
            }
            val = s;


        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if(args[0].equals("-")) {
            if(args.length == 2){
                char fillType = args[1].charAt(0);
                switch (fillType){
                    case 'n':
                        compress(1);
                        break;
                    case 'r':
                        compress(2);
                        break;
                    case 'm':
                        compress(3);
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal command line argument");
                }
            } else {
                throw new IllegalArgumentException("Illegal command line argument");
            }
        }
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
