import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.*;
import java.util.ArrayList;

import static com.google.common.base.Charsets.UTF_8;

public class MyBloomFilter {

    public static ArrayList<String> getList(String filename) throws IOException {

        ArrayList<String> words = new ArrayList<String>();

        File wordFile = new File(filename);

        BufferedReader br = new BufferedReader(new FileReader(wordFile));

        String st;
        while ((st = br.readLine()) != null)
            words.add(st);

        return words;
    }

    public static void main( String [] args) throws IOException {

        ArrayList<String> positiveWords = getList("files/positive_words.txt");//getList(args[0]);
        ArrayList<String> negativeWords = getList("files/negative_words.txt");//getList(args[1]);

        float fpr = 0.0001f; //Float.parseFloat(args[2]);

        BloomFilter<String> positiveWordsFilter = BloomFilter.create(Funnels.stringFunnel(UTF_8), positiveWords.size(), fpr);

        for (String word : positiveWords)
            positiveWordsFilter.put(word);

        BloomFilter<String> negativeWordsFilter = BloomFilter.create(Funnels.stringFunnel(UTF_8), positiveWords.size(), fpr);

        for (String word : negativeWords)
            negativeWordsFilter.put(word);


        System.out.println(positiveWordsFilter.mightContain("winner"));
        System.out.println(positiveWordsFilter.mightContain("pramod"));
        OutputStream os = new FileOutputStream("PositiveBloomFilter.txt");
        positiveWordsFilter.writeTo(os);

        InputStream bf = new FileInputStream("PositiveBloomFilter.txt");
        BloomFilter loadBloomFilter = BloomFilter.readFrom(bf,Funnels.stringFunnel(UTF_8));

        System.out.println(loadBloomFilter.mightContain("winner"));
        System.out.println(loadBloomFilter.mightContain("Pramod"));


        System.out.println(negativeWordsFilter.mightContain("absurd"));
        System.out.println(negativeWordsFilter.mightContain("pramod"));
        OutputStream os1 = new FileOutputStream("NegativeBloomFilter.txt");
        negativeWordsFilter.writeTo(os1);

        InputStream bf1 = new FileInputStream("NegativeBloomFilter.txt");
        BloomFilter loadBloomFilter1 = BloomFilter.readFrom(bf1,Funnels.stringFunnel(UTF_8));

        System.out.println(loadBloomFilter1.mightContain("absurd"));
        System.out.println(loadBloomFilter1.mightContain("Pramod"));


    }
}
