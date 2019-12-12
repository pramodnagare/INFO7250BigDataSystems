package Project;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.*;
import java.util.ArrayList;

public class MyBloomFilter {

    public static void main(String[] args) throws IOException {

        ArrayList<String> stopWords = new ArrayList<String>();

        File wordFile = new File("files/stop_words.txt");

        BufferedReader br = new BufferedReader(new FileReader(wordFile));

        String st;
        while ((st = br.readLine()) != null)
            stopWords.add(st);

        BloomFilter<Integer> bloomFilter = BloomFilter.create(
                Funnels.integerFunnel(),
                stopWords.size(),
                0.0001);

        for (String word : stopWords)
            bloomFilter.put(word.hashCode());

        //Testing:

        System.out.println(bloomFilter.mightContain("do".hashCode()));
        OutputStream os = new FileOutputStream("JavaBloomFilter.txt");
        bloomFilter.writeTo(os);

        InputStream bf = new FileInputStream("JavaBloomFilter.txt");
        BloomFilter loadBloomFilter = BloomFilter.readFrom(bf,Funnels.integerFunnel());

        System.out.println(loadBloomFilter.mightContain("do".hashCode()));
        System.out.println(loadBloomFilter.mightContain("Pramod".hashCode()));
}

}

