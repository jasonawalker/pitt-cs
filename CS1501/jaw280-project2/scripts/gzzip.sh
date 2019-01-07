javac MyLZW.java
gzip -c example_files/all.tar > compressed/all.gz
echo all done
gzip -c example_files/assig2.doc > compressed/assig2.gz
echo assig2 done
gzip -c example_files/bmps.tar > compressed/bmps.gz
echo bmps done
gzip -c example_files/code.txt > compressed/code.gz
echo code done
gzip -c example_files/code2.txt > compressed/code2.gz
echo code2 done
gzip -c example_files/edit.exe > compressed/edit.gz
echo edit done
gzip -c example_files/frosty.jpg > compressed/frosty.gz
echo frosty done
gzip -c example_files/gone_fishing.bmp > compressed/gone_fishing.gz
echo gone_fishing done
gzip -c example_files/large.txt > compressed/large.gz
echo large done
gzip -c example_files/Lego-big.gif > compressed/Lego-big.gz
echo Lego-big done
gzip -c example_files/medium.txt > compressed/medium.gz
echo medium done
gzip -c example_files/texts.tar > compressed/texts.gz
echo texts done
gzip -c example_files/wacky.bmp > compressed/wacky.gz
echo wacky done
gzip -c example_files/winnt256.bmp > compressed/winnt256.gz
echo winnt256 done
echo FINISHED